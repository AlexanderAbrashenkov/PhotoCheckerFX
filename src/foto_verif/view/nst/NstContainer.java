package foto_verif.view.nst;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by market6 on 19.01.2017.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class NstContainer {
    @XmlElementWrapper(name = "address_List")
    @XmlElement(name = "Address")
    private ArrayList<NstAddress> nstAddresses = new ArrayList<>();

    public NstContainer() {
    }

    public ArrayList<NstAddress> getNstAddresses() {
        return nstAddresses;
    }

    public void setNstAddresses(ArrayList<NstAddress> nstAddresses) {
        this.nstAddresses = nstAddresses;
    }

    public boolean add(NstAddress nstAddress) {
        return nstAddresses.add(nstAddress);
    }

    public boolean addAll(List<NstAddress> addressList) {
        return nstAddresses.addAll(addressList);
    }

    public NstAddress get(int i) {
        return nstAddresses.get(i);
    }

    public void clear() {
        nstAddresses.clear();
    }

    public boolean contains(NstAddress nstAddress) {
        return nstAddresses.contains(nstAddress);
    }
}
