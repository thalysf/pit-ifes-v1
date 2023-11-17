package br.com.ifes.backend_pit.reports.service;

import br.com.ifes.backend_pit.constants.ErrorConstants;
import br.com.ifes.backend_pit.enums.TipoAtividadeEnum;
import br.com.ifes.backend_pit.enums.TipoDetalhamentoEnum;
import br.com.ifes.backend_pit.exception.BusinessException;
import br.com.ifes.backend_pit.exception.NotFoundException;
import br.com.ifes.backend_pit.models.atividades.Atividade;
import br.com.ifes.backend_pit.models.atividades.detalhamento.DetalhamentoAluno;
import br.com.ifes.backend_pit.models.atividades.detalhamento.DetalhamentoComponenteCurricular;
import br.com.ifes.backend_pit.models.atividades.detalhamento.DetalhamentoPortaria;
import br.com.ifes.backend_pit.models.atividades.resposta.RespostaAtividade;
import br.com.ifes.backend_pit.models.pit.PIT;
import br.com.ifes.backend_pit.models.usuarios.Servidor;
import br.com.ifes.backend_pit.reports.dto.PITDto;
import br.com.ifes.backend_pit.reports.dto.atividades.ensino.apoio_ensino.ApoioEnsino;
import br.com.ifes.backend_pit.reports.dto.atividades.ensino.apoio_ensino.AtividadesApoioEnsino;
import br.com.ifes.backend_pit.reports.dto.atividades.ensino.aula.Aula;
import br.com.ifes.backend_pit.reports.dto.atividades.ensino.aula.Aulas;
import br.com.ifes.backend_pit.reports.dto.atividades.ensino.detalhamento_componente_curricular.DetalhamentoAula;
import br.com.ifes.backend_pit.reports.dto.atividades.ensino.detalhamento_componente_curricular.DetalhamentosComponenteCurricular;
import br.com.ifes.backend_pit.reports.dto.atividades.ensino.detalhamento_projetos.DetalhamentoProjeto;
import br.com.ifes.backend_pit.reports.dto.atividades.ensino.detalhamento_projetos.DetalhamentosProjetos;
import br.com.ifes.backend_pit.reports.dto.atividades.ensino.mediacao_pedagogica.MediacaoPedagogica;
import br.com.ifes.backend_pit.reports.dto.atividades.ensino.mediacao_pedagogica.MediacoesPedagogicas;
import br.com.ifes.backend_pit.reports.dto.atividades.ensino.planejamento_manutencao_ensino.AtividadesPlanejamentoManutencaoEnsino;
import br.com.ifes.backend_pit.reports.dto.atividades.ensino.planejamento_manutencao_ensino.PlanejamentoManutencaoEnsino;
import br.com.ifes.backend_pit.reports.dto.atividades.extensao.atividade.AtividadeExtensao;
import br.com.ifes.backend_pit.reports.dto.atividades.extensao.atividade.AtividadesExtensao;
import br.com.ifes.backend_pit.reports.dto.atividades.extensao.detalhamento.DetalhamentoExtensao;
import br.com.ifes.backend_pit.reports.dto.atividades.extensao.detalhamento.DetalhamentosExtensao;
import br.com.ifes.backend_pit.reports.dto.atividades.gestao.AtividadesGestao;
import br.com.ifes.backend_pit.reports.dto.atividades.gestao.Gestao;
import br.com.ifes.backend_pit.reports.dto.atividades.outras.Outra;
import br.com.ifes.backend_pit.reports.dto.atividades.outras.OutrasAtividades;
import br.com.ifes.backend_pit.reports.dto.atividades.pesquisa.atividade.AtividadesPesquisa;
import br.com.ifes.backend_pit.reports.dto.atividades.pesquisa.atividade.Pesquisa;
import br.com.ifes.backend_pit.reports.dto.atividades.pesquisa.detalhamento.DetalhamentoPesquisa;
import br.com.ifes.backend_pit.reports.dto.atividades.pesquisa.detalhamento.DetalhamentosPesquisas;
import br.com.ifes.backend_pit.reports.dto.atividades.representacao.AtividadesRepresentacao;
import br.com.ifes.backend_pit.reports.dto.atividades.representacao.Representacao;
import br.com.ifes.backend_pit.reports.dto.cadastro.DadosCadastraisDTO;
import br.com.ifes.backend_pit.reports.dto.resumo.ResumoDTO;
import br.com.ifes.backend_pit.repositories.atividade.AtividadeRepository;
import br.com.ifes.backend_pit.repositories.atividade.resposta.RespostaAtividadeRepository;
import br.com.ifes.backend_pit.repositories.usuarios.ServidorRepository;
import br.com.ifes.backend_pit.utils.TimeOperations;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class GerarDtoRelatorioService {
    private final UUID idAula = UUID.fromString("8a42d95b-21f5-4299-8d39-5d172b655ddb");
    private final UUID idMediacaoPedagogica = UUID.fromString("ae280b35-8eaf-488f-9570-eb0805debdec");
    private final UUID idGestao = UUID.fromString("d1f16fa8-8318-4b2e-bace-ac101ad84291");
    private final UUID idRepresentacao = UUID.fromString("29d23456-0ca7-4072-a837-46bc101ebb15");
    private final ServidorRepository servidorRepository;
    private final RespostaAtividadeRepository respostaAtividadeRepository;
    private final AtividadeRepository atividadeRepository;

    public PITDto gerarDtoPit(PIT pit) {
        PITDto pitDto = new PITDto();

        List<RespostaAtividade> respostaAtividades = respostaAtividadeRepository.findAllByPitIdPIT(pit.getIdPIT());

        pitDto.setPeriodo(pit.getPeriodo());
        pitDto.setIdPit(pit.getIdPIT().toString());
        pitDto.setResumoCargaHorariaTotal("40");
        pitDto.setResumoDTO(this.getResumoDtoPit(respostaAtividades));
        pitDto.setDadosCadastraisDTO(this.getDadosCadastraisDto(pit));
        pitDto.setAulas(this.getAulasPit(respostaAtividades));
        pitDto.setAtividadesPlanejamentoManutencaoEnsino(this.getAtividadesPlanejamentoManutencaoEnsino(respostaAtividades));
        pitDto.setAtividadesApoioEnsino(this.getAtividadesApoioEnsino(respostaAtividades));
        pitDto.setDetalhamentosComponenteCurricular(this.getDetalhamentoComponenteCurricular(respostaAtividades));
        pitDto.setDetalhamentosProjetos(this.getDetalhamentoProjetos(respostaAtividades));
        pitDto.setMediacoesPedagogicas(this.getMediacoesPedagogica(respostaAtividades));
        pitDto.setAtividadesPesquisa(this.getAtividadesPesquisa(respostaAtividades));
        pitDto.setDetalhamentosPesquisas(this.getDetalhamentoPesquisa(respostaAtividades));
        pitDto.setAtividadesExtensao(this.getAtividadesExtensao(respostaAtividades));
        pitDto.setDetalhamentosExtensao(this.getDetalhamentoExtensao(respostaAtividades));
        pitDto.setAtividadesGestao(this.getAtividadesGestao(respostaAtividades));
        pitDto.setAtividadesRepresentacao(this.getAtividadesRepresentacao(respostaAtividades));
        pitDto.setOutrasAtividades(this.getOutrasAtividades(respostaAtividades));

        return pitDto;
    }

    private ResumoDTO getResumoDtoPit(List<RespostaAtividade> respostaAtividades) {
        ResumoDTO resumoDTO = new ResumoDTO();
        List<String> cargaHorarias = new ArrayList<>();

        Double totalEnsino = 0D;
        Double totalPesquisa = 0D;
        Double totalExtensao = 0D;
        Double totalGestao = 0D;
        Double totalRepresentacao = 0D;
        Double totalOutras = 0D;

        for (RespostaAtividade respostaAtividade : respostaAtividades) {
            UUID idAtividade = respostaAtividade.getAtividade().getIdAtividade();
            TipoAtividadeEnum tipoAtividadeEnum = respostaAtividade.getAtividade().getTipoAtividade();

            if (idAtividade.equals(idAula) || idAtividade.equals(idMediacaoPedagogica)) {
                totalEnsino = TimeOperations.tratarSomatorioCargaHoraria(totalEnsino, respostaAtividade.getCargaHorariaSemanal());
                // aula e mediação pedagogica tem a carga horaria duplicada para planejamento
                totalEnsino = TimeOperations.tratarSomatorioCargaHoraria(totalEnsino, respostaAtividade.getCargaHorariaSemanal());
            }

            else if(respostaAtividade.getAtividade().getTipoAtividade().equals(TipoAtividadeEnum.ATIVIDADE_APOIO_ENSINO)){
                totalEnsino = TimeOperations.tratarSomatorioCargaHoraria(totalEnsino, respostaAtividade.getCargaHorariaSemanal());
            }

            else if(tipoAtividadeEnum.equals(TipoAtividadeEnum.ATIVIDADE_PESQUISA)){
                totalPesquisa = TimeOperations.tratarSomatorioCargaHoraria(totalPesquisa, respostaAtividade.getCargaHorariaSemanal());
            }

            else if(tipoAtividadeEnum.equals(TipoAtividadeEnum.ATIVIDADE_EXTENSAO)){
                totalExtensao = TimeOperations.tratarSomatorioCargaHoraria(totalExtensao, respostaAtividade.getCargaHorariaSemanal());
            }

            else if(idAtividade.equals(idGestao)){
                totalGestao = TimeOperations.tratarSomatorioCargaHoraria(totalGestao, respostaAtividade.getCargaHorariaSemanal());
            }

            else if(idAtividade.equals(idRepresentacao)){
                totalRepresentacao = TimeOperations.tratarSomatorioCargaHoraria(totalRepresentacao, respostaAtividade.getCargaHorariaSemanal());
            }

            else if(tipoAtividadeEnum.equals(TipoAtividadeEnum.OUTRAS_ATIVIDADES)){
                totalOutras = TimeOperations.tratarSomatorioCargaHoraria(totalOutras, respostaAtividade.getCargaHorariaSemanal());
            }
        }

        cargaHorarias.add(TimeOperations.formatDoubleToDateString(totalEnsino));
        cargaHorarias.add(TimeOperations.formatDoubleToDateString(totalPesquisa));
        cargaHorarias.add(TimeOperations.formatDoubleToDateString(totalExtensao));
        cargaHorarias.add(TimeOperations.formatDoubleToDateString(totalGestao));
        cargaHorarias.add(TimeOperations.formatDoubleToDateString(totalRepresentacao));
        cargaHorarias.add(TimeOperations.formatDoubleToDateString(totalOutras));

        resumoDTO.setCargasHorarias(cargaHorarias);
        return resumoDTO;
    }

    private OutrasAtividades getOutrasAtividades(List<RespostaAtividade> respostaAtividades) {
        List<Atividade> atividades = atividadeRepository.findByTipoAtividadeOrderByNumeroOrdem(TipoAtividadeEnum.OUTRAS_ATIVIDADES);
        OutrasAtividades outrasAtividades = new OutrasAtividades();

        List<RespostaAtividade> respostasOutras = respostaAtividades.stream()
                .filter(resposta -> resposta.getAtividade().getTipoAtividade().equals(TipoAtividadeEnum.OUTRAS_ATIVIDADES))
                .collect(Collectors.toList());

        Double subTotal = 0D;
        Double subTotalParcial = 0D;

        for (Atividade atividade : atividades) {
            Outra outra = new Outra();

            Optional<RespostaAtividade> respostaAtividadeOptional = respostasOutras.stream()
                    .filter(resposta -> resposta.getAtividade().getIdAtividade().equals(atividade.getIdAtividade()))
                    .findFirst();

            outra.setTipo(atividade.getNomeAtividade());

            if(respostaAtividadeOptional.isPresent()){
                RespostaAtividade respostasOutra = respostaAtividadeOptional.get();

                outra.setChSemanal(TimeOperations.formatDoubleToDateString(respostasOutra.getCargaHorariaSemanal()));
                outra.setNumeroPortaria(respostasOutra.getDetalhamentoPortarias().get(0).getPortaria().getNome());

                subTotal = TimeOperations.tratarSomatorioCargaHoraria(subTotal, respostasOutra.getCargaHorariaSemanal());

                if(!atividade.getAbaixoDoSubTotal()){ // subtotal parcial soma somente os valores acima do subtotal
                    subTotalParcial = TimeOperations.tratarSomatorioCargaHoraria(subTotalParcial, respostasOutra.getCargaHorariaSemanal());
                }
            } else {
                outra.setChSemanal("");
                outra.setNumeroPortaria("");
            }

            if(atividade.getAbaixoDoSubTotal()){
                outrasAtividades.getOutrasAtividadesBolsita().add(outra);
            } else {
                outrasAtividades.getOutrasAtividades().add(outra);
            }
        }

        outrasAtividades.setSubtotal(TimeOperations.formatDoubleToDateString(subTotal));
        outrasAtividades.setSubtotalParcial(TimeOperations.formatDoubleToDateString(subTotalParcial));

        return outrasAtividades;
    }

    private AtividadesRepresentacao getAtividadesRepresentacao(List<RespostaAtividade> respostaAtividades) {
        AtividadesRepresentacao atividadesRepresentacao = new AtividadesRepresentacao();

        RespostaAtividade respostaGestao = respostaAtividades.stream()
                .filter(resposta -> resposta.getAtividade().getIdAtividade().equals(idRepresentacao))
                .findFirst()
                .orElseGet(RespostaAtividade::new);

        Double subTotal = 0D;

        for (DetalhamentoPortaria detalhamentoPortaria : respostaGestao.getDetalhamentoPortarias()) {
            Representacao representacao = new Representacao();
            representacao.setDescricaoAtribuicao(detalhamentoPortaria.getPortaria().getDescricao());
            representacao.setNumeroPortaria(detalhamentoPortaria.getPortaria().getNome());
            representacao.setDataInicio(TimeOperations.formatDateString(detalhamentoPortaria.getPortaria().getDataInicioVigencia()));
            representacao.setPeriodoVigencia(TimeOperations.formatDateString(detalhamentoPortaria.getPortaria().getDataFimVigencia()));
            representacao.setChSemanal(TimeOperations.formatDoubleToDateString(detalhamentoPortaria.getCargaHorariaSemanal()));

            atividadesRepresentacao.getRepresentacoes().add(representacao);
            subTotal = TimeOperations.tratarSomatorioCargaHoraria(subTotal, detalhamentoPortaria.getCargaHorariaSemanal());
        }

        atividadesRepresentacao.setSubtotal(TimeOperations.formatDoubleToDateString(subTotal));
        return atividadesRepresentacao;
    }

    private AtividadesGestao getAtividadesGestao(List<RespostaAtividade> respostaAtividades) {
        AtividadesGestao atividadesGestao = new AtividadesGestao();

        RespostaAtividade respostaGestao = respostaAtividades.stream()
                .filter(resposta -> resposta.getAtividade().getIdAtividade().equals(idGestao))
                .findFirst()
                .orElseGet(RespostaAtividade::new);

        Double subTotal = 0D;

        for (DetalhamentoPortaria detalhamentoPortaria : respostaGestao.getDetalhamentoPortarias()) {
            Gestao gestao = new Gestao();
            gestao.setDescricaoAtribuicao(detalhamentoPortaria.getPortaria().getDescricao());
            gestao.setNumeroPortaria(detalhamentoPortaria.getPortaria().getNome());
            gestao.setDataInicio(TimeOperations.formatDateString(detalhamentoPortaria.getPortaria().getDataInicioVigencia()));
            gestao.setPeriodoVigencia(TimeOperations.formatDateString(detalhamentoPortaria.getPortaria().getDataFimVigencia()));
            gestao.setChSemanal(TimeOperations.formatDoubleToDateString(detalhamentoPortaria.getCargaHorariaSemanal()));

            atividadesGestao.getGestoes().add(gestao);
            subTotal = TimeOperations.tratarSomatorioCargaHoraria(subTotal, detalhamentoPortaria.getCargaHorariaSemanal());
        }

        atividadesGestao.setSubtotal(TimeOperations.formatDoubleToDateString(subTotal));
        return atividadesGestao;
    }

    private DetalhamentosExtensao getDetalhamentoExtensao(List<RespostaAtividade> respostaAtividades) {
        DetalhamentosExtensao detalhamentosExtensao = new DetalhamentosExtensao();

        List<RespostaAtividade> respostaATividadesExtensao = respostaAtividades.stream()
                .filter(resposta ->
                        resposta.getAtividade().getTipoAtividade().equals(TipoAtividadeEnum.ATIVIDADE_EXTENSAO)
                                && (
                                resposta.getAtividade().getTipoDetalhamento().equals(TipoDetalhamentoEnum.DETALHAMENTO_PROJETO)
                                        || resposta.getAtividade().getTipoDetalhamento().equals(TipoDetalhamentoEnum.DETALHAMENTO_ALUNO)
                        )
                )
                .collect(Collectors.toList());

        for (RespostaAtividade respostaAtividade : respostaATividadesExtensao) {
            TipoDetalhamentoEnum tipoDetalhamentoEnum = respostaAtividade.getAtividade().getTipoDetalhamento();

            switch (tipoDetalhamentoEnum) {
                case DETALHAMENTO_PROJETO:
                    for (br.com.ifes.backend_pit.models.atividades.detalhamento.DetalhamentoProjeto detalhamentoProjeto : respostaAtividade.getDetalhamentoProjetos()) {
                        DetalhamentoExtensao detalhamentoExtensao = new DetalhamentoExtensao();
                        detalhamentoExtensao.setTipoAcao(detalhamentoProjeto.getProjeto().getTipoAcao());
                        detalhamentoExtensao.setTituloAcao(detalhamentoProjeto.getProjeto().getTituloProjeto());
                        detalhamentoExtensao.setTipoAtuacao(detalhamentoProjeto.getTipoParticipacao());
                        detalhamentoExtensao.setNumeroCadastro(detalhamentoProjeto.getProjeto().getNumeroCadastro());
                        detalhamentoExtensao.setChSemanal(TimeOperations.formatDoubleToDateString(detalhamentoProjeto.getCargaHorariaSemanal()));
                        detalhamentosExtensao.getDetalhamentosExtensao().add(detalhamentoExtensao);
                    }
                    break;
                case DETALHAMENTO_ALUNO:
                    for (DetalhamentoAluno detalhamentoAluno : respostaAtividade.getDetalhamentoAlunos()) {
                        DetalhamentoExtensao detalhamentoExtensao = new DetalhamentoExtensao();
                        detalhamentoExtensao.setTipoAtuacao(detalhamentoAluno.getTipoAtuacao());
                        detalhamentoExtensao.setTituloAcao(detalhamentoAluno.getAluno().getNome());
                        detalhamentoExtensao.setTipoAcao(detalhamentoAluno.getTipoAcao());
                        detalhamentoExtensao.setNumeroCadastro(detalhamentoAluno.getAluno().getMatricula());
                        detalhamentoExtensao.setChSemanal(TimeOperations.formatDoubleToDateString(detalhamentoAluno.getCargaHorariaSemanal()));

                        detalhamentosExtensao.getDetalhamentosExtensao().add(detalhamentoExtensao);
                    }
                    break;
            }
        }

        return detalhamentosExtensao;
    }

    private AtividadesExtensao getAtividadesExtensao(List<RespostaAtividade> respostaAtividades) {
        List<Atividade> atividades = atividadeRepository.findByTipoAtividadeOrderByNumeroOrdem(TipoAtividadeEnum.ATIVIDADE_EXTENSAO);
        AtividadesExtensao atividadesExtensao = new AtividadesExtensao();

        List<RespostaAtividade> respostaPesquisas = respostaAtividades.stream()
                .filter(resposta -> resposta.getAtividade().getTipoAtividade().equals(TipoAtividadeEnum.ATIVIDADE_EXTENSAO))
                .collect(Collectors.toList());

        Double subTotal = 0D;

        for (Atividade atividade : atividades) {
            AtividadeExtensao atividadeExtensao = new AtividadeExtensao();

            Optional<RespostaAtividade> respostaPesquisaOptional = respostaPesquisas.stream()
                    .filter(resposta -> resposta.getAtividade().getIdAtividade().equals(atividade.getIdAtividade()))
                    .findFirst();

            atividadeExtensao.setAtividade(atividade.getNomeAtividade());

            if(respostaPesquisaOptional.isPresent()){
                RespostaAtividade respostaPesquisa = respostaPesquisaOptional.get();

                atividadeExtensao.setChSemanal(TimeOperations.formatDoubleToDateString(respostaPesquisa.getCargaHorariaSemanal()));
                subTotal = TimeOperations.tratarSomatorioCargaHoraria(subTotal, respostaPesquisa.getCargaHorariaSemanal());
            } else {
                atividadeExtensao.setChSemanal(TimeOperations.formatDoubleToDateString(0D));
            }

            atividadesExtensao.getAtividadesExtensao().add(atividadeExtensao);
        }

        atividadesExtensao.setSubtotal(TimeOperations.formatDoubleToDateString(subTotal));
        return atividadesExtensao;
    }

    private DetalhamentosPesquisas getDetalhamentoPesquisa(List<RespostaAtividade> respostaAtividades) {
        DetalhamentosPesquisas detalhamentosPesquisas = new DetalhamentosPesquisas();

        List<RespostaAtividade> respostaAtividadesApoioEnsino = respostaAtividades.stream()
                .filter(resposta ->
                        resposta.getAtividade().getTipoAtividade().equals(TipoAtividadeEnum.ATIVIDADE_PESQUISA)
                                && (
                                resposta.getAtividade().getTipoDetalhamento().equals(TipoDetalhamentoEnum.DETALHAMENTO_PROJETO)
                                        || resposta.getAtividade().getTipoDetalhamento().equals(TipoDetalhamentoEnum.DETALHAMENTO_ALUNO)
                        )
                )
                .collect(Collectors.toList());

        for (RespostaAtividade respostaAtividade : respostaAtividadesApoioEnsino) {
            TipoDetalhamentoEnum tipoDetalhamentoEnum = respostaAtividade.getAtividade().getTipoDetalhamento();

            switch (tipoDetalhamentoEnum) {
                case DETALHAMENTO_PROJETO:
                    for (br.com.ifes.backend_pit.models.atividades.detalhamento.DetalhamentoProjeto detalhamentoProjeto : respostaAtividade.getDetalhamentoProjetos()) {
                        DetalhamentoPesquisa detalhamentoPesquisa = new DetalhamentoPesquisa();
                        detalhamentoPesquisa.setTipoAcao(detalhamentoProjeto.getProjeto().getTipoAcao());
                        detalhamentoPesquisa.setTituloAcao(detalhamentoProjeto.getProjeto().getTituloProjeto());
                        detalhamentoPesquisa.setTipoAtuacao(detalhamentoProjeto.getTipoParticipacao());
                        detalhamentoPesquisa.setNumeroCadastro(detalhamentoProjeto.getProjeto().getNumeroCadastro());
                        detalhamentoPesquisa.setChSemanal(TimeOperations.formatDoubleToDateString(detalhamentoProjeto.getCargaHorariaSemanal()));
                        detalhamentosPesquisas.getDetalhamentosPesquisas().add(detalhamentoPesquisa);
                    }
                    break;
                case DETALHAMENTO_ALUNO:
                    for (DetalhamentoAluno detalhamentoAluno : respostaAtividade.getDetalhamentoAlunos()) {
                        DetalhamentoPesquisa detalhamentoPesquisa = new DetalhamentoPesquisa();
                        detalhamentoPesquisa.setTipoAtuacao(detalhamentoAluno.getTipoAtuacao());
                        detalhamentoPesquisa.setTituloAcao(detalhamentoAluno.getAluno().getNome());
                        detalhamentoPesquisa.setTipoAcao(detalhamentoAluno.getTipoAcao());
                        detalhamentoPesquisa.setNumeroCadastro(detalhamentoAluno.getAluno().getMatricula());
                        detalhamentoPesquisa.setChSemanal(TimeOperations.formatDoubleToDateString(detalhamentoAluno.getCargaHorariaSemanal()));
                        detalhamentosPesquisas.getDetalhamentosPesquisas().add(detalhamentoPesquisa);
                    }
                    break;
            }
        }

        return detalhamentosPesquisas;
    }

    private AtividadesPesquisa getAtividadesPesquisa(List<RespostaAtividade> respostaAtividades) {
        List<Atividade> atividades = atividadeRepository.findByTipoAtividadeOrderByNumeroOrdem(TipoAtividadeEnum.ATIVIDADE_PESQUISA);

        AtividadesPesquisa atividadesPesquisa = new AtividadesPesquisa();

        List<RespostaAtividade> respostaPesquisas = respostaAtividades.stream()
                .filter(resposta -> resposta.getAtividade().getTipoAtividade().equals(TipoAtividadeEnum.ATIVIDADE_PESQUISA))
                .collect(Collectors.toList());

        Double subTotal = 0D;

        for (Atividade atividade : atividades) {
            Pesquisa pesquisa = new Pesquisa();
            Optional<RespostaAtividade> respostaAtividadeOptional = respostaPesquisas.stream()
                    .filter(resposta -> resposta.getAtividade().getIdAtividade().equals(atividade.getIdAtividade()))
                    .findFirst();

            pesquisa.setAtividade(atividade.getNomeAtividade());

            if(respostaAtividadeOptional.isPresent()){
                RespostaAtividade respostaPesquisa = respostaAtividadeOptional.get();
                pesquisa.setChSemanal(TimeOperations.formatDoubleToDateString(respostaPesquisa.getCargaHorariaSemanal()));
                subTotal = TimeOperations.tratarSomatorioCargaHoraria(subTotal, respostaPesquisa.getCargaHorariaSemanal());
            } else {
                pesquisa.setChSemanal(TimeOperations.formatDoubleToDateString(0D));
            }

            atividadesPesquisa.getAtividadesPesquisas().add(pesquisa);
        }

        atividadesPesquisa.setSubtotal(TimeOperations.formatDoubleToDateString(subTotal));
        return atividadesPesquisa;
    }

    private MediacoesPedagogicas getMediacoesPedagogica(List<RespostaAtividade> respostaAtividades) {
        MediacoesPedagogicas mediacoesPedagogicas = new MediacoesPedagogicas();

        RespostaAtividade respostaMediacao = respostaAtividades.stream()
                .filter(resposta -> resposta.getAtividade().getIdAtividade().equals(idMediacaoPedagogica))
                .findFirst()
                .orElseGet(RespostaAtividade::new);

        Double subtotal = 0D;

        for (DetalhamentoComponenteCurricular detalhamentoComponentesCurricular : respostaMediacao.getDetalhamentoComponentesCurriculares()) {
            MediacaoPedagogica mediacaoPedagogica = new MediacaoPedagogica();
            mediacaoPedagogica.setCurso(detalhamentoComponentesCurricular.getComponenteCurricular().getCurso().getNome());
            mediacaoPedagogica.setComponenteCurricular(detalhamentoComponentesCurricular.getComponenteCurricular().getNome());
            mediacaoPedagogica.setChSemanal(TimeOperations.formatDoubleToDateString(detalhamentoComponentesCurricular.getCargaHorariaSemanal()));

            mediacoesPedagogicas.getMediacoesPedagogicas().add(mediacaoPedagogica);
            subtotal = TimeOperations.tratarSomatorioCargaHoraria(subtotal, detalhamentoComponentesCurricular.getCargaHorariaSemanal());
        }

        mediacoesPedagogicas.setSubtotal(TimeOperations.formatDoubleToDateString(subtotal));
        return mediacoesPedagogicas;
    }

    /**
     * Retorna apenas os detalhamentos do tipo projetos e alunos
     *
     * @param respostaAtividades
     * @return
     */
    private DetalhamentosProjetos getDetalhamentoProjetos(List<RespostaAtividade> respostaAtividades) {
        DetalhamentosProjetos detalhamentosProjetos = new DetalhamentosProjetos();

        List<RespostaAtividade> respostaAtividadesApoioEnsino = respostaAtividades.stream()
                .filter(resposta ->
                        resposta.getAtividade().getTipoAtividade().equals(TipoAtividadeEnum.ATIVIDADE_APOIO_ENSINO)
                                && (
                                resposta.getAtividade().getTipoDetalhamento().equals(TipoDetalhamentoEnum.DETALHAMENTO_PROJETO)
                                        || resposta.getAtividade().getTipoDetalhamento().equals(TipoDetalhamentoEnum.DETALHAMENTO_ALUNO)
                        )
                )
                .collect(Collectors.toList());

        for (RespostaAtividade respostaAtividade : respostaAtividadesApoioEnsino) {
            TipoDetalhamentoEnum tipoDetalhamentoEnum = respostaAtividade.getAtividade().getTipoDetalhamento();

            switch (tipoDetalhamentoEnum) {
                case DETALHAMENTO_PROJETO:
                    for (br.com.ifes.backend_pit.models.atividades.detalhamento.DetalhamentoProjeto detalhamentoProjeto : respostaAtividade.getDetalhamentoProjetos()) {
                        DetalhamentoProjeto detalhamentoProjetoDto = new DetalhamentoProjeto();
                        detalhamentoProjetoDto.setTipoAcao(detalhamentoProjeto.getProjeto().getTipoAcao());
                        detalhamentoProjetoDto.setTituloAcao(detalhamentoProjeto.getProjeto().getTituloProjeto());
                        detalhamentoProjetoDto.setTipoAtuacao(detalhamentoProjeto.getTipoParticipacao());
                        detalhamentoProjetoDto.setNumeroCadastro(detalhamentoProjeto.getProjeto().getNumeroCadastro());
                        detalhamentoProjetoDto.setChSemanal(TimeOperations.formatDoubleToDateString(detalhamentoProjeto.getCargaHorariaSemanal()));
                        detalhamentosProjetos.getDetalhamentosProjetos().add(detalhamentoProjetoDto);
                    }
                    break;
                case DETALHAMENTO_ALUNO:
                    for (DetalhamentoAluno detalhamentoAluno : respostaAtividade.getDetalhamentoAlunos()) {
                        DetalhamentoProjeto detalhamentoProjetoDto = new DetalhamentoProjeto();
                        detalhamentoProjetoDto.setTipoAtuacao(detalhamentoAluno.getTipoAtuacao());
                        detalhamentoProjetoDto.setTituloAcao(detalhamentoAluno.getAluno().getNome());
                        detalhamentoProjetoDto.setTipoAcao(detalhamentoAluno.getTipoAcao());
                        detalhamentoProjetoDto.setNumeroCadastro(detalhamentoAluno.getAluno().getMatricula());
                        detalhamentoProjetoDto.setChSemanal(TimeOperations.formatDoubleToDateString(detalhamentoAluno.getCargaHorariaSemanal()));
                        detalhamentosProjetos.getDetalhamentosProjetos().add(detalhamentoProjetoDto);
                    }
                    break;
            }
        }

        return detalhamentosProjetos;
    }

    private DetalhamentosComponenteCurricular getDetalhamentoComponenteCurricular(List<RespostaAtividade> respostaAtividades) {
        DetalhamentosComponenteCurricular detalhamentosComponenteCurricular = new DetalhamentosComponenteCurricular();

        List<RespostaAtividade> respostaAtividadesApoioEnsino = respostaAtividades.stream()
                .filter(resposta ->
                        resposta.getAtividade().getTipoAtividade().equals(TipoAtividadeEnum.ATIVIDADE_APOIO_ENSINO)
                                && resposta.getAtividade().getTipoDetalhamento().equals(TipoDetalhamentoEnum.DETALHAMENTO_AULA)
                )
                .collect(Collectors.toList());

        Double subTotal = 0D;

        for (RespostaAtividade respostaAtividade : respostaAtividadesApoioEnsino) {
            for (DetalhamentoComponenteCurricular detalhamentoComponentesCurricular : respostaAtividade.getDetalhamentoComponentesCurriculares()) {
                DetalhamentoAula detalhamentoAula = new DetalhamentoAula();
                detalhamentoAula.setComponenteCurricular(detalhamentoComponentesCurricular.getComponenteCurricular().getNome());
                detalhamentoAula.setCurso(detalhamentoComponentesCurricular.getComponenteCurricular().getCurso().getNome());
                detalhamentoAula.setChSemanal(TimeOperations.formatDoubleToDateString(detalhamentoComponentesCurricular.getCargaHorariaSemanal()));
                detalhamentosComponenteCurricular.getDetalhamentoAulas().add(detalhamentoAula);
                subTotal = TimeOperations.tratarSomatorioCargaHoraria(subTotal, detalhamentoComponentesCurricular.getCargaHorariaSemanal());
            }
        }

        detalhamentosComponenteCurricular.setSubtotal(TimeOperations.formatDoubleToDateString(subTotal));
        return detalhamentosComponenteCurricular;
    }

    private AtividadesApoioEnsino getAtividadesApoioEnsino(List<RespostaAtividade> respostaAtividades) {
        List<Atividade> atividades = atividadeRepository.findByTipoAtividadeOrderByNumeroOrdem(TipoAtividadeEnum.ATIVIDADE_APOIO_ENSINO);

        AtividadesApoioEnsino atividadesApoioEnsino = new AtividadesApoioEnsino();
        List<ApoioEnsino> atividadesPlanejamentoManutencaoEnsino = new ArrayList<>();
        List<ApoioEnsino> atividadesPlanejamentoManutencaoEnsinoCoordenacao = new ArrayList<>();

        List<RespostaAtividade> respostaAtividadesApoioEnsino = respostaAtividades.stream()
                .filter(resposta -> resposta.getAtividade().getTipoAtividade().equals(TipoAtividadeEnum.ATIVIDADE_APOIO_ENSINO))
                .collect(Collectors.toList());

        Double subTotal = 0D;
        Double subTotalParcial = 0D;

        for(Atividade atividade : atividades){
            ApoioEnsino apoioEnsino = new ApoioEnsino();
            Optional<RespostaAtividade> respostaAtividadeOptional = respostaAtividadesApoioEnsino.stream()
                    .filter(resposta -> resposta.getAtividade().getIdAtividade().equals(atividade.getIdAtividade()))
                    .findFirst();

            apoioEnsino.setAtividade(atividade.getNomeAtividade());

            if(respostaAtividadeOptional.isPresent()){
                RespostaAtividade respostaAtividade = respostaAtividadeOptional.get();
                apoioEnsino.setChSemanal(TimeOperations.formatDoubleToDateString(respostaAtividade.getCargaHorariaSemanal()));

                subTotal = TimeOperations.tratarSomatorioCargaHoraria(subTotal, respostaAtividade.getCargaHorariaSemanal());
                if(!atividade.getAbaixoDoSubTotal()){ // subtotal parcial soma somente os valores acima do subtotal
                    subTotalParcial = TimeOperations.tratarSomatorioCargaHoraria(subTotalParcial, respostaAtividade.getCargaHorariaSemanal());
                }
            } else {
                apoioEnsino.setChSemanal(TimeOperations.formatDoubleToDateString(0D));
            }

            if(atividade.getAbaixoDoSubTotal()){
                atividadesPlanejamentoManutencaoEnsinoCoordenacao.add(apoioEnsino);
            } else {
                atividadesPlanejamentoManutencaoEnsino.add(apoioEnsino);
            }
        }

        atividadesApoioEnsino.setSubtotal(TimeOperations.formatDoubleToDateString(subTotal));
        atividadesApoioEnsino.setSubtotalParcial(TimeOperations.formatDoubleToDateString(subTotalParcial));
        atividadesApoioEnsino.setAtividadesPlanejamentoManutencaoEnsino(atividadesPlanejamentoManutencaoEnsino);
        atividadesApoioEnsino.setAtividadesPlanejamentoManutencaoEnsinoCoordenacao(atividadesPlanejamentoManutencaoEnsinoCoordenacao);
        return atividadesApoioEnsino;
    }

    private AtividadesPlanejamentoManutencaoEnsino getAtividadesPlanejamentoManutencaoEnsino(List<RespostaAtividade> respostaAtividades) {
        AtividadesPlanejamentoManutencaoEnsino atividadesPlanejamentoManutencaoEnsino = new AtividadesPlanejamentoManutencaoEnsino();


        PlanejamentoManutencaoEnsino planejamentoManutencaoEnsino = new PlanejamentoManutencaoEnsino();
        planejamentoManutencaoEnsino.setAtividade("Atividades de Planejamento e Manutenção de Ensino");


        // obter as aulas e as mediações pedagogicas
        List<RespostaAtividade> respostasAulasERespostasMediacao = respostaAtividades.stream()
                .filter(resposta -> resposta.getAtividade().getIdAtividade().equals(idAula) || resposta.getAtividade().getIdAtividade().equals(idMediacaoPedagogica))
                .collect(Collectors.toList());

        Double total = 0D;
        for (RespostaAtividade respostaAtividade : respostasAulasERespostasMediacao) {
            total = TimeOperations.tratarSomatorioCargaHoraria(total, respostaAtividade.getCargaHorariaSemanal());
        }

        planejamentoManutencaoEnsino.setChSemanal(TimeOperations.formatDoubleToDateString(total));

        atividadesPlanejamentoManutencaoEnsino.getAtividadesPlanejamentoManutencaoEnsino().add(planejamentoManutencaoEnsino);
        atividadesPlanejamentoManutencaoEnsino.setSubtotal(TimeOperations.formatDoubleToDateString(total));
        return atividadesPlanejamentoManutencaoEnsino;
    }

    private DadosCadastraisDTO getDadosCadastraisDto(PIT pit) {
        Servidor servidor = this.servidorRepository.findById(pit.getProfessor().getIdServidor())
                .orElseThrow(() -> new NotFoundException(ErrorConstants.PROFESSOR_ERROS.MSG_ERRO_PROFESSOR_NAO_ENCONTRADO));

        if(
                servidor.getJornadaTrabalho() == null
                || servidor.getAreaPrincipalAtuacao() == null || servidor.getAreaPrincipalAtuacao().equals("")
                || servidor.getTitulacao() == null || servidor.getTitulacao().equals("")
                || servidor.getSiape() == null || servidor.getSiape().equals("")
                || servidor.getCampus() == null || servidor.getCampus().equals("")
                || servidor.getDepartamento() == null || servidor.getDepartamento().equals("")
        ){
            throw new BusinessException("Preencha todos os dados cadastrais do seu perfil para gerar o relatório");
        }

        DadosCadastraisDTO dadosCadastraisDTO = new DadosCadastraisDTO();
        dadosCadastraisDTO.setCampus(servidor.getCampus());
        dadosCadastraisDTO.setDepartamento(servidor.getDepartamento());
        dadosCadastraisDTO.setEfetivo(servidor.isEfetivo() ? "SIM" : "NÃO");
        dadosCadastraisDTO.setEmAfastamento(servidor.isPossuiAfastamento() ? "SIM" : "");
        dadosCadastraisDTO.setJornadaTrabalho(servidor.getJornadaTrabalho().toString());
        dadosCadastraisDTO.setAreaPrincipalAtuacao(servidor.getAreaPrincipalAtuacao());
        dadosCadastraisDTO.setNome(servidor.getNome());
        dadosCadastraisDTO.setTitulacao(servidor.getTitulacao());
        dadosCadastraisDTO.setSiape(servidor.getSiape());

        return dadosCadastraisDTO;
    }

    private Aulas getAulasPit(List<RespostaAtividade> respostaAtividades) {
        RespostaAtividade respostaAula = respostaAtividades.stream()
                .filter(respostaAtividade -> respostaAtividade.getAtividade().getIdAtividade().equals(idAula))
                .findFirst()
                .orElseGet(RespostaAtividade::new);

        Aulas aulas = new Aulas();

        aulas.setSubtotal(TimeOperations.formatDoubleToDateString(respostaAula.getCargaHorariaSemanal()));

        for (DetalhamentoComponenteCurricular detalhamentoComponenteCurricular : respostaAula.getDetalhamentoComponentesCurriculares()) {
            Aula aula = new Aula();
            aula.setChSemanal(TimeOperations.formatDoubleToDateString(detalhamentoComponenteCurricular.getCargaHorariaSemanal()));
            aula.setCurso(detalhamentoComponenteCurricular.getComponenteCurricular().getCurso().getNome());
            aula.setComponenteCurricular(detalhamentoComponenteCurricular.getComponenteCurricular().getNome());
            aulas.getAulas().add(aula);
        }

        return aulas;
    }
}
