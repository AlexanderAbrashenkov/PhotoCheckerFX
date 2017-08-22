package foto_verif.model.jdbc;

import foto_verif.model.AddressTT;
import foto_verif.model.DmpAddressTT;
import foto_verif.view.dmp.DmpDescr;
import foto_verif.model.Photo;
import foto_verif.util.DateUtil;
import foto_verif.util.Logger;

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
import java.util.HashMap;
import java.util.List;

/**
 * Created by market6 on 10.01.2017.
 */
public class JdbcModelDmpNka extends AbstractJdbcModel implements JdbcModel {

    private HashMap<String, Integer> rjkamMap = new HashMap<>();
    private HashMap<String, Integer> nkaMap = new HashMap<>();

    private Date dDateFrom;
    private Timestamp dDateTo;
    private String sDateFrom;
    private String sDateTo;

    private String rjkamName;
    private String nkaName;

    public JdbcModelDmpNka() {
        checkConnection();
    }

    @Override
    public List<String> getDatasForFirstQuery(String net, LocalDate dateFrom, LocalDate dateTo) throws SQLException {
        rjkamMap.clear();
        dDateFrom = Date.valueOf(dateFrom);
        dDateTo = Timestamp.valueOf(dateTo.atTime(23, 59, 59));
        sDateFrom = DateUtil.format(dateFrom);
        sDateTo = DateUtil.format(dateTo);

        PreparedStatement statement = connection.prepareStatement(
            "SELECT DISTINCT (ISNULL(per.surname,'') + ' ' + ISNULL(per.name,'') + ' ' + ISNULL(per.patronymic,'')) AS fio, e.id " +
            "FROM report_foto rf " +
            "INNER JOIN report_foto_tag ON rf.id = report_foto_tag.foto_id " +
            "INNER JOIN client_card ccd ON ccd.client_id = rf.client_id " +
            "LEFT JOIN co_contractor c ON c.id = ccd.contractor_id " +
            "LEFT JOIN co_contractor_attr_customer ccac ON ccac.contractor_id = c.id " +
            "LEFT JOIN region2 r ON r.region_id2 = rf.region_id2 " +
            "LEFT JOIN employee e ON rf.employee_id=e.id " +
            "LEFT JOIN co_person per ON e.person_id=per.id " +
            "WHERE report_foto_tag.tag_id = 22 " +
            "AND ccd.factory_id = 2 " +
            "AND rf.date_foto BETWEEN ? AND ? " +
            "AND r.region_id2 = 12 " +
            "AND c.active = 1 " +
            "ORDER BY 1 ");
        statement.setQueryTimeout(QUERY_TIMEOUT);
        statement.setDate(1, dDateFrom);
        statement.setTimestamp(2, dDateTo);
        ResultSet resultSet = statement.executeQuery();
        ArrayList<String> result = new ArrayList<>();
        while (resultSet.next()) {
            rjkamMap.put(resultSet.getString("fio").trim(), resultSet.getInt("id"));
            result.add(resultSet.getString("fio").trim());
        }
        return result;
    }

    @Override
    public List<String> getOblsList(String region) throws SQLException {
        /*NONE*/
        return null;
    }

    @Override
    public List<String> getChannelList(String obl) throws SQLException {
        /*NONE*/
        return null;
    }

    @Override
    public List<String> getNetList() throws SQLException {
        /*NONE*/
        return null;
    }

    @Override
    public List<String> getMlkaList(String obl) throws SQLException {
        /*NONE*/
        return null;
    }

    @Override
    public List<String> getNkaList(String rjkam) throws SQLException {
        nkaMap.clear();
        rjkamName = rjkam;
        PreparedStatement statement = connection.prepareStatement(
            "SELECT DISTINCT sn.id, sn.name " +
            "FROM report_foto rf " +
            "INNER JOIN report_foto_tag ON rf.id = report_foto_tag.foto_id " +
            "INNER JOIN client_card ccd ON ccd.client_id = rf.client_id " +
            "LEFT JOIN co_contractor c ON c.id = ccd.contractor_id " +
            "LEFT JOIN co_contractor_attr_customer ccac ON ccac.contractor_id = c.id " +
            "LEFT JOIN region2 r ON r.region_id2 = rf.region_id2 " +
            "LEFT JOIN employee e ON rf.employee_id=e.id " +
            "LEFT JOIN co_person per ON e.person_id=per.id " +
            "LEFT JOIN sam_network sn on sn.id = ccd.tnet_id " +
            "WHERE report_foto_tag.tag_id = 22 " +
            "AND ccd.factory_id = 2 " +
            "AND rf.date_foto BETWEEN ? AND ? " +
            "AND r.region_id2 = 12 " +
            "AND c.active = 1 " +
            "AND e.id = ? " +
            "ORDER BY 1 ");
        statement.setQueryTimeout(QUERY_TIMEOUT);
        statement.setDate(1, dDateFrom);
        statement.setTimestamp(2, dDateTo);
        statement.setInt(3, rjkamMap.get(rjkamName));
        ResultSet resultSet = statement.executeQuery();
        ArrayList result = new ArrayList();
        while (resultSet.next()) {
            String nkaName = "";
            try {
                nkaName = resultSet.getString("name").trim();
                nkaMap.put(nkaName, resultSet.getInt("id"));
            } catch (NullPointerException e) {
                if (resultSet.wasNull()) nkaName = "Без привязки к сети";
                nkaMap.put(nkaName, 0);
            }
            result.add(nkaName);
        }
        return result;
    }

