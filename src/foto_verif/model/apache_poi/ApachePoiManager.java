package foto_verif.model.apache_poi;

/**
 * Created by market6 on 13.01.2017.
 */
public class ApachePoiManager {
    private static ApachePoi apachePoi;

    public static void createApachePoi (int reportNumber) {
        switch (reportNumber) {
            case 0: apachePoi = new ApachePoiLkaDmp(); break;
            case 1: apachePoi = new ApachePoiNkaMlka(); break;
            case 2: apachePoi = new ApachePoiNkaDmp(); break;
            case 3: apachePoi = new ApachePoiNst(); break;
            case 4: apachePoi = new ApachePoiLka(); break;
        }
    }

    public static ApachePoi getInstance() {
        if (apachePoi != null) {
            return apachePoi;
        } else {
            return new ApachePoiNkaMlka();
        }
    }
}
