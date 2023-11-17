package br.com.ifes.backend_pit.services.batch;

import br.com.ifes.backend_pit.models.dto.batch.ComponenteCurricularDTO;
import br.com.ifes.backend_pit.exception.BusinessException;
import br.com.ifes.backend_pit.models.cadastros.ComponenteCurricular;
import br.com.ifes.backend_pit.models.cadastros.Curso;
import br.com.ifes.backend_pit.repositories.cadastros.ComponenteCurricularRepository;
import br.com.ifes.backend_pit.repositories.cadastros.CursoRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;

@Service
@Transactional
@RequiredArgsConstructor
public class ComponenteCurricularBatchService {
    private final ComponenteCurricularRepository componenteCurricularRepository;
    private final CursoRepository cursoRepository;


    public void processarComponentesBatch(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            Workbook workbook = new HSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0); // Assumindo que a planilha está na primeira aba


            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue; // Pule o cabeçalho, valores estão na próxima linha
                }

                try{
                    if(
                            row.getCell(0).getStringCellValue().equals("")
                            || row.getCell(1).getStringCellValue().equals("")
                            || row.getCell(2).getRichStringCellValue().equals("")
                    ){
                        continue;
                    }

                    ComponenteCurricularDTO dto = new ComponenteCurricularDTO();
                    dto.setCurso(row.getCell(0).getStringCellValue());
                    dto.setComponenteCurricular(row.getCell(1).getStringCellValue());

                    String cargaHoraria = row.getCell(2).getRichStringCellValue().toString().replace(",", ".");
                    dto.setCargaHoraria(Double.parseDouble(cargaHoraria));

                    Curso curso = cursoRepository.findByNomeIgnoreCase(dto.getCurso())
                            .orElseGet(() -> {
                                Curso novoCurso = new Curso();
                                novoCurso.setNome(dto.getCurso());
                                return cursoRepository.save(novoCurso);
                            });

                    ComponenteCurricular componente = componenteCurricularRepository
                            .findByNomeIgnoreCaseAndCursoNomeIgnoreCase(dto.getComponenteCurricular(), curso.getNome())
                            .orElseGet(() -> {
                                ComponenteCurricular novoComponente = new ComponenteCurricular();
                                novoComponente.setNome(dto.getComponenteCurricular());
                                novoComponente.setCargaHoraria(dto.getCargaHoraria());
                                novoComponente.setCurso(curso);
                                return novoComponente;
                            });

                    componente.setCargaHoraria(dto.getCargaHoraria());
                    componenteCurricularRepository.save(componente);
                } catch (Exception ex){
                    throw new BusinessException("Não foi possível realizar a carga em lote. " +
                            "Verifique se a carga horária do componente na linha " + (row.getRowNum() + 1) +
                            " está no formato válido. A carga horária deve estar no formato decimal usando virgula ou ponto. Exemplo: 4.0");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException("Não foi possível realizar a carga em lote dos componentes curriculares " + e.getMessage());
        }
    }


}
