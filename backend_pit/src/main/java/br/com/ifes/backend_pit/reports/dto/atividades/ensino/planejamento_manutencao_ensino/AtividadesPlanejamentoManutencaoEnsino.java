package br.com.ifes.backend_pit.reports.dto.atividades.ensino.planejamento_manutencao_ensino;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AtividadesPlanejamentoManutencaoEnsino {
    List<PlanejamentoManutencaoEnsino> atividadesPlanejamentoManutencaoEnsino = new ArrayList<>();
    String subtotal = String.valueOf(0);
}
