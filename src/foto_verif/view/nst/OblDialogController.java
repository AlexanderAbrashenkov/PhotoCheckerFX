package foto_verif.view.nst;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by market6 on 19.01.2017.
 */
public class OblDialogController {
    @FXML
    Label titleLabel;
    @FXML
    ScrollPane scrollPane;
    @FXML
    VBox vBox;
    @FXML
    Button shuffleButton;

    NstLayoutController nstLayoutController;
    Stage dialogStage;
    private List<String> allOblList;
    private List<String> downloadedOblList;

    private List<String> resultOblList;

    public void setNstLayoutController(NstLayoutController nstLayoutController) {
        this.nstLayoutController = nstLayoutController;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    public void buttonOkPressed () {
        resultOblList = new ArrayList<>();
        for (Node node: vBox.getChildren()) {
            CheckBox checkBox = (CheckBox) node;
            if (checkBox.isSelected()) {
                resultOblList.add(checkBox.getText());
            }
        }
        nstLayoutController.setChosenObls(resultOblList);
        dialogStage.close();
    }

    @FXML
    public void buttonCancelPressed() {
        resultOblList = null;
        nstLayoutController.setChosenObls(resultOblList);
        dialogStage.close();
    }

    @FXML
    public void buttonClearPressed() {
        for (Node node : vBox.getChildren()) {
            CheckBox checkBox = (CheckBox) node;
            checkBox.setSelected(false);
        }
    }

    public void addOblsToScrollPane(List<String> oblList, List<String> oblLoaded, String title) {
        allOblList = oblList;
        downloadedOblList = oblLoaded;
        titleLabel.setText(title);
        for (String s : oblList) {
            if (oblLoaded.contains(s)) continue;
            CheckBox checkBox = new CheckBox(s);
            checkBox.setPadding(new Insets(10, 0, 0, 0));
            vBox.getChildren().add(checkBox);
        }
        shuffleButton.setVisible(true);
    }

    @FXML
    public void shuffleObls() {
        int loadedOblsCount = downloadedOblList.size();
        if (loadedOblsCount == 1 && downloadedOblList.contains("По введенным параметрам нет фото...")) {
            loadedOblsCount = 0;
        }
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Ввод количества точек для загрузки");
        dialog.setHeaderText("Всего точек на сервере НСТ: " + allOblList.size() + ";\n" +
                "Загружено точек: " + loadedOblsCount + ";\n" +
                "Доступно для загрузки: " + (allOblList.size() - loadedOblsCount));
        dialog.setContentText("Сколько точек выделить для загрузки?");
        dialog.initOwner(dialogStage);
        int maxInt = allOblList.size() - loadedOblsCount;
        dialog.showAndWait().ifPresent(result -> {
            int numberToDownload;
            try {
                numberToDownload = Integer.parseInt(result);
            } catch (NumberFormatException e) {
                numberToDownload = 0;
            }
            if (numberToDownload > 0 && numberToDownload <= maxInt) {
                Set<Integer> numbersToCheck = new TreeSet<>();
                while (numbersToCheck.size() < numberToDownload) {
                    numbersToCheck.add(ThreadLocalRandom.current().nextInt(0, maxInt));
                }
                buttonClearPressed();
                for (int i = 0; i < maxInt; i++) {
                    if (numbersToCheck.contains(i)) {
                        ((CheckBox) vBox.getChildren().get(i)).setSelected(true);
                    }
                }
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Точки выделены случайным образом");
                alert.setHeaderText(null);
                alert.initOwner(dialogStage);
                alert.show();
                return;
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Такое количество точек загрузить нельзя!");
                alert.setHeaderText(null);
                alert.initOwner(dialogStage);
                alert.show();
            }
        });
    }
}
