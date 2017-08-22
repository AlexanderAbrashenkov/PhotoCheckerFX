package foto_verif.model.jdbc;

import foto_verif.model.Photo;
import foto_verif.util.DateUtil;
import foto_verif.view.lka.LkaAddress;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by market6 on 10.01.2017.
 */
public class JdbcModelLka extends AbstractJdbcModel implements JdbcModel {

    private HashMap<String, Integer> regionMap = new HashMap<>();

    private Date dDateFrom;
    private Timestamp dDateTo;
    private String sDateFrom;
    private String sDateTo;


    public JdbcModelLka() {
        checkConnection();
    }

    @Override
    public List<String> getDatasForFirstQuery(String nothing, LocalDate dateFrom, LocalDate dateTo) throws SQLException {
        regionMap.clear();
        dDateFrom = Date.valueOf(dateFrom);
        dDateTo = Timestamp.valueOf(dateTo.atTime(23, 59, 59));
        sDateFrom = DateUtil.format(dateFrom);
        sDateTo = DateUtil.format(dateTo);
        PreparedStatement statement = connection.prepareStatement(
                "SELECT distinct r.region_id2, r.region_name\n" +
                        "FROM report_foto f\n" +
                        "left JOIN report_foto_tag tag ON tag.foto_id = f.id\n" +
                        "INNER JOIN client_card ccd ON ccd.client_id = f.client_id\n" +
                        "LEFT JOIN co_contractor c ON c.id = ccd.contractor_id\n" +
                        "LEFT JOIN co_contractor_attr_customer ccac ON ccac.contractor_id = c.id\n" +
                        "INNER JOIN region2 r ON r.region_id2 = ccac.region_id2\n" +
                        "LEFT JOIN client_card_category ccc ON ccc.category_id = ccd.category_id\n" +
                        "LEFT JOIN sam_network sn ON sn.id = ccd.tnet_id\n" +
                        "LEFT JOIN client_card_type as tt on ccd.type_id=tt.type_id and tt.factory_id=2\n" +
                        "WHERE tag.tag_id IS NULL\n" +
                        "AND ccd.factory_id = 2\n" +
                        "and ccd.category_id = 1\n" +
                        "AND f.date_foto BETWEEN ? AND ?\n" +
                        "AND c.active = 1\n" +
                        "AND ccd.visible = 0\n" +
                        "AND r.region_id2 in (1, 2, 3, 4, 5, 6, 10, 11)");
        statement.setQueryTimeout(QUERY_TIMEOUT);
        statement.setDate(1, dDateFrom);
        statement.setTimestamp(2, dDateTo);
        ResultSet resultSet = statement.executeQuery();
        ArrayList<String> result = new ArrayList<>();
        while (resultSet.next()) {
            regionMap.put(resultSet.getString("region_name").trim(), resultSet.getInt("region_id2"));
            result.add(resultSet.getString("region_name").trim());
        }
        return result;
    }

    @Override
    public List<String> getOblsList(String region) throws SQLException {
        return null;
    }

    @Override
    public List<String> getChannelList(String obl) throws SQLException {
        /* Doesn't use here */
        return null;
    }


    @Override
    public List<String> getNetList() {
        /* Doesn't use here */
        return null;
    }

    @Override
    public List<String> getMlkaList(String obl) throws SQLException {
        return null;
    }

    @Override
    public List<String> getNkaList(String nkaName) throws SQLException {
        /* Doesn't use here */
        return null;
    }

