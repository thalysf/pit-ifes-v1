package br.com.ifes.backend_pit.controllers.atividades.resposta;

import br.com.ifes.backend_pit.exception.BusinessException;
import br.com.ifes.backend_pit.models.atividades.Atividade;
import br.com.ifes.backend_pit.models.atividades.detalhamento.DetalhamentoAluno;
import br.com.ifes.backend_pit.models.atividades.detalhamento.DetalhamentoComponenteCurricular;
import br.com.ifes.backend_pit.models.atividades.resposta.RespostaAtividade;
import br.com.ifes.backend_pit.models.cadastros.ComponenteCurricular;
import br.com.ifes.backend_pit.models.pit.PIT;
import br.com.ifes.backend_pit.services.atividades.resposta.RespostaService;
import br.com.ifes.backend_pit.services.pit.PITService;
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

import java.util.List;
import java.util.UUID;

import static br.com.ifes.backend_pit.constants.ErrorConstants.PIT_ERROS.PIT_EM_ANDAMENTO_NAO_ENCONTRADO;

@RestController
@RequestMapping("api/respostas/pesquisa")
@AllArgsConstructor
@Validated
public class RespostaPesquisaController {
    private final RespostaService respostaService;

    private final PITService pitService;


    /**
     * Retorna as respostas das atividades de pesquisa do professor
     *
     * @param idPit
     * @return
     */
    @GetMapping()
    public List<RespostaAtividade> listarRespostaAtividadesPesquisa(@RequestParam UUID idPit) {
        return respostaService.listarRespostaAtividadesPesquisa(idPit);
    }

    /**
     * Listar todas as Atividades que possuam tipo_detalhamento NENHUM
     * Listar todas as Atividades que possuam tipo_detalhamento PROJETO e que possuam ao menos um projeto em que o professor participa
     * Listar todas as Atividades que possuam tipo_detalhamento PORTARIAS e que possuam ao menos uma portaria em que o professor participa
     * Listar todas as Atividades que possuam tipo_detalhamento ALUNO
     * Listar todas as Atividades que possuam tipo_detalhamento AULA
     * Filtrar todas as atividades pelo tipo_atividade ATIVIDADE_PESQUISA
     * Filtrar por projetos/portarias que a data início vigência é menor ou igual a data atual
     * Filtrar por projetos/portarias que a data fim vigência é maior ou igual a data atual
     *
     * @return List<Atividade>
     */
    @GetMapping("atividades")
    public List<Atividade> listarAtividadesProfessorParticipa(@RequestParam("idUsuario") UUID idUsuario, @RequestParam("idPit") UUID idPit) {
        PIT pit = pitService.getPitById(idPit);
        return respostaService.listarAtividadesPesquisaProfessorParticipa(pit, idUsuario);
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

    /**
     * Função utilizada para salvar um detalhamento aluno individualmente por atividade
     * @param idAtividade
     * @param idPit
     * @return
     */
    @PostMapping("detalhamentoAluno")
    public DetalhamentoAluno cadastrarDetalhamentoAlunoAtividade(@RequestParam UUID idAtividade, @RequestParam UUID idPit, @RequestBody DetalhamentoAluno detalhamentoAluno){
        return respostaService.salvarDetalhamentoAlunoAtividade(idAtividade, idPit, detalhamentoAluno);
    }

    @GetMapping("detalhamentoAlunos")
    public List<DetalhamentoAluno> listarDetalhamentoAlunosAtividade(@RequestParam UUID idAtividade, @RequestParam UUID idPit){
        return respostaService.listarDetalhamentoAlunosAtividade(idAtividade, idPit);
    }

    @DeleteMapping("detalhamentoAluno/{idDetalhamentoAluno}")
    public RespostaAtividade excluirDetalhamentoAluno(@PathVariable UUID idDetalhamentoAluno){
        return respostaService.excluirDetalhamentoAluno(idDetalhamentoAluno);
    }

    @GetMapping("aulas")
    public List<DetalhamentoComponenteCurricular> obterRespostasAulas(@RequestParam UUID idAtividade, @RequestParam UUID idPit) {
        return respostaService.obterRespostasAulas(idAtividade, idPit);
    }

    @GetMapping("componentes_nao_detalhados")
    public List<ComponenteCurricular> obterComponentesNaoDetalhados(@RequestParam UUID idPit, @RequestParam UUID idAtividade) {
        return respostaService.getComponentesNaoDetalhados(idPit, idAtividade);
    }
}
