package foto_verif.view;

import foto_verif.Main;
import foto_verif.model.jdbc.JdbcModelManager;
import foto_verif.util.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

public class RootLayoutController1 {
    @FXML
    private ChoiceBox<String> photoType;
    @FXML
    private Label netLabel;
    @FXML
    private ChoiceBox<String> netChoice;
    @FXML
    private DatePicker dateFrom;
    @FXML
    private DatePicker dateTo;
    @FXML
    private Label errorLabel;
    @FXML
    private AnchorPane childPane;
    @FXML
    private Button importTMA;
    @FXML
    private Button exportTMA;
    @FXML
    private Button clearAll;

    private Main main;

    private int photoTypeSelected = -1;
    private String netSelected = null;
    private LocalDate dateFromSelected = null;
    private LocalDate dateToSelected = null;

    public Button getImportTMA() {
        return importTMA;
    }

    public Button getExportTMA() {
        return exportTMA;
    }

    public Button getClearAll() {
        return clearAll;
    }

    public int getPhotoTypeSelected() {
        return photoTypeSelected;
    }

    public void setPhotoTypeSelected(int photoTypeSelected) {
        this.photoTypeSelected = photoTypeSelected;
    }

    public Label getNetLabel() {
        return netLabel;
    }

    public ChoiceBox<String> getNetChoice() {
        return netChoice;
    }

    public String getNetSelected() {
        return netSelected;
    }

    public void setNetSelected(String netSelected) {
        this.netSelected = netSelected;
    }

    public LocalDate getDateFromSelected() {
        return dateFromSelected;
    }

    public void setDateFromSelected(LocalDate dateFromSelected) {
        this.dateFromSelected = dateFromSelected;
    }

    public LocalDate getDateToSelected() {
        return dateToSelected;
    }

    public void setDateToSelected(LocalDate dateToSelected) {
        this.dateToSelected = dateToSelected;
    }

    public AnchorPane getChildPane() {
        return childPane;
    }

    public RootLayoutController1() {
    }

