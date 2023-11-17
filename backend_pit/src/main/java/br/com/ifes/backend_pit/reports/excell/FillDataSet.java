package br.com.ifes.backend_pit.reports.excell;

import br.com.ifes.backend_pit.reports.dto.atividades.ensino.apoio_ensino.ApoioEnsino;
import br.com.ifes.backend_pit.reports.dto.atividades.ensino.apoio_ensino.AtividadesApoioEnsino;
import br.com.ifes.backend_pit.reports.dto.atividades.ensino.aula.Aula;
import br.com.ifes.backend_pit.reports.dto.atividades.ensino.aula.Aulas;
import br.com.ifes.backend_pit.reports.dto.atividades.ensino.detalhamento_componente_curricular.DetalhamentoAula;
import br.com.ifes.backend_pit.reports.dto.atividades.ensino.detalhamento_componente_curricular.DetalhamentosComponenteCurricular;
import br.com.ifes.backend_pit.reports.dto.atividades.ensino.detalhamento_projetos.DetalhamentoProjeto;
import br.com.ifes.backend_pit.reports.dto.atividades.ensino.detalhamento_projetos.DetalhamentosProjetos;
import br.com.ifes.backend_pit.reports.dto.atividades.ensino.mediacao_pedagogica.MediacaoPedagogica;
import br.com.ifes.backend_pit.reports.dto.atividades.ensino.mediacao_pedagogica.MediacoesPedagogicas;
import br.com.ifes.backend_pit.reports.dto.atividades.ensino.planejamento_manutencao_ensino.AtividadesPlanejamentoManutencaoEnsino;
import br.com.ifes.backend_pit.reports.dto.atividades.ensino.planejamento_manutencao_ensino.PlanejamentoManutencaoEnsino;
import br.com.ifes.backend_pit.reports.dto.atividades.extensao.atividade.AtividadeExtensao;
import br.com.ifes.backend_pit.reports.dto.atividades.extensao.atividade.AtividadesExtensao;
import br.com.ifes.backend_pit.reports.dto.atividades.extensao.detalhamento.DetalhamentoExtensao;
import br.com.ifes.backend_pit.reports.dto.atividades.extensao.detalhamento.DetalhamentosExtensao;
import br.com.ifes.backend_pit.reports.dto.atividades.gestao.AtividadesGestao;
import br.com.ifes.backend_pit.reports.dto.atividades.gestao.Gestao;
import br.com.ifes.backend_pit.reports.dto.atividades.outras.Outra;
import br.com.ifes.backend_pit.reports.dto.atividades.outras.OutrasAtividades;
import br.com.ifes.backend_pit.reports.dto.atividades.pesquisa.atividade.AtividadesPesquisa;
import br.com.ifes.backend_pit.reports.dto.atividades.pesquisa.atividade.Pesquisa;
import br.com.ifes.backend_pit.reports.dto.atividades.pesquisa.detalhamento.DetalhamentoPesquisa;
import br.com.ifes.backend_pit.reports.dto.atividades.pesquisa.detalhamento.DetalhamentosPesquisas;
import br.com.ifes.backend_pit.reports.dto.atividades.representacao.AtividadesRepresentacao;
import br.com.ifes.backend_pit.reports.dto.atividades.representacao.Representacao;
import br.com.ifes.backend_pit.reports.dto.cadastro.DadosCadastraisDTO;
import br.com.ifes.backend_pit.reports.dto.resumo.ResumoDTO;
import br.com.ifes.backend_pit.reports.styles.Style;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import static br.com.ifes.backend_pit.reports.styles.Style.*;
import static br.com.ifes.backend_pit.reports.util.Util.getNextRowToFill;

public class FillDataSet {
    public static void fillDadosCadastrais(HSSFWorkbook workbook, HSSFSheet sheet, DadosCadastraisDTO dadosCadastrais) {
        // Estilo para os dados cadastrais
        CellStyle dadosCellStyle = getDefaultCellStyle(workbook);

        // Labels das colunas A (linhas 9 até 17)
        String[] labels = {"Campus:", "Departamento:", "Nome:", "SIAPE:", "Jornada de trabalho (horas):",
                "Efetivo:", "Em Afastamento:", "Área Principal de Atuação:", "Titulação:"};

        // Preenche os labels nas colunas A (linhas 9 até 17)
        for (int i = 0; i < labels.length; i++) {
            Row row = sheet.getRow(i + 8); // Linha 9 corresponde ao índice 8
            if (row == null) {
                row = sheet.createRow(i + 8);

            }
            Cell cellLabel = row.createCell(0);
            cellLabel.setCellValue(labels[i]);
            cellLabel.setCellStyle(dadosCellStyle);
            applyBorders(sheet, workbook, new CellRangeAddress(i + 8, i + 8, 0, 0), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN); // Aplica bordas na coluna A
        }

        // Dados do DTO nas colunas B, C e D (linhas 9 até 17)
        String[] dados = {
                dadosCadastrais.getCampus(),
                dadosCadastrais.getDepartamento(),
                dadosCadastrais.getNome(),
                dadosCadastrais.getSiape(),
                dadosCadastrais.getJornadaTrabalho(),
                dadosCadastrais.getEfetivo(),
                dadosCadastrais.getEmAfastamento(),
                dadosCadastrais.getAreaPrincipalAtuacao(),
                dadosCadastrais.getTitulacao()
        };

        // Preenche os dados do DTO nas colunas B, C e D (linhas 9 até 17)
        for (int i = 0; i < dados.length; i++) {
            Row row = sheet.getRow(i + 8); // Linha 9 corresponde ao índice 8
            if (row == null) {
                row = sheet.createRow(i + 8);
            }
            for (int j = 1; j <= 3; j++) { // Colunas B, C e D (índices 1, 2 e 3)
                Cell cell = row.createCell(j);
                cell.setCellValue(dados[i]);
                cell.setCellStyle(dadosCellStyle);
                applyBorders(sheet, workbook, new CellRangeAddress(i + 8, i + 8, j, j), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN); // Aplica bordas nas colunas B, C e D
            }
            sheet.addMergedRegion(new CellRangeAddress(i + 8, i + 8, 1, 3)); // Mescla as células B, C e D
        }
    }

