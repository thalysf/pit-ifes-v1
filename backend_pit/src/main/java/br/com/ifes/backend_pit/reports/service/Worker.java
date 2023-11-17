package br.com.ifes.backend_pit.reports.service;

import br.com.ifes.backend_pit.reports.dto.PITDto;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Component;

import static br.com.ifes.backend_pit.reports.excell.CreateHeaders.*;
import static br.com.ifes.backend_pit.reports.excell.FillDataSet.*;

@Component
public class Worker {
    public void workflowReport(HSSFWorkbook workbook, HSSFSheet sheet, PITDto pitDto) throws Exception {
        createHeaderWithImage(workbook, sheet);
        createPeriodHeader(workbook, sheet, pitDto.getPeriodo());

        createDadosCadastraisHeader(workbook, sheet);
        fillDadosCadastrais(workbook, sheet, pitDto.getDadosCadastraisDTO());

        createResumoHeaders(workbook, sheet, pitDto.getResumoCargaHorariaTotal());
        fillContentResumoCHTotal(workbook, sheet, pitDto.getResumoDTO());

        createHeaderEnsinoAulas(workbook, sheet);
        fillAulas(workbook, sheet, pitDto.getAulas());

        createHeaderAtividadesPlanejamentoManutencaoEnsino(workbook, sheet);
        fillAtividadesPlanejamentoManutencaoEnsino(workbook, sheet, pitDto.getAtividadesPlanejamentoManutencaoEnsino());

        createHeaderAtividadesApoioEnsino(workbook, sheet);
        fillAtividadesApoioEnsino(workbook, sheet, pitDto.getAtividadesApoioEnsino());

        createHeaderDetalhamentoExecucaoPlano(workbook, sheet);
        fillDetalhamentoExecucaoPlano(workbook, sheet, pitDto.getDetalhamentosComponenteCurricular());

        createHeaderCoordenacaoParticipacaoAcoesEnsino(workbook, sheet);
        fillDetalhamentosProjetos(workbook, sheet, pitDto.getDetalhamentosProjetos());

        createHeaderMediacaoPedagogica(workbook, sheet);
        fillMediacaoPedagogica(workbook, sheet, pitDto.getMediacoesPedagogicas());

        createHeaderAtividadesPesquisa(workbook, sheet);
        fillAtividadesPesquisa(workbook, sheet, pitDto.getAtividadesPesquisa());

        createHeaderDetalhamentoPesquisa(workbook, sheet);
        fillDetalhamentosPesquisas(workbook, sheet, pitDto.getDetalhamentosPesquisas());

        createHeaderAtividadesExtensao(workbook, sheet);
        fillAtividadesExtensao(workbook, sheet, pitDto.getAtividadesExtensao());

        createHeaderCoordenacaoParticipacaoAcoesExtensao(workbook, sheet);
        fillDetalhamentosExtensao(workbook, sheet, pitDto.getDetalhamentosExtensao());

        createHeaderAtividadesGestao(workbook, sheet);
        fillAtividadesGestao(workbook, sheet, pitDto.getAtividadesGestao());

        createHeaderAtividadesRepresentacao(workbook, sheet);
        fillAtividadesRepresentacao(workbook, sheet, pitDto.getAtividadesRepresentacao());

        createHeaderOutrasAtividades(workbook, sheet);
        fillOutrasAtividades(workbook, sheet, pitDto.getOutrasAtividades());
    }
}
