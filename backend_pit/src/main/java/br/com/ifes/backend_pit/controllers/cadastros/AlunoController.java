package br.com.ifes.backend_pit.controllers.cadastros;

import br.com.ifes.backend_pit.models.cadastros.Aluno;
import br.com.ifes.backend_pit.services.cadastros.AlunoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/alunos")
@RequiredArgsConstructor
public class AlunoController {
    private final AlunoService alunoService;

    @GetMapping()
    public List<Aluno> obterAlunos(){
        return this.alunoService.obterAlunos();
    }
}
