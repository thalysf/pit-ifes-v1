package br.com.ifes.backend_pit.models.atividades;

import br.com.ifes.backend_pit.enums.TipoAtividadeEnum;
import br.com.ifes.backend_pit.enums.TipoDetalhamentoEnum;
import br.com.ifes.backend_pit.models.atividades.resposta.RespostaAtividade;
import br.com.ifes.backend_pit.models.cadastros.Portaria;
import br.com.ifes.backend_pit.models.cadastros.Projeto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static br.com.ifes.backend_pit.constants.ErrorConstants.ATIVIDADE_ERROS.ATIVIDADE_SEM_NOME;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
public class Atividade implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true)
    private UUID idAtividade;

    private Integer numeroOrdem;

    @Column(columnDefinition = "boolean default false")
    private Boolean abaixoDoSubTotal;

    @Enumerated(EnumType.STRING)
    private TipoAtividadeEnum tipoAtividade;

    @NotBlank(message = ATIVIDADE_SEM_NOME)
    private String nomeAtividade;

    private Double cargaHorariaMinima;

    private Double cargaHorariaMaxima;

    @Enumerated(EnumType.STRING)
    private TipoDetalhamentoEnum tipoDetalhamento;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "atividade")
    @JsonIgnoreProperties("atividade")
    private List<Portaria> portarias;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "atividade_projeto",
            joinColumns = @JoinColumn(name = "id_atividade"),
            inverseJoinColumns = @JoinColumn(name = "id_projeto")
    )
    @JsonIgnoreProperties("atividade")
    private List<Projeto> projetos;

    @OneToMany(mappedBy = "atividade", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("atividade")
    @JsonIgnore
    private List<RespostaAtividade> respostaAtividades = new ArrayList<>();

    public Atividade() {
    }

    public void addProjeto(Projeto projeto) {
        if (this.projetos == null) {
            this.projetos = new ArrayList<>();
        }
        this.projetos.add(projeto);
    }

    public void addPortaria(Portaria portaria) {
        if (this.portarias == null) {
            this.portarias = new ArrayList<>();
        }
        this.portarias.add(portaria);
    }

}
