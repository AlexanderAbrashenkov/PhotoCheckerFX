package foto_verif.view.dmp;

import foto_verif.model.Photo;
import foto_verif.util.LocalDateAdapter;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by market6 on 21.02.2017.
 */
public class DmpAddress {
    private int id;
    private StringProperty name = new SimpleStringProperty();
    private String region;
    private String obl;
    private String channel;
    private String net;
    private StringProperty address = new SimpleStringProperty();
    private List<Photo> photoList = new ArrayList<>();
    private List<DmpDescr> dmpDescrList = new ArrayList<>();
    private StringProperty checked = new SimpleStringProperty();
    private String typeName;
    private LocalDate savedDate;

    public DmpAddress() {
    }

    public DmpAddress(int id, String name, String region, String obl, String channel, String net, String address,
                      List<Photo> photoList, List<DmpDescr> DmpDescrList, String checked, String typeName, LocalDate savedDate) {
        this.id = id;
        this.name.set(name);
        this.region = region;
        this.obl = obl;
        this.channel = channel;
        this.net = net;
        this.address.set(address);
        this.photoList = photoList;
        this.dmpDescrList = dmpDescrList;
        this.checked.set(checked);
        this.typeName = typeName;
        this.savedDate = savedDate;
    }

    @XmlElement
    public int getId() {
        return id;
    }

    @XmlElement
    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    @XmlElement
    public String getRegion() {
        return region;
    }

    @XmlElement
    public String getObl() {
        return obl;
    }

    @XmlElement
    public String getChannel() {
        return channel;
    }

    @XmlElement
    public String getNet() {
        return net;
    }

    @XmlElement
    public String getAddress() {
        return address.get();
    }

    public StringProperty addressProperty() {
        return address;
    }

    @XmlElementWrapper(name = "photoList")
    @XmlElement(name = "photo")
    public List<Photo> getPhotoList() {
        return photoList;
    }

    @XmlElementWrapper(name = "dmpDescrList")
    @XmlElement(name = "dmpDescr")
    public List<DmpDescr> getDmpDescrList() {
        return dmpDescrList;
    }

    @XmlElement
    public String getChecked() {
        return checked.get();
    }

    public StringProperty checkedProperty() {
        return checked;
    }

    @XmlElement
    public String getTypeName() {
        return typeName;
    }

    @XmlElement
    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    public LocalDate getSavedDate() {
        return savedDate;
    };

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setObl(String obl) {
        this.obl = obl;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public void setNet(String net) {
        this.net = net;
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public void setPhotoList(List<Photo> photoList) {
        this.photoList = photoList;
    }

    public void setDmpDescrList(List<DmpDescr> dmpDescrList) {
        this.dmpDescrList = dmpDescrList;
    }

    public void setChecked(String checked) {
        this.checked.set(checked);
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public void setSavedDate(LocalDate savedDate) {
        this.savedDate = savedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DmpAddress that = (DmpAddress) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "DmpAddress{" +
                "id=" + id +
                ", name=" + name +
                ", region='" + region + '\'' +
                ", obl='" + obl + '\'' +
                ", channel='" + channel + '\'' +
                ", net='" + net + '\'' +
                ", address=" + address +
                ", photoList=" + photoList +
                ", dmpDescrList=" + dmpDescrList +
                ", checked=" + checked +
                ", typeName='" + typeName + '\'' +
                ", savedDate=" + savedDate +
                '}';
    }
}
