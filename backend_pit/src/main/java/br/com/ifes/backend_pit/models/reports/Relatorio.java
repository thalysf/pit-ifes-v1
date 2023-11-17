package br.com.ifes.backend_pit.models.reports;

import br.com.ifes.backend_pit.models.pit.PIT;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class Relatorio {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idRelatorio;

    @ManyToOne
    private PIT pit;

    @Lob
    @Type(type="org.hibernate.type.BinaryType")
    private byte[] relatorioExcel;

    public Relatorio() {

    }
}
