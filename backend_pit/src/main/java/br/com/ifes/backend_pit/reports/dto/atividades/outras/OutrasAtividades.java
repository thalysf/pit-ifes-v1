package br.com.ifes.backend_pit.reports.dto.atividades.outras;

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
public class OutrasAtividades {
    private List<Outra> outrasAtividades = new ArrayList<>();
    private String subtotalParcial = String.valueOf(0);

    private List<Outra> outrasAtividadesBolsita = new ArrayList<>();
    private String subtotal = String.valueOf(0);
}