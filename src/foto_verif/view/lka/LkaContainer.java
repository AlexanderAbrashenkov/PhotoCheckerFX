package foto_verif.view.lka;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by market6 on 19.01.2017.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class LkaContainer {
    @XmlElementWrapper(name = "lka_address_List")
    @XmlElement(name = "Lka_Address")
    private ArrayList<LkaAddress> lkaAddresses = new ArrayList<>();

    public LkaContainer() {
    }

    public ArrayList<LkaAddress> getLkaAddresses() {
        return lkaAddresses;
    }

    public void setLkaAddresses(ArrayList<LkaAddress> lkaAddresses) {
        this.lkaAddresses = lkaAddresses;
    }

    public boolean add(LkaAddress lkaAddress) {
        return lkaAddresses.add(lkaAddress);
    }

    public boolean addAll(List<LkaAddress> addressList) {
        return lkaAddresses.addAll(addressList);
    }

    public LkaAddress get(int i) {
        return lkaAddresses.get(i);
    }

    public void clear() {
        lkaAddresses.clear();
    }
}
