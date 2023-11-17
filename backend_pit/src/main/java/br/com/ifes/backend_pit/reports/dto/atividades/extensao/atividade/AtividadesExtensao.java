package br.com.ifes.backend_pit.reports.dto.atividades.extensao.atividade;

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
public class AtividadesExtensao {
    List<AtividadeExtensao> atividadesExtensao = new ArrayList<>();
    String subtotal = String.valueOf(0);
}
