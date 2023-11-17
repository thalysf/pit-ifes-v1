package br.com.ifes.backend_pit.services.atividades.resposta;

import br.com.ifes.backend_pit.enums.PeriodoEnum;
import br.com.ifes.backend_pit.enums.TipoAtividadeEnum;
import br.com.ifes.backend_pit.enums.TipoDetalhamentoEnum;
import br.com.ifes.backend_pit.exception.BusinessException;
import br.com.ifes.backend_pit.exception.NotFoundException;
import br.com.ifes.backend_pit.models.atividades.Atividade;
import br.com.ifes.backend_pit.models.atividades.detalhamento.DetalhamentoAluno;
import br.com.ifes.backend_pit.models.atividades.detalhamento.DetalhamentoComponenteCurricular;
import br.com.ifes.backend_pit.models.atividades.detalhamento.DetalhamentoPortaria;
import br.com.ifes.backend_pit.models.atividades.detalhamento.DetalhamentoProjeto;
import br.com.ifes.backend_pit.models.atividades.resposta.RespostaAtividade;
import br.com.ifes.backend_pit.models.cadastros.Aluno;
import br.com.ifes.backend_pit.models.cadastros.ComponenteCurricular;
import br.com.ifes.backend_pit.models.cadastros.Portaria;
import br.com.ifes.backend_pit.models.cadastros.Projeto;
import br.com.ifes.backend_pit.models.pit.PIT;
import br.com.ifes.backend_pit.models.usuarios.Servidor;
import br.com.ifes.backend_pit.repositories.atividade.AtividadeRepository;
import br.com.ifes.backend_pit.repositories.atividade.detalhamento.DetalhamentoAlunoRepository;
import br.com.ifes.backend_pit.repositories.atividade.detalhamento.DetalhamentoComponenteCurricularRepository;
import br.com.ifes.backend_pit.repositories.atividade.detalhamento.DetalhamentoProjetoRepository;
import br.com.ifes.backend_pit.repositories.atividade.resposta.RespostaAtividadeRepository;
import br.com.ifes.backend_pit.repositories.cadastros.AlunoRepository;
import br.com.ifes.backend_pit.repositories.cadastros.ComponenteCurricularRepository;
import br.com.ifes.backend_pit.services.pit.PITService;
import br.com.ifes.backend_pit.services.usuarios.ServidorService;
import br.com.ifes.backend_pit.utils.TimeOperations;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static br.com.ifes.backend_pit.constants.ErrorConstants.ATIVIDADE_ERROS.*;
import static br.com.ifes.backend_pit.constants.ErrorConstants.PIT_ERROS.PIT_PERIODO_INVALIDO;

@Service
@RequiredArgsConstructor
@Transactional
public class RespostaService {

    private final RespostaAtividadeRepository respostaAtividadeRepository;
    private final ComponenteCurricularRepository componenteCurricularRepository;
    private final AtividadeRepository atividadeRepository;
    private final DetalhamentoComponenteCurricularRepository detalhamentoComponenteCurricularRepository;
    private final DetalhamentoAlunoRepository detalhamentoAlunoRepository;
    private final DetalhamentoProjetoRepository detalhamentoProjetoRepository;
    private final AlunoRepository alunoRepository;
    private final ServidorService servidorService;
    private final PITService pitService;

    String obterPeriodoRegex = "^(1|2)\\/";


    public List<DetalhamentoPortaria> getDetalhamentoPortariasResposta(UUID idAtividade, UUID idPit) {
        return respostaAtividadeRepository.findByAtividadeIdAtividadeAndPitIdPIT(idAtividade, idPit)
                .orElseGet(RespostaAtividade::new)
                .getDetalhamentoPortarias();
    }

    public List<DetalhamentoProjeto> getDetalhamentoProjetosResposta(UUID idAtividade, UUID idPit) {
        return respostaAtividadeRepository.findByAtividadeIdAtividadeAndPitIdPIT(idAtividade, idPit)
                .orElseGet(RespostaAtividade::new)
                .getDetalhamentoProjetos();
    }

    public List<ComponenteCurricular> getComponentesNaoDetalhados(UUID idPit, UUID idAtividade) {
        Optional<List<ComponenteCurricular>> componentesCurricularesNaoDetalhados = respostaAtividadeRepository.findComponenteNaoSelecionados(idPit, idAtividade);
        return componentesCurricularesNaoDetalhados.orElseGet(ArrayList::new);
    }

