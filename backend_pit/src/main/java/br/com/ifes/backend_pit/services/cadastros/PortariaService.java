package br.com.ifes.backend_pit.services.cadastros;

import br.com.ifes.backend_pit.constants.ErrorConstants;
import br.com.ifes.backend_pit.enums.RoleNameEnum;
import br.com.ifes.backend_pit.enums.TipoPortariaEnum;
import br.com.ifes.backend_pit.exception.BusinessException;
import br.com.ifes.backend_pit.exception.NotFoundException;
import br.com.ifes.backend_pit.models.atividades.Atividade;
import br.com.ifes.backend_pit.models.atividades.detalhamento.DetalhamentoPortaria;
import br.com.ifes.backend_pit.models.atividades.resposta.RespostaAtividade;
import br.com.ifes.backend_pit.models.cadastros.Portaria;
import br.com.ifes.backend_pit.models.cadastros.Projeto;
import br.com.ifes.backend_pit.models.dto.api.EnumDto;
import br.com.ifes.backend_pit.models.pit.PIT;
import br.com.ifes.backend_pit.models.usuarios.Servidor;
import br.com.ifes.backend_pit.repositories.atividade.AtividadeRepository;
import br.com.ifes.backend_pit.repositories.atividade.detalhamento.DetalhamentoPortariaRepository;
import br.com.ifes.backend_pit.repositories.atividade.resposta.RespostaAtividadeRepository;
import br.com.ifes.backend_pit.repositories.cadastros.PortariaRepository;
import br.com.ifes.backend_pit.repositories.usuarios.ProfessorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static br.com.ifes.backend_pit.constants.ErrorConstants.PORTARIA_ERROS.ALTERAR_PORTARIA_COM_ATIVIDADE;
import static br.com.ifes.backend_pit.constants.ErrorConstants.PORTARIA_ERROS.EXCLUIR_PORTARIA_COM_ATIVIDADE;
import static br.com.ifes.backend_pit.constants.ErrorConstants.PORTARIA_ERROS.PORTARIA_NAO_ENCONTRADA;
import static br.com.ifes.backend_pit.constants.ErrorConstants.PROFESSOR_ERROS.MSG_ERRO_PROFESSOR_NAO_ENCONTRADO;

@Service
@RequiredArgsConstructor
@Transactional
public class PortariaService {
    private final PortariaRepository portariaRepository;
    private final ProfessorRepository professorRepository;
    private final AtividadeRepository atividadeRepository;
    private final DetalhamentoPortariaRepository detalhamentoPortariaRepository;
    private final RespostaAtividadeRepository respostaAtividadeRepository;
    private final UUID idGestao = UUID.fromString("d1f16fa8-8318-4b2e-bace-ac101ad84291");
    private final UUID idRepresentacao = UUID.fromString("29d23456-0ca7-4072-a837-46bc101ebb15");

    public Portaria savePortaria(Portaria portaria) {
        // obter o tipo da portaria, se for gestao ou representação, alterar o vinculo com a atividade
        // pois gestão e representação são duas atividades que o usuario não enxerga no sistema
        // logo devemos remover ou inserir a portaria nessa atividade manualmente

        UUID idAtividade = null;
        String idTipoPortaria = portaria.getTipoAtividade().getId();
        if(idTipoPortaria.equals(TipoPortariaEnum.ATIVIDADE_REPRESENTACAO.getId())){
            idAtividade = this.idRepresentacao;
        } else if(idTipoPortaria.equals(TipoPortariaEnum.ATIVIDADE_GESTAO.getId())){
            idAtividade = this.idGestao;
        }

        if(idAtividade != null){
            Atividade atividade = this.atividadeRepository.findById(idAtividade)
                    .orElseThrow(() -> new NotFoundException(ErrorConstants.ATIVIDADE_ERROS.ATIVIDADE_NAO_ENCONTRADA));

            portaria.setAtividade(atividade);
        }
        return portariaRepository.save(portaria);
    }

    public List<Portaria> getPortarias() {
        return portariaRepository.findAll();
    }

    public Portaria getPortaria(UUID idPortaria) {
        return portariaRepository.findById(idPortaria)
                .orElseThrow(() -> new NotFoundException(PORTARIA_NAO_ENCONTRADA));
    }

    /**
     * Ao alterar uma portaria caso mude o tipo de atividade, a portaria é cadastrada na nova atividade e removida da anterior
     * @param portariaAtualizar
     * @return
     */
    public Portaria updatePortaria(Portaria portariaAtualizar) {
        Portaria portariaBefore = this.portariaRepository.findById(portariaAtualizar.getIdPortaria())
                .orElseThrow(() -> new NotFoundException(PORTARIA_NAO_ENCONTRADA));

        // mudou o tipo da portaria
        if(!portariaAtualizar.getTipoAtividade().getId().equals(portariaBefore.getTipoAtividade().getId())){
            // se portaria era do tipo outras, verificar se possui atividades associadas antes de mudar
            if(portariaBefore.getTipoAtividade().getId().equals(TipoPortariaEnum.OUTRAS_ATIVIDADES.getId())){
                if(portariaBefore.getAtividade() != null){ // estourando erro caso possua atividade vinculada
                    throw new BusinessException(ALTERAR_PORTARIA_COM_ATIVIDADE);
                }
            } else {
                portariaAtualizar.setAtividade(null);
            }
        }

        return savePortaria(portariaAtualizar);
    }

