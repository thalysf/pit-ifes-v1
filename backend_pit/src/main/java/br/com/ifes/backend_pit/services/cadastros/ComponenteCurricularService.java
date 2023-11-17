package br.com.ifes.backend_pit.services.cadastros;

import br.com.ifes.backend_pit.exception.BusinessException;
import br.com.ifes.backend_pit.exception.NotFoundException;
import br.com.ifes.backend_pit.models.cadastros.ComponenteCurricular;
import br.com.ifes.backend_pit.models.cadastros.Curso;
import br.com.ifes.backend_pit.repositories.cadastros.ComponenteCurricularRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

import static br.com.ifes.backend_pit.constants.ErrorConstants.COMPONENTE_CURRICULAR_ERROS.EXCLUIR_COMPONENTE_DETALHADO;
import static br.com.ifes.backend_pit.constants.ErrorConstants.COMPONENTE_CURRICULAR_ERROS.MSG_ERRO_COMPONENTE_CURRICULAR_NAO_ENCONTRADO;

@Service
@RequiredArgsConstructor
@Transactional
public class ComponenteCurricularService {
    private final ComponenteCurricularRepository componenteCurricularRepository;


    public List<ComponenteCurricular> listarComponentesCurso(UUID cursoId) {
        Curso curso = new Curso();
        curso.setIdCurso(cursoId);

        return this.componenteCurricularRepository.findAllByCurso(curso);
    }

    public ComponenteCurricular salvarComponenteCurricular(ComponenteCurricular componenteCurricular) {
        return this.componenteCurricularRepository.save(componenteCurricular);
    }

    public ComponenteCurricular getComponenteCursoById(UUID idComponente) {
        return componenteCurricularRepository.findById(idComponente)
                .orElseThrow(() -> new NotFoundException(MSG_ERRO_COMPONENTE_CURRICULAR_NAO_ENCONTRADO));
    }

    public void deleteComponente(UUID idComponente) {
        ComponenteCurricular componenteCurricular = this.getComponenteCursoById(idComponente);
        if(componenteCurricular.getDetalhamentoComponenteCurriculares().size() > 0){
            throw new BusinessException(EXCLUIR_COMPONENTE_DETALHADO);
        }
        this.componenteCurricularRepository.deleteById(idComponente);
    }


    public List<ComponenteCurricular> listarComponentesCurriculares() {
        return this.componenteCurricularRepository.findAll();
    }

    public List<ComponenteCurricular> listarComponentesCurriculares(String nome) {
        return this.componenteCurricularRepository.findAllByNomeContainingIgnoreCase(nome);
    }

    public List<ComponenteCurricular> listarComponentesCurriculares(String nome, String nome_curso) {
        return this.componenteCurricularRepository.findAllByNomeContainingIgnoreCaseOrCursoNomeContainingIgnoreCase(nome, nome_curso);
    }
}