    public static void fillContentResumoCHTotal(HSSFWorkbook workbook, HSSFSheet sheet, ResumoDTO resumoDTO) {
        // Estilo para o resumo CH total
        CellStyle resumoCHStyle = getDefaultAtividadesHeaderCellStyle(workbook);

        // Atividades de Ensino
        Row rowF10 = sheet.getRow(9);
        if (rowF10 == null) {
            rowF10 = sheet.createRow(9);
        }
        Cell cellF10 = rowF10.createCell(5);
        cellF10.setCellValue("1. Atividades de Ensino");
        cellF10.setCellStyle(resumoCHStyle);
        sheet.addMergedRegion(new CellRangeAddress(9, 10, 5, 5));
        applyBorders(sheet, workbook, new CellRangeAddress(9, 10, 5, 5), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);
        applyBorders(sheet, workbook, new CellRangeAddress(9, 10, 6, 6), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

        // Atividades de Pesquisa
        Row rowF12 = sheet.getRow(11);
        if (rowF12 == null) {
            rowF12 = sheet.createRow(11);
        }
        Cell cellF12 = rowF12.createCell(5);
        cellF12.setCellValue("2. Atividades de Pesquisa");
        cellF12.setCellStyle(resumoCHStyle);
        sheet.addMergedRegion(new CellRangeAddress(11, 12, 5, 5));
        applyBorders(sheet, workbook, new CellRangeAddress(11, 12, 5, 5), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);
        applyBorders(sheet, workbook, new CellRangeAddress(11, 12, 6, 6), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

        // Atividades de Extensão
        Row rowF14 = sheet.getRow(13);
        if (rowF14 == null) {
            rowF14 = sheet.createRow(13);
        }
        Cell cellF14 = rowF14.createCell(5);
        cellF14.setCellValue("3. Atividades de Extensão");
        cellF14.setCellStyle(resumoCHStyle);
        sheet.addMergedRegion(new CellRangeAddress(13, 14, 5, 5));
        applyBorders(sheet, workbook, new CellRangeAddress(13, 14, 5, 5), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);
        applyBorders(sheet, workbook, new CellRangeAddress(13, 14, 6, 6), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

        // Carga Horária de Ensino
        Row rowG10 = sheet.getRow(9);
        if (rowG10 == null) {
            rowG10 = sheet.createRow(9);
        }
        Cell cellG10 = rowG10.createCell(6);
        cellG10.setCellValue(resumoDTO.getCargasHorarias().get(0));
        cellG10.setCellStyle(resumoCHStyle);
        sheet.addMergedRegion(new CellRangeAddress(9, 10, 6, 6));
        applyBorders(sheet, workbook, new CellRangeAddress(9, 10, 6, 6), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

        // Carga Horária de Pesquisa
        Row rowG12 = sheet.getRow(11);
        if (rowG12 == null) {
            rowG12 = sheet.createRow(11);
        }
        Cell cellG12 = rowG12.createCell(6);
        cellG12.setCellValue(resumoDTO.getCargasHorarias().get(1));
        cellG12.setCellStyle(resumoCHStyle);
        sheet.addMergedRegion(new CellRangeAddress(11, 12, 6, 6));
        applyBorders(sheet, workbook, new CellRangeAddress(11, 12, 6, 6), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

        // Carga Horária de Extensão
        Row rowG14 = sheet.getRow(13);
        if (rowG14 == null) {
            rowG14 = sheet.createRow(13);
        }
        Cell cellG14 = rowG14.createCell(6);
        cellG14.setCellValue(resumoDTO.getCargasHorarias().get(2));
        cellG14.setCellStyle(resumoCHStyle);
        sheet.addMergedRegion(new CellRangeAddress(13, 14, 6, 6));
        applyBorders(sheet, workbook, new CellRangeAddress(13, 14, 6, 6), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

        // Atividades de Gestão
        Row rowHI10 = sheet.getRow(9);
        if (rowHI10 == null) {
            rowHI10 = sheet.createRow(9);
        }
        Cell cellH10 = rowHI10.createCell(7);
        cellH10.setCellValue("4. Atividades de Gestão");
        cellH10.setCellStyle(resumoCHStyle);
        sheet.addMergedRegion(new CellRangeAddress(9, 10, 7, 8));
        applyBorders(sheet, workbook, new CellRangeAddress(9, 10, 7, 8), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

        // Atividades de Representação
        Row rowHI12 = sheet.getRow(11);
        if (rowHI12 == null) {
            rowHI12 = sheet.createRow(11);
        }
        Cell cellH12 = rowHI12.createCell(7);
        cellH12.setCellValue("5. Atividades de Representação");
        cellH12.setCellStyle(resumoCHStyle);
        sheet.addMergedRegion(new CellRangeAddress(11, 12, 7, 8));
        applyBorders(sheet, workbook, new CellRangeAddress(11, 12, 7, 8), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

        // Outras atividades
        Row rowHI14 = sheet.getRow(13);
        if (rowHI14 == null) {
            rowHI14 = sheet.createRow(13);
        }
        Cell cellH14 = rowHI14.createCell(7);
        cellH14.setCellValue("6. Outras atividades");
        cellH14.setCellStyle(resumoCHStyle);
        sheet.addMergedRegion(new CellRangeAddress(13, 14, 7, 8));
        applyBorders(sheet, workbook, new CellRangeAddress(13, 14, 7, 8), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

        // Carga Horária de Gestão
        Row rowJ10 = sheet.getRow(9);
        if (rowJ10 == null) {
            rowJ10 = sheet.createRow(9);
        }
        Cell cellJ10 = rowJ10.createCell(9);
        cellJ10.setCellValue(resumoDTO.getCargasHorarias().get(3));
        cellJ10.setCellStyle(resumoCHStyle);
        sheet.addMergedRegion(new CellRangeAddress(9, 10, 9, 9));
        applyBorders(sheet, workbook, new CellRangeAddress(9, 10, 9, 9), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

        // Carga Horária de Representação
        Row rowJ12 = sheet.getRow(11);
        if (rowJ12 == null) {
            rowJ12 = sheet.createRow(11);
        }
        Cell cellJ12 = rowJ12.createCell(9);
        cellJ12.setCellValue(resumoDTO.getCargasHorarias().get(4));
        cellJ12.setCellStyle(resumoCHStyle);
        sheet.addMergedRegion(new CellRangeAddress(11, 12, 9, 9));
        applyBorders(sheet, workbook, new CellRangeAddress(11, 12, 9, 9), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

        // Carga Horária de Outras atividades
        Row rowJ14 = sheet.getRow(13);
        if (rowJ14 == null) {
            rowJ14 = sheet.createRow(13);
        }
        Cell cellJ14 = rowJ14.createCell(9);
        cellJ14.setCellValue(resumoDTO.getCargasHorarias().get(5));
        cellJ14.setCellStyle(resumoCHStyle);
        sheet.addMergedRegion(new CellRangeAddress(13, 14, 9, 9));
        applyBorders(sheet, workbook, new CellRangeAddress(13, 14, 9, 9), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);
    }

    public static void fillAulas(HSSFWorkbook workbook, HSSFSheet sheet, Aulas aulas) {
        // Estilo para o preenchimento das aulas
        CellStyle aulasStyle = Style.getDefaultSubCellStyle(workbook);

        // Percorre cada aula na lista de aulas
        int nextRow = getNextRowToFill(sheet, 0) - 1;
        // Preencher aulas apenas se existirem:
        if (aulas != null) {
            for (Aula aula : aulas.getAulas()) {
                // Preencher o curso na coluna A
                Row rowCurso = sheet.createRow(nextRow);
                Cell cellCurso = rowCurso.createCell(0);
                cellCurso.setCellValue(aula.getCurso());
                cellCurso.setCellStyle(aulasStyle);

                // Preencher o componente curricular na coluna D
                Cell cellComponenteCurricular = rowCurso.createCell(3);
                cellComponenteCurricular.setCellValue(aula.getComponenteCurricular());
                cellComponenteCurricular.setCellStyle(aulasStyle);

                // Preencher a chSemanal na coluna I
                Cell cellChSemanal = rowCurso.createCell(8);
                cellChSemanal.setCellValue(aula.getChSemanal());
                cellChSemanal.setCellStyle(aulasStyle);

                // Mesclar as células das colunas A até C para o curso
                sheet.addMergedRegion(new CellRangeAddress(nextRow, nextRow, 0, 2));

                // Mesclar as células das colunas D até H para o componente curricular
                sheet.addMergedRegion(new CellRangeAddress(nextRow, nextRow, 3, 7));

                // Mesclar as células das colunas I até J para a chSemanal
                sheet.addMergedRegion(new CellRangeAddress(nextRow, nextRow, 8, 9));

                // Aplicar as bordas linha a linha para cada célula na linha da aula
                Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRow, nextRow, 0, 2), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);
                Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRow, nextRow, 3, 7), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);
                Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRow, nextRow, 8, 9), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

                // Incrementar a próxima linha para a próxima aula
                nextRow++;
            }
        } else {
            aulas = new Aulas();
        }


        // Preencher o subtotal
        int nextRowSubtotal = getNextRowToFill(sheet, 0) - 1;
        Row rowSubtotal = sheet.createRow(nextRowSubtotal);

        // Preencher a coluna com a palavra "Subtotal" no range de A até H
        Cell cellSubtotalLabel = rowSubtotal.createCell(0);
        cellSubtotalLabel.setCellValue("Subtotal");
        CellStyle subtotalLabelStyle = Style.getCellStyleBoldAlighRight(workbook);
        cellSubtotalLabel.setCellStyle(subtotalLabelStyle);
        sheet.addMergedRegion(new CellRangeAddress(nextRowSubtotal, nextRowSubtotal, 0, 7));
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowSubtotal, nextRowSubtotal, 0, 7), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);


        // Preencher a coluna do valor do subtotal que está em aulas no range de I até J
        Cell cellSubtotalValue = rowSubtotal.createCell(8);
        cellSubtotalValue.setCellValue(aulas.getSubtotal());
        CellStyle subtotalValueStyle = Style.getBoldCellStyle(workbook);
        cellSubtotalValue.setCellStyle(subtotalValueStyle);
        sheet.addMergedRegion(new CellRangeAddress(nextRowSubtotal, nextRowSubtotal, 8, 9));
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowSubtotal, nextRowSubtotal, 8, 9), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);
    }

    public static void fillAtividadesPlanejamentoManutencaoEnsino(HSSFWorkbook workbook, HSSFSheet sheet, AtividadesPlanejamentoManutencaoEnsino atividades) {
        // Estilo para o preenchimento das atividades
        CellStyle atividadesStyle = Style.getDefaultCellStyle(workbook);
        // Estilo para o preenchimento dos valores de CH Semanal
        CellStyle chSemanalStyle = Style.getDefaultSubCellStyle(workbook);

        // Percorre cada atividade na lista de atividades de planejamento e manutenção de ensino
        int nextRow = getNextRowToFill(sheet, 0) - 1;
        // Preencher atividades apenas se elas existirem
        if (atividades != null) {
            for (PlanejamentoManutencaoEnsino atividade : atividades.getAtividadesPlanejamentoManutencaoEnsino()) {
                // Preencher a atividade na coluna A até H
                Row rowAtividade = sheet.createRow(nextRow);
                Cell cellAtividade = rowAtividade.createCell(0);
                cellAtividade.setCellValue(atividade.getAtividade());
                cellAtividade.setCellStyle(atividadesStyle);

                // Preencher a chSemanal na coluna I
                Cell cellChSemanal = rowAtividade.createCell(8);
                cellChSemanal.setCellValue(atividade.getChSemanal());
                cellChSemanal.setCellStyle(chSemanalStyle);

                // Mesclar as células das colunas A até H para a atividade
                sheet.addMergedRegion(new CellRangeAddress(nextRow, nextRow, 0, 7));

                // Mesclar as células das colunas I até J para a chSemanal
                sheet.addMergedRegion(new CellRangeAddress(nextRow, nextRow, 8, 9));

                // Aplicar as bordas linha a linha para cada célula na linha da atividade
                Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRow, nextRow, 0, 7), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);
                Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRow, nextRow, 8, 9), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

                // Incrementar a próxima linha para a próxima atividade
                nextRow++;
            }
        } else {
            atividades = new AtividadesPlanejamentoManutencaoEnsino();
        }


