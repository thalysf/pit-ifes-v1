package br.com.ifes.backend_pit.services.cadastros;

import br.com.ifes.backend_pit.exception.BusinessException;
import br.com.ifes.backend_pit.exception.NotFoundException;
import br.com.ifes.backend_pit.models.cadastros.Curso;
import br.com.ifes.backend_pit.repositories.cadastros.CursoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

import static br.com.ifes.backend_pit.constants.ErrorConstants.CURSO_ERROS.EXCLUIR_CURSO_COM_COMPONENTES;
import static br.com.ifes.backend_pit.constants.ErrorConstants.CURSO_ERROS.MSG_ERRO_CURSO_NAO_ENCONTRADO;

@Service
@RequiredArgsConstructor
@Transactional
public class CursoService {
    private final CursoRepository cursoRepository;

    public Curso salvarCurso(Curso curso) {
        return this.cursoRepository.save(curso);
    }

    public List<Curso> getCursos() {
        return this.cursoRepository.findAll();
    }

    public void deletarCurso(UUID id) {
        Curso curso = this.cursoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(MSG_ERRO_CURSO_NAO_ENCONTRADO));

        if (curso.getComponenteCurriculares().size() > 0) {
            throw new BusinessException(EXCLUIR_CURSO_COM_COMPONENTES);
        }
        this.cursoRepository.deleteById(id);
    }

    public Curso getCurso(UUID id) {
        return cursoRepository.findById(id).orElseThrow(() -> new NotFoundException(MSG_ERRO_CURSO_NAO_ENCONTRADO));
    }
}
