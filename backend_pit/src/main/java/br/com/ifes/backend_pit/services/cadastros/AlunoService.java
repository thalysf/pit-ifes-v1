package br.com.ifes.backend_pit.services.cadastros;

import br.com.ifes.backend_pit.models.cadastros.Aluno;
import br.com.ifes.backend_pit.repositories.cadastros.AlunoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AlunoService {
    private final AlunoRepository alunoRepository;

    public List<Aluno> obterAlunos() {
        return alunoRepository.findAll();
    }
}
