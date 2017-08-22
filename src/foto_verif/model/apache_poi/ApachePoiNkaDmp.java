package foto_verif.model.apache_poi;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFRow;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by market6 on 13.01.2017.
 */
public class ApachePoiNkaDmp extends AbstractApachePoi implements ApachePoi {

    @Override
    public void createConcreteSheet(String rjkamName, List activities) {
        rowIndex = 5;
        String[] rjkamFio = rjkamName.split(" ");
        String rjkamFioForSheet = rjkamFio.length > 2 ? rjkamFio[0] + " " + rjkamFio[1].substring(0, 1).toUpperCase() + "." +
                rjkamFio[2].substring(0, 1).toUpperCase() + "." : rjkamFio[0] + " " + rjkamFio[1].substring(0, 1).toUpperCase() + ".";
        spreadsheet = workbook.createSheet(rjkamFioForSheet);
        spreadsheet.groupColumn(2, 2);
        spreadsheet.setColumnGroupCollapsed(2, true);
        XSSFRow row = spreadsheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("Отчет по проверке фото сотрудника RJKAM " + rjkamName + " c " + dateFrom + " по " + dateTo);
        Font boldFont = workbook.createFont();
        boldFont.setBold(true);
        Font bigBoldFont = workbook.createFont();
        bigBoldFont.setBold(true);
        bigBoldFont.setFontHeightInPoints((short) 16);
        titleStyle = workbook.createCellStyle();
        titleStyle.setFont(bigBoldFont);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cell.setCellStyle(titleStyle);

        // цвета для заливки
        lightRed = new XSSFColor(new Color(252, 228, 214));
        lightGreen = new XSSFColor(new Color(226, 239, 218));
        lightBlue = new XSSFColor(new Color(221, 235, 247));
        lightPurple = new XSSFColor(new Color(228, 223, 236));

        // набор стилей для обычных заголовков
        headerStyle = createBorderedStyle();
        headerStyle.setFont(boldFont);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle.setWrapText(true);

        // набор стилей для ТГ
        headerStyle1 = createBorderedStyle();
        headerStyle1.setFont(boldFont);
        headerStyle1.setAlignment(HorizontalAlignment.CENTER);
        headerStyle1.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle1.setWrapText(true);
        headerStyle1.setFillForegroundColor(lightGreen);
        headerStyle1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle2 = (XSSFCellStyle) headerStyle1.clone();
        headerStyle2.setFillForegroundColor(lightBlue);
        headerStyle3 = (XSSFCellStyle) headerStyle1.clone();
        headerStyle3.setFillForegroundColor(lightRed);
        headerStyle4 = (XSSFCellStyle) headerStyle1.clone();
        headerStyle4.setFillForegroundColor(lightPurple);

        row = spreadsheet.createRow(3);
        row.setHeight((short) 600);
        cell = row.createCell(0);
        cell.setCellValue("№");
        cell.setCellStyle(headerStyle);
        cell = row.createCell(1);
        cell.setCellValue("Сеть");
        cell.setCellStyle(headerStyle);
        cell = row.createCell(2);
        cell.setCellValue("Код клиента");
        cell.setCellStyle(headerStyle);
        cell = row.createCell(3);
        cell.setCellValue("Тип точки");
        cell.setCellStyle(headerStyle);
        cell = row.createCell(4);
        cell.setCellValue("Наименование клиента");
        cell.setCellStyle(headerStyle);
        cell = row.createCell(5);
        cell.setCellValue("Адрес клиента");
        cell.setCellStyle(headerStyle);
        cell = row.createCell(6);
        cell.setCellValue("Корректность фото");
        cell.setCellStyle(headerStyle1);
        cell = row.createCell(7);
        cell.setCellValue("Наличие кодового слова");
        cell.setCellStyle(headerStyle1);
        cell = row.createCell(8);
        cell.setCellValue("Товарные группы на ДМП");
        cell.setCellStyle(headerStyle2);
        cell = row.createCell(9);
        cell.setCellStyle(headerStyle2);
        cell = row.createCell(10);
        cell.setCellStyle(headerStyle2);
        cell = row.createCell(11);
        cell.setCellStyle(headerStyle2);
        cell = row.createCell(12);
        cell.setCellValue("Соответствие минимальному размеру");
        cell.setCellStyle(headerStyle3);
        cell = row.createCell(13);
        cell.setCellValue("Наличие акционного продукта на ДМП");
        cell.setCellStyle(headerStyle3);
        cell = row.createCell(14);
        cell.setCellValue("Наличие акц. ценников");
        cell.setCellStyle(headerStyle3);
        cell = row.createCell(15);
        cell.setCellValue("Заполненность ДМП");
        cell.setCellStyle(headerStyle3);
        cell = row.createCell(16);
        cell.setCellValue("Соответствие требованиям по месту");
        cell.setCellStyle(headerStyle3);
        cell = row.createCell(17);
        cell.setCellValue("Оценка паллета");
        cell.setCellStyle(headerStyle4);
        cell = row.createCell(18);
        cell.setCellValue("Комментарии");
        cell.setCellStyle(headerStyle4);
        cell = row.createCell(19);
        cell.setCellValue("Ссылки на фото");

        row = spreadsheet.createRow(4);
        row.setHeight((short) 600);
        for (int i = 0; i < 6; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(headerStyle);
        }
        for (int i = 6; i < 8; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(headerStyle1);
        }
        cell = row.createCell(8);
        cell.setCellValue("Майонез");
        cell.setCellStyle(headerStyle2);
        cell = row.createCell(9);
        cell.setCellValue("Кетчуп");
        cell.setCellStyle(headerStyle2);
        cell = row.createCell(10);
        cell.setCellValue("Томатная паста");
        cell.setCellStyle(headerStyle2);
        cell = row.createCell(11);
        cell.setCellValue("Соус");
        cell.setCellStyle(headerStyle2);
        for (int i = 12; i < 17; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(headerStyle3);
        }
        for (int i = 17; i < 19; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(headerStyle4);
        }

        // Объединение ячеек
        spreadsheet.addMergedRegion(new CellRangeAddress(0, 2, 0, 18));
        spreadsheet.addMergedRegion(new CellRangeAddress(3, 4, 0, 0));
        spreadsheet.addMergedRegion(new CellRangeAddress(3, 4, 1, 1));
        spreadsheet.addMergedRegion(new CellRangeAddress(3, 4, 2, 2));
        spreadsheet.addMergedRegion(new CellRangeAddress(3, 4, 3, 3));
        spreadsheet.addMergedRegion(new CellRangeAddress(3, 4, 4, 4));
        spreadsheet.addMergedRegion(new CellRangeAddress(3, 4, 5, 5));
        spreadsheet.addMergedRegion(new CellRangeAddress(3, 4, 6, 6));
        spreadsheet.addMergedRegion(new CellRangeAddress(3, 4, 7, 7));
        spreadsheet.addMergedRegion(new CellRangeAddress(3, 3, 8, 11));
        spreadsheet.addMergedRegion(new CellRangeAddress(3, 4, 12, 12));
        spreadsheet.addMergedRegion(new CellRangeAddress(3, 4, 13, 13));
        spreadsheet.addMergedRegion(new CellRangeAddress(3, 4, 14, 14));
        spreadsheet.addMergedRegion(new CellRangeAddress(3, 4, 15, 15));
        spreadsheet.addMergedRegion(new CellRangeAddress(3, 4, 16, 16));
        spreadsheet.addMergedRegion(new CellRangeAddress(3, 4, 17, 17));
        spreadsheet.addMergedRegion(new CellRangeAddress(3, 4, 18, 18));
        spreadsheet.addMergedRegion(new CellRangeAddress(3, 4, 19, 19));

        //ширина столбцов
        spreadsheet.setColumnWidth(0, 1000);
        spreadsheet.setColumnWidth(1, 6000);
        spreadsheet.setColumnWidth(2, 3000);
        spreadsheet.setColumnWidth(3, 4000);
        spreadsheet.setColumnWidth(4, 9000);
        spreadsheet.setColumnWidth(5, 9000);
        for (int i = 6; i < 12; i++) {
            spreadsheet.setColumnWidth(i, 3000);
        }
        for (int i = 12; i < 18; i++) {
            spreadsheet.setColumnWidth(i, 4000);
        }
        spreadsheet.setColumnWidth(18, 13000);
    }

