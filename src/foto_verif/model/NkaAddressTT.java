package foto_verif.model;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;

/**
 * Created by market6 on 23.11.2016.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class NkaAddressTT {
    private int id;
    private String name;
    private String address;
    @XmlElementWrapper(name = "photo-list")
    @XmlElement(name = "photo")
    private ArrayList<Photo> photoList;
    private ArrayList<TMAActivity> tmaActivityList;
    private String checked;
    private String typeName;
    private boolean noPhotoMZ;
    private boolean incorrectPhotoMZ;
    private boolean centerPhotoMZ;
    private boolean _30PhotoMZ;
    private boolean vertPhotoMZ;
    private boolean noPhotoK;
    private boolean incorrectPhotoK;
    private boolean centerPhotoK;
    private boolean _30PhotoK;
    private boolean vertPhotoK;
    private boolean noPhotoS;
    private boolean incorrectPhotoS;
    private boolean centerPhotoS;
    private boolean _30PhotoS;
    private boolean vertPhotoS;
    private boolean oos;
    private String comment;

    public NkaAddressTT() {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public ArrayList<Photo> getPhotoList() {
        return photoList;
    }

    public ArrayList<TMAActivity> getTmaActivityList() {
        return tmaActivityList;
    }

    public String getChecked() {
        return checked;
    }

    public String getTypeName() {
        return typeName;
    }

    public boolean isNoPhotoMZ() {
        return noPhotoMZ;
    }

    public boolean isIncorrectPhotoMZ() {
        return incorrectPhotoMZ;
    }

    public boolean isCenterPhotoMZ() {
        return centerPhotoMZ;
    }

    public boolean is_30PhotoMZ() {
        return _30PhotoMZ;
    }

    public boolean isVertPhotoMZ() {
        return vertPhotoMZ;
    }

    public boolean isNoPhotoK() {
        return noPhotoK;
    }

    public boolean isIncorrectPhotoK() {
        return incorrectPhotoK;
    }

    public boolean isCenterPhotoK() {
        return centerPhotoK;
    }

    public boolean is_30PhotoK() {
        return _30PhotoK;
    }

    public boolean isVertPhotoK() {
        return vertPhotoK;
    }

    public boolean isNoPhotoS() {
        return noPhotoS;
    }

    public boolean isIncorrectPhotoS() {
        return incorrectPhotoS;
    }

    public boolean isCenterPhotoS() {
        return centerPhotoS;
    }

    public boolean is_30PhotoS() {
        return _30PhotoS;
    }

    public boolean isVertPhotoS() {
        return vertPhotoS;
    }

    public boolean isOos() {
        return oos;
    }

    public String getComment() {
        return comment;
    }

    public void setTmaActivityList(ArrayList<TMAActivity> tmaActivityList) {
        this.tmaActivityList = tmaActivityList;
    }

    public void copyFromAddressTT(AddressTT addressTT) {
        this.id = addressTT.getId();
        this.name = addressTT.getName();
        this.address = addressTT.getAddress();
        this.photoList = addressTT.getPhotoList();
        this.checked = addressTT.getChecked();
        this.typeName = addressTT.getTypeName();
    }

    public void setAllCriterias(boolean noPhotoMZ, boolean incorrectPhotoMZ, boolean centerPhotoMZ, boolean _30PhotoMZ, boolean vertPhotoMZ,
                                boolean noPhotoK, boolean incorrectPhotoK, boolean centerPhotoK, boolean _30PhotoK, boolean vertPhotoK,
                                boolean noPhotoS, boolean incorrectPhotoS, boolean centerPhotoS, boolean _30PhotoS, boolean vertPhotoS,
                                boolean oos, String comment) {
        this.noPhotoMZ = noPhotoMZ;
        this.incorrectPhotoMZ = incorrectPhotoMZ;
        this.centerPhotoMZ = centerPhotoMZ;
        this._30PhotoMZ = _30PhotoMZ;
        this.vertPhotoMZ = vertPhotoMZ;
        this.noPhotoK = noPhotoK;
        this.incorrectPhotoK = incorrectPhotoK;
        this.centerPhotoK = centerPhotoK;
        this._30PhotoK = _30PhotoK;
        this.vertPhotoK = vertPhotoK;
        this.noPhotoS = noPhotoS;
        this.incorrectPhotoS = incorrectPhotoS;
        this.centerPhotoS = centerPhotoS;
        this._30PhotoS = _30PhotoS;
        this.vertPhotoS = vertPhotoS;
        this.oos = oos;
        this.comment = comment;
    }
}
