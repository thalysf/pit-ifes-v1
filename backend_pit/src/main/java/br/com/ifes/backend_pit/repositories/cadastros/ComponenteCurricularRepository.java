package br.com.ifes.backend_pit.repositories.cadastros;

import br.com.ifes.backend_pit.models.cadastros.ComponenteCurricular;
import br.com.ifes.backend_pit.models.cadastros.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ComponenteCurricularRepository extends JpaRepository<ComponenteCurricular, UUID> {
    List<ComponenteCurricular> findAllByCurso(Curso curso);

    List<ComponenteCurricular> findAllByNomeContainingIgnoreCaseOrCursoNomeContainingIgnoreCase(String nome, String nomeCurso);

    List<ComponenteCurricular> findAllByNomeContainingIgnoreCase(String nome);

    Optional<ComponenteCurricular> findByNomeIgnoreCaseAndCursoNomeIgnoreCase(String nomeComponente, String nomeCurso);
}
