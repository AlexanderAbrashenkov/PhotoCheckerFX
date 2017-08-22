package foto_verif.view.NKA;

import foto_verif.model.AddressTT;
import foto_verif.model.Photo;
import foto_verif.model.TMAActivity;
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
 * Created by market6 on 08.02.2017.
 */
public class NkaAddress {
    private int id;
    private String name;
    private String region;
    private String obl;
    private String mlka;
    private StringProperty address = new SimpleStringProperty();
    private List<Photo> photoList = new ArrayList<>();
    private List<TMAActivity> tmaActivityList = new ArrayList<>();
    private StringProperty checked = new SimpleStringProperty();
    private String typeName;
    private boolean havePhotoMZ;
    private boolean correctPhotoMZ;
    private boolean centerPhotoMZ;
    private boolean _30PhotoMZ;
    private boolean vertPhotoMZ;
    private boolean havePhotoK;
    private boolean correctPhotoK;
    private boolean centerPhotoK;
    private boolean _30PhotoK;
    private boolean vertPhotoK;
    private boolean havePhotoS;
    private boolean correctPhotoS;
    private boolean centerPhotoS;
    private boolean _30PhotoS;
    private boolean vertPhotoS;
    private boolean oos;
    private String comment;
    private LocalDate savedDate;

    public NkaAddress() {
    }

    public NkaAddress(int id, String name, String region, String obl, String mlka, String address,
                      List<Photo> photoList, List<TMAActivity> tmaActivityList, String checked, String typeName,
                      boolean havePhotoMZ, boolean correctPhotoMZ, boolean centerPhotoMZ, boolean _30PhotoMZ, boolean vertPhotoMZ,
                      boolean havePhotoK, boolean correctPhotoK, boolean centerPhotoK, boolean _30PhotoK, boolean vertPhotoK,
                      boolean havePhotoS, boolean correctPhotoS, boolean centerPhotoS, boolean _30PhotoS, boolean vertPhotoS,
                      boolean oos, String comment, LocalDate savedDate) {
        this.id = id;
        this.name = name;
        this.region = region;
        this.obl = obl;
        this.mlka = mlka;
        this.address.set(address);
        this.photoList = photoList;
        this.tmaActivityList = tmaActivityList;
        this.checked.set(checked);
        this.typeName = typeName;
        this.havePhotoMZ = havePhotoMZ;
        this.correctPhotoMZ = correctPhotoMZ;
        this.centerPhotoMZ = centerPhotoMZ;
        this._30PhotoMZ = _30PhotoMZ;
        this.vertPhotoMZ = vertPhotoMZ;
        this.havePhotoK = havePhotoK;
        this.correctPhotoK = correctPhotoK;
        this.centerPhotoK = centerPhotoK;
        this._30PhotoK = _30PhotoK;
        this.vertPhotoK = vertPhotoK;
        this.havePhotoS = havePhotoS;
        this.correctPhotoS = correctPhotoS;
        this.centerPhotoS = centerPhotoS;
        this._30PhotoS = _30PhotoS;
        this.vertPhotoS = vertPhotoS;
        this.oos = oos;
        this.comment = comment;
        this.savedDate = savedDate;
    }

    @XmlElement
    public int getId() {
        return id;
    }