    public List<DetalhamentoComponenteCurricular> getRespostasAtividade(UUID idPit, UUID idAtividade) {
        Optional<RespostaAtividade> respostaAtividadeOptional = respostaAtividadeRepository.findByPitIdPITAndAtividadeIdAtividade(idPit, idAtividade);
        if (respostaAtividadeOptional.isEmpty()) {
            return new ArrayList<>();
        }
        return respostaAtividadeOptional.get().getDetalhamentoComponentesCurriculares();
    }

    public RespostaAtividade getRespostaAtividade(UUID idResposta) {
        return respostaAtividadeRepository.findById(idResposta).orElseThrow(() -> new NotFoundException(ATIVIDADE_NAO_ENCONTRADA));
    }

    public DetalhamentoComponenteCurricular criarRespostaComponenteCurricularAtividade(
            ComponenteCurricular componenteCurricular, PIT pit, Double cargaHorariaSemanal, UUID idAtividade
    ) {

        validarCargaHorariaRespostaComponenteCurricular(cargaHorariaSemanal, componenteCurricular.getIdComponenteCurricular(), idAtividade);

        // validar se o componente curricular já existe no PIT e na atividade atual
        if (respostaAtividadeRepository.findByPitIdAndComponenteCurricularId(pit.getIdPIT(), componenteCurricular.getIdComponenteCurricular(), idAtividade).isPresent())
            throw new BusinessException("Esse Componente Curricular já está cadastrado nessa atividade do PIT atual.");

        RespostaAtividade respostaAtividade = respostaAtividadeRepository
                .findByPitIdPITAndAtividadeIdAtividade(pit.getIdPIT(), idAtividade)
                .orElseGet(RespostaAtividade::new);

        respostaAtividade.addComponenteCurricular(componenteCurricular, cargaHorariaSemanal);
        respostaAtividade.atualizarCargaHorariaSemanalAulas();
        respostaAtividade.setPit(pit);
        respostaAtividade.setAtividade(
                atividadeRepository.findById(idAtividade)
                        .orElseThrow(() -> new NotFoundException(ATIVIDADE_NAO_ENCONTRADA))
        );

        RespostaAtividade respostaAtividadeCriada = respostaAtividadeRepository.save(respostaAtividade);

        return respostaAtividadeCriada.getDetalhamentoComponentesCurriculares()
                .stream()
                .filter(d -> d.getComponenteCurricular().getIdComponenteCurricular().equals(componenteCurricular.getIdComponenteCurricular()))
                .findFirst()
                .get();
    }

    /**
     * Valida se a carga horaria da resposta não é menor que zero ou maior que a carga horaria do componente curricular
     *
     * @param cargaHoraria
     * @param idComponenteCurricular
     * @param idAtividade
     */
    private void validarCargaHorariaRespostaComponenteCurricular(Double cargaHoraria, UUID idComponenteCurricular, UUID idAtividade) {
        if (cargaHoraria == null || cargaHoraria <= 0)
            throw new BusinessException("Carga horária deve ser maior que 0");

        Atividade atividade = getAtividade(idAtividade);

        if (atividade.getCargaHorariaMaxima() != null) {
            // se atividade tiver carga horaria min e max validar por ela
            this.validarCargaHoraria(cargaHoraria, atividade.getCargaHorariaMinima(), atividade.getCargaHorariaMaxima());
        } else {
            //validar carga horaria pelo componente curricular
            ComponenteCurricular componenteCurricular = getComponenteCurricularById(idComponenteCurricular);
            this.validarCargaHoraria(cargaHoraria, 0D, componenteCurricular.getCargaHoraria());
        }
    }

    private Atividade getAtividade(UUID idAtividade) {
        return this.atividadeRepository.findById(idAtividade)
                .orElseThrow(() -> new NotFoundException(ATIVIDADE_NAO_ENCONTRADA));
    }

    private ComponenteCurricular getComponenteCurricularById(UUID idComponente) {
        Optional<ComponenteCurricular> componenteCurricular = componenteCurricularRepository.findById(idComponente);
        if (componenteCurricular.isEmpty()) throw new NotFoundException(COMPONENTE_CURRICULAR_NAO_ENCONTRADO);
        return componenteCurricular.get();
    }

