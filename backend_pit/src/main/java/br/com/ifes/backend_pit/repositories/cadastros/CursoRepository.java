package br.com.ifes.backend_pit.repositories.cadastros;

import br.com.ifes.backend_pit.models.cadastros.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CursoRepository extends JpaRepository<Curso, UUID> {
    Optional<Curso> findByNomeIgnoreCase(String nome);
}
