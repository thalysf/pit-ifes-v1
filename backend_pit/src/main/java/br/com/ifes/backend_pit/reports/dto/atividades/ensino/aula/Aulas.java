package br.com.ifes.backend_pit.reports.dto.atividades.ensino.aula;

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
public class Aulas {
    List<Aula> aulas = new ArrayList<>();
    String subtotal = String.valueOf(0);
}