    @Override
    public List<AddressTT> getAddresses(String nka, String nothing) throws SQLException {
        nkaName = nka;
        String nkaQueryPart = nka.equals("Без привязки к сети") ? "AND sn.id IS NULL " : "AND sn.id = " + nkaMap.get(nkaName) + " ";
        PreparedStatement statement = connection.prepareStatement(
            "SELECT ccd.client_id, ccd.client_name, ccd.client_adress, rf.date_foto, rf.date_add, rf.src, tt.type, rf.comment " +
            "FROM report_foto rf " +
            "INNER JOIN report_foto_tag ON rf.id = report_foto_tag.foto_id " +
            "INNER JOIN client_card ccd ON ccd.client_id = rf.client_id " +
            "LEFT JOIN co_contractor c ON c.id = ccd.contractor_id " +
            "LEFT JOIN co_contractor_attr_customer ccac ON ccac.contractor_id = c.id " +
            "LEFT JOIN region2 r ON r.region_id2 = rf.region_id2 " +
            "LEFT JOIN employee e ON rf.employee_id=e.id " +
            "LEFT JOIN co_person per ON e.person_id=per.id " +
            "LEFT JOIN client_card_type as tt on ccd.type_id=tt.type_id and tt.factory_id=2 " +
            "LEFT JOIN sam_network sn on sn.id = ccd.tnet_id " +
            "WHERE report_foto_tag.tag_id = 22 " +
            "AND ccd.factory_id = 2 " +
            "AND rf.date_foto BETWEEN ? AND ? " +
            "AND r.region_id2 = 12 " +
            "AND c.active = 1 " +
            nkaQueryPart +
            "AND e.id = ? " +
            "ORDER BY 1");
        statement.setQueryTimeout(QUERY_TIMEOUT);
        statement.setDate(1, dDateFrom);
        statement.setTimestamp(2, dDateTo);
        statement.setInt(3, rjkamMap.get(rjkamName));
        ResultSet resultSet = statement.executeQuery();
        return fillUpAddressesList(resultSet);
    }

    private List<AddressTT> fillUpAddressesList(ResultSet resultSet) throws SQLException {
        int id = 0;
        String name = null;
        String address = null;
        String typeName = null;
        String comment = null;
        ArrayList<Photo> photoList = new ArrayList<>();
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
                String path;

                String tmpRjkamName = "";
                if (rjkamName != null) {
                    tmpRjkamName = rjkamName.endsWith(".") ? rjkamName.substring(0, rjkamName.length() - 1) : rjkamName;
                }

                path = ("save/2/" + sDateFrom + "-" + sDateTo + "/" + tmpRjkamName + "/" +
                        nkaName).replace(" ", "_").replace("\"", "") + "/" + id + ".adr";

                String allPhotosChecked = "0";
                // смотрим, проверяли ли уже эту фотку
                if (new File(path).exists()) {
                    try {
                        BufferedReader reader = new BufferedReader(new FileReader(path));
                        JAXBContext context = JAXBContext.newInstance(DmpAddressTT.class, Photo.class, DmpDescr.class);
                        Unmarshaller unmarshaller = context.createUnmarshaller();
                        DmpAddressTT dmpAddressTT = (DmpAddressTT) unmarshaller.unmarshal(reader);
                        reader.close();

                        for (Photo photo : photoList) {
                            if (dmpAddressTT.getPhotoList().contains(photo)) {
                                photo.setChecked(true);
                            }
                        }

                        if (dmpAddressTT.getPhotoList().size() == photoList.size()) {
                            if (photoList.containsAll(dmpAddressTT.getPhotoList())) {
                                allPhotosChecked = "1";
                                main.getChannelLayoutController().getCheckedRows().add(main.getChannelLayoutController().getAddresses().size());
                            }
                        }
                    } catch (IOException | JAXBException e) {
                        Logger.log(e.getMessage());
                        e.printStackTrace();
                    }
                }
                main.getChannelLayoutController().getAddresses().add(new AddressTT(id, name, address, (ArrayList) photoList.clone(), allPhotosChecked, typeName));
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
            typeName = resultSet.getString("type");
        }
        String path;

        String tmpRjkamName = "";
        if (rjkamName != null) {
            tmpRjkamName = rjkamName.endsWith(".") ? rjkamName.substring(0, rjkamName.length() - 1) : rjkamName;
        }

        path = ("save/2/" + sDateFrom + "-" + sDateTo + "/" + tmpRjkamName + "/" +
                nkaName).replace(" ", "_").replace("\"", "") + "/" + id + ".adr";

        String allPhotosChecked = "0";
        // смотрим, проверяли ли уже эту фотку
        if (new File(path).exists()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(path));
                JAXBContext context = JAXBContext.newInstance(DmpAddressTT.class, Photo.class, DmpDescr.class);
                Unmarshaller unmarshaller = context.createUnmarshaller();
                DmpAddressTT dmpAddressTT = (DmpAddressTT) unmarshaller.unmarshal(reader);
                reader.close();

                for (Photo photo : photoList) {
                    if (dmpAddressTT.getPhotoList().contains(photo)) {
                        photo.setChecked(true);
                    }
                }

                if (dmpAddressTT.getPhotoList().size() == photoList.size()) {
                    if (photoList.containsAll(dmpAddressTT.getPhotoList())) {
                        allPhotosChecked = "1";
                        main.getChannelLayoutController().getCheckedRows().add(main.getChannelLayoutController().getAddresses().size());
                    }
                }
            } catch (IOException | JAXBException e) {
                Logger.log(e.getMessage());
                e.printStackTrace();
            }
        }
        main.getChannelLayoutController().getAddresses().add(new AddressTT(id, name, address, (ArrayList) photoList.clone(), allPhotosChecked, typeName));
        photoList.clear();
        return null;
    }
}
