package br.com.ifes.backend_pit.services.cadastros;

import br.com.ifes.backend_pit.constants.ErrorConstants;
import br.com.ifes.backend_pit.enums.RoleNameEnum;
import br.com.ifes.backend_pit.enums.TipoParticipacaoProjetoEnum;
import br.com.ifes.backend_pit.enums.TipoProjetoEnum;
import br.com.ifes.backend_pit.exception.BusinessException;
import br.com.ifes.backend_pit.exception.NotFoundException;
import br.com.ifes.backend_pit.models.cadastros.ParticipacaoProjeto;
import br.com.ifes.backend_pit.models.cadastros.Projeto;
import br.com.ifes.backend_pit.models.dto.api.EnumDto;
import br.com.ifes.backend_pit.models.usuarios.Servidor;
import br.com.ifes.backend_pit.repositories.cadastros.ParticipacaoProjetoRepository;
import br.com.ifes.backend_pit.repositories.cadastros.ProjetoRepository;
import br.com.ifes.backend_pit.repositories.usuarios.ProfessorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static br.com.ifes.backend_pit.constants.ErrorConstants.PROFESSOR_ERROS.MSG_ERRO_PROFESSOR_NAO_ENCONTRADO;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjetoService {
    private final ProjetoRepository projetoRepository;
    private final ParticipacaoProjetoRepository participacaoProjetoRepository;
    private final ProfessorRepository professorRepository;


    public List<Projeto> getProjetos() {
        return this.projetoRepository.findAll();
    }

    public Projeto getProjeto(UUID id) {
        Optional<Projeto> projetoOpt = this.projetoRepository.findById(id);
        if (projetoOpt.isEmpty()) {
            throw new NotFoundException("Projeto não encontrado!");
        }
        return projetoOpt.get();
    }

    public Projeto criarProjeto(Projeto projeto) {
        return this.projetoRepository.save(projeto);
    }

    public void deletarProjeto(UUID id) {
        Projeto projeto = this.getProjeto(id);
        this.projetoRepository.delete(projeto);
    }

    public Projeto atualizarProjeto(Projeto projeto) {
        return projetoRepository.save(projeto);
    }

    public List<EnumDto> listarTipoAtividadeProjetoNameEnum() {
        return Arrays.stream(TipoProjetoEnum.values())
                .map(tipoProjeto -> new EnumDto(tipoProjeto.getId(), tipoProjeto.getNome()))
                .collect(Collectors.toList());
    }

    public List<ParticipacaoProjeto> listarProfessoresParticipantes(UUID idProjeto) {
        return participacaoProjetoRepository.findAllByProjetoIdProjeto(idProjeto);
    }

    public List<Servidor> listarProfessoresNaoParticipantes(UUID idProjeto) {
        return participacaoProjetoRepository.findProfessoresNaoParticipantes(idProjeto, RoleNameEnum.PROFESSOR);
    }

    public List<EnumDto> listarTiposParticipacaoProjeto() {
        return Arrays.stream(TipoParticipacaoProjetoEnum.values())
                .map(tipoParticipaoProjeto -> new EnumDto(tipoParticipaoProjeto.getId(), tipoParticipaoProjeto.getNome()))
                .collect(Collectors.toList());
    }

    public ParticipacaoProjeto criarAssociacaoProfessorProjeto(ParticipacaoProjeto participacaoProjeto) {
        Optional<ParticipacaoProjeto> participacaoProjetoOpt = participacaoProjetoRepository
                .findByProjetoIdProjetoAndProfessorIdServidor(participacaoProjeto.getProjeto().getIdProjeto(),
                        participacaoProjeto.getProfessor().getIdServidor());
        if (participacaoProjetoOpt.isPresent()) {
            throw new BusinessException("A associação informada de projeto e professor já existe!");
        } else {
            return participacaoProjetoRepository.save(participacaoProjeto);
        }
    }

    public ParticipacaoProjeto atualizarAssociacaoProfessorProjeto(UUID idProjeto, UUID idProfessor) {
        Optional<ParticipacaoProjeto> participacaoProjetoOpt = participacaoProjetoRepository
                .findByProjetoIdProjetoAndProfessorIdServidor(idProjeto,
                        idProfessor);
        if (participacaoProjetoOpt.isPresent()) {
            var participacaoProjeto = participacaoProjetoOpt.get();
            return participacaoProjetoRepository.save(participacaoProjeto);
        } else {
            throw new NotFoundException("A associação informada de projeto e professor não existe!");
        }
    }

    public void deletarAssociacaoProfessorProjeto(UUID idProjeto, UUID idProfessor) {
        long participacaoProjetoLinhasAfetadas = participacaoProjetoRepository
                .deleteByProjetoIdProjetoAndProfessorIdServidor(idProjeto,
                        idProfessor);
        if (participacaoProjetoLinhasAfetadas == 0L) {
            throw new NotFoundException("A associação informada de projeto e professor não existe!");
        }
    }

    public ParticipacaoProjeto criarAssociacaoProfessorProjeto(UUID idProjeto, UUID idProfessor) {
        ParticipacaoProjeto participacaoProjeto = new ParticipacaoProjeto();

        Servidor professor = this.professorRepository.findById(idProfessor)
                .orElseThrow(() -> new NotFoundException(MSG_ERRO_PROFESSOR_NAO_ENCONTRADO));

        Projeto projeto = this.getProjeto(idProjeto);

        participacaoProjeto.setProjeto(projeto);
        participacaoProjeto.setProfessor(professor);

        return this.criarAssociacaoProfessorProjeto(participacaoProjeto);
    }

    public List<ParticipacaoProjeto> listarParticipantes(UUID idProjeto) {
        return this.participacaoProjetoRepository.findAllByProjetoIdProjeto(idProjeto);
    }
}