    public DetalhamentoComponenteCurricular atualizarDetalhamentoComponenteCurricular(ComponenteCurricular componenteCurricular, UUID idDetalhamento, Double novaCargaHorariaSemanal) {
        DetalhamentoComponenteCurricular detalhamentoComponenteCurricular = detalhamentoComponenteCurricularRepository
                .findById(idDetalhamento).orElseThrow(() -> new NotFoundException(DETALHAMENTO_COMPONENTE_CURRICULAR_NAO_ENCONTRADO));

        RespostaAtividade respostaAtividade = detalhamentoComponenteCurricular.getRespostaAtividade();

        validarCargaHorariaRespostaComponenteCurricular(novaCargaHorariaSemanal, componenteCurricular.getIdComponenteCurricular(), respostaAtividade.getAtividade().getIdAtividade());


        // chamando essa função para validar o status do pit se está aprovado ou em revisão
        // aprovado ou em revisão não altera
        this.pitService.getPitResponder(respostaAtividade.getPit().getIdPIT());

        detalhamentoComponenteCurricular.setCargaHorariaSemanal(novaCargaHorariaSemanal);

        respostaAtividade.atualizarCargaHorariaSemanalAulas();
        respostaAtividadeRepository.save(respostaAtividade);

        return detalhamentoComponenteCurricularRepository.save(detalhamentoComponenteCurricular);
    }

    public void deletarDetalhamentoComponenteCurricular(UUID idDetalhamento) {
        RespostaAtividade respostaAtividade = detalhamentoComponenteCurricularRepository.findById(idDetalhamento)
                .orElseThrow(() -> new NotFoundException(DETALHAMENTO_COMPONENTE_CURRICULAR_NAO_ENCONTRADO))
                .getRespostaAtividade();

        respostaAtividade.getDetalhamentoComponentesCurriculares().removeIf(d -> d.getIdDetalhamentoComponenteCurricular().equals(idDetalhamento));

        // chamando essa função para validar o status do pit se está aprovado ou em revisão
        // aprovado ou em revisão não altera
        this.pitService.getPitResponder(respostaAtividade.getPit().getIdPIT());

        respostaAtividade.atualizarCargaHorariaSemanalAulas();
        respostaAtividadeRepository.save(respostaAtividade);
    }

    public List<RespostaAtividade> listarRespostaAtividadesApoioEnsino(UUID idPit) {
        return respostaAtividadeRepository.findAllByPitIdPITAndAtividadeTipoAtividade(idPit, TipoAtividadeEnum.ATIVIDADE_APOIO_ENSINO);
    }

    public List<RespostaAtividade> listarRespostaAtividadesPesquisa(UUID idPit) {
        return respostaAtividadeRepository.findAllByPitIdPITAndAtividadeTipoAtividade(idPit, TipoAtividadeEnum.ATIVIDADE_PESQUISA);
    }

    public List<RespostaAtividade> listarRespostaAtividadesExtensao(UUID idPit) {
        return respostaAtividadeRepository.findAllByPitIdPITAndAtividadeTipoAtividade(idPit, TipoAtividadeEnum.ATIVIDADE_EXTENSAO);
    }

    public List<RespostaAtividade> listarRespostaAtividadesOutrasAtividades(UUID idPit) {
        return respostaAtividadeRepository.findAllByPitIdPITAndAtividadeTipoAtividade(idPit, TipoAtividadeEnum.OUTRAS_ATIVIDADES);
    }

    public List<Atividade> listarAtividadesApoioEnsinoProfessorParticipa(PIT pit, UUID idUsuario) {
        Servidor servidor = this.servidorService.getServidorByUserId(idUsuario);

        Pattern pattern = Pattern.compile(obterPeriodoRegex);

        Matcher matcher = pattern.matcher(pit.getPeriodo());

        if (matcher.find()) {
            PeriodoEnum periodoEnum = PeriodoEnum.fromId(matcher.group(1));
            return respostaAtividadeRepository.listarAtividadesProfessor(servidor.getIdServidor(), TipoAtividadeEnum.ATIVIDADE_APOIO_ENSINO, periodoEnum.getPeriodo());
        } else {
            throw new BusinessException(PIT_PERIODO_INVALIDO);
        }
    }

    public List<Atividade> listarAtividadesPesquisaProfessorParticipa(PIT pit, UUID idUsuario) {
        Servidor servidor = this.servidorService.getServidorByUserId(idUsuario);
        Pattern pattern = Pattern.compile(obterPeriodoRegex);

        Matcher matcher = pattern.matcher(pit.getPeriodo());

        if (matcher.find()) {
            PeriodoEnum periodoEnum = PeriodoEnum.fromId(matcher.group(1));
            return respostaAtividadeRepository.listarAtividadesProfessor(servidor.getIdServidor(), TipoAtividadeEnum.ATIVIDADE_PESQUISA, periodoEnum.getPeriodo());
        } else {
            throw new BusinessException(PIT_PERIODO_INVALIDO);
        }
    }

