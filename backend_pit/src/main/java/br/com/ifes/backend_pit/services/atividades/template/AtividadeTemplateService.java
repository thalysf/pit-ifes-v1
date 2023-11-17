package br.com.ifes.backend_pit.services.atividades.template;

import br.com.ifes.backend_pit.enums.TipoAtividadeEnum;
import br.com.ifes.backend_pit.enums.TipoDetalhamentoEnum;
import br.com.ifes.backend_pit.exception.BusinessException;
import br.com.ifes.backend_pit.exception.NotFoundException;
import br.com.ifes.backend_pit.models.atividades.Atividade;
import br.com.ifes.backend_pit.models.cadastros.Portaria;
import br.com.ifes.backend_pit.models.cadastros.Projeto;
import br.com.ifes.backend_pit.models.dto.api.AssociarDto;
import br.com.ifes.backend_pit.repositories.atividade.AtividadeRepository;
import br.com.ifes.backend_pit.repositories.cadastros.PortariaRepository;
import br.com.ifes.backend_pit.repositories.cadastros.ProjetoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static br.com.ifes.backend_pit.constants.ErrorConstants.ATIVIDADE_ERROS.ATIVIDADE_NAO_ENCONTRADA;
import static br.com.ifes.backend_pit.constants.ErrorConstants.ATIVIDADE_ERROS.ATIVIDADE_SEM_CARGA_HORARIA;

@Service
@RequiredArgsConstructor
@Transactional
public abstract class AtividadeTemplateService {

    protected final AtividadeRepository atividadeRepository;
    protected final ProjetoRepository projetoRepository;
    protected final PortariaRepository portariaRepository;

    protected Atividade cadastrarAtividade(Atividade atividade) {
        this.validarAtividade(atividade);
        return atividadeRepository.save(atividade);
    }

    protected void validarAtividade(Atividade atividade){
        if((atividade.getTipoDetalhamento().equals(TipoDetalhamentoEnum.DETALHAMENTO_ALUNO) || atividade.getTipoDetalhamento().equals(TipoDetalhamentoEnum.NENHUM))){
            if (atividade.getCargaHorariaMaxima() == null || atividade.getCargaHorariaMinima() == null) {
                throw new BusinessException(ATIVIDADE_SEM_CARGA_HORARIA);
            }
            if(atividade.getCargaHorariaMaxima() < atividade.getCargaHorariaMinima()){
                throw new BusinessException("Carga horaria máxima não pode ser maior que a mínima");
            }
            if(atividade.getCargaHorariaMinima() < 0){
                throw new BusinessException("Carga horaria mínima não pode ser menor que zero");
            }
            if(atividade.getCargaHorariaMaxima() < 0){
                throw new BusinessException("Carga horaria máxima não pode ser menor que zero");
            }
        }
    }

    public void vincularAtividadeProjeto(UUID idAtividade, AssociarDto associarAtividadeProjetoDto) {
        Atividade atividade = atividadeRepository.findById(idAtividade).orElseThrow(() -> new NotFoundException(ATIVIDADE_NAO_ENCONTRADA));

        List<Projeto> projetos = projetoRepository.findAllById(associarAtividadeProjetoDto.getEntidades());

        for (Projeto projeto : atividade.getProjetos()) {
            // remover da atividade os projetos que não vieram no dto
            Optional<Projeto> projetoOptional = projetos.stream().filter(p -> p.getIdProjeto().equals(projeto.getIdProjeto())).findFirst();
            if(projetoOptional.isEmpty()){
                projeto.getAtividades().removeIf(a -> a.getIdAtividade().equals(atividade.getIdAtividade()));
                this.projetoRepository.save(projeto);
            }
        }

        projetos.forEach(projeto -> projeto.getAtividades().add(atividade));
        atividade.setProjetos(projetos);
        atividadeRepository.save(atividade);
    }

    public void vincularAtividadePortarias(UUID idAtividade, AssociarDto associarAtividadePortariaDto) {
        Atividade atividade = atividadeRepository.findById(idAtividade).orElseThrow(() -> new NotFoundException(ATIVIDADE_NAO_ENCONTRADA));

        List<Portaria> portarias = portariaRepository.findAllById(associarAtividadePortariaDto.getEntidades());

        for (Portaria portaria : atividade.getPortarias()) {
            // remover da atividade as portarias que não vieram no dto
            Optional<Portaria> portariaOptional = portarias.stream().filter(p -> p.getIdPortaria().equals(portaria.getIdPortaria())).findFirst();
            if(portariaOptional.isEmpty()){
                portaria.setAtividade(null); //remover atividade da portaria que não veio no dto
                this.portariaRepository.save(portaria);
            }
        }

        portarias.forEach(portaria -> portaria.setAtividade(atividade));
        atividade.setPortarias(portarias);
        atividadeRepository.save(atividade);
    }

    public List<Projeto> recuperarVinculoAtividadeProjeto(UUID idAtividade) {
        return atividadeRepository.findById(idAtividade).orElseThrow(() -> new NotFoundException(ATIVIDADE_NAO_ENCONTRADA)).getProjetos();
    }

    public List<Portaria> recuperarAtividadePortaria(UUID idAtividade) {
        return atividadeRepository.findById(idAtividade).orElseThrow(() -> new NotFoundException(ATIVIDADE_NAO_ENCONTRADA)).getPortarias();
    }

    protected List<Atividade> getAtividadeByTipo(TipoAtividadeEnum tipoAtividade) {
        return atividadeRepository.findByTipoAtividadeOrderByNumeroOrdem(tipoAtividade);
    }


    public Atividade getAtividade(UUID idAtividade) {
        return atividadeRepository.findById(idAtividade).orElseThrow(() -> new NotFoundException(ATIVIDADE_NAO_ENCONTRADA));
    }
}
