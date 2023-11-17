package br.com.ifes.backend_pit.controllers.batch;

import br.com.ifes.backend_pit.services.batch.PortariaBatchService;
import br.com.ifes.backend_pit.services.batch.ProjetoBatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/batch/projeto")
@RequiredArgsConstructor
public class ProjetosBatchController {
     private final ProjetoBatchService processarPortariasBatch;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadExcel(@RequestParam("file") MultipartFile file) {
        processarPortariasBatch.processarProjetosBatch(file);
        return ResponseEntity.ok("Arquivo Excel de Projetos recebido e processado com sucesso.");
    }
}
