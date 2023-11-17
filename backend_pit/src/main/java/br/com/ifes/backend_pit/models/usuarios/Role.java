package br.com.ifes.backend_pit.models.usuarios;

import br.com.ifes.backend_pit.enums.RoleNameEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Role implements GrantedAuthority, Serializable {
    private static final long serialVersionUID = 2979801072074086063L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID roleId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private RoleNameEnum roleName;

    @Override
    public String getAuthority() {
        return this.roleName.name();
    }
}
