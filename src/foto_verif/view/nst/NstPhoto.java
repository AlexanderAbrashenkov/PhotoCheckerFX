package foto_verif.view.nst;

import foto_verif.util.LocalDateAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;

/**
 * Created by market6 on 18.01.2017.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class NstPhoto implements Comparable<NstPhoto> {

    private String title;
    private String obl;
    private String city;
    private String shop;
    private String url;
    private String fullUrl;
    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    private LocalDate date;
    private boolean isChecked;

    public NstPhoto() {
    }

    public NstPhoto(String title, String obl, String city, String shop, String url, String fullUrl, LocalDate date) {
        this.title = title;
        this.obl = obl;
        this.city = city;
        this.shop = shop;
        this.url = url;
        this.fullUrl = fullUrl;
        this.date = date;
        this.isChecked = false;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getObl() {
        return obl;
    }

    public void setObl(String obl) {
        this.obl = obl;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFullUrl() {
        return fullUrl;
    }

    public void setFullUrl(String fullUrl) {
        this.fullUrl = fullUrl;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public String toString() {
        return "NstPhoto{" +
                "title='" + title + '\'' +
                ", obl='" + obl + '\'' +
                ", city='" + city + '\'' +
                ", shop='" + shop + '\'' +
                ", url='" + url + '\'' +
                ", fullUrl='" + fullUrl + '\'' +
                ", date=" + date +
                ", isChecked=" + isChecked +
                '}';
    }

    @Override
    public int compareTo(NstPhoto o) {
        return date.compareTo(o.getDate());
    }
}
