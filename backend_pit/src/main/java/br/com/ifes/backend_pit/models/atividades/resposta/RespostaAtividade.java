package br.com.ifes.backend_pit.models.atividades.resposta;

import br.com.ifes.backend_pit.models.atividades.Atividade;
import br.com.ifes.backend_pit.models.atividades.detalhamento.DetalhamentoAluno;
import br.com.ifes.backend_pit.models.atividades.detalhamento.DetalhamentoComponenteCurricular;
import br.com.ifes.backend_pit.models.atividades.detalhamento.DetalhamentoPortaria;
import br.com.ifes.backend_pit.models.atividades.detalhamento.DetalhamentoProjeto;
import br.com.ifes.backend_pit.models.cadastros.ComponenteCurricular;
import br.com.ifes.backend_pit.models.pit.PIT;
import br.com.ifes.backend_pit.reports.dto.atividades.ensino.detalhamento_componente_curricular.DetalhamentoAula;
import br.com.ifes.backend_pit.utils.TimeOperations;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Builder
public class RespostaAtividade implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idRespostaAtividade;

    private Double cargaHorariaSemanal;

    @NotNull
    @ManyToOne
    private PIT pit;

    @ManyToOne
    private Atividade atividade;

    @OneToMany(mappedBy = "respostaAtividade", cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(FetchMode.JOIN)
    @JsonIgnoreProperties("respostaAtividade")
    private List<DetalhamentoComponenteCurricular> detalhamentoComponentesCurriculares = new ArrayList<>();

    @OneToMany(mappedBy = "respostaAtividade", cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(FetchMode.SELECT)
    @JsonIgnoreProperties("respostaAtividade")
    private List<DetalhamentoProjeto> detalhamentoProjetos = new ArrayList<>();

    @OneToMany(mappedBy = "respostaAtividade", cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(FetchMode.SELECT)
    @JsonIgnoreProperties("respostaAtividade")
    private List<DetalhamentoPortaria> detalhamentoPortarias = new ArrayList<>();

    @OneToMany(mappedBy = "respostaAtividade", cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(FetchMode.SELECT)
    @JsonIgnoreProperties("respostaAtividade")
    private List<DetalhamentoAluno> detalhamentoAlunos = new ArrayList<>();

    public RespostaAtividade() {

    }

    public void addComponenteCurricular(ComponenteCurricular componenteCurricular, Double cargaHorariaSemanal) {
        this.getDetalhamentoComponentesCurriculares().add(DetalhamentoComponenteCurricular.builder()
                .componenteCurricular(componenteCurricular)
                .cargaHorariaSemanal(cargaHorariaSemanal)
                .respostaAtividade(this)
                .build());
    }

    public void atualizarCargaHorariaSemanalAulas() {
        this.cargaHorariaSemanal = 0D;
        for(DetalhamentoComponenteCurricular detalhamentoComponenteCurricular : this.detalhamentoComponentesCurriculares){
            this.cargaHorariaSemanal = TimeOperations.tratarSomatorioCargaHoraria(this.cargaHorariaSemanal, detalhamentoComponenteCurricular.getCargaHorariaSemanal());
        }
    }

    public void atualizarCargaHorariaSemanalProjetos(){
        this.cargaHorariaSemanal = 0D;
        for(DetalhamentoProjeto detalhamentoProjeto : this.detalhamentoProjetos){
            this.cargaHorariaSemanal = TimeOperations.tratarSomatorioCargaHoraria(this.cargaHorariaSemanal, detalhamentoProjeto.getCargaHorariaSemanal());
        }
    }

    public void atualizarCargaHorariaSemanalPortarias(){
        this.cargaHorariaSemanal = 0D;
        for(DetalhamentoPortaria detalhamentoPortaria : this.detalhamentoPortarias){
            this.cargaHorariaSemanal = TimeOperations.tratarSomatorioCargaHoraria(this.cargaHorariaSemanal, detalhamentoPortaria.getCargaHorariaSemanal());
        }
    }

}
