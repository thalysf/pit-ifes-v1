package br.com.ifes.backend_pit.services.pit;

import br.com.ifes.backend_pit.email.EmailService;
import br.com.ifes.backend_pit.exception.BusinessException;
import br.com.ifes.backend_pit.exception.NotFoundException;
import br.com.ifes.backend_pit.models.atividades.resposta.RespostaAtividade;
import br.com.ifes.backend_pit.models.dto.api.RequestPITDto;
import br.com.ifes.backend_pit.models.pit.PIT;
import br.com.ifes.backend_pit.models.usuarios.Servidor;
import br.com.ifes.backend_pit.reports.dto.PITDto;
import br.com.ifes.backend_pit.reports.service.GerarDtoRelatorioService;
import br.com.ifes.backend_pit.reports.service.RelatorioService;
import br.com.ifes.backend_pit.repositories.atividade.resposta.RespostaAtividadeRepository;
import br.com.ifes.backend_pit.repositories.pit.PITRepository;
import br.com.ifes.backend_pit.repositories.usuarios.ProfessorRepository;
import br.com.ifes.backend_pit.utils.TimeOperations;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static br.com.ifes.backend_pit.constants.ErrorConstants.PIT_ERROS.PIT_APROVADO_NAO_ALTERA;
import static br.com.ifes.backend_pit.constants.ErrorConstants.PIT_ERROS.PIT_EM_REVISAO_NAO_ALTERA;
import static br.com.ifes.backend_pit.constants.ErrorConstants.PIT_ERROS.PIT_NAO_ENCONTRADO;
import static br.com.ifes.backend_pit.constants.ErrorConstants.PIT_ERROS.PIT_PROFESSOR_PERIODO_JA_EXISTENTE;
import static br.com.ifes.backend_pit.constants.ErrorConstants.PIT_ERROS.TOTAL_HORAS_INVALIDO;
import static br.com.ifes.backend_pit.constants.ErrorConstants.PROFESSOR_ERROS.MSG_ERRO_PROFESSOR_NAO_ENCONTRADO;

@Service
@RequiredArgsConstructor
@Transactional
public class PITService {

    private final UUID idAula = UUID.fromString("8a42d95b-21f5-4299-8d39-5d172b655ddb");
    private final UUID idMediacaoPedagogica = UUID.fromString("ae280b35-8eaf-488f-9570-eb0805debdec");

    private final PITRepository pitRepository;
    private final RespostaAtividadeRepository respostaAtividadeRepository;
    private final ProfessorRepository professorRepository;
    private final GerarDtoRelatorioService gerarDtoRelatorioService;
    private final RelatorioService relatorioService;
    private final EmailService emailService;


    public PIT iniciarPITProfessor(UUID idUsuario, RequestPITDto requestPITDto) {
        Optional<Servidor> professorOpt = professorRepository.findProfessorByUsuarioUserId(idUsuario);

        if (professorOpt.isEmpty()) {
            throw new NotFoundException(MSG_ERRO_PROFESSOR_NAO_ENCONTRADO);
        }

        this.verificarPitEmAbertoProfessor(idUsuario);
        this.verificaPeriodoPit(professorOpt.get().getIdServidor(), requestPITDto.getPeriodo());

        PIT pit = new PIT();
        pit.setProfessor(professorOpt.get());
        pit.setPeriodo(requestPITDto.getPeriodo());

        return pitRepository.save(pit);
    }

    private void verificaPeriodoPit(UUID idServidor, String periodo) {
        Optional<PIT> pitOptional = this.pitRepository.findPITByProfessorIdServidorAndPeriodoEquals(idServidor, periodo);
        if(pitOptional.isPresent()){
            throw new BusinessException(PIT_PROFESSOR_PERIODO_JA_EXISTENTE);
        }
    }

    public void verificarPitEmAbertoProfessor(UUID idUsuario) {
        Optional<PIT> pitOpt = pitRepository.findPitEmAberto(idUsuario);
        if (pitOpt.isPresent()) {
            throw new BusinessException(PIT_PROFESSOR_PERIODO_JA_EXISTENTE);
        }
    }

