package foto_verif.view.nst;

import foto_verif.model.TMAActivity;
import foto_verif.util.LocalDateAdapter;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by market6 on 18.01.2017.
 */

@XmlAccessorType(XmlAccessType.PROPERTY)
public class NstAddress {
    private String obl;
    private String city;
    private StringProperty name = new SimpleStringProperty();
    private boolean hasPhotos;
    private List<NstPhoto> photoList = new ArrayList<>();
    private List<TMAActivity> tmaActivityList = new ArrayList<>();
    private StringProperty checked = new SimpleStringProperty();
    private int visitCount;
    private boolean noMatrixMZ;
    private boolean havePhotoMZ;
    private boolean bordersPhotoMZ;
    private boolean centerPhotoMZ;
    private boolean _30PhotoMZ;
    private boolean vertPhotoMZ;
    private String commentMZ;
    private boolean noMatrixKS;
    private boolean havePhotoKS;
    private boolean bordersPhotoKS;
    private boolean centerPhotoKS;
    private boolean _30PhotoKS;
    private boolean vertPhotoKS;
    private String commentKS;
    private boolean noMatrixM;
    private boolean havePhotoM;
    private boolean bordersPhotoM;
    private boolean centerPhotoM;
    private boolean vertPhotoM;
    private String commentM;
    private LocalDate savedDate;
    private int shopFormat;

    public NstAddress() {
    }

    public NstAddress(String obl, String city, String name, boolean hasPhotos,
                      List<NstPhoto> photoList, List<TMAActivity> tmaActivityList, String checked, int visitCount,
                      boolean noMatrixMZ, boolean havePhotoMZ, boolean bordersPhotoMZ, boolean centerPhotoMZ, boolean _30PhotoMZ, boolean vertPhotoMZ, String commentMZ,
                      boolean noMatrixKS, boolean havePhotoKS, boolean bordersPhotoKS, boolean centerPhotoKS, boolean _30PhotoKS, boolean vertPhotoKS, String commentKS,
                      boolean noMatrixM, boolean havePhotoM, boolean bordersPhotoM, boolean centerPhotoM, boolean vertPhotoM, String commentM, LocalDate savedDate, int shopFormat) {
        this.obl = obl;
        this.city = city;
        this.name.set(name);
        this.hasPhotos = hasPhotos;
        this.photoList = photoList;
        this.tmaActivityList = tmaActivityList;
        this.checked.set(checked);
        this.visitCount = visitCount;
        this.noMatrixMZ = noMatrixMZ;
        this.havePhotoMZ = havePhotoMZ;
        this.bordersPhotoMZ = bordersPhotoMZ;
        this.centerPhotoMZ = centerPhotoMZ;
        this._30PhotoMZ = _30PhotoMZ;
        this.vertPhotoMZ = vertPhotoMZ;
        this.commentMZ = commentMZ;
        this.noMatrixKS = noMatrixKS;
        this.havePhotoKS = havePhotoKS;
        this.bordersPhotoKS = bordersPhotoKS;
        this.centerPhotoKS = centerPhotoKS;
        this._30PhotoKS = _30PhotoKS;
        this.vertPhotoKS = vertPhotoKS;
        this.commentKS = commentKS;
        this.noMatrixM = noMatrixM;
        this.havePhotoM = havePhotoM;
        this.bordersPhotoM = bordersPhotoM;
        this.centerPhotoM = centerPhotoM;
        this.vertPhotoM = vertPhotoM;
        this.commentM = commentM;
        this.savedDate = savedDate;
        this.shopFormat = shopFormat;
    }

    @XmlElement
    public String getObl() {
        return obl;
    }

    @XmlElement
    public String getCity() {
        return city;
    }

    @XmlElement
    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    @XmlElement
    public boolean isHasPhotos() {
        return hasPhotos;
    }

    @XmlElementWrapper(name = "photoList")
    @XmlElement(name = "photo")
    public List<NstPhoto> getPhotoList() {
        return photoList;
    }

    @XmlElementWrapper(name = "tmaActivityList")
    @XmlElement(name = "tmaActivity")
    public List<TMAActivity> getTmaActivityList() {
        return tmaActivityList;
    }

