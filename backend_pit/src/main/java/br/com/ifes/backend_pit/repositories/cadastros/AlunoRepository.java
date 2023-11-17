package br.com.ifes.backend_pit.repositories.cadastros;

import br.com.ifes.backend_pit.models.cadastros.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, UUID> {
    Optional<Aluno> findByMatricula(String matricula);
}
