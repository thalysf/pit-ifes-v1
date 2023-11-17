package br.com.ifes.backend_pit.reports.dto.cadastro;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DadosCadastraisDTO {
    String campus;
    String departamento;
    String nome;
    String siape;
    String jornadaTrabalho;
    String efetivo;
    String emAfastamento;
    String areaPrincipalAtuacao;
    String titulacao;
}