    @Override
    public void writeOneTtToConcreteSheet(List parameters) {
        String netName = (String) parameters.get(0);
        int clientId = (int) parameters.get(1);
        String clientType = (String) parameters.get(2);
        String clientName = (String) parameters.get(3);
        String clientAddress = (String) parameters.get(4);
        String comment = (String) parameters.get(5);
        ArrayList<String> allPhotoList = (ArrayList<String>) parameters.get(6);
        ArrayList<Boolean> allDmpCriteria = (ArrayList<Boolean>) parameters.get(7);

        XSSFCellStyle leftTextStyle = createBorderedStyle();
        XSSFCellStyle centerTextStyle = createBorderedStyle();
        centerTextStyle.setAlignment(HorizontalAlignment.CENTER);
        XSSFRow row = spreadsheet.createRow(rowIndex++);
        Cell cell = row.createCell(0);
        cell.setCellValue(rowIndex - 5);
        cell.setCellStyle(centerTextStyle);
        cell = row.createCell(1);
        cell.setCellValue(netName);
        cell.setCellStyle(leftTextStyle);
        cell = row.createCell(2);
        cell.setCellType(CellType.NUMERIC);
        cell.setCellValue(clientId);
        cell.setCellStyle(leftTextStyle);
        cell = row.createCell(3);
        cell.setCellValue(clientType.trim());
        cell.setCellStyle(leftTextStyle);
        cell = row.createCell(4);
        cell.setCellValue(clientName);
        cell.setCellStyle(leftTextStyle);
        cell = row.createCell(5);
        cell.setCellValue(clientAddress);
        cell.setCellStyle(leftTextStyle);

        for (int i = 0; i < 11; i++) {
            XSSFCellStyle cellStyle = (XSSFCellStyle) centerTextStyle.clone();
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            if (i >= 0 && i < 2) {
                cellStyle.setFillForegroundColor(lightGreen);
            } else if (i >= 2 && i < 6) {
                cellStyle.setFillForegroundColor(lightBlue);
            } else {
                cellStyle.setFillForegroundColor(lightRed);
            }
            cell = row.createCell(i + 6, CellType.NUMERIC);
            cell.setCellValue(allDmpCriteria.get(i) ? 1 : 0);
            cell.setCellStyle(cellStyle);
        }
        cell = row.createCell(17);
        XSSFCellStyle lastColumnsStyle = (XSSFCellStyle) centerTextStyle.clone();
        lastColumnsStyle.setFillForegroundColor(lightPurple);
        lastColumnsStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        XSSFCellStyle lastColumnsPercentageStyle = (XSSFCellStyle) lastColumnsStyle.clone();
        lastColumnsPercentageStyle.setDataFormat(workbook.createDataFormat().getFormat("0%"));
        cell.setCellStyle(lastColumnsPercentageStyle);
        cell.setCellType(CellType.FORMULA);
        cell.setCellFormula("SUM(M" + rowIndex + ":Q" + rowIndex + ")/5");
        cell = row.createCell(18);
        XSSFCellStyle commentStyle = (XSSFCellStyle) lastColumnsStyle.clone();
        commentStyle.setAlignment(HorizontalAlignment.LEFT);
        cell.setCellValue(comment);
        cell.setCellType(CellType.STRING);
        cell.setCellStyle(commentStyle);
        for (int i = 0; i < allPhotoList.size(); i++) {
            cell = row.createCell(19 + i);
            cell.setCellValue(allPhotoList.get(i));
        }
    }

