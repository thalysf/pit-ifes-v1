package br.com.ifes.backend_pit.controllers.atividades;

import br.com.ifes.backend_pit.enums.TipoAtividadeEnum;
import br.com.ifes.backend_pit.models.atividades.Atividade;
import br.com.ifes.backend_pit.models.cadastros.ParticipacaoProjeto;
import br.com.ifes.backend_pit.models.cadastros.Portaria;
import br.com.ifes.backend_pit.models.cadastros.Projeto;
import br.com.ifes.backend_pit.models.dto.api.AssociarDto;
import br.com.ifes.backend_pit.models.dto.api.EnumDto;
import br.com.ifes.backend_pit.models.pit.PIT;
import br.com.ifes.backend_pit.models.usuarios.Servidor;
import br.com.ifes.backend_pit.services.atividades.AtividadesService;
import br.com.ifes.backend_pit.services.cadastros.ProjetoService;
import br.com.ifes.backend_pit.services.pit.PITService;
import br.com.ifes.backend_pit.services.usuarios.ServidorService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

import static br.com.ifes.backend_pit.enums.TipoAtividadeEnum.*;

@RestController
@RequestMapping("api/atividades")
@RequiredArgsConstructor
@Validated
public class AtividadesController {
    private final AtividadesService atividadesService;
    private final ServidorService servidorService;
    private final PITService pitService;
    private final ProjetoService projetoService;

    private final UUID idGestao = UUID.fromString("d1f16fa8-8318-4b2e-bace-ac101ad84291");
    private final UUID idRepresentacao = UUID.fromString("29d23456-0ca7-4072-a837-46bc101ebb15");

    @GetMapping("tipoDetalhamento")
    public List<EnumDto> getTipoDetalhamentoEnum() {
        return atividadesService.listarTipoDetalhamentoEnum();
    }

    /*************
     *   Ensino  *
     ************/

    @GetMapping("apoioEnsino")
    public List<Atividade> getAtividadesEnsino() {
        return atividadesService.getAtividadesByTipo(ATIVIDADE_APOIO_ENSINO);
    }

    @GetMapping("apoioEnsino/tipoDetalhamento")
    public List<EnumDto> getTipoDetalhamentoApoioEnsino() {
        return atividadesService.listarTipoDetalhamentoApoioEnsino();
    }

    @PostMapping("apoioEnsino")
    @PreAuthorize("hasAuthority('DIRETOR')")
    public Atividade cadastrarAtividadeApoioEnsino(@RequestBody @Valid Atividade atividade) {
        return atividadesService.cadastrarAtividade(atividade, ATIVIDADE_APOIO_ENSINO);
    }

    /**
     * Metodo para atualizar qualquer tipo de atividade
     *
     * @param tipoAtividade
     * @param idAtividade
     * @param atividade
     * @return
     */
    @PutMapping("{tipoAtividade}/{idAtividade}")
    @PreAuthorize("hasAuthority('DIRETOR')")
    public Atividade editarAtividadeApoioEnsino(@PathVariable String tipoAtividade, @PathVariable UUID idAtividade, @RequestBody @Valid Atividade atividade) {
        return atividadesService.atualizarAtividade(idAtividade, atividade);
    }

    /***************
     *   Pesquisa  *
     ***************/
    @GetMapping("pesquisa")
    public List<Atividade> getAtividadesPesquisa() {
        return atividadesService.getAtividadesByTipo(ATIVIDADE_PESQUISA);
    }

    @GetMapping("pesquisa/tipoDetalhamento")
    public List<EnumDto> getTipoDetalhamentoPesquisa() {
        return atividadesService.listarTipoDetalhamentoPesquisa();
    }

    @PostMapping("pesquisa")
    @PreAuthorize("hasAuthority('DIRETOR')")
    public Atividade cadastrarAtividadePesquisa(@RequestBody @Valid Atividade atividade) {
        return atividadesService.cadastrarAtividade(atividade, ATIVIDADE_PESQUISA);
    }

    /****************
     *   Extensão   *
     ***************/
    @GetMapping("extensao")
    public List<Atividade> getAtividadesExtensao() {
        return atividadesService.getAtividadesByTipo(ATIVIDADE_EXTENSAO);
    }

    @GetMapping("extensao/tipoDetalhamento")
    public List<EnumDto> getTipoDetalhamentoExtensao() {
        return atividadesService.listarTipoDetalhamentoExtensao();
    }