        // Preencher o subtotal
        int nextRowSubtotal = getNextRowToFill(sheet, 0) - 1;
        Row rowSubtotal = sheet.createRow(nextRowSubtotal);

        // Preencher a coluna com a palavra "Subtotal" no range de A até H
        Cell cellSubtotalLabel = rowSubtotal.createCell(0);
        cellSubtotalLabel.setCellValue("Subtotal");
        CellStyle subtotalLabelStyle = Style.getCellStyleBoldAlighRight(workbook);
        cellSubtotalLabel.setCellStyle(subtotalLabelStyle);
        sheet.addMergedRegion(new CellRangeAddress(nextRowSubtotal, nextRowSubtotal, 0, 7));
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowSubtotal, nextRowSubtotal, 0, 7), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

        // Preencher a coluna do valor do subtotal que está em atividades no range de I até J
        Cell cellSubtotalValue = rowSubtotal.createCell(8);
        cellSubtotalValue.setCellValue(atividades.getSubtotal());
        CellStyle subtotalValueStyle = Style.getBoldCellStyle(workbook);
        cellSubtotalValue.setCellStyle(subtotalValueStyle);
        sheet.addMergedRegion(new CellRangeAddress(nextRowSubtotal, nextRowSubtotal, 8, 9));
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowSubtotal, nextRowSubtotal, 8, 9), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);
    }

    public static void fillAtividadesApoioEnsino(HSSFWorkbook workbook, HSSFSheet sheet, AtividadesApoioEnsino atividades) {
        // Estilo para o preenchimento das atividades de apoio ao ensino
        CellStyle atividadesStyle = Style.getDefaultCellStyle(workbook);

        // Estilo para o preenchimento dos valores de CH Semanal
        CellStyle chSemanalStyle = Style.getDefaultSubCellStyle(workbook);

        // Percorre cada atividade na lista de atividades de apoio ao ensino
        int nextRow = getNextRowToFill(sheet, 0) - 1;
        // Preencher atividades apenas se elas existirem
        if (atividades != null) {
            for (ApoioEnsino atividade : atividades.getAtividadesPlanejamentoManutencaoEnsino()) {
                // Preencher a atividade na coluna A até H
                Row rowAtividade = sheet.createRow(nextRow);
                Cell cellAtividade = rowAtividade.createCell(0);
                cellAtividade.setCellValue(atividade.getAtividade());
                cellAtividade.setCellStyle(atividadesStyle);

                // Preencher a chSemanal na coluna I
                Cell cellChSemanal = rowAtividade.createCell(8);
                cellChSemanal.setCellValue(atividade.getChSemanal());
                cellChSemanal.setCellStyle(chSemanalStyle);

                // Mesclar as células das colunas A até H para a atividade
                sheet.addMergedRegion(new CellRangeAddress(nextRow, nextRow, 0, 7));

                // Mesclar as células das colunas I até J para a chSemanal
                sheet.addMergedRegion(new CellRangeAddress(nextRow, nextRow, 8, 9));

                // Aplicar as bordas linha a linha para cada célula na linha da atividade
                Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRow, nextRow, 0, 7), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);
                Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRow, nextRow, 8, 9), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

                // Incrementar a próxima linha para a próxima atividade
                nextRow++;
            }
        } else {
            atividades = new AtividadesApoioEnsino();
        }

        // Preencher o subtotal parcial
        int nextRowSubtotal = getNextRowToFill(sheet, 0) - 1;
        Row rowSubtotal = sheet.createRow(nextRowSubtotal);

        // Preencher a coluna com a palavra "Subtotal" no range de A até H
        Cell cellSubtotalLabel = rowSubtotal.createCell(0);
        CellStyle subtotalLabelStyle = getSubtotalParcialStyle(workbook);
        RichTextString richText = formatRichText("Subtotal (menor ou igual à carga horária de aulas + mediação pedagógica)", workbook);
        cellSubtotalLabel.setCellValue(richText);
        cellSubtotalLabel.setCellStyle(subtotalLabelStyle);
        sheet.addMergedRegion(new CellRangeAddress(nextRowSubtotal, nextRowSubtotal, 0, 7));
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowSubtotal, nextRowSubtotal, 0, 7), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

        // Preencher a coluna do valor do subtotal que está em atividades no range de I até J
        Cell cellSubtotalValue = rowSubtotal.createCell(8);
        cellSubtotalValue.setCellValue(atividades.getSubtotalParcial());
        CellStyle subtotalValueStyle = Style.getBoldCellStyle(workbook);
        cellSubtotalValue.setCellStyle(subtotalValueStyle);
        sheet.addMergedRegion(new CellRangeAddress(nextRowSubtotal, nextRowSubtotal, 8, 9));
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowSubtotal, nextRowSubtotal, 8, 9), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);


        // Percorre cada atividade na lista de atividadesPlanejamentoManutencaoEnsinoCoordenacao
        int nextRowSubTotal = getNextRowToFill(sheet, 0) - 1;

        for (ApoioEnsino atividade : atividades.getAtividadesPlanejamentoManutencaoEnsinoCoordenacao()) {
            // Preencher a atividade na coluna A até H
            Row rowAtividade = sheet.createRow(nextRowSubTotal);
            Cell cellAtividade = rowAtividade.createCell(0);
            cellAtividade.setCellValue(atividade.getAtividade());
            cellAtividade.setCellStyle(atividadesStyle);

            // Preencher a chSemanal na coluna I
            Cell cellChSemanal = rowAtividade.createCell(8);
            cellChSemanal.setCellValue(atividade.getChSemanal());
            cellChSemanal.setCellStyle(chSemanalStyle);

            // Mesclar as células das colunas A até H para a atividade
            sheet.addMergedRegion(new CellRangeAddress(nextRowSubTotal, nextRowSubTotal, 0, 7));

            // Mesclar as células das colunas I até J para a chSemanal
            sheet.addMergedRegion(new CellRangeAddress(nextRowSubTotal, nextRowSubTotal, 8, 9));

            // Aplicar as bordas linha a linha para cada célula na linha da atividade
            Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowSubTotal, nextRowSubTotal, 0, 7), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);
            Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowSubTotal, nextRowSubTotal, 8, 9), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

            // Incrementar a próxima linha para a próxima atividade
            nextRowSubTotal++;
        }

        // Preencher o subtotal
        int nextRowSub = getNextRowToFill(sheet, 0) - 1;
        Row rowSub = sheet.createRow(nextRowSub);

        // Preencher a coluna com a palavra "Subtotal" no range de A até H
        Cell cellSub = rowSub.createCell(0);
        cellSub.setCellValue("Subtotal");
        CellStyle subStyle = Style.getCellStyleBoldAlighRight(workbook);
        cellSub.setCellStyle(subStyle);
        sheet.addMergedRegion(new CellRangeAddress(nextRowSub, nextRowSub, 0, 7));
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowSub, nextRowSub, 0, 7), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

        // Preencher a coluna do valor do subtotal que está em atividades no range de I até J
        Cell cellSubValue = rowSub.createCell(8);
        cellSubValue.setCellValue(atividades.getSubtotal());
        CellStyle subValueStyle = Style.getBoldCellStyle(workbook);
        cellSubValue.setCellStyle(subValueStyle);
        sheet.addMergedRegion(new CellRangeAddress(nextRowSub, nextRowSub, 8, 9));
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowSub, nextRowSub, 8, 9), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);
    }

    public static void fillDetalhamentoExecucaoPlano(HSSFWorkbook workbook, HSSFSheet sheet, DetalhamentosComponenteCurricular detalhamentos) {
        // Estilo para o preenchimento do detalhamento de execução de plano
        CellStyle detalhamentoStyle = Style.getDefaultSubCellStyle(workbook);

        // Percorre cada detalhamento na lista de detalhamentos de execução de plano
        int nextRow = getNextRowToFill(sheet, 0) - 1;
        // Preencher detalhamentos apenas se existirem:
        if (detalhamentos != null) {
            for (DetalhamentoAula detalhamento : detalhamentos.getDetalhamentoAulas()) {
                // Preencher o curso na coluna A
                Row rowCurso = sheet.createRow(nextRow);
                Cell cellCurso = rowCurso.createCell(0);
                cellCurso.setCellValue(detalhamento.getCurso());
                cellCurso.setCellStyle(detalhamentoStyle);

                // Preencher o componente curricular na coluna D
                Cell cellComponenteCurricular = rowCurso.createCell(3);
                cellComponenteCurricular.setCellValue(detalhamento.getComponenteCurricular());
                cellComponenteCurricular.setCellStyle(detalhamentoStyle);

                // Preencher a chSemanal na coluna I
                Cell cellChSemanal = rowCurso.createCell(8);
                cellChSemanal.setCellValue(detalhamento.getChSemanal());
                cellChSemanal.setCellStyle(detalhamentoStyle);

                // Mesclar as células das colunas A até C para o curso
                sheet.addMergedRegion(new CellRangeAddress(nextRow, nextRow, 0, 2));

                // Mesclar as células das colunas D até H para o componente curricular
                sheet.addMergedRegion(new CellRangeAddress(nextRow, nextRow, 3, 7));

                // Mesclar as células das colunas I até J para a chSemanal
                sheet.addMergedRegion(new CellRangeAddress(nextRow, nextRow, 8, 9));

                // Aplicar as bordas linha a linha para cada célula na linha do detalhamento
                Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRow, nextRow, 0, 2), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);
                Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRow, nextRow, 3, 7), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);
                Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRow, nextRow, 8, 9), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

                // Incrementar a próxima linha para o próximo detalhamento
                nextRow++;
            }
        } else {
            detalhamentos = new DetalhamentosComponenteCurricular();
        }

        // Preencher o subtotal
        int nextRowSubtotal = getNextRowToFill(sheet, 0) - 1;
        Row rowSubtotal = sheet.createRow(nextRowSubtotal);

        // Preencher a coluna com a palavra "Subtotal" no range de A até H
        Cell cellSubtotalLabel = rowSubtotal.createCell(0);
        cellSubtotalLabel.setCellValue("Subtotal");
        CellStyle subtotalLabelStyle = Style.getCellStyleBoldAlighRight(workbook);
        cellSubtotalLabel.setCellStyle(subtotalLabelStyle);
        sheet.addMergedRegion(new CellRangeAddress(nextRowSubtotal, nextRowSubtotal, 0, 7));
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowSubtotal, nextRowSubtotal, 0, 7), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

        // Preencher a coluna do valor do subtotal que está em detalhamentos no range de I até J
        Cell cellSubtotalValue = rowSubtotal.createCell(8);
        cellSubtotalValue.setCellValue(detalhamentos.getSubtotal());
        CellStyle subtotalValueStyle = Style.getBoldCellStyle(workbook);
        cellSubtotalValue.setCellStyle(subtotalValueStyle);
        sheet.addMergedRegion(new CellRangeAddress(nextRowSubtotal, nextRowSubtotal, 8, 9));
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowSubtotal, nextRowSubtotal, 8, 9), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);
    }

    public static void fillDetalhamentosProjetos(HSSFWorkbook workbook, HSSFSheet sheet, DetalhamentosProjetos detalhamentos) {
        // Estilo para o preenchimento dos detalhamentos de projetos
        CellStyle detalhamentoStyle = Style.getDefaultSubCellStyle(workbook);

        // Percorre cada detalhamento na lista de detalhamentos de projetos
        int nextRow = getNextRowToFill(sheet, 0) - 1;
        if (detalhamentos != null) {
            for (DetalhamentoProjeto detalhamento : detalhamentos.getDetalhamentosProjetos()) {
                // Preencher o tituloAcao na coluna A até B
                Row rowTituloAcao = sheet.createRow(nextRow);
                Cell cellTituloAcao = rowTituloAcao.createCell(0);
                cellTituloAcao.setCellValue(detalhamento.getTituloAcao());
                cellTituloAcao.setCellStyle(detalhamentoStyle);
                sheet.addMergedRegion(new CellRangeAddress(nextRow, nextRow, 0, 1));

                // Preencher o tipoAcao na coluna C
                Cell cellTipoAcao = rowTituloAcao.createCell(2);
                cellTipoAcao.setCellValue(detalhamento.getTipoAcao());
                cellTipoAcao.setCellStyle(detalhamentoStyle);

                // Preencher o numeroCadastro na coluna D até E
                Cell cellNumeroCadastro = rowTituloAcao.createCell(3);
                cellNumeroCadastro.setCellValue(detalhamento.getNumeroCadastro());
                cellNumeroCadastro.setCellStyle(detalhamentoStyle);
                sheet.addMergedRegion(new CellRangeAddress(nextRow, nextRow, 3, 4));

                // Preencher o tipoAtuacao na coluna F até H
                Cell cellTipoAtuacao = rowTituloAcao.createCell(5);
                cellTipoAtuacao.setCellValue(detalhamento.getTipoAtuacao());
                cellTipoAtuacao.setCellStyle(detalhamentoStyle);
                sheet.addMergedRegion(new CellRangeAddress(nextRow, nextRow, 5, 7));

                // Preencher o chSemanal na coluna I até J
                Cell cellCHSemanal = rowTituloAcao.createCell(8);
                cellCHSemanal.setCellValue(detalhamento.getChSemanal());
                cellCHSemanal.setCellStyle(detalhamentoStyle);
                sheet.addMergedRegion(new CellRangeAddress(nextRow, nextRow, 8, 9));

                // Aplicar as bordas linha a linha para cada célula na linha do detalhamento de projeto
                Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRow, nextRow, 0, 1), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);
                Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRow, nextRow, 2, 2), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);
                Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRow, nextRow, 3, 4), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);
                Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRow, nextRow, 5, 7), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);
                Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRow, nextRow, 8, 9), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

                // Incrementar a próxima linha para o próximo detalhamento de projeto
                nextRow++;
            }
        }
    }

    public static void fillMediacaoPedagogica(HSSFWorkbook workbook, HSSFSheet sheet, MediacoesPedagogicas mediacoes) {
        // Estilo para o preenchimento da mediação pedagógica
        CellStyle detalhamentoStyle = Style.getDefaultSubCellStyle(workbook);

        // Percorre cada mediação na lista de mediações pedagógicas
        int nextRow = getNextRowToFill(sheet, 0) - 1;
        // Preencher mediações apenas se existirem:
        if (mediacoes != null) {
            for (MediacaoPedagogica mediacao : mediacoes.getMediacoesPedagogicas()) {
                // Preencher o curso na coluna A
                Row rowCurso = sheet.createRow(nextRow);
                Cell cellCurso = rowCurso.createCell(0);
                cellCurso.setCellValue(mediacao.getCurso());
                cellCurso.setCellStyle(detalhamentoStyle);

                // Preencher o componente curricular na coluna D
                Cell cellComponenteCurricular = rowCurso.createCell(3);
                cellComponenteCurricular.setCellValue(mediacao.getComponenteCurricular());
                cellComponenteCurricular.setCellStyle(detalhamentoStyle);

                // Preencher a chSemanal na coluna I
                Cell cellChSemanal = rowCurso.createCell(8);
                cellChSemanal.setCellValue(mediacao.getChSemanal());
                cellChSemanal.setCellStyle(detalhamentoStyle);

                // Mesclar as células das colunas A até C para o curso
                sheet.addMergedRegion(new CellRangeAddress(nextRow, nextRow, 0, 2));

                // Mesclar as células das colunas D até H para o componente curricular
                sheet.addMergedRegion(new CellRangeAddress(nextRow, nextRow, 3, 7));

                // Mesclar as células das colunas I até J para a chSemanal
                sheet.addMergedRegion(new CellRangeAddress(nextRow, nextRow, 8, 9));

                // Aplicar as bordas linha a linha para cada célula na linha da mediação
                Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRow, nextRow, 0, 2), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);
                Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRow, nextRow, 3, 7), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);
                Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRow, nextRow, 8, 9), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

                // Incrementar a próxima linha para a próxima mediação
                nextRow++;
            }
        } else {
            mediacoes = new MediacoesPedagogicas();
        }

        // Preencher o subtotal
        int nextRowSubtotal = getNextRowToFill(sheet, 0) - 1;
        Row rowSubtotal = sheet.createRow(nextRowSubtotal);

        // Preencher a coluna com a palavra "Subtotal" no range de A até H
        Cell cellSubtotalLabel = rowSubtotal.createCell(0);
        cellSubtotalLabel.setCellValue("Subtotal");
        CellStyle subtotalLabelStyle = Style.getCellStyleBoldAlighRight(workbook);
        cellSubtotalLabel.setCellStyle(subtotalLabelStyle);
        sheet.addMergedRegion(new CellRangeAddress(nextRowSubtotal, nextRowSubtotal, 0, 7));
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowSubtotal, nextRowSubtotal, 0, 7), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

        // Preencher a coluna do valor do subtotal que está em mediações no range de I até J
        Cell cellSubtotalValue = rowSubtotal.createCell(8);
        cellSubtotalValue.setCellValue(mediacoes.getSubtotal());
        CellStyle subtotalValueStyle = Style.getBoldCellStyle(workbook);
        cellSubtotalValue.setCellStyle(subtotalValueStyle);
        sheet.addMergedRegion(new CellRangeAddress(nextRowSubtotal, nextRowSubtotal, 8, 9));
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowSubtotal, nextRowSubtotal, 8, 9), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);
    }

    public static void fillAtividadesPesquisa(HSSFWorkbook workbook, HSSFSheet sheet, AtividadesPesquisa atividadesPesquisa) {
        // Estilo para o preenchimento das atividades de pesquisa
        CellStyle atividadesStyle = Style.getDefaultLeftAlignSubCellStyle(workbook);

        // Estilo para CH semanal
        CellStyle chSemanalStyle = Style.getDefaultSubCellStyle(workbook);

        // Percorre cada atividade de pesquisa na lista de atividades de pesquisa
        int nextRow = getNextRowToFill(sheet, 0) - 1;
        // Preencher atividades de pesquisa apenas se existirem:
        if (atividadesPesquisa != null) {
            for (Pesquisa pesquisa : atividadesPesquisa.getAtividadesPesquisas()) {
                // Preencher a atividade na coluna A
                Row rowAtividade = sheet.createRow(nextRow);
                Cell cellAtividade = rowAtividade.createCell(0);
                cellAtividade.setCellValue(pesquisa.getAtividade());
                cellAtividade.setCellStyle(atividadesStyle);

                // Preencher a chSemanal na coluna I
                Cell cellChSemanal = rowAtividade.createCell(8);
                cellChSemanal.setCellValue(pesquisa.getChSemanal());
                cellChSemanal.setCellStyle(chSemanalStyle);

                // Mesclar as células das colunas A até H para a atividade
                sheet.addMergedRegion(new CellRangeAddress(nextRow, nextRow, 0, 7));

                // Mesclar as células das colunas I até J para a chSemanal
                sheet.addMergedRegion(new CellRangeAddress(nextRow, nextRow, 8, 9));

                // Aplicar as bordas linha a linha para cada célula na linha da atividade
                Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRow, nextRow, 0, 7), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);
                Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRow, nextRow, 8, 9), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

                // Incrementar a próxima linha para a próxima atividade de pesquisa
                nextRow++;
            }
        } else {
            atividadesPesquisa = new AtividadesPesquisa();
        }

        // Preencher o subtotal
        int nextRowSubtotal = getNextRowToFill(sheet, 0) - 1;
        Row rowSubtotal = sheet.createRow(nextRowSubtotal);

        // Preencher a coluna com a palavra "Subtotal" no range de A até H
        Cell cellSubtotalLabel = rowSubtotal.createCell(0);
        cellSubtotalLabel.setCellValue("Subtotal");
        CellStyle subtotalLabelStyle = Style.getCellStyleBoldAlighRight(workbook);
        cellSubtotalLabel.setCellStyle(subtotalLabelStyle);
        sheet.addMergedRegion(new CellRangeAddress(nextRowSubtotal, nextRowSubtotal, 0, 7));
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowSubtotal, nextRowSubtotal, 0, 7), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

        // Preencher a coluna do valor do subtotal que está em atividadesPesquisa no range de I até J
        Cell cellSubtotalValue = rowSubtotal.createCell(8);
        cellSubtotalValue.setCellValue(atividadesPesquisa.getSubtotal());
        CellStyle subtotalValueStyle = Style.getBoldCellStyle(workbook);
        cellSubtotalValue.setCellStyle(subtotalValueStyle);
        sheet.addMergedRegion(new CellRangeAddress(nextRowSubtotal, nextRowSubtotal, 8, 9));
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowSubtotal, nextRowSubtotal, 8, 9), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);
    }

    public static void fillDetalhamentosPesquisas(HSSFWorkbook workbook, HSSFSheet sheet, DetalhamentosPesquisas detalhamentosPesquisas) {
        // Estilo para o preenchimento dos detalhamentos de pesquisas
        CellStyle detalhamentoStyle = Style.getDefaultSubCellStyle(workbook);

        // Percorre cada detalhamento de pesquisa na lista de detalhamentos de pesquisas
        int nextRow = getNextRowToFill(sheet, 0) - 1;
        if (detalhamentosPesquisas != null) {
            for (DetalhamentoPesquisa detalhamento : detalhamentosPesquisas.getDetalhamentosPesquisas()) {
                // Preencher o tituloAcao na coluna A até B
                Row rowTituloAcao = sheet.createRow(nextRow);
                Cell cellTituloAcao = rowTituloAcao.createCell(0);
                cellTituloAcao.setCellValue(detalhamento.getTituloAcao());
                cellTituloAcao.setCellStyle(detalhamentoStyle);
                sheet.addMergedRegion(new CellRangeAddress(nextRow, nextRow, 0, 1));

                // Preencher o tipoAcao na coluna C
                Cell cellTipoAcao = rowTituloAcao.createCell(2);
                cellTipoAcao.setCellValue(detalhamento.getTipoAcao());
                cellTipoAcao.setCellStyle(detalhamentoStyle);

                // Preencher o numeroCadastro na coluna D até E
                Cell cellNumeroCadastro = rowTituloAcao.createCell(3);
                cellNumeroCadastro.setCellValue(detalhamento.getNumeroCadastro());
                cellNumeroCadastro.setCellStyle(detalhamentoStyle);
                sheet.addMergedRegion(new CellRangeAddress(nextRow, nextRow, 3, 4));

                // Preencher o tipoAtuacao na coluna F até H
                Cell cellTipoAtuacao = rowTituloAcao.createCell(5);
                cellTipoAtuacao.setCellValue(detalhamento.getTipoAtuacao());
                cellTipoAtuacao.setCellStyle(detalhamentoStyle);
                sheet.addMergedRegion(new CellRangeAddress(nextRow, nextRow, 5, 7));

                // Preencher o chSemanal na coluna I até J
                Cell cellCHSemanal = rowTituloAcao.createCell(8);
                cellCHSemanal.setCellValue(detalhamento.getChSemanal());
                cellCHSemanal.setCellStyle(detalhamentoStyle);
                sheet.addMergedRegion(new CellRangeAddress(nextRow, nextRow, 8, 9));

                // Aplicar as bordas linha a linha para cada célula na linha do detalhamento de pesquisa
                Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRow, nextRow, 0, 1), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);
                Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRow, nextRow, 2, 2), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);
                Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRow, nextRow, 3, 4), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);
                Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRow, nextRow, 5, 7), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);
                Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRow, nextRow, 8, 9), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

                // Incrementar a próxima linha para o próximo detalhamento de pesquisa
                nextRow++;
            }
        }
    }

    public static void fillAtividadesExtensao(HSSFWorkbook workbook, HSSFSheet sheet, AtividadesExtensao atividadesExtensao) {
        // Estilo para o preenchimento das atividades de extensão
        CellStyle atividadeStyle = Style.getDefaultLeftAlignSubCellStyle(workbook);

        // Estilo para CH semanal
        CellStyle chSemanalStyle = Style.getDefaultSubCellStyle(workbook);

        // Percorre cada atividade de extensão na lista de atividades de extensão
        int nextRow = getNextRowToFill(sheet, 0) - 1;
        if (atividadesExtensao != null) {
            for (AtividadeExtensao atividade : atividadesExtensao.getAtividadesExtensao()) {
                // Preencher a atividade na coluna A até H
                Row rowAtividade = sheet.createRow(nextRow);
                Cell cellAtividade = rowAtividade.createCell(0);
                cellAtividade.setCellValue(atividade.getAtividade());
                cellAtividade.setCellStyle(atividadeStyle);
                sheet.addMergedRegion(new CellRangeAddress(nextRow, nextRow, 0, 7));

                // Preencher o chSemanal na coluna I até J
                Cell cellCHSemanal = rowAtividade.createCell(8);
                cellCHSemanal.setCellValue(atividade.getChSemanal());
                cellCHSemanal.setCellStyle(chSemanalStyle);
                sheet.addMergedRegion(new CellRangeAddress(nextRow, nextRow, 8, 9));

                // Aplicar as bordas linha a linha para cada célula na linha da atividade de extensão
                Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRow, nextRow, 0, 7), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);
                Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRow, nextRow, 8, 9), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

                // Incrementar a próxima linha para a próxima atividade de extensão
                nextRow++;
            }
        } else {
            atividadesExtensao = new AtividadesExtensao();
        }

        // Preencher o subtotal
        int nextRowSubtotal = getNextRowToFill(sheet, 0) - 1;
        Row rowSubtotal = sheet.createRow(nextRowSubtotal);

        // Preencher a coluna com a palavra "Subtotal" no range de A até H
        Cell cellSubtotalLabel = rowSubtotal.createCell(0);
        cellSubtotalLabel.setCellValue("Subtotal");
        CellStyle subtotalLabelStyle = Style.getCellStyleBoldAlighRight(workbook);
        cellSubtotalLabel.setCellStyle(subtotalLabelStyle);
        sheet.addMergedRegion(new CellRangeAddress(nextRowSubtotal, nextRowSubtotal, 0, 7));
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowSubtotal, nextRowSubtotal, 0, 7), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

        // Preencher a coluna do valor do subtotal que está em atividadesPesquisa no range de I até J
        Cell cellSubtotalValue = rowSubtotal.createCell(8);
        cellSubtotalValue.setCellValue(atividadesExtensao.getSubtotal());
        CellStyle subtotalValueStyle = Style.getBoldCellStyle(workbook);
        cellSubtotalValue.setCellStyle(subtotalValueStyle);
        sheet.addMergedRegion(new CellRangeAddress(nextRowSubtotal, nextRowSubtotal, 8, 9));
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowSubtotal, nextRowSubtotal, 8, 9), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);
    }

    public static void fillDetalhamentosExtensao(HSSFWorkbook workbook, HSSFSheet sheet, DetalhamentosExtensao detalhamentosExtensao) {
        // Estilo para o preenchimento dos detalhamentos de extensão
        CellStyle detalhamentoStyle = Style.getDefaultSubCellStyle(workbook);

        // Percorre cada detalhamento na lista de detalhamentos de extensão
        int nextRow = getNextRowToFill(sheet, 0) - 1;
        if (detalhamentosExtensao != null) {
            for (DetalhamentoExtensao detalhamento : detalhamentosExtensao.getDetalhamentosExtensao()) {
                // Preencher o tituloAcao na coluna A até B
                Row rowTituloAcao = sheet.createRow(nextRow);
                Cell cellTituloAcao = rowTituloAcao.createCell(0);
                cellTituloAcao.setCellValue(detalhamento.getTituloAcao());
                cellTituloAcao.setCellStyle(detalhamentoStyle);
                sheet.addMergedRegion(new CellRangeAddress(nextRow, nextRow, 0, 1));

                // Preencher o tipoAcao na coluna C
                Cell cellTipoAcao = rowTituloAcao.createCell(2);
                cellTipoAcao.setCellValue(detalhamento.getTipoAcao());
                cellTipoAcao.setCellStyle(detalhamentoStyle);

                // Preencher o numeroCadastro na coluna D até E
                Cell cellNumeroCadastro = rowTituloAcao.createCell(3);
                cellNumeroCadastro.setCellValue(detalhamento.getNumeroCadastro());
                cellNumeroCadastro.setCellStyle(detalhamentoStyle);
                sheet.addMergedRegion(new CellRangeAddress(nextRow, nextRow, 3, 4));

                // Preencher o tipoAtuacao na coluna F até H
                Cell cellTipoAtuacao = rowTituloAcao.createCell(5);
                cellTipoAtuacao.setCellValue(detalhamento.getTipoAtuacao());
                cellTipoAtuacao.setCellStyle(detalhamentoStyle);
                sheet.addMergedRegion(new CellRangeAddress(nextRow, nextRow, 5, 7));

                // Preencher o chSemanal na coluna I até J
                Cell cellCHSemanal = rowTituloAcao.createCell(8);
                cellCHSemanal.setCellValue(detalhamento.getChSemanal());
                cellCHSemanal.setCellStyle(detalhamentoStyle);
                sheet.addMergedRegion(new CellRangeAddress(nextRow, nextRow, 8, 9));

                // Aplicar as bordas linha a linha para cada célula na linha do detalhamento de extensão
                Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRow, nextRow, 0, 1), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);
                Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRow, nextRow, 2, 2), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);
                Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRow, nextRow, 3, 4), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);
                Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRow, nextRow, 5, 7), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);
                Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRow, nextRow, 8, 9), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

                // Incrementar a próxima linha para o próximo detalhamento de extensão
                nextRow++;
            }
        }
    }

    public static void fillAtividadesGestao(HSSFWorkbook workbook, HSSFSheet sheet, AtividadesGestao atividadesGestao) {
        // Estilo para o preenchimento das atividades de gestão
        CellStyle gestaoStyle = Style.getDefaultLeftAlignSubCellStyle(workbook);

        // Estilo para demais itens
        CellStyle styleHeaders = Style.getDefaultSubCellStyle(workbook);

        // Percorre cada gestão na lista de gestões de atividades de gestão
        int nextRow = getNextRowToFill(sheet, 0) - 1;
        if (atividadesGestao != null) {
            for (Gestao gestao : atividadesGestao.getGestoes()) {
                // Preencher a descrição da atribuição na coluna A até B
                Row rowGestao = sheet.createRow(nextRow);
                Cell cellDescricaoAtribuicao = rowGestao.createCell(0);
                cellDescricaoAtribuicao.setCellValue(gestao.getDescricaoAtribuicao());
                cellDescricaoAtribuicao.setCellStyle(gestaoStyle);
                sheet.addMergedRegion(new CellRangeAddress(nextRow, nextRow, 0, 1));

                // Preencher o número da portaria na coluna C
                Cell cellNumeroPortaria = rowGestao.createCell(2);
                cellNumeroPortaria.setCellValue(gestao.getNumeroPortaria());
                cellNumeroPortaria.setCellStyle(styleHeaders);

                // Preencher a data de início na coluna D até E
                Cell cellDataInicio = rowGestao.createCell(3);
                cellDataInicio.setCellValue(gestao.getDataInicio());
                cellDataInicio.setCellStyle(styleHeaders);
                sheet.addMergedRegion(new CellRangeAddress(nextRow, nextRow, 3, 4));

                // Preencher o período de vigência na coluna F até H
                Cell cellPeriodoVigencia = rowGestao.createCell(5);
                cellPeriodoVigencia.setCellValue(gestao.getPeriodoVigencia());
                cellPeriodoVigencia.setCellStyle(styleHeaders);
                sheet.addMergedRegion(new CellRangeAddress(nextRow, nextRow, 5, 7));

                // Preencher a CH semanal na coluna I até J
                Cell cellCHSemanal = rowGestao.createCell(8);
                cellCHSemanal.setCellValue(gestao.getChSemanal());
                cellCHSemanal.setCellStyle(styleHeaders);
                sheet.addMergedRegion(new CellRangeAddress(nextRow, nextRow, 8, 9));

                // Aplicar as bordas linha a linha para cada célula na linha da gestão
                Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRow, nextRow, 0, 1), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);
                Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRow, nextRow, 2, 2), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);
                Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRow, nextRow, 3, 4), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);
                Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRow, nextRow, 5, 7), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);
                Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRow, nextRow, 8, 9), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

                // Incrementar a próxima linha para a próxima gestão
                nextRow++;
            }
        } else {
            atividadesGestao = new AtividadesGestao();
        }

        // Preencher o subtotal
        int nextRowSubtotal = getNextRowToFill(sheet, 0) - 1;
        Row rowSubtotal = sheet.createRow(nextRowSubtotal);

        // Preencher a coluna com a palavra "Subtotal" no range de A até H
        Cell cellSubtotalLabel = rowSubtotal.createCell(0);
        cellSubtotalLabel.setCellValue("Subtotal");
        CellStyle subtotalLabelStyle = Style.getCellStyleBoldAlighRight(workbook);
        cellSubtotalLabel.setCellStyle(subtotalLabelStyle);
        sheet.addMergedRegion(new CellRangeAddress(nextRowSubtotal, nextRowSubtotal, 0, 7));
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowSubtotal, nextRowSubtotal, 0, 7), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

        // Preencher a coluna do valor do subtotal que está em atividadesPesquisa no range de I até J
        Cell cellSubtotalValue = rowSubtotal.createCell(8);
        cellSubtotalValue.setCellValue(atividadesGestao.getSubtotal());
        CellStyle subtotalValueStyle = Style.getBoldCellStyle(workbook);
        cellSubtotalValue.setCellStyle(subtotalValueStyle);
        sheet.addMergedRegion(new CellRangeAddress(nextRowSubtotal, nextRowSubtotal, 8, 9));
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowSubtotal, nextRowSubtotal, 8, 9), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

    }

    public static void fillAtividadesRepresentacao(HSSFWorkbook workbook, HSSFSheet sheet, AtividadesRepresentacao atividadesRepresentacao) {
        // Estilo para o preenchimento das atividades de representação
        CellStyle representacaoStyle = Style.getDefaultLeftAlignSubCellStyle(workbook);

        // Estilo para demais itens
        CellStyle styleHeaders = Style.getDefaultSubCellStyle(workbook);

        // Percorre cada representação na lista de representações de atividades de representação
        int nextRow = getNextRowToFill(sheet, 0) - 1;
        if (atividadesRepresentacao != null) {
            for (Representacao representacao : atividadesRepresentacao.getRepresentacoes()) {
                // Preencher a descrição da atribuição na coluna A até B
                Row rowRepresentacao = sheet.createRow(nextRow);
                Cell cellDescricaoAtribuicao = rowRepresentacao.createCell(0);
                cellDescricaoAtribuicao.setCellValue(representacao.getDescricaoAtribuicao());
                cellDescricaoAtribuicao.setCellStyle(representacaoStyle);
                sheet.addMergedRegion(new CellRangeAddress(nextRow, nextRow, 0, 1));

                // Preencher o número da portaria na coluna C
                Cell cellNumeroPortaria = rowRepresentacao.createCell(2);
                cellNumeroPortaria.setCellValue(representacao.getNumeroPortaria());
                cellNumeroPortaria.setCellStyle(styleHeaders);

                // Preencher a data de início na coluna D até E
                Cell cellDataInicio = rowRepresentacao.createCell(3);
                cellDataInicio.setCellValue(representacao.getDataInicio());
                cellDataInicio.setCellStyle(styleHeaders);
                sheet.addMergedRegion(new CellRangeAddress(nextRow, nextRow, 3, 4));

                // Preencher o período de vigência na coluna F até H
                Cell cellPeriodoVigencia = rowRepresentacao.createCell(5);
                cellPeriodoVigencia.setCellValue(representacao.getPeriodoVigencia());
                cellPeriodoVigencia.setCellStyle(styleHeaders);
                sheet.addMergedRegion(new CellRangeAddress(nextRow, nextRow, 5, 7));

                // Preencher a CH semanal na coluna I até J
                Cell cellCHSemanal = rowRepresentacao.createCell(8);
                cellCHSemanal.setCellValue(representacao.getChSemanal());
                cellCHSemanal.setCellStyle(styleHeaders);
                sheet.addMergedRegion(new CellRangeAddress(nextRow, nextRow, 8, 9));

                // Aplicar as bordas linha a linha para cada célula na linha da representação
                Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRow, nextRow, 0, 1), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);
                Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRow, nextRow, 2, 2), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);
                Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRow, nextRow, 3, 4), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);
                Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRow, nextRow, 5, 7), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);
                Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRow, nextRow, 8, 9), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

                // Incrementar a próxima linha para a próxima representação
                nextRow++;
            }
        } else {
            atividadesRepresentacao = new AtividadesRepresentacao();
        }

        // Preencher o subtotal
        int nextRowSubtotal = getNextRowToFill(sheet, 0) - 1;
        Row rowSubtotal = sheet.createRow(nextRowSubtotal);

        // Preencher a coluna com a palavra "Subtotal" no range de A até H
        Cell cellSubtotalLabel = rowSubtotal.createCell(0);
        cellSubtotalLabel.setCellValue("Subtotal");
        CellStyle subtotalLabelStyle = Style.getCellStyleBoldAlighRight(workbook);
        cellSubtotalLabel.setCellStyle(subtotalLabelStyle);
        sheet.addMergedRegion(new CellRangeAddress(nextRowSubtotal, nextRowSubtotal, 0, 7));
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowSubtotal, nextRowSubtotal, 0, 7), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

        // Preencher a coluna do valor do subtotal que está em atividadesRepresentacao no range de I até J
        Cell cellSubtotalValue = rowSubtotal.createCell(8);
        cellSubtotalValue.setCellValue(atividadesRepresentacao.getSubtotal());
        CellStyle subtotalValueStyle = Style.getBoldCellStyle(workbook);
        cellSubtotalValue.setCellStyle(subtotalValueStyle);
        sheet.addMergedRegion(new CellRangeAddress(nextRowSubtotal, nextRowSubtotal, 8, 9));
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowSubtotal, nextRowSubtotal, 8, 9), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);
    }

    public static void fillOutrasAtividades(HSSFWorkbook workbook, HSSFSheet sheet, OutrasAtividades outrasAtividades) {
        // Estilo para o preenchimento das outras atividades
        CellStyle outraStyle = Style.getDefaultLeftAlignSubCellStyle(workbook);

        // Estilo para demais itens
        CellStyle styleHeaders = Style.getDefaultSubCellStyle(workbook);

        // Percorre cada outra atividade na lista de outras atividades
        int nextRow = getNextRowToFill(sheet, 0) - 1;
        if (outrasAtividades != null) {
            for (Outra outra : outrasAtividades.getOutrasAtividades()) {
                // Preencher o tipo na coluna A até E
                Row rowOutra = sheet.createRow(nextRow);
                Cell cellTipo = rowOutra.createCell(0);
                cellTipo.setCellValue(outra.getTipo());
                cellTipo.setCellStyle(outraStyle);
                sheet.addMergedRegion(new CellRangeAddress(nextRow, nextRow, 0, 4));

                // Preencher o número da portaria na coluna F até H
                Cell cellNumeroPortaria = rowOutra.createCell(5);
                cellNumeroPortaria.setCellValue(outra.getNumeroPortaria());
                cellNumeroPortaria.setCellStyle(styleHeaders);
                sheet.addMergedRegion(new CellRangeAddress(nextRow, nextRow, 5, 7));

                // Preencher a CH semanal na coluna I até J
                Cell cellCHSemanal = rowOutra.createCell(8);
                cellCHSemanal.setCellValue(outra.getChSemanal());
                cellCHSemanal.setCellStyle(styleHeaders);
                sheet.addMergedRegion(new CellRangeAddress(nextRow, nextRow, 8, 9));

                // Aplicar as bordas linha a linha para cada célula na linha da outra atividade
                Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRow, nextRow, 0, 4), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);
                Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRow, nextRow, 5, 7), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);
                Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRow, nextRow, 8, 9), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

                // Incrementar a próxima linha para a próxima outra atividade
                nextRow++;
            }
        } else {
            outrasAtividades = new OutrasAtividades();
        }

        // Preencher o subtotal parciak
        int nextRowSubtotal = getNextRowToFill(sheet, 0) - 1;
        Row rowSubtotal = sheet.createRow(nextRowSubtotal);

        // Preencher a coluna com a palavra "Subtotal" no range de A até H
        Cell cellSubtotalLabel = rowSubtotal.createCell(0);
        cellSubtotalLabel.setCellValue("Subtotal");
        CellStyle subtotalLabelStyle = Style.getCellStyleBoldAlighRight(workbook);
        cellSubtotalLabel.setCellStyle(subtotalLabelStyle);
        sheet.addMergedRegion(new CellRangeAddress(nextRowSubtotal, nextRowSubtotal, 0, 7));
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowSubtotal, nextRowSubtotal, 0, 7), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

        // Preencher a coluna do valor do subtotal que está em outrasAtividades no range de I até J
        Cell cellSubtotalValue = rowSubtotal.createCell(8);
        cellSubtotalValue.setCellValue(outrasAtividades.getSubtotalParcial());
        CellStyle subtotalValueStyle = Style.getBoldCellStyle(workbook);
        cellSubtotalValue.setCellStyle(subtotalValueStyle);
        sheet.addMergedRegion(new CellRangeAddress(nextRowSubtotal, nextRowSubtotal, 8, 9));
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowSubtotal, nextRowSubtotal, 8, 9), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);


        // Percorre cada outra atividade na lista outrasAtividadesBolsita
        int nextRowBolsita = getNextRowToFill(sheet, 0) - 1;

        for (Outra outra : outrasAtividades.getOutrasAtividadesBolsita()) {
            // Preencher o tipo na coluna A até E
            Row rowOutra = sheet.createRow(nextRowBolsita);
            Cell cellTipo = rowOutra.createCell(0);
            cellTipo.setCellValue(outra.getTipo());
            cellTipo.setCellStyle(outraStyle);
            sheet.addMergedRegion(new CellRangeAddress(nextRowBolsita, nextRowBolsita, 0, 4));

            // Preencher o número da portaria na coluna F até H
            Cell cellNumeroPortaria = rowOutra.createCell(5);
            cellNumeroPortaria.setCellValue(outra.getNumeroPortaria());
            cellNumeroPortaria.setCellStyle(styleHeaders);
            sheet.addMergedRegion(new CellRangeAddress(nextRowBolsita, nextRowBolsita, 5, 7));

            // Preencher a CH semanal na coluna I até J
            Cell cellCHSemanal = rowOutra.createCell(8);
            cellCHSemanal.setCellValue(outra.getChSemanal());
            cellCHSemanal.setCellStyle(styleHeaders);
            sheet.addMergedRegion(new CellRangeAddress(nextRowBolsita, nextRowBolsita, 8, 9));

            // Aplicar as bordas linha a linha para cada célula na linha da outra atividade
            Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowBolsita, nextRowBolsita, 0, 4), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);
            Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowBolsita, nextRowBolsita, 5, 7), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);
            Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowBolsita, nextRowBolsita, 8, 9), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

            // Incrementar a próxima linha para a próxima outra atividade
            nextRowBolsita++;
        }

        // Preencher o subtotal parciak
        int nextRowSubBolsita = getNextRowToFill(sheet, 0) - 1;
        Row rowSubBolsita = sheet.createRow(nextRowSubBolsita);

        // Preencher a coluna com a palavra "Subtotal" no range de A até H
        Cell celSubLabel = rowSubBolsita.createCell(0);
        celSubLabel.setCellValue("Total de atividades de plano(s) de trabalho de bolsista");
        CellStyle subStyle = Style.getCellStyleBoldAlighRight(workbook);
        celSubLabel.setCellStyle(subStyle);
        sheet.addMergedRegion(new CellRangeAddress(nextRowSubBolsita, nextRowSubBolsita, 0, 7));
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowSubBolsita, nextRowSubBolsita, 0, 7), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);

        // Preencher a coluna do valor do subtotal que está em outrasAtividades no range de I até J
        Cell subValueCell = rowSubBolsita.createCell(8);
        subValueCell.setCellValue(outrasAtividades.getSubtotalParcial());
        CellStyle subValueStyle = Style.getBoldCellStyle(workbook);
        subValueCell.setCellStyle(subValueStyle);
        sheet.addMergedRegion(new CellRangeAddress(nextRowSubBolsita, nextRowSubBolsita, 8, 9));
        Style.applyBorders(sheet, workbook, new CellRangeAddress(nextRowSubBolsita, nextRowSubBolsita, 8, 9), BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN);


    }


}