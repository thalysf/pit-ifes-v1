package br.com.ifes.backend_pit.models.atividades.detalhamento;

import br.com.ifes.backend_pit.models.atividades.resposta.RespostaAtividade;
import br.com.ifes.backend_pit.models.cadastros.ComponenteCurricular;
import br.com.ifes.backend_pit.models.cadastros.Projeto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Builder
public class DetalhamentoProjeto implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idDetalhamentoProjeto;

    private Double cargaHorariaSemanal;

    private String tipoParticipacao;

    @ManyToOne
    @Fetch(FetchMode.JOIN)
    private Projeto projeto;

    @ManyToOne
    @JsonIgnore
    private RespostaAtividade respostaAtividade;

    public DetalhamentoProjeto() {

    }
}
