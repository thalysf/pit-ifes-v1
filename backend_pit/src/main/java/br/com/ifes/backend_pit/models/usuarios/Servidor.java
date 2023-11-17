package br.com.ifes.backend_pit.models.usuarios;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Getter
@Setter
@NoArgsConstructor
public class Servidor implements Serializable {

    private static final long serialVersionUID = 9084789396376488616L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idServidor;

    @Column(nullable = false)
    @NotBlank
    private String nome;

    @Column(nullable = false, unique = true)
    @Email
    @NotBlank
    private String email;

    @Column
    private String campus;

    @Column
    private String departamento;

    @Column
    private String siape;

    @Column
    private Double jornadaTrabalho;

    @Column
    private String areaPrincipalAtuacao;

    @Column
    private String titulacao;

    @Column
    private boolean efetivo;

    @Column
    private boolean possuiAfastamento;

    @Column
    private boolean administrador;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private Usuario usuario;
}
