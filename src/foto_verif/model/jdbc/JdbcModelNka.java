package foto_verif.model.jdbc;

import foto_verif.model.AddressTT;
import foto_verif.model.NkaAddressTT;
import foto_verif.model.Photo;
import foto_verif.model.TMAActivity;
import foto_verif.util.DateUtil;
import foto_verif.util.Logger;
import foto_verif.view.NKA.NkaAddress;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by market6 on 10.01.2017.
 */
public class JdbcModelNka extends AbstractJdbcModel implements JdbcModel {

    private HashMap<String, Integer> regionMap = new HashMap<>();
    private HashMap<String, Integer> distrMap = new HashMap<>();
    private HashMap<String, Integer> mlkaMap = new HashMap<>();

    private Date dDateFrom;
    private Timestamp dDateTo;
    private String sDateFrom;
    private String sDateTo;
    private String netSelected;
    private String regionName;
    private String oblName;
    private String mlkaName;
    private String netQ;


    public JdbcModelNka() {
        checkConnection();
    }

    @Override
    public List<String> getDatasForFirstQuery(String net, LocalDate dateFrom, LocalDate dateTo) throws SQLException {
        regionMap.clear();
        dDateFrom = Date.valueOf(dateFrom);
        dDateTo = Timestamp.valueOf(dateTo.atTime(23, 59, 59));
        sDateFrom = DateUtil.format(dateFrom);
        sDateTo = DateUtil.format(dateTo);
        netSelected = net;
        if (net.equals("Тандер")) {
            netQ = "AND ccd.client_name LIKE \'%тандер%\'";
        } else if (net.equals("X5")) {
            netQ = "AND (ccd.client_name LIKE '%x5%' OR ccd.client_name LIKE '%х5%')";
        }
        PreparedStatement statement = connection.prepareStatement(
                "SELECT DISTINCT r.region_name, r.region_id2 " +
                        "FROM report_foto rf " +
                        "INNER JOIN report_foto_tag ON rf.id = report_foto_tag.foto_id " +
                        "INNER JOIN client_card ccd ON ccd.client_id = rf.client_id " +
                        "INNER JOIN co_contractor c ON c.id = ccd.contractor_id " +
                        "INNER JOIN region2 r ON r.region_id2 = rf.region_id2 " +
                        "WHERE report_foto_tag.tag_id = 23 " +
                        "AND ccd.factory_id = 2 " +
                        netQ +
                        "AND rf.date_foto BETWEEN ? AND ? " +
                        "AND c.active = 1 " +
                        "AND ccd.visible = 0 " +
                        "ORDER BY r.region_name ");
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
        distrMap.clear();
        regionName = region;
        PreparedStatement statement = connection.prepareStatement("SELECT DISTINCT ccac.city, c.name, c.id " +
                "FROM report_foto rf " +
                "INNER JOIN report_foto_tag ON rf.id = report_foto_tag.foto_id " +
                "INNER JOIN client_card ccd ON ccd.client_id = rf.client_id " +
                "INNER JOIN co_contractor c ON c.id = ccd.contractor_id " +
                "INNER JOIN co_contractor_attr_customer ccac ON ccac.contractor_id = c.id " +
                "INNER JOIN region2 r ON r.region_id2 = rf.region_id2 " +
                "WHERE report_foto_tag.tag_id = 23 " +
                "AND ccd.factory_id = 2 " +
                netQ +
                "AND rf.date_foto BETWEEN ? AND ? " +
                "AND c.active = 1 " +
                "AND ccd.visible = 0 " +
                "AND r.region_id2 = ? ");
        statement.setQueryTimeout(QUERY_TIMEOUT);
        statement.setDate(1, dDateFrom);
        statement.setTimestamp(2, dDateTo);
        statement.setInt(3, regionMap.get(regionName));
        ResultSet resultSet = statement.executeQuery();
        ArrayList<String> result = new ArrayList<>();
        while (resultSet.next()) {
            String obl = resultSet.getString("city");
            if (obl.startsWith("Москва") || obl.startsWith("Санкт")) {
                result.add(obl.substring(0, obl.indexOf("(") - 1) + " (" + resultSet.getString("name").trim() + ")");
            } else if ((obl.length() - obl.replaceAll(",", "").length()) == 2) {
                result.add(obl.substring(obl.indexOf(",") + 2, obl.lastIndexOf(",")) + " (" + resultSet.getString("name").trim() + ")");
            } else {
                result.add(obl.substring(obl.indexOf("(") + 1, obl.indexOf(",")) + " (" + resultSet.getString("name").trim() + ")");
            }
            distrMap.put(resultSet.getString("name").trim(), resultSet.getInt("id"));
        }
        Collections.sort(result);
        return result;
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
        mlkaMap.clear();
        oblName = obl;
        PreparedStatement statement = connection.prepareStatement("SELECT DISTINCT (ISNULL(per.surname,'') + ' ' + ISNULL(per.name,'') + ' ' + ISNULL(per.patronymic,'')) AS fio, e.id\n" +
                "FROM report_foto f\n" +
                "INNER JOIN report_foto_tag tag ON tag.foto_id = f.id " +
                "INNER JOIN client_card ccd ON ccd.client_id = f.client_id " +
                "INNER JOIN co_contractor c ON c.id = ccd.contractor_id " +
                "INNER JOIN region2 r ON r.region_id2 = f.region_id2 " +
                "LEFT JOIN co_employee_sector es ON ccd.sector_id=es.sector_id " +
                "LEFT JOIN employee e ON es.employee_id=e.id and e.del<>1 " +
                "LEFT JOIN co_person per ON e.person_id=per.id " +
                "WHERE tag.tag_id = 23 " +
                "AND ccd.factory_id = 2 " +
                netQ +
                "AND f.date_foto BETWEEN ? AND ? " +
                "AND c.active = 1 " +
                "AND ccd.visible = 0 " +
                "AND r.region_id2 = ? " +
                "AND c.id = ? " +
                "ORDER BY fio");
        statement.setQueryTimeout(QUERY_TIMEOUT);
        statement.setDate(1, dDateFrom);
        statement.setTimestamp(2, dDateTo);
        statement.setInt(3, regionMap.get(regionName));
        statement.setInt(4, distrMap.get(oblName.substring(oblName.indexOf("(") + 1, oblName.length() - 1)));
        ResultSet resultSet = statement.executeQuery();
        ArrayList result = new ArrayList();
        while (resultSet.next()) {
            result.add(resultSet.getString("fio").trim());
            mlkaMap.put(resultSet.getString("fio").trim(), resultSet.getInt("id"));
        }
        return result;
    }

