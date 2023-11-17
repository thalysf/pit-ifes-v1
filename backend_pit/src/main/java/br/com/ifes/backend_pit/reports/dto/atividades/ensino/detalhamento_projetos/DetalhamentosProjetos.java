package br.com.ifes.backend_pit.reports.dto.atividades.ensino.detalhamento_projetos;

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
public class DetalhamentosProjetos {
    List<DetalhamentoProjeto> detalhamentosProjetos = new ArrayList<>();
}
