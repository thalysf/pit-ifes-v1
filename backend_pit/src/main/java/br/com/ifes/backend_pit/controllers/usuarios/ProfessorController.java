package br.com.ifes.backend_pit.controllers.usuarios;

import br.com.ifes.backend_pit.models.usuarios.Servidor;
import br.com.ifes.backend_pit.services.usuarios.ProfessorService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("api/professores")
@RequiredArgsConstructor
public class ProfessorController {

    private final ProfessorService professorService;

    @GetMapping
    @PreAuthorize("hasAuthority('DIRETOR')")
    public List<Servidor> getProfessores() {
        return this.professorService.getProfessores();
    }

    @GetMapping(value = "{id}")
    @PreAuthorize("hasAuthority('DIRETOR')")
    public Servidor getProfessor(@PathVariable UUID id) {
        return this.professorService.getProfessor(id);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('DIRETOR')")
    public Servidor createProfessor(@Valid @RequestBody Servidor professor) {
        if(!professor.isEfetivo()) professor.setEfetivo(false);
        if(!professor.isAdministrador()) professor.setAdministrador(false);
        if(!professor.isPossuiAfastamento()) professor.setPossuiAfastamento(false);
        return this.professorService.saveProfessor(professor);
    }

    @PutMapping(value = "{id}")
    @PreAuthorize("hasAuthority('DIRETOR')")
    public Servidor atualizarProfessor(@PathVariable UUID id, @Valid @RequestBody Servidor professor) {
        professor.setIdServidor(id);
        if(!professor.isEfetivo()) professor.setEfetivo(false);
        if(!professor.isAdministrador()) professor.setAdministrador(false);
        if(!professor.isPossuiAfastamento()) professor.setPossuiAfastamento(false);
        return this.professorService.updateProfessor(professor);
    }

    @DeleteMapping(value = "{id}")
    @PreAuthorize("hasAuthority('DIRETOR')")
    public void deletarProfessor(@PathVariable UUID id) {
        this.professorService.deleteProfessor(id);
    }
}
