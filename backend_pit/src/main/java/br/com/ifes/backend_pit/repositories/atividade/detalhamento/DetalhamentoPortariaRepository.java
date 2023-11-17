package br.com.ifes.backend_pit.repositories.atividade.detalhamento;

import br.com.ifes.backend_pit.models.atividades.detalhamento.DetalhamentoPortaria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DetalhamentoPortariaRepository extends JpaRepository<DetalhamentoPortaria, UUID> {
    List<DetalhamentoPortaria> findAllByPortariaIdPortariaAndPortariaProfessoresIdServidor(UUID idPortaria, UUID idProfessor);
}
