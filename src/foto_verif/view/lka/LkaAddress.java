package foto_verif.view.lka;

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
public class LkaAddress {
    private int id;
    private StringProperty name = new SimpleStringProperty();
    private String region;
    private String obl;
    private String lka;
    private StringProperty address = new SimpleStringProperty();
    private List<Photo> photoList = new ArrayList<>();
    private List<TMAActivity> tmaActivityList = new ArrayList<>();
    private StringProperty checked = new SimpleStringProperty();
    private String typeName;
    private boolean haveMz;
    private boolean havePhotoMZ;
    private boolean haveAdditionalProduct;
    private boolean correctPhotoMZ;
    private boolean param1MZ;
    private boolean param2MZ;
    private boolean haveK;
    private boolean havePhotoK;
    private boolean correctPhotoK;
    private boolean param1K;
    private boolean param2K;
    private boolean haveS;
    private boolean havePhotoS;
    private boolean correctPhotoS;
    private boolean param1S;
    private boolean param2S;
    private boolean oos;
    private String comment;
    private LocalDate savedDate;

    public LkaAddress() {
    }

    public LkaAddress(int id, String name, String region, String obl, String lka, String address,
                      List<Photo> photoList, List<TMAActivity> tmaActivityList, String checked, String typeName,
                      boolean haveMz, boolean havePhotoMZ, boolean haveAdditionalProduct, boolean correctPhotoMZ, boolean param1MZ, boolean param2MZ,
                      boolean haveK, boolean havePhotoK, boolean correctPhotoK, boolean param1K, boolean param2K,
                      boolean haveS, boolean havePhotoS, boolean correctPhotoS, boolean param1S, boolean param2S,
                      boolean oos, String comment, LocalDate savedDate) {
        this.id = id;
        this.name.set(name);
        this.region = region;
        this.obl = obl;
        this.lka = lka;
        this.address.set(address);
        this.photoList = photoList;
        this.tmaActivityList = tmaActivityList;
        this.checked.set(checked);
        this.typeName = typeName;
        this.haveMz = haveMz;
        this.havePhotoMZ = havePhotoMZ;
        this.haveAdditionalProduct = haveAdditionalProduct;
        this.correctPhotoMZ = correctPhotoMZ;
        this.param1MZ = param1MZ;
        this.param2MZ = param2MZ;
        this.haveK = haveK;
        this.havePhotoK = havePhotoK;
        this.correctPhotoK = correctPhotoK;
        this.param1K = param1K;
        this.param2K = param2K;
        this.haveS = haveS;
        this.havePhotoS = havePhotoS;
        this.correctPhotoS = correctPhotoS;
        this.param1S = param1S;
        this.param2S = param2S;
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
    public String getLka() {
        return lka;
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
    public boolean isHaveMz() {
        return haveMz;
    }

    @XmlElement
    public boolean isHavePhotoMZ() {
        return havePhotoMZ;
    }

    @XmlElement
    public boolean isHaveAdditionalProduct() {
        return haveAdditionalProduct;
    }

    @XmlElement
    public boolean isCorrectPhotoMZ() {
        return correctPhotoMZ;
    }

    @XmlElement
    public boolean isParam1MZ() {
        return param1MZ;
    }

    @XmlElement
    public boolean isParam2MZ() {
        return param2MZ;
    }

    @XmlElement
    public boolean isHaveK() {
        return haveK;
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
    public boolean isParam1K() {
        return param1K;
    }

    @XmlElement
    public boolean isParam2K() {
        return param2K;
    }

    @XmlElement
    public boolean isHaveS() {
        return haveS;
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
    public boolean isParam1S() {
        return param1S;
    }

    @XmlElement
    public boolean isParam2S() {
        return param2S;
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
        this.name.set(name);
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setObl(String obl) {
        this.obl = obl;
    }

    public void setLka(String lka) {
        this.lka = lka;
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

    public void setHaveMz(boolean haveMz) {
        this.haveMz = haveMz;
    }

    public void setHavePhotoMZ(boolean havePhotoMZ) {
        this.havePhotoMZ = havePhotoMZ;
    }

    public void setHaveAdditionalProduct(boolean haveAdditionalProduct) {
        this.haveAdditionalProduct = haveAdditionalProduct;
    }

    public void setCorrectPhotoMZ(boolean correctPhotoMZ) {
        this.correctPhotoMZ = correctPhotoMZ;
    }

    public void setParam1MZ(boolean param1MZ) {
        this.param1MZ = param1MZ;
    }

    public void setParam2MZ(boolean param2MZ) {
        this.param2MZ = param2MZ;
    }

    public void setHaveK(boolean haveK) {
        this.haveK = haveK;
    }

    public void setHavePhotoK(boolean havePhotoK) {
        this.havePhotoK = havePhotoK;
    }

    public void setCorrectPhotoK(boolean correctPhotoK) {
        this.correctPhotoK = correctPhotoK;
    }

    public void setParam1K(boolean param1K) {
        this.param1K = param1K;
    }

    public void setParam2K(boolean param2K) {
        this.param2K = param2K;
    }

    public void setHaveS(boolean haveS) {
        this.haveS = haveS;
    }

    public void setHavePhotoS(boolean havePhotoS) {
        this.havePhotoS = havePhotoS;
    }

    public void setCorrectPhotoS(boolean correctPhotoS) {
        this.correctPhotoS = correctPhotoS;
    }

    public void setParam1S(boolean param1S) {
        this.param1S = param1S;
    }

    public void setParam2S(boolean param2S) {
        this.param2S = param2S;
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

    public void setAllCriterias(boolean haveMz, boolean havePhotoMZ, boolean haveAdditionalProduct, boolean correctPhotoMZ, boolean param1MZ, boolean param2MZ,
                                boolean haveK, boolean havePhotoK, boolean correctPhotoK, boolean param1K, boolean param2K,
                                boolean haveS, boolean havePhotoS, boolean correctPhotoS, boolean param1S, boolean param2S, boolean oos, String comment) {
        this.haveMz = haveMz;
        this.havePhotoMZ = havePhotoMZ;
        this.haveAdditionalProduct = haveAdditionalProduct;
        this.correctPhotoMZ = correctPhotoMZ;
        this.param1MZ = param1MZ;
        this.param2MZ = param2MZ;
        this.haveK = haveK;
        this.havePhotoK = havePhotoK;
        this.correctPhotoK = correctPhotoK;
        this.param1K = param1K;
        this.param2K = param2K;
        this.haveS = haveS;
        this.havePhotoS = havePhotoS;
        this.correctPhotoS = correctPhotoS;
        this.param1S = param1S;
        this.param2S = param2S;
        this.oos = oos;
        this.comment = comment;
    }

    public void clearCriterias() {
        haveMz = false;
        havePhotoMZ = false;
        haveAdditionalProduct = false;
        correctPhotoMZ = false;
        param1MZ = false;
        param2MZ = false;
        haveK = false;
        havePhotoK = false;
        correctPhotoK = false;
        param1K = false;
        param2K = false;
        haveS = false;
        havePhotoS = false;
        correctPhotoS = false;
        param1S = false;
        param2S = false;
        oos = false;
        comment = "";
        savedDate = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LkaAddress that = (LkaAddress) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