    @PostMapping("extensao")
    @PreAuthorize("hasAuthority('DIRETOR')")
    public Atividade cadastrarAtividadeExtensao(@RequestBody @Valid Atividade atividade) {
        return atividadesService.cadastrarAtividade(atividade, ATIVIDADE_EXTENSAO);
    }


    /****************
     *   Gestão     *
     ***************/
    @GetMapping("gestao")
    public List<Atividade> getAtividadesGestao() {
        return atividadesService.getAtividadesByTipo(ATIVIDADE_GESTAO);
    }

    @PostMapping("gestao")
    @PreAuthorize("hasAuthority('DIRETOR')")
    public Atividade cadastrarAtividadeGestao(@RequestBody @Valid Atividade atividade) {
        return atividadesService.cadastrarAtividade(atividade, ATIVIDADE_GESTAO);
    }

    /********************
     *   Representação  *
     ********************/
    @GetMapping("representacao")
    public List<Atividade> getAtividadesRepresentacao() {
        return atividadesService.getAtividadesByTipo(ATIVIDADE_REPRESENTACAO);
    }

    @PostMapping("representacao")
    @PreAuthorize("hasAuthority('DIRETOR')")
    public Atividade cadastrarAtividadeRepresentacao(@RequestBody @Valid Atividade atividade) {
        return atividadesService.cadastrarAtividade(atividade, ATIVIDADE_REPRESENTACAO);
    }


    /************************
     *   Outras Atividades  *
     ************************/
    @GetMapping("outras")
    public List<Atividade> getOutrasAtividades() {
        return atividadesService.getAtividadesByTipo(OUTRAS_ATIVIDADES);
    }

    @PostMapping("outras")
    @PreAuthorize("hasAuthority('DIRETOR')")
    public Atividade cadastrarOutraAtividade(@RequestBody @Valid Atividade atividade) {
        return atividadesService.cadastrarAtividade(atividade, OUTRAS_ATIVIDADES);
    }


    /*************
     *   Geral  *
     ************/

    @GetMapping("{tipoAtividade}/{idAtividade}")
    public Atividade getAtividade(@PathVariable String tipoAtividade, @PathVariable UUID idAtividade) {
        return atividadesService.getAtividade(idAtividade);
    }

    @PostMapping("{idAtividade}/projetos")
    @PreAuthorize("hasAuthority('DIRETOR')")
    public void vincularAtividadeProjeto(@PathVariable UUID idAtividade,
                                         @RequestBody @Valid AssociarDto associarAtividadeProjetoDto) {
        atividadesService.vincularAtividadeProjeto(idAtividade, associarAtividadeProjetoDto);
    }

    @DeleteMapping("{tipoAtividade}/{idAtividade}")
    public void deletarAtividade(@PathVariable UUID idAtividade) {
        this.atividadesService.deletarAtividade(idAtividade);
    }

    /**
     * Usada pelo diretor
     * Diretor cadastra e lista os projetos
     *
     * @param idAtividade
     * @return
     */
    @GetMapping("{idAtividade}/projetos")
    public List<Projeto> recuperarVinculoAtividadeProjeto(@PathVariable UUID idAtividade) {
        return atividadesService.recuperarVinculoAtividadeProjeto(idAtividade);
    }

    @PostMapping("{idAtividade}/portarias/{tipoAtividade}")
    @PreAuthorize("hasAuthority('DIRETOR')")
    public void vincularAtividadePortaria(@PathVariable UUID idAtividade,
                                          @RequestBody @Valid AssociarDto associarAtividadePortariaDto) {
        atividadesService.vincularAtividadePortarias(idAtividade, associarAtividadePortariaDto);
    }

    @GetMapping("{idAtividade}/portarias/apoioEnsino")
    @PreAuthorize("hasAuthority('DIRETOR')")
    public List<Portaria> getPortariasApoioEnsinoAtividade(@PathVariable UUID idAtividade) {
        return atividadesService.recuperarAtividadePortaria(idAtividade);
    }

    @GetMapping("{idAtividade}/portarias/pesquisa")
    @PreAuthorize("hasAuthority('DIRETOR')")
    public List<Portaria> getPortariasPesquisaAtividade(@PathVariable UUID idAtividade) {
        return atividadesService.recuperarAtividadePortaria(idAtividade);
    }

