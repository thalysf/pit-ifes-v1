package br.com.ifes.backend_pit.services.batch;

import br.com.ifes.backend_pit.enums.TipoProjetoEnum;
import br.com.ifes.backend_pit.exception.BusinessException;
import br.com.ifes.backend_pit.models.atividades.Atividade;
import br.com.ifes.backend_pit.models.cadastros.ParticipacaoProjeto;
import br.com.ifes.backend_pit.models.cadastros.Projeto;
import br.com.ifes.backend_pit.models.usuarios.Servidor;
import br.com.ifes.backend_pit.repositories.atividade.AtividadeRepository;
import br.com.ifes.backend_pit.repositories.cadastros.ProjetoRepository;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjetoBatchService {

    private final ProjetoRepository projetoRepository;
    private final AtividadeRepository atividadeRepository;
    private final ServidorRepository servidorRepository;

    public void processarProjetosBatch(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            Workbook workbook = new HSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0); // Assumindo que a planilha está na primeira aba

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue; // pula o header
                }

                if(
                        row.getCell(0).getStringCellValue().equals("")
                        || row.getCell(1).getStringCellValue().equals("")
                ){
                    continue;
                }


                Projeto projeto = new Projeto();
                projeto.setTituloProjeto(row.getCell(0).getStringCellValue());
                projeto.setTipoProjeto(TipoProjetoEnum.valueOf(row.getCell(1).getStringCellValue()));
                projeto.setTipoAcao(row.getCell(2).getStringCellValue());
                projeto.setNumeroCadastro(row.getCell(3).getStringCellValue());
                projeto.setCargaHorariaMinima(Double.parseDouble(row.getCell(4).getRichStringCellValue().toString()));
                projeto.setCargaHorariaMaxima(Double.parseDouble(row.getCell(5).getRichStringCellValue().toString()));

                Date dataInicio = row.getCell(6).getDateCellValue();
                Date dataFim = row.getCell(7).getDateCellValue();
                projeto.setDataInicioVigencia(new Timestamp(dataInicio.getTime()));
                projeto.setDataFimVigencia(new Timestamp(dataFim.getTime()));

                String professorSiape = String.valueOf(Math.round(row.getCell(8).getNumericCellValue()));
                Optional<Servidor> professor = servidorRepository.findServidorBySiape(professorSiape);

                String atividadeNome = row.getCell(9).getStringCellValue();
                Atividade atividade = atividadeRepository.findAtividadeByNomeAtividadeContainingIgnoreCase(atividadeNome);

                salvarOuAtualizarProjeto(projeto, professor, atividade);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("Não foi possível realizar a carga em lote dos projetos");
        }
    }

    private void salvarOuAtualizarProjeto(Projeto projeto, Optional<Servidor> professor, Atividade atividade) {
        Projeto projetoExistente = projetoRepository.findAByTituloProjetoContainingIgnoreCaseAndNumeroCadastroContainingIgnoreCase(projeto.getTituloProjeto(), projeto.getNumeroCadastro());

        if (projetoExistente != null) {
            projetoExistente.setDataInicioVigencia(projeto.getDataInicioVigencia());
            projetoExistente.setDataFimVigencia(projeto.getDataFimVigencia());
            projetoExistente.setCargaHorariaMinima(projeto.getCargaHorariaMinima());
            projetoExistente.setCargaHorariaMaxima(projeto.getCargaHorariaMaxima());

            if(professor.isPresent()) adicionarParticipacaoProjeto(projetoExistente, professor.get());
            projetoRepository.save(projetoExistente);

        } else {
            projeto.getAtividades().add(atividade);

            if(professor.isPresent()) adicionarParticipacaoProjeto(projeto, professor.get());
            projetoRepository.save(projeto);
        }
    }

    private void adicionarParticipacaoProjeto(Projeto projeto, Servidor professor) {
        ParticipacaoProjeto novaParticipacao = new ParticipacaoProjeto();
        novaParticipacao.setProfessor(professor);
        novaParticipacao.setProjeto(projeto);

        if (projeto.getParticipacaoProjetos() != null) {
            for (ParticipacaoProjeto participacao : projeto.getParticipacaoProjetos()) {
                if (participacao.getProfessor().equals(professor)) {
                    return;
                }
            }
            projeto.getParticipacaoProjetos().add(novaParticipacao);
        }
        else {
            projeto.setParticipacaoProjetos(new ArrayList<>());
        }

        projeto.getParticipacaoProjetos().add(novaParticipacao);
    }

}
