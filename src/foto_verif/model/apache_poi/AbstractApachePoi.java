package foto_verif.model.apache_poi;

import foto_verif.model.TMAActivity;
import foto_verif.view.lka.LkaCriteria;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.*;

import java.io.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

/**
 * Created by market6 on 13.01.2017.
 */
public abstract class AbstractApachePoi implements ApachePoi {
    XSSFWorkbook workbook;
    XSSFSheet spreadsheet;
    String reportPath;
    String dateFrom;
    String dateTo;
    int rowIndex = 4;
    int nkaRowIndex = 0;
    int nkaSumStartRow;
    int nkaSumEndRow;

    HashMap<String, Integer> downLimitMap = new HashMap<>();
    HashMap<TMAActivity, Integer> activityMap = null;

    XSSFCellStyle titleStyle;
    XSSFCellStyle headerStyle;
    XSSFCellStyle headerStyle1;
    XSSFCellStyle headerStyle2;
    XSSFCellStyle headerStyle3;
    XSSFCellStyle headerStyle4;

    XSSFColor lightRed;
    XSSFColor lightGreen;
    XSSFColor lightBlue;
    XSSFColor lightPurple;

    public void setReportPath(String reportPath) {
        this.reportPath = reportPath;
    }

    public void createReportFile(String dateFrom, String dateTo) {
        workbook = new XSSFWorkbook();
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public void endWriting(String net) throws IOException {
        FileOutputStream out = new FileOutputStream(new File(reportPath + "/photo_report " + net + " " + dateFrom + "-" + dateTo + ".xlsx"));
        workbook.write(out);
        out.close();
    }

    public ArrayList<TMAActivity> getTMAActivityFromFile(int photoType, String netName, LocalDate dateFrom, LocalDate dateTo) throws IOException {
        File file = new File("tma/tma_list.xlsx");
        if (!file.exists() || !file.isFile())
            return null;
        ArrayList<TMAActivity> activityList = new ArrayList<>();
        FileInputStream fis = new FileInputStream(file);
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(fis);
        XSSFSheet spreadsheet = xssfWorkbook.getSheetAt(0);
        XSSFRow row;
        Iterator<Row> rowIterator = spreadsheet.iterator();
        int i = 0;
        while (rowIterator.hasNext()) {
            i++;
            if (i < 4) {
                rowIterator.next();
                continue;
            }
            row = (XSSFRow) rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();
            Cell cell = cellIterator.next();
            String channel = cell.getStringCellValue();
            if (channel == "")
                break;
            cell = cellIterator.next();
            String net = cell.getStringCellValue();
            cell = cellIterator.next();
            String tg = cell.getStringCellValue();
            cell = cellIterator.next();
            String tmaName = cell.getStringCellValue();
            cell = cellIterator.next();
            LocalDate dateStart = cell.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            cell = cellIterator.next();
            LocalDate dateEnd = cell.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            TMAActivity activity = new TMAActivity(channel, net, tg, tmaName, dateStart, dateEnd, false, false);
            activityList.add(activity);
        }

        Collections.sort(activityList);

        String selectedChannel;
        if ((photoType == 1 || photoType == 3) && netName.equals("X5")) {
            selectedChannel = "X5";
        } else if ((photoType == 1 || photoType == 3) && netName.equals("Тандер")) {
            selectedChannel = "Тандер";
        } else {
            selectedChannel = "LKA";
        }

        for (int k = activityList.size() - 1; k >= 0; k--) {
            if (!activityList.get(k).getChannel().equals(selectedChannel)) {
                activityList.remove(k);
                continue;
            }

            if (!(
                    (activityList.get(k).getActivityStartDate().isBefore(dateTo) || activityList.get(k).getActivityStartDate().isEqual(dateTo))
                            &&
                            (activityList.get(k).getActivityEndDate().isAfter(dateFrom) || activityList.get(k).getActivityEndDate().isEqual(dateFrom))
            )) {
                activityList.remove(k);
                continue;
            }
        }

        return activityList;
    }

    public List<LkaCriteria> getLkaCriteriaList() throws IOException {
        List<LkaCriteria> result = new ArrayList<>();
        File file = new File("save/критерии выкладка.xlsx");
        if (!file.exists() || !file.isFile())
            return null;
        FileInputStream fis = new FileInputStream(file);
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(fis);
        XSSFSheet spreadsheet = xssfWorkbook.getSheetAt(0);
        XSSFRow row;
        Iterator<Row> rowIterator = spreadsheet.iterator();
        int i = 0;
        while (rowIterator.hasNext()) {
            i++;
            if (i < 2) {
                rowIterator.next();
                continue;
            }
            row = (XSSFRow) rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();
            Cell cell = cellIterator.next();
            String lka = cell.getStringCellValue();
            if (lka == "")
                break;
            cell = cellIterator.next();
            int id = (int) cell.getNumericCellValue();
            cell = cellIterator.next();
            String criteria1Text = cell.getStringCellValue();
            cell = cellIterator.next();
            double criteria1Mz = cell.getNumericCellValue();
            cell = cellIterator.next();
            double criteria1K = cell.getNumericCellValue();
            cell = cellIterator.next();
            double criteria1S = cell.getNumericCellValue();
            cell = cellIterator.next();
            String criteria2Text = cell.getStringCellValue();
            LkaCriteria lkaCriteria = new LkaCriteria(id, criteria1Text, criteria1Mz, criteria1K, criteria1S, criteria2Text);
            result.add(lkaCriteria);
        }
        return result;
    }

    protected XSSFCellStyle createBorderedStyle() {
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
        return cellStyle;
    }
}