    public void deletePortaria(UUID idPortaria) {
        Portaria portaria = portariaRepository.findById(idPortaria)
                .orElseThrow(() -> new NotFoundException(PORTARIA_NAO_ENCONTRADA));
        if (portaria.getAtividade() != null) {
            throw new BusinessException(EXCLUIR_PORTARIA_COM_ATIVIDADE);
        }
        portariaRepository.deleteById(idPortaria);
    }

    public List<EnumDto> getTipoAtividades() {
        return List.of(
                new EnumDto(TipoPortariaEnum.OUTRAS_ATIVIDADES.getId(), TipoPortariaEnum.OUTRAS_ATIVIDADES.getNome()),
                new EnumDto(TipoPortariaEnum.ATIVIDADE_GESTAO.getId(), TipoPortariaEnum.ATIVIDADE_GESTAO.getNome()),
                new EnumDto(TipoPortariaEnum.ATIVIDADE_REPRESENTACAO.getId(), TipoPortariaEnum.ATIVIDADE_REPRESENTACAO.getNome())
        );
    }

    public List<Servidor> getProfessoresPortaria(UUID idPortaria) {
        Optional<Portaria> portariaOptional = this.portariaRepository.findById(idPortaria);
        if (portariaOptional.isPresent()) {
            return portariaOptional.get().getProfessores();
        }
        return new ArrayList<>();
    }


    /**
     * @param idPortaria    codigo da portaria a alterar
     * @param idProfessores lista de professoes que farão parte da portaria
     *                      Substitui a lista de professores atual da portaria pela lista informada em idProfessores
     */
    public void salvarProfessoresPortaria(UUID idPortaria, List<UUID> idProfessores) {
        Portaria portaria = this.portariaRepository.findById(idPortaria)
                .orElseThrow(() -> new NotFoundException(PORTARIA_NAO_ENCONTRADA));

        List<Servidor> professoresSalvar = this.professorRepository.findAllById(idProfessores);

        portaria.setProfessores(professoresSalvar);

        this.portariaRepository.save(portaria);
    }

    public List<Portaria> getPortariasApoioEnsino() {
        return this.portariaRepository.findAllByTipoAtividade(TipoPortariaEnum.ATIVIDADE_APOIO_ENSINO);
    }

    public List<Portaria> getPortariasPesquisa() {
        return this.portariaRepository.findAllByTipoAtividade(TipoPortariaEnum.ATIVIDADE_PESQUISA);
    }

    public List<Portaria> getPortariasExtensao() {
        return this.portariaRepository.findAllByTipoAtividade(TipoPortariaEnum.ATIVIDADE_EXTENSAO);
    }

    public List<Portaria> getPortariasOutrasAtividades() {
        return this.portariaRepository.findAllByTipoAtividade(TipoPortariaEnum.OUTRAS_ATIVIDADES);
    }

    public List<Servidor> listarProfessoresNaoParticipantes(UUID idPortaria) {
        return this.portariaRepository.findProfessoresNaoParticipantes(idPortaria, RoleNameEnum.PROFESSOR);
    }

    public Servidor inserirProfessorNaPortaria(UUID idPortaria, UUID idProfessor) {
        Servidor professor = professorRepository.findById(idProfessor)
                .orElseThrow(() -> new NotFoundException(MSG_ERRO_PROFESSOR_NAO_ENCONTRADO));

        Portaria portaria = this.getPortaria(idPortaria);

        portaria.getProfessores().add(professor);
        portariaRepository.save(portaria);
        return professor;
    }

    public void removerProfessorDaPortaria(UUID idPortaria, UUID idProfessor) {
        Servidor professor = professorRepository.findById(idProfessor)
                .orElseThrow(() -> new NotFoundException(MSG_ERRO_PROFESSOR_NAO_ENCONTRADO));

        Portaria portaria = this.getPortaria(idPortaria);

        List<DetalhamentoPortaria> detalhamentoPortarias = detalhamentoPortariaRepository.findAllByPortariaIdPortariaAndPortariaProfessoresIdServidor(idPortaria, idProfessor);

        for (DetalhamentoPortaria detalhamentoPortaria : detalhamentoPortarias) {
            PIT pit = detalhamentoPortaria.getRespostaAtividade().getPit();
            if(!pit.isAprovado()){
                RespostaAtividade respostaAtividade = detalhamentoPortaria.getRespostaAtividade();
                respostaAtividade.getDetalhamentoPortarias().removeIf(d -> d.getIdDetalhamentoPortaria().equals(detalhamentoPortaria.getIdDetalhamentoPortaria()));
                respostaAtividade.atualizarCargaHorariaSemanalPortarias();
                respostaAtividadeRepository.save(respostaAtividade);
            }
        }

        portaria.getProfessores().removeIf(p -> p.getIdServidor().equals(idProfessor));
    }
}