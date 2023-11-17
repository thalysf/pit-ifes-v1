package br.com.ifes.backend_pit.services.atividades;

import br.com.ifes.backend_pit.enums.TipoAtividadeEnum;
import br.com.ifes.backend_pit.enums.TipoDetalhamentoEnum;
import br.com.ifes.backend_pit.enums.TipoProjetoEnum;
import br.com.ifes.backend_pit.exception.BusinessException;
import br.com.ifes.backend_pit.exception.NotFoundException;
import br.com.ifes.backend_pit.models.atividades.Atividade;
import br.com.ifes.backend_pit.models.cadastros.ParticipacaoProjeto;
import br.com.ifes.backend_pit.models.cadastros.Portaria;
import br.com.ifes.backend_pit.models.cadastros.Projeto;
import br.com.ifes.backend_pit.models.dto.api.EnumDto;
import br.com.ifes.backend_pit.repositories.atividade.AtividadeRepository;
import br.com.ifes.backend_pit.repositories.cadastros.PortariaRepository;
import br.com.ifes.backend_pit.repositories.cadastros.ProjetoRepository;
import br.com.ifes.backend_pit.services.atividades.template.AtividadeTemplateService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static br.com.ifes.backend_pit.constants.ErrorConstants.ATIVIDADE_ERROS.*;

@Service
@Transactional
public class AtividadesService extends AtividadeTemplateService {
    public AtividadesService(AtividadeRepository atividadeRepository, ProjetoRepository projetoRepository, PortariaRepository portariaRepository) {
        super(atividadeRepository, projetoRepository, portariaRepository);
    }

    public List<EnumDto> listarTipoDetalhamentoEnum() {
        return Arrays.stream(TipoDetalhamentoEnum.values())
                .map(tipoDetalhamento -> new EnumDto(tipoDetalhamento.getId(), tipoDetalhamento.getNome()))
                .collect(Collectors.toList());
    }

    public Atividade cadastrarAtividade(Atividade atividade, TipoAtividadeEnum tipoAtividade) {
        if(atividade.getAbaixoDoSubTotal() == null) atividade.setAbaixoDoSubTotal(false);
        atividade.setTipoAtividade(tipoAtividade);
        return super.cadastrarAtividade(atividade);
    }

    public List<Atividade> getAtividadesByTipo(TipoAtividadeEnum tipoAtividade) {
        return super.getAtividadeByTipo(tipoAtividade);
    }

    public List<ParticipacaoProjeto> recuperarProjetosAtividadeProfessor(UUID idAtividade, UUID idProfessor) {
        return this.atividadeRepository.findProjetosProfessorAtividade(idProfessor, idAtividade);
    }

    public List<Portaria> recuperarPortariasAtividadeProfessor(UUID idAtividade, UUID idServidor) {
        return this.atividadeRepository.findPortariasProfessorAtividade(idServidor, idAtividade);
    }

    public Atividade atualizarAtividade(UUID idAtividade, Atividade atividadeAfter) {
        Atividade atividadeBefor = this.atividadeRepository.findById(idAtividade).orElseThrow(() -> new NotFoundException(ATIVIDADE_NAO_ENCONTRADA));

        if (!atividadeBefor.getTipoDetalhamento().equals(atividadeAfter.getTipoDetalhamento())) {
            if (atividadeBefor.getTipoDetalhamento().equals(TipoDetalhamentoEnum.DETALHAMENTO_PROJETO)) {
                if (atividadeBefor.getProjetos() != null && atividadeBefor.getProjetos().size() > 0) {
                    throw new BusinessException("Para alterar o tipo de detalhamento, remova os projetos associados.");
                }
            }
            if (atividadeBefor.getTipoDetalhamento().equals(TipoDetalhamentoEnum.DETALHAMENTO_PORTARIA)) {
                if (atividadeBefor.getPortarias() != null && atividadeBefor.getPortarias().size() > 0) {
                    throw new BusinessException("Para alterar o tipo de detalhamento, remova as portarias associadas.");
                }
            }
        }

        this.validarAtividade(atividadeAfter);

        return this.atividadeRepository.save(atividadeAfter);
    }