    @GetMapping("{idAtividade}/portarias/extensao")
    @PreAuthorize("hasAuthority('DIRETOR')")
    public List<Portaria> getPortariasExtensaoAtividade(@PathVariable UUID idAtividade) {
        return atividadesService.recuperarAtividadePortaria(idAtividade);
    }

    @GetMapping("{idAtividade}/portarias/outras")
    @PreAuthorize("hasAuthority('DIRETOR')")
    public List<Portaria> getPortariasOutrasAtividades(@PathVariable UUID idAtividade) {
        return atividadesService.recuperarAtividadePortaria(idAtividade);
    }

    /**
     * Retorna as portarias que estão vinculadas a uma atividade
     * Filtrando pelas portarias em que esse professor está vinculado
     * Função usada por todas as telas de atividades do pit em que há detalhamento de portaria
     *
     * @param idAtividade
     * @param idUsuario
     * @return
     */
    @GetMapping("{idAtividade}/portarias/{idUsuario}")
    public List<Portaria> recuperarPortariasAtividadeProfessor(@PathVariable UUID idAtividade, @PathVariable UUID idUsuario) {
        Servidor servidor = this.servidorService.getServidorByUserId(idUsuario);
        return atividadesService.recuperarPortariasAtividadeProfessor(idAtividade, servidor.getIdServidor());
    }

    /**
     * Função usada pelo professor para listar o detalhamento de projetos de uma atividade na tela de responder
     * Retorna apenas as portarias em que o professor está associado
     *
     * @param idAtividade
     * @return
     */
    @GetMapping("{idAtividade}/projetos/{idUsuario}")
    public List<ParticipacaoProjeto> recuperarProjetosAtividadeProfessor(@PathVariable UUID idAtividade, @PathVariable UUID idUsuario) {
        Servidor servidor = this.servidorService.getServidorByUserId(idUsuario);
        return atividadesService.recuperarProjetosAtividadeProfessor(idAtividade, servidor.getIdServidor());
    }

    /**
     * Retorna as portarias que estão vinculadas a atividade de gestão do pit do professor
     * Filtrando pelas portarias em que esse professor está vinculado
     * Função usada por todas as telas de atividades do pit em que há detalhamento de portaria
     */
    @GetMapping("gestao/portarias/{idPit}")
    public List<Portaria> recuperarPortariasAtividadeGestaoDoPit(@PathVariable UUID idPit) {
        PIT pit = this.pitService.getPitById(idPit);
        return atividadesService.recuperarPortariasAtividadeProfessor(this.idGestao, pit.getProfessor().getIdServidor());
    }

    /**
     * Retorna as portarias que estão vinculadas a atividade de representacao do pit do professor
     * Filtrando pelas portarias em que esse professor está vinculado
     * Função usada por todas as telas de atividades do pit em que há detalhamento de portaria
     */
    @GetMapping("representacao/portarias/{idPit}")
    public List<Portaria> recuperarPortariasAtividadeRepresentacaoDoPit(@PathVariable UUID idPit) {
        PIT pit = this.pitService.getPitById(idPit);
        return atividadesService.recuperarPortariasAtividadeProfessor(this.idRepresentacao, pit.getProfessor().getIdServidor());
    }

    /**
     * Usada pelo diretor para listar os projetos que não estão associados a uma atividade ainda
     * Na tela de formulario da atividade, de pesqusia e extensão
     */
    @GetMapping("{idAtividade}/{tipoAtividade}/projetos_nao_associados")
    public List<Projeto> recuperarProjetosNaoAssociadosAtividade(@PathVariable UUID idAtividade, @PathVariable TipoAtividadeEnum tipoAtividade) {
        return atividadesService.getProjetosPeloTipoEIdAtividadeNaoPresenteNoProjeto(idAtividade, tipoAtividade);
    }

    @PostMapping("{idAtividade}/projetos/{idProjeto}")
    public void associarProjetoAtividade(@PathVariable UUID idAtividade, @PathVariable UUID idProjeto) {
        Projeto projeto = projetoService.getProjeto(idProjeto);
        atividadesService.associarProjetoAtividade(idAtividade, projeto);
    }

    @DeleteMapping("{idAtividade}/projetos/{idProjeto}")
    public void removerProjetoAtividade(@PathVariable UUID idAtividade, @PathVariable UUID idProjeto) {
        atividadesService.removerProjetoAtividade(idAtividade, idProjeto);
    }
}
