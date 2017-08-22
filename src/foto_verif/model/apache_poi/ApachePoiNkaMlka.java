package foto_verif.model.apache_poi;

import foto_verif.model.TMAActivity;
import foto_verif.view.NKA.NkaAddress;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFRow;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by market6 on 13.01.2017.
 */
public class ApachePoiNkaMlka extends AbstractApachePoi implements ApachePoi {
    @Override
    public void createConcreteSheet(String net, List activities) {
        rowIndex = 4;
        spreadsheet = workbook.createSheet(net);
        XSSFRow row = spreadsheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("Отчет по проверке фото в сети " + net + " c " + dateFrom + " по " + dateTo);
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
                activityMap.put(activitiesList.get(i), 22 + i * 2);
            }
        }

        row = spreadsheet.createRow(2);
        cell = row.createCell(0);
        cell.setCellValue("№");
        cell.setCellStyle(headerStyle);
        cell = row.createCell(1);
        cell.setCellValue("Сеть");
        cell.setCellStyle(headerStyle);
        cell = row.createCell(2);
        cell.setCellValue("MLKA");
        cell.setCellStyle(headerStyle);
        cell = row.createCell(3);
        cell.setCellValue("Адрес");
        cell.setCellStyle(headerStyle);
        cell = row.createCell(4);
        cell.setCellValue("Майонез");
        cell.setCellStyle(headerStyle1);
        cell = row.createCell(5);
        cell.setCellStyle(headerStyle1);
        cell = row.createCell(6);
        cell.setCellStyle(headerStyle1);
        cell = row.createCell(7);
        cell.setCellStyle(headerStyle1);
        cell = row.createCell(8);
        cell.setCellStyle(headerStyle1);
        cell = row.createCell(9);
        cell.setCellValue("Кетчуп");
        cell.setCellStyle(headerStyle2);
        cell = row.createCell(10);
        cell.setCellStyle(headerStyle2);
        cell = row.createCell(11);
        cell.setCellStyle(headerStyle2);
        cell = row.createCell(12);
        cell.setCellStyle(headerStyle2);
        cell = row.createCell(13);
        cell.setCellStyle(headerStyle2);
        cell = row.createCell(14);
        cell.setCellValue("Соус");
        cell.setCellStyle(headerStyle3);
        cell = row.createCell(15);
        cell.setCellStyle(headerStyle3);
        cell = row.createCell(16);
        cell.setCellStyle(headerStyle3);
        cell = row.createCell(17);
        cell.setCellStyle(headerStyle3);
        cell = row.createCell(18);
        cell.setCellStyle(headerStyle3);
        cell = row.createCell(19);
        cell.setCellValue("Количество нарушений");
        cell.setCellStyle(headerStyle4);
        cell = row.createCell(20);
        cell.setCellValue("Наличие out of stock");
        cell.setCellStyle(headerStyle4);
        cell = row.createCell(21);
        cell.setCellValue("Комментарии");
        cell.setCellStyle(headerStyle4);

        if (activityMap != null) {
            for (Map.Entry<TMAActivity, Integer> pair : activityMap.entrySet()) {
                int colNum = pair.getValue();
                cell = row.createCell(colNum);
                cell.setCellValue(pair.getKey().getActivityName());
                cell.setCellStyle(headerStyle);
                cell = row.createCell(colNum + 1);
                cell.setCellStyle(headerStyle);
                spreadsheet.addMergedRegion(new CellRangeAddress(2, 2, colNum, colNum + 1));
            }
        }

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
        int colInd = 4;
        for (int i = 0; i < 3; i++) {
            XSSFCellStyle cellStyle = null;
            switch (i) {
                case 0:
                    cellStyle = headerStyle1;
                    break;
                case 1:
                    cellStyle = headerStyle2;
                    break;
                case 2:
                    cellStyle = headerStyle3;
                    break;
            }
            cell = row.createCell(colInd + 0);
            cell.setCellValue("Наличие фотоотчета");
            cell.setCellStyle(cellStyle);
            cell = row.createCell(colInd + 1);
            cell.setCellValue("Корректность фото");
            cell.setCellStyle(cellStyle);
            cell = row.createCell(colInd + 2);
            cell.setCellValue("Выложена по центру полки");
            cell.setCellStyle(cellStyle);
            cell = row.createCell(colInd + 3);
            if (net.equals("X5")) {
                cell.setCellValue("Минимум 2 SKU");
            } else {
                cell.setCellValue("Занимает  30% полочного пространства");
            }
            cell.setCellStyle(cellStyle);
            cell = row.createCell(colInd + 4);
            if (net.equals("X5")) {
                cell.setCellValue("Загруженность полки");
            } else {
                cell.setCellValue("Выложена вертикальным брендблоком");
            }
            cell.setCellStyle(cellStyle);
            colInd += 5;
        }
        cell = row.createCell(19);
        cell.setCellStyle(headerStyle4);
        cell = row.createCell(20);
        cell.setCellStyle(headerStyle4);
        cell = row.createCell(21);
        cell.setCellStyle(headerStyle4);

        if (activityMap != null) {
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
        }

        // Объединение ячеек
        spreadsheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 21));
        spreadsheet.addMergedRegion(new CellRangeAddress(2, 3, 0, 0));
        spreadsheet.addMergedRegion(new CellRangeAddress(2, 3, 1, 1));
        spreadsheet.addMergedRegion(new CellRangeAddress(2, 3, 2, 2));
        spreadsheet.addMergedRegion(new CellRangeAddress(2, 3, 3, 3));
        spreadsheet.addMergedRegion(new CellRangeAddress(2, 2, 4, 8));
        spreadsheet.addMergedRegion(new CellRangeAddress(2, 2, 9, 13));
        spreadsheet.addMergedRegion(new CellRangeAddress(2, 2, 14, 18));
        spreadsheet.addMergedRegion(new CellRangeAddress(2, 3, 19, 19));
        spreadsheet.addMergedRegion(new CellRangeAddress(2, 3, 20, 20));
        spreadsheet.addMergedRegion(new CellRangeAddress(2, 3, 21, 21));
        //ширина столбцов
        spreadsheet.setColumnWidth(2, 9000);
        spreadsheet.setColumnWidth(3, 13000);
        for (int i = 4; i < 20; i++) {
            spreadsheet.setColumnWidth(i, 3000);
        }
        spreadsheet.setColumnWidth(20, 3000);
        spreadsheet.setColumnWidth(21, 13000);
    }

    @Override
    public void writeOneTtToConcreteSheet(List parameters) {

        NkaAddress nkaAddress = (NkaAddress) parameters.get(1);

        String netName = (String) parameters.get(0);
        String mlka = nkaAddress.getMlka();
        String address = nkaAddress.getAddress();
        String comment = nkaAddress.getComment();
        List<TMAActivity> activities = nkaAddress.getTmaActivityList();

        // стили
        XSSFCellStyle leftTextStyle = createBorderedStyle();
        XSSFCellStyle centerTextStyle = createBorderedStyle();
        centerTextStyle.setAlignment(HorizontalAlignment.CENTER);

        if (nkaAddress.getChecked().equals("1")) {
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
        cell.setCellValue(rowIndex - 4);
        cell.setCellStyle(centerTextStyle);
        cell = row.createCell(1);
        cell.setCellValue(netName);
        cell.setCellStyle(centerTextStyle);
        cell = row.createCell(2);
        cell.setCellValue(mlka);
        cell.setCellStyle(leftTextStyle);
        cell = row.createCell(3);
        cell.setCellValue(address);
        cell.setCellStyle(leftTextStyle);

        for (int i = 0; i < 15; i++) {
            XSSFCellStyle cellStyle = (XSSFCellStyle) centerTextStyle.clone();
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            if (i >= 0 && i < 5) {
                cellStyle.setFillForegroundColor(lightBlue);
            } else if (i >= 5 && i < 10) {
                cellStyle.setFillForegroundColor(lightRed);
            } else {
                cellStyle.setFillForegroundColor(lightGreen);
            }
            cell = row.createCell(i + 4, CellType.NUMERIC);
            if (nkaAddress.getChecked().equals("1")) {
                switch (i) {
                    case 0:
                        cell.setCellValue(nkaAddress.isHavePhotoMZ() ? 1 : 0);
                        break;
                    case 1:
                        cell.setCellValue(nkaAddress.isCorrectPhotoMZ() ? 1 : 0);
                        break;
                    case 2:
                        cell.setCellValue(nkaAddress.isCenterPhotoMZ() ? 1 : 0);
                        break;
                    case 3:
                        cell.setCellValue(nkaAddress.is_30PhotoMZ() ? 1 : 0);
                        break;
                    case 4:
                        cell.setCellValue(nkaAddress.isVertPhotoMZ() ? 1 : 0);
                        break;
                    case 5:
                        cell.setCellValue(nkaAddress.isHavePhotoK() ? 1 : 0);
                        break;
                    case 6:
                        cell.setCellValue(nkaAddress.isCorrectPhotoK() ? 1 : 0);
                        break;
                    case 7:
                        cell.setCellValue(nkaAddress.isCenterPhotoK() ? 1 : 0);
                        break;
                    case 8:
                        cell.setCellValue(nkaAddress.is_30PhotoK() ? 1 : 0);
                        break;
                    case 9:
                        cell.setCellValue(nkaAddress.isVertPhotoK() ? 1 : 0);
                        break;
                    case 10:
                        cell.setCellValue(nkaAddress.isHavePhotoS() ? 1 : 0);
                        break;
                    case 11:
                        cell.setCellValue(nkaAddress.isCorrectPhotoS() ? 1 : 0);
                        break;
                    case 12:
                        cell.setCellValue(nkaAddress.isCenterPhotoS() ? 1 : 0);
                        break;
                    case 13:
                        cell.setCellValue(nkaAddress.is_30PhotoS() ? 1 : 0);
                        break;
                    case 14:
                        cell.setCellValue(nkaAddress.isVertPhotoS() ? 1 : 0);
                        break;
                }
            }
            cell.setCellStyle(cellStyle);
        }
        cell = row.createCell(19);
        XSSFCellStyle lastColumnsStyle = (XSSFCellStyle) centerTextStyle.clone();
        lastColumnsStyle.setFillForegroundColor(lightPurple);
        lastColumnsStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cell.setCellStyle(lastColumnsStyle);
        cell.setCellType(CellType.FORMULA);
        cell.setCellFormula("COUNTIF(E" + rowIndex + ":S" + rowIndex + ",\"=0\")");
        cell = row.createCell(20);
        if (nkaAddress.getChecked().equals("1")) {
            cell.setCellValue(nkaAddress.isOos() ? 1 : 0);
        }
        cell.setCellStyle(lastColumnsStyle);
        cell = row.createCell(21);
        XSSFCellStyle commentStyle = (XSSFCellStyle) lastColumnsStyle.clone();
        commentStyle.setAlignment(HorizontalAlignment.LEFT);
        cell.setCellValue(comment);
        cell.setCellType(CellType.STRING);
        cell.setCellStyle(commentStyle);

        XSSFCellStyle centerErrorStyle = (XSSFCellStyle) centerTextStyle.clone();
        centerErrorStyle.setFillForegroundColor(new XSSFColor(new Color(255, 242, 204)));
        centerErrorStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        if (activityMap != null && activityMap.size() > 0) {
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
                    cell.setCellStyle(centerErrorStyle);
                    cell = row.createCell(colNum + 1);
                    cell.setCellValue(0);
                    cell.setCellStyle(centerErrorStyle);
                }
            }
        }
    }

    @Override
    public void calcSumRowConcreteSheet(String net) {
        downLimitMap.put(net, rowIndex);

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

        for (int i = 4; i < 21; i++) {
            cell = row.createCell(i);
            cell.setCellType(CellType.FORMULA);
            cell.setCellFormula("SUM(" + (char) ('A' + i) + 5 + ":" + (char) ('A' + i) + rowIndex + ")");
            cell.setCellStyle(cellStyle);
        }
        cell = row.createCell(21);
        cell.setCellStyle(cellStyle);
        spreadsheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, 3));

        if (activityMap != null) {
            for (int i = 22; i < 22 + activityMap.size() * 2; i++) {
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
        }

        // условное форматирование
        /*SheetConditionalFormatting sheetCF = spreadsheet.getSheetConditionalFormatting();
        ConditionalFormattingRule rule = sheetCF.createConditionalFormattingRule(ComparisonOperator.EQUAL, "0");
        PatternFormatting fill = rule.createPatternFormatting();
        fill.setFillBackgroundColor(new XSSFColor(new Color(255, 204, 204)));
        fill.setFillPattern(PatternFormatting.SOLID_FOREGROUND);
        FontFormatting fontF = rule.createFontFormatting();
        fontF.setFontColorIndex(HSSFColor.RED.index);
        CellRangeAddress[] regions = {
                CellRangeAddress.valueOf("E5:S" + rowIndex)
        };
        sheetCF.addConditionalFormatting(regions, rule);*/
    }

    @Override
    public void createTotalSheet(String net) {
        spreadsheet = workbook.createSheet("Свод");
        spreadsheet.groupColumn(4, 7);
        spreadsheet.groupColumn(12, 15);
        spreadsheet.groupColumn(20, 23);
        spreadsheet.groupColumn(28, 29);
        spreadsheet.setColumnGroupCollapsed(4, true);
        spreadsheet.setColumnGroupCollapsed(12, true);
        spreadsheet.setColumnGroupCollapsed(20, true);
        spreadsheet.setColumnGroupCollapsed(28, true);
        spreadsheet.setColumnWidth(0, 8500);
        spreadsheet.setColumnWidth(1, 2000);
        spreadsheet.setColumnWidth(2, 2000);
        spreadsheet.setColumnWidth(3, 3300);
        for (int i = 4; i < 28; i++) {
            spreadsheet.setColumnWidth(i, 3700);
        }
        for (int i = 28; i < 31; i++) {
            spreadsheet.setColumnWidth(i, 3500);
        }
    }

    @Override
    public void createTotalSheetHeader(String net) {
        // заголовок таблицы
        XSSFRow row = spreadsheet.createRow(nkaRowIndex++);
        Cell cell = row.createCell(0);
        cell.setCellValue("Свод по соблюдению планограмм в магазинах сети АО \"" + net + "\" за период с " + dateFrom + " по " + dateTo);
        cell.setCellStyle(titleStyle);
        nkaRowIndex++;

        // первая строка заголовка
        row = spreadsheet.createRow(nkaRowIndex++);
        cell = row.createCell(0);
        cell.setCellValue("MLKA");
        cell.setCellStyle(headerStyle4);
        cell = row.createCell(1);
        cell.setCellValue("Кол-во магазинов");
        cell.setCellStyle(headerStyle4);
        cell = row.createCell(2);
        cell.setCellStyle(headerStyle4);
        cell = row.createCell(3);
        cell.setCellStyle(headerStyle4);
        cell = row.createCell(4);
        cell.setCellValue("МАЙОНЕЗ");
        cell.setCellStyle(headerStyle1);
        for (int i = 5; i < 12; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(headerStyle1);
        }
        cell = row.createCell(12);
        cell.setCellValue("КЕТЧУП");
        cell.setCellStyle(headerStyle2);
        for (int i = 13; i < 20; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(headerStyle2);
        }
        cell = row.createCell(20);
        cell.setCellValue("СОУСЫ");
        cell.setCellStyle(headerStyle3);
        for (int i = 21; i < 28; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(headerStyle3);
        }
        cell = row.createCell(28);
        cell.setCellValue("Выполнено 3 критерия");
        cell.setCellStyle(headerStyle4);
        cell = row.createCell(29);
        cell.setCellStyle(headerStyle4);
        cell = row.createCell(30);
        cell.setCellValue("Итоговая оценка");
        cell.setCellStyle(headerStyle4);

        // вторая строка заголовка
        row = spreadsheet.createRow(nkaRowIndex++);
        for (int i = 0; i < 4; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(headerStyle4);
        }

        for (int i = 4; i < 28; i++) {
            cell = row.createCell(i);
            if (i % 8 == 0) {
                cell.setCellValue("Соблюдение требований к выкладке, %");
            } else if (i % 4 == 0) {
                cell.setCellValue("Соблюдение требований к выкладке, кол-во магазинов");
            }
            int tgNum = (i - 4) / 8;
            switch (tgNum) {
                case 0:
                    cell.setCellStyle(headerStyle1);
                    break;
                case 1:
                    cell.setCellStyle(headerStyle2);
                    break;
                case 2:
                    cell.setCellStyle(headerStyle3);
                    break;
            }
        }

        for (int i = 28; i < 31; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(headerStyle4);
        }

        // третья строка заголовка
        row = spreadsheet.createRow(nkaRowIndex++);
        row.setHeight((short) 1000);
        cell = row.createCell(0);
        cell.setCellStyle(headerStyle4);
        cell = row.createCell(1);
        cell.setCellValue("План");
        cell.setCellStyle(headerStyle4);
        cell = row.createCell(2);
        cell.setCellValue("Факт");
        cell.setCellStyle(headerStyle4);
        cell = row.createCell(3);
        cell.setCellValue("% выполнения");
        cell.setCellStyle(headerStyle4);

        for (int i = 0; i < 6; i++) {
            XSSFCellStyle cellStyle;
            switch (i) {
                case 0:
                case 1:
                    cellStyle = headerStyle1;
                    break;
                case 2:
                case 3:
                    cellStyle = headerStyle2;
                    break;
                case 4:
                case 5:
                    cellStyle = headerStyle3;
                    break;
                default:
                    cellStyle = null;
            }
            cell = row.createCell(i * 4 + 4);
            cell.setCellValue("Корректность фото");
            cell.setCellStyle(cellStyle);
            cell = row.createCell(i * 4 + 5);
            cell.setCellValue("Выложена по центру полки");
            cell.setCellStyle(cellStyle);
            cell = row.createCell(i * 4 + 6);
            if (net.equals("X5")) {
                cell.setCellValue("Минимум 2 SKU");
            } else {
                cell.setCellValue("Занимает  30% полочного пространства");
            }
            cell.setCellStyle(cellStyle);
            cell = row.createCell(i * 4 + 7);
            if (net.equals("X5")) {
                cell.setCellValue("Загруженность полки");
            } else {
                cell.setCellValue("Выложена вертикальным брендблоком");
            }
            cell.setCellStyle(cellStyle);
        }

        cell = row.createCell(28);
        cell.setCellValue("Кол-во магазинов");
        cell.setCellStyle(headerStyle4);
        cell = row.createCell(29);
        cell.setCellValue("% от кол-ва магазинов");
        cell.setCellStyle(headerStyle4);
        cell = row.createCell(30);
        cell.setCellStyle(headerStyle4);

        // объединение ячеек
        spreadsheet.addMergedRegion(new CellRangeAddress(nkaRowIndex - 5, nkaRowIndex - 5, 0, 30));
        spreadsheet.addMergedRegion(new CellRangeAddress(nkaRowIndex - 3, nkaRowIndex - 1, 0, 0));
        spreadsheet.addMergedRegion(new CellRangeAddress(nkaRowIndex - 3, nkaRowIndex - 2, 1, 3));
        spreadsheet.addMergedRegion(new CellRangeAddress(nkaRowIndex - 3, nkaRowIndex - 3, 4, 11));
        spreadsheet.addMergedRegion(new CellRangeAddress(nkaRowIndex - 3, nkaRowIndex - 3, 12, 19));
        spreadsheet.addMergedRegion(new CellRangeAddress(nkaRowIndex - 3, nkaRowIndex - 3, 20, 27));
        spreadsheet.addMergedRegion(new CellRangeAddress(nkaRowIndex - 2, nkaRowIndex - 2, 4, 7));
        spreadsheet.addMergedRegion(new CellRangeAddress(nkaRowIndex - 2, nkaRowIndex - 2, 8, 11));
        spreadsheet.addMergedRegion(new CellRangeAddress(nkaRowIndex - 2, nkaRowIndex - 2, 12, 15));
        spreadsheet.addMergedRegion(new CellRangeAddress(nkaRowIndex - 2, nkaRowIndex - 2, 16, 19));
        spreadsheet.addMergedRegion(new CellRangeAddress(nkaRowIndex - 2, nkaRowIndex - 2, 20, 23));
        spreadsheet.addMergedRegion(new CellRangeAddress(nkaRowIndex - 2, nkaRowIndex - 2, 24, 27));
        spreadsheet.addMergedRegion(new CellRangeAddress(nkaRowIndex - 3, nkaRowIndex - 2, 28, 29));
        spreadsheet.addMergedRegion(new CellRangeAddress(nkaRowIndex - 3, nkaRowIndex - 1, 30, 30));

        nkaSumStartRow = nkaRowIndex + 1;
    }

    @Override
    public void writeOneTtToTotalSheet(List parameters) {

        String mlka = (String) parameters.get(0);
        String net = (String) parameters.get(1);

        XSSFRow row = spreadsheet.createRow(nkaRowIndex++);

        // стили для ячеек
        XSSFCellStyle boldLeftStyle = createBorderedStyle();
        Font boldFont = workbook.createFont();
        boldFont.setBold(true);
        boldLeftStyle.setFont(boldFont);
        boldLeftStyle.setAlignment(HorizontalAlignment.LEFT);
        XSSFCellStyle centerStyle = createBorderedStyle();
        centerStyle.setAlignment(HorizontalAlignment.CENTER);
        XSSFCellStyle centerPercentageStyle = (XSSFCellStyle) centerStyle.clone();
        centerPercentageStyle.setDataFormat(workbook.createDataFormat().getFormat("0%"));
        XSSFCellStyle filledCenterStyle = (XSSFCellStyle) centerStyle.clone();
        filledCenterStyle.setFillForegroundColor(new XSSFColor(new Color(228, 223, 236)));
        filledCenterStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        XSSFCellStyle filledPercentageStyle = (XSSFCellStyle) filledCenterStyle.clone();
        filledPercentageStyle.setFont(boldFont);
        filledPercentageStyle.setDataFormat(workbook.createDataFormat().getFormat("0%"));


        Cell cell = row.createCell(0);
        cell.setCellValue(mlka);
        cell.setCellStyle(boldLeftStyle);
        cell = row.createCell(1);
        cell.setCellStyle(filledCenterStyle);
        cell = row.createCell(2);
        cell.setCellStyle(filledCenterStyle);
        cell.setCellType(CellType.FORMULA);
        cell.setCellFormula("COUNTIF(" + net + "!$C$5:$C$" + downLimitMap.get(net) + ",A" + nkaRowIndex + ")");
        cell = row.createCell(3);
        cell.setCellStyle(filledPercentageStyle);
        cell.setCellType(CellType.FORMULA);
        cell.setCellFormula("IFERROR(C" + nkaRowIndex + "/B" + nkaRowIndex + ",0)");

        for (int i = 4; i < 28; i++) {
            cell = row.createCell(i);
            int tgNum = (i - 4) / 8;
            int formulaType = (i / 4) % 2;

            if (formulaType == 1) {
                int corrIndex;
                switch (tgNum) {
                    case 0:
                        corrIndex = 1;
                        break;
                    case 1:
                        corrIndex = -2;
                        break;
                    case 2:
                        corrIndex = -5;
                        break;
                    default:
                        corrIndex = 0;
                }
                cell.setCellStyle(centerStyle);
                cell.setCellType(CellType.FORMULA);
                cell.setCellFormula("SUMIFS(" + net + "!" + (char) ('A' + (i + corrIndex)) + "$5:" +
                        (char) ('A' + (i + corrIndex)) + "$" + downLimitMap.get(net) + "," + net +
                        "!$C$5:$C$" + downLimitMap.get(net) + ",$A" + nkaRowIndex + ")");
            } else {
                cell.setCellStyle(centerPercentageStyle);
                cell.setCellType(CellType.FORMULA);
                cell.setCellFormula("IFERROR(" + (char) ('A' + (i - 4)) + "" + nkaRowIndex + "/$C" + nkaRowIndex + ",0)");
            }
        }

        cell = row.createCell(28);
        cell.setCellStyle(filledCenterStyle);
        cell = row.createCell(29);
        cell.setCellStyle(filledPercentageStyle);
        cell = row.createCell(30);
        cell.setCellStyle(filledPercentageStyle);
        cell.setCellType(CellType.FORMULA);
        cell.setCellFormula("AVERAGE(J" + nkaRowIndex + ":L" + nkaRowIndex + ",R" + nkaRowIndex +
                ":T" + nkaRowIndex + ",Z" + nkaRowIndex + ":AB" + nkaRowIndex + ")");
        nkaSumEndRow = nkaRowIndex;
    }

    @Override
    public void calcSumRowTotalSheet() {
        XSSFCellStyle leftStyle = createBorderedStyle();
        Font boldFont = workbook.createFont();
        boldFont.setBold(true);
        leftStyle.setFont(boldFont);
        leftStyle.setAlignment(HorizontalAlignment.LEFT);
        leftStyle.setFillForegroundColor(HSSFColor.YELLOW.index);
        leftStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        XSSFCellStyle centerStyle = (XSSFCellStyle) leftStyle.clone();
        centerStyle.setAlignment(HorizontalAlignment.CENTER);
        XSSFCellStyle percentageStyle = (XSSFCellStyle) centerStyle.clone();
        percentageStyle.setDataFormat(workbook.createDataFormat().getFormat("0%"));

        XSSFRow row = spreadsheet.createRow(nkaRowIndex++);
        Cell cell = row.createCell(0);
        cell.setCellStyle(leftStyle);
        cell.setCellValue("Итого");
        cell = row.createCell(1);
        cell.setCellStyle(centerStyle);
        cell.setCellType(CellType.FORMULA);
        cell.setCellFormula("SUM(B" + nkaSumStartRow + ":B" + nkaSumEndRow + ")");
        cell = row.createCell(2);
        cell.setCellStyle(centerStyle);
        cell.setCellType(CellType.FORMULA);
        cell.setCellFormula("SUM(C" + nkaSumStartRow + ":C" + nkaSumEndRow + ")");
        cell = row.createCell(3);
        cell.setCellStyle(percentageStyle);
        cell.setCellType(CellType.FORMULA);
        cell.setCellFormula("IFERROR(C" + nkaRowIndex + "/B" + nkaRowIndex + ",0)");
        for (int i = 4; i < 28; i++) {
            cell = row.createCell(i);
            cell.setCellType(CellType.FORMULA);
            if ((i / 4) % 2 == 1) {
                cell.setCellStyle(centerStyle);
                cell.setCellFormula("SUM(" + (char) ('A' + i) + "" + nkaSumStartRow + ":" + (char) ('A' + i) + "" + nkaSumEndRow + ")");
            } else {
                cell.setCellStyle(percentageStyle);
                cell.setCellFormula("IFERROR(" + (char) ('A' + i - 4) + "" + nkaRowIndex + "/$C" + nkaRowIndex + ",0)");
            }
        }
        cell = row.createCell(28);
        cell.setCellStyle(centerStyle);
        cell.setCellType(CellType.FORMULA);
        cell.setCellFormula("SUM(AC" + nkaSumStartRow + ":AC" + nkaSumEndRow + ")");
        cell = row.createCell(29);
        cell.setCellStyle(percentageStyle);
        cell.setCellType(CellType.FORMULA);
        cell.setCellFormula("IFERROR(AC" + nkaRowIndex + "/C" + nkaRowIndex + ",0)");
        cell = row.createCell(30);
        cell.setCellStyle(percentageStyle);
        cell.setCellType(CellType.FORMULA);
        cell.setCellFormula("AVERAGE(J" + nkaRowIndex + ":L" + nkaRowIndex + ",R" + nkaRowIndex +
                ":T" + nkaRowIndex + ",Z" + nkaRowIndex + ":AB" + nkaRowIndex + ")");

        nkaRowIndex++;
        nkaRowIndex++;
    }
}