    @Override
    public List<LkaAddress> getAddresses(String regionName, String nothing1) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT r.region_name, ccac.city, c.name, c.id, sn.name as lka_name, sn.id as lka_id,\n" +
                "ccd.client_id, ccd.client_name, ccd.client_adress, f.date_foto, f.date_add, f.src, tt.type, f.comment\n" +
                "FROM report_foto f\n" +
                "left JOIN report_foto_tag tag ON tag.foto_id = f.id\n" +
                "INNER JOIN client_card ccd ON ccd.client_id = f.client_id\n" +
                "LEFT JOIN co_contractor c ON c.id = ccd.contractor_id\n" +
                "LEFT JOIN co_contractor_attr_customer ccac ON ccac.contractor_id = c.id\n" +
                "INNER JOIN region2 r ON r.region_id2 = ccac.region_id2\n" +
                "LEFT JOIN client_card_category ccc ON ccc.category_id = ccd.category_id\n" +
                "LEFT JOIN sam_network sn ON sn.id = ccd.tnet_id\n" +
                "LEFT JOIN client_card_type as tt on ccd.type_id=tt.type_id and tt.factory_id=2\n" +
                "WHERE tag.tag_id IS NULL\n" +
                "AND ccd.factory_id = 2\n" +
                "and ccd.category_id = 1\n" +
                "AND f.date_foto BETWEEN ? AND ?\n" +
                "AND c.active = 1\n" +
                "AND ccd.visible = 0\n" +
                "AND r.region_id2 = ?\n" +
                "order by 1, 4, 6, 7");
        statement.setQueryTimeout(QUERY_TIMEOUT);
        statement.setDate(1, dDateFrom);
        statement.setTimestamp(2, dDateTo);
        statement.setInt(3, regionMap.get(regionName));
        ResultSet resultSet = statement.executeQuery();
        return fillUpAddressesList(resultSet);
    }

    private List<LkaAddress> fillUpAddressesList(ResultSet resultSet) throws SQLException {
        int id = 0;
        String name = null;
        String address = null;
        String region = null;
        String obl = null;
        String lka = null;
        String typeName = null;
        ArrayList<Photo> photoList = new ArrayList<>();
        List<LkaAddress> addressesList = new ArrayList<>();
        while (resultSet.next()) {
            if (id == 0) {
                id = resultSet.getInt("client_id");
                photoList.add(new Photo(
                        resultSet.getDate("date_foto").toLocalDate(),
                        resultSet.getDate("date_add").toLocalDate(),
                        "https://report.ncsd.ru/upload/foto100g3/" + DateUtil.format(resultSet.getDate("date_add").toLocalDate()).substring(6, 10) +
                                "_" + DateUtil.format(resultSet.getDate("date_add").toLocalDate()).substring(3, 5) + "/" +
                                DateUtil.format(resultSet.getDate("date_add").toLocalDate()).substring(0, 2) + "/thumb/" + resultSet.getString("src"),
                        resultSet.getString("comment"), false
                ));
            } else if (resultSet.getInt("client_id") == id) {
                id = resultSet.getInt("client_id");
                photoList.add(new Photo(
                        resultSet.getDate("date_foto").toLocalDate(),
                        resultSet.getDate("date_add").toLocalDate(),
                        "https://report.ncsd.ru/upload/foto100g3/" + DateUtil.format(resultSet.getDate("date_add").toLocalDate()).substring(6, 10) +
                                "_" + DateUtil.format(resultSet.getDate("date_add").toLocalDate()).substring(3, 5) + "/" +
                                DateUtil.format(resultSet.getDate("date_add").toLocalDate()).substring(0, 2) + "/thumb/" + resultSet.getString("src"),
                        resultSet.getString("comment"), false
                ));
            } else {
                // добавляем точку с фотками в список
                LkaAddress lkaAddress = new LkaAddress(id, name, region, obl, lka, address,
                        (ArrayList) photoList.clone(), new ArrayList<>(), "0", typeName,
                        true, false, false, false, false, false,
                        true, false, false, false, false,
                        true, false, false, false, false,
                        false, null, null);
                addressesList.add(lkaAddress);
                photoList.clear();
                photoList.add(new Photo(
                        resultSet.getDate("date_foto").toLocalDate(),
                        resultSet.getDate("date_add").toLocalDate(),
                        "https://report.ncsd.ru/upload/foto100g3/" + DateUtil.format(resultSet.getDate("date_add").toLocalDate()).substring(6, 10) +
                                "_" + DateUtil.format(resultSet.getDate("date_add").toLocalDate()).substring(3, 5) + "/" +
                                DateUtil.format(resultSet.getDate("date_add").toLocalDate()).substring(0, 2) + "/thumb/" + resultSet.getString("src"),
                        resultSet.getString("comment"), false
                ));
                id = resultSet.getInt("client_id");
            }

            name = resultSet.getString("client_name");
            address = resultSet.getString("client_adress");
            region = resultSet.getString("region_name").trim();
            String oblTemp = resultSet.getString("city");
            if (oblTemp.startsWith("Москва") || oblTemp.startsWith("Санкт")) {
                obl = oblTemp.substring(0, oblTemp.indexOf("(") - 1) + " (" + resultSet.getString("name").trim() + ")";
            } else if ((oblTemp.length() - oblTemp.replaceAll(",", "").length()) == 2) {
                obl = oblTemp.substring(oblTemp.indexOf(",") + 2, oblTemp.lastIndexOf(",")) + " (" + resultSet.getString("name").trim() + ")";
            } else {
                obl = oblTemp.substring(oblTemp.indexOf("(") + 1, oblTemp.indexOf(",")) + " (" + resultSet.getString("name").trim() + ")";
            }
            lka = resultSet.getString("lka_name") + " (" + resultSet.getInt("lka_id") + ")";
            typeName = resultSet.getString("type") == null ? "" : resultSet.getString("type").trim();
        }

        LkaAddress lkaAddress = new LkaAddress(id, name, region, obl, lka, address,
                (ArrayList) photoList.clone(), new ArrayList<>(), "0", typeName,
                true, false, false, false, false, false,
                true, false, false, false, false,
                true,false, false, false, false,
                false, null, null);
        addressesList.add(lkaAddress);

        photoList.clear();
        return addressesList;
    }
}
