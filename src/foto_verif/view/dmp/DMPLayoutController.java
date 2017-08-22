package foto_verif.view.dmp;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import foto_verif.Main;
import foto_verif.model.AddressTT;
import foto_verif.model.Photo;
import foto_verif.util.DateUtil;
import foto_verif.util.Logger;
import foto_verif.view.ChannelLayoutController;
import foto_verif.view.RootLayoutController1;
import javafx.application.Platform;
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

public class DMPLayoutController implements ChannelLayoutController {
    @FXML
    private ChoiceBox<String> regionChoice;
    @FXML
    private ChoiceBox<String> oblChoice;
    @FXML
    private ChoiceBox<String> channelChoice;
    @FXML
    private ChoiceBox<String> lkaChoice;
    @FXML
    private CheckBox showNonChecked;
    @FXML
    private TableView<DmpAddress> addressTable;
    @FXML
    private TableColumn<DmpAddress, String> nameColumn;
    @FXML
    private TableColumn<DmpAddress, String> addressColumn;
    @FXML
    private TableColumn<DmpAddress, String> checkedColumn;

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

    private ObservableList<String> regionList = FXCollections.observableArrayList();
    private ObservableList<String> oblList = FXCollections.observableArrayList();
    private ObservableList<String> channelList = FXCollections.observableArrayList();
    private ObservableList<String> lkaList = FXCollections.observableArrayList();
    private ObservableList<DmpAddress> dmpAddressesToShow = FXCollections.observableArrayList();

    private DmpContainer dmpContainer = new DmpContainer();

    private String regionSelected = null;
    private String oblSelected = null;
    private String channelSelected = null;
    private String lkaSelected = null;

    String pathToSave;

    private ArrayList<CriteriasLayoutController> criteriasLayoutControllerList = new ArrayList<>();

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

    public ObservableList<String> getChannelList() {
        return channelList;
    }

    public ObservableList<String> getLkaList() {
        return lkaList;
    }

    public ObservableList<DmpAddress> getDmpAddressesToShow() {
        return dmpAddressesToShow;
    }

    public DMPLayoutController() {
    }

