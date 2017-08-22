package foto_verif.model.apache_poi;

import foto_verif.model.TMAActivity;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFRow;

import java.awt.Color;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by market6 on 23.01.2017.
 */
public class ApachePoiNst extends AbstractApachePoi implements ApachePoi {
    @Override
    public void createConcreteSheet(String format, List activities) {
        rowIndex = 4;
        spreadsheet = workbook.createSheet(format);
        XSSFRow row = spreadsheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("Комментарии к фотоотчету по ММ за период c " + dateFrom + " по " + dateTo);
        Font boldFont = workbook.createFont();
        boldFont.setBold(true);
        titleStyle = workbook.createCellStyle();
        titleStyle.setFont(boldFont);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
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
        headerStyle1.setFillForegroundColor(lightBlue);
        headerStyle1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle2 = (XSSFCellStyle) headerStyle1.clone();
        headerStyle2.setFillForegroundColor(lightRed);
        headerStyle3 = (XSSFCellStyle) headerStyle1.clone();
        headerStyle3.setFillForegroundColor(lightGreen);
        headerStyle4 = (XSSFCellStyle) headerStyle1.clone();
        headerStyle4.setFillForegroundColor(lightPurple);

        // заполняем список акций
        ArrayList<TMAActivity> activitiesList = (ArrayList<TMAActivity>) activities;
        if (activitiesList != null && activitiesList.size() > 0) {
            activityMap = new HashMap<>();
            for (int i = 0; i < activitiesList.size(); i++) {
                activityMap.put(activitiesList.get(i), 23 + i * 2);
            }
        }

        row = spreadsheet.createRow(2);
        cell = row.createCell(0);
        cell.setCellValue("№");
        cell.setCellStyle(headerStyle);
        cell = row.createCell(1);
        cell.setCellValue("Филиал");
        cell.setCellStyle(headerStyle);
        cell = row.createCell(2);
        cell.setCellValue("Область");
        cell.setCellStyle(headerStyle);
        cell = row.createCell(3);
        cell.setCellValue("Город");
        cell.setCellStyle(headerStyle);
        cell = row.createCell(4);
        cell.setCellValue("Название магазина");
        cell.setCellStyle(headerStyle);
        cell = row.createCell(5);
        cell.setCellValue("Адрес");
        cell.setCellStyle(headerStyle);
        cell = row.createCell(6);
        cell.setCellValue("Стоимость ММ, без учета НДС, руб.");
        cell.setCellStyle(headerStyle);
        cell = row.createCell(7);
        cell.setCellValue("Количество посещений");
        cell.setCellStyle(headerStyle);

        cell = row.createCell(8);
        cell.setCellValue("Полка МЗ");
        cell.setCellStyle(headerStyle1);
        cell = row.createCell(9);
        cell.setCellStyle(headerStyle1);
        cell = row.createCell(10);
        cell.setCellStyle(headerStyle1);
        cell = row.createCell(11);
        cell.setCellStyle(headerStyle1);
        cell = row.createCell(12);
        cell.setCellStyle(headerStyle1);

        cell = row.createCell(13);
        cell.setCellValue("Полка КП и Соус");
        cell.setCellStyle(headerStyle2);
        cell = row.createCell(14);
        cell.setCellStyle(headerStyle2);
        cell = row.createCell(15);
        cell.setCellStyle(headerStyle2);
        cell = row.createCell(16);
        cell.setCellStyle(headerStyle2);
        cell = row.createCell(17);
        cell.setCellStyle(headerStyle2);

        cell = row.createCell(18);
        cell.setCellValue("Полка Масло");
        cell.setCellStyle(headerStyle3);
        cell = row.createCell(19);
        cell.setCellStyle(headerStyle3);
        cell = row.createCell(20);
        cell.setCellStyle(headerStyle3);
        cell = row.createCell(21);
        cell.setCellStyle(headerStyle3);

        cell = row.createCell(22);
        cell.setCellValue("Количество нарушений");
        cell.setCellStyle(headerStyle4);
        cell = row.createCell(23);
        cell.setCellValue("Сумма штрафа за несоблюдение планограммы");
        cell.setCellStyle(headerStyle4);
        cell = row.createCell(24);
        cell.setCellValue("Сумма штрафа за неотработанное время");
        cell.setCellStyle(headerStyle4);
        cell = row.createCell(25);
        cell.setCellValue("Размер штрафа за каждое нарушение (2% от стоимости ММ)");
        cell.setCellStyle(headerStyle4);
        cell = row.createCell(26);
        cell.setCellValue("Комментарии");
        cell.setCellStyle(headerStyle4);
        cell = row.createCell(27);
        cell.setCellStyle(headerStyle4);
        cell = row.createCell(28);
        cell.setCellStyle(headerStyle4);
        cell = row.createCell(29);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Дата проверки");

        /*if (activityMap != null) {
            for (Map.Entry<TMAActivity, Integer> pair : activityMap.entrySet()) {
                int colNum = pair.getValue();
                cell = row.createCell(colNum);
                cell.setCellValue(pair.getKey().getActivityName());
                cell.setCellStyle(headerStyle);
                cell = row.createCell(colNum + 1);
                cell.setCellStyle(headerStyle);
                spreadsheet.addMergedRegion(new CellRangeAddress(2, 2, colNum, colNum + 1));
            }
        }*/

        row = spreadsheet.createRow(3);
        row.setHeight((short) 1200);
        cell = row.createCell(0);
        cell.setCellStyle(headerStyle);
        cell = row.createCell(1);
        cell.setCellStyle(headerStyle);
        cell = row.createCell(2);
        cell.setCellStyle(headerStyle);
        cell = row.createCell(3);
        cell.setCellStyle(headerStyle);
        cell = row.createCell(4);
        cell.setCellStyle(headerStyle);
        cell = row.createCell(5);
        cell.setCellStyle(headerStyle);
        cell = row.createCell(6);
        cell.setCellStyle(headerStyle);
        cell = row.createCell(7);
        cell.setCellStyle(headerStyle);
        int colInd = 8;
        for (int i = 0; i < 2; i++) {
            XSSFCellStyle cellStyle = null;
            switch (i) {
                case 0:
                    cellStyle = headerStyle1;
                    break;
                case 1:
                    cellStyle = headerStyle2;
                    break;
            }
            cell = row.createCell(colInd + 0);
            cell.setCellValue("Наличие фотоотчета");
            cell.setCellStyle(cellStyle);
            cell = row.createCell(colInd + 1);
            cell.setCellValue("Видны границы полки");
            cell.setCellStyle(cellStyle);
            cell = row.createCell(colInd + 2);
            cell.setCellValue("Выложена вертикальным брендблоком");
            cell.setCellStyle(cellStyle);
            cell = row.createCell(colInd + 3);
            cell.setCellValue("Занимает  30% полочного пространства");
            cell.setCellStyle(cellStyle);
            cell = row.createCell(colInd + 4);
            cell.setCellValue("Выложена по центру полки");
            cell.setCellStyle(cellStyle);
            colInd += 5;
        }

        cell = row.createCell(18);
        cell.setCellValue("Наличие фотоотчета");
        cell.setCellStyle(headerStyle3);
        cell = row.createCell(19);
        cell.setCellValue("Видны границы полки");
        cell.setCellStyle(headerStyle3);
        cell = row.createCell(20);
        cell.setCellValue("Выложена вертикальным брендблоком");
        cell.setCellStyle(headerStyle3);
        cell = row.createCell(21);
        cell.setCellValue("Выложена по центру полки");
        cell.setCellStyle(headerStyle3);

        cell = row.createCell(22);
        cell.setCellStyle(headerStyle4);
        cell = row.createCell(23);
        cell.setCellStyle(headerStyle4);
        cell = row.createCell(24);
        cell.setCellStyle(headerStyle4);
        cell = row.createCell(25);
        cell.setCellStyle(headerStyle4);

        cell = row.createCell(26);
        cell.setCellValue("МЗ");
        cell.setCellStyle(headerStyle4);
        cell = row.createCell(27);
        cell.setCellValue("КП + Соус");
        cell.setCellStyle(headerStyle4);
        cell = row.createCell(28);
        cell.setCellValue("Масло");
        cell.setCellStyle(headerStyle4);
        cell = row.createCell(29);
        cell.setCellStyle(headerStyle);

        /*if (activityMap != null) {
            for (Map.Entry<TMAActivity, Integer> pair : activityMap.entrySet()) {
                int colNum = pair.getValue();
                cell = row.createCell(colNum);
                cell.setCellValue("Желтый ценник");
                cell.setCellStyle(headerStyle);
                cell = row.createCell(colNum + 1);
                cell.setCellValue("Наличие OOS");
                cell.setCellStyle(headerStyle);
                spreadsheet.setColumnWidth(colNum, 3000);
                spreadsheet.setColumnWidth(colNum + 1, 3000);
            }
        }*/

        // Объединение ячеек
        spreadsheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 28));

        spreadsheet.addMergedRegion(new CellRangeAddress(2, 3, 0, 0));
        spreadsheet.addMergedRegion(new CellRangeAddress(2, 3, 1, 1));
        spreadsheet.addMergedRegion(new CellRangeAddress(2, 3, 2, 2));
        spreadsheet.addMergedRegion(new CellRangeAddress(2, 3, 3, 3));
        spreadsheet.addMergedRegion(new CellRangeAddress(2, 3, 4, 4));
        spreadsheet.addMergedRegion(new CellRangeAddress(2, 3, 5, 5));
        spreadsheet.addMergedRegion(new CellRangeAddress(2, 3, 6, 6));
        spreadsheet.addMergedRegion(new CellRangeAddress(2, 3, 7, 7));

        spreadsheet.addMergedRegion(new CellRangeAddress(2, 2, 8, 12));
        spreadsheet.addMergedRegion(new CellRangeAddress(2, 2, 13, 17));
        spreadsheet.addMergedRegion(new CellRangeAddress(2, 2, 18, 21));

        spreadsheet.addMergedRegion(new CellRangeAddress(2, 3, 22, 22));
        spreadsheet.addMergedRegion(new CellRangeAddress(2, 3, 23, 23));
        spreadsheet.addMergedRegion(new CellRangeAddress(2, 3, 24, 24));
        spreadsheet.addMergedRegion(new CellRangeAddress(2, 3, 25, 25));
        spreadsheet.addMergedRegion(new CellRangeAddress(2, 3, 29, 29));

        spreadsheet.addMergedRegion(new CellRangeAddress(2, 2, 26, 28));

        //ширина столбцов
        spreadsheet.setColumnWidth(1, 5000);
        spreadsheet.setColumnWidth(2, 7000);
        spreadsheet.setColumnWidth(3, 5000);
        spreadsheet.setColumnWidth(4, 7000);
        for (int i = 8; i < 25; i++) {
            spreadsheet.setColumnWidth(i, 3000);
        }
        spreadsheet.setColumnWidth(26, 13000);
        spreadsheet.setColumnWidth(27, 13000);
        spreadsheet.setColumnWidth(28, 13000);
    }

    @Override
    public void writeOneTtToConcreteSheet(List parameters) {
        String netName = (String) parameters.get(0);
        String oblName = (String) parameters.get(1);
        String cityName = (String) parameters.get(2);
        String shopName = (String) parameters.get(3);
        boolean hasPhotos = (boolean) parameters.get(4);
        String commentMZ = (String) parameters.get(5);
        String commentKS = (String) parameters.get(6);
        String commentM = (String) parameters.get(7);
        ArrayList<Boolean> photoList = (ArrayList<Boolean>) parameters.get(8);
        ArrayList<TMAActivity> activities = (ArrayList<TMAActivity>) parameters.get(9);
        String isChecked = (String) parameters.get(10);
        LocalDate savedDate = (LocalDate) parameters.get(11);
        int visitCount = (int) parameters.get(12);

        // стили
        XSSFCellStyle leftTextStyle = createBorderedStyle();
        XSSFCellStyle centerTextStyle = createBorderedStyle();
        centerTextStyle.setAlignment(HorizontalAlignment.CENTER);

        if (hasPhotos) {
            leftTextStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            centerTextStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            if (isChecked.equals("0")) {
                leftTextStyle.setFillForegroundColor(new XSSFColor(new Color(255, 230, 153)));
                centerTextStyle.setFillForegroundColor(new XSSFColor(new Color(255, 230, 153)));
            } else {
                leftTextStyle.setFillForegroundColor(new XSSFColor(new Color(226, 239, 218)));
                centerTextStyle.setFillForegroundColor(new XSSFColor(new Color(226, 239, 218)));
            }
        }

        XSSFRow row = spreadsheet.createRow(rowIndex++);
        Cell cell = row.createCell(0);
        cell.setCellValue(rowIndex - 4);
        cell.setCellStyle(centerTextStyle);
        cell = row.createCell(1);
        cell.setCellStyle(leftTextStyle);
        cell = row.createCell(2);
        if (!oblName.equals("10%")) {
            cell.setCellValue(oblName);
        }
        cell.setCellStyle(leftTextStyle);
        cell = row.createCell(3);
        /*if (!cityName.equals("Все города")) {
            cell.setCellValue(cityName);
        }*/
        cell.setCellStyle(leftTextStyle);
        cell = row.createCell(4);
        cell.setCellValue(shopName);
        cell.setCellStyle(leftTextStyle);
        cell = row.createCell(5);
        cell.setCellStyle(leftTextStyle);
        cell = row.createCell(6);
        cell.setCellValue(646.8);
        cell.setCellStyle(leftTextStyle);
        cell = row.createCell(7);
        cell.setCellValue(hasPhotos ? visitCount : -1);
        cell.setCellStyle(leftTextStyle);

        for (int i = 0; i < 14; i++) {
            XSSFCellStyle cellStyle = (XSSFCellStyle) centerTextStyle.clone();
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            if (i >= 0 && i < 5) {
                cellStyle.setFillForegroundColor(lightBlue);
            } else if (i >= 5 && i < 10) {
                cellStyle.setFillForegroundColor(lightRed);
            } else {
                cellStyle.setFillForegroundColor(lightGreen);
            }
            cell = row.createCell(i + 8);
            if (hasPhotos && isChecked.equals("1")) {
                if ((i >= 0 && i < 5 && !photoList.get(14)) ||
                        (i >= 5 && i < 10 && !photoList.get(15)) ||
                        (i >= 10 && !photoList.get(16)))
                cell.setCellValue(photoList.get(i) ? "+" : "-");
            }
            cell.setCellStyle(cellStyle);
        }

        cell = row.createCell(22);
        XSSFCellStyle lastColumnsStyle = (XSSFCellStyle) leftTextStyle.clone();
        lastColumnsStyle.setFillForegroundColor(lightPurple);
        lastColumnsStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cell.setCellStyle(lastColumnsStyle);
        cell.setCellType(CellType.FORMULA);
        cell.setCellFormula("COUNTIF(K" + rowIndex + ":M" + rowIndex + ",\"-\")+COUNTIF(P" + rowIndex + ":R" + rowIndex + ",\"-\")");

        cell = row.createCell(23);
        cell.setCellStyle(lastColumnsStyle);
        cell.setCellType(CellType.FORMULA);
        cell.setCellFormula("W" + rowIndex + "*Z" + rowIndex);

        cell = row.createCell(24);
        cell.setCellStyle(lastColumnsStyle);

        cell = row.createCell(25);
        cell.setCellStyle(lastColumnsStyle);
        cell.setCellType(CellType.FORMULA);
        cell.setCellFormula("G" + rowIndex + "*2%");

        cell = row.createCell(26);
        XSSFCellStyle commentStyle = (XSSFCellStyle) lastColumnsStyle.clone();
        commentStyle.setAlignment(HorizontalAlignment.LEFT);
        cell.setCellValue(commentMZ);
        cell.setCellType(CellType.STRING);
        cell.setCellStyle(leftTextStyle);

        cell = row.createCell(27);
        cell.setCellValue(commentKS);
        cell.setCellStyle(leftTextStyle);

        cell = row.createCell(28);
        cell.setCellStyle(leftTextStyle);
        cell.setCellValue(commentM);

        cell = row.createCell(29);
        cell.setCellStyle(centerTextStyle);
        if (savedDate != null) {
            cell.setCellValue(foto_verif.util.DateUtil.format(savedDate));
        }

        /*XSSFCellStyle centerErrorStyle = (XSSFCellStyle) centerTextStyle.clone();
        centerErrorStyle.setFillForegroundColor(new XSSFColor(new Color(255, 242, 204)));
        centerErrorStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);*/

        /*if (activityMap != null && activityMap.size() > 0) {
            for (Map.Entry<TMAActivity, Integer> pair : activityMap.entrySet()) {
                if (activities != null && activities.contains(pair.getKey())) {
                    TMAActivity tmaActivity = activities.get(activities.indexOf(pair.getKey()));
                    int colNum = pair.getValue();
                    cell = row.createCell(colNum);
                    cell.setCellValue(tmaActivity.isSalePrice() ? 1 : 0);
                    cell.setCellStyle(centerTextStyle);
                    cell = row.createCell(colNum + 1);
                    cell.setCellValue(tmaActivity.isHasOos() ? 1 : 0);
                    cell.setCellStyle(centerTextStyle);
                } else {
                    int colNum = pair.getValue();
                    cell = row.createCell(colNum);
                    cell.setCellValue(0);
                    cell.setCellStyle(centerTextStyle);
                    cell = row.createCell(colNum + 1);
                    cell.setCellValue(0);
                    cell.setCellStyle(centerTextStyle);
                }
            }
        }*/
    }

    @Override
    public void calcSumRowConcreteSheet(String partForHeader) {
        //downLimitMap.put(net, rowIndex);

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
        cell = row.createCell(1);
        cell.setCellStyle(cellStyle);
        cell = row.createCell(2);
        cell.setCellStyle(cellStyle);
        cell = row.createCell(3);
        cell.setCellStyle(cellStyle);
        cell = row.createCell(4);
        cell.setCellStyle(cellStyle);
        cell = row.createCell(5);
        cell.setCellStyle(cellStyle);
        cell = row.createCell(6);
        cell.setCellStyle(cellStyle);

        cell = row.createCell(7);
        cell.setCellStyle(cellStyle);
        //cell.setCellType(CellType.FORMULA);
        //cell.setCellFormula("SUM(H5:H" + rowIndex + ")");

        for (int i = 8; i < 22; i++) {
            cell = row.createCell(i);
            cell.setCellType(CellType.FORMULA);
            cell.setCellFormula("COUNTIF(" + (char) ('A' + i) + 5 + ":" + (char) ('A' + i) + rowIndex + ",\"+\")");
            cell.setCellStyle(cellStyle);
        }

        for (int i = 22; i < 25; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
            cell.setCellType(CellType.FORMULA);
            cell.setCellFormula("SUM(" + (char) ('A' + i) + 5 + ":" + (char) ('A' + i) + rowIndex + ")");
        }
        for (int i = 25; i < 29; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
        }

        cell = row.createCell(29);
        cell.setCellStyle(cellStyle);
        
        row = spreadsheet.createRow(rowIndex + 1);
        cell = row.createCell(0);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Общая сумма штрафов");

        for (int i = 1; i < 23; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
        }

        cell = row.createCell(23);
        cell.setCellStyle(cellStyle);
        cell.setCellType(CellType.FORMULA);
        cell.setCellFormula("X" + (rowIndex + 1) + "+Y" + (rowIndex + 1));

        for (int i = 24; i < 29; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
        }
        cell = row.createCell(29);
        cell.setCellStyle(cellStyle);

        spreadsheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, 6));
        spreadsheet.addMergedRegion(new CellRangeAddress(rowIndex + 1, rowIndex + 1, 0, 22));
        spreadsheet.addMergedRegion(new CellRangeAddress(rowIndex + 1, rowIndex + 1, 23, 25));

        /*if (activityMap != null) {
            for (int i = 23; i < 23 + activityMap.size() * 2; i++) {
                String col;
                if (i < 26) {
                    col = "" + (char) ('A' + i);
                } else {
                    col = "" + (char) ('A' + i / 26 - 1) + (char) ('A' + i % 26);
                }
                cell = row.createCell(i);
                cell.setCellType(CellType.FORMULA);
                cell.setCellFormula("SUM(" + col + 5 + ":" + col + rowIndex + ")");
                cell.setCellStyle(cellStyle);
            }
        }*/

        // условное форматирование
        /*SheetConditionalFormatting sheetCF = spreadsheet.getSheetConditionalFormatting();
        ConditionalFormattingRule rule = sheetCF.createConditionalFormattingRule(ComparisonOperator.EQUAL, "0");
        PatternFormatting fill = rule.createPatternFormatting();
        fill.setFillBackgroundColor(new XSSFColor(new Color(255, 204, 204)));
        fill.setFillPattern(PatternFormatting.SOLID_FOREGROUND);
        FontFormatting fontF = rule.createFontFormatting();
        fontF.setFontColorIndex(HSSFColor.RED.index);
        CellRangeAddress[] regions = {
                CellRangeAddress.valueOf("F5:T" + rowIndex)
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