    @Override
    public List<String> getNkaList(String nkaName) throws SQLException {
        /* Doesn't use here */
        return null;
    }

    @Override
    public List<NkaAddress> getAddresses(String regionName, String nothing1) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT r.region_name, ccac.city, c.name, c.id,\n" +
                "(ISNULL(per.surname,'') + ' ' + ISNULL(per.name,'') + ' ' + ISNULL(per.patronymic,'')) AS fio, e.id,\n" +
                "ccd.client_id, ccd.client_name, ccd.client_adress, f.date_foto, f.date_add, f.src, tt.type, f.comment\n" +
                "FROM report_foto f\n" +
                "INNER JOIN report_foto_tag tag ON tag.foto_id = f.id\n" +
                "INNER JOIN client_card ccd ON ccd.client_id = f.client_id\n" +
                "INNER JOIN co_contractor c ON c.id = ccd.contractor_id\n" +
                "INNER JOIN co_contractor_attr_customer ccac ON ccac.contractor_id = c.id\n" +
                "INNER JOIN region2 r ON r.region_id2 = f.region_id2\n" +
                "LEFT JOIN co_employee_sector es ON ccd.sector_id=es.sector_id\n" +
                "LEFT JOIN employee e ON es.employee_id=e.id and e.del<>1\n" +
                "LEFT JOIN co_person per ON e.person_id=per.id\n" +
                "LEFT JOIN client_card_type as tt on ccd.type_id=tt.type_id and tt.factory_id=2\n" +
                "WHERE tag.tag_id = 23\n" +
                "AND ccd.factory_id = 2\n" +
                netQ +
                "AND f.date_foto BETWEEN ? AND ? \n" +
                "AND c.active = 1\n" +
                "AND ccd.visible = 0\n" +
                "AND r.region_id2 = ?\n" +
                "ORDER BY 1, 2, 4, 6, 7");
        statement.setQueryTimeout(QUERY_TIMEOUT);
        statement.setDate(1, dDateFrom);
        statement.setTimestamp(2, dDateTo);
        statement.setInt(3, regionMap.get(regionName));
        ResultSet resultSet = statement.executeQuery();
        return fillUpAddressesList(resultSet);
    }

    /*@Override
    public List<NkaAddress> getAddresses(String mlka, String nothing) throws SQLException {
        mlkaName = mlka;
        PreparedStatement statement = connection.prepareStatement("SELECT ccd.client_id, ccd.client_adress, f.date_foto, f.date_add, f.src, tt.type, f.comment " +
                "FROM report_foto f " +
                "INNER JOIN report_foto_tag tag ON tag.foto_id = f.id " +
                "INNER JOIN client_card ccd ON ccd.client_id = f.client_id " +
                "INNER JOIN co_contractor c ON c.id = ccd.contractor_id " +
                "INNER JOIN region2 r ON r.region_id2 = f.region_id2 " +
                "LEFT JOIN co_employee_sector es ON ccd.sector_id=es.sector_id " +
                "LEFT JOIN employee e ON es.employee_id=e.id and e.del<>1 " +
                "LEFT JOIN co_person per ON e.person_id=per.id " +
                "LEFT JOIN client_card_type as tt on ccd.type_id=tt.type_id and tt.factory_id=2 " +
                "WHERE tag.tag_id = 23 " +
                "AND ccd.factory_id = 2 " +
                netQ +
                "AND f.date_foto BETWEEN ? AND ? " +
                "AND c.active = 1 " +
                "AND ccd.visible = 0 " +
                "AND r.region_id2 = ? " +
                "AND c.id = ? " +
                "AND e.id = ? " +
                "order by 1, 2");
        statement.setQueryTimeout(QUERY_TIMEOUT);
        statement.setDate(1, dDateFrom);
        statement.setTimestamp(2, dDateTo);
        statement.setInt(3, regionMap.get(regionName));
        statement.setInt(4, distrMap.get(oblName.substring(oblName.indexOf("(") + 1, oblName.length() - 1)));
        statement.setInt(5, mlkaMap.get(mlkaName));
        ResultSet resultSet = statement.executeQuery();
        return fillUpAddressesList(resultSet);
    }*/

    private List<NkaAddress> fillUpAddressesList(ResultSet resultSet) throws SQLException {
        int id = 0;
        String name = null;
        String address = null;
        String region = null;
        String obl = null;
        String mlka = null;
        String typeName = null;
        ArrayList<Photo> photoList = new ArrayList<>();
        List<NkaAddress> addressesList = new ArrayList<>();
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
                NkaAddress nkaAddress = new NkaAddress(id, name, region, obl, mlka, address,
                        (ArrayList) photoList.clone(), new ArrayList<>(), "0", typeName,
                        false, false, false, false, false,
                        false, false, false, false, false,
                        false, false, false, false, false,
                        false, null, null);
                addressesList.add(nkaAddress);
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
            mlka = resultSet.getString("fio") == null ? "" : resultSet.getString("fio").trim();
            typeName = resultSet.getString("type") == null ? "" : resultSet.getString("type").trim();
        }

        NkaAddress nkaAddress = new NkaAddress(id, name, region, obl, mlka, address,
                (ArrayList) photoList.clone(), new ArrayList<>(), "0", typeName,
                false, false, false, false, false,
                false, false, false, false, false,
                false, false, false, false, false,
                false, null, LocalDate.now());
        addressesList.add(nkaAddress);

        photoList.clear();
        return addressesList;
    }
}