    @FXML
    private void initialize() {

        // запрет сортировки точек в таблице
        //nameColumn.setSortable(false);
        //addressColumn.setSortable(false);
        //checkedColumn.setSortable(false);

        // Отслеживание выбора таблицы
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        addressColumn.setCellValueFactory(cellData -> cellData.getValue().addressProperty());
        checkedColumn.setCellValueFactory(cellData -> cellData.getValue().checkedProperty());

        addressTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
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

            if (newValue != null && newValue.getDmpDescrList().size() > 0) {
                setCheckBoxesFromFile(newValue);
            }
        });

        // Отслеживание выбора региона
        regionChoice.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.startsWith("По введенным")) {
                rootLayoutController.showErrorMessage("Загружаются данные. Ждите...");
                Platform.runLater(() ->
                        selectRegion(newValue));
            }
        });

        // Отслеживание выбора области
        oblChoice.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.startsWith("По введенным")) {
                rootLayoutController.showErrorMessage("Загружаются данные. Ждите...");
                Platform.runLater(() ->
                        selectObl(newValue));
            }
        });

        // Отслеживание выбора канала
        channelChoice.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.startsWith("По введенным")) {
                rootLayoutController.showErrorMessage("Загружаются данные. Ждите...");
                Platform.runLater(() ->
                        selectChannel(newValue));
            }
        });

        // Отслеживание выбора сети
        lkaChoice.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.startsWith("По введенным")) {
                rootLayoutController.showErrorMessage("Загружаются данные. Ждите...");
                Platform.runLater(() ->
                        selectLka(newValue));
            }
        });

        showNonChecked.selectedProperty().addListener((observable, oldValue, newValue) -> {
            fillUpRegionList();
        });

        // Параметры поля просмотра фото
        tilePane.setPadding(new Insets(15, 15, 15, 15));
        tilePane.setHgap(15);
        tilePane.setVgap(15);
        scrollPane.setFitToWidth(true);

        dmpCount.getItems().addAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        dmpCount.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            resizeTabNumbers(newValue);
        });

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(this.getClass().getResource("CriteriasLayout.fxml"));
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
    private void showAddressPhotos(DmpAddress dmpAddress) {
        if (dmpAddress != null) {
            List<Photo> photos = dmpAddress.getPhotoList();
            Collections.sort(photos);

            for (Photo photo : photos) {
                String url = photo.getUrl();
                ImageView imageView = null;

                Future<ImageView> future = main.getExecutor().submit(() -> {
                    return createImageView(url, photos.indexOf(photo));
                });
                //executor.shutdown();            //        <-- reject all further submissions
                try {
                    imageView = future.get(10, TimeUnit.SECONDS);  //     <-- wait 8 seconds to finish
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

    private static int showPhotoPos;

    // Создание вьюшки для фотки
    private ImageView createImageView(final String imageUrl, int pos) {
        int photoPos = pos;
        ImageView imageView = new ImageView(imageUrl);
        imageView.setFitWidth(200);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setCache(true);
        imageView.setOnMouseClicked(mouseEvent -> {

            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {

                if (mouseEvent.getClickCount() == 2) {
                    BorderPane borderPane = new BorderPane();
                    String fullPhotoUrl = imageUrl.replace("thumb/", "");
                    ImageView imageView1 = new ImageView(fullPhotoUrl);
                    showPhotoPos = photoPos;
                    imageView1.setStyle("-fx-background-color: BLACK");
                    imageView1.setFitHeight(main.getPrimaryStage().getHeight());
                    imageView1.setFitWidth(main.getPrimaryStage().getWidth());
                    imageView1.setPreserveRatio(true);
                    imageView1.setSmooth(true);
                    imageView1.setCache(true);

                    VBox rightBox = new VBox();
                    rightBox.setPrefWidth(85);
                    rightBox.setMinWidth(85);

                    ImageView forward = new ImageView(new Image(Main.class.getResourceAsStream("images/forward.png")));
                    forward.setOnMouseClicked(event ->
                            imageView1.setRotate(imageView1.getRotate() + 90));

                    ImageView back = new ImageView(new Image(Main.class.getResourceAsStream("images/back.png")));
                    back.setOnMouseClicked(event ->
                            imageView1.setRotate(imageView1.getRotate() - 90));

                    ImageView zoomin = new ImageView(new Image(Main.class.getResourceAsStream("images/zoom-in.png")));
                    zoomin.setOnMouseClicked(event -> {
                            imageView1.setFitWidth(imageView1.getFitWidth() * 1.1);
                            imageView1.setFitHeight(imageView1.getFitHeight() * 1.1);
                    });

                    ImageView zoomout = new ImageView(new Image(Main.class.getResourceAsStream("images/zoom-out.png")));
                    zoomout.setOnMouseClicked(event -> {
                            imageView1.setFitWidth(imageView1.getFitWidth() / 1.1);
                            imageView1.setFitHeight(imageView1.getFitHeight() / 1.1);
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
                            imageView1.setImage(new Image(leftImageUrl));
                            showPhotoPos--;
                        }
                    });

                    ImageView right = new ImageView(new Image(Main.class.getResourceAsStream("images/right.png")));
                    right.setOnMouseClicked(event -> {
                        int photoCount = addressTable.getSelectionModel().getSelectedItem().getPhotoList().size();
                        if (showPhotoPos < photoCount - 1) {
                            String rightImageUrl = addressTable.getSelectionModel().getSelectedItem().getPhotoList().get(showPhotoPos + 1).getUrl().replace("thumb/", "");
                            imageView1.setImage(new Image(rightImageUrl));
                            showPhotoPos++;
                        }
                    });
                    AnchorPane blankPane = new AnchorPane();
                    blankPane.setPrefHeight(100);
                    rightBox.getChildren().addAll(blankPane, forward, back, zoomin, zoomout, copy, left, right);
                    borderPane.setRight(rightBox);
                    ScrollPane scrollPane = new ScrollPane(imageView1);
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
        });

        return imageView;
    }

    @Override
    public void setMain(Main main) {
        this.main = main;
        addressTable.setItems(getDmpAddressesToShow());
        regionChoice.setItems(getRegionList());
        oblChoice.setItems(getOblList());
        channelChoice.setItems(getChannelList());
        lkaChoice.setItems(getLkaList());
    }

    @Override
    public void setRootLayoutController(RootLayoutController1 rootLayoutController) {
        this.rootLayoutController = rootLayoutController;

        rootLayoutController.getImportTMA().setVisible(false);
        rootLayoutController.getExportTMA().setVisible(false);
        rootLayoutController.getClearAll().setVisible(false);

        rootLayoutController.getNetLabel().setVisible(false);
        rootLayoutController.getNetChoice().setVisible(false);
    }

    public void loadDatas() {
        clearCheckBoxes();
        getRegionList().clear();
        getOblList().clear();
        getChannelList().clear();
        getLkaList().clear();
        getDmpAddressesToShow().clear();

        pathToSave = new File("save").getAbsolutePath() + "/LKA_DMP/" +
                DateUtil.format(rootLayoutController.getDateFromSelected()) + "-" + DateUtil.format(rootLayoutController.getDateToSelected()) + "/";

        new File(pathToSave).mkdirs();

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
    }

    private void fillUpRegionList() {
        clearCheckBoxes();
        dmpContainer.clear();
        getRegionList().clear();
        getOblList().clear();
        getChannelList().clear();
        getLkaList().clear();
        getDmpAddressesToShow().clear();

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
        regionSelected = region;
        rootLayoutController.showErrorMessage("");
        getOblList().clear();
        getChannelList().clear();
        getLkaList().clear();
        getDmpAddressesToShow().clear();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(pathToSave + regionSelected.replace(" ", "_") + ".dat"));
            JAXBContext context = JAXBContext.newInstance(DmpContainer.class, DmpAddress.class, Photo.class, DmpDescr.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            dmpContainer = (DmpContainer) unmarshaller.unmarshal(reader);
            reader.close();

        } catch (IOException | JAXBException e) {
            Logger.log(e.getMessage());
            e.printStackTrace();
            rootLayoutController.showErrorMessage("Не удается открыть файл.");
            return;
        }

        List<String> oblList;
        if (showNonChecked.isSelected()) {
            oblList = dmpContainer.getDmpAddresses().stream()
                    .filter(nkaAddress -> nkaAddress.getRegion().equals(regionSelected))
                    .filter(nkaAddress -> nkaAddress.getChecked().equals("0"))
                    .map(nkaAddress -> nkaAddress.getObl())
                    .distinct()
                    .collect(Collectors.toList());
        } else {
            oblList = dmpContainer.getDmpAddresses().stream()
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
        oblSelected = obl;
        rootLayoutController.showErrorMessage("");
        getChannelList().clear();
        getLkaList().clear();
        getDmpAddressesToShow().clear();

        List<String> channelList;
        if (showNonChecked.isSelected()) {
            channelList = dmpContainer.getDmpAddresses().stream()
                    .filter(nkaAddress -> nkaAddress.getRegion().equals(regionSelected))
                    .filter(nkaAddress -> nkaAddress.getObl().equals(oblSelected))
                    .filter(nkaAddress -> nkaAddress.getChecked().equals("0"))
                    .map(nkaAddress -> nkaAddress.getChannel())
                    .distinct()
                    .collect(Collectors.toList());
        } else {
            channelList = dmpContainer.getDmpAddresses().stream()
                    .filter(nkaAddress -> nkaAddress.getRegion().equals(regionSelected))
                    .filter(nkaAddress -> nkaAddress.getObl().equals(oblSelected))
                    .map(nkaAddress -> nkaAddress.getChannel())
                    .distinct()
                    .collect(Collectors.toList());
        }

        if (channelList.size() > 0) {
            getChannelList().addAll(channelList);
        } else {
            getChannelList().addAll("По введенным параметрам нет фото...");
        }
        rootLayoutController.showErrorMessage("");

        /*try {
            List<String> channelList = main.getJdbcModel().getChannelList(obl);
            if (channelList.size() > 0) {
                getChannelList().addAll(channelList);
            } else {
                getChannelList().addAll("По введенным параметрам нет фото...");
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
    Действия при выборе канала
     */
    private void selectChannel(String channel) {
        channelSelected = channel;
        rootLayoutController.showErrorMessage("");
        getLkaList().clear();
        getDmpAddressesToShow().clear();
        getCheckedRows().clear();

        if (channel.contains("Сетевой")) {
            List<String> lkaList;
            lkaChoice.setDisable(false);
            if (showNonChecked.isSelected()) {
                lkaList = dmpContainer.getDmpAddresses().stream()
                        .filter(nkaAddress -> nkaAddress.getRegion().equals(regionSelected))
                        .filter(nkaAddress -> nkaAddress.getObl().equals(oblSelected))
                        .filter(dmpAddress -> dmpAddress.getChannel().equals(channelSelected))
                        .filter(nkaAddress -> nkaAddress.getChecked().equals("0"))
                        .map(nkaAddress -> nkaAddress.getNet())
                        .distinct()
                        .collect(Collectors.toList());
            } else {
                lkaList = dmpContainer.getDmpAddresses().stream()
                        .filter(nkaAddress -> nkaAddress.getRegion().equals(regionSelected))
                        .filter(nkaAddress -> nkaAddress.getObl().equals(oblSelected))
                        .filter(dmpAddress -> dmpAddress.getChannel().equals(channelSelected))
                        .map(nkaAddress -> nkaAddress.getNet())
                        .distinct()
                        .collect(Collectors.toList());
            }
            if (lkaList.size() > 0) {
                getLkaList().addAll(lkaList);
            } else {
                getLkaList().addAll("По введенным параметрам нет фото...");
            }
            rootLayoutController.showErrorMessage("");
        } else {
            lkaSelected = null;
            lkaChoice.setDisable(true);
            List<DmpAddress> dmpAddresses;
            if (showNonChecked.isSelected()) {
                dmpAddresses = dmpContainer.getDmpAddresses().stream()
                        .filter(nkaAddress -> nkaAddress.getRegion().equals(regionSelected))
                        .filter(nkaAddress -> nkaAddress.getObl().equals(oblSelected))
                        .filter(nkaAddress -> nkaAddress.getChannel().equals(channelSelected))
                        .filter(nkaAddress -> nkaAddress.getChecked().equals("0"))
                        .collect(Collectors.toList());
            } else {
                dmpAddresses = dmpContainer.getDmpAddresses().stream()
                        .filter(nkaAddress -> nkaAddress.getRegion().equals(regionSelected))
                        .filter(nkaAddress -> nkaAddress.getObl().equals(oblSelected))
                        .filter(nkaAddress -> nkaAddress.getChannel().equals(channelSelected))
                        .collect(Collectors.toList());
            }

            dmpAddressesToShow.addAll(dmpAddresses);
            rootLayoutController.showErrorMessage("");
        }



        /*try {
            if (channel.contains("Сетевой")) {
                lkaChoice.setDisable(false);
                List<String> lkaList = main.getJdbcModel().getNetList();
                if (lkaList.size() > 0) {
                    getLkaList().addAll(lkaList);
                } else {
                    getLkaList().addAll("По введенным параметрам нет фото...");
                }
            } else {
                lkaSelected = null;
                lkaChoice.setDisable(true);
                main.getJdbcModel().getAddresses(channel, null);
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
    Действия при выборе локальной сети
     */
    private void selectLka(String lka) {
        lkaSelected = lka;
        getDmpAddressesToShow().clear();
        getCheckedRows().clear();

        List<DmpAddress> dmpAddresses;
        if (showNonChecked.isSelected()) {
            dmpAddresses = dmpContainer.getDmpAddresses().stream()
                    .filter(nkaAddress -> nkaAddress.getRegion().equals(regionSelected))
                    .filter(nkaAddress -> nkaAddress.getObl().equals(oblSelected))
                    .filter(nkaAddress -> nkaAddress.getChannel().equals(channelSelected))
                    .filter(dmpAddress -> dmpAddress.getNet().equals(lkaSelected))
                    .filter(nkaAddress -> nkaAddress.getChecked().equals("0"))
                    .collect(Collectors.toList());
        } else {
            dmpAddresses = dmpContainer.getDmpAddresses().stream()
                    .filter(nkaAddress -> nkaAddress.getRegion().equals(regionSelected))
                    .filter(nkaAddress -> nkaAddress.getObl().equals(oblSelected))
                    .filter(nkaAddress -> nkaAddress.getChannel().equals(channelSelected))
                    .filter(dmpAddress -> dmpAddress.getNet().equals(lkaSelected))
                    .collect(Collectors.toList());
        }
        dmpAddressesToShow.addAll(dmpAddresses);
        rootLayoutController.showErrorMessage("");

        /*try {
            main.getJdbcModel().getAddresses(channelSelected, lka);
            rootLayoutController.showErrorMessage("");
        } catch (SQLServerException e) {
            Logger.log(e.getMessage());
            e.printStackTrace();
            rootLayoutController.showExceptionAlert("База не отвечает на запрос. Попробуйте позднее.");
            rootLayoutController.showErrorMessage("Невозможно подключиться к базе данных");
        } catch (SQLException e) {
            Logger.log(e.getMessage());
            e.printStackTrace();
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
            List<DmpAddress> allAddressList = new ArrayList<>();
            try {
                allAddressList = (List<DmpAddress>) main.getJdbcModel().getAddresses(region, null);
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
                List<DmpAddress> allAddressList = new ArrayList<>();
                try {
                    allAddressList = (List<DmpAddress>) main.getJdbcModel().getAddresses(regionList.get(i), null);
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

        DmpContainer newDmpContainer;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileToImport));
            JAXBContext context = JAXBContext.newInstance(DmpContainer.class, DmpAddress.class, Photo.class, DmpDescr.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            newDmpContainer = (DmpContainer) unmarshaller.unmarshal(reader);
            reader.close();

        } catch (IOException | JAXBException e) {
            Logger.log(e.getMessage());
            e.printStackTrace();
            rootLayoutController.showErrorMessage("Не удается открыть файл.");
            return;
        }

        addNewAddressesAndPhotos(newDmpContainer.getDmpAddresses(), fileToImport.getName().replace(".dat", ""));

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Данные успешно добавлены");
        alert.setHeaderText(null);
        alert.showAndWait();

        fillUpRegionList();
    }

    private void addNewAddressesAndPhotos(List<DmpAddress> allAddressesList, String region) {
        DmpContainer dmpContainer = null;

        File saveFile = new File(pathToSave + region.replace(" ", "_") + ".dat");

        if (saveFile.exists()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(saveFile));
                JAXBContext context = JAXBContext.newInstance(DmpContainer.class, DmpAddress.class, Photo.class, DmpDescr.class);
                Unmarshaller unmarshaller = context.createUnmarshaller();

                dmpContainer = (DmpContainer) unmarshaller.unmarshal(reader);
                reader.close();

                for (DmpAddress dmpAddress : allAddressesList) {
                    if (dmpContainer.getDmpAddresses().contains(dmpAddress)) {
                        DmpAddress currentDmpAddress = dmpContainer.getDmpAddresses().get(dmpContainer.getDmpAddresses().indexOf(dmpAddress));
                        for (Photo photo : dmpAddress.getPhotoList()) {
                            if (!currentDmpAddress.getPhotoList().contains(photo)) {
                                photo.setChecked(false);
                                currentDmpAddress.getPhotoList().add(photo);
                                currentDmpAddress.setChecked("0");
                            }
                        }
                    } else {
                        dmpAddress.getDmpDescrList().forEach(DmpDescr::clearDmpDescr);
                        dmpAddress.getPhotoList().forEach(photo -> photo.setChecked(false));
                        dmpAddress.setChecked("0");
                        dmpContainer.add(dmpAddress);
                    }
                }
            } catch (IOException | JAXBException e) {
                Logger.log(e.getMessage());
                e.printStackTrace();
                rootLayoutController.showErrorMessage("Не удается открыть файл.");
            }
        } else {
            dmpContainer = new DmpContainer();
            allAddressesList.forEach(dmpAddress -> {
                dmpAddress.getDmpDescrList().forEach(DmpDescr::clearDmpDescr);
                dmpAddress.getPhotoList().forEach(photo -> photo.setChecked(false));
                dmpAddress.setChecked("0");
            });
            dmpContainer.addAll(allAddressesList);
            new File(pathToSave).mkdirs();
        }

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(saveFile));
            JAXBContext context = JAXBContext.newInstance(DmpContainer.class, DmpAddress.class, Photo.class, DmpDescr.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(dmpContainer, writer);
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
        for (int i = 0; i < criteriasTabPane.getTabs().size(); i++) {
            criteriasLayoutControllerList.get(i).clearCheckBoxesCurrDMP();
        }
        changesSavedLabel.setText("");
    }

    @Override
    public void clearCheckedRows() {
        getCheckedRows().clear();
        for (DmpAddress dmpAddress : getDmpAddressesToShow()) {
            for (Photo photo : (ArrayList<Photo>) dmpAddress.getPhotoList()) {
                photo.setChecked(false);
            }
            dmpAddress.setChecked("0");
        }
    }

    private void setCheckBoxesFromFile(DmpAddress dmpAddress) {

        int dmpNum = dmpAddress.getDmpDescrList().size();
        dmpCount.getSelectionModel().select(dmpNum - 1);
        resizeTabNumbers(dmpNum);

        for(int i = 0; i < dmpNum; i++) {
            criteriasLayoutControllerList.get(i).setCheckBoxesFromDmpDescr(dmpAddress.getDmpDescrList().get(i));
        }

        /*String tmpLkaSelected = null;
        if (lkaSelected != null) {
            tmpLkaSelected = new String(lkaSelected).replace("/", ", ");
        }
        String path = ("save/" + rootLayoutController.getPhotoTypeSelected() + "/" + "LKA/" +
                DateUtil.format(rootLayoutController.getDateFromSelected()) + "-" + DateUtil.format(rootLayoutController.getDateToSelected()) + "/" + regionSelected + "/" +
                oblSelected + "/" + channelSelected + "/" + tmpLkaSelected).replace(" ", "_").replace("\"", "") +
                "/" + addressTable.getSelectionModel().getSelectedItem().getId() + ".txt";
        String path1 = ("save/" + rootLayoutController.getPhotoTypeSelected() + "/" + "LKA/" +
                DateUtil.format(rootLayoutController.getDateFromSelected()) + "-" + DateUtil.format(rootLayoutController.getDateToSelected()) + "/" + regionSelected + "/" +
                oblSelected + "/" + channelSelected + "/" + tmpLkaSelected).replace(" ", "_").replace("\"", "") +
                "/" + addressTable.getSelectionModel().getSelectedItem().getId() + ".adr";

        if (new File(path1).exists()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(path1));
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

        } else {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(path));
                reader.readLine();
                reader.readLine();
                String nextLine = reader.readLine();

                int dmpNum;
                boolean isOldVersion = false;

                if (nextLine.length() > 3) {
                    isOldVersion = true;
                    dmpNum = 1;
                } else {
                    dmpNum = Integer.parseInt(nextLine);
                }

                dmpCount.getSelectionModel().select(dmpNum - 1);

                resizeTabNumbers(dmpNum);

                for (int i = 0; i < dmpNum; i++) {
                    String datas;
                    if (i == 0 && isOldVersion) {
                        datas = nextLine;
                    } else {
                        datas = reader.readLine();
                    }

                    if (datas.length() == 10) {
                        String tmp = datas.substring(0, 4) + "0" + datas.substring(4, 10);
                        datas = tmp;
                    }

                    String comm = reader.readLine();
                    criteriasLayoutControllerList.get(i).setCheckBoxesFromFileCurrDMP(datas, comm);
                }
                reader.close();
            } catch (IOException e) {
                Logger.log(e.getMessage());
                e.printStackTrace();
            }
        }*/
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

        DmpAddress dmpAddressSelected = addressTable.getSelectionModel().getSelectedItem();

        DmpAddress dmpAddressCurrent = dmpContainer.get(dmpContainer.getDmpAddresses().indexOf(dmpAddressSelected));

        int dmpNum = criteriasTabPane.getTabs().size();
        ArrayList<DmpDescr> dmpDescrs = new ArrayList<>();
        for (int i = 0; i < dmpNum; i++) {
            dmpDescrs.add(criteriasLayoutControllerList.get(i).getDmpDescr());
        }
        dmpAddressCurrent.setDmpDescrList(dmpDescrs);

        dmpAddressCurrent.setChecked("1");

        for (Photo photo : (ArrayList<Photo>) dmpAddressCurrent.getPhotoList()) {
            photo.setChecked(true);
        }
        for (Node node : tilePane.getChildren()) {
            VBox vBox = (VBox) node;
            vBox.getChildren().get(1).setStyle("-fx-background-color: lightgreen");
            vBox.getChildren().get(2).setStyle("-fx-background-color: lightgreen");
        }

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(pathToSave + regionSelected.replace(" ", "_") + ".dat"));
            JAXBContext context = JAXBContext.newInstance(DmpContainer.class, DmpAddress.class, Photo.class, DmpDescr.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(dmpContainer, writer);
            writer.flush();
            writer.close();
        } catch (JAXBException | IOException e) {
            Logger.log(e.getMessage());
            e.printStackTrace();
        }

        changesSavedLabel.setText("Изменения сохранены");

        /*DmpAddress dmpAddress = getDmpAddressesToShow().get(addressIndex);
        String tmpLkaSelected = null;
        if (lkaSelected != null) {
            tmpLkaSelected = new String(lkaSelected).replace("/", ", ");
        }
        String path = "save/" + rootLayoutController.getPhotoTypeSelected() + "/" + "LKA/" +
                DateUtil.format(rootLayoutController.getDateFromSelected()) + "-" + DateUtil.format(rootLayoutController.getDateToSelected()) + "/" + regionSelected + "/" +
                oblSelected + "/" + channelSelected + "/" + tmpLkaSelected;
        path = path.replace(" ", "_").replace("\"", "");
        new File(path).mkdirs();
        File file1 = new File(path + "/" + dmpAddress.getId() + ".adr");

        try {
            if (!file1.exists())
                file1.createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(file1));
            DmpAddressTT dmpAddressTT = new DmpAddressTT();
            //dmpAddressTT.copyFromAddressTT(dmpAddress);
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

        dmpAddress.setChecked("1");
        getCheckedRows().add(addressTable.getSelectionModel().getSelectedIndex());
        for (Photo photo : (ArrayList<Photo>) dmpAddress.getPhotoList()) {
            photo.setChecked(true);
        }
        for (Node node : tilePane.getChildren()) {
            VBox vBox = (VBox) node;
            vBox.getChildren().get(1).setStyle("-fx-background-color: lightgreen");
            vBox.getChildren().get(2).setStyle("-fx-background-color: lightgreen");
        }
        changesSavedLabel.setText("Изменения сохранены");*/
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
                    loader.setLocation(this.getClass().getResource("CriteriasLayout.fxml"));
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
