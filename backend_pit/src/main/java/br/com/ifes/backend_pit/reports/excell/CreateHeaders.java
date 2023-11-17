package br.com.ifes.backend_pit.reports.excell;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import br.com.ifes.backend_pit.reports.styles.Style;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static br.com.ifes.backend_pit.reports.styles.Style.*;
import static br.com.ifes.backend_pit.reports.util.Util.adjustColumnWidths;
import static br.com.ifes.backend_pit.reports.util.Util.getNextRowToFill;

public class CreateHeaders {
    public static void createHeaderWithImage(HSSFWorkbook workbook, HSSFSheet sheet) throws IOException {
        // Merge na coluna A (linhas 1, 2, 3 e 4)
        sheet.addMergedRegion(new CellRangeAddress(0, 3, 0, 0));

        // Imagem IFES
        InputStream inputStream = CreateHeaders.class.getClassLoader().getResourceAsStream("images/ifes-horizontal-small.png");
        byte[] bytes = IOUtils.toByteArray(inputStream);
        int pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);

        // Fecha o input stream
        inputStream.close();

        // Cria o anchor para a imagem
        CreationHelper helper = workbook.getCreationHelper();
        ClientAnchor anchor = helper.createClientAnchor();
        anchor.setCol1(0); // Início da coluna A
        anchor.setRow1(0); // Início da linha 1 (depois da mesclagem)

        // Define o tamanho da imagem para ocupar toda a coluna A
        anchor.setCol2(1); // Fim da coluna A (mesma da início para ocupar a coluna A inteira)
        anchor.setRow2(4); // Fim da linha 4

        // Insere a imagem no header
        Drawing drawing = sheet.createDrawingPatriarch();
        Picture picture = drawing.createPicture(anchor, pictureIdx);

        // Ajusta a largura da coluna A para a largura da imagem
        int imageWidthInPixels = 200; // Defina a largura desejada da imagem em pixels (ajuste conforme necessário)
        int columnWidthInUnits = imageWidthInPixels * 256 / 7; // Converta pixels para unidades de largura
        sheet.setColumnWidth(0, columnWidthInUnits);
        picture.resize(0.7D); // Redimensiona a imagem para ocupar as células A1 a A4

