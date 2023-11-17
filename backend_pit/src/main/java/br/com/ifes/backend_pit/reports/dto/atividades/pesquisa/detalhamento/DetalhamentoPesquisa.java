package br.com.ifes.backend_pit.reports.dto.atividades.pesquisa.detalhamento;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetalhamentoPesquisa {
    String tituloAcao;
    String tipoAcao;
    String numeroCadastro;
    String tipoAtuacao;
    String chSemanal;

}
