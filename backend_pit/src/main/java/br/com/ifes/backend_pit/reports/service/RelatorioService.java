package br.com.ifes.backend_pit.reports.service;

import br.com.ifes.backend_pit.exception.BusinessException;
import br.com.ifes.backend_pit.exception.NotFoundException;
import br.com.ifes.backend_pit.models.pit.PIT;
import br.com.ifes.backend_pit.models.reports.Relatorio;
import br.com.ifes.backend_pit.reports.dto.PITDto;
import br.com.ifes.backend_pit.repositories.reports.RelatorioRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.util.Optional;
import java.util.UUID;

import static br.com.ifes.backend_pit.reports.util.Util.getNextRowToFill;


@Service
@RequiredArgsConstructor
@Transactional
public class RelatorioService {
    private final RelatorioRepository relatorioRepository;

    private final Worker worker;

    public ByteArrayResource gerarRelatorio(PITDto pitDto) {
        File excelFile = null;
        try {
            // Gerando relatório
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet(String.format("relatorio_pit_%s_%s", pitDto.getPeriodo(), pitDto.getDadosCadastraisDTO().getNome()).replaceAll("[/\\\\]", "_"));
            excelFile = new File("report_pit_temp.xls");
            FileOutputStream fileOut = new FileOutputStream(excelFile, false);
            worker.workflowReport(workbook, sheet, pitDto);
            workbook.write(fileOut);

            System.out.println("Ultima linha preenchida: " + getNextRowToFill(sheet, 0));
            System.out.println("Relatório em Excell gerado com sucesso!");

            // Persistência relatório
            byte[] relatorioBytes = Files.readAllBytes(excelFile.toPath());

            UUID idPit = UUID.fromString(pitDto.getIdPit());
            Relatorio relatorio = new Relatorio();
            relatorio.setPit(PIT.builder().idPIT(idPit).build());
            relatorio.setRelatorioExcel(relatorioBytes);

            Optional<Relatorio> relatorioOptional = relatorioRepository.findByPitIdPIT(idPit);
            relatorioOptional.ifPresent(value -> relatorio.setIdRelatorio(value.getIdRelatorio()));

            relatorioRepository.save(relatorio);

            // Retornando conteúdo do relatório para download pelo front-end
            return new ByteArrayResource(relatorioBytes);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("Falha ao gerar relatório: " + e.getMessage() + " \n");
        } finally {
            if (excelFile != null && excelFile.exists()) {
                excelFile.delete();
            }
        }
    }

    public ByteArrayResource baixarRelatorioPorPitID(UUID idPit) {
        Relatorio relatorio = relatorioRepository.findByPitIdPIT(idPit).orElseThrow(() -> new NotFoundException("Relatório não encontrado!"));

        return new ByteArrayResource(relatorio.getRelatorioExcel());
    }
}
