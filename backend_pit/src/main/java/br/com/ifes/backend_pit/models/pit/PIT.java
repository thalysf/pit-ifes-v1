package br.com.ifes.backend_pit.models.pit;

import br.com.ifes.backend_pit.models.usuarios.Servidor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
public class PIT implements Serializable {

    private static final long serialVersionUID = 9084789396376488616L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idPIT;

    @Column(nullable = false)
    private String periodo;

    @Column(nullable = true)
    private Timestamp dataEntrega;

    private boolean aprovado;
    private boolean emRevisao;

    @ManyToOne
    private Servidor professor;

    private String observacoes;

    public PIT() {

    }
}
