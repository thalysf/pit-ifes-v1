package br.com.ifes.backend_pit.models.cadastros;

import br.com.ifes.backend_pit.enums.TipoProjetoEnum;
import br.com.ifes.backend_pit.models.atividades.Atividade;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Builder
public class Projeto implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idProjeto;
    @NotBlank(message = "O título do projeto não pode ser vazio")
    private String tituloProjeto;

    @NotNull(message = "O tipo de projeto não pode ser nulo")
    @Enumerated(EnumType.STRING)
    private TipoProjetoEnum tipoProjeto;

    @NotBlank(message = "O tipo de ação não pode ser vazio")
    private String tipoAcao;

    @NotBlank(message = "O número de cadastro não pode ser vazio")
    private String numeroCadastro;

    @NotNull(message = "A carga horária mínima não pode ser nula")
    @Positive(message = "A carga horária mínima deve ser um valor positivo")
    private Double cargaHorariaMinima;

    @NotNull(message = "A carga horária máxima não pode ser nula")
    @Positive(message = "A carga horária máxima deve ser um valor positivo")
    private Double cargaHorariaMaxima;

    @NotNull(message = "A data de início de vigência não pode ser nula")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Timestamp dataInicioVigencia;

    @NotNull(message = "A data de fim de vigência não pode ser nula")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Timestamp dataFimVigencia;

    @OneToMany(mappedBy = "projeto", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnoreProperties("projeto")
    private List<ParticipacaoProjeto> participacaoProjetos;

    @ManyToMany(mappedBy = "projetos")
    @JsonIgnoreProperties({"projetos", "respostaAtividades"})
    private List<Atividade> atividades = new ArrayList<>();

    public Projeto() {

    }

    public Projeto(UUID idProjeto) {
        this.idProjeto = idProjeto;
    }
}
