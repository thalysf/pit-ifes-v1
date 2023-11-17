package br.com.ifes.backend_pit.reports.dto.atividades.extensao.atividade;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AtividadeExtensao {
    String atividade;
    String chSemanal;
}
