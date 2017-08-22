package foto_verif.model.jdbc;

/**
 * Created by market6 on 11.01.2017.
 */
public class JdbcModelManager {
    private static JdbcModel jdbcModel = null;

    public static void createJdbcModel (int photoTypeSelected) {
        if (photoTypeSelected == 0) jdbcModel = new JdbcModelDmpLka();
        else if (photoTypeSelected == 1) jdbcModel = new JdbcModelNka();
        else if (photoTypeSelected == 2) jdbcModel = new JdbcModelDmpNka();
        else if (photoTypeSelected == 4) jdbcModel = new JdbcModelLka();
        else throw new RuntimeException("Wrong photoType. Can not create jdbc model.");
    }

    public synchronized static JdbcModel getInstance() {
        if (jdbcModel != null) return jdbcModel;
        else throw new RuntimeException("Jdbc model wasn't created.");
    }
}
