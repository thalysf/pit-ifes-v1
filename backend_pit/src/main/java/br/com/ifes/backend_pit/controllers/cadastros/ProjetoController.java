package br.com.ifes.backend_pit.controllers.cadastros;

import br.com.ifes.backend_pit.models.cadastros.ParticipacaoProjeto;
import br.com.ifes.backend_pit.models.cadastros.Projeto;
import br.com.ifes.backend_pit.models.dto.api.EnumDto;
import br.com.ifes.backend_pit.models.dto.api.ParticipacaoProjetoDto;
import br.com.ifes.backend_pit.models.usuarios.Servidor;
import br.com.ifes.backend_pit.services.cadastros.ProjetoService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/projetos")
@RequiredArgsConstructor
@Validated
public class ProjetoController {
    private final ProjetoService projetoService;

    @GetMapping
    public List<Projeto> getProjetos() {
        return this.projetoService.getProjetos();
    }

    @GetMapping("{id}")
    public Projeto getProjeto(@PathVariable UUID id) {
        return this.projetoService.getProjeto(id);
    }

    @GetMapping("tipoProjeto")
    public List<EnumDto> getTipoAtividadeProjetoNameEnum() {
        return this.projetoService.listarTipoAtividadeProjetoNameEnum();
    }

    @PostMapping
    public Projeto criarProjeto(@RequestBody @Valid Projeto projeto) {
        return this.projetoService.criarProjeto(projeto);
    }

    @PutMapping("{id}")
    public Projeto atualizarProjeto(@PathVariable UUID id, @RequestBody @Valid Projeto projeto) {
        projeto.setIdProjeto(id);
        return this.projetoService.atualizarProjeto(projeto);
    }

    @DeleteMapping("{id}")
    public void deletarProjeto(@PathVariable UUID id) {
        this.projetoService.deletarProjeto(id);
    }


    @GetMapping("{idProjeto}/participantes")
    public List<ParticipacaoProjeto> listarParticipantes(@PathVariable UUID idProjeto) {
        return this.projetoService.listarParticipantes(idProjeto);
    }

    @GetMapping("{id_projeto}/professores_participantes")
    public List<ParticipacaoProjeto> listarProfessoresParticipantes(@PathVariable UUID id_projeto) {
        return this.projetoService.listarProfessoresParticipantes(id_projeto);
    }

    @GetMapping("{id_projeto}/professores_nao_participantes")
    public List<Servidor> listarProfessoresNaoParticipantes(@PathVariable UUID id_projeto) {
        return this.projetoService.listarProfessoresNaoParticipantes(id_projeto);
    }

    @GetMapping("tipo_participacao_projeto")
    public List<EnumDto> listarTiposParticipacaoProjeto() {
        return this.projetoService.listarTiposParticipacaoProjeto();
    }

    @PostMapping("{id_projeto}/participacao_projeto")
    public ParticipacaoProjeto criarAssociacaoProfessorProjeto(@PathVariable UUID id_projeto, @RequestBody @Valid ParticipacaoProjetoDto participacaoProjetoDto) {
        return this.projetoService.criarAssociacaoProfessorProjeto(id_projeto, participacaoProjetoDto.getIdProfessor());
    }

    @PutMapping("{id_projeto}/professores/{id_professor}/participacao_projeto")
    public ParticipacaoProjeto atualizarAssociacaoProfessorProjeto(@PathVariable UUID id_projeto, @PathVariable UUID id_professor, @RequestBody @Valid ParticipacaoProjetoDto participacaoProjetoDto) {
        return this.projetoService.atualizarAssociacaoProfessorProjeto(id_projeto, id_professor);
    }

    @DeleteMapping("{id_projeto}/professores/{id_professor}/participacao_projeto")
    public void deletarAssociacaoProfessorProjeto(@PathVariable UUID id_projeto, @PathVariable UUID id_professor) {
        this.projetoService.deletarAssociacaoProfessorProjeto(id_projeto, id_professor);
    }
}
