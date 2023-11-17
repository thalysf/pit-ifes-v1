package br.com.ifes.backend_pit.controllers.atividades.resposta;

import br.com.ifes.backend_pit.exception.BusinessException;
import br.com.ifes.backend_pit.models.atividades.Atividade;
import br.com.ifes.backend_pit.models.atividades.resposta.RespostaAtividade;
import br.com.ifes.backend_pit.models.pit.PIT;
import br.com.ifes.backend_pit.services.atividades.resposta.RespostaService;
import br.com.ifes.backend_pit.services.pit.PITService;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

import static br.com.ifes.backend_pit.constants.ErrorConstants.PIT_ERROS.PIT_EM_ANDAMENTO_NAO_ENCONTRADO;

@RestController
@RequestMapping("api/respostas/outras")
@AllArgsConstructor
@Validated
public class RespostaOutrasController {
    private final RespostaService respostaService;

    private final PITService pitService;

    /**
     * Listar todas as Atividades que possuam tipo_detalhamento NENHUM
     * Listar todas as Atividades que possuam tipo_detalhamento PROJETO e que possuam ao menos um projeto em que o professor participa
     * Listar todas as Atividades que possuam tipo_detalhamento PORTARIAS e que possuam ao menos uma portaria em que o professor participa
     * Listar todas as Atividades que possuam tipo_detalhamento ALUNO
     * Listar todas as Atividades que possuam tipo_detalhamento AULA
     * Filtrar todas as atividades pelo tipo_atividade OUTRAS_ATIVIDADES
     * Filtrar por projetos/portarias que a data início vigência é menor ou igual a data atual
     * Filtrar por projetos/portarias que a data fim vigência é maior ou igual a data atual
     *
     * @return List<Atividade>
     */
    @GetMapping("atividades")
    public List<Atividade> listarAtividadesProfessorParticipa(@RequestParam("idUsuario") UUID idUsuario, @RequestParam("idPit") UUID idPIt) {
        PIT pit = pitService.getPitById(idPIt);
        return respostaService.listarAtividadesOutrasProfessorParticipa(pit, idUsuario);
    }

    @GetMapping()
    public List<RespostaAtividade> listarRespostaAtividadesOutrasAtividades(@RequestParam UUID idPit) {
        return respostaService.listarRespostaAtividadesOutrasAtividades(idPit);
    }

    /**
     * Resposta atividade para outras precisa conter
     * idPit
     * lista de detalhamento de portarias contendo apenas um detalhamento
     * idAtividade
     *
     * @param respostaAtividade
     * @return
     */
    @PostMapping()
    public RespostaAtividade cadastrarRespostaAtividade(@RequestBody RespostaAtividade respostaAtividade) {
        return respostaService.salvarRespostaOutras(respostaAtividade);
    }

}
