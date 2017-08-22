package foto_verif.view.dmp;

import foto_verif.view.NKA.NkaAddress;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by market6 on 19.01.2017.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DmpContainer {
    @XmlElementWrapper(name = "address_List")
    @XmlElement(name = "Address")
    private ArrayList<DmpAddress> dmpAddresses = new ArrayList<>();

    public DmpContainer() {
    }

    public ArrayList<DmpAddress> getDmpAddresses() {
        return dmpAddresses;
    }

    public void setDmpAddresses(ArrayList<DmpAddress> dmpAddresses) {
        this.dmpAddresses = dmpAddresses;
    }

    public boolean add(DmpAddress dmpAddress) {
        return dmpAddresses.add(dmpAddress);
    }

    public boolean addAll(List<DmpAddress> addressList) {
        return dmpAddresses.addAll(addressList);
    }

    public DmpAddress get(int i) {
        return dmpAddresses.get(i);
    }

    public void clear() {
        dmpAddresses.clear();
    }
}