    @FXML
    private void initialize() {

        // Отслеживание выбора типа фото
        photoType.getItems().addAll(FXCollections.observableArrayList("1. Локал. сети: ДМП", "2. Федер сети: фотоотчеты MLKA", "3. Федер. сети: ДМП",
                "4. Фото НСТ", "5. Локальные сети"));
        photoType.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            selectPhotoType(newValue);
        });

        // Отслеживание выбора сети
        netChoice.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            selectNetName(newValue);
        });

        // Отслеживание изменения дат
        dateFrom.setOnAction(event -> {
            dateFromSelected = dateFrom.getValue();
            errorLabel.setText("");
            main.showGreetingLayout();
        });
        dateTo.setOnAction(event -> {
            dateToSelected = dateTo.getValue();
            errorLabel.setText("");
            main.showGreetingLayout();
        });
    }

    public void setMain(Main main) {
        this.main = main;
        netChoice.setItems(main.getNetList());
    }

    /*
    Действия при выборе типа фотографий
     */
    private void selectPhotoType(Number newValue) {
        errorLabel.setText("");
        if (newValue.intValue() == 0) {
            photoTypeSelected = 0;
            netSelected = null;
            main.getNetList().clear();
            netChoice.setDisable(true);
            main.showGreetingLayout();

            getImportTMA().setVisible(false);
            getExportTMA().setVisible(false);

            getNetLabel().setVisible(false);
            getNetChoice().setVisible(false);

        } else if (newValue.intValue() == 1) {
            photoTypeSelected = 1;
            netChoice.setDisable(false);
            main.getNetList().addAll("X5", "Тандер");
            main.showGreetingLayout();

            getImportTMA().setVisible(true);
            getExportTMA().setVisible(true);

            getNetLabel().setVisible(true);
            getNetChoice().setVisible(true);

        } else if (newValue.intValue() == 2) {
            photoTypeSelected = 2;
            netSelected = null;
            main.getNetList().clear();
            netChoice.setDisable(true);
            main.showGreetingLayout();

            getImportTMA().setVisible(true);
            getExportTMA().setVisible(true);

            getNetLabel().setVisible(false);
            getNetChoice().setVisible(false);

        } else if (newValue.intValue() == 3) {
            photoTypeSelected = 3;
            netSelected = null;
            main.getNetList().clear();
            netChoice.setDisable(true);
            main.showGreetingLayout();

            getImportTMA().setVisible(false);
            getExportTMA().setVisible(false);

            getNetLabel().setVisible(false);
            getNetChoice().setVisible(false);
        } else if (newValue.intValue() == 4) {
            photoTypeSelected = 4;
            netSelected = null;
            main.getNetList().clear();
            netChoice.setDisable(true);
            main.showGreetingLayout();

            getImportTMA().setVisible(true);
            getExportTMA().setVisible(true);

            getNetLabel().setVisible(false);
            getNetChoice().setVisible(false);
        }
    }

    /*
    Действия при выборе сети
     */
    private void selectNetName(Number newValue) {
        errorLabel.setText("");
        if (newValue.intValue() == 0) {
            netSelected = "X5";
            main.showGreetingLayout();
        } else if (newValue.intValue() == 1) {
            netSelected = "Тандер";
            main.showGreetingLayout();
        }
    }

    /*
    Подключение к БД и выгрузка данных
     */
    @FXML
    private void loadDatas() {
        try {
            errorLabel.setText("Загружаются данные. Ждите...");
            // не введены какие-то из данных
            if (photoTypeSelected == -1) {
                errorLabel.setText("Не выбран тип фото");
                return;
            }
            if (photoTypeSelected == 1 && netSelected == null) {
                errorLabel.setText("Не выбрана сеть");
                return;
            }
            dateFromSelected = dateFrom.getValue();
            dateToSelected = dateTo.getValue();
            if (dateFromSelected == null || dateToSelected == null) {
                errorLabel.setText("Не выбрана одна из дат");
                return;
            }

            // все введено
            if (photoTypeSelected == 0) {
                main.showDMPLayout();
            } else if (photoTypeSelected == 1) {
                main.showNKALayout();
            } else if (photoTypeSelected == 2) {
                main.showDmpNkaLayout();
            } else if (photoTypeSelected == 3) {
                main.showNstLayout();
            } else if (photoTypeSelected == 4) {
                main.showLkaLayout();
            }

            if (photoTypeSelected != 3) {
                JdbcModelManager.createJdbcModel(photoTypeSelected);
                main.setJdbcModel(JdbcModelManager.getInstance());
            }

            Platform.runLater(() -> main.getChannelLayoutController().loadDatas());
        } catch (Exception e) {
            Logger.log(e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void clearAllSaves() {
        recursiveDeleteSaves(new File("save"));
        if (main.getChannelLayoutController() != null) {
            main.getChannelLayoutController().clearCheckedRows();
            main.getChannelLayoutController().clearCheckBoxes();
        }
    }

    private void recursiveDeleteSaves(File file) {
        if (!file.exists()) {
            return;
        } else if (file.isDirectory()) {
            for (File file1 : file.listFiles()) {
                recursiveDeleteSaves(file1);
            }
            file.delete();
        } else {
            file.delete();
        }
    }

    @FXML
    private void makeExcelReport() {
        main.makeExcelReport();
    }

    @FXML
    private void getTMATemplate() {
        try {
            main.saveAndShowTMATemplate();
        } catch (Exception e) {
            errorLabel.setText("Не удалось выгрузить шаблон");
            Logger.log(e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void importTMAFile() {
        try {
            main.importTMAList();
        } catch (IOException e) {
            errorLabel.setText("Не удалось импортировать акции");
            Logger.log(e.getMessage());
            e.printStackTrace();
        }
    }

    public void showErrorMessage(String message) {
        errorLabel.setText(message);
    }

    public void showExceptionAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    public void showHelp() {
        //todo: write FAQ for NST
    }
}
