package br.com.ifes.backend_pit.services.batch;

import br.com.ifes.backend_pit.enums.BatchTemplateEnum;
import br.com.ifes.backend_pit.repositories.batch.BatchTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TemplateService {
    private final BatchTemplateRepository batchTemplateRepository;
    public ByteArrayResource baixarTemplate(BatchTemplateEnum templateName) {
        return batchTemplateRepository.findByTemplateName(templateName)
                .map(batchTemplate -> new ByteArrayResource(batchTemplate.getTemplateFile()))
                .orElse(null);
    }
}
