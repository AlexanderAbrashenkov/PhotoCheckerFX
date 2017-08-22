package foto_verif.model.apache_poi;

import foto_verif.model.Photo;
import foto_verif.view.dmp.DmpAddress;
import foto_verif.view.dmp.DmpDescr;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFRow;

import java.awt.Color;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by market6 on 13.01.2017.
 */
public class ApachePoiLkaDmp extends AbstractApachePoi implements ApachePoi {

    @Override
    public void createConcreteSheet(String net, List activities) {
        rowIndex = 5;
        spreadsheet = workbook.createSheet(net);
        spreadsheet.groupColumn(5, 5);
        spreadsheet.setColumnGroupCollapsed(5, true);
        XSSFRow row = spreadsheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("Отчет по проверке фото в канале " + net + " c " + dateFrom + " по " + dateTo);
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
        cell.setCellValue("Регион");
        cell.setCellStyle(headerStyle);
        cell = row.createCell(2);
        cell.setCellValue("Дистрибьютор");
        cell.setCellStyle(headerStyle);
        cell = row.createCell(3);
        cell.setCellValue("Канал");
        cell.setCellStyle(headerStyle);
        cell = row.createCell(4);
        cell.setCellValue("Наименование сети");
        cell.setCellStyle(headerStyle);
        cell = row.createCell(5);
        cell.setCellValue("Код клиента");
        cell.setCellStyle(headerStyle);
        cell = row.createCell(6);
        cell.setCellValue("Тип точки");
        cell.setCellStyle(headerStyle);
        cell = row.createCell(7);
        cell.setCellValue("Наименование клиента");
        cell.setCellStyle(headerStyle);
        cell = row.createCell(8);
        cell.setCellValue("Адрес клиента");
        cell.setCellStyle(headerStyle);
        cell = row.createCell(9);
        cell.setCellValue("Корректность фото");
        cell.setCellStyle(headerStyle1);
        cell = row.createCell(10);
        cell.setCellValue("Наличие кодового слова");
        cell.setCellStyle(headerStyle1);
        cell = row.createCell(11);
        cell.setCellValue("Товарные группы на ДМП");
        cell.setCellStyle(headerStyle2);
        cell = row.createCell(12);
        cell.setCellStyle(headerStyle2);
        cell = row.createCell(13);
        cell.setCellStyle(headerStyle2);
        cell = row.createCell(14);
        cell.setCellStyle(headerStyle2);
        cell = row.createCell(15);
        cell.setCellStyle(headerStyle2);
        cell = row.createCell(16);
        cell.setCellValue("Соответствие минимальному размеру");
        cell.setCellStyle(headerStyle3);
        cell = row.createCell(17);
        cell.setCellValue("Наличие акционного продукта на ДМП");
        cell.setCellStyle(headerStyle3);
        cell = row.createCell(18);
        cell.setCellValue("Наличие акц. ценников");
        cell.setCellStyle(headerStyle3);
        cell = row.createCell(19);
        cell.setCellValue("Заполненность ДМП");
        cell.setCellStyle(headerStyle3);
        cell = row.createCell(20);
        cell.setCellValue("Соответствие требованиям по месту");
        cell.setCellStyle(headerStyle3);
        cell = row.createCell(21);
        cell.setCellValue("Оценка паллета");
        cell.setCellStyle(headerStyle4);
        cell = row.createCell(22);
        cell.setCellValue("Комментарии");
        cell.setCellStyle(headerStyle4);
        cell = row.createCell(23);
        cell.setCellValue("Ссылки на фото");

        row = spreadsheet.createRow(4);
        row.setHeight((short) 600);
        cell = row.createCell(0);
        cell.setCellStyle(headerStyle);
        for (int i = 1; i < 9; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(headerStyle);
        }
        for (int i = 9; i < 11; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(headerStyle1);
        }
        cell = row.createCell(11);
        cell.setCellValue("Майонез");
        cell.setCellStyle(headerStyle2);
        cell = row.createCell(12);
        cell.setCellValue("Кетчуп");
        cell.setCellStyle(headerStyle2);
        cell = row.createCell(13);
        cell.setCellValue("Ласка Постный");
        cell.setCellStyle(headerStyle2);
        cell = row.createCell(14);
        cell.setCellValue("Соус");
        cell.setCellStyle(headerStyle2);
        cell = row.createCell(15);
        cell.setCellValue("ДМП Печка");
        cell.setCellStyle(headerStyle2);
        for (int i = 16; i < 21; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(headerStyle3);
        }
        for (int i = 21; i < 23; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(headerStyle4);
        }

        // Объединение ячеек
        spreadsheet.addMergedRegion(new CellRangeAddress(0, 2, 0, 21));
        spreadsheet.addMergedRegion(new CellRangeAddress(3, 4, 0, 0));
        spreadsheet.addMergedRegion(new CellRangeAddress(3, 4, 1, 1));
        spreadsheet.addMergedRegion(new CellRangeAddress(3, 4, 2, 2));
        spreadsheet.addMergedRegion(new CellRangeAddress(3, 4, 3, 3));
        spreadsheet.addMergedRegion(new CellRangeAddress(3, 4, 4, 4));
        spreadsheet.addMergedRegion(new CellRangeAddress(3, 4, 5, 5));
        spreadsheet.addMergedRegion(new CellRangeAddress(3, 4, 6, 6));
        spreadsheet.addMergedRegion(new CellRangeAddress(3, 4, 7, 7));
        spreadsheet.addMergedRegion(new CellRangeAddress(3, 4, 8, 8));
        spreadsheet.addMergedRegion(new CellRangeAddress(3, 4, 9, 9));
        spreadsheet.addMergedRegion(new CellRangeAddress(3, 4, 10, 10));
        spreadsheet.addMergedRegion(new CellRangeAddress(3, 3, 11, 15));
        spreadsheet.addMergedRegion(new CellRangeAddress(3, 4, 16, 16));
        spreadsheet.addMergedRegion(new CellRangeAddress(3, 4, 17, 17));
        spreadsheet.addMergedRegion(new CellRangeAddress(3, 4, 18, 18));
        spreadsheet.addMergedRegion(new CellRangeAddress(3, 4, 19, 19));
        spreadsheet.addMergedRegion(new CellRangeAddress(3, 4, 20, 20));
        spreadsheet.addMergedRegion(new CellRangeAddress(3, 4, 21, 21));
        spreadsheet.addMergedRegion(new CellRangeAddress(3, 4, 22, 22));
        spreadsheet.addMergedRegion(new CellRangeAddress(3, 4, 23, 23));

        //ширина столбцов
        spreadsheet.setColumnWidth(0, 1000);
        spreadsheet.setColumnWidth(1, 4000);
        spreadsheet.setColumnWidth(2, 6000);
        spreadsheet.setColumnWidth(3, 3000);
        spreadsheet.setColumnWidth(4, 4000);
        spreadsheet.setColumnWidth(5, 3000);
        spreadsheet.setColumnWidth(6, 9000);
        spreadsheet.setColumnWidth(7, 9000);
        spreadsheet.setColumnWidth(8, 9000);
        for (int i = 9; i < 16; i++) {
            spreadsheet.setColumnWidth(i, 3000);
        }
        for (int i = 16; i < 22; i++) {
            spreadsheet.setColumnWidth(i, 4000);
        }
        spreadsheet.setColumnWidth(22, 13000);
    }

