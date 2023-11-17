package br.com.ifes.backend_pit.reports.dto.atividades.gestao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Gestao {
    String descricaoAtribuicao;
    String numeroPortaria;
    String dataInicio;
    String periodoVigencia;
    String chSemanal;
}
