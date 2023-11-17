package br.com.ifes.backend_pit.reports.dto.atividades.ensino.apoio_ensino;

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
public class AtividadesApoioEnsino {
    List<ApoioEnsino> atividadesPlanejamentoManutencaoEnsino = new ArrayList<>();
    String subtotalParcial = String.valueOf(0);

    List<ApoioEnsino> atividadesPlanejamentoManutencaoEnsinoCoordenacao = new ArrayList<>();

    String subtotal = String.valueOf(0);
}
