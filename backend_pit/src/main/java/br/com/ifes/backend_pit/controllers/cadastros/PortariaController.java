package br.com.ifes.backend_pit.controllers.cadastros;

import br.com.ifes.backend_pit.models.cadastros.ParticipacaoProjeto;
import br.com.ifes.backend_pit.models.cadastros.Portaria;
import br.com.ifes.backend_pit.models.dto.api.AssociarDto;
import br.com.ifes.backend_pit.models.dto.api.EnumDto;
import br.com.ifes.backend_pit.models.usuarios.Servidor;
import br.com.ifes.backend_pit.services.cadastros.PortariaService;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("api/portarias")
@RequiredArgsConstructor
public class PortariaController {
    private final PortariaService portariaService;

    @GetMapping
    public List<Portaria> getPortarias() {
        return this.portariaService.getPortarias();
    }

    @GetMapping("apoioEnsino")
    public List<Portaria> obterPortariasApoioEnsino(){
        return this.portariaService.getPortariasApoioEnsino();
    }

    @GetMapping("pesquisa")
    public List<Portaria> obterPortariasPesquisa(){
        return this.portariaService.getPortariasPesquisa();
    }

    @GetMapping("extensao")
    public List<Portaria> obterPortariasExtensao(){
        return this.portariaService.getPortariasExtensao();
    }

    @GetMapping("outras")
    public List<Portaria> obterPortariasOutrasAtividades(){
        return this.portariaService.getPortariasOutrasAtividades();
    }

    @GetMapping(value = "{id}")
    public Portaria getPortaria(@PathVariable UUID id) {
        return this.portariaService.getPortaria(id);
    }

    @PostMapping
    public Portaria createPortaria(@Valid @RequestBody Portaria Portaria) {
        return this.portariaService.savePortaria(Portaria);
    }

    @PutMapping(value = "{id}")
    public Portaria atualizarPortaria(@PathVariable UUID id, @Valid @RequestBody Portaria Portaria) {
        Portaria.setIdPortaria(id);
        return this.portariaService.updatePortaria(Portaria);
    }

    @DeleteMapping(value = "{id}")
    public void deletarPortaria(@PathVariable UUID id) {
        this.portariaService.deletePortaria(id);
    }

    @GetMapping("tipoAtividade")
    public List<EnumDto> getTipoAtividades() {
        return this.portariaService.getTipoAtividades();
    }

    @GetMapping(value = "{idPortaria}/professores")
    public List<Servidor> getProfessoresPortaria(@PathVariable UUID idPortaria) {
        return this.portariaService.getProfessoresPortaria(idPortaria);
    }

    @PostMapping(value = "{idPortaria}/professores")
    public void salvarProfessoresPortaria(@PathVariable UUID idPortaria, @RequestBody AssociarDto associarDto) {
        this.portariaService.salvarProfessoresPortaria(idPortaria, associarDto.getEntidades());
    }

    @GetMapping("{idPortaria}/professores_nao_participantes")
    public List<Servidor> listarProfessoresNaoParticipantes(@PathVariable UUID idPortaria) {
        return this.portariaService.listarProfessoresNaoParticipantes(idPortaria);
    }

    @PostMapping("{idPortaria}/professores/{idProfessor}")
    public Servidor inserirProfessorNaPortaria(@PathVariable UUID idPortaria, @PathVariable UUID idProfessor){
        return this.portariaService.inserirProfessorNaPortaria(idPortaria, idProfessor);
    }

    @DeleteMapping("{idPortaria}/professores/{idProfessor}")
    public void removerProfessorDaPortaria(@PathVariable UUID idPortaria, @PathVariable UUID idProfessor){
        this.portariaService.removerProfessorDaPortaria(idPortaria, idProfessor);
    }
}
