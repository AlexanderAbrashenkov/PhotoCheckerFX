package foto_verif.util;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDate;

/**
 * Created by market6 on 23.11.2016.
 */
public class LocalDateAdapter extends XmlAdapter<String, LocalDate> {
    public LocalDate unmarshal(String v) throws Exception {
        return DateUtil.parse(v);
    }

    public String marshal(LocalDate v) throws Exception {
        return DateUtil.format(v);
    }
}
