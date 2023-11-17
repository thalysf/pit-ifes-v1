package br.com.ifes.backend_pit.models.cadastros;

import br.com.ifes.backend_pit.models.usuarios.Servidor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class ParticipacaoProjeto implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idParticipacaoProjeto;

    @ManyToOne
    private Servidor professor;

    @ManyToOne
    private Projeto projeto;
}
