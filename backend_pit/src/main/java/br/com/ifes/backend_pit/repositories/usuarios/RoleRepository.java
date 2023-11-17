package br.com.ifes.backend_pit.repositories.usuarios;

import br.com.ifes.backend_pit.enums.RoleNameEnum;
import br.com.ifes.backend_pit.models.usuarios.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findByRoleName(RoleNameEnum roleName);

    Optional<List<Role>> findAllByRoleNameIn(List<RoleNameEnum> roleNames);
}
