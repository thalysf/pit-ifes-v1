package br.com.ifes.backend_pit.models.dto.api;

import br.com.ifes.backend_pit.models.cadastros.ComponenteCurricular;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class DetalharComponenteCurricularDto {
    private Double cargaHorariaSemanal;
    private ComponenteCurricular componenteCurricular;
    private UUID idPit;
}
