package br.com.ifes.backend_pit.services.batch;

import br.com.ifes.backend_pit.enums.TipoPortariaEnum;
import br.com.ifes.backend_pit.exception.BusinessException;
import br.com.ifes.backend_pit.models.atividades.Atividade;
import br.com.ifes.backend_pit.models.cadastros.Portaria;
import br.com.ifes.backend_pit.models.usuarios.Servidor;
import br.com.ifes.backend_pit.repositories.atividade.AtividadeRepository;
import br.com.ifes.backend_pit.repositories.cadastros.PortariaRepository;
import br.com.ifes.backend_pit.repositories.usuarios.ServidorRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class PortariaBatchService {
    private final PortariaRepository portariaRepository;
    private final ServidorRepository servidorRepository;
    private final AtividadeRepository atividadeRepository;

    public void processarPortariasBatch(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            Workbook workbook = new HSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0); // Assumindo que a planilha está na primeira aba

            for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
                Row row = sheet.getRow(rowNum);
                if (row == null) {
                    continue; // pula o header
                }

                if(
                        row.getCell(0).getStringCellValue().equals("")
                        || row.getCell(1).getStringCellValue().equals("")
                ){
                    continue;
                }

                Portaria portaria = new Portaria();
                portaria.setNome(row.getCell(0).getStringCellValue());
                portaria.setTipoAtividade(TipoPortariaEnum.valueOf(row.getCell(1).getStringCellValue()));
                portaria.setDescricao(row.getCell(2).getStringCellValue());

                Date dataInicio = row.getCell(3).getDateCellValue();
                Date dataFim = row.getCell(4).getDateCellValue();
                portaria.setDataInicioVigencia(new Timestamp(dataInicio.getTime()));
                portaria.setDataFimVigencia(new Timestamp(dataFim.getTime()));

                portaria.setCargaHorariaMinima(Double.parseDouble(row.getCell(5).getRichStringCellValue().toString()));
                portaria.setCargaHorariaMaxima(Double.parseDouble(row.getCell(6).getRichStringCellValue().toString()));

                String siapeProfessor = String.valueOf(Math.round(row.getCell(7).getNumericCellValue()));
                Optional<Servidor> professor = servidorRepository.findServidorBySiape(siapeProfessor);

                String nomeAtividade = row.getCell(8).getStringCellValue();
                Atividade atividade = atividadeRepository.findAtividadeByNomeAtividadeContainingIgnoreCase(nomeAtividade);

                salvarOuAtualizarPortaria(portaria, professor, atividade);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("Não foi possível realizar a carga em lote das portarias " + e.getMessage());
        }
    }

    private void salvarOuAtualizarPortaria(Portaria portaria, Optional<Servidor> professor, Atividade atividade) {
        Portaria portariaExistente = portariaRepository.findAByNomeContainingIgnoreCaseAndDescricaoContainingIgnoreCase(portaria.getNome(), portaria.getDescricao());

        if (portariaExistente != null) {
            portariaExistente.setTipoAtividade(portaria.getTipoAtividade());
            portariaExistente.setDataInicioVigencia(portaria.getDataInicioVigencia());
            portariaExistente.setDataFimVigencia(portaria.getDataFimVigencia());
            portariaExistente.setCargaHorariaMinima(portaria.getCargaHorariaMinima());
            portariaExistente.setCargaHorariaMaxima(portaria.getCargaHorariaMaxima());
            portariaExistente.setAtividade(atividade);
            if(professor.isPresent()){
                if(!portariaExistente.getProfessores().contains(professor.get()))
                    portariaExistente.getProfessores().add(professor.get());
            }
            portariaRepository.save(portariaExistente);
        } else {
            portaria.setProfessores(new ArrayList<>());
            if(professor.isPresent()) portaria.getProfessores().add(professor.get());
            portaria.setAtividade(atividade);
            portariaRepository.save(portaria);
        }
    }
}
