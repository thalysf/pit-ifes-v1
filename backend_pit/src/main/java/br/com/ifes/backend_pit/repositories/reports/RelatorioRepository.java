package br.com.ifes.backend_pit.repositories.reports;

import br.com.ifes.backend_pit.models.reports.Relatorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RelatorioRepository extends JpaRepository<Relatorio, UUID> {

    Optional<Relatorio> findByPitIdPIT(UUID idPit);
}
