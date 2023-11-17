package br.com.ifes.backend_pit.reports.dto.atividades.extensao.detalhamento;

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
public class DetalhamentosExtensao {
    List<DetalhamentoExtensao> detalhamentosExtensao = new ArrayList<>();
}