    @Override
    public void writeOneTtToConcreteSheet(List parameters) {

        DmpAddress dmpAddress = (DmpAddress) parameters.get(0);
        DmpDescr dmpDescr = parameters.get(1) != null ? (DmpDescr) parameters.get(1) : null;
        String region = dmpAddress.getRegion();
        String obl = dmpAddress.getObl();
        String distr = obl.substring(obl.indexOf("(") + 1, obl.indexOf(")"));
        String channel = dmpAddress.getChannel();
        String lka = dmpAddress.getNet();
        int clientId = dmpAddress.getId();
        String clientType = dmpAddress.getTypeName();
        String clientName = dmpAddress.getName();
        String clientAddress = dmpAddress.getAddress();
        List<String> allPhotoList = dmpAddress.getPhotoList().stream()
                .map(Photo::getUrl)
                .collect(Collectors.toList());

        XSSFCellStyle leftTextStyle = createBorderedStyle();
        XSSFCellStyle centerTextStyle = createBorderedStyle();
        centerTextStyle.setAlignment(HorizontalAlignment.CENTER);

        if (dmpAddress.getChecked().equals("1")) {
            leftTextStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            centerTextStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            leftTextStyle.setFillForegroundColor(new XSSFColor(new Color(170, 250, 200)));
            centerTextStyle.setFillForegroundColor(new XSSFColor(new Color(170, 250, 200)));
        } else {
            leftTextStyle.setFillPattern(FillPatternType.NO_FILL);
            centerTextStyle.setFillPattern(FillPatternType.NO_FILL);
        }

        XSSFRow row = spreadsheet.createRow(rowIndex++);
        Cell cell = row.createCell(0);
        cell.setCellValue(rowIndex - 5);
        cell.setCellStyle(centerTextStyle);
        cell = row.createCell(1);
        cell.setCellValue(region);
        cell.setCellStyle(leftTextStyle);
        cell = row.createCell(2);
        cell.setCellValue(distr);
        cell.setCellStyle(leftTextStyle);
        cell = row.createCell(3);
        cell.setCellValue(channel);
        cell.setCellStyle(leftTextStyle);
        cell = row.createCell(4);
        if (lka != null) {
            cell.setCellValue(lka);
        }

        cell.setCellStyle(leftTextStyle);
        cell = row.createCell(5);
        cell.setCellType(CellType.NUMERIC);
        cell.setCellValue(clientId);
        cell.setCellStyle(leftTextStyle);

        cell = row.createCell(6);
        if (clientType != null) {
            cell.setCellValue(clientType.trim());
        }
        cell.setCellStyle(leftTextStyle);
        cell = row.createCell(7);
        cell.setCellValue(clientName);

        cell.setCellStyle(leftTextStyle);
        cell = row.createCell(8);
        cell.setCellValue(clientAddress);
        cell.setCellStyle(leftTextStyle);

        for (int i = 0; i < 12; i++) {
            XSSFCellStyle cellStyle = (XSSFCellStyle) centerTextStyle.clone();
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            if (i >= 0 && i < 2) {
                cellStyle.setFillForegroundColor(lightGreen);
            } else if (i >= 2 && i < 7) {
                cellStyle.setFillForegroundColor(lightBlue);
            } else {
                cellStyle.setFillForegroundColor(lightRed);
            }
            cell = row.createCell(i + 9, CellType.NUMERIC);
            if (dmpDescr != null) {
                switch (i) {
                    case 0:
                        cell.setCellValue(dmpDescr.isCorrectPhoto() ? 1 : 0);
                        break;
                    case 1:
                        cell.setCellValue(dmpDescr.isHasKeyWord() ? 1 : 0);
                        break;
                    case 2:
                        cell.setCellValue(dmpDescr.isProdMZ() ? 1 : 0);
                        break;
                    case 3:
                        cell.setCellValue(dmpDescr.isProdK() ? 1 : 0);
                        break;
                    case 4:
                        cell.setCellValue(dmpDescr.isProdLP() ? 1 : 0);
                        break;
                    case 5:
                        cell.setCellValue(dmpDescr.isProdS() ? 1 : 0);
                        break;
                    case 6:
                        cell.setCellValue(dmpDescr.isDmpPechka() ? 1 : 0);
                        break;
                    case 7:
                        cell.setCellValue(dmpDescr.isMinSize() ? 1 : 0);
                        break;
                    case 8:
                        cell.setCellValue(dmpDescr.isTmaProd() ? 1 : 0);
                        break;
                    case 9:
                        cell.setCellValue(dmpDescr.isHasPrice() ? 1 : 0);
                        break;
                    case 10:
                        cell.setCellValue(dmpDescr.isFilledOver80() ? 1 : 0);
                        break;
                    case 11:
                        cell.setCellValue(dmpDescr.isPlaceDMP() ? 1 : 0);
                        break;
                }
            }
            cell.setCellStyle(cellStyle);
        }

        cell = row.createCell(21);
        XSSFCellStyle lastColumnsStyle = (XSSFCellStyle) centerTextStyle.clone();
        lastColumnsStyle.setFillForegroundColor(lightPurple);
        lastColumnsStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        XSSFCellStyle lastColumnsPercentageStyle = (XSSFCellStyle) lastColumnsStyle.clone();
        lastColumnsPercentageStyle.setDataFormat(workbook.createDataFormat().getFormat("0%"));
        cell.setCellStyle(lastColumnsPercentageStyle);
        cell.setCellType(CellType.FORMULA);
        cell.setCellFormula("SUM(Q" + rowIndex + ":U" + rowIndex + ")/5");
        cell = row.createCell(22);

        XSSFCellStyle commentStyle = (XSSFCellStyle) lastColumnsStyle.clone();
        commentStyle.setAlignment(HorizontalAlignment.LEFT);
        if (dmpDescr != null) {
            cell.setCellValue(dmpDescr.getComment());
        }
        cell.setCellType(CellType.STRING);
        cell.setCellStyle(commentStyle);
        for (int i = 0; i < allPhotoList.size(); i++) {
            cell = row.createCell(23 + i);
            cell.setCellValue(allPhotoList.get(i));
        }
    }