    @XmlElement
    public String getName() {
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
    public String getMlka() {
        return mlka;
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

    @XmlElementWrapper(name = "tmaActivityList")
    @XmlElement(name = "tmaActivity")
    public List<TMAActivity> getTmaActivityList() {
        return tmaActivityList;
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
    public boolean isHavePhotoMZ() {
        return havePhotoMZ;
    }

    @XmlElement
    public boolean isCorrectPhotoMZ() {
        return correctPhotoMZ;
    }

    @XmlElement
    public boolean isCenterPhotoMZ() {
        return centerPhotoMZ;
    }

    @XmlElement
    public boolean is_30PhotoMZ() {
        return _30PhotoMZ;
    }

    @XmlElement
    public boolean isVertPhotoMZ() {
        return vertPhotoMZ;
    }

    @XmlElement
    public boolean isHavePhotoK() {
        return havePhotoK;
    }

    @XmlElement
    public boolean isCorrectPhotoK() {
        return correctPhotoK;
    }

    @XmlElement
    public boolean isCenterPhotoK() {
        return centerPhotoK;
    }

    @XmlElement
    public boolean is_30PhotoK() {
        return _30PhotoK;
    }

    @XmlElement
    public boolean isVertPhotoK() {
        return vertPhotoK;
    }

    @XmlElement
    public boolean isHavePhotoS() {
        return havePhotoS;
    }

    @XmlElement
    public boolean isCorrectPhotoS() {
        return correctPhotoS;
    }

    @XmlElement
    public boolean isCenterPhotoS() {
        return centerPhotoS;
    }

    @XmlElement
    public boolean is_30PhotoS() {
        return _30PhotoS;
    }

    @XmlElement
    public boolean isVertPhotoS() {
        return vertPhotoS;
    }

    @XmlElement
    public boolean isOos() {
        return oos;
    }

    @XmlElement
    public String getComment() {
        return comment;
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
        this.name = name;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setObl(String obl) {
        this.obl = obl;
    }

    public void setMlka(String mlka) {
        this.mlka = mlka;
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public void setPhotoList(List<Photo> photoList) {
        this.photoList = photoList;
    }

    public void setTmaActivityList(List<TMAActivity> tmaActivityList) {
        this.tmaActivityList = tmaActivityList;
    }

    public void setChecked(String checked) {
        this.checked.set(checked);
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public void setHavePhotoMZ(boolean havePhotoMZ) {
        this.havePhotoMZ = havePhotoMZ;
    }

    public void setCorrectPhotoMZ(boolean correctPhotoMZ) {
        this.correctPhotoMZ = correctPhotoMZ;
    }

    public void setCenterPhotoMZ(boolean centerPhotoMZ) {
        this.centerPhotoMZ = centerPhotoMZ;
    }

    public void set_30PhotoMZ(boolean _30PhotoMZ) {
        this._30PhotoMZ = _30PhotoMZ;
    }

    public void setVertPhotoMZ(boolean vertPhotoMZ) {
        this.vertPhotoMZ = vertPhotoMZ;
    }

    public void setHavePhotoK(boolean havePhotoK) {
        this.havePhotoK = havePhotoK;
    }

    public void setCorrectPhotoK(boolean correctPhotoK) {
        this.correctPhotoK = correctPhotoK;
    }

    public void setCenterPhotoK(boolean centerPhotoK) {
        this.centerPhotoK = centerPhotoK;
    }

    public void set_30PhotoK(boolean _30PhotoK) {
        this._30PhotoK = _30PhotoK;
    }

    public void setVertPhotoK(boolean vertPhotoK) {
        this.vertPhotoK = vertPhotoK;
    }

    public void setHavePhotoS(boolean havePhotoS) {
        this.havePhotoS = havePhotoS;
    }

    public void setCorrectPhotoS(boolean correctPhotoS) {
        this.correctPhotoS = correctPhotoS;
    }

    public void setCenterPhotoS(boolean centerPhotoS) {
        this.centerPhotoS = centerPhotoS;
    }

    public void set_30PhotoS(boolean _30PhotoS) {
        this._30PhotoS = _30PhotoS;
    }

    public void setVertPhotoS(boolean vertPhotoS) {
        this.vertPhotoS = vertPhotoS;
    }

    public void setOos(boolean oos) {
        this.oos = oos;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setSavedDate(LocalDate savedDate) {
        this.savedDate = savedDate;
    }

    public void setAllCriterias(boolean havePhotoMZ, boolean correctPhotoMZ, boolean centerPhotoMZ, boolean _30PhotoMZ, boolean vertPhotoMZ,
                                boolean havePhotoK, boolean correctPhotoK, boolean centerPhotoK, boolean _30PhotoK, boolean vertPhotoK,
                                boolean havePhotoS, boolean correctPhotoS, boolean centerPhotoS, boolean _30PhotoS, boolean vertPhotoS, boolean oos, String comment) {
        this.havePhotoMZ = havePhotoMZ;
        this.correctPhotoMZ = correctPhotoMZ;
        this.centerPhotoMZ = centerPhotoMZ;
        this._30PhotoMZ = _30PhotoMZ;
        this.vertPhotoMZ = vertPhotoMZ;
        this.havePhotoK = havePhotoK;
        this.correctPhotoK = correctPhotoK;
        this.centerPhotoK = centerPhotoK;
        this._30PhotoK = _30PhotoK;
        this.vertPhotoK = vertPhotoK;
        this.havePhotoS = havePhotoS;
        this.correctPhotoS = correctPhotoS;
        this.centerPhotoS = centerPhotoS;
        this._30PhotoS = _30PhotoS;
        this.vertPhotoS = vertPhotoS;
        this.oos = oos;
        this.comment = comment;
    }

    public void clearCriterias() {
        havePhotoMZ = false;
        correctPhotoMZ = false;
        centerPhotoMZ = false;
        _30PhotoMZ = false;
        vertPhotoMZ = false;
        havePhotoK = false;
        correctPhotoK = false;
        centerPhotoK = false;
        _30PhotoK = false;
        vertPhotoK = false;
        havePhotoS = false;
        correctPhotoS = false;
        centerPhotoS = false;
        _30PhotoS = false;
        vertPhotoS = false;
        oos = false;
        comment = "";
        savedDate = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NkaAddress that = (NkaAddress) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
