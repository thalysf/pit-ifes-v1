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
@RequestMapping("api/aulas")
@AllArgsConstructor
@Validated
public class RespostaAulaController {

    private final UUID idAula = UUID.fromString("8a42d95b-21f5-4299-8d39-5d172b655ddb");
    private final PITService pitService;
    private final RespostaService respostaService;

    /**
     * Pega o professor pelo id do usuario
     * Pega o pit pelo Id informado e estoura erro caso o pit infomrado esteja encerrado
     * Cria o objeto aula e o relaciona com uma RespostaAtividade
     *
     * @param detalharComponenteCurricularDto
     * @return
     */
    @PostMapping("respostaAula")
    public DetalhamentoComponenteCurricular criarRespostaAula(@RequestBody DetalharComponenteCurricularDto detalharComponenteCurricularDto) {
        PIT pit = pitService.getPitResponder(detalharComponenteCurricularDto.getIdPit());
        return respostaService.criarRespostaComponenteCurricularAtividade(detalharComponenteCurricularDto.getComponenteCurricular(), pit, detalharComponenteCurricularDto.getCargaHorariaSemanal(), idAula);
    }

    @PutMapping("respostaAula/{idDetalhamento}")
    public DetalhamentoComponenteCurricular atualizarRespostaAula(@RequestBody DetalharComponenteCurricularDto detalharComponenteCurricularDto, @PathVariable UUID idDetalhamento) {
        pitService.getPitResponder(detalharComponenteCurricularDto.getIdPit());
        return respostaService.atualizarDetalhamentoComponenteCurricular(detalharComponenteCurricularDto.getComponenteCurricular(), idDetalhamento, detalharComponenteCurricularDto.getCargaHorariaSemanal());
    }

    @GetMapping("respostaAulas")
    public List<DetalhamentoComponenteCurricular> getRespostaAulas(@RequestParam UUID idPit) {
        return respostaService.getRespostasAtividade(idPit, idAula);
    }

    @GetMapping("componentes_nao_detalhados/{idPit}")
    public List<ComponenteCurricular> getDetalhamento(@PathVariable UUID idPit) {
        return respostaService.getComponentesNaoDetalhados(idPit, idAula);
    }


    @GetMapping("respostaAulas/{idResposta}")
    public RespostaAtividade getRespostaAula(@PathVariable UUID idResposta) {
        return respostaService.getRespostaAtividade(idResposta);
    }

    @DeleteMapping("detalhamentos/{idDetalhamento}")
    public void deletarRespostaAula(@PathVariable UUID idDetalhamento) {
        respostaService.deletarDetalhamentoComponenteCurricular(idDetalhamento);
    }
}
