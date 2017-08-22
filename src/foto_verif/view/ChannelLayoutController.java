package foto_verif.view;

import foto_verif.Main;
import foto_verif.model.AddressTT;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by market6 on 20.10.2016.
 */
public interface ChannelLayoutController {
    ObservableList<AddressTT> addresses = FXCollections.observableArrayList();
    ObservableList<Integer> checkedRows = FXCollections.observableArrayList();

    public ObservableList<AddressTT> getAddresses();

    public ObservableList<Integer> getCheckedRows();

    public void setMain(Main main);

    public void setRootLayoutController(RootLayoutController1 rootLayoutController);

    public void loadDatas();

    public void clearCheckBoxes();

    public void clearCheckedRows();
}
