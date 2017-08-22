package foto_verif.model.apache_poi;

import foto_verif.model.TMAActivity;
import foto_verif.view.lka.LkaCriteria;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by market6 on 13.01.2017.
 */
public interface ApachePoi {

    void setReportPath(String reportPath);

    void createReportFile(String dateFrom, String dateTo);

    void endWriting(String net) throws IOException;

    ArrayList<TMAActivity> getTMAActivityFromFile(int photoType, String netName, LocalDate dateFrom, LocalDate dateTo) throws IOException;

    void createConcreteSheet(String partForHeader, List activities);

    void writeOneTtToConcreteSheet(List parameters);

    void calcSumRowConcreteSheet(String partForHeader);

    void createTotalSheet(String partForHeader);

    void createTotalSheetHeader(String partForHeader);

    void writeOneTtToTotalSheet(List parameters);

    void calcSumRowTotalSheet();

    List<LkaCriteria> getLkaCriteriaList() throws IOException;
}
