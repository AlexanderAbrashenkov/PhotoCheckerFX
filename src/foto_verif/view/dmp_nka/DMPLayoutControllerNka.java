package foto_verif.view.dmp_nka;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import foto_verif.Main;
import foto_verif.model.AddressTT;
import foto_verif.model.DmpAddressTT;
import foto_verif.view.dmp.DmpDescr;
import foto_verif.model.Photo;
import foto_verif.util.DateUtil;
import foto_verif.util.Logger;
import foto_verif.view.ChannelLayoutController;
import foto_verif.view.RootLayoutController1;
import foto_verif.view.dmp.CriteriasLayoutController;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

public class DMPLayoutControllerNka implements ChannelLayoutController {
    @FXML
    private ChoiceBox<String> rjkamChoice;
    @FXML
    private ChoiceBox<String> nkaChoice;
    @FXML
    private TableView<AddressTT> addressTable;
    @FXML
    private TableColumn<AddressTT, String> nameColumn;
    @FXML
    private TableColumn<AddressTT, String> addressColumn;
    @FXML
    private TableColumn<AddressTT, String> checkedColumn;

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private TilePane tilePane;

    @FXML
    private Label selectedAddress;
    @FXML
    private Label clientType;
    @FXML
    private ChoiceBox<Integer> dmpCount;
    @FXML
    private TabPane criteriasTabPane;
    @FXML
    private Label changesSavedLabel;

    private Main main;
    private RootLayoutController1 rootLayoutController;

    private ObservableList<String> rjkamList = FXCollections.observableArrayList();
    private ObservableList<String> nkaList = FXCollections.observableArrayList();

    private String rjkamSelected = null;
    private String nkaSelected = null;

    private ArrayList<CriteriasLayoutController> criteriasLayoutControllerList = new ArrayList<>();

    public ObservableList<AddressTT> getAddresses() {
        return addresses;
    }

    public ObservableList<Integer> getCheckedRows() {
        return checkedRows;
    }

    public ObservableList<String> getRjkamList() {
        return rjkamList;
    }

    public ObservableList<String> getNkaList() {
        return nkaList;
    }

    public DMPLayoutControllerNka() {
    }

    @FXML
    private void initialize() {

        // запрет сортировки точек в таблице
        nameColumn.setSortable(false);
        addressColumn.setSortable(false);
        checkedColumn.setSortable(false);

        // Отслеживание выбора таблицы
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        addressColumn.setCellValueFactory(cellData -> cellData.getValue().addressProperty());
        checkedColumn.setCellValueFactory(cellData -> cellData.getValue().checkedProperty());

        addressTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<AddressTT>() {
            @Override
            public void changed(ObservableValue<? extends AddressTT> observable, AddressTT oldValue, AddressTT newValue) {
                resizeTabNumbers(1);
                dmpCount.getSelectionModel().select(0);
                showAddressPhotos(null);
                showAddressPhotos(newValue);
                clearCheckBoxes();

                if (newValue == null) {
                    getCheckedRows().clear();
                }
                selectedAddress.setText("");
                clientType.setText("");
                if (newValue != null) {
                    selectedAddress.setText(Integer.toString(newValue.getId()));
                    clientType.setText(newValue.getTypeName());
                }

                rootLayoutController.showErrorMessage("");

                if (newValue != null) {
                    if (getAddresses().get(addressTable.getSelectionModel().getSelectedIndex()).getChecked().equals("1")) {
                        setCheckBoxesFromFile();
                    }
                }
            }
        });