    @Override
    public void calcSumRowConcreteSheet(String partForHeader) {
        XSSFCellStyle cellStyle = createBorderedStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        Font boldFont = workbook.createFont();
        boldFont.setBold(true);
        cellStyle.setFont(boldFont);
        cellStyle.setFillForegroundColor(HSSFColor.YELLOW.index);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFRow row = spreadsheet.createRow(rowIndex);
        Cell cell = row.createCell(0);
        cell.setCellValue("ИТОГО");
        cell.setCellStyle(cellStyle);
        for (int i = 1; i < 6; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
        }

        for (int i = 6; i < 17; i++) {
            cell = row.createCell(i);
            cell.setCellType(CellType.FORMULA);
            cell.setCellFormula("SUM(" + (char) ('A' + i) + 6 + ":" + (char) ('A' + i) + rowIndex + ")");
            cell.setCellStyle(cellStyle);
        }
        cell = row.createCell(17);
        cell.setCellType(CellType.FORMULA);
        cell.setCellFormula("AVERAGE(R" + 6 + ":R" + rowIndex + ")");
        XSSFCellStyle percentageStyle = (XSSFCellStyle) cellStyle.clone();
        percentageStyle.setDataFormat(workbook.createDataFormat().getFormat("0%"));
        cell.setCellStyle(percentageStyle);
        cell = row.createCell(18);
        cell.setCellStyle(cellStyle);
        spreadsheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, 5));

        // условное форматирование
        SheetConditionalFormatting sheetCF = spreadsheet.getSheetConditionalFormatting();
        ConditionalFormattingRule rule = sheetCF.createConditionalFormattingRule(ComparisonOperator.EQUAL, "0");
        PatternFormatting fill = rule.createPatternFormatting();
        fill.setFillBackgroundColor(new XSSFColor(new Color(255, 204, 204)));
        fill.setFillPattern(PatternFormatting.SOLID_FOREGROUND);
        FontFormatting fontF = rule.createFontFormatting();
        fontF.setFontColorIndex(HSSFColor.RED.index);
        CellRangeAddress[] regions = {
                CellRangeAddress.valueOf("G6:Q" + rowIndex)
        };
        sheetCF.addConditionalFormatting(regions, rule);
    }

    @Override
    public void createTotalSheet(String partForHeader) {
        spreadsheet = workbook.createSheet("СВОД");

        spreadsheet.groupColumn(3, 3);
        spreadsheet.setColumnGroupCollapsed(3, true);

        spreadsheet.addMergedRegion(new CellRangeAddress(0, 2, 0, 18));
        spreadsheet.addMergedRegion(new CellRangeAddress(3, 4, 0, 0));
        spreadsheet.addMergedRegion(new CellRangeAddress(3, 4, 1, 1));
        spreadsheet.addMergedRegion(new CellRangeAddress(3, 4, 2, 2));
        spreadsheet.addMergedRegion(new CellRangeAddress(3, 4, 3, 3));
        spreadsheet.addMergedRegion(new CellRangeAddress(3, 4, 4, 4));
        spreadsheet.addMergedRegion(new CellRangeAddress(3, 4, 5, 5));
        spreadsheet.addMergedRegion(new CellRangeAddress(3, 4, 6, 6));
        spreadsheet.addMergedRegion(new CellRangeAddress(3, 4, 7, 7));
        spreadsheet.addMergedRegion(new CellRangeAddress(3, 4, 8, 8));
        spreadsheet.addMergedRegion(new CellRangeAddress(3, 3, 9, 12));
        spreadsheet.addMergedRegion(new CellRangeAddress(3, 4, 13, 13));
        spreadsheet.addMergedRegion(new CellRangeAddress(3, 4, 14, 14));
        spreadsheet.addMergedRegion(new CellRangeAddress(3, 4, 15, 15));
        spreadsheet.addMergedRegion(new CellRangeAddress(3, 4, 16, 16));
        spreadsheet.addMergedRegion(new CellRangeAddress(3, 4, 17, 17));
        spreadsheet.addMergedRegion(new CellRangeAddress(3, 4, 18, 18));
        spreadsheet.addMergedRegion(new CellRangeAddress(3, 4, 19, 19));
        spreadsheet.addMergedRegion(new CellRangeAddress(3, 4, 20, 20));

        //ширина столбцов
        spreadsheet.setColumnWidth(0, 1000);
        spreadsheet.setColumnWidth(1, 9000);
        spreadsheet.setColumnWidth(2, 4000);
        spreadsheet.setColumnWidth(3, 3000);
        spreadsheet.setColumnWidth(4, 8000);
        spreadsheet.setColumnWidth(5, 9000);
        spreadsheet.setColumnWidth(6, 9000);
        for (int i = 7; i < 13; i++) {
            spreadsheet.setColumnWidth(i, 3000);
        }
        for (int i = 13; i < 19; i++) {
            spreadsheet.setColumnWidth(i, 4000);
        }
        spreadsheet.setColumnWidth(19, 13000);
    }

    @Override
    public void createTotalSheetHeader(String partForHeader) {
        rowIndex = 5;
        XSSFRow row = spreadsheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("Сводный отчет по проверке фото от RJKAM c " + dateFrom + " по " + dateTo);
        Font boldFont = workbook.createFont();
        boldFont.setBold(true);
        Font bigBoldFont = workbook.createFont();
        bigBoldFont.setBold(true);
        bigBoldFont.setFontHeightInPoints((short) 16);
        titleStyle = workbook.createCellStyle();
        titleStyle.setFont(bigBoldFont);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cell.setCellStyle(titleStyle);

        // цвета для заливки
        lightRed = new XSSFColor(new Color(252, 228, 214));
        lightGreen = new XSSFColor(new Color(226, 239, 218));
        lightBlue = new XSSFColor(new Color(221, 235, 247));
        lightPurple = new XSSFColor(new Color(228, 223, 236));

        // набор стилей для обычных заголовков
        headerStyle = createBorderedStyle();
        headerStyle.setFont(boldFont);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle.setWrapText(true);

        // набор стилей для ТГ
        headerStyle1 = createBorderedStyle();
        headerStyle1.setFont(boldFont);
        headerStyle1.setAlignment(HorizontalAlignment.CENTER);
        headerStyle1.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle1.setWrapText(true);
        headerStyle1.setFillForegroundColor(lightGreen);
        headerStyle1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle2 = (XSSFCellStyle) headerStyle1.clone();
        headerStyle2.setFillForegroundColor(lightBlue);
        headerStyle3 = (XSSFCellStyle) headerStyle1.clone();
        headerStyle3.setFillForegroundColor(lightRed);
        headerStyle4 = (XSSFCellStyle) headerStyle1.clone();
        headerStyle4.setFillForegroundColor(lightPurple);

        row = spreadsheet.createRow(3);
        row.setHeight((short) 600);
        cell = row.createCell(0);
        cell.setCellValue("№");
        cell.setCellStyle(headerStyle);
        cell = row.createCell(1);
        cell.setCellValue("RJKAM");
        cell.setCellStyle(headerStyle);
        cell = row.createCell(2);
        cell.setCellValue("Сеть");
        cell.setCellStyle(headerStyle);
        cell = row.createCell(3);
        cell.setCellValue("Код клиента");
        cell.setCellStyle(headerStyle);
        cell = row.createCell(4);
        cell.setCellValue("Тип точки");
        cell.setCellStyle(headerStyle);
        cell = row.createCell(5);
        cell.setCellValue("Наименование клиента");
        cell.setCellStyle(headerStyle);
        cell = row.createCell(6);
        cell.setCellValue("Адрес клиента");
        cell.setCellStyle(headerStyle);
        cell = row.createCell(7);
        cell.setCellValue("Корректность фото");
        cell.setCellStyle(headerStyle1);
        cell = row.createCell(8);
        cell.setCellValue("Наличие кодового слова");
        cell.setCellStyle(headerStyle1);
        cell = row.createCell(9);
        cell.setCellValue("Товарные группы на ДМП");
        cell.setCellStyle(headerStyle2);
        cell = row.createCell(10);
        cell.setCellStyle(headerStyle2);
        cell = row.createCell(11);
        cell.setCellStyle(headerStyle2);
        cell = row.createCell(12);
        cell.setCellStyle(headerStyle2);
        cell = row.createCell(13);
        cell.setCellValue("Соответствие минимальному размеру");
        cell.setCellStyle(headerStyle3);
        cell = row.createCell(14);
        cell.setCellValue("Наличие акционного продукта на ДМП");
        cell.setCellStyle(headerStyle3);
        cell = row.createCell(15);
        cell.setCellValue("Наличие акц. ценников");
        cell.setCellStyle(headerStyle3);
        cell = row.createCell(16);
        cell.setCellValue("Заполненность ДМП");
        cell.setCellStyle(headerStyle3);
        cell = row.createCell(17);
        cell.setCellValue("Соответствие требованиям по месту");
        cell.setCellStyle(headerStyle3);
        cell = row.createCell(18);
        cell.setCellValue("Оценка паллета");
        cell.setCellStyle(headerStyle4);
        cell = row.createCell(19);
        cell.setCellValue("Комментарии");
        cell.setCellStyle(headerStyle4);
        cell = row.createCell(20);
        cell.setCellValue("Ссылки на фото");

        row = spreadsheet.createRow(4);
        row.setHeight((short) 600);
        for (int i = 0; i < 7; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(headerStyle);
        }
        for (int i = 7; i < 9; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(headerStyle1);
        }
        cell = row.createCell(9);
        cell.setCellValue("Майонез");
        cell.setCellStyle(headerStyle2);
        cell = row.createCell(10);
        cell.setCellValue("Кетчуп");
        cell.setCellStyle(headerStyle2);
        cell = row.createCell(11);
        cell.setCellValue("Томатная паста");
        cell.setCellStyle(headerStyle2);
        cell = row.createCell(12);
        cell.setCellValue("Соус");
        cell.setCellStyle(headerStyle2);
        for (int i = 13; i < 18; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(headerStyle3);
        }
        for (int i = 18; i < 20; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(headerStyle4);
        }
    }

    @Override
    public void writeOneTtToTotalSheet(List parameters) {
        String rjkamName = (String) parameters.get(0);
        String netName = (String) parameters.get(1);
        int clientId = (int) parameters.get(2);
        String clientType = (String) parameters.get(3);
        String clientName = (String) parameters.get(4);
        String clientAddress = (String) parameters.get(5);
        String comment = (String) parameters.get(6);
        ArrayList<String> allPhotoList = (ArrayList<String>) parameters.get(7);
        ArrayList<Boolean> allDmpCriteria = (ArrayList<Boolean>) parameters.get(8);

        XSSFCellStyle leftTextStyle = createBorderedStyle();
        XSSFCellStyle centerTextStyle = createBorderedStyle();
        centerTextStyle.setAlignment(HorizontalAlignment.CENTER);
        XSSFRow row = spreadsheet.createRow(rowIndex++);
        Cell cell = row.createCell(0);
        cell.setCellValue(rowIndex - 5);
        cell.setCellStyle(centerTextStyle);
        cell = row.createCell(1);
        cell.setCellValue(rjkamName);
        cell.setCellStyle(leftTextStyle);
        cell = row.createCell(2);
        cell.setCellValue(netName);
        cell.setCellStyle(leftTextStyle);
        cell = row.createCell(3);
        cell.setCellType(CellType.NUMERIC);
        cell.setCellValue(clientId);
        cell.setCellStyle(leftTextStyle);
        cell = row.createCell(4);
        cell.setCellValue(clientType.trim());
        cell.setCellStyle(leftTextStyle);
        cell = row.createCell(5);
        cell.setCellValue(clientName);
        cell.setCellStyle(leftTextStyle);
        cell = row.createCell(6);
        cell.setCellValue(clientAddress);
        cell.setCellStyle(leftTextStyle);

        for (int i = 0; i < 11; i++) {
            XSSFCellStyle cellStyle = (XSSFCellStyle) centerTextStyle.clone();
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            if (i >= 0 && i < 2) {
                cellStyle.setFillForegroundColor(lightGreen);
            } else if (i >= 2 && i < 6) {
                cellStyle.setFillForegroundColor(lightBlue);
            } else {
                cellStyle.setFillForegroundColor(lightRed);
            }
            cell = row.createCell(i + 7, CellType.NUMERIC);
            cell.setCellValue(allDmpCriteria.get(i) ? 1 : 0);
            cell.setCellStyle(cellStyle);
        }
        cell = row.createCell(18);
        XSSFCellStyle lastColumnsStyle = (XSSFCellStyle) centerTextStyle.clone();
        lastColumnsStyle.setFillForegroundColor(lightPurple);
        lastColumnsStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        XSSFCellStyle lastColumnsPercentageStyle = (XSSFCellStyle) lastColumnsStyle.clone();
        lastColumnsPercentageStyle.setDataFormat(workbook.createDataFormat().getFormat("0%"));
        cell.setCellStyle(lastColumnsPercentageStyle);
        cell.setCellType(CellType.FORMULA);
        cell.setCellFormula("SUM(N" + rowIndex + ":R" + rowIndex + ")/5");
        cell = row.createCell(19);
        XSSFCellStyle commentStyle = (XSSFCellStyle) lastColumnsStyle.clone();
        commentStyle.setAlignment(HorizontalAlignment.LEFT);
        cell.setCellValue(comment);
        cell.setCellType(CellType.STRING);
        cell.setCellStyle(commentStyle);
        for (int i = 0; i < allPhotoList.size(); i++) {
            cell = row.createCell(20 + i);
            cell.setCellValue(allPhotoList.get(i));
        }
    }

    @Override
    public void calcSumRowTotalSheet() {
        XSSFCellStyle cellStyle = createBorderedStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        Font boldFont = workbook.createFont();
        boldFont.setBold(true);
        cellStyle.setFont(boldFont);
        cellStyle.setFillForegroundColor(HSSFColor.YELLOW.index);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFRow row = spreadsheet.createRow(rowIndex);
        Cell cell = row.createCell(0);
        cell.setCellValue("ИТОГО");
        cell.setCellStyle(cellStyle);
        for (int i = 1; i < 7; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
        }

        for (int i = 7; i < 18; i++) {
            cell = row.createCell(i);
            cell.setCellType(CellType.FORMULA);
            cell.setCellFormula("SUM(" + (char) ('A' + i) + 6 + ":" + (char) ('A' + i) + rowIndex + ")");
            cell.setCellStyle(cellStyle);
        }
        cell = row.createCell(18);
        cell.setCellType(CellType.FORMULA);
        cell.setCellFormula("AVERAGE(S" + 6 + ":S" + rowIndex + ")");
        XSSFCellStyle percentageStyle = (XSSFCellStyle) cellStyle.clone();
        percentageStyle.setDataFormat(workbook.createDataFormat().getFormat("0%"));
        cell.setCellStyle(percentageStyle);
        cell = row.createCell(19);
        cell.setCellStyle(cellStyle);
        spreadsheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, 6));

        // условное форматирование
        SheetConditionalFormatting sheetCF = spreadsheet.getSheetConditionalFormatting();
        ConditionalFormattingRule rule = sheetCF.createConditionalFormattingRule(ComparisonOperator.EQUAL, "0");
        PatternFormatting fill = rule.createPatternFormatting();
        fill.setFillBackgroundColor(new XSSFColor(new Color(255, 204, 204)));
        fill.setFillPattern(PatternFormatting.SOLID_FOREGROUND);
        FontFormatting fontF = rule.createFontFormatting();
        fontF.setFontColorIndex(HSSFColor.RED.index);
        CellRangeAddress[] regions = {
                CellRangeAddress.valueOf("H6:R" + rowIndex)
        };
        sheetCF.addConditionalFormatting(regions, rule);
    }
}
