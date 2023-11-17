package br.com.ifes.backend_pit.repositories.usuarios;

import br.com.ifes.backend_pit.models.usuarios.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<Usuario, UUID> {
    Optional<Usuario> findByUsername(String username);
}