    public List<Atividade> listarAtividadesExtensaoProfessorParticipa(PIT pit, UUID idUsuario) {
        Servidor servidor = this.servidorService.getServidorByUserId(idUsuario);
        Pattern pattern = Pattern.compile(obterPeriodoRegex);

        Matcher matcher = pattern.matcher(pit.getPeriodo());

        if (matcher.find()) {
            PeriodoEnum periodoEnum = PeriodoEnum.fromId(matcher.group(1));
            return respostaAtividadeRepository.listarAtividadesProfessor(servidor.getIdServidor(), TipoAtividadeEnum.ATIVIDADE_EXTENSAO, periodoEnum.getPeriodo());
        } else {
            throw new BusinessException(PIT_PERIODO_INVALIDO);
        }
    }

    public List<Atividade> listarAtividadesOutrasProfessorParticipa(PIT pit, UUID idUsuario) {
        Servidor servidor = this.servidorService.getServidorByUserId(idUsuario);
        Pattern pattern = Pattern.compile(obterPeriodoRegex);

        Matcher matcher = pattern.matcher(pit.getPeriodo());

        if (matcher.find()) {
            PeriodoEnum periodoEnum = PeriodoEnum.fromId(matcher.group(1));
            return respostaAtividadeRepository.listarAtividadesProfessor(servidor.getIdServidor(), TipoAtividadeEnum.OUTRAS_ATIVIDADES, periodoEnum.getPeriodo());
        } else {
            throw new BusinessException(PIT_PERIODO_INVALIDO);
        }
    }

    public RespostaAtividade salvarRespostaAtividade(RespostaAtividade respostaAtividade) {

        PIT pit = this.pitService.getPitResponder(respostaAtividade.getPit().getIdPIT());

        Optional<RespostaAtividade> respostaAtividadeOpt = respostaAtividadeRepository.findByAtividadeIdAtividadeAndPitIdPIT(respostaAtividade.getAtividade().getIdAtividade(), pit.getIdPIT());

        // forçar inserir o id da resposta atividade para evitar de criar mais de uma resposta atividade para a mesma atividade
        respostaAtividadeOpt.ifPresent(atividade -> respostaAtividade.setIdRespostaAtividade(atividade.getIdRespostaAtividade()));

        UUID idAtividade = respostaAtividade.getAtividade().getIdAtividade();

        Atividade atividade = this.atividadeRepository.findById(idAtividade).orElseThrow(() -> new NotFoundException(ATIVIDADE_NAO_ENCONTRADA));

        if (atividade.getTipoDetalhamento().equals(TipoDetalhamentoEnum.NENHUM)) {
            this.validarCargaHoraria(respostaAtividade.getCargaHorariaSemanal(), atividade.getCargaHorariaMinima(), atividade.getCargaHorariaMaxima());
            return this.respostaAtividadeRepository.save(respostaAtividade);
        }
        if (atividade.getTipoDetalhamento().equals(TipoDetalhamentoEnum.DETALHAMENTO_PROJETO)) {

            for (DetalhamentoProjeto detalhamentoProjeto : respostaAtividade.getDetalhamentoProjetos()) {
                detalhamentoProjeto.setRespostaAtividade(respostaAtividade);
                Projeto projeto = atividade.getProjetos().stream().filter(p -> p.getIdProjeto().equals(detalhamentoProjeto.getProjeto().getIdProjeto())).findFirst().get();
                this.validarCargaHoraria(detalhamentoProjeto.getCargaHorariaSemanal(), projeto.getCargaHorariaMinima(), projeto.getCargaHorariaMaxima());
                respostaAtividade.atualizarCargaHorariaSemanalProjetos();
            }

            return this.respostaAtividadeRepository.save(respostaAtividade);
        }

        if (atividade.getTipoDetalhamento().equals(TipoDetalhamentoEnum.DETALHAMENTO_PORTARIA)) {

            for (DetalhamentoPortaria detalhamentoPortaria : respostaAtividade.getDetalhamentoPortarias()) {
                detalhamentoPortaria.setRespostaAtividade(respostaAtividade);
                Portaria portaria = atividade.getPortarias().stream().filter(p -> p.getIdPortaria().equals(detalhamentoPortaria.getPortaria().getIdPortaria())).findFirst().get();
                this.validarCargaHoraria(detalhamentoPortaria.getCargaHorariaSemanal(), portaria.getCargaHorariaMinima(), portaria.getCargaHorariaMaxima());
                respostaAtividade.atualizarCargaHorariaSemanalPortarias();
            }

            return this.respostaAtividadeRepository.save(respostaAtividade);
        }

        if (atividade.getTipoDetalhamento().equals(TipoDetalhamentoEnum.DETALHAMENTO_AULA)) {
            for (DetalhamentoComponenteCurricular detalhamentoComponenteCurricular : respostaAtividade.getDetalhamentoComponentesCurriculares()) {
                detalhamentoComponenteCurricular.setRespostaAtividade(respostaAtividade);
                ComponenteCurricular componenteCurricular = componenteCurricularRepository.findById(detalhamentoComponenteCurricular.getComponenteCurricular().getIdComponenteCurricular())
                        .orElseThrow(() -> new NotFoundException(COMPONENTE_CURRICULAR_NAO_ENCONTRADO));
                this.validarCargaHorariaRespostaComponenteCurricular(detalhamentoComponenteCurricular.getCargaHorariaSemanal(), componenteCurricular.getIdComponenteCurricular(), atividade.getIdAtividade());
                respostaAtividade.atualizarCargaHorariaSemanalAulas();
            }

            return this.respostaAtividadeRepository.save(respostaAtividade);
        }

        throw new BusinessException("Tipo de detalhamento inválido");
    }

