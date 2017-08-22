package foto_verif.model.jdbc;

import foto_verif.Main;
import foto_verif.util.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by market6 on 10.01.2017.
 */
public abstract class AbstractJdbcModel implements JdbcModel {

    final static int QUERY_TIMEOUT = 600;

    protected Main main;
    protected Connection connection;

    protected static final String URL = "jdbc:sqlserver://192.168.2.128:1433";
    protected static final String USER_NAME = "kgk_analitic";
    protected static final String PASSWORD = "p3kd52";

    protected void checkConnection() {
        if (connection == null) {
            try {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
                connection.setCatalog("nefco");
            } catch (Exception e) {
                Logger.log(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public void stop() {
        try {
            connection.close();
            System.out.println("connection closed");
        } catch (SQLException e) {
            Logger.log(e.getMessage());
            e.printStackTrace();
        }
    }

}
