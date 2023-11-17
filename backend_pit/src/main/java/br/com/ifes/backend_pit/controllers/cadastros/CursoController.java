package br.com.ifes.backend_pit.controllers.cadastros;

import br.com.ifes.backend_pit.models.cadastros.ComponenteCurricular;
import br.com.ifes.backend_pit.models.cadastros.Curso;
import br.com.ifes.backend_pit.services.cadastros.ComponenteCurricularService;
import br.com.ifes.backend_pit.services.cadastros.CursoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/cursos")
@RequiredArgsConstructor
public class CursoController {

    private final CursoService cursoService;
    private final ComponenteCurricularService componenteCurricularService;

    @GetMapping
    public List<Curso> getCursos() {
        return this.cursoService.getCursos();
    }

    @GetMapping("{id}")
    public Curso getCurso(@PathVariable UUID id) {
        return this.cursoService.getCurso(id);
    }

    @PostMapping
    public Curso criarCurso(@Valid @RequestBody Curso curso) {
        return this.cursoService.salvarCurso(curso);
    }

    @DeleteMapping("{id}")
    public void deletarCurso(@PathVariable UUID id) {
        this.cursoService.deletarCurso(id);
    }

    @PutMapping("{id}")
    public Curso editarCurso(@PathVariable UUID id, @Valid @RequestBody Curso curso) {
        curso.setIdCurso(id);
        return this.cursoService.salvarCurso(curso);
    }

    @GetMapping("{idCurso}/componentesCurriculares")
    public List<ComponenteCurricular> getComponentesCurriculares(@PathVariable UUID idCurso) {
        return this.componenteCurricularService.listarComponentesCurso(idCurso);
    }

    @GetMapping("componentesCurriculares")
    public List<ComponenteCurricular> getComponentesCurriculares(@RequestParam(required = false) String nome, @RequestParam(required = false) String nome_curso) {
        if(nome != null && nome_curso != null){
            return this.componenteCurricularService.listarComponentesCurriculares(nome, nome_curso);
        } else if (nome != null){
            return this.componenteCurricularService.listarComponentesCurriculares(nome);
        } else {
            return this.componenteCurricularService.listarComponentesCurriculares();
        }
    }

    @GetMapping("{idCurso}/componentesCurriculares/{idComponente}")
    public ComponenteCurricular getComponenteCurricular(@PathVariable UUID idCurso, @PathVariable UUID idComponente) {
        return this.componenteCurricularService.getComponenteCursoById(idComponente);
    }

    @DeleteMapping("{idCurso}/componentesCurriculares/{idComponente}")
    public void deletarComponenteCurricular(@PathVariable UUID idCurso, @PathVariable UUID idComponente) {
        this.componenteCurricularService.deleteComponente(idComponente);
    }

    @PostMapping("{idCurso}/componentesCurriculares")
    public ComponenteCurricular criarComponenteCurricularNoCurso(@PathVariable UUID idCurso, @RequestBody @Valid ComponenteCurricular componenteCurricular) {
        Curso curso = new Curso();
        curso.setIdCurso(idCurso);
        componenteCurricular.setCurso(curso);
        return this.componenteCurricularService.salvarComponenteCurricular(componenteCurricular);
    }

    @PutMapping("{idCurso}/componentesCurriculares/{idComponente}")
    public ComponenteCurricular atualizarComponenteCurricular(@PathVariable UUID idCurso, @PathVariable UUID idComponente, @RequestBody @Valid ComponenteCurricular componenteCurricular) {
        componenteCurricular.setIdComponenteCurricular(idComponente);
        return this.componenteCurricularService.salvarComponenteCurricular(componenteCurricular);
    }
}
