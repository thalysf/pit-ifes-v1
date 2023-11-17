package br.com.ifes.backend_pit.repositories.usuarios;

import br.com.ifes.backend_pit.models.usuarios.Servidor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProfessorRepository extends JpaRepository<Servidor, UUID> {
    Boolean existsByEmailAndIdServidorNot(String email, UUID idProfessor);

    Optional<Servidor> findProfessorByUsuarioUserId(UUID userId);

    Servidor findServidorBySiape(String siape);
}
