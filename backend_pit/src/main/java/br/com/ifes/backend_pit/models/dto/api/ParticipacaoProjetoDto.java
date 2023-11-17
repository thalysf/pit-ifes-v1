package br.com.ifes.backend_pit.models.dto.api;

import br.com.ifes.backend_pit.enums.TipoParticipacaoProjetoEnum;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
public class ParticipacaoProjetoDto {
    private UUID idProfessor;
}
