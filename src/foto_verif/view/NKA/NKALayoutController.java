package foto_verif.view.NKA;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import foto_verif.Main;
import foto_verif.model.AddressTT;
import foto_verif.model.NkaAddressTT;
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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class NKALayoutController implements ChannelLayoutController {
    @FXML
    private ChoiceBox<String> regionChoice;
    @FXML
    private ChoiceBox<String> oblChoice;
    @FXML
    private ChoiceBox<String> mlkaChoice;
    @FXML
    private CheckBox showNonChecked;
    @FXML
    private TableView<NkaAddress> addressTable;
    @FXML
    private TableColumn<NkaAddress, String> addressColumn;
    @FXML
    private TableColumn<NkaAddress, String> checkedColumn;

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private TilePane tilePane;

    @FXML
    private Label selectedAddress;
    @FXML
    private CheckBox havePhotoMZ;
    @FXML
    private CheckBox isCorrectPhotoMZ;
    @FXML
    private CheckBox centerPhotoMZ;
    @FXML
    private CheckBox _30PhotoMZ;
    @FXML
    private CheckBox vertPhotoMZ;
    @FXML
    private CheckBox havePhotoK;
    @FXML
    private CheckBox isCorrectPhotoK;
    @FXML
    private CheckBox centerPhotoK;
    @FXML
    private CheckBox _30PhotoK;
    @FXML
    private CheckBox vertPhotoK;
    @FXML
    private CheckBox havePhotoS;
    @FXML
    private CheckBox isCorrectPhotoS;
    @FXML
    private CheckBox centerPhotoS;
    @FXML
    private CheckBox _30PhotoS;
    @FXML
    private CheckBox vertPhotoS;
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
    private ObservableList<String> mlkaList = FXCollections.observableArrayList();
    private ObservableList<NkaAddress> nkaAddressesToShow = FXCollections.observableArrayList();

    private NkaContainer nkaContainer = new NkaContainer();

    private String regionSelected = null;
    private String oblSelected = null;
    private String mlkaSelected = null;

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

    public ObservableList<String> getMlkaList() {
        return mlkaList;
    }

    public ObservableList<NkaAddress> getNkaAddressesToShow() {
        return nkaAddressesToShow;
    }

    public NKALayoutController() {
    }

    @FXML
    private void initialize() {

        // запрет сортировки точек в таблице
        //addressColumn.setSortable(false);
        //checkedColumn.setSortable(false);

        // Отслеживание выбора таблицы
        addressColumn.setCellValueFactory(cellData -> cellData.getValue().addressProperty());
        checkedColumn.setCellValueFactory(cellData -> cellData.getValue().checkedProperty());

        addressTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            showAddressPhotos(null);
            showAddressPhotos(newValue);

            if (newValue == null) {
                getCheckedRows().clear();
            }
            selectedAddress.setText("");
            if (newValue != null) {
                selectedAddress.setText(newValue.getAddress());
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
        mlkaChoice.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.startsWith("По введенным")) {
                rootLayoutController.showErrorMessage("Загружаются данные. Ждите...");
                Platform.runLater(() -> selectMlka(newValue));
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
    private void showAddressPhotos(NkaAddress address) {
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

                        //TODO: добавить прокрутку влево-вправо - выполнено
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
        addressTable.setItems(getNkaAddressesToShow());
        regionChoice.setItems(getRegionList());
        oblChoice.setItems(getOblList());
        mlkaChoice.setItems(getMlkaList());
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
        clearTMAActivityPane();
        clearCheckBoxes();
        nkaContainer.clear();
        getRegionList().clear();
        getOblList().clear();
        getNkaAddressesToShow().clear();

        pathToSave = new File("save").getAbsolutePath() + "/NKA/" + rootLayoutController.getNetSelected() + "/" +
                DateUtil.format(rootLayoutController.getDateFromSelected()) + "-" + DateUtil.format(rootLayoutController.getDateToSelected()) + "/";

        new File(pathToSave).mkdirs();

        //TODO: загружать данные по областям из файла сохранения - ready

        fillUpRegionList();

        /*try {
            List<String> regionList = main.getJdbcModel().getDatasForFirstQuery(rootLayoutController.getNetSelected(),
                    rootLayoutController.getDateFromSelected(), rootLayoutController.getDateToSelected());
            if (regionList.size() > 0) {
                getRegionList().addAll(regionList);
            } else {
                getRegionList().addAll("По введенным параметрам нет фото...");
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
        }*/

        if (rootLayoutController.getPhotoTypeSelected() == 1) {
            if (rootLayoutController.getNetSelected().equals("X5")) {
                _30PhotoMZ.setText("min 2 SKU");
                vertPhotoMZ.setText("загруженность полки");
                _30PhotoK.setText("min 2 SKU");
                vertPhotoK.setText("загруженность полки");
                _30PhotoS.setText("min 2 SKU");
                vertPhotoS.setText("загруженность полки");
            } else {
                _30PhotoMZ.setText("30% полки");
                vertPhotoMZ.setText("верт. блок");
                _30PhotoK.setText("30% полки");
                vertPhotoK.setText("верт. блок");
                _30PhotoS.setText("30% полки");
                vertPhotoS.setText("верт. блок");
            }
        }
        clearTMAActivityPane();
        fillUpTMAActivityPane();
    }

    private void fillUpRegionList() {
        clearTMAActivityPane();
        clearCheckBoxes();
        nkaContainer.clear();
        getRegionList().clear();
        getOblList().clear();
        getMlkaList().clear();
        getNkaAddressesToShow().clear();

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
        nkaContainer.clear();
        getOblList().clear();
        getMlkaList().clear();
        getNkaAddressesToShow().clear();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(pathToSave + regionSelected.replace(" ", "_") + ".dat"));
            JAXBContext context = JAXBContext.newInstance(NkaContainer.class, NkaAddress.class, Photo.class, TMAActivity.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            nkaContainer = (NkaContainer) unmarshaller.unmarshal(reader);
            reader.close();

        } catch (IOException | JAXBException e) {
            Logger.log(e.getMessage());
            e.printStackTrace();
            rootLayoutController.showErrorMessage("Не удается открыть файл.");
            return;
        }

        List<String> oblList;
        if (showNonChecked.isSelected()) {
            oblList = nkaContainer.getNkaAddresses().stream()
                    .filter(nkaAddress -> nkaAddress.getRegion().equals(regionSelected))
                    .filter(nkaAddress -> nkaAddress.getChecked().equals("0"))
                    .map(nkaAddress -> nkaAddress.getObl())
                    .distinct()
                    .collect(Collectors.toList());
        } else {
            oblList = nkaContainer.getNkaAddresses().stream()
                    .filter(nkaAddress -> nkaAddress.getRegion().equals(regionSelected))
                    .map(nkaAddress -> nkaAddress.getObl())
                    .distinct()
                    .collect(Collectors.toList());
        }

        if (oblList.size() > 0) {
            getOblList().addAll(oblList);
        } else {
            getOblList().addAll("По введенным параметрам нет фото...");
        }
        rootLayoutController.showErrorMessage("");

        /*try {
            List<String> oblList = main.getJdbcModel().getOblsList(region);
            if (oblList.size() > 0) {
                getOblList().addAll(oblList);
            } else {
                getOblList().addAll("По введенным параметрам нет фото...");
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
        }*/
    }

    /*
    Действия при выборе области
     */
    private void selectObl(String obl) {
        // TODO: загружать из контейнера - ready
        oblSelected = obl;
        rootLayoutController.showErrorMessage("");
        getMlkaList().clear();
        getNkaAddressesToShow().clear();

        List<String> mlkaList;
        if (showNonChecked.isSelected()) {
            mlkaList = nkaContainer.getNkaAddresses().stream()
                    .filter(nkaAddress -> nkaAddress.getRegion().equals(regionSelected))
                    .filter(nkaAddress -> nkaAddress.getObl().equals(oblSelected))
                    .filter(nkaAddress -> nkaAddress.getChecked().equals("0"))
                    .map(nkaAddress -> nkaAddress.getMlka())
                    .distinct()
                    .collect(Collectors.toList());
        } else {
            mlkaList = nkaContainer.getNkaAddresses().stream()
                    .filter(nkaAddress -> nkaAddress.getRegion().equals(regionSelected))
                    .filter(nkaAddress -> nkaAddress.getObl().equals(oblSelected))
                    .map(nkaAddress -> nkaAddress.getMlka())
                    .distinct()
                    .collect(Collectors.toList());
        }

        if (mlkaList.size() > 0) {
            getMlkaList().addAll(mlkaList);
        } else {
            getMlkaList().addAll("По введенным параметрам нет фото...");
        }
        rootLayoutController.showErrorMessage("");

        /*try {
            List<String> mlkaList = main.getJdbcModel().getMlkaList(obl);
            if (mlkaList.size() > 0) {
                getMlkaList().addAll(mlkaList);
            } else {
                getMlkaList().addAll("По введенным параметрам нет фото...");
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
        }*/
    }

    /*
    Действия при выборе сотрудника
     */
    private void selectMlka(String mlka) {
        // TODO: также загружать из контейнера - ready
        mlkaSelected = mlka;
        rootLayoutController.showErrorMessage("");
        getNkaAddressesToShow().clear();
        getCheckedRows().clear();

        List<NkaAddress> nkaAddresses;
        if (showNonChecked.isSelected()) {
            nkaAddresses = nkaContainer.getNkaAddresses().stream()
                    .filter(nkaAddress -> nkaAddress.getRegion().equals(regionSelected))
                    .filter(nkaAddress -> nkaAddress.getObl().equals(oblSelected))
                    .filter(nkaAddress -> nkaAddress.getMlka().equals(mlkaSelected))
                    .filter(nkaAddress -> nkaAddress.getChecked().equals("0"))
                    .collect(Collectors.toList());
        } else {
            nkaAddresses = nkaContainer.getNkaAddresses().stream()
                    .filter(nkaAddress -> nkaAddress.getRegion().equals(regionSelected))
                    .filter(nkaAddress -> nkaAddress.getObl().equals(oblSelected))
                    .filter(nkaAddress -> nkaAddress.getMlka().equals(mlkaSelected))
                    .collect(Collectors.toList());
        }

        nkaAddressesToShow.addAll(nkaAddresses);
        rootLayoutController.showErrorMessage("");

        /*try {
            main.getJdbcModel().getAddresses(mlka, null);
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
        }*/
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
            List<NkaAddress> allAddressList = new ArrayList<>();
            try {
                allAddressList = (List<NkaAddress>) main.getJdbcModel().getAddresses(region, null);
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
                List<NkaAddress> allAddressList = new ArrayList<>();
                try {
                    allAddressList = (List<NkaAddress>) main.getJdbcModel().getAddresses(regionList.get(i), null);
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

        /*if (!new File(pathToSave + fileToImport.getName()).exists()) {
            try {
                Files.copy(Paths.get(fileToImport.getAbsolutePath()), Paths.get(pathToSave + fileToImport.getName()));
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setContentText("Файл успешно импортирован");
                alert.showAndWait();
                return;
            } catch (IOException e) {
                rootLayoutController.showExceptionAlert("Не удалось импортировать файл");
                return;
            }
        }*/

        NkaContainer newNkaContainer;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileToImport));
            JAXBContext context = JAXBContext.newInstance(NkaContainer.class, NkaAddress.class, Photo.class, TMAActivity.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            newNkaContainer = (NkaContainer) unmarshaller.unmarshal(reader);
            reader.close();

        } catch (IOException | JAXBException e) {
            Logger.log(e.getMessage());
            e.printStackTrace();
            rootLayoutController.showErrorMessage("Не удается открыть файл.");
            return;
        }

        addNewAddressesAndPhotos(newNkaContainer.getNkaAddresses(), fileToImport.getName().replace(".dat", ""));

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Данные успешно добавлены");
        alert.setHeaderText(null);
        alert.showAndWait();

        fillUpRegionList();
    }

    private void addNewAddressesAndPhotos(List<NkaAddress> allAddressesList, String region) {
        NkaContainer nkaContainer = null;

        File saveFile = new File(pathToSave + region.replace(" ", "_") + ".dat");

        if (saveFile.exists()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(saveFile));
                JAXBContext context = JAXBContext.newInstance(NkaContainer.class, NkaAddress.class, Photo.class, TMAActivity.class);
                Unmarshaller unmarshaller = context.createUnmarshaller();

                nkaContainer = (NkaContainer) unmarshaller.unmarshal(reader);
                reader.close();

                for (NkaAddress nkaAddress : allAddressesList) {
                    if (nkaContainer.getNkaAddresses().contains(nkaAddress)) {
                        NkaAddress currentNkaAddress = nkaContainer.getNkaAddresses().get(nkaContainer.getNkaAddresses().indexOf(nkaAddress));
                        for (Photo photo : nkaAddress.getPhotoList()) {
                            if (!currentNkaAddress.getPhotoList().contains(photo)) {
                                photo.setChecked(false);
                                currentNkaAddress.getPhotoList().add(photo);
                                currentNkaAddress.setChecked("0");
                            }
                        }
                    } else {
                        nkaAddress.clearCriterias();
                        nkaAddress.setChecked("0");
                        nkaAddress.getPhotoList().forEach(photo -> photo.setChecked(false));
                        nkaContainer.add(nkaAddress);
                    }
                }
            } catch (IOException | JAXBException e) {
                Logger.log(e.getMessage());
                e.printStackTrace();
                rootLayoutController.showErrorMessage("Не удается открыть файл.");
            }
        } else {
            nkaContainer = new NkaContainer();
            allAddressesList.forEach(nkaAddress -> {
                nkaAddress.clearCriterias();
                nkaAddress.getPhotoList().forEach(photo -> photo.setChecked(false));
                nkaAddress.setChecked("0");
            });
            nkaContainer.addAll(allAddressesList);
            new File(pathToSave).mkdirs();
        }

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(saveFile));
            JAXBContext context = JAXBContext.newInstance(NkaContainer.class, NkaAddress.class, Photo.class, TMAActivity.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(nkaContainer, writer);
            writer.flush();
            writer.close();
        } catch (JAXBException | IOException e) {
            Logger.log(e.getMessage());
            e.printStackTrace();
        }
    }

    /*
    Очистка заполнения полей
     */
    @FXML
    public void clearCheckBoxes() {
        havePhotoMZ.setSelected(false);
        isCorrectPhotoMZ.setSelected(false);
        centerPhotoMZ.setSelected(false);
        _30PhotoMZ.setSelected(false);
        vertPhotoMZ.setSelected(false);
        havePhotoK.setSelected(false);
        isCorrectPhotoK.setSelected(false);
        centerPhotoK.setSelected(false);
        _30PhotoK.setSelected(false);
        vertPhotoK.setSelected(false);
        havePhotoS.setSelected(false);
        isCorrectPhotoS.setSelected(false);
        centerPhotoS.setSelected(false);
        _30PhotoS.setSelected(false);
        vertPhotoS.setSelected(false);
        oos.setSelected(false);
        comment.setText("");
        changesSavedLabel.setText("");
    }

    @Override
    public void clearCheckedRows() {
        getCheckedRows().clear();
        for (NkaAddress nkaAddress : getNkaAddressesToShow()) {
            for (Photo photo : (ArrayList<Photo>) nkaAddress.getPhotoList()) {
                photo.setChecked(false);
            }
            nkaAddress.setChecked("0");
        }
    }

    private void setCheckBoxesFromFile(NkaAddress nkaAddress) {
        // TODO: загружать из контейнера

        havePhotoMZ.setSelected(nkaAddress.isHavePhotoMZ());
        isCorrectPhotoMZ.setSelected(nkaAddress.isCorrectPhotoMZ());
        centerPhotoMZ.setSelected(nkaAddress.isCenterPhotoMZ());
        _30PhotoMZ.setSelected(nkaAddress.is_30PhotoMZ());
        vertPhotoMZ.setSelected(nkaAddress.isVertPhotoMZ());

        havePhotoK.setSelected(nkaAddress.isHavePhotoK());
        isCorrectPhotoK.setSelected(nkaAddress.isCorrectPhotoK());
        centerPhotoK.setSelected(nkaAddress.isCenterPhotoK());
        _30PhotoK.setSelected(nkaAddress.is_30PhotoK());
        vertPhotoK.setSelected(nkaAddress.isVertPhotoK());

        havePhotoS.setSelected(nkaAddress.isHavePhotoS());
        isCorrectPhotoS.setSelected(nkaAddress.isCorrectPhotoS());
        centerPhotoS.setSelected(nkaAddress.isCenterPhotoS());
        _30PhotoS.setSelected(nkaAddress.is_30PhotoS());
        vertPhotoS.setSelected(nkaAddress.isVertPhotoS());

        oos.setSelected(nkaAddress.isOos());
        comment.setText(nkaAddress.getComment());

        if (tmaGridPane.getRowConstraints().size() > 1 && nkaAddress.getTmaActivityList() != null && nkaAddress.getTmaActivityList().size() > 0) {
            int activityCount = (tmaGridPane.getChildren().size() - 5) / 8;
            for (int i = 0; i < activityCount; i++) {
                String tgName = ((Label) tmaGridPane.getChildren().get(i * 8 + 6)).getText();
                String tmaName = ((Label) tmaGridPane.getChildren().get(i * 8 + 7)).getText();
                TMAActivity tmaActivity = new TMAActivity(null, null, tgName, tmaName, null, null, false, false);
                if (nkaAddress.getTmaActivityList().contains(tmaActivity)) {
                    TMAActivity savedActivity = nkaAddress.getTmaActivityList().get(nkaAddress.getTmaActivityList().indexOf(tmaActivity));
                    ((CheckBox) tmaGridPane.getChildren().get(i * 8 + 8)).setSelected(savedActivity.isSalePrice());
                    ((CheckBox) tmaGridPane.getChildren().get(i * 8 + 9)).setSelected(savedActivity.isHasOos());
                }
            }
        }

        /*String tmpMlkaSelected = mlkaSelected.endsWith(".") ? mlkaSelected.substring(0, mlkaSelected.length() - 1) : mlkaSelected;
        String path = ("save/" + rootLayoutController.getPhotoTypeSelected() + "/" + rootLayoutController.getNetSelected() + "/" +
                DateUtil.format(rootLayoutController.getDateFromSelected()) + "-" + DateUtil.format(rootLayoutController.getDateToSelected()) + "/" + regionSelected + "/" + tmpMlkaSelected).replace(" ", "_") +
                "/" + addressTable.getSelectionModel().getSelectedItem().getAddress().replace("/", "к") + ".txt";
        String path1 = ("save/" + rootLayoutController.getPhotoTypeSelected() + "/" + rootLayoutController.getNetSelected() + "/" +
                DateUtil.format(rootLayoutController.getDateFromSelected()) + "-" + DateUtil.format(rootLayoutController.getDateToSelected()) + "/" + regionSelected + "/" + tmpMlkaSelected).replace(" ", "_") +
                "/" + addressTable.getSelectionModel().getSelectedItem().getAddress().replace("/", "к") + ".adr";
        if (new File(path1).exists()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(path1));
                JAXBContext context = JAXBContext.newInstance(NkaAddressTT.class, Photo.class, TMAActivity.class);
                Unmarshaller unmarshaller = context.createUnmarshaller();
                NkaAddressTT nkaAddressTT = (NkaAddressTT) unmarshaller.unmarshal(reader);
                reader.close();
                noPhotoMZ.setSelected(nkaAddressTT.isNoPhotoMZ());
                incorrectPhotoMZ.setSelected(nkaAddressTT.isIncorrectPhotoMZ());
                centerPhotoMZ.setSelected(nkaAddressTT.isCenterPhotoMZ());
                _30PhotoMZ.setSelected(nkaAddressTT.is_30PhotoMZ());
                vertPhotoMZ.setSelected(nkaAddressTT.isVertPhotoMZ());

                noPhotoK.setSelected(nkaAddressTT.isNoPhotoK());
                incorrectPhotoK.setSelected(nkaAddressTT.isIncorrectPhotoK());
                centerPhotoK.setSelected(nkaAddressTT.isCenterPhotoK());
                _30PhotoK.setSelected(nkaAddressTT.is_30PhotoK());
                vertPhotoK.setSelected(nkaAddressTT.isVertPhotoK());

                noPhotoS.setSelected(nkaAddressTT.isNoPhotoS());
                incorrectPhotoS.setSelected(nkaAddressTT.isIncorrectPhotoS());
                centerPhotoS.setSelected(nkaAddressTT.isCenterPhotoS());
                _30PhotoS.setSelected(nkaAddressTT.is_30PhotoS());
                vertPhotoS.setSelected(nkaAddressTT.isVertPhotoS());

                oos.setSelected(nkaAddressTT.isOos());
                comment.setText(nkaAddressTT.getComment());

                if (tmaGridPane.getRowConstraints().size() > 1 && nkaAddressTT.getTmaActivityList() != null && nkaAddressTT.getTmaActivityList().size() > 0) {
                    int activityCount = (tmaGridPane.getChildren().size() - 5) / 8;
                    for (int i = 0; i < activityCount; i++) {
                        String tgName = ((Label) tmaGridPane.getChildren().get(i * 8 + 6)).getText();
                        String tmaName = ((Label) tmaGridPane.getChildren().get(i * 8 + 7)).getText();
                        TMAActivity tmaActivity = new TMAActivity(null, null, tgName, tmaName, null, null, false, false);
                        if (nkaAddressTT.getTmaActivityList().contains(tmaActivity)) {
                            TMAActivity savedActivity = nkaAddressTT.getTmaActivityList().get(nkaAddressTT.getTmaActivityList().indexOf(tmaActivity));
                            ((CheckBox) tmaGridPane.getChildren().get(i * 8 + 8)).setSelected(savedActivity.isSalePrice());
                            ((CheckBox) tmaGridPane.getChildren().get(i * 8 + 9)).setSelected(savedActivity.isHasOos());
                        }
                    }
                }
            } catch (IOException | JAXBException e) {
                Logger.log(e.getMessage());
                e.printStackTrace();
            }
        } else {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(path));
                String datas = reader.readLine();
                noPhotoMZ.setSelected(datas.substring(0, 1).equals("1"));
                incorrectPhotoMZ.setSelected(datas.substring(1, 2).equals("1"));
                centerPhotoMZ.setSelected(datas.substring(2, 3).equals("1"));
                _30PhotoMZ.setSelected(datas.substring(3, 4).equals("1"));
                vertPhotoMZ.setSelected(datas.substring(4, 5).equals("1"));

                noPhotoK.setSelected(datas.substring(5, 6).equals("1"));
                incorrectPhotoK.setSelected(datas.substring(6, 7).equals("1"));
                centerPhotoK.setSelected(datas.substring(7, 8).equals("1"));
                _30PhotoK.setSelected(datas.substring(8, 9).equals("1"));
                vertPhotoK.setSelected(datas.substring(9, 10).equals("1"));

                noPhotoS.setSelected(datas.substring(10, 11).equals("1"));
                incorrectPhotoS.setSelected(datas.substring(11, 12).equals("1"));
                centerPhotoS.setSelected(datas.substring(12, 13).equals("1"));
                _30PhotoS.setSelected(datas.substring(13, 14).equals("1"));
                vertPhotoS.setSelected(datas.substring(14, 15).equals("1"));
                String oosChecked = reader.readLine();
                oos.setSelected(oosChecked.equals("1"));
                String comm = reader.readLine();
                if (comm == null)
                    comm = "";
                comment.setText(comm);
                reader.close();
            } catch (IOException e) {
                Logger.log(e.getMessage());
                e.printStackTrace();
            }
        }*/
    }

    @FXML
    private void saveAddress() {

        int addressIndex = addressTable.getSelectionModel().getSelectedIndex();
        if (addressIndex == -1) {
            rootLayoutController.showErrorMessage("Не выбрана точка для сохранения");
            return;
        }

        NkaAddress nkaAddressSelected = addressTable.getSelectionModel().getSelectedItem();

        NkaAddress nkaAddressCurrent = nkaContainer.get(nkaContainer.getNkaAddresses().indexOf(nkaAddressSelected));

        // TODO: сохранять в один файл сохранения
        /*String tmpMlkaSelected = mlkaSelected.endsWith(".") ? mlkaSelected.substring(0, mlkaSelected.length() - 1) : mlkaSelected;
        int addressIndex = addressTable.getSelectionModel().getSelectedIndex();
        if (addressIndex == -1) {
            rootLayoutController.showErrorMessage("Не выбрана точка для сохранения");
            return;
        }
        AddressTT addressTT = getAddresses().get(addressIndex);
        String path = "save/" + rootLayoutController.getPhotoTypeSelected() + "/" + rootLayoutController.getNetSelected() + "/" +
                DateUtil.format(rootLayoutController.getDateFromSelected()) + "-" + DateUtil.format(rootLayoutController.getDateToSelected()) + "/" + regionSelected +
                "/" + tmpMlkaSelected;
        path = path.replace(" ", "_");
        new File(path).mkdirs();
        //File file = new File(path + "/" + addressTT.getAddress().replace("/", "к") + ".txt");
        File file1 = new File(path + "/" + addressTT.getAddress().replace("/", "к") + ".adr");

        NkaAddressTT nkaAddressTT = new NkaAddressTT();
        nkaAddressTT.copyFromAddressTT(addressTT);*/

        nkaAddressCurrent.setAllCriterias(havePhotoMZ.isSelected(), isCorrectPhotoMZ.isSelected(), centerPhotoMZ.isSelected(),
                _30PhotoMZ.isSelected(), vertPhotoMZ.isSelected(),
                havePhotoK.isSelected(), isCorrectPhotoK.isSelected(), centerPhotoK.isSelected(),
                _30PhotoK.isSelected(), vertPhotoK.isSelected(),
                havePhotoS.isSelected(), isCorrectPhotoS.isSelected(), centerPhotoS.isSelected(),
                _30PhotoS.isSelected(), vertPhotoS.isSelected(),
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
        nkaAddressCurrent.setTmaActivityList(tmaActivities);
        nkaAddressCurrent.setChecked("1");
        nkaAddressSelected.setChecked("1");
        for (Photo photo : (ArrayList<Photo>) nkaAddressCurrent.getPhotoList()) {
            photo.setChecked(true);
        }
        for (Node node : tilePane.getChildren()) {
            VBox vBox = (VBox) node;
            vBox.getChildren().get(1).setStyle("-fx-background-color: lightgreen");
        }

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(pathToSave + regionSelected.replace(" ", "_") + ".dat"));
            JAXBContext context = JAXBContext.newInstance(NkaContainer.class, NkaAddress.class, Photo.class, TMAActivity.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(nkaContainer, writer);
            writer.flush();
            writer.close();
        } catch (JAXBException | IOException e) {
            Logger.log(e.getMessage());
            e.printStackTrace();
        }

        /*addressTT.setChecked("1");
        getCheckedRows().add(addressTable.getSelectionModel().getSelectedIndex());
        for (Photo photo : (ArrayList<Photo>) addressTT.getPhotoList()) {
            photo.setChecked(true);
        }
        for (Node node : tilePane.getChildren()) {
            VBox vBox = (VBox) node;
            vBox.getChildren().get(1).setStyle("-fx-background-color: lightgreen");
        }*/
        changesSavedLabel.setText("Изменения сохранены");
    }

    private void setMZGroupEnable(boolean isEnable) {
        isCorrectPhotoMZ.setDisable(!isEnable);
        centerPhotoMZ.setDisable(!isEnable);
        _30PhotoMZ.setDisable(!isEnable);
        vertPhotoMZ.setDisable(!isEnable);

        if (!isEnable) {
            isCorrectPhotoMZ.setSelected(false);
            centerPhotoMZ.setSelected(false);
            _30PhotoMZ.setSelected(false);
            vertPhotoMZ.setSelected(false);
        }
    }

    private void setKGroupEnable(boolean isEnable) {
        isCorrectPhotoK.setDisable(!isEnable);
        centerPhotoK.setDisable(!isEnable);
        _30PhotoK.setDisable(!isEnable);
        vertPhotoK.setDisable(!isEnable);

        if (!isEnable) {
            isCorrectPhotoK.setSelected(false);
            centerPhotoK.setSelected(false);
            _30PhotoK.setSelected(false);
            vertPhotoK.setSelected(false);
        }
    }

    private void setSGroupEnable(boolean isEnable) {
        isCorrectPhotoS.setDisable(!isEnable);
        centerPhotoS.setDisable(!isEnable);
        _30PhotoS.setDisable(!isEnable);
        vertPhotoS.setDisable(!isEnable);

        if (!isEnable) {
            isCorrectPhotoS.setSelected(false);
            centerPhotoS.setSelected(false);
            _30PhotoS.setSelected(false);
            vertPhotoS.setSelected(false);
        }
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
}
