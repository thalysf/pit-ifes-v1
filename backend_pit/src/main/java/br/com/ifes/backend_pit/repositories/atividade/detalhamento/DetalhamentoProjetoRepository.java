package br.com.ifes.backend_pit.repositories.atividade.detalhamento;

import br.com.ifes.backend_pit.models.atividades.detalhamento.DetalhamentoProjeto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DetalhamentoProjetoRepository extends JpaRepository<DetalhamentoProjeto, UUID> {
}