        // Отслеживание выбора rjkam
        rjkamChoice.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue != null && !newValue.startsWith("По введенным")) {
                    rootLayoutController.showErrorMessage("Загружаются данные. Ждите...");
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            selectedRjkam(newValue);
                        }
                    });
                }
            }
        });

        // Отслеживание выбора сети
        nkaChoice.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue != null && !newValue.startsWith("По введенным")) {
                    rootLayoutController.showErrorMessage("Загружаются данные. Ждите...");
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            selectedNka(newValue);
                        }
                    });
                }
            }
        });

        // Параметры поля просмотра фото
        tilePane.setPadding(new Insets(15, 15, 15, 15));
        tilePane.setHgap(15);
        tilePane.setVgap(15);
        scrollPane.setFitToWidth(true);

        dmpCount.getItems().addAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        dmpCount.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                resizeTabNumbers(newValue);
            }
        });

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(this.getClass().getResource("CriteriasLayoutNka.fxml"));
            AnchorPane anchorPane = (AnchorPane) loader.load();

            CriteriasLayoutController controller = loader.getController();
            criteriasLayoutControllerList.add(controller);
            criteriasTabPane.getTabs().get(0).setContent(anchorPane);
        } catch (IOException e) {
            Logger.log(e.getMessage());
            e.printStackTrace();
        }
    }

    // Отображение фотографий по выбранному адресу
    private void showAddressPhotos(AddressTT address) {
        if (address != null) {
            ArrayList<Photo> photos = address.getPhotoList();
            Collections.sort(photos);
            for (Photo photo : photos) {
                String url = photo.getUrl();
                ImageView imageView = null;

                Future<ImageView> future = main.getExecutor().submit(new Callable<ImageView>() {
                    @Override
                    public ImageView call() throws Exception {
                        return createImageView(url);
                    }
                });
                //executor.shutdown();            //        <-- reject all further submissions
                try {
                    imageView = future.get(10, TimeUnit.SECONDS);  //     <-- wait 8 seconds to finish
                } catch (InterruptedException e) {    //     <-- possible error cases
                    Logger.log(e.getMessage());
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    Logger.log(e.getMessage());
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    future.cancel(true);              //     <-- interrupt the job
                    rootLayoutController.showExceptionAlert("Нет подключения к интернету. Попробуйте позднее.");
                }

                if (imageView == null) return;

                VBox photoBox = new VBox();
                photoBox.setPrefHeight(imageView.getFitHeight() + 30);
                photoBox.getChildren().addAll(imageView);
                Label photoDate = new Label(DateUtil.format(photo.getDate()));
                Label photoAdded = new Label("Дата добавления: " + DateUtil.format(photo.getDateAdded()));
                TextArea comment = new TextArea(photo.getComment());
                if (photo.isChecked()) {
                    photoDate.setStyle("-fx-background-color: lightgreen");
                    photoAdded.setStyle("-fx-background-color: lightgreen");
                    comment.setStyle("-fx-background-color: lightgreen");
                }
                photoDate.setFont(Font.font("sans-serif", FontWeight.BOLD, 12d));
                photoDate.setPrefWidth(200);
                photoDate.setAlignment(Pos.CENTER);
                photoAdded.setFont(Font.font("sans-serif", FontWeight.BOLD, 12d));
                photoAdded.setPrefWidth(200);
                photoAdded.setAlignment(Pos.CENTER);
                comment.setFont(Font.font("sans-serif", 12d));
                comment.setPrefWidth(200);
                comment.setWrapText(true);
                comment.setEditable(false);
                comment.setPrefRowCount(2);
                photoBox.getChildren().addAll(photoDate, photoAdded, comment);
                tilePane.getChildren().addAll(photoBox);
            }
        } else {
            tilePane.getChildren().clear();
        }
    }

    // Создание вьюшки для фотки
    private ImageView createImageView(final String imageUrl) {
        ImageView imageView = new ImageView(imageUrl);
        imageView.setFitWidth(200);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setCache(true);
        imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent mouseEvent) {

                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {

                    if (mouseEvent.getClickCount() == 2) {
                        BorderPane borderPane = new BorderPane();
                        String fullPhotoUrl = imageUrl.replace("thumb/", "");
                        ImageView imageView = new ImageView(fullPhotoUrl);
                        imageView.setStyle("-fx-background-color: BLACK");
                        imageView.setFitHeight(main.getPrimaryStage().getHeight());
                        imageView.setFitWidth(main.getPrimaryStage().getWidth());
                        imageView.setPreserveRatio(true);
                        imageView.setSmooth(true);
                        imageView.setCache(true);

                        VBox rightBox = new VBox();
                        rightBox.setPrefWidth(85);
                        rightBox.setMinWidth(85);
                        ImageView forward = new ImageView(new Image(Main.class.getResourceAsStream("images/forward.png")));
                        forward.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent event) {
                                imageView.setRotate(imageView.getRotate() + 90);
                            }
                        });
                        ImageView back = new ImageView(new Image(Main.class.getResourceAsStream("images/back.png")));
                        back.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent event) {
                                imageView.setRotate(imageView.getRotate() - 90);
                            }
                        });
                        ImageView zoomin = new ImageView(new Image(Main.class.getResourceAsStream("images/zoom-in.png")));
                        zoomin.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent event) {
                                imageView.setFitWidth(imageView.getFitWidth() * 1.1);
                                imageView.setFitHeight(imageView.getFitHeight() * 1.1);
                            }
                        });
                        ImageView zoomout = new ImageView(new Image(Main.class.getResourceAsStream("images/zoom-out.png")));
                        zoomout.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent event) {
                                imageView.setFitWidth(imageView.getFitWidth() / 1.1);
                                imageView.setFitHeight(imageView.getFitHeight() / 1.1);
                            }
                        });
                        ImageView copy = new ImageView(new Image(Main.class.getResourceAsStream("images/copy.png")));
                        copy.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent event) {
                                StringSelection ss = new StringSelection(fullPhotoUrl);
                                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, ss);
                            }
                        });
                        AnchorPane blankPane = new AnchorPane();
                        blankPane.setPrefHeight(100);
                        rightBox.getChildren().addAll(blankPane, forward, back, zoomin, zoomout, copy);
                        borderPane.setRight(rightBox);
                        ScrollPane scrollPane = new ScrollPane(imageView);
                        scrollPane.setStyle("-fx-background: BLACK");
                        borderPane.setCenter(scrollPane);
                        borderPane.setStyle("-fx-background-color: BLACK");
                        Stage newStage = new Stage();
                        newStage.setWidth(main.getPrimaryStage().getWidth());
                        newStage.setHeight(main.getPrimaryStage().getHeight());
                        Scene scene = new Scene(borderPane, Color.BLACK);
                        newStage.setScene(scene);
                        newStage.show();
                    }
                }
            }
        });

        return imageView;
    }

    @Override
    public void setMain(Main main) {
        this.main = main;
        addressTable.setItems(getAddresses());
        nkaChoice.setItems(getNkaList());
        rjkamChoice.setItems(getRjkamList());
    }

    @Override
    public void setRootLayoutController(RootLayoutController1 rootLayoutController) {
        this.rootLayoutController = rootLayoutController;

        rootLayoutController.getImportTMA().setVisible(true);
        rootLayoutController.getExportTMA().setVisible(true);
        rootLayoutController.getClearAll().setVisible(false);

        rootLayoutController.getNetLabel().setVisible(true);
        rootLayoutController.getNetChoice().setVisible(true);
    }

    public void loadDatas() {
        clearCheckBoxes();
        getNkaList().clear();
        getRjkamList().clear();
        getAddresses().clear();

        try {
            List<String> rjkamList = main.getJdbcModel().getDatasForFirstQuery(rootLayoutController.getNetSelected(),
                    rootLayoutController.getDateFromSelected(), rootLayoutController.getDateToSelected());
            if (rjkamList.size() > 0) {
                getRjkamList().addAll(rjkamList);
            } else {
                getRjkamList().addAll("По введенным параметрам нет фото...");
            }
            rootLayoutController.showErrorMessage("");
        } catch (SQLServerException e) {
            Logger.log(e.getMessage());
            e.printStackTrace();
            rootLayoutController.showExceptionAlert("База не отвечает на запрос. Попробуйте позднее.");
            rootLayoutController.showErrorMessage("Невозможно подключиться к базе данных");
        } catch (Exception e) {
            Logger.log(e.getMessage());
            e.printStackTrace();
            rootLayoutController.showErrorMessage("Невозможно подключиться к базе данных");
        }
    }

    /*
    Действия при выборе сети
     */
    private void selectedRjkam (String rjkamName) {
        rjkamSelected = rjkamName;
        rootLayoutController.showErrorMessage("");
        getNkaList().clear();
        getAddresses().clear();

        try {
            List<String> nkaList = main.getJdbcModel().getNkaList(rjkamName);
            if (nkaList.size() > 0) {
                getNkaList().addAll(nkaList);
            } else {
                getNkaList().addAll("По введенным параметрам нет фото...");
            }
            rootLayoutController.showErrorMessage("");
        } catch (SQLServerException e) {
            Logger.log(e.getMessage());
            e.printStackTrace();
            rootLayoutController.showExceptionAlert("База не отвечает на запрос. Попробуйте позднее.");
            rootLayoutController.showErrorMessage("Невозможно подключиться к базе данных");
        } catch (SQLException e) {
            Logger.log(e.getMessage());
            e.printStackTrace();
            rootLayoutController.showErrorMessage("Невозможно подключиться к базе данных");
        }
    }

    /*
    Действия при выборе rjkam
     */
    private void selectedNka(String nkaName) {
        nkaSelected = nkaName;
        rootLayoutController.showErrorMessage("");
        getAddresses().clear();
        getCheckedRows().clear();

        try {
            main.getJdbcModel().getAddresses(nkaName, null);
            rootLayoutController.showErrorMessage("");
        } catch (SQLServerException e) {
            Logger.log(e.getMessage());
            e.printStackTrace();
            rootLayoutController.showExceptionAlert("База не отвечает на запрос. Попробуйте позднее.");
            rootLayoutController.showErrorMessage("Невозможно подключиться к базе данных");
        } catch (SQLException e) {
            Logger.log(e.getMessage());
            e.printStackTrace();
        }
    }

    /*
    Очистка заполнения полей
     */
    @FXML
    public void clearCheckBoxes() {
        for (int i = 0; i < criteriasTabPane.getTabs().size(); i++) {
            criteriasLayoutControllerList.get(i).clearCheckBoxesCurrDMP();
        }
        changesSavedLabel.setText("");
    }

    @Override
    public void clearCheckedRows() {
        getCheckedRows().clear();
        for (AddressTT addressTT : getAddresses()) {
            for (Photo photo : (ArrayList<Photo>) addressTT.getPhotoList()) {
                photo.setChecked(false);
            }
            addressTT.setChecked("0");
        }
    }

    private void setCheckBoxesFromFile() {
        String tmpRjkamSelected = null;
        if (rjkamSelected != null) {
            tmpRjkamSelected = rjkamSelected.endsWith(".") ? rjkamSelected.substring(0, rjkamSelected.length() - 1) : rjkamSelected;
        }
        String path = ("save/2/" + DateUtil.format(rootLayoutController.getDateFromSelected()) + "-" + DateUtil.format(rootLayoutController.getDateToSelected()) + "/" +
                tmpRjkamSelected + "/" + nkaSelected).replace(" ", "_").replace("\"", "") + "/" + addressTable.getSelectionModel().getSelectedItem().getId() + ".adr";

        if (new File(path).exists()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(path));
                JAXBContext context = JAXBContext.newInstance(DmpAddressTT.class, Photo.class, DmpDescr.class);
                Unmarshaller unmarshaller = context.createUnmarshaller();
                DmpAddressTT dmpAddressTT = (DmpAddressTT) unmarshaller.unmarshal(reader);
                reader.close();

                int dmpNum = dmpAddressTT.getDmpDescrList().size();
                dmpCount.getSelectionModel().select(dmpNum - 1);
                resizeTabNumbers(dmpNum);

                for (int i = 0; i < dmpNum; i++) {
                    criteriasLayoutControllerList.get(i).setCheckBoxesFromDmpDescr(dmpAddressTT.getDmpDescrList().get(i));
                }
            } catch (IOException | JAXBException e) {
                Logger.log(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void saveAddress() {
        rootLayoutController.showErrorMessage("");
        changesSavedLabel.setText("");
        int addressIndex = addressTable.getSelectionModel().getSelectedIndex();
        if (addressIndex == -1) {
            rootLayoutController.showErrorMessage("Не выбрана точка для сохранения");
            return;
        }

        for (int i = 0; i < criteriasLayoutControllerList.size(); i++) {
            if (criteriasLayoutControllerList.get(i).isPhotoGood() && !criteriasLayoutControllerList.get(i).isTgSelected()) {
                rootLayoutController.showErrorMessage("Не выбраны ТГ");
                return;
            }
        }

        AddressTT addressTT = getAddresses().get(addressIndex);
        String tmpRjkamSelected = null;
        if (rjkamSelected != null) {
            tmpRjkamSelected = rjkamSelected.endsWith(".") ? rjkamSelected.substring(0, rjkamSelected.length() - 1) : rjkamSelected;
        }
        String path = "save/2/" + DateUtil.format(rootLayoutController.getDateFromSelected()) + "-" + DateUtil.format(rootLayoutController.getDateToSelected()) + "/" +
                tmpRjkamSelected + "/" + nkaSelected;
        path = path.replace(" ", "_").replace("\"", "");
        new File(path).mkdirs();
        File file1 = new File(path + "/" + addressTT.getId() + ".adr");

        try {
            if (!file1.exists())
                file1.createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(file1));
            DmpAddressTT dmpAddressTT = new DmpAddressTT();
            dmpAddressTT.copyFromAddressTT(addressTT);
            int dmpNum = criteriasTabPane.getTabs().size();
            ArrayList<DmpDescr> dmpDescrs = new ArrayList<>();
            for (int i = 0; i < dmpNum; i++) {
                dmpDescrs.add(criteriasLayoutControllerList.get(i).getDmpDescr());
            }
            dmpAddressTT.setDmpDescrList(dmpDescrs);
            JAXBContext context = JAXBContext.newInstance(DmpAddressTT.class, Photo.class, DmpDescr.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(dmpAddressTT, writer);
            writer.flush();
            writer.close();
        } catch (IOException | JAXBException e) {
            rootLayoutController.showErrorMessage("Не удалось сохранить данные");
            Logger.log(e.getMessage());
            e.printStackTrace();
            return;
        }

        addressTT.setChecked("1");
        getCheckedRows().add(addressTable.getSelectionModel().getSelectedIndex());
        for (Photo photo : (ArrayList<Photo>) addressTT.getPhotoList()) {
            photo.setChecked(true);
        }
        for (Node node : tilePane.getChildren()) {
            VBox vBox = (VBox) node;
            vBox.getChildren().get(1).setStyle("-fx-background-color: lightgreen");
            vBox.getChildren().get(2).setStyle("-fx-background-color: lightgreen");
        }
        changesSavedLabel.setText("Изменения сохранены");
    }

    private void resizeTabNumbers(int dmpNum) {
        int tabsCount = criteriasTabPane.getTabs().size();
        if (dmpNum == tabsCount) {
            return;
        } else if (dmpNum > tabsCount) {
            for (int i = 0; i < dmpNum - tabsCount; i++) {
                try {
                    Tab tab = new Tab();
                    tab.setText("ДМП " + (tabsCount + i + 1));
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(this.getClass().getResource("CriteriasLayoutNka.fxml"));
                    AnchorPane anchorPane = (AnchorPane) loader.load();

                    CriteriasLayoutController controller = loader.getController();
                    this.criteriasLayoutControllerList.add(controller);
                    tab.setContent(anchorPane);

                    criteriasTabPane.getTabs().add(tab);
                } catch (IOException e) {
                    Logger.log(e.getMessage());
                    e.printStackTrace();
                }
            }
        } else {
            for (int i = tabsCount - 1; i >= dmpNum; i--) {
                criteriasTabPane.getTabs().remove(i);
                criteriasLayoutControllerList.remove(i);
            }
        }
    }
}
