package br.com.ifes.backend_pit.models.atividades.detalhamento;

import br.com.ifes.backend_pit.models.atividades.resposta.RespostaAtividade;
import br.com.ifes.backend_pit.models.cadastros.Portaria;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Builder
public class DetalhamentoPortaria implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idDetalhamentoPortaria;

    private Double cargaHorariaSemanal;

    @ManyToOne
    @Fetch(FetchMode.JOIN)
    private Portaria portaria;

    @ManyToOne
    @JsonIgnore
    private RespostaAtividade respostaAtividade;

    public DetalhamentoPortaria() {

    }
}
