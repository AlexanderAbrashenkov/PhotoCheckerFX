package foto_verif.view.NKA;

import foto_verif.view.nst.NstAddress;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by market6 on 19.01.2017.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class NkaContainer {
    @XmlElementWrapper(name = "address_List")
    @XmlElement(name = "Address")
    private ArrayList<NkaAddress> nkaAddresses = new ArrayList<>();

    public NkaContainer() {
    }

    public ArrayList<NkaAddress> getNkaAddresses() {
        return nkaAddresses;
    }

    public void setNkaAddresses(ArrayList<NkaAddress> nkaAddresses) {
        this.nkaAddresses = nkaAddresses;
    }

    public boolean add(NkaAddress nkaAddress) {
        return nkaAddresses.add(nkaAddress);
    }

    public boolean addAll(List<NkaAddress> addressList) {
        return nkaAddresses.addAll(addressList);
    }

    public NkaAddress get(int i) {
        return nkaAddresses.get(i);
    }

    public void clear() {
        nkaAddresses.clear();
    }
}
