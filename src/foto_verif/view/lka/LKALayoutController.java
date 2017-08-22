package foto_verif.view.lka;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import foto_verif.Main;
import foto_verif.model.AddressTT;
import foto_verif.model.Photo;
import foto_verif.model.TMAActivity;
import foto_verif.model.apache_poi.ApachePoi;
import foto_verif.model.apache_poi.ApachePoiManager;
import foto_verif.util.DateUtil;
import foto_verif.util.Logger;
import foto_verif.view.ChannelLayoutController;
import foto_verif.view.RootLayoutController1;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
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
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

public class LKALayoutController implements ChannelLayoutController {
    @FXML
    private ChoiceBox<String> regionChoice;
    @FXML
    private ChoiceBox<String> oblChoice;
    @FXML
    private ChoiceBox<String> lkaChoice;
    @FXML
    private CheckBox showNonChecked;
    @FXML
    private TableView<LkaAddress> addressTable;
    @FXML
    private TableColumn<LkaAddress, String> nameColumn;
    @FXML
    private TableColumn<LkaAddress, String> addressColumn;
    @FXML
    private TableColumn<LkaAddress, String> checkedColumn;

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private TilePane tilePane;

    @FXML
    private Label selectedAddress;
    @FXML
    private Label typeShopLabel;
    @FXML
    private Label mzLabel;
    @FXML
    private CheckBox havePhotoMZ;
    @FXML
    private CheckBox haveAdditionalProduct;
    @FXML
    private CheckBox isCorrectPhotoMZ;
    @FXML
    private CheckBox param1MZ;
    @FXML
    private CheckBox param2MZ;
    @FXML
    private Label kLabel;
    @FXML
    private CheckBox havePhotoK;
    @FXML
    private CheckBox isCorrectPhotoK;
    @FXML
    private CheckBox param1K;
    @FXML
    private CheckBox param2K;
    @FXML
    private Label sLabel;
    @FXML
    private CheckBox havePhotoS;
    @FXML
    private CheckBox isCorrectPhotoS;
    @FXML
    private CheckBox param1S;
    @FXML
    private CheckBox param2S;
    @FXML
    private CheckBox oos;
    @FXML
    private TextArea comment;
    @FXML
    private Label changesSavedLabel;
    @FXML
    private ScrollPane tmaScrollPane;
    @FXML
    private GridPane tmaGridPane;

    private Main main;
    private RootLayoutController1 rootLayoutController;

    private ObservableList<String> regionList = FXCollections.observableArrayList();
    private ObservableList<String> oblList = FXCollections.observableArrayList();
    private ObservableList<String> lkaList = FXCollections.observableArrayList();
    private ObservableList<LkaAddress> lkaAddressesToShow = FXCollections.observableArrayList();

    private LkaContainer lkaContainer = new LkaContainer();
    private List<LkaCriteria> lkaCriteriaList;

    private String regionSelected = null;
    private String oblSelected = null;
    private String lkaSelected = null;

    private static boolean haveMz = true;
    private static boolean haveK = true;
    private static boolean haveS = true;

    String pathToSave;

    public ObservableList<AddressTT> getAddresses() {
        return addresses;
    }

    public ObservableList<Integer> getCheckedRows() {
        return checkedRows;
    }

    public ObservableList<String> getRegionList() {
        return regionList;
    }

    public ObservableList<String> getOblList() {
        return oblList;
    }

    public ObservableList<String> getLkaList() {
        return lkaList;
    }

    public ObservableList<LkaAddress> getLkaAddressesToShow() {
        return lkaAddressesToShow;
    }

    public LKALayoutController() {
    }

