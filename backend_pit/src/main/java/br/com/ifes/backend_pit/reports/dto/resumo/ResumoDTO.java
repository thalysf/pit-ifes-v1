package br.com.ifes.backend_pit.reports.dto.resumo;

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
public class ResumoDTO {
    List<String> cargasHorarias = new ArrayList<>(); // cada item corresponde a uma carga horária, ex: 1. Atividades de Ensino, corresponde ao index 0 e assim em diante.
}
