package br.com.ifes.backend_pit.models.batch;

import br.com.ifes.backend_pit.enums.BatchTemplateEnum;
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
public class BatchTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idTemplate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private BatchTemplateEnum templateName;

    @Lob
    @Type(type="org.hibernate.type.BinaryType")
    private byte[] templateFile;

    public BatchTemplate() {

    }
}