        // Mescla as células B1 a J1 para o texto da linha 1
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 9));

        // Mescla as células B2 a J2 para o texto da linha 2
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 1, 9));

        // Mescla as células B3 a J3 para o texto da linha 3
        sheet.addMergedRegion(new CellRangeAddress(2, 2, 1, 9));

        // Mescla as células B4 a J4 para o texto da linha 4
        sheet.addMergedRegion(new CellRangeAddress(3, 3, 1, 9));

        // Ajusta verticalmente o tamanho das células
        adjustColumnWidths(sheet, 9);

        // Cria o estilo para o texto no header (default)
        CellStyle defaultCellSyle = getDefaultSubCellStyle(workbook);

        List<String> textoCabecalho = new ArrayList<>(List.of("MINISTÉRIO DA EDUCAÇÃO", "SECRETARIA DE EDUCAÇÃO PROFISSIONAL E TECNOLÓGICA", "INSTITUTO FEDERAL DE EDUCAÇÃO, CIÊNCIA E TECNOLOGIA DO ESPÍRITO SANTO"));

        // Cria as células para o texto
        for (int rowIdx = 0; rowIdx < 3; rowIdx++) {
            Row row = sheet.getRow(rowIdx);
            if (row == null) {
                row = sheet.createRow(rowIdx);
            }
            Cell cell = row.createCell(1);
            cell.setCellValue(textoCabecalho.get(rowIdx));

            if (rowIdx == 0) {
                cell.setCellStyle(getBoldCellStyle(workbook));
            } else {
                cell.setCellStyle(defaultCellSyle);
            }
        }
    }


    public static void createPeriodHeader(HSSFWorkbook workbook, HSSFSheet sheet, String periodo) {
        // Mescla as células A6 a G6 para o texto (Anexo II - Plano Individual de Trabalho)
        sheet.addMergedRegion(new CellRangeAddress(5, 5, 0, 6));

        // Cria a célula para o texto (Anexo II - Plano Individual de Trabalho) e aplica borda completa
        Row rowAnexo = sheet.getRow(5);
        if (rowAnexo == null) {
            rowAnexo = sheet.createRow(5);
        }
        Cell cellAnexo = rowAnexo.createCell(0);
        cellAnexo.setCellValue("Anexo II - Plano Individual de Trabalho");
        cellAnexo.setCellStyle(getMainHeaderBoldCellStyle(workbook));

        // Cria a célula para o texto (Período) na coluna H da linha 6 e aplica borda superior e inferior
        Row rowPeriodo = sheet.getRow(5);
        if (rowPeriodo == null) {
            rowPeriodo = sheet.createRow(5);
        }
        Cell cellPeriodo = rowPeriodo.createCell(7); // Coluna H (índice 7)
        cellPeriodo.setCellValue("Período");
        cellPeriodo.setCellStyle(getMainHeaderBoldCellStyle(workbook));

        // Mescla as células I6 a J6 para o valor do período
        sheet.addMergedRegion(new CellRangeAddress(5, 5, 8, 9));

        // Cria a célula para o valor do período e aplica borda superior, inferior e direita
        Cell cellValorPeriodo = rowPeriodo.createCell(8); // Coluna I (índice 8)
        cellValorPeriodo.setCellValue(periodo);
        cellValorPeriodo.setCellStyle(getMainHeaderBoldCellStyle(workbook));

        // Aplica o background cinza claro no range A6 até J6
        CellRangeAddress rangeAtoJ = new CellRangeAddress(5, 5, 0, 9);
        applyBackground(workbook, rangeAtoJ, IndexedColors.GREY_25_PERCENT);

        // Aplicar bordas

        // Células A6 a G6 (borda completa)
        CellRangeAddress rangeAtoG = new CellRangeAddress(5, 5, 0, 6);
        applyBorders(sheet, workbook, rangeAtoG, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

        // Célula H6 (borda em cima e em baixo)
        CellRangeAddress rangeH6 = new CellRangeAddress(5, 5, 7, 7);
        applyBorders(sheet, workbook, rangeH6, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.NONE, BorderStyle.NONE);

        // Células I6 a J6 (borda em cima, em baixo e na direita)
        CellRangeAddress rangeItoJ = new CellRangeAddress(5, 5, 8, 9);
        applyBorders(sheet, workbook, rangeItoJ, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.NONE, BorderStyle.THIN);
    }


    public static void createDadosCadastraisHeader(HSSFWorkbook workbook, HSSFSheet sheet) {
        // Estilo para os headers
        CellStyle headerStyle = getSubMainHeaderBoldCellStyle(workbook);

        // DADOS CADASTRAIS
        CellRangeAddress rangeDadosCadastrais = new CellRangeAddress(7, 7, 0, 3);
        sheet.addMergedRegion(rangeDadosCadastrais);

        Row rowDadosCadastrais = sheet.getRow(7);
        if (rowDadosCadastrais == null) {
            rowDadosCadastrais = sheet.createRow(7);
        }
        Cell cellDadosCadastrais = rowDadosCadastrais.createCell(0);
        cellDadosCadastrais.setCellValue("DADOS CADASTRAIS");
        cellDadosCadastrais.setCellStyle(headerStyle);

        applyBackground(workbook, rangeDadosCadastrais, IndexedColors.GREY_25_PERCENT);
        applyBorders(sheet, workbook, rangeDadosCadastrais, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);
    }

    public static void createResumoHeaders(HSSFWorkbook workbook, HSSFSheet sheet, String cargaHorariaTotal) {
        // Estilo para os headers
        CellStyle headerStyle = getSubMainHeaderBoldCellStyle(workbook);

        // RESUMO - CH TOTAL
        CellRangeAddress rangeResumoCHTotal = new CellRangeAddress(7, 7, 5, 7);
        sheet.addMergedRegion(rangeResumoCHTotal);


        Row rowResumoCHTotal = sheet.getRow(7);
        if (rowResumoCHTotal == null) {
            rowResumoCHTotal = sheet.createRow(7);
        }
        Cell cellResumoCHTotal = rowResumoCHTotal.createCell(5);
        cellResumoCHTotal.setCellValue("RESUMO - CH TOTAL:");
        cellResumoCHTotal.setCellStyle(headerStyle);

        applyBackground(workbook, rangeResumoCHTotal, IndexedColors.GREY_25_PERCENT);
        applyBorders(sheet, workbook, rangeResumoCHTotal, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

        // Valor da carga horária total
        CellRangeAddress rangeCargaHorariaTotal = new CellRangeAddress(7, 7, 8, 9);
        sheet.addMergedRegion(rangeCargaHorariaTotal);


        Row rowCargaHorariaTotal = sheet.getRow(7);
        if (rowCargaHorariaTotal == null) {
            rowCargaHorariaTotal = sheet.createRow(7);
        }
        Cell cellCargaHorariaTotal = rowCargaHorariaTotal.createCell(8);
        cellCargaHorariaTotal.setCellValue(cargaHorariaTotal);
        cellCargaHorariaTotal.setCellStyle(headerStyle);

        applyBackground(workbook, rangeCargaHorariaTotal, IndexedColors.ORANGE);
        applyBorders(sheet, workbook, rangeCargaHorariaTotal, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

        /// Estilo para o resumo CH total
        CellStyle resumoCHStyle = getDefaultCellStyle(workbook);

        // Labels para as colunas F, G, H, I e J
        String[] labelsF = {"ATIVIDADE"};
        String[] labelsG = {"CH"};
        String[] labelsHI = {"ATIVIDADE", "", "CH"};

        // Preenche os labels nas colunas F, G, H, I e J
        Row rowF = sheet.getRow(8);
        if (rowF == null) {
            rowF = sheet.createRow(8);
        }
        Row rowG = sheet.getRow(8);
        if (rowG == null) {
            rowG = sheet.createRow(8);
        }
        Row rowH = sheet.getRow(8);
        if (rowH == null) {
            rowH = sheet.createRow(8);
        }
        Row rowI = sheet.getRow(8);
        if (rowI == null) {
            rowI = sheet.createRow(8);
        }
        Row rowJ = sheet.getRow(8);
        if (rowJ == null) {
            rowJ = sheet.createRow(8);
        }

        for (int i = 0; i < labelsF.length; i++) {
            Cell cellF = rowF.createCell(5 + i);
            cellF.setCellValue(labelsF[i]);
            cellF.setCellStyle(resumoCHStyle);
        }

        for (int i = 0; i < labelsG.length; i++) {
            Cell cellG = rowG.createCell(6 + i);
            cellG.setCellValue(labelsG[i]);
            cellG.setCellStyle(resumoCHStyle);
        }

        int startCol = 7;
        sheet.addMergedRegion(new CellRangeAddress(8, 8, startCol, startCol + 1));
        for (int i = 0; i < labelsHI.length; i++) {
            Cell cellH = rowH.createCell(startCol + i);
            cellH.setCellValue(labelsHI[i]);
            cellH.setCellStyle(resumoCHStyle);
        }

        // Aplicar bordas em todas as células de header do resumo
        CellRangeAddress rangeF9 = new CellRangeAddress(8, 8, 5, 5);
        applyBorders(sheet, workbook, rangeF9, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

        CellRangeAddress rangeG9 = new CellRangeAddress(8, 8, 6, 6);
        applyBorders(sheet, workbook, rangeG9, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

        CellRangeAddress rangeHI9 = new CellRangeAddress(8, 8, 7, 8);
        applyBorders(sheet, workbook, rangeHI9, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

        CellRangeAddress rangeJ9 = new CellRangeAddress(8, 8, 9, 9);
        applyBorders(sheet, workbook, rangeJ9, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);
    }


    public static void createHeaderEnsinoAulas(HSSFWorkbook workbook, HSSFSheet sheet) {
        // Estilo para o header "1. Atividades de Ensino"
        CellStyle headerStyle = Style.getDefaultAtividadesHeaderCellStyle(workbook);


        // Estilo para o header "1.1. Aulas"
        CellStyle subHeaderStyle = Style.getDefaultAtividadesHeaderCellStyle(workbook);

        // Estilo para os subitens "Curso", "Componente curricular" e "CH semanal"
        CellStyle subItemStyle = Style.getDefaultSubCellStyle(workbook);

        // Preencher o header "1. Atividades de Ensino"
        int nextRowEnsino = getNextRowToFill(sheet, 0);
        Row rowAtividadesEnsino = sheet.createRow(nextRowEnsino);
        Cell cellAtividadesEnsino = rowAtividadesEnsino.createCell(0);
        cellAtividadesEnsino.setCellValue("1. Atividades de Ensino");
        cellAtividadesEnsino.setCellStyle(headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(nextRowEnsino, nextRowEnsino, 0, 9));

        // Preencher o header "1.1. Aulas"
        int nextRowAulas = nextRowEnsino + 1;
        Row rowAulas = sheet.createRow(nextRowAulas);
        Cell cellAulas = rowAulas.createCell(0);
        cellAulas.setCellValue("1.1. Aulas");
        cellAulas.setCellStyle(subHeaderStyle);
        sheet.addMergedRegion(new CellRangeAddress(nextRowAulas, nextRowAulas, 0, 9));

        // Bordas e Background "1. Atividades de Ensino"
        Style.applyBackground(workbook, new CellRangeAddress(nextRowEnsino, nextRowEnsino, 0, 9), IndexedColors.YELLOW);
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowEnsino, nextRowEnsino, 0, 9), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

        // Bordas e Background "1.1. Aulas"
        Style.applyBackground(workbook, new CellRangeAddress(nextRowAulas, nextRowAulas, 0, 9), IndexedColors.GREY_25_PERCENT);
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowAulas, nextRowAulas, 0, 9), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

        // Preencher os subitens do header "1.1. Aulas"
        int nextRowSubItens = nextRowAulas + 1;

        // Subitem "Curso"
        Row rowCurso = sheet.createRow(nextRowSubItens);
        Cell cellCurso = rowCurso.createCell(0);
        cellCurso.setCellValue("Curso");
        cellCurso.setCellStyle(subItemStyle);
        sheet.addMergedRegion(new CellRangeAddress(nextRowSubItens, nextRowSubItens, 0, 2));

        // Subitem "Componente curricular"
        Row rowComponenteCurricular = sheet.getRow(nextRowSubItens);
        if (rowComponenteCurricular == null) {
            rowComponenteCurricular = sheet.createRow(nextRowSubItens);
        }
        Cell cellComponenteCurricular = rowComponenteCurricular.createCell(3);
        cellComponenteCurricular.setCellValue("Componente curricular");
        cellComponenteCurricular.setCellStyle(subItemStyle);
        sheet.addMergedRegion(new CellRangeAddress(nextRowSubItens, nextRowSubItens, 3, 7));

        // Subitem "CH semanal"
        Row rowCHSemanal = sheet.getRow(nextRowSubItens);
        if (rowCHSemanal == null) {
            rowCHSemanal = sheet.createRow(nextRowSubItens);
        }
        Cell cellCHSemanal = rowCHSemanal.createCell(8);
        cellCHSemanal.setCellValue("CH semanal");
        cellCHSemanal.setCellStyle(subItemStyle);
        sheet.addMergedRegion(new CellRangeAddress(nextRowSubItens, nextRowSubItens, 8, 9));

        // Bordas para os subitens
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowSubItens, nextRowSubItens, 0, 2), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowSubItens, nextRowSubItens, 3, 7), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowSubItens, nextRowSubItens, 8, 9), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);
    }

    public static void createHeaderAtividadesPlanejamentoManutencaoEnsino(HSSFWorkbook workbook, HSSFSheet sheet) {
        // Estilo para o header "1.2. Atividades de Planejamento e Manutenção de Ensino"
        CellStyle headerStyle = Style.getDefaultAtividadesHeaderCellStyle(workbook);


        // Estilo para os subitens "Atividade" e "CH semanal"
        CellStyle subItemStyle = Style.getDefaultSubCellStyle(workbook);

        // Preencher o header "1.2. Atividades de Planejamento e Manutenção de Ensino"
        int nextRow = getNextRowToFill(sheet, 0);
        Row rowHeader = sheet.createRow(nextRow);
        Cell cellHeader = rowHeader.createCell(0);
        cellHeader.setCellValue("1.2. Atividades de Planejamento e Manutenção de Ensino");
        cellHeader.setCellStyle(headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(nextRow, nextRow, 0, 9));

        // Bordas e Background para "1.2. Atividades de Planejamento e Manutenção de Ensino"
        Style.applyBackground(workbook, new CellRangeAddress(nextRow, nextRow, 0, 9), IndexedColors.GREY_25_PERCENT);
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRow, nextRow, 0, 9), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

        // Preencher os subitens do header "1.2. Atividades de Planejamento e Manutenção de Ensino"
        int nextRowAtividade = nextRow + 1;

        // Subitem "Atividade"
        Row rowAtividade = sheet.createRow(nextRowAtividade);
        Cell cellAtividade = rowAtividade.createCell(0);
        cellAtividade.setCellValue("Atividade");
        cellAtividade.setCellStyle(subItemStyle);
        sheet.addMergedRegion(new CellRangeAddress(nextRowAtividade, nextRowAtividade, 0, 7));

        // Subitem "CH semanal"
        Cell cellCHSemanal = rowAtividade.createCell(8);
        cellCHSemanal.setCellValue("CH semanal");
        cellCHSemanal.setCellStyle(subItemStyle);
        sheet.addMergedRegion(new CellRangeAddress(nextRowAtividade, nextRowAtividade, 8, 9));

        // Bordas para os subitens
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowAtividade, nextRowAtividade, 0, 7), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowAtividade, nextRowAtividade, 8, 9), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);
    }

    public static void createHeaderAtividadesApoioEnsino(HSSFWorkbook workbook, HSSFSheet sheet) {
        // Estilo para o header "1.3. Atividades de Apoio ao Ensino"
        CellStyle headerStyle = Style.getDefaultAtividadesHeaderCellStyle(workbook);

        // Estilo para os subitens "Atividade" e "CH semanal"
        CellStyle subItemStyle = Style.getDefaultSubCellStyle(workbook);

        // Preencher o header "1.3. Atividades de Apoio ao Ensino"
        int nextRow = getNextRowToFill(sheet, 0);
        Row rowHeader = sheet.createRow(nextRow);
        Cell cellHeader = rowHeader.createCell(0);
        cellHeader.setCellValue("1.3. Atividades de Apoio ao Ensino");
        cellHeader.setCellStyle(headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(nextRow, nextRow, 0, 9));

        // Bordas e Background para "1.3. Atividades de Apoio ao Ensino"
        Style.applyBackground(workbook, new CellRangeAddress(nextRow, nextRow, 0, 9), IndexedColors.GREY_25_PERCENT);
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRow, nextRow, 0, 9), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

        // Preencher os subitens do header "1.3. Atividades de Apoio ao Ensino"
        int nextRowAtividade = nextRow + 1;

        // Subitem "Atividade"
        Row rowAtividade = sheet.createRow(nextRowAtividade);
        Cell cellAtividade = rowAtividade.createCell(0);
        cellAtividade.setCellValue("Atividade");
        cellAtividade.setCellStyle(subItemStyle);
        sheet.addMergedRegion(new CellRangeAddress(nextRowAtividade, nextRowAtividade, 0, 7));

        // Subitem "CH semanal"
        Cell cellCHSemanal = rowAtividade.createCell(8);
        cellCHSemanal.setCellValue("CH semanal");
        cellCHSemanal.setCellStyle(subItemStyle);
        sheet.addMergedRegion(new CellRangeAddress(nextRowAtividade, nextRowAtividade, 8, 9));

        // Bordas para os subitens
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowAtividade, nextRowAtividade, 0, 7), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowAtividade, nextRowAtividade, 8, 9), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);
    }

    public static void createHeaderDetalhamentoExecucaoPlano(HSSFWorkbook workbook, HSSFSheet sheet) {
        // Estilo para o header "1.3.1 - Detalhamento - Execução de plano de trabalho para estruturação inicial de componente curricular"
        CellStyle subHeaderStyle = Style.getDefaultAtividadesHeaderCellStyle(workbook);

        // Estilo para os subitens "Curso", "Componente curricular" e "CH semanal"
        CellStyle subItemStyle = Style.getDefaultSubCellStyle(workbook);

        // Preencher o header "1.3.1 - Detalhamento - Execução de plano de trabalho para estruturação inicial de componente curricular"
        int nextRowDetalhamentoPlano = getNextRowToFill(sheet, 0);
        Row rowDetalhamentoPlano = sheet.createRow(nextRowDetalhamentoPlano);
        Cell cellDetalhamentoPlano = rowDetalhamentoPlano.createCell(0);
        cellDetalhamentoPlano.setCellValue("1.3.1 - Detalhamento - Execução de plano de trabalho para estruturação inicial de componente curricular");
        cellDetalhamentoPlano.setCellStyle(subHeaderStyle);
        sheet.addMergedRegion(new CellRangeAddress(nextRowDetalhamentoPlano, nextRowDetalhamentoPlano, 0, 9));

        // Bordas e Background "1.3.1 - Detalhamento - Execução de plano de trabalho para estruturação inicial de componente curricular"
        Style.applyBackground(workbook, new CellRangeAddress(nextRowDetalhamentoPlano, nextRowDetalhamentoPlano, 0, 9), IndexedColors.GREY_25_PERCENT);
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowDetalhamentoPlano, nextRowDetalhamentoPlano, 0, 9), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

        // Preencher os subitens do header "1.3.1 - Detalhamento - Execução de plano de trabalho para estruturação inicial de componente curricular"
        int nextRowSubItens = nextRowDetalhamentoPlano + 1;

        // Subitem "Curso"
        Row rowCurso = sheet.createRow(nextRowSubItens);
        Cell cellCurso = rowCurso.createCell(0);
        cellCurso.setCellValue("Curso");
        cellCurso.setCellStyle(subItemStyle);
        sheet.addMergedRegion(new CellRangeAddress(nextRowSubItens, nextRowSubItens, 0, 2));

        // Subitem "Componente curricular"
        Cell cellComponenteCurricular = rowCurso.createCell(3);
        cellComponenteCurricular.setCellValue("Componente curricular");
        cellComponenteCurricular.setCellStyle(subItemStyle);
        sheet.addMergedRegion(new CellRangeAddress(nextRowSubItens, nextRowSubItens, 3, 7));

        // Subitem "CH semanal"
        Cell cellCHSemanal = rowCurso.createCell(8);
        cellCHSemanal.setCellValue("CH semanal");
        cellCHSemanal.setCellStyle(subItemStyle);
        sheet.addMergedRegion(new CellRangeAddress(nextRowSubItens, nextRowSubItens, 8, 9));

        // Bordas para os subitens
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowSubItens, nextRowSubItens, 0, 2), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowSubItens, nextRowSubItens, 3, 7), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowSubItens, nextRowSubItens, 8, 9), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);
    }

    public static void createHeaderCoordenacaoParticipacaoAcoesEnsino(HSSFWorkbook workbook, HSSFSheet sheet) {
        // Estilo para o header "1.3.2. Detalhamento - Coordenação e Participação em Ações de Ensino – Programas, Projetos e Eventos"
        CellStyle subHeaderStyle = Style.getDefaultAtividadesHeaderCellStyle(workbook);

        // Estilo para os subitens "Titulo da Ação", "Tipo da Ação", "Número de Cadastro", "Tipo de Atuação" e "CH semanal"
        CellStyle subItemStyle = Style.getDefaultSubCellStyle(workbook);

        // Preencher o header "1.3.2. Detalhamento - Coordenação e Participação em Ações de Ensino – Programas, Projetos e Eventos"
        int nextRowDetalhamentoAcoes = getNextRowToFill(sheet, 0);
        Row rowDetalhamentoAcoes = sheet.createRow(nextRowDetalhamentoAcoes);
        Cell cellDetalhamentoAcoes = rowDetalhamentoAcoes.createCell(0);
        cellDetalhamentoAcoes.setCellValue("1.3.2. Detalhamento - Coordenação e Participação em Ações de Ensino – Programas, Projetos e Eventos");
        cellDetalhamentoAcoes.setCellStyle(subHeaderStyle);
        sheet.addMergedRegion(new CellRangeAddress(nextRowDetalhamentoAcoes, nextRowDetalhamentoAcoes, 0, 9));

        // Bordas e Background "1.3.2. Detalhamento - Coordenação e Participação em Ações de Ensino – Programas, Projetos e Eventos"
        Style.applyBackground(workbook, new CellRangeAddress(nextRowDetalhamentoAcoes, nextRowDetalhamentoAcoes, 0, 9), IndexedColors.GREY_25_PERCENT);
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowDetalhamentoAcoes, nextRowDetalhamentoAcoes, 0, 9), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);


        // Preencher os subitens do header "1.3.2. Detalhamento - Coordenação e Participação em Ações de Ensino – Programas, Projetos e Eventos"
        int nextRowSubItens = nextRowDetalhamentoAcoes + 1;

        // Subitem "Titulo da Ação"
        Row rowTituloAcao = sheet.createRow(nextRowSubItens);
        Cell cellTituloAcao = rowTituloAcao.createCell(0);
        cellTituloAcao.setCellValue("Titulo da Ação");
        cellTituloAcao.setCellStyle(subItemStyle);
        sheet.addMergedRegion(new CellRangeAddress(nextRowSubItens, nextRowSubItens, 0, 1));
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowSubItens, nextRowSubItens, 0, 1), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

        // Subitem "Tipo da Ação"
        Cell cellTipoAcao = rowTituloAcao.createCell(2);
        cellTipoAcao.setCellValue("Tipo da Ação");
        cellTipoAcao.setCellStyle(subItemStyle);
        sheet.addMergedRegion(new CellRangeAddress(nextRowSubItens, nextRowSubItens, 2, 2));
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowSubItens, nextRowSubItens, 2, 2), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

        // Subitem "Número de Cadastro"
        Cell cellNumeroCadastro = rowTituloAcao.createCell(3);
        cellNumeroCadastro.setCellValue("Número de Cadastro");
        cellNumeroCadastro.setCellStyle(subItemStyle);
        sheet.addMergedRegion(new CellRangeAddress(nextRowSubItens, nextRowSubItens, 3, 4));
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowSubItens, nextRowSubItens, 3, 4), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

        // Subitem "Tipo de Atuação (Coordenação ou Participação, indicando se é Bolsista se for o caso)"
        Cell cellTipoAtuacao = rowTituloAcao.createCell(5);
        cellTipoAtuacao.setCellValue("Tipo de Atuação\n(Coordenação ou Participação,\nindicando se é Bolsista se for o caso)");
        cellTipoAtuacao.setCellStyle(subItemStyle);
        sheet.addMergedRegion(new CellRangeAddress(nextRowSubItens, nextRowSubItens, 5, 7));
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowSubItens, nextRowSubItens, 5, 7), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

        // Subitem "CH semanal"
        Cell cellCHSemanal = rowTituloAcao.createCell(8);
        cellCHSemanal.setCellValue("CH semanal");
        cellCHSemanal.setCellStyle(subItemStyle);
        sheet.addMergedRegion(new CellRangeAddress(nextRowSubItens, nextRowSubItens, 8, 9));
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowSubItens, nextRowSubItens, 8, 9), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

        // Aumentar o tamanho vertical da linha dos subitens
        rowTituloAcao.setHeightInPoints(40);
    }


    public static void createHeaderMediacaoPedagogica(HSSFWorkbook workbook, HSSFSheet sheet) {
        // Estilo para o header "1.4. Mediação pedagógica"
        CellStyle subHeaderStyle = Style.getDefaultAtividadesHeaderCellStyle(workbook);

        // Estilo para os subitens "Curso", "Componente curricular" e "CH semanal"
        CellStyle subItemStyle = Style.getDefaultSubCellStyle(workbook);

        // Preencher o header "1.4. Mediação pedagógica"
        int nextRowMediacaoPedagogica = getNextRowToFill(sheet, 0);
        Row rowMediacaoPedagogica = sheet.createRow(nextRowMediacaoPedagogica);
        Cell cellMediacaoPedagogica = rowMediacaoPedagogica.createCell(0);
        cellMediacaoPedagogica.setCellValue("1.4. Mediação pedagógica");
        cellMediacaoPedagogica.setCellStyle(subHeaderStyle);
        sheet.addMergedRegion(new CellRangeAddress(nextRowMediacaoPedagogica, nextRowMediacaoPedagogica, 0, 9));

        // Bordas e Background "1.4. Mediação pedagógica"
        Style.applyBackground(workbook, new CellRangeAddress(nextRowMediacaoPedagogica, nextRowMediacaoPedagogica, 0, 9), IndexedColors.GREY_25_PERCENT);
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowMediacaoPedagogica, nextRowMediacaoPedagogica, 0, 9), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

        // Preencher os subitens do header "1.4. Mediação pedagógica"
        int nextRowSubItens = nextRowMediacaoPedagogica + 1;

        // Subitem "Curso"
        Row rowCurso = sheet.createRow(nextRowSubItens);
        Cell cellCurso = rowCurso.createCell(0);
        cellCurso.setCellValue("Curso");
        cellCurso.setCellStyle(subItemStyle);
        sheet.addMergedRegion(new CellRangeAddress(nextRowSubItens, nextRowSubItens, 0, 2));

        // Subitem "Componente curricular"
        Cell cellComponenteCurricular = rowCurso.createCell(3);
        cellComponenteCurricular.setCellValue("Componente curricular");
        cellComponenteCurricular.setCellStyle(subItemStyle);
        sheet.addMergedRegion(new CellRangeAddress(nextRowSubItens, nextRowSubItens, 3, 7));

        // Subitem "CH semanal"
        Cell cellCHSemanal = rowCurso.createCell(8);
        cellCHSemanal.setCellValue("CH semanal");
        cellCHSemanal.setCellStyle(subItemStyle);
        sheet.addMergedRegion(new CellRangeAddress(nextRowSubItens, nextRowSubItens, 8, 9));

        // Bordas para os subitens
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowSubItens, nextRowSubItens, 0, 2), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowSubItens, nextRowSubItens, 3, 7), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowSubItens, nextRowSubItens, 8, 9), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);
    }

    public static void createHeaderAtividadesPesquisa(HSSFWorkbook workbook, HSSFSheet sheet) {
        // Estilo para o header "2. Atividades de Pesquisa"
        CellStyle headerStyle = Style.getDefaultAtividadesHeaderCellStyle(workbook);

        // Preencher o header "2. Atividades de Pesquisa"
        int nextRowAtividadesPesquisa = getNextRowToFill(sheet, 0);
        Row rowAtividadesPesquisa = sheet.createRow(nextRowAtividadesPesquisa);
        Cell cellAtividadesPesquisa = rowAtividadesPesquisa.createCell(0);
        cellAtividadesPesquisa.setCellValue("2. Atividades de Pesquisa");
        cellAtividadesPesquisa.setCellStyle(headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(nextRowAtividadesPesquisa, nextRowAtividadesPesquisa, 0, 9));

        // Bordas e Background "2. Atividades de Pesquisa"
        Style.applyBackground(workbook, new CellRangeAddress(nextRowAtividadesPesquisa, nextRowAtividadesPesquisa, 0, 9), IndexedColors.YELLOW);
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowAtividadesPesquisa, nextRowAtividadesPesquisa, 0, 9), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

        // Preencher os subitens do header "2. Atividades de Pesquisa"
        int nextRowSubItens = nextRowAtividadesPesquisa + 1;

        // Subitem "Atividade"
        Row rowAtividade = sheet.createRow(nextRowSubItens);
        Cell cellAtividade = rowAtividade.createCell(0);
        cellAtividade.setCellValue("Atividade");
        cellAtividade.setCellStyle(Style.getDefaultSubCellStyle(workbook));
        sheet.addMergedRegion(new CellRangeAddress(nextRowSubItens, nextRowSubItens, 0, 7));

        // Subitem "CH semanal"
        Cell cellChSemanal = rowAtividade.createCell(8);
        cellChSemanal.setCellValue("CH semanal");
        cellChSemanal.setCellStyle(Style.getDefaultSubCellStyle(workbook));
        sheet.addMergedRegion(new CellRangeAddress(nextRowSubItens, nextRowSubItens, 8, 9));

        // Bordas para os subitens
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowSubItens, nextRowSubItens, 0, 7), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowSubItens, nextRowSubItens, 8, 9), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);
    }

    public static void createHeaderDetalhamentoPesquisa(HSSFWorkbook workbook, HSSFSheet sheet) {
        // Estilo para o header "2.1. Detalhamento - Coordenação e Participação em Ações de Pesquisa"
        CellStyle subHeaderStyle = Style.getDefaultAtividadesHeaderCellStyle(workbook);
        CellStyle subItemStyle = Style.getDefaultSubCellStyle(workbook);

        // Preencher o header "2.1. Detalhamento - Coordenação e Participação em Ações de Pesquisa"
        int nextRowDetalhamentoPesquisa = getNextRowToFill(sheet, 0);
        Row rowDetalhamentoPesquisa = sheet.createRow(nextRowDetalhamentoPesquisa);
        Cell cellDetalhamentoPesquisa = rowDetalhamentoPesquisa.createCell(0);
        cellDetalhamentoPesquisa.setCellValue("2.1. Detalhamento - Coordenação e Participação em Ações de Pesquisa");
        cellDetalhamentoPesquisa.setCellStyle(subHeaderStyle);
        sheet.addMergedRegion(new CellRangeAddress(nextRowDetalhamentoPesquisa, nextRowDetalhamentoPesquisa, 0, 9));

        // Bordas e Background "2.1. Detalhamento - Coordenação e Participação em Ações de Pesquisa"
        Style.applyBackground(workbook, new CellRangeAddress(nextRowDetalhamentoPesquisa, nextRowDetalhamentoPesquisa, 0, 9), IndexedColors.GREY_25_PERCENT);
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowDetalhamentoPesquisa, nextRowDetalhamentoPesquisa, 0, 9), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

        // Preencher os subitens do header "2.1. Detalhamento - Coordenação e Participação em Ações de Pesquisa"
        int nextRowSubItens = nextRowDetalhamentoPesquisa + 1;

        // Subitem "Titulo da Ação"
        Row rowTituloAcao = sheet.createRow(nextRowSubItens);
        Cell cellTituloAcao = rowTituloAcao.createCell(0);
        cellTituloAcao.setCellValue("Titulo da Ação");
        cellTituloAcao.setCellStyle(subItemStyle);
        sheet.addMergedRegion(new CellRangeAddress(nextRowSubItens, nextRowSubItens, 0, 1));
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowSubItens, nextRowSubItens, 0, 1), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

        // Subitem "Tipo da Ação"
        Cell cellTipoAcao = rowTituloAcao.createCell(2);
        cellTipoAcao.setCellValue("Tipo da Ação");
        cellTipoAcao.setCellStyle(subItemStyle);
        sheet.addMergedRegion(new CellRangeAddress(nextRowSubItens, nextRowSubItens, 2, 2));
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowSubItens, nextRowSubItens, 2, 2), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

        // Subitem "Número de Cadastro"
        Cell cellNumeroCadastro = rowTituloAcao.createCell(3);
        cellNumeroCadastro.setCellValue("Número de Cadastro");
        cellNumeroCadastro.setCellStyle(subItemStyle);
        sheet.addMergedRegion(new CellRangeAddress(nextRowSubItens, nextRowSubItens, 3, 4));
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowSubItens, nextRowSubItens, 3, 4), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

        // Subitem "Tipo de Atuação (Coordenação ou Participação, indicando se é Bolsista se for o caso)"
        Cell cellTipoAtuacao = rowTituloAcao.createCell(5);
        cellTipoAtuacao.setCellValue("Tipo de Atuação\n(Coordenação ou Participação,\nindicando se é Bolsista se for o caso)");
        cellTipoAtuacao.setCellStyle(subItemStyle);
        sheet.addMergedRegion(new CellRangeAddress(nextRowSubItens, nextRowSubItens, 5, 7));
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowSubItens, nextRowSubItens, 5, 7), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

        // Subitem "CH semanal"
        Cell cellCHSemanal = rowTituloAcao.createCell(8);
        cellCHSemanal.setCellValue("CH semanal");
        cellCHSemanal.setCellStyle(subItemStyle);
        sheet.addMergedRegion(new CellRangeAddress(nextRowSubItens, nextRowSubItens, 8, 9));
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowSubItens, nextRowSubItens, 8, 9), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

        // Aumentar o tamanho vertical da linha dos subitens
        rowTituloAcao.setHeightInPoints(40);
    }

    public static void createHeaderAtividadesExtensao(HSSFWorkbook workbook, HSSFSheet sheet) {
        // Estilo para o header "3. Atividades de Extensão"
        CellStyle subHeaderStyle = Style.getDefaultAtividadesHeaderCellStyle(workbook);
        CellStyle subItemStyle = Style.getDefaultSubCellStyle(workbook);

        // Preencher o header "3. Atividades de Extensão"
        int nextRowAtividadesExtensao = getNextRowToFill(sheet, 0);
        Row rowAtividadesExtensao = sheet.createRow(nextRowAtividadesExtensao);
        Cell cellAtividadesExtensao = rowAtividadesExtensao.createCell(0);
        cellAtividadesExtensao.setCellValue("3. Atividades de Extensão");
        cellAtividadesExtensao.setCellStyle(subHeaderStyle);
        sheet.addMergedRegion(new CellRangeAddress(nextRowAtividadesExtensao, nextRowAtividadesExtensao, 0, 9));

        // Bordas e Background "3. Atividades de Extensão"
        Style.applyBackground(workbook, new CellRangeAddress(nextRowAtividadesExtensao, nextRowAtividadesExtensao, 0, 9), IndexedColors.YELLOW);
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowAtividadesExtensao, nextRowAtividadesExtensao, 0, 9), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

        // Preencher os subitens do header "3. Atividades de Extensão"
        int nextRowSubItens = nextRowAtividadesExtensao + 1;

        // Subitem "Atividade"
        Row rowAtividade = sheet.createRow(nextRowSubItens);
        Cell cellAtividade = rowAtividade.createCell(0);
        cellAtividade.setCellValue("Atividade");
        cellAtividade.setCellStyle(subItemStyle);
        sheet.addMergedRegion(new CellRangeAddress(nextRowSubItens, nextRowSubItens, 0, 7));
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowSubItens, nextRowSubItens, 0, 7), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

        // Subitem "CH semanal"
        Cell cellCHSemanal = rowAtividade.createCell(8);
        cellCHSemanal.setCellValue("CH semanal");
        cellCHSemanal.setCellStyle(subItemStyle);
        sheet.addMergedRegion(new CellRangeAddress(nextRowSubItens, nextRowSubItens, 8, 9));
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowSubItens, nextRowSubItens, 8, 9), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);
    }

    public static void createHeaderCoordenacaoParticipacaoAcoesExtensao(HSSFWorkbook workbook, HSSFSheet sheet) {
        // Estilo para o header "3.1. Detalhamento - Coordenação e Participação em Ações de Extensão"
        CellStyle subHeaderStyle = Style.getDefaultAtividadesHeaderCellStyle(workbook);

        // Estilo para os subitens "Titulo da Ação", "Tipo da Ação", "Número de Cadastro", "Tipo de Atuação" e "CH semanal"
        CellStyle subItemStyle = Style.getDefaultSubCellStyle(workbook);

        // Preencher o header "3.1. Detalhamento - Coordenação e Participação em Ações de Extensão"
        int nextRowDetalhamentoAcoesExtensao = getNextRowToFill(sheet, 0);
        Row rowDetalhamentoAcoesExtensao = sheet.createRow(nextRowDetalhamentoAcoesExtensao);
        Cell cellDetalhamentoAcoesExtensao = rowDetalhamentoAcoesExtensao.createCell(0);
        cellDetalhamentoAcoesExtensao.setCellValue("3.1. Detalhamento - Coordenação e Participação em Ações de Extensão");
        cellDetalhamentoAcoesExtensao.setCellStyle(subHeaderStyle);
        sheet.addMergedRegion(new CellRangeAddress(nextRowDetalhamentoAcoesExtensao, nextRowDetalhamentoAcoesExtensao, 0, 9));

        // Bordas e Background "3.1. Detalhamento - Coordenação e Participação em Ações de Extensão"
        Style.applyBackground(workbook, new CellRangeAddress(nextRowDetalhamentoAcoesExtensao, nextRowDetalhamentoAcoesExtensao, 0, 9), IndexedColors.GREY_25_PERCENT);
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowDetalhamentoAcoesExtensao, nextRowDetalhamentoAcoesExtensao, 0, 9), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

        // Preencher os subitens do header "3.1. Detalhamento - Coordenação e Participação em Ações de Extensão"
        int nextRowSubItensExtensao = nextRowDetalhamentoAcoesExtensao + 1;

        // Subitem "Titulo da Ação"
        Row rowTituloAcaoExtensao = sheet.createRow(nextRowSubItensExtensao);
        Cell cellTituloAcaoExtensao = rowTituloAcaoExtensao.createCell(0);
        cellTituloAcaoExtensao.setCellValue("Titulo da Ação");
        cellTituloAcaoExtensao.setCellStyle(subItemStyle);
        sheet.addMergedRegion(new CellRangeAddress(nextRowSubItensExtensao, nextRowSubItensExtensao, 0, 1));
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowSubItensExtensao, nextRowSubItensExtensao, 0, 1), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

        // Subitem "Tipo da Ação"
        Cell cellTipoAcaoExtensao = rowTituloAcaoExtensao.createCell(2);
        cellTipoAcaoExtensao.setCellValue("Tipo da Ação");
        cellTipoAcaoExtensao.setCellStyle(subItemStyle);
        sheet.addMergedRegion(new CellRangeAddress(nextRowSubItensExtensao, nextRowSubItensExtensao, 2, 2));
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowSubItensExtensao, nextRowSubItensExtensao, 2, 2), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

        // Subitem "Número de Cadastro"
        Cell cellNumeroCadastroExtensao = rowTituloAcaoExtensao.createCell(3);
        cellNumeroCadastroExtensao.setCellValue("Número de Cadastro");
        cellNumeroCadastroExtensao.setCellStyle(subItemStyle);
        sheet.addMergedRegion(new CellRangeAddress(nextRowSubItensExtensao, nextRowSubItensExtensao, 3, 4));
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowSubItensExtensao, nextRowSubItensExtensao, 3, 4), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

        // Subitem "Tipo de Atuação (Coordenação ou Participação, indicando se é Bolsista se for o caso)"
        Cell cellTipoAtuacaoExtensao = rowTituloAcaoExtensao.createCell(5);
        cellTipoAtuacaoExtensao.setCellValue("Tipo de Atuação\n(Coordenação ou Participação,\nindicando se é Bolsista se for o caso)");
        cellTipoAtuacaoExtensao.setCellStyle(subItemStyle);
        sheet.addMergedRegion(new CellRangeAddress(nextRowSubItensExtensao, nextRowSubItensExtensao, 5, 7));
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowSubItensExtensao, nextRowSubItensExtensao, 5, 7), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

        // Subitem "CH semanal"
        Cell cellCHSemanalExtensao = rowTituloAcaoExtensao.createCell(8);
        cellCHSemanalExtensao.setCellValue("CH semanal");
        cellCHSemanalExtensao.setCellStyle(subItemStyle);
        sheet.addMergedRegion(new CellRangeAddress(nextRowSubItensExtensao, nextRowSubItensExtensao, 8, 9));
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowSubItensExtensao, nextRowSubItensExtensao, 8, 9), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

        // Aumentar o tamanho vertical da linha dos subitens
        rowTituloAcaoExtensao.setHeightInPoints(40);
    }

    public static void createHeaderAtividadesGestao(HSSFWorkbook workbook, HSSFSheet sheet) {
        // Estilo para o header "4. Atividades de Gestão"
        CellStyle headerStyle = Style.getDefaultAtividadesHeaderCellStyle(workbook);
        headerStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());

        // Preencher o header "4. Atividades de Gestão"
        int nextRow = getNextRowToFill(sheet, 0);
        Row rowHeader = sheet.createRow(nextRow);
        Cell cellHeader = rowHeader.createCell(0);
        cellHeader.setCellValue("4. Atividades de Gestão");
        cellHeader.setCellStyle(headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(nextRow, nextRow, 0, 9));

        // Aplicar bordas para o header
        Style.applyBackground(workbook, new CellRangeAddress(nextRow, nextRow, 0, 9), IndexedColors.YELLOW);
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRow, nextRow, 0, 9), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

        // Estilo para os subitens/subheaders
        CellStyle subHeaderStyle = getDefaultSubCellStyle(workbook);

        // Preencher os subitens/subheaders
        int nextRowSubItem = getNextRowToFill(sheet, 0) - 1;

        // Subitem "Descrição da Atribuição"
        Row rowDescricaoAtribuicao = sheet.createRow(nextRowSubItem);
        Cell cellDescricaoAtribuicao = rowDescricaoAtribuicao.createCell(0);
        cellDescricaoAtribuicao.setCellValue("Descrição da Atribuição");
        cellDescricaoAtribuicao.setCellStyle(subHeaderStyle);
        sheet.addMergedRegion(new CellRangeAddress(nextRowSubItem, nextRowSubItem, 0, 1));
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowSubItem, nextRowSubItem, 0, 1), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

        // Subitem "Número da Portaria"
        Cell cellNumeroPortaria = rowDescricaoAtribuicao.createCell(2);
        cellNumeroPortaria.setCellValue("Número da Portaria");
        cellNumeroPortaria.setCellStyle(subHeaderStyle);
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowSubItem, nextRowSubItem, 2, 2), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

        // Subitem "Data de início"
        Cell cellDataInicio = rowDescricaoAtribuicao.createCell(3);
        cellDataInicio.setCellValue("Data de início");
        cellDataInicio.setCellStyle(subHeaderStyle);
        sheet.addMergedRegion(new CellRangeAddress(nextRowSubItem, nextRowSubItem, 3, 4));
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowSubItem, nextRowSubItem, 3, 4), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

        // Subitem "Período de vigência"
        Cell cellPeriodoVigencia = rowDescricaoAtribuicao.createCell(5);
        cellPeriodoVigencia.setCellValue("Período de vigência");
        cellPeriodoVigencia.setCellStyle(subHeaderStyle);
        sheet.addMergedRegion(new CellRangeAddress(nextRowSubItem, nextRowSubItem, 5, 7));
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowSubItem, nextRowSubItem, 5, 7), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

        // Subitem "CH semanal"
        Cell cellCHSemanal = rowDescricaoAtribuicao.createCell(8);
        cellCHSemanal.setCellValue("CH semanal");
        cellCHSemanal.setCellStyle(subHeaderStyle);
        sheet.addMergedRegion(new CellRangeAddress(nextRowSubItem, nextRowSubItem, 8, 9));
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowSubItem, nextRowSubItem, 8, 9), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

        // Aumentar o tamanho vertical da linha dos subitens
        rowDescricaoAtribuicao.setHeightInPoints(30);
    }

    public static void createHeaderAtividadesRepresentacao(HSSFWorkbook workbook, HSSFSheet sheet) {
        // Estilo para o header "5. Atividades de Representação"
        CellStyle headerStyle = Style.getDefaultAtividadesHeaderCellStyle(workbook);
        headerStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());

        // Preencher o header "5. Atividades de Representação"
        int nextRow = getNextRowToFill(sheet, 0);
        Row rowHeader = sheet.createRow(nextRow);
        Cell cellHeader = rowHeader.createCell(0);
        cellHeader.setCellValue("5. Atividades de Representação");
        cellHeader.setCellStyle(headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(nextRow, nextRow, 0, 9));

        // Aplicar bordas para o header
        Style.applyBackground(workbook, new CellRangeAddress(nextRow, nextRow, 0, 9), IndexedColors.YELLOW);
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRow, nextRow, 0, 9), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

        // Estilo para os subitens/subheaders
        CellStyle subHeaderStyle = Style.getDefaultSubCellStyle(workbook);

        // Preencher os subitens/subheaders
        int nextRowSubItem = getNextRowToFill(sheet, 0) - 1;

        // Subitem "Descrição da Atribuição"
        Row rowDescricaoAtribuicao = sheet.createRow(nextRowSubItem);
        Cell cellDescricaoAtribuicao = rowDescricaoAtribuicao.createCell(0);
        cellDescricaoAtribuicao.setCellValue("Descrição da Atribuição");
        cellDescricaoAtribuicao.setCellStyle(subHeaderStyle);
        sheet.addMergedRegion(new CellRangeAddress(nextRowSubItem, nextRowSubItem, 0, 1));
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowSubItem, nextRowSubItem, 0, 1), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

        // Subitem "Número da Portaria"
        Cell cellNumeroPortaria = rowDescricaoAtribuicao.createCell(2);
        cellNumeroPortaria.setCellValue("Número da Portaria");
        cellNumeroPortaria.setCellStyle(subHeaderStyle);
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowSubItem, nextRowSubItem, 2, 2), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

        // Subitem "Data de início"
        Cell cellDataInicio = rowDescricaoAtribuicao.createCell(3);
        cellDataInicio.setCellValue("Data de início");
        cellDataInicio.setCellStyle(subHeaderStyle);
        sheet.addMergedRegion(new CellRangeAddress(nextRowSubItem, nextRowSubItem, 3, 4));
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowSubItem, nextRowSubItem, 3, 4), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

        // Subitem "Período de vigência"
        Cell cellPeriodoVigencia = rowDescricaoAtribuicao.createCell(5);
        cellPeriodoVigencia.setCellValue("Período de vigência");
        cellPeriodoVigencia.setCellStyle(subHeaderStyle);
        sheet.addMergedRegion(new CellRangeAddress(nextRowSubItem, nextRowSubItem, 5, 7));
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowSubItem, nextRowSubItem, 5, 7), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

        // Subitem "CH semanal"
        Cell cellCHSemanal = rowDescricaoAtribuicao.createCell(8);
        cellCHSemanal.setCellValue("CH semanal");
        cellCHSemanal.setCellStyle(subHeaderStyle);
        sheet.addMergedRegion(new CellRangeAddress(nextRowSubItem, nextRowSubItem, 8, 9));
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowSubItem, nextRowSubItem, 8, 9), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

        // Aumentar o tamanho vertical da linha dos subitens
        rowDescricaoAtribuicao.setHeightInPoints(30);
    }

    public static void createHeaderOutrasAtividades(HSSFWorkbook workbook, HSSFSheet sheet) {
        // Estilo para o header "6. Outras atividades"
        CellStyle headerStyle = Style.getDefaultAtividadesHeaderCellStyle(workbook);
        headerStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());

        // Preencher o header "6. Outras atividades"
        int nextRow = getNextRowToFill(sheet, 0);
        Row rowHeader = sheet.createRow(nextRow);
        Cell cellHeader = rowHeader.createCell(0);
        cellHeader.setCellValue("6. Outras atividades");
        cellHeader.setCellStyle(headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(nextRow, nextRow, 0, 9));

        // Aplicar bordas para o header
        Style.applyBackground(workbook, new CellRangeAddress(nextRow, nextRow, 0, 9), IndexedColors.YELLOW);
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRow, nextRow, 0, 9), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

        // Estilo para os subitens/subheaders
        CellStyle subHeaderStyle = Style.getDefaultSubCellStyle(workbook);

        // Preencher os subitens/subheaders
        int nextRowSubItem = getNextRowToFill(sheet, 0) - 1;

        // Subitem "Tipo"
        Row rowTipo = sheet.createRow(nextRowSubItem);
        Cell cellTipo = rowTipo.createCell(0);
        cellTipo.setCellValue("Tipo");
        cellTipo.setCellStyle(subHeaderStyle);
        sheet.addMergedRegion(new CellRangeAddress(nextRowSubItem, nextRowSubItem, 0, 4));
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowSubItem, nextRowSubItem, 0, 4), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

        // Subitem "Número da Portaria"
        Cell cellNumeroPortaria = rowTipo.createCell(5);
        cellNumeroPortaria.setCellValue("Número da Portaria");
        cellNumeroPortaria.setCellStyle(subHeaderStyle);
        sheet.addMergedRegion(new CellRangeAddress(nextRowSubItem, nextRowSubItem, 5, 7));
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowSubItem, nextRowSubItem, 5, 7), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

        // Subitem "CH semanal"
        Cell cellCHSemanal = rowTipo.createCell(8);
        cellCHSemanal.setCellValue("CH semanal");
        cellCHSemanal.setCellStyle(subHeaderStyle);
        sheet.addMergedRegion(new CellRangeAddress(nextRowSubItem, nextRowSubItem, 8, 9));
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowSubItem, nextRowSubItem, 8, 9), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

        // Aumentar o tamanho vertical da linha dos subitens
        rowTipo.setHeightInPoints(30);
    }

}