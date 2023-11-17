package br.com.ifes.backend_pit.controllers.atividades.resposta;

import br.com.ifes.backend_pit.models.atividades.detalhamento.DetalhamentoComponenteCurricular;
import br.com.ifes.backend_pit.models.atividades.resposta.RespostaAtividade;
import br.com.ifes.backend_pit.models.cadastros.ComponenteCurricular;
import br.com.ifes.backend_pit.models.dto.api.DetalharComponenteCurricularDto;
import br.com.ifes.backend_pit.models.pit.PIT;
import br.com.ifes.backend_pit.services.atividades.resposta.RespostaService;
import br.com.ifes.backend_pit.services.pit.PITService;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/mediacaoPedagogica")
@AllArgsConstructor
@Validated
public class RespostaMediacaoPedagogicaController {
    private final UUID idMediacaoPedagogica = UUID.fromString("ae280b35-8eaf-488f-9570-eb0805debdec");
    private final PITService pitService;
    private final RespostaService respostaService;


    @PostMapping("resposta")
    public DetalhamentoComponenteCurricular criarResposta(@RequestBody DetalharComponenteCurricularDto detalharComponenteCurricularDto) {
        PIT pit = pitService.getPitResponder(detalharComponenteCurricularDto.getIdPit());
        return respostaService.criarRespostaComponenteCurricularAtividade(detalharComponenteCurricularDto.getComponenteCurricular(), pit, detalharComponenteCurricularDto.getCargaHorariaSemanal(), idMediacaoPedagogica);
    }

    @PutMapping("resposta/{idDetalhamento}")
    public DetalhamentoComponenteCurricular atualizarRespostaAula(@RequestBody DetalharComponenteCurricularDto detalharComponenteCurricularDto, @PathVariable UUID idDetalhamento) {
        pitService.getPitResponder(detalharComponenteCurricularDto.getIdPit());
        return respostaService.atualizarDetalhamentoComponenteCurricular(detalharComponenteCurricularDto.getComponenteCurricular(), idDetalhamento, detalharComponenteCurricularDto.getCargaHorariaSemanal());
    }

    @GetMapping("respostas")
    public List<DetalhamentoComponenteCurricular> getRespostaAulas(@RequestParam UUID idPit) {
        return respostaService.getRespostasAtividade(idPit, idMediacaoPedagogica);
    }

    @GetMapping("componentes_nao_detalhados/{idPit}")
    public List<ComponenteCurricular> getDetalhamento(@PathVariable UUID idPit) {
        return respostaService.getComponentesNaoDetalhados(idPit, idMediacaoPedagogica);
    }


    @GetMapping("resposta/{idResposta}")
    public RespostaAtividade getRespostaAula(@PathVariable UUID idResposta) {
        return respostaService.getRespostaAtividade(idResposta);
    }

    @DeleteMapping("detalhamentos/{idDetalhamento}")
    public void deletarRespostaAula(@PathVariable UUID idDetalhamento) {
        respostaService.deletarDetalhamentoComponenteCurricular(idDetalhamento);
    }
}
