package br.com.ifes.backend_pit.controllers.batch;

import br.com.ifes.backend_pit.services.batch.ServidorBatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/batch/servidor")
@RequiredArgsConstructor
public class ServidorBatchController {
    private final ServidorBatchService servidorBatchService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadExcel(@RequestParam("file") MultipartFile file) {
        servidorBatchService.processarServidoresBatch(file);
        return ResponseEntity.ok("Arquivo Excel de Servidores recebido e processado com sucesso.");
    }
}
