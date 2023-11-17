package br.com.ifes.backend_pit.controllers.atividades.resposta;

import br.com.ifes.backend_pit.models.atividades.Atividade;
import br.com.ifes.backend_pit.models.atividades.detalhamento.DetalhamentoPortaria;
import br.com.ifes.backend_pit.models.atividades.resposta.RespostaAtividade;
import br.com.ifes.backend_pit.models.pit.PIT;
import br.com.ifes.backend_pit.services.atividades.AtividadesService;
import br.com.ifes.backend_pit.services.atividades.resposta.RespostaService;
import br.com.ifes.backend_pit.services.pit.PITService;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/respostas/gestao")
@AllArgsConstructor
@Validated
public class RespostaGestaoController {
    private final UUID idGestao = UUID.fromString("d1f16fa8-8318-4b2e-bace-ac101ad84291");
    private final RespostaService respostaService;
    private final AtividadesService atividadesService;

    @GetMapping("{idPit}/detalhamento_portarias")
    public List<DetalhamentoPortaria> obterRespostaPortariasAtividadeGestao(@PathVariable UUID idPit){
        return respostaService.getDetalhamentoPortariasResposta(idGestao, idPit);
    }

    /**
     * respostaAtividade precisa:
     * idPit
     * idAtividade
     * cargaHorariaSemanal caso o tipo detalhamento seja NENHUM
     *
     * @param respostaAtividade
     * @return
     */
    @PostMapping()
    public RespostaAtividade cadastrarRespostaAtividade(@RequestBody RespostaAtividade respostaAtividade) {
        Atividade atividade = atividadesService.getAtividade(this.idGestao);
        respostaAtividade.setAtividade(atividade);
        return respostaService.salvarRespostaAtividade(respostaAtividade);
    }
}
