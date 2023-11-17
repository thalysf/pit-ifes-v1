package br.com.ifes.backend_pit.reports.dto.atividades.representacao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Representacao {
    private String descricaoAtribuicao;
    private String numeroPortaria;
    private String dataInicio;
    private String periodoVigencia;
    private String chSemanal;
}