    @Override
    public void calcSumRowConcreteSheet(String net) {
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
        for (int i = 1; i < 9; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
        }

        for (int i = 9; i < 21; i++) {
            cell = row.createCell(i);
            cell.setCellType(CellType.FORMULA);
            cell.setCellFormula("SUM(" + (char) ('A' + i) + 6 + ":" + (char) ('A' + i) + rowIndex + ")");
            cell.setCellStyle(cellStyle);
        }
        cell = row.createCell(21);
        cell.setCellType(CellType.FORMULA);
        cell.setCellFormula("AVERAGE(V" + 6 + ":V" + rowIndex + ")");
        XSSFCellStyle percentageStyle = (XSSFCellStyle) cellStyle.clone();
        percentageStyle.setDataFormat(workbook.createDataFormat().getFormat("0%"));
        cell.setCellStyle(percentageStyle);
        cell = row.createCell(22);
        cell.setCellStyle(cellStyle);
        spreadsheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, 7));

        // условное форматирование
        /*SheetConditionalFormatting sheetCF = spreadsheet.getSheetConditionalFormatting();
        ConditionalFormattingRule rule = sheetCF.createConditionalFormattingRule(ComparisonOperator.EQUAL, "0");
        PatternFormatting fill = rule.createPatternFormatting();
        fill.setFillBackgroundColor(new XSSFColor(new Color(255, 204, 204)));
        fill.setFillPattern(PatternFormatting.SOLID_FOREGROUND);
        FontFormatting fontF = rule.createFontFormatting();
        fontF.setFontColorIndex(HSSFColor.RED.index);
        CellRangeAddress[] regions = {
                CellRangeAddress.valueOf("J6:T" + rowIndex)
        };
        sheetCF.addConditionalFormatting(regions, rule);*/
    }

    @Override
    public void createTotalSheet(String partForHeader) {
        /*NONE*/
    }

    @Override
    public void createTotalSheetHeader(String partForHeader) {
        /*NONE*/
    }

    @Override
    public void writeOneTtToTotalSheet(List parameters) {
        /*NONE*/
    }

    @Override
    public void calcSumRowTotalSheet() {
        /*NONE*/
    }
}
