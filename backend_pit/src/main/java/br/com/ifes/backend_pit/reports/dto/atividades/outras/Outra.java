package br.com.ifes.backend_pit.reports.dto.atividades.outras;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Outra {
    private String tipo;
    private String numeroPortaria;
    private String chSemanal;
}
