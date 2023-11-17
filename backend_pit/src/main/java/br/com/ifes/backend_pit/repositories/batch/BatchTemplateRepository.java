package br.com.ifes.backend_pit.repositories.batch;

import br.com.ifes.backend_pit.enums.BatchTemplateEnum;
import br.com.ifes.backend_pit.models.batch.BatchTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BatchTemplateRepository extends JpaRepository<BatchTemplate, UUID> {
    Optional<BatchTemplate> findByTemplateName(BatchTemplateEnum templateName);
}