package br.com.ifes.backend_pit.controllers.atividades.resposta;

import br.com.ifes.backend_pit.models.atividades.detalhamento.DetalhamentoPortaria;
import br.com.ifes.backend_pit.models.atividades.detalhamento.DetalhamentoProjeto;
import br.com.ifes.backend_pit.models.atividades.resposta.RespostaAtividade;
import br.com.ifes.backend_pit.services.atividades.resposta.RespostaService;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/respostas")
@AllArgsConstructor
@Validated
public class RespostaAtividadeController {

    private final RespostaService respostaService;

    /**
     * Retorna a lista de detalhamento de projetos de uma resposta da atividade de um PIT
     * @param idAtividade
     * @return
     */
    @GetMapping("{idAtividade}/pit/{idPit}/detalhamento_projetos")
    public List<DetalhamentoProjeto> getDetalhamentoProjetos(@PathVariable UUID idAtividade, @PathVariable UUID idPit){
        return respostaService.getDetalhamentoProjetosResposta(idAtividade, idPit);
    }

    /**
     * Retorna a lista de detalhamento de portarias de uma resposta da atividade do PIT
     */
    @GetMapping("{idAtividade}/pit/{idPit}/detalhamento_portarias")
    public List<DetalhamentoPortaria> getDetalhamentoPortarias(@PathVariable UUID idAtividade, @PathVariable UUID idPit){
        return respostaService.getDetalhamentoPortariasResposta(idAtividade, idPit);
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
        return respostaService.salvarRespostaAtividade(respostaAtividade);
    }


    @DeleteMapping("detalhamento_projetos/{idDetalhamentoProjeto}")
    public void excluirDetalhamentoProjeto(@PathVariable UUID idDetalhamentoProjeto){
        respostaService.excluirDetalhamentoProjeto(idDetalhamentoProjeto);
    }
}
