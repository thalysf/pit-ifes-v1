package br.com.ifes.backend_pit.models.cadastros;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class Curso implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idCurso;

    @Column(nullable = false, unique = true)
    @NotBlank
    private String nome;

    @OneToMany(mappedBy = "curso")
    @JsonIgnoreProperties("curso")
    private List<ComponenteCurricular> componenteCurriculares = new ArrayList<>();

    public Curso() {
    }
}
