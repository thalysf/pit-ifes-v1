package br.com.ifes.backend_pit.models.atividades.detalhamento;

import br.com.ifes.backend_pit.models.atividades.resposta.RespostaAtividade;
import br.com.ifes.backend_pit.models.cadastros.Aluno;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

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
public class DetalhamentoAluno implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idDetalhamentoAluno;

    private Double cargaHorariaSemanal;

    // estágio, orientação de tcc, etc
    private String tipoAcao;

    // orientador ou coordenador
    private String tipoAtuacao;

    @ManyToOne
    @JsonIgnore
    private RespostaAtividade respostaAtividade;

    @ManyToOne
    private Aluno aluno;

    public DetalhamentoAluno() {
    }
}
