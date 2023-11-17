package br.com.ifes.backend_pit.reports.dto.atividades.ensino.apoio_ensino;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApoioEnsino {
    String atividade;
    String chSemanal;
}
