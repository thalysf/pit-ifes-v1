package br.com.ifes.backend_pit.controllers.batch;

import br.com.ifes.backend_pit.repositories.atividade.AtividadeRepository;
import br.com.ifes.backend_pit.services.batch.PortariaBatchService;
import br.com.ifes.backend_pit.services.batch.ServidorBatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/batch/portaria")
@RequiredArgsConstructor
public class PortariaBatchController {
     private final PortariaBatchService portariaBatchService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadExcel(@RequestParam("file") MultipartFile file) {
        portariaBatchService.processarPortariasBatch(file);
        return ResponseEntity.ok("Arquivo Excel de Portarias recebido e processado com sucesso.");
    }
}
