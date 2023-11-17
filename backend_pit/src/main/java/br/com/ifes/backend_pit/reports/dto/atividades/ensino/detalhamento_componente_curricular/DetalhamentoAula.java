package br.com.ifes.backend_pit.reports.dto.atividades.ensino.detalhamento_componente_curricular;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetalhamentoAula {
    String curso;
    String componenteCurricular;
    String chSemanal;
}
