package br.com.ifes.backend_pit.reports.dto.atividades.ensino.planejamento_manutencao_ensino;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanejamentoManutencaoEnsino {
    String atividade;
    String chSemanal;
}
