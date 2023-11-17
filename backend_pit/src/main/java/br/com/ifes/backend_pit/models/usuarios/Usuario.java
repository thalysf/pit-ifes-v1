package br.com.ifes.backend_pit.models.usuarios;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class Usuario implements Serializable, UserDetails {
    private static final long serialVersionUID = 2762464740208422452L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID userId;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String recoveryCode;

    @Column
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Instant recoveryCodeGeneratedAt;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "usuario_role",
            foreignKey = @ForeignKey(name = "none"),
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles;

    public Usuario() {
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public List<Role> getRoles() {
        if (this.roles == null) {
            return new ArrayList<>();
        }
        return this.roles;
    }

    public void clearRecoveryCode() {
        this.recoveryCode = null;
        this.recoveryCodeGeneratedAt = null;
    }
}
