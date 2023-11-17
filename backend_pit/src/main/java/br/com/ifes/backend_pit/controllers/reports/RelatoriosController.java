package br.com.ifes.backend_pit.controllers.reports;

import br.com.ifes.backend_pit.reports.dto.PITDto;
import br.com.ifes.backend_pit.reports.service.RelatorioService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.util.UUID;

@RestController
@RequestMapping("api/relatorios")
@RequiredArgsConstructor
@Validated
public class RelatoriosController {
    private final RelatorioService relatorioService;

    @PostMapping
    public ResponseEntity<Resource> persistirRelatorio(@Valid @RequestBody PITDto pitDto) {
        ByteArrayResource resource = relatorioService.gerarRelatorio(pitDto);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=relatorio_pit.xls")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(resource.contentLength())
                .body(resource);

    }

    @GetMapping("/{idPit}")
    public ResponseEntity<Resource> downloadRelatorio(@PathVariable UUID idPit) {
        ByteArrayResource resource = relatorioService.baixarRelatorioPorPitID(idPit);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=relatorio_pit.xls")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(resource.contentLength())
                .body(resource);
    }
}
