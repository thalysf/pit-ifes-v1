package br.com.ifes.backend_pit.repositories.atividade.detalhamento;

import br.com.ifes.backend_pit.models.atividades.detalhamento.DetalhamentoComponenteCurricular;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DetalhamentoComponenteCurricularRepository extends JpaRepository<DetalhamentoComponenteCurricular, UUID> {
    @Modifying
    @Query("DELETE FROM DetalhamentoComponenteCurricular detalhamento where detalhamento.idDetalhamentoComponenteCurricular = :idDetalhamento")
    void deleteDetalhamentoById(UUID idDetalhamento);
}