    public void deletarAtividade(UUID idAtividade) {
        Atividade atividade = this.atividadeRepository.findById(idAtividade)
                .orElseThrow(() -> new NotFoundException(ATIVIDADE_NAO_ENCONTRADA));

        if (atividade.getPortarias().size() > 0) {
            throw new BusinessException(EXCLUIR_ATIVIDADE_COM_PORTARIRAS);
        }
        if (atividade.getProjetos().size() > 0) {
            throw new BusinessException(EXCLUIR_ATIVIDADE_COM_PROJETOS);
        }
        if (atividade.getRespostaAtividades().size() > 0) {
            throw new BusinessException(EXCLUIR_ATIVIDADE_COM_RESPOSTA);
        }

        this.atividadeRepository.delete(atividade);
    }

    public List<EnumDto> listarTipoDetalhamentoApoioEnsino() {
        List<EnumDto> tipos = new ArrayList<>();
        tipos.add(new EnumDto(TipoDetalhamentoEnum.DETALHAMENTO_ALUNO.getId(), TipoDetalhamentoEnum.DETALHAMENTO_ALUNO.getNome()));
        tipos.add(new EnumDto(TipoDetalhamentoEnum.DETALHAMENTO_PROJETO.getId(), TipoDetalhamentoEnum.DETALHAMENTO_PROJETO.getNome()));
        tipos.add(new EnumDto(TipoDetalhamentoEnum.DETALHAMENTO_AULA.getId(), TipoDetalhamentoEnum.DETALHAMENTO_AULA.getNome()));
        tipos.add(new EnumDto(TipoDetalhamentoEnum.NENHUM.getId(), TipoDetalhamentoEnum.NENHUM.getNome()));
        return tipos;
    }

    public List<EnumDto> listarTipoDetalhamentoPesquisa() {
        List<EnumDto> tipos = new ArrayList<>();
        tipos.add(new EnumDto(TipoDetalhamentoEnum.DETALHAMENTO_ALUNO.getId(), TipoDetalhamentoEnum.DETALHAMENTO_ALUNO.getNome()));
        tipos.add(new EnumDto(TipoDetalhamentoEnum.DETALHAMENTO_PROJETO.getId(), TipoDetalhamentoEnum.DETALHAMENTO_PROJETO.getNome()));
        return tipos;
    }

    public List<EnumDto> listarTipoDetalhamentoExtensao() {
        List<EnumDto> tipos = new ArrayList<>();
        tipos.add(new EnumDto(TipoDetalhamentoEnum.DETALHAMENTO_ALUNO.getId(), TipoDetalhamentoEnum.DETALHAMENTO_ALUNO.getNome()));
        tipos.add(new EnumDto(TipoDetalhamentoEnum.DETALHAMENTO_PROJETO.getId(), TipoDetalhamentoEnum.DETALHAMENTO_PROJETO.getNome()));
        return tipos;
    }

    /**
     * traz todos os projetos que não estão nessa idAtividade e que sejam do tipo atividade/projeto requisitado
     */
    public List<Projeto> getProjetosPeloTipoEIdAtividadeNaoPresenteNoProjeto(UUID idAtividade, TipoAtividadeEnum tipoAtividade) {
        TipoProjetoEnum tipoProjetoEnum;
        switch (tipoAtividade){
            case ATIVIDADE_PESQUISA:
                tipoProjetoEnum = TipoProjetoEnum.ATIVIDADE_PESQUISA;
                break;
            case ATIVIDADE_EXTENSAO:
                tipoProjetoEnum = TipoProjetoEnum.ATIVIDADE_EXTENSAO;
                break;
            case ATIVIDADE_APOIO_ENSINO:
                tipoProjetoEnum = TipoProjetoEnum.ATIVIDADE_ENSINO;
                break;
            default:
                throw new BusinessException("Tipo de atividade não mapeada");
        }

        Atividade atividade = this.getAtividade(idAtividade);

        return projetoRepository.findByTipoProjetoAndAtividadesNotContains(tipoProjetoEnum, atividade);
    }

    public void associarProjetoAtividade(UUID idAtividade, Projeto projeto) {
        Atividade atividade = this.getAtividade(idAtividade);
        atividade.getProjetos().removeIf(p -> p.getIdProjeto().equals(projeto.getIdProjeto()));
        atividade.getProjetos().add(projeto);
        atividadeRepository.save(atividade);
    }

    public void removerProjetoAtividade(UUID idAtividade, UUID idProjeto) {
        Atividade atividade = this.getAtividade(idAtividade);
        atividade.getProjetos().removeIf(p -> p.getIdProjeto().equals(idProjeto));
        atividadeRepository.save(atividade);
    }
}
