package br.com.ifes.backend_pit.models.cadastros;

import br.com.ifes.backend_pit.enums.TipoPortariaEnum;
import br.com.ifes.backend_pit.models.atividades.Atividade;
import br.com.ifes.backend_pit.models.usuarios.Servidor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class Portaria implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idPortaria;

    private String nome;

    @Enumerated(EnumType.STRING)
    private TipoPortariaEnum tipoAtividade;

    private String descricao;

    private Timestamp dataInicioVigencia;

    private Timestamp dataFimVigencia;

    private Double cargaHorariaMinima;

    private Double cargaHorariaMaxima;

    @ManyToMany
    @JoinTable(
            name = "portaria_professor",
            joinColumns = @JoinColumn(name = "id_portaria"),
            inverseJoinColumns = @JoinColumn(name = "id_servidor")
    )
    private List<Servidor> professores;

    @ManyToOne
    @JsonIgnoreProperties({"portarias", "respostaAtividades"})
    private Atividade atividade;

    public Portaria() {

    }

    public Portaria(UUID idPortaria) {
        this.idPortaria = idPortaria;
    }
}