    public DetalhamentoAluno salvarDetalhamentoAlunoAtividade(UUID idAtividade, UUID idPit, DetalhamentoAluno detalhamentoAluno) {
        PIT pit = this.pitService.getPitResponder(idPit);
        Atividade atividade = this.atividadeRepository.findById(idAtividade).orElseThrow(() -> new NotFoundException(ATIVIDADE_NAO_ENCONTRADA));

        Optional<RespostaAtividade> respostaAtividadeOpt = respostaAtividadeRepository.findByAtividadeIdAtividadeAndPitIdPIT(idAtividade, pit.getIdPIT());

        // verificar se resposta atividade existe, senao criar uma nova
        RespostaAtividade respostaAtividade = respostaAtividadeOpt.orElseGet(() -> this.criarRespostaAtividade(pit, atividade));

        //validar hora
        this.validarCargaHoraria(detalhamentoAluno.getCargaHorariaSemanal(), atividade.getCargaHorariaMinima(), atividade.getCargaHorariaMaxima());

        detalhamentoAluno.setAluno(this.verificarSeAlunoExisteESalvar(detalhamentoAluno.getAluno()));

        respostaAtividade.getDetalhamentoAlunos().removeIf(
                detalhamento -> detalhamento.getAluno().getMatricula().equals(detalhamentoAluno.getAluno().getMatricula())
        );

        Double totalHoras = 0D;

        for (DetalhamentoAluno detalhamentoAlunoResposta : respostaAtividade.getDetalhamentoAlunos()) {
            totalHoras = TimeOperations.tratarSomatorioCargaHoraria(detalhamentoAlunoResposta.getCargaHorariaSemanal(), totalHoras);
        }
        totalHoras = TimeOperations.tratarSomatorioCargaHoraria(totalHoras, detalhamentoAluno.getCargaHorariaSemanal());


        respostaAtividade.getDetalhamentoAlunos().add(detalhamentoAluno);
        detalhamentoAluno.setRespostaAtividade(respostaAtividade);
        respostaAtividade.setCargaHorariaSemanal(totalHoras);

        this.respostaAtividadeRepository.save(respostaAtividade);

        return detalhamentoAluno;
    }

    private RespostaAtividade criarRespostaAtividade(PIT pit, Atividade atividade) {
        RespostaAtividade respostaAtividade = new RespostaAtividade();
        respostaAtividade.setPit(pit);
        respostaAtividade.setAtividade(atividade);
        return this.respostaAtividadeRepository.save(respostaAtividade);
    }