    @FXML
    private void initialize() {

        // запрет сортировки точек в таблице
        //addressColumn.setSortable(false);
        //checkedColumn.setSortable(false);

        // Отслеживание выбора таблицы
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        addressColumn.setCellValueFactory(cellData -> cellData.getValue().addressProperty());
        checkedColumn.setCellValueFactory(cellData -> cellData.getValue().checkedProperty());

        addressTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            showAddressPhotos(null);
            showAddressPhotos(newValue);

            if (newValue == null) {
                getCheckedRows().clear();
            }
            selectedAddress.setText("");
            typeShopLabel.setText("");
            if (newValue != null) {
                selectedAddress.setText(newValue.getAddress());
                typeShopLabel.setText(newValue.getTypeName());
            }

            clearCheckBoxes();
            rootLayoutController.showErrorMessage("");

            if (newValue != null) {
                setCheckBoxesFromFile(newValue);
            }
        });

        // Отслеживание выбора региона
        regionChoice.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.startsWith("По введенным")) {
                rootLayoutController.showErrorMessage("Загружаются данные. Ждите...");
                Platform.runLater(() -> selectRegion(newValue));
            }
        });

        // Отслеживание выбора области
        oblChoice.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.startsWith("По введенным")) {
                rootLayoutController.showErrorMessage("Загружаются данные. Ждите...");
                Platform.runLater(() -> selectObl(newValue));
            }
        });

        // Отслеживание выбора MLKA
        lkaChoice.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.startsWith("По введенным")) {
                rootLayoutController.showErrorMessage("Загружаются данные. Ждите...");
                Platform.runLater(() -> selectLka(newValue));
            }
        });

        showNonChecked.selectedProperty().addListener((observable, oldValue, newValue) -> {
            fillUpRegionList();
        });

        // Отслеживание выбора наличия фото
        havePhotoMZ.selectedProperty().addListener((observable, oldValue, newValue) -> {
            setMZGroupEnable(newValue);
            enableTMAbyTG("Майонез", newValue);
        });

        havePhotoK.selectedProperty().addListener((observable, oldValue, newValue) -> {
            setKGroupEnable(newValue);
            enableTMAbyTG("Кетчуп", newValue);
        });

        havePhotoS.selectedProperty().addListener((observable, oldValue, newValue) -> {
            setSGroupEnable(newValue);
            enableTMAbyTG("Соус", newValue);
        });

        // Параметры поля просмотра фото
        tilePane.setPadding(new Insets(15, 15, 15, 15));
        tilePane.setHgap(15);
        tilePane.setVgap(15);
        scrollPane.setFitToWidth(true);

        // Параметры поля с акциями
        tmaScrollPane.setFitToWidth(true);
    }

    // Отображение фотографий по выбранному адресу
    private void showAddressPhotos(LkaAddress address) {
        if (address != null) {
            List<Photo> photos = address.getPhotoList();
            Collections.sort(photos);
            for (Photo photo : photos) {
                String url = photo.getUrl();
                ImageView imageView = null;

                Future<ImageView> future = main.getExecutor().submit(() -> {
                    return createImageView(url, photos.indexOf(photo));
                });
                //executor.shutdown();            //        <-- reject all further submissions
                try {
                    imageView = future.get(10, TimeUnit.SECONDS);  //     <-- wait 10 seconds to finish
                } catch (InterruptedException | ExecutionException e) {    //     <-- possible error cases
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
                TextArea comment = new TextArea(photo.getComment());
                if (photo.isChecked()) {
                    photoDate.setStyle("-fx-background-color: lightgreen");
                    comment.setStyle("-fx-background-color: lightgreen");
                }
                photoDate.setFont(Font.font("sans-serif", FontWeight.BOLD, 12d));
                photoDate.setPrefWidth(200);
                photoDate.setAlignment(Pos.CENTER);
                comment.setFont(Font.font("sans-serif", 12d));
                comment.setPrefWidth(200);
                comment.setWrapText(true);
                comment.setEditable(false);
                comment.setPrefRowCount(2);
                photoBox.getChildren().addAll(photoDate, comment);
                tilePane.getChildren().addAll(photoBox);
            }
        } else {
            tilePane.getChildren().clear();
        }
    }

    private static int showPhotoPos;

    // Создание вьюшки для фотки
    private ImageView createImageView(final String imageUrl, int pos) {
        int photoPos = pos;
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
                        showPhotoPos = photoPos;
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
                        forward.setOnMouseClicked(event -> {
                            imageView.setRotate(imageView.getRotate() + 90);
                        });

                        ImageView back = new ImageView(new Image(Main.class.getResourceAsStream("images/back.png")));
                        back.setOnMouseClicked(event -> {
                            imageView.setRotate(imageView.getRotate() - 90);
                        });

                        ImageView zoomin = new ImageView(new Image(Main.class.getResourceAsStream("images/zoom-in.png")));
                        zoomin.setOnMouseClicked(event -> {
                            imageView.setFitWidth(imageView.getFitWidth() * 1.1);
                            imageView.setFitHeight(imageView.getFitHeight() * 1.1);
                        });

                        ImageView zoomout = new ImageView(new Image(Main.class.getResourceAsStream("images/zoom-out.png")));
                        zoomout.setOnMouseClicked(event -> {
                            imageView.setFitWidth(imageView.getFitWidth() / 1.1);
                            imageView.setFitHeight(imageView.getFitHeight() / 1.1);
                        });

                        ImageView copy = new ImageView(new Image(Main.class.getResourceAsStream("images/copy.png")));
                        copy.setOnMouseClicked(event -> {
                            StringSelection ss = new StringSelection(fullPhotoUrl);
                            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, ss);
                        });

                        ImageView left = new ImageView(new Image(Main.class.getResourceAsStream("images/left.png")));
                        left.setOnMouseClicked(event -> {
                            if (showPhotoPos > 0) {
                                String leftImageUrl = addressTable.getSelectionModel().getSelectedItem().getPhotoList().get(showPhotoPos - 1).getUrl().replace("thumb/", "");
                                imageView.setImage(new Image(leftImageUrl));
                                showPhotoPos--;
                            }
                        });

                        ImageView right = new ImageView(new Image(Main.class.getResourceAsStream("images/right.png")));
                        right.setOnMouseClicked(event -> {
                            int photoCount = addressTable.getSelectionModel().getSelectedItem().getPhotoList().size();
                            if (showPhotoPos < photoCount - 1) {
                                String rightImageUrl = addressTable.getSelectionModel().getSelectedItem().getPhotoList().get(showPhotoPos + 1).getUrl().replace("thumb/", "");
                                imageView.setImage(new Image(rightImageUrl));
                                showPhotoPos++;
                            }
                        });

                        AnchorPane blankPane = new AnchorPane();
                        blankPane.setPrefHeight(100);
                        rightBox.getChildren().addAll(blankPane, forward, back, zoomin, zoomout, copy, left, right);

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
        addressTable.setItems(getLkaAddressesToShow());
        regionChoice.setItems(getRegionList());
        oblChoice.setItems(getOblList());
        lkaChoice.setItems(getLkaList());
    }

    @Override
    public void setRootLayoutController(RootLayoutController1 rootLayoutController) {
        this.rootLayoutController = rootLayoutController;

        rootLayoutController.getImportTMA().setVisible(true);
        rootLayoutController.getExportTMA().setVisible(true);
        rootLayoutController.getClearAll().setVisible(false);

        //rootLayoutController.getNetLabel().setVisible(true);
        //rootLayoutController.getNetChoice().setVisible(true);
    }

    public void loadDatas() {
        clearTMAActivityPane();
        clearCheckBoxes();
        lkaContainer.clear();
        getRegionList().clear();
        getOblList().clear();
        getLkaList().clear();
        getLkaAddressesToShow().clear();

        pathToSave = new File("save").getAbsolutePath() + "/LKA/" +
                DateUtil.format(rootLayoutController.getDateFromSelected()) + "-" + DateUtil.format(rootLayoutController.getDateToSelected()) + "/";

        new File(pathToSave).mkdirs();

        fillUpRegionList();

        clearTMAActivityPane();
        fillUpTMAActivityPane();

        fillUpLkaCriteriaList();
    }

    private void fillUpRegionList() {
        clearTMAActivityPane();
        clearCheckBoxes();
        lkaContainer.clear();
        getRegionList().clear();
        getOblList().clear();
        getLkaList().clear();
        getLkaAddressesToShow().clear();

        List<String> regionList;
        regionList = Arrays.stream(new File(pathToSave).listFiles())
                .filter(file -> file.getName().endsWith(".dat"))
                .map(file -> file.getName().replace(".dat", "").replace("_", " "))
                .collect(Collectors.toList());

        if (regionList.size() > 0) {
            getRegionList().addAll(regionList);
        } else {
            getRegionList().addAll("По введенным параметрам нет фото...");
        }
        rootLayoutController.showErrorMessage("");
    }

    /*
    Действия при выборе региона
     */
    private void selectRegion(String region) {
        //TODO: загружать из контейнера (надо сделать) - ready
        regionSelected = region;
        rootLayoutController.showErrorMessage("");
        lkaContainer.clear();
        getOblList().clear();
        getLkaList().clear();
        getLkaAddressesToShow().clear();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(pathToSave + regionSelected.replace(" ", "_") + ".dat"));
            JAXBContext context = JAXBContext.newInstance(LkaContainer.class, LkaAddress.class, Photo.class, TMAActivity.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            lkaContainer = (LkaContainer) unmarshaller.unmarshal(reader);
            reader.close();

        } catch (IOException | JAXBException e) {
            Logger.log(e.getMessage());
            e.printStackTrace();
            rootLayoutController.showErrorMessage("Не удается открыть файл.");
            return;
        }

        List<String> oblList;
        if (showNonChecked.isSelected()) {
            oblList = lkaContainer.getLkaAddresses().stream()
                    .filter(lkaAddress -> lkaAddress.getRegion().equals(regionSelected))
                    .filter(lkaAddress -> lkaAddress.getChecked().equals("0"))
                    .map(lkaAddress -> lkaAddress.getObl())
                    .distinct()
                    .collect(Collectors.toList());
        } else {
            oblList = lkaContainer.getLkaAddresses().stream()
                    .filter(lkaAddress -> lkaAddress.getRegion().equals(regionSelected))
                    .map(lkaAddress -> lkaAddress.getObl())
                    .distinct()
                    .collect(Collectors.toList());
        }

        if (oblList.size() > 0) {
            getOblList().addAll(oblList);
        } else {
            getOblList().addAll("По введенным параметрам нет фото...");
        }
        rootLayoutController.showErrorMessage("");
    }

    /*
    Действия при выборе области
     */
    private void selectObl(String obl) {
        // TODO: загружать из контейнера - ready
        oblSelected = obl;
        rootLayoutController.showErrorMessage("");
        getLkaList().clear();
        getLkaAddressesToShow().clear();

        List<String> lkaList;
        if (showNonChecked.isSelected()) {
            lkaList = lkaContainer.getLkaAddresses().stream()
                    .filter(lkaAddress -> lkaAddress.getRegion().equals(regionSelected))
                    .filter(lkaAddress -> lkaAddress.getObl().equals(oblSelected))
                    .filter(lkaAddress -> lkaAddress.getChecked().equals("0"))
                    .map(lkaAddress -> lkaAddress.getLka())
                    .distinct()
                    .collect(Collectors.toList());
        } else {
            lkaList = lkaContainer.getLkaAddresses().stream()
                    .filter(lkaAddress -> lkaAddress.getRegion().equals(regionSelected))
                    .filter(lkaAddress -> lkaAddress.getObl().equals(oblSelected))
                    .map(lkaAddress -> lkaAddress.getLka())
                    .distinct()
                    .collect(Collectors.toList());
        }

        if (lkaList.size() > 0) {
            getLkaList().addAll(lkaList);
        } else {
            getLkaList().addAll("По введенным параметрам нет фото...");
        }
        rootLayoutController.showErrorMessage("");
    }

    /*
    Действия при выборе сотрудника
     */
    private void selectLka(String lka) {
        // TODO: также загружать из контейнера - ready
        lkaSelected = lka;
        rootLayoutController.showErrorMessage("");
        getLkaAddressesToShow().clear();
        getCheckedRows().clear();

        List<LkaAddress> lkaAddresses;
        if (showNonChecked.isSelected()) {
            lkaAddresses = lkaContainer.getLkaAddresses().stream()
                    .filter(lkaAddress -> lkaAddress.getRegion().equals(regionSelected))
                    .filter(lkaAddress -> lkaAddress.getObl().equals(oblSelected))
                    .filter(lkaAddress -> lkaAddress.getLka().equals(lkaSelected))
                    .filter(lkaAddress -> lkaAddress.getChecked().equals("0"))
                    .collect(Collectors.toList());
        } else {
            lkaAddresses = lkaContainer.getLkaAddresses().stream()
                    .filter(lkaAddress -> lkaAddress.getRegion().equals(regionSelected))
                    .filter(lkaAddress -> lkaAddress.getObl().equals(oblSelected))
                    .filter(lkaAddress -> lkaAddress.getLka().equals(lkaSelected))
                    .collect(Collectors.toList());
        }

        lkaAddressesToShow.addAll(lkaAddresses);
        rootLayoutController.showErrorMessage("");

        setCriteriasToPane(lka);
    }

    @FXML
    public void getDatasFromDataBase() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setContentText("Выгрузка данных из БД займет некоторое время. Вы хотите продолжить?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.CANCEL) {
            return;
        }

        List<String> regionList = new ArrayList<>();
        try {
            regionList = main.getJdbcModel().getDatasForFirstQuery(rootLayoutController.getNetSelected(),
                    rootLayoutController.getDateFromSelected(), rootLayoutController.getDateToSelected());
            rootLayoutController.showErrorMessage("");
        } catch (SQLServerException e) {
            Logger.log(e.getMessage());
            e.printStackTrace();
            rootLayoutController.showExceptionAlert("База не отвечает на запрос. Попробуйте позднее.");
            rootLayoutController.showErrorMessage("Невозможно подключиться к базе данных");
            return;
        } catch (Exception e) {
            Logger.log(e.getMessage());
            e.printStackTrace();
            rootLayoutController.showErrorMessage("Невозможно подключиться к базе данных");
            return;
        }

        if (regionList.size() == 0) {
            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
            alert1.setHeaderText(null);
            alert1.setContentText("По указанным данным фото не найдены");
            alert1.showAndWait();
            return;
        }

        regionList.add(0, "Все");

        ChoiceDialog<String> choiceDialog = new ChoiceDialog<>(regionList.get(0), regionList);
        choiceDialog.setHeaderText("Выберите регион для загрузки");
        choiceDialog.setContentText("Регион:");

        Optional<String> result1 = choiceDialog.showAndWait();
        String region;
        if (result1.isPresent()) {
            region = result1.get();
        } else {
            return;
        }

        if (!region.equals("Все")) {
            List<LkaAddress> allAddressList = new ArrayList<>();
            try {
                allAddressList = (List<LkaAddress>) main.getJdbcModel().getAddresses(region, null);
            } catch (SQLServerException e) {
                Logger.log(e.getMessage());
                e.printStackTrace();
                rootLayoutController.showExceptionAlert("База не отвечает на запрос. Попробуйте позднее.");
                rootLayoutController.showErrorMessage("Невозможно подключиться к базе данных");
                return;
            } catch (Exception e) {
                Logger.log(e.getMessage());
                e.printStackTrace();
                rootLayoutController.showErrorMessage("Невозможно подключиться к базе данных");
                return;
            }

            if (allAddressList.size() == 0) {
                rootLayoutController.showExceptionAlert("Нет адресов с фото");
                return;
            }

            addNewAddressesAndPhotos(allAddressList, region);
        } else {
            for (int i = 1; i < regionList.size(); i++) {
                List<LkaAddress> allAddressList = new ArrayList<>();
                try {
                    allAddressList = (List<LkaAddress>) main.getJdbcModel().getAddresses(regionList.get(i), null);
                    if (allAddressList.size() == 0) continue;
                    addNewAddressesAndPhotos(allAddressList, regionList.get(i));
                } catch (Exception e) {
                    Logger.log(e.getMessage());
                    e.printStackTrace();
                    rootLayoutController.showErrorMessage("Невозможно подключиться к базе данных");
                    continue;
                }
            }
        }

        Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
        alert1.setContentText("Данные успешно добавлены");
        alert1.setHeaderText(null);
        alert1.showAndWait();

        fillUpRegionList();
    }

    @FXML
    public void importDatasFromFile() {
        FileChooser fileChooser = new FileChooser();
        File fileToImport = fileChooser.showOpenDialog(main.getPrimaryStage());

        if (fileToImport == null || !fileToImport.getName().endsWith(".dat")) return;

        LkaContainer newLkaContainer;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileToImport));
            JAXBContext context = JAXBContext.newInstance(LkaContainer.class, LkaAddress.class, Photo.class, TMAActivity.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            newLkaContainer = (LkaContainer) unmarshaller.unmarshal(reader);
            reader.close();

        } catch (IOException | JAXBException e) {
            Logger.log(e.getMessage());
            e.printStackTrace();
            rootLayoutController.showErrorMessage("Не удается открыть файл.");
            return;
        }

        addNewAddressesAndPhotos(newLkaContainer.getLkaAddresses(), fileToImport.getName().replace(".dat", ""));

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Данные успешно добавлены");
        alert.setHeaderText(null);
        alert.showAndWait();

        fillUpRegionList();
    }

    private void addNewAddressesAndPhotos(List<LkaAddress> allAddressesList, String region) {
        LkaContainer lkaContainer = null;

        File saveFile = new File(pathToSave + region.replace(" ", "_") + ".dat");

        if (saveFile.exists()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(saveFile));
                JAXBContext context = JAXBContext.newInstance(LkaContainer.class, LkaAddress.class, Photo.class, TMAActivity.class);
                Unmarshaller unmarshaller = context.createUnmarshaller();

                lkaContainer = (LkaContainer) unmarshaller.unmarshal(reader);
                reader.close();

                for (LkaAddress lkaAddress : allAddressesList) {
                    if (lkaContainer.getLkaAddresses().contains(lkaAddress)) {
                        LkaAddress currentNkaAddress = lkaContainer.getLkaAddresses().get(lkaContainer.getLkaAddresses().indexOf(lkaAddress));
                        for (Photo photo : lkaAddress.getPhotoList()) {
                            if (!currentNkaAddress.getPhotoList().contains(photo)) {
                                photo.setChecked(false);
                                currentNkaAddress.getPhotoList().add(photo);
                                currentNkaAddress.setChecked("0");
                            }
                        }
                    } else {
                        lkaAddress.clearCriterias();
                        lkaAddress.setChecked("0");
                        lkaAddress.getPhotoList().forEach(photo -> photo.setChecked(false));
                        lkaContainer.add(lkaAddress);
                    }
                }
            } catch (IOException | JAXBException e) {
                Logger.log(e.getMessage());
                e.printStackTrace();
                rootLayoutController.showErrorMessage("Не удается открыть файл.");
            }
        } else {
            lkaContainer = new LkaContainer();
            allAddressesList.forEach(lkaAddress -> {
                lkaAddress.clearCriterias();
                lkaAddress.getPhotoList().forEach(photo -> photo.setChecked(false));
                lkaAddress.setChecked("0");
            });
            lkaContainer.addAll(allAddressesList);
            new File(pathToSave).mkdirs();
        }

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(saveFile));
            JAXBContext context = JAXBContext.newInstance(LkaContainer.class, LkaAddress.class, Photo.class, TMAActivity.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(lkaContainer, writer);
            writer.flush();
            writer.close();
        } catch (JAXBException | IOException e) {
            Logger.log(e.getMessage());
            e.printStackTrace();
        }
    }

    private void setCriteriasToPane (String lka) {
        int idSelected = Integer.parseInt(lka.substring(lka.indexOf("(") + 1, lka.length() - 1));

        Set<Integer> idsSet;
        if (lkaCriteriaList != null && lkaCriteriaList.size() > 0) {
            idsSet = lkaCriteriaList.stream()
                    .map(LkaCriteria::getId)
                    .collect(Collectors.toSet());
        } else {
            idsSet = new HashSet<>();
        }
        if (idsSet.contains(idSelected)) {
            LkaCriteria lkaCriteria = lkaCriteriaList.stream().filter(lkaCriteria1 -> lkaCriteria1.getId() == idSelected).findFirst().get();
            param1MZ.setText(lkaCriteria.getCriteria1Text() + " " + (int) (lkaCriteria.getCriteria1Mz() * 100) + "%");
            param1K.setText(lkaCriteria.getCriteria1Text() + " " + (int) (lkaCriteria.getCriteria1K() * 100) + "%");
            param1S.setText(lkaCriteria.getCriteria1Text() + " " + (int) (lkaCriteria.getCriteria1S() * 100) + "%");
            param2MZ.setText(lkaCriteria.getCriteria2Text());
            param2K.setText(lkaCriteria.getCriteria2Text());
            param2S.setText(lkaCriteria.getCriteria2Text());

            if (lkaCriteria.getCriteria1Mz() == 0) {
                setMzGroupVisible(false);
            } else {
                setMzGroupVisible(true);
            }

            if (lkaCriteria.getCriteria1K() == 0 ) {
                setKGroupVisible(false);
            } else {
                setKGroupVisible(true);
            }

            if (lkaCriteria.getCriteria1S() == 0) {
                setSGroupVisible(false);
            } else {
                setSGroupVisible(true);
            }
        } else {
            param1MZ.setText("критерий 1");
            param1K.setText("критерий 1");
            param1S.setText("критерий 1");
            param2MZ.setText("критерий 2");
            param2K.setText("критерий 2");
            param2S.setText("критерий 2");
            setMzGroupVisible(false);
            setKGroupVisible(false);
            setSGroupVisible(false);
        }
    }

    /*
    Очистка заполнения полей
     */
    @FXML
    public void clearCheckBoxes() {
        havePhotoMZ.setSelected(false);
        haveAdditionalProduct.setSelected(false);
        isCorrectPhotoMZ.setSelected(false);
        param1MZ.setSelected(false);
        param2MZ.setSelected(false);
        havePhotoK.setSelected(false);
        isCorrectPhotoK.setSelected(false);
        param1K.setSelected(false);
        param2K.setSelected(false);
        havePhotoS.setSelected(false);
        isCorrectPhotoS.setSelected(false);
        param1S.setSelected(false);
        param2S.setSelected(false);
        oos.setSelected(false);
        comment.setText("");
        changesSavedLabel.setText("");
    }

    @Override
    public void clearCheckedRows() {
        getCheckedRows().clear();
        for (LkaAddress lkaAddress : getLkaAddressesToShow()) {
            for (Photo photo : (ArrayList<Photo>) lkaAddress.getPhotoList()) {
                photo.setChecked(false);
            }
            lkaAddress.setChecked("0");
        }
    }

    private void setCheckBoxesFromFile(LkaAddress lkaAddress) {
        // TODO: загружать из контейнера

        havePhotoMZ.setSelected(lkaAddress.isHavePhotoMZ());
        haveAdditionalProduct.setSelected(lkaAddress.isHaveAdditionalProduct());
        isCorrectPhotoMZ.setSelected(lkaAddress.isCorrectPhotoMZ());
        param1MZ.setSelected(lkaAddress.isParam1MZ());
        param2MZ.setSelected(lkaAddress.isParam2MZ());

        havePhotoK.setSelected(lkaAddress.isHavePhotoK());
        isCorrectPhotoK.setSelected(lkaAddress.isCorrectPhotoK());
        param1K.setSelected(lkaAddress.isParam1K());
        param2K.setSelected(lkaAddress.isParam2K());

        havePhotoS.setSelected(lkaAddress.isHavePhotoS());
        isCorrectPhotoS.setSelected(lkaAddress.isCorrectPhotoS());
        param1S.setSelected(lkaAddress.isParam1S());
        param2S.setSelected(lkaAddress.isParam2S());

        oos.setSelected(lkaAddress.isOos());
        comment.setText(lkaAddress.getComment());

        if (tmaGridPane.getRowConstraints().size() > 1 && lkaAddress.getTmaActivityList() != null && lkaAddress.getTmaActivityList().size() > 0) {
            int activityCount = (tmaGridPane.getChildren().size() - 5) / 8;
            for (int i = 0; i < activityCount; i++) {
                String tgName = ((Label) tmaGridPane.getChildren().get(i * 8 + 6)).getText();
                String tmaName = ((Label) tmaGridPane.getChildren().get(i * 8 + 7)).getText();
                TMAActivity tmaActivity = new TMAActivity(null, null, tgName, tmaName, null, null, false, false);
                if (lkaAddress.getTmaActivityList().contains(tmaActivity)) {
                    TMAActivity savedActivity = lkaAddress.getTmaActivityList().get(lkaAddress.getTmaActivityList().indexOf(tmaActivity));
                    ((CheckBox) tmaGridPane.getChildren().get(i * 8 + 8)).setSelected(savedActivity.isSalePrice());
                    ((CheckBox) tmaGridPane.getChildren().get(i * 8 + 9)).setSelected(savedActivity.isHasOos());
                }
            }
        }
    }

    @FXML
    private void saveAddress() {

        int addressIndex = addressTable.getSelectionModel().getSelectedIndex();
        if (addressIndex == -1) {
            rootLayoutController.showErrorMessage("Не выбрана точка для сохранения");
            return;
        }

        LkaAddress lkaAddressSelected = addressTable.getSelectionModel().getSelectedItem();

        LkaAddress lkaAddressCurrent = lkaContainer.get(lkaContainer.getLkaAddresses().indexOf(lkaAddressSelected));

        lkaAddressCurrent.setAllCriterias(haveMz, havePhotoMZ.isSelected(), haveAdditionalProduct.isSelected(), isCorrectPhotoMZ.isSelected(), param1MZ.isSelected(), param2MZ.isSelected(),
                haveK, havePhotoK.isSelected(), isCorrectPhotoK.isSelected(), param1K.isSelected(), param2K.isSelected(),
                haveS, havePhotoS.isSelected(), isCorrectPhotoS.isSelected(), param1S.isSelected(), param2S.isSelected(),
                oos.isSelected(), comment.getText() == null ? "" : comment.getText().replace("\n", " "));
        ArrayList<TMAActivity> tmaActivities = new ArrayList<>();
        if (tmaGridPane.getRowConstraints().size() > 1) {
            int activityCount = (tmaGridPane.getChildren().size() - 5) / 8;
            for (int i = 0; i < activityCount; i++) {
                String tgName = ((Label) tmaGridPane.getChildren().get(i * 8 + 6)).getText();
                String tmaName = ((Label) tmaGridPane.getChildren().get(i * 8 + 7)).getText();
                boolean salePrice = ((CheckBox) tmaGridPane.getChildren().get(i * 8 + 8)).isSelected();
                boolean oos = ((CheckBox) tmaGridPane.getChildren().get(i * 8 + 9)).isSelected();
                TMAActivity activity = new TMAActivity(null, null, tgName, tmaName, null, null, salePrice, oos);
                tmaActivities.add(activity);
            }
        }
        lkaAddressCurrent.setTmaActivityList(tmaActivities);
        lkaAddressCurrent.setChecked("1");
        lkaAddressSelected.setChecked("1");
        for (Photo photo : (ArrayList<Photo>) lkaAddressCurrent.getPhotoList()) {
            photo.setChecked(true);
        }
        for (Node node : tilePane.getChildren()) {
            VBox vBox = (VBox) node;
            vBox.getChildren().get(1).setStyle("-fx-background-color: lightgreen");
        }

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(pathToSave + regionSelected.replace(" ", "_") + ".dat"));
            JAXBContext context = JAXBContext.newInstance(LkaContainer.class, LkaAddress.class, Photo.class, TMAActivity.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(lkaContainer, writer);
            writer.flush();
            writer.close();
        } catch (JAXBException | IOException e) {
            Logger.log(e.getMessage());
            e.printStackTrace();
        }

        changesSavedLabel.setText("Изменения сохранены");
    }

    private void setMZGroupEnable(boolean isEnable) {
        haveAdditionalProduct.setDisable(!isEnable);
        isCorrectPhotoMZ.setDisable(!isEnable);
        param1MZ.setDisable(!isEnable);
        param2MZ.setDisable(!isEnable);

        if (!isEnable) {
            haveAdditionalProduct.setSelected(false);
            isCorrectPhotoMZ.setSelected(false);
            param1MZ.setSelected(false);
            param2MZ.setSelected(false);
        }
    }

    private void setKGroupEnable(boolean isEnable) {
        isCorrectPhotoK.setDisable(!isEnable);
        param1K.setDisable(!isEnable);
        param2K.setDisable(!isEnable);

        if (!isEnable) {
            isCorrectPhotoK.setSelected(false);
            param1K.setSelected(false);
            param2K.setSelected(false);
        }
    }

    private void setSGroupEnable(boolean isEnable) {
        isCorrectPhotoS.setDisable(!isEnable);
        param1S.setDisable(!isEnable);
        param2S.setDisable(!isEnable);

        if (!isEnable) {
            isCorrectPhotoS.setSelected(false);
            param1S.setSelected(false);
            param2S.setSelected(false);
        }
    }

    private void setMzGroupVisible (boolean isVisible) {
        mzLabel.setVisible(isVisible);
        havePhotoMZ.setVisible(isVisible);
        haveAdditionalProduct.setVisible(isVisible);
        isCorrectPhotoMZ.setVisible(isVisible);
        param1MZ.setVisible(isVisible);
        param2MZ.setVisible(isVisible);
        haveMz = isVisible;
    }

    private void setKGroupVisible (boolean isVisible) {
        kLabel.setVisible(isVisible);
        havePhotoK.setVisible(isVisible);
        isCorrectPhotoK.setVisible(isVisible);
        param1K.setVisible(isVisible);
        param2K.setVisible(isVisible);
        haveK = isVisible;
    }

    private void setSGroupVisible (boolean isVisible) {
        sLabel.setVisible(isVisible);
        havePhotoS.setVisible(isVisible);
        isCorrectPhotoS.setVisible(isVisible);
        param1S.setVisible(isVisible);
        param2S.setVisible(isVisible);
        haveS = isVisible;
    }

    private void clearTMAActivityPane() {
        for (int i = tmaGridPane.getChildren().size() - 1; i > 4; i--) {
            tmaGridPane.getChildren().remove(i);
        }

        for (int i = tmaGridPane.getRowConstraints().size() - 1; i > 0; i--) {
            tmaGridPane.getRowConstraints().remove(i);
        }
    }

    private void fillUpTMAActivityPane() {
        // TODO: загружать из файла
        ApachePoi apachePoi = ApachePoiManager.getInstance();
        ArrayList<TMAActivity> tmaList = null;
        try {
            tmaList = apachePoi.getTMAActivityFromFile(rootLayoutController.getPhotoTypeSelected(), rootLayoutController.getNetSelected(),
                    rootLayoutController.getDateFromSelected(), rootLayoutController.getDateToSelected());
        } catch (IOException e) {
            Logger.log(e.getMessage());
            e.printStackTrace();
            rootLayoutController.showErrorMessage("Не удалось загрузить акции");
            return;
        }

        if (tmaList == null || tmaList.size() == 0) {
            tmaGridPane.add(new Label("Акции по введенным параметрам не проводятся"), 1, 1);
            return;
        }

        for (int i = 0; i < tmaList.size(); i++) {
            RowConstraints rowCon = new RowConstraints(10, 25, 25);
            tmaGridPane.getRowConstraints().add(i + 1, rowCon);
            Separator separator = new Separator();
            separator.setPrefWidth(200);
            separator.setStyle("-fx-background-color: grey;");
            tmaGridPane.add(separator, 0, i, 7, 2);
            Label tgLabel = new Label(tmaList.get(i).getTg());
            tgLabel.setDisable(true);
            tmaGridPane.add(tgLabel, 0, i + 1);
            Label activityNameLabel = new Label(tmaList.get(i).getActivityName());
            activityNameLabel.setDisable(true);
            tmaGridPane.add(activityNameLabel, 1, i + 1);
            CheckBox checkBox = new CheckBox();
            checkBox.setMnemonicParsing(false);
            checkBox.setDisable(true);
            tmaGridPane.add(checkBox, 2, i + 1);
            CheckBox checkBox1 = new CheckBox();
            checkBox1.setMnemonicParsing(false);
            checkBox1.setDisable(true);
            tmaGridPane.add(checkBox1, 3, i + 1);
            Label startDate = new Label(DateUtil.format(tmaList.get(i).getActivityStartDate()));
            startDate.setDisable(true);
            tmaGridPane.add(startDate, 4, i + 1);
            Label defis = new Label("-");
            defis.setDisable(true);
            tmaGridPane.add(defis, 4, i + 1, 2, 1);
            Label endDate = new Label(DateUtil.format(tmaList.get(i).getActivityEndDate()));
            endDate.setDisable(true);
            tmaGridPane.add(endDate, 5, i + 1);
        }
    }

    private void enableTMAbyTG(String tg, boolean isEnable) {
        int activityCount = (tmaGridPane.getChildren().size() - 5) / 8;
        for (int i = 0; i < activityCount; i++) {
            String tgName = ((Label) tmaGridPane.getChildren().get(i * 8 + 6)).getText();
            if (tgName.equals(tg)) {
                for (int j = i * 8 + 6; j <= i * 8 + 12; j++) {
                    tmaGridPane.getChildren().get(j).setDisable(!isEnable);
                }
                if (!isEnable) {
                    ((CheckBox) tmaGridPane.getChildren().get(i * 8 + 8)).setSelected(false);
                    ((CheckBox) tmaGridPane.getChildren().get(i * 8 + 9)).setSelected(false);
                }
            }
        }
    }

    private void fillUpLkaCriteriaList() {
        ApachePoi apachePoi = ApachePoiManager.getInstance();
        try {
            lkaCriteriaList = apachePoi.getLkaCriteriaList();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