    public List<TMAActivity> tmaActivityListProperty() {
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
    public int getVisitCount() {
        return visitCount;
    }

    @XmlElement
    public boolean isNoMatrixMZ() {
        return noMatrixMZ;
    }

    @XmlElement
    public boolean isHavePhotoMZ() {
        return havePhotoMZ;
    }

    @XmlElement
    public boolean isBordersPhotoMZ() {
        return bordersPhotoMZ;
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
    public String getCommentMZ() {
        return commentMZ;
    }

    @XmlElement
    public boolean isNoMatrixKS() {
        return noMatrixKS;
    }

    @XmlElement
    public boolean isHavePhotoKS() {
        return havePhotoKS;
    }

    @XmlElement
    public boolean isBordersPhotoKS() {
        return bordersPhotoKS;
    }

    @XmlElement
    public boolean isCenterPhotoKS() {
        return centerPhotoKS;
    }

    @XmlElement
    public boolean is_30PhotoKS() {
        return _30PhotoKS;
    }

    @XmlElement
    public boolean isVertPhotoKS() {
        return vertPhotoKS;
    }

    @XmlElement
    public String getCommentKS() {
        return commentKS;
    }

    @XmlElement
    public boolean isNoMatrixM() {
        return noMatrixM;
    }

    @XmlElement
    public boolean isHavePhotoM() {
        return havePhotoM;
    }

    @XmlElement
    public boolean isBordersPhotoM() {
        return bordersPhotoM;
    }

    @XmlElement
    public boolean isCenterPhotoM() {
        return centerPhotoM;
    }

    @XmlElement
    public boolean isVertPhotoM() {
        return vertPhotoM;
    }

    @XmlElement
    public String getCommentM() {
        return commentM;
    }

    @XmlElement
    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    public LocalDate getSavedDate() {
        return savedDate;
    };

    @XmlElement
    public int getShopFormat() {
        return shopFormat;
    }

    public void setObl(String obl) {
        this.obl = obl;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setHasPhotos(boolean hasPhotos) {
        this.hasPhotos = hasPhotos;
    }

    public void setPhotoList(List<NstPhoto> photoList) {
        this.photoList = photoList;
    }

    public void setTmaActivityList(List<TMAActivity> tmaActivityList) {
        this.tmaActivityList = tmaActivityList;
    }

    public void setVisitCount(int visitCount) {
        this.visitCount = visitCount;
    }

    public void setNoMatrixMZ (boolean noMatrixMZ) {
        this.noMatrixMZ = noMatrixMZ;
    }

    public void setHavePhotoMZ(boolean havePhotoMZ) {
        this.havePhotoMZ = havePhotoMZ;
    }

    public void setBordersPhotoMZ(boolean bordersPhotoMZ) {
        this.bordersPhotoMZ = bordersPhotoMZ;
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

    public void setCommentMZ(String commentMZ) {
        this.commentMZ = commentMZ;
    }

    public void setNoMatrixKS (boolean noMatrixKS) {
        this.noMatrixKS = noMatrixKS;
    }

    public void setHavePhotoKS(boolean havePhotoKS) {
        this.havePhotoKS = havePhotoKS;
    }

    public void setBordersPhotoKS(boolean bordersPhotoKS) {
        this.bordersPhotoKS = bordersPhotoKS;
    }

    public void setCenterPhotoKS(boolean centerPhotoKS) {
        this.centerPhotoKS = centerPhotoKS;
    }

    public void set_30PhotoKS(boolean _30PhotoKS) {
        this._30PhotoKS = _30PhotoKS;
    }

    public void setVertPhotoKS(boolean vertPhotoKS) {
        this.vertPhotoKS = vertPhotoKS;
    }

    public void setCommentKS(String commentKS) {
        this.commentKS = commentKS;
    }

    public void setNoMatrixM(boolean noMatrixM) {
        this.noMatrixM = noMatrixM;
    }

    public void setHavePhotoM(boolean havePhotoM) {
        this.havePhotoM = havePhotoM;
    }

    public void setBordersPhotoM(boolean bordersPhotoM) {
        this.bordersPhotoM = bordersPhotoM;
    }

    public void setCenterPhotoM(boolean centerPhotoM) {
        this.centerPhotoM = centerPhotoM;
    }

    public void setVertPhotoM(boolean vertPhotoM) {
        this.vertPhotoM = vertPhotoM;
    }

    public void setCommentM(String commentM) {
        this.commentM = commentM;
    }

    public void setChecked(String checked) {
        this.checked.set(checked);
    }

    public void setSavedDate (LocalDate savedDate) {
        this.savedDate = savedDate;
    }

    public void setShopFormat(int shopFormat) {
        this.shopFormat = shopFormat;
    }

    public void setTmaActivityList(ArrayList<TMAActivity> tmaActivityList) {
        this.tmaActivityList = tmaActivityList;
    }

    public void setAllCriterias(int visitCount,
                                boolean noMatrixMZ, boolean havePhotoMZ, boolean bordersPhotoMZ, boolean centerPhotoMZ, boolean _30PhotoMZ, boolean vertPhotoMZ, String commentMZ,
                                boolean noMatrixKS, boolean havePhotoKS, boolean bordersPhotoKS, boolean centerPhotoKS, boolean _30PhotoKS, boolean vertPhotoKS, String commentKS,
                                boolean noMatrixM, boolean havePhotoM, boolean bordersPhotoM, boolean centerPhotoM, boolean vertPhotoM, String commentM) {
        this.visitCount = visitCount;
        this.noMatrixMZ = noMatrixMZ;
        this.havePhotoMZ = havePhotoMZ;
        this.bordersPhotoMZ = bordersPhotoMZ;
        this.centerPhotoMZ = centerPhotoMZ;
        this._30PhotoMZ = _30PhotoMZ;
        this.vertPhotoMZ = vertPhotoMZ;
        this.commentMZ = commentMZ;
        this.noMatrixKS = noMatrixKS;
        this.havePhotoKS = havePhotoKS;
        this.bordersPhotoKS = bordersPhotoKS;
        this.centerPhotoKS = centerPhotoKS;
        this._30PhotoKS = _30PhotoKS;
        this.vertPhotoKS = vertPhotoKS;
        this.commentKS = commentKS;
        this.noMatrixM = noMatrixM;
        this.havePhotoM = havePhotoM;
        this.bordersPhotoM = bordersPhotoM;
        this.centerPhotoM = centerPhotoM;
        this.vertPhotoM = vertPhotoM;
        this.commentM = commentM;
    }

    @Override
    public String toString() {
        return "NstAddress{" +
                "obl='" + obl + '\'' +
                ", city='" + city + '\'' +
                ", name=" + name +
                ", hasPhotos=" + hasPhotos +
                ", photoList=" + photoList +
                ", tmaActivityList=" + tmaActivityList +
                ", checked=" + checked +
                ", visitCount=" + visitCount +
                ", noMatrixMZ=" + noMatrixMZ +
                ", havePhotoMZ=" + havePhotoMZ +
                ", bordersPhotoMZ=" + bordersPhotoMZ +
                ", centerPhotoMZ=" + centerPhotoMZ +
                ", _30PhotoMZ=" + _30PhotoMZ +
                ", vertPhotoMZ=" + vertPhotoMZ +
                ", commentMZ='" + commentMZ + '\'' +
                ", noMatrixKS=" + noMatrixKS +
                ", havePhotoKS=" + havePhotoKS +
                ", bordersPhotoKS=" + bordersPhotoKS +
                ", centerPhotoKS=" + centerPhotoKS +
                ", _30PhotoKS=" + _30PhotoKS +
                ", vertPhotoKS=" + vertPhotoKS +
                ", commentKS='" + commentKS + '\'' +
                ", noMatrixM=" + noMatrixM +
                ", havePhotoM=" + havePhotoM +
                ", bordersPhotoM=" + bordersPhotoM +
                ", centerPhotoM=" + centerPhotoM +
                ", vertPhotoM=" + vertPhotoM +
                ", commentM='" + commentM + '\'' +
                ", savedDate=" + savedDate +
                ", shopFormat=" + shopFormat +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NstAddress)) return false;

        NstAddress that = (NstAddress) o;

        if (!obl.equals(that.obl)) return false;
        //if (!city.equals(that.city)) return false;
        return name.equals(that.name);

    }

    @Override
    public int hashCode() {
        int result = obl.hashCode();
        result = 31 * result + city.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }
}
