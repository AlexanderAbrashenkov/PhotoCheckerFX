package foto_verif.model;

import javafx.beans.property.*;

import java.util.ArrayList;

/**
 * Created by market6 on 29.09.2016.
 */

public class AddressTT {
    private IntegerProperty id;
    private StringProperty name;
    private StringProperty address;
    private ObjectProperty<ArrayList> photoList;
    private StringProperty checked;
    private StringProperty typeName;

    public AddressTT(int id, String name, String address, ArrayList<Photo> photoList, String checked, String typeName) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.address = new SimpleStringProperty(address);
        this.photoList = new SimpleObjectProperty<>(photoList);
        this.checked = new SimpleStringProperty(checked);
        this.typeName = new SimpleStringProperty(typeName);
    }

    public AddressTT() {
    }

    public int getId() {
        return id.get();
    }
    public IntegerProperty idProperty() {
        return id;
    }
    public void setId(int id) {
        this.id.set(id);
    }
    public String getName() {
        return name.get();
    }
    public StringProperty nameProperty() {
        return name;
    }
    public void setName(String name) {
        this.name.set(name);
    }
    public String getAddress() {
        return address.get();
    }
    public StringProperty addressProperty() {
        return address;
    }
    public void setAddress(String address) {
        this.address.set(address);
    }
    public ArrayList getPhotoList() {
        return photoList.get();
    }
    public ObjectProperty<ArrayList> photoListProperty() {
        return photoList;
    }
    public void setPhotoList(ArrayList photoList) {
        this.photoList.set(photoList);
    }
    public String getChecked() {
        return checked.get();
    }
    public StringProperty checkedProperty() {
        return checked;
    }
    public void setChecked(String checked) {
        this.checked.set(checked);
    }
    public String getTypeName() {
        return typeName.get();
    }
    public StringProperty typeNameProperty() {
        return typeName;
    }
    public void setTypeName(String typeName) {
        this.typeName.set(typeName);
    }

    @Override
    public String toString() {
        return "AddressTT{" +
                "id=" + id +
                ", name=" + name +
                ", address=" + address +
                ", photoList=" + photoList +
                ", checked=" + checked +
                ", typeName=" + typeName +
                '}';
    }
}
