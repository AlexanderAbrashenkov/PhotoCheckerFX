package foto_verif.model;

import foto_verif.view.dmp.DmpDescr;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;

/**
 * Created by market6 on 30.11.2016.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class DmpAddressTT {
    private int id;
    private String name;
    private String address;
    @XmlElementWrapper(name = "photo-list")
    @XmlElement(name = "photo")
    private ArrayList<Photo> photoList;
    @XmlElementWrapper(name = "dmpDescrList")
    @XmlElement(name = "dmpDescr")
    private ArrayList<DmpDescr> dmpDescrList;
    private String checked;
    private String typeName;

    public DmpAddressTT() {
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

    public ArrayList<DmpDescr> getDmpDescrList() {
        return dmpDescrList;
    }

    public String getChecked() {
        return checked;
    }

    public String getTypeName() {
        return typeName;
    }

    public void copyFromAddressTT(AddressTT addressTT) {
        this.id = addressTT.getId();
        this.name = addressTT.getName();
        this.address = addressTT.getAddress();
        this.photoList = addressTT.getPhotoList();
        this.checked = addressTT.getChecked();
        this.typeName = addressTT.getTypeName();
    }

    public void setDmpDescrList(ArrayList<DmpDescr> dmpDescrList) {
        this.dmpDescrList = dmpDescrList;
    }
}
