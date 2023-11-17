package br.com.ifes.backend_pit.repositories.atividade.detalhamento;

import br.com.ifes.backend_pit.models.atividades.detalhamento.DetalhamentoAluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DetalhamentoAlunoRepository extends JpaRepository<DetalhamentoAluno, UUID> {
}