    /**
     * Retorna o pit pelo id informado, porem estoura erro se o pit estiver aprovado
     *
     * @param idPit
     * @return
     */
    public PIT getPitResponder(UUID idPit) {
        Optional<PIT> pitOpt = pitRepository.findById(idPit);
        if (pitOpt.isEmpty()) throw new NotFoundException(PIT_NAO_ENCONTRADO);
        if (pitOpt.get().isAprovado()) throw new BusinessException(PIT_APROVADO_NAO_ALTERA);
        if(pitOpt.get().isEmRevisao()) throw new BusinessException(PIT_EM_REVISAO_NAO_ALTERA);
        return pitOpt.get();
    }

    public PIT getPitById(UUID idPit) {
        return pitRepository.findById(idPit).orElseThrow(() -> new NotFoundException(PIT_NAO_ENCONTRADO));
    }

    /**
     * Retorna o PIT em aberto para o usuario informado
     *
     * @param idUsuario
     * @return
     */
    public Optional<PIT> getPitEmAberto(UUID idUsuario) {
        return pitRepository.findPitEmAberto(idUsuario);
    }

    public void encerrar(UUID idPit) {
        PIT pit = getPitById(idPit);
        pit.setDataEntrega(new Timestamp(new Date().getTime()));
    }

    public Double getTotalHoras(UUID idPit) {
        PIT pit = getPitById(idPit);
        List<RespostaAtividade> respostaAtividades = this.respostaAtividadeRepository.findAllByPitIdPIT(idPit);

        Double total = 0D;

        for (RespostaAtividade respostaAtividade : respostaAtividades) {
            if(respostaAtividade.getCargaHorariaSemanal() != null){
                total = TimeOperations.tratarSomatorioCargaHoraria(total, respostaAtividade.getCargaHorariaSemanal());
                // duplicar a quantidade de horas de aulas e mediação pedagogica para o planejamento
                if(respostaAtividade.getAtividade().getIdAtividade().equals(idAula) || respostaAtividade.getAtividade().getIdAtividade().equals(idMediacaoPedagogica)){
                    total = TimeOperations.tratarSomatorioCargaHoraria(total, respostaAtividade.getCargaHorariaSemanal());
                }
            }
        }

        return total;
    }

    public void enviarParaRevisao(UUID idPit) {
        PIT pit = getPitById(idPit);
        Double totalHoras = this.getTotalHoras(pit.getIdPIT());
        if(!totalHoras.equals(40D)){
            throw new BusinessException(TOTAL_HORAS_INVALIDO);
        }

        pit.setEmRevisao(true);
        pit.setDataEntrega(new Timestamp(new Date().getTime()));
        PITDto pitDto = this.gerarDtoRelatorioService.gerarDtoPit(pit);

        this.relatorioService.gerarRelatorio(pitDto);
    }

    public List<PIT> obterPitsProfessor(Servidor servidor) {
        return this.pitRepository.findAllByProfessorIdServidorOrderByAprovadoAscEmRevisaoAsc(servidor.getIdServidor());
    }

    public List<PIT> obterPitsEmRevisao() {
        return this.pitRepository.findAllByEmRevisaoOrderByDataEntregaDesc(true);
    }

    public List<PIT> obterPitsAprovados(){
        return this.pitRepository.findAllByAprovadoOrderByDataEntregaDesc(true);
    }

    public void pedirAlteracoes(UUID idPit, String texto) {
        PIT pit = this.getPitById(idPit);

        if(texto.isEmpty() || texto.isBlank()){
            throw new BusinessException("Informe um texto nas alterações");
        }

        String email = pit.getProfessor().getEmail();
        String assunto = "Alterações solicitadas no PIT " + pit.getPeriodo();

        this.emailService.enviarEmail(email, assunto, texto);

        pit.setEmRevisao(false);
        pit.setAprovado(false);
        pitRepository.save(pit);
    }

    public void aprovar(UUID idPit) {
        PIT pit = this.getPitById(idPit);
        pit.setAprovado(true);
        pit.setEmRevisao(false);
        pit.setDataEntrega(new Timestamp(new Date().getTime()));
        pitRepository.save(pit);
    }
}