    public RespostaAtividade excluirDetalhamentoAluno(UUID idDetalhamentoAluno) {
        RespostaAtividade respostaAtividade = this.detalhamentoAlunoRepository.findById(idDetalhamentoAluno)
                .orElseThrow(() -> new BusinessException(ATIVIDADE_NAO_ENCONTRADA))
                .getRespostaAtividade();

        // chamando essa função para validar o status do pit se está aprovado ou em revisão
        // aprovado ou em revisão não altera
        this.pitService.getPitResponder(respostaAtividade.getPit().getIdPIT());

        respostaAtividade.getDetalhamentoAlunos().removeIf(detalhamentoAluno -> detalhamentoAluno.getIdDetalhamentoAluno().equals(idDetalhamentoAluno));

        Double totalHoras = 0D;

        for (DetalhamentoAluno detalhamentoAlunoResposta : respostaAtividade.getDetalhamentoAlunos()) {
            totalHoras = TimeOperations.tratarSomatorioCargaHoraria(detalhamentoAlunoResposta.getCargaHorariaSemanal(), totalHoras);
        }
        respostaAtividade.setCargaHorariaSemanal(totalHoras);

        return this.respostaAtividadeRepository.save(respostaAtividade);
    }

    public List<DetalhamentoAluno> listarDetalhamentoAlunosAtividade(UUID idAtividade, UUID idPit) {
        return this.respostaAtividadeRepository.findByAtividadeIdAtividadeAndPitIdPIT(idAtividade, idPit)
                .orElseThrow(() -> new BusinessException(ATIVIDADE_NAO_ENCONTRADA))
                .getDetalhamentoAlunos();
    }

    public List<DetalhamentoComponenteCurricular> obterRespostasAulas(UUID idAtividade, UUID idPit) {
        return this.respostaAtividadeRepository.findByAtividadeIdAtividadeAndPitIdPIT(idAtividade, idPit)
                .orElseGet(RespostaAtividade::new)
                .getDetalhamentoComponentesCurriculares();
    }

    public RespostaAtividade salvarRespostaOutras(RespostaAtividade respostaAtividade) {
        PIT pit = this.pitService.getPitResponder(respostaAtividade.getPit().getIdPIT());

        Optional<RespostaAtividade> respostaAtividadeOpt = respostaAtividadeRepository.findByAtividadeIdAtividadeAndPitIdPIT(respostaAtividade.getAtividade().getIdAtividade(), pit.getIdPIT());

        // forçar inserir o id da resposta atividade para evitar de criar mais de uma resposta atividade para a mesma atividade
        respostaAtividadeOpt.ifPresent(atividade -> respostaAtividade.setIdRespostaAtividade(atividade.getIdRespostaAtividade()));

        DetalhamentoPortaria detalhamentoPortaria = respostaAtividade.getDetalhamentoPortarias().get(0);
        detalhamentoPortaria.setRespostaAtividade(respostaAtividade);
        respostaAtividade.setDetalhamentoPortarias(List.of(detalhamentoPortaria));

        return respostaAtividadeRepository.save(respostaAtividade);
    }

    private Aluno verificarSeAlunoExisteESalvar(Aluno aluno) {
        Optional<Aluno> alunoOptional = this.alunoRepository.findByMatricula(aluno.getMatricula().trim());
        if (alunoOptional.isPresent()) {
            aluno.setIdAluno(alunoOptional.get().getIdAluno());
        }

        aluno.setMatricula(aluno.getMatricula().trim());
        aluno.setNome(aluno.getNome().trim());
        return this.alunoRepository.save(aluno);
    }

    private void validarCargaHoraria(Double cargaHorariaAtividade, Double cargaMinima, Double cargaMaxima) {
        if (cargaHorariaAtividade < 0) {
            throw new BusinessException("Carga horária deve ser maior que 0");
        } else if (cargaHorariaAtividade < cargaMinima || cargaHorariaAtividade > cargaMaxima) {
            throw new BusinessException("Carga horária deve respeitar a carga mínima e máxima");
        }
    }

    public void excluirDetalhamentoProjeto(UUID idDetalhamentoProjeto) {
        DetalhamentoProjeto detalhamentoProjeto = this.detalhamentoProjetoRepository.findById(idDetalhamentoProjeto)
                .orElseThrow(() -> new NotFoundException("Detalhamento não encontrado"));

        RespostaAtividade respostaAtividade = detalhamentoProjeto.getRespostaAtividade();

        // chamando essa função para validar o status do pit se está aprovado ou em revisão
        // aprovado ou em revisão não altera
        this.pitService.getPitResponder(respostaAtividade.getPit().getIdPIT());

        respostaAtividade.getDetalhamentoProjetos().removeIf(d -> d.getIdDetalhamentoProjeto().equals(idDetalhamentoProjeto));
        respostaAtividade.atualizarCargaHorariaSemanalProjetos();

        this.respostaAtividadeRepository.save(respostaAtividade);
    }
}
