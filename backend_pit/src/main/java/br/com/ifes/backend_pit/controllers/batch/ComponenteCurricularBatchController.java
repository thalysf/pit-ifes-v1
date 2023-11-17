package br.com.ifes.backend_pit.controllers.batch;

import br.com.ifes.backend_pit.services.batch.ComponenteCurricularBatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/batch/componenteCurricular")
@RequiredArgsConstructor
public class ComponenteCurricularBatchController {
    private final ComponenteCurricularBatchService componenteCurricularBatchService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadExcel(@RequestParam("file") MultipartFile file) {
        componenteCurricularBatchService.processarComponentesBatch(file);
        return ResponseEntity.ok("Arquivo Excel de Componentes Curriculares recebido e processado com sucesso.");
    }
}
