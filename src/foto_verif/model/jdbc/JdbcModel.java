package foto_verif.model.jdbc;

import foto_verif.Main;
import foto_verif.model.AddressTT;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by market6 on 10.01.2017.
 */
public interface JdbcModel {

    void setMain(Main main);

    List<String> getDatasForFirstQuery(String net, LocalDate dateFrom, LocalDate dateTo) throws SQLException;

    List<String> getOblsList(String region) throws SQLException;

    List<String> getChannelList(String obl) throws SQLException;

    List<String> getNetList() throws SQLException;

    List<String> getMlkaList(String obl) throws SQLException;

    List<String> getNkaList(String rjkamName) throws SQLException;

    List<?> getAddresses(String parameter1, String parameter2) throws SQLException;

    void stop();
}