package br.com.ifes.backend_pit.controllers.batch;

import br.com.ifes.backend_pit.enums.BatchTemplateEnum;
import br.com.ifes.backend_pit.services.batch.TemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/batch/template")
@RequiredArgsConstructor
public class BatchTemplateController {
    private final TemplateService templateService;

    @GetMapping("/download")
    public ResponseEntity<ByteArrayResource> downloadTemplate(@RequestParam("template") BatchTemplateEnum templateName) {
        ByteArrayResource resource = templateService.baixarTemplate(templateName);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=template_batch_componente.xls")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(resource.contentLength())
                .body(resource);
    }
}
