package br.com.ifes.backend_pit.repositories.usuarios;

import br.com.ifes.backend_pit.enums.RoleNameEnum;
import br.com.ifes.backend_pit.models.usuarios.Servidor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ServidorRepository extends JpaRepository<Servidor, UUID> {
    Boolean existsByEmailAndIdServidorNot(String email, UUID idServidor);

    @Query("SELECT s from Servidor s JOIN s.usuario.roles role where " +
            "role.roleName = :roleName")
    List<Servidor> findServidoresByRoleName(RoleNameEnum roleName);

    Optional<Servidor> findByUsuarioUserId(UUID userId);

    Optional<Servidor> findServidorBySiape(String siape);

    Optional<Servidor> findByEmailIgnoreCaseAndSiape(String email, String siape);
}
