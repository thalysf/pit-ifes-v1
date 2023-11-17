package br.com.ifes.backend_pit.models.cadastros;

import br.com.ifes.backend_pit.models.atividades.detalhamento.DetalhamentoComponenteCurricular;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class ComponenteCurricular implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idComponenteCurricular;

    @Column
    private String nome;

    @Column
    private Double cargaHoraria;

    @ManyToOne
    private Curso curso;

    @OneToMany(mappedBy = "componenteCurricular")
    @JsonIgnoreProperties("componenteCurricular")
    private List<DetalhamentoComponenteCurricular> detalhamentoComponenteCurriculares = new ArrayList<>();

    public ComponenteCurricular() {
    }
}
