package br.com.ifes.backend_pit.reports.dto.atividades.ensino.aula;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Aula {
    String curso;
    String componenteCurricular;
    String chSemanal;
}
