package foto_verif.view.nst;

import foto_verif.Main;
import foto_verif.model.AddressTT;
import foto_verif.model.TMAActivity;
import foto_verif.util.DateUtil;
import foto_verif.util.Logger;
import foto_verif.view.ChannelLayoutController;
import foto_verif.view.RootLayoutController1;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.commons.io.output.FileWriterWithEncoding;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.*;
import java.net.*;
import java.time.LocalDate;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

public class NstLayoutController implements ChannelLayoutController {
    @FXML
    private ChoiceBox<String> oblChoice;
    @FXML
    private CheckBox proxyCheckBox;
    @FXML
    private Label statAll;
    @FXML
    private Label statChecked;
    @FXML
    private Label statCheckedToday;
    /*@FXML
    private ChoiceBox<String> cityChoice;*/
    @FXML
    private TableView<NstAddress> addressTable;
    @FXML
    private TableColumn<NstAddress, String> nameColumn;
    @FXML
    private TableColumn<NstAddress, String> checkedColumn;

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private TilePane tilePane;

    /*@FXML
    private Label selectedShop;*/
    @FXML
    private ChoiceBox<Integer> visitCountChoiceBox;
    @FXML
    private CheckBox noMatrixMZ;
    @FXML
    private CheckBox havePhotoMZ;
    @FXML
    private CheckBox bordersPhotoMZ;
    @FXML
    private CheckBox centerPhotoMZ;
    @FXML
    private CheckBox _30PhotoMZ;
    @FXML
    private CheckBox vertPhotoMZ;
    @FXML
    private TextField textCommentMZ;
    @FXML
    private ChoiceBox<String> commentMZ;
    @FXML
    private CheckBox noMatrixKS;
    @FXML
    private CheckBox havePhotoKS;
    @FXML
    private CheckBox bordersPhotoKS;
    @FXML
    private CheckBox centerPhotoKS;
    @FXML
    private CheckBox _30PhotoKS;
    @FXML
    private CheckBox vertPhotoKS;
    @FXML
    private TextField textCommentKS;
    @FXML
    private ChoiceBox<String> commentKS;
    @FXML
    private CheckBox noMatrixM;
    @FXML
    private CheckBox havePhotoM;
    @FXML
    private CheckBox bordersPhotoM;
    @FXML
    private CheckBox centerPhotoM;
    /*  @FXML
      private CheckBox _30PhotoM;*/
    @FXML
    private CheckBox vertPhotoM;
    @FXML
    private TextField textCommentM;
    @FXML
    private ChoiceBox<String> commentM;
    /* @FXML
     private CheckBox oos;*/
    /*@FXML
    private TextArea comment;*/
    @FXML
    private Label changesSavedLabel;
    /*@FXML
    private ScrollPane tmaScrollPane;
    @FXML
    private GridPane tmaGridPane;*/

    private Main main;
    private RootLayoutController1 rootLayoutController;

    private ObservableList<String> oblList = FXCollections.observableArrayList();
    private ArrayList<String> oblGmList = new ArrayList<>();
    private ObservableList<String> cityList = FXCollections.observableArrayList();

    private String oblSelected = null;
    private String citySelected = null;

    private java.net.Proxy proxy;
    private static String PHP_SESS_ID;
    private static String IDENTITY;

    //private ArrayList<NstAddress> allNstAddresses = new ArrayList<>();
    private NstContainer container = new NstContainer();
    private ObservableList<NstAddress> nstAddresses = FXCollections.observableArrayList();

    private ObservableList<NstAddress> getNstAddresses() {
        return nstAddresses;
    }

    @Override
    public ObservableList<AddressTT> getAddresses() {
        return addresses;
    }

    public ObservableList<Integer> getCheckedRows() {
        return checkedRows;
    }

    private ObservableList<String> getOblList() {
        return oblList;
    }

    public ArrayList<String> getOblGmList() {
        return oblGmList;
    }

    private ObservableList<String> getCityList() {
        return cityList;
    }

    public NstLayoutController() {
    }

    @FXML
    private void initialize() {

        // запрет сортировки точек в таблице
        nameColumn.setSortable(false);
        checkedColumn.setSortable(false);

        // Отслеживание выбора таблицы
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        checkedColumn.setCellValueFactory(cellData -> cellData.getValue().checkedProperty());

        addressTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            showAddressPhotos(null);
            showAddressPhotos(newValue);

            clearCheckBoxes();

            if (newValue == null) {
                getCheckedRows().clear();
            }
                /*selectedShop.setText("");*/
                /*if (newValue != null) {
                    selectedShop.setText(newValue.getName());
                }*/

            rootLayoutController.showErrorMessage("");

            if (newValue != null) {
                if (getNstAddresses().get(addressTable.getSelectionModel().getSelectedIndex()).getChecked().equals("1")) {
                    setCheckBoxesFromFile(newValue);
                }
            }
        });

        // Отслеживание выбора области
        oblChoice.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.startsWith("По введенным")) {
                rootLayoutController.showErrorMessage("Загружаются данные. Ждите...");
                selectedObl(newValue);
            }
        });

        /*// Отслеживание выбора города
        cityChoice.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue != null && !newValue.startsWith("По введенным")) {
                    rootLayoutController.showErrorMessage("Загружаются данные. Ждите...");
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            selectedCity(newValue);
                        }
                    });
                }
            }
        });*/

        // Отслеживание выбора наличия фото
        havePhotoMZ.selectedProperty().addListener((observable, oldValue, newValue) -> {
            setMzGroupEnable(newValue);
            //enableTMAbyTG("Майонез", newValue);
        });

        havePhotoKS.selectedProperty().addListener((observable, oldValue, newValue) -> {
            setKsGroupEnable(newValue);
            //enableTMAbyTG("Кетчуп", newValue);

        });

        havePhotoM.selectedProperty().addListener((observable, oldValue, newValue) -> {
            setMGroupEnable(newValue);
            //enableTMAbyTG("Соус", newValue);
        });

        bordersPhotoMZ.selectedProperty().addListener((observable, oldValue, newValue) -> {
            setMzBordersEnable(newValue);
        });

        bordersPhotoKS.selectedProperty().addListener((observable, oldValue, newValue) -> {
            setKsBordersEnable(newValue);
        });

        bordersPhotoM.selectedProperty().addListener((observable, oldValue, newValue) -> {
            setMBordersEnable(newValue);
        });

        // Параметры поля просмотра фото
        tilePane.setPadding(new Insets(15, 15, 15, 15));
        tilePane.setHgap(15);
        tilePane.setVgap(15);
        scrollPane.setFitToWidth(true);

        // Параметры поля с акциями
        //tmaScrollPane.setFitToWidth(true);

        visitCountChoiceBox.getItems().addAll(1, 2, 3, 4, 5, 6, 7);

        commentMZ.getItems().addAll("Выставить по центру вертикальным бренд-блоком во всю высоту полочного пространства",
                "Расширить вертикальный бренд-блок от верхней до нижней полки",
                "Выровнять вертикальный бренд-блок",
                "По наличию товара выставить вертикальным бренд-блоком во всю высоту полочного пр-ва занимая не менее 30% полочного пр-ва",
                "Фото ДМП. Предоставить фото основной полки",
                "Фото плохого качества",
                "Занять долю полки 30%",
                "Выставить по центру");
        commentKS.getItems().addAll("Выставить по центру вертикальным бренд-блоком во всю высоту полочного пространства",
                "Расширить вертикальный бренд-блок от верхней до нижней полки",
                "Выровнять вертикальный бренд-блок",
                "По наличию товара выставить вертикальным бренд-блоком во всю высоту полочного пр-ва занимая не менее 30% полочного пр-ва",
                "Фото ДМП. Предоставить фото основной полки",
                "Фото плохого качества",
                "Занять долю полки 30%",
                "Выставить по центру");
        commentM.getItems().addAll("Выставить по центру вертикальным бренд-блоком во всю высоту полочного пространства",
                "Расширить вертикальный бренд-блок от верхней до нижней полки",
                "Выровнять вертикальный бренд-блок",
                "По наличию товара выставить вертикальным бренд-блоком во всю высоту полочного пр-ва занимая не менее 30% полочного пр-ва",
                "Фото ДМП. Предоставить фото основной полки",
                "Фото плохого качества",
                "Занять долю полки 30%",
                "Выставить по центру");

        commentMZ.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                String comment = textCommentMZ.getText();
                comment += comment.length() > 0 ? ", " + newValue : newValue;
                textCommentMZ.clear();
                textCommentMZ.setText(comment);
            }
            commentMZ.getSelectionModel().clearSelection();
        });

        commentKS.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                String comment = textCommentKS.getText();
                comment += comment.length() > 0 ? ", " + newValue : newValue;
                textCommentKS.clear();
                textCommentKS.setText(comment);
            }
            commentKS.getSelectionModel().clearSelection();
        });

        commentM.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                String comment = textCommentM.getText();
                comment += comment.length() > 0 ? ", " + newValue : newValue;
                textCommentM.clear();
                textCommentM.setText(comment);
            }
            commentM.getSelectionModel().clearSelection();
        });

        Authenticator authenticator = new Authenticator() {

            public PasswordAuthentication getPasswordAuthentication() {
                return (new PasswordAuthentication("market6",
                        "Wert1234".toCharArray()));
            }
        };
        Authenticator.setDefault(authenticator);

        proxy = new java.net.Proxy(java.net.Proxy.Type.HTTP, new InetSocketAddress("192.168.5.230", 3128));
    }

    // Отображение фотографий по выбранному адресу
    private void showAddressPhotos(NstAddress address) {
        if (address != null) {
            List<NstPhoto> photos = address.getPhotoList();
            Collections.sort(photos);
            for (NstPhoto photo : photos) {
                String url = photo.getUrl().replace("&amp;", "&");
                String fullUrl = photo.getFullUrl().replace("&amp;", "&");
                ImageView imageView = null;

                Future<ImageView> future = main.getExecutor().submit(() ->   {
                    return createImageView(url, fullUrl, photos.indexOf(photo));
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
                if (photo.isChecked()) {
                    photoDate.setStyle("-fx-background-color: lightgreen");
                }
                photoDate.setFont(Font.font("sans-serif", FontWeight.BOLD, 12d));
                photoDate.setPrefWidth(200);
                photoDate.setAlignment(Pos.CENTER);
                photoBox.getChildren().addAll(photoDate);
                tilePane.getChildren().addAll(photoBox);
            }
        } else {
            tilePane.getChildren().clear();
        }
    }

    private static int showPhotoPos;

    // Создание вьюшки для фотки
    private ImageView createImageView(final String imageUrl, final String fullUrl, int pos) throws IOException {
        int photoPos = pos;

        URL url = new URL(imageUrl);

        HttpURLConnection connection;
        if (proxyCheckBox.isSelected()) {
            connection = (HttpURLConnection) url.openConnection(proxy);
        } else {
            connection = (HttpURLConnection) url.openConnection();
        }
        String cookie = "PHPSESSID=" + PHP_SESS_ID + "; _identity=" + IDENTITY;
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Cookie", cookie);
        connection.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; Win86; x86) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
        connection.connect();
        InputStream is = connection.getInputStream();

        //InputStream is = new FileInputStream(new File(imageUrl).getAbsolutePath());
        final ImageView imageView = new ImageView(new Image(is));
        is.close();
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
                        InputStream is = null;
                        try {
                            URL url = new URL(fullUrl);

                            HttpURLConnection connection;
                            if (proxyCheckBox.isSelected()) {
                                connection = (HttpURLConnection) url.openConnection(proxy);
                            } else {
                                connection = (HttpURLConnection) url.openConnection();
                            }
                            String cookie = "PHPSESSID=" + PHP_SESS_ID + "; _identity=" + IDENTITY;
                            connection.setRequestMethod("GET");
                            connection.setRequestProperty("Cookie", cookie);
                            connection.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
                            connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
                            //connection.connect();
                            is = connection.getInputStream();

                            //is = new FileInputStream(imageUrl);
                            final ImageView imageView = new ImageView(new Image(is));
                            is.close();
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
                                StringSelection ss = new StringSelection(imageUrl);
                                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, ss);
                            });

                            ImageView left = new ImageView(new Image(Main.class.getResourceAsStream("images/left.png")));
                            left.setOnMouseClicked(event -> {
                                if (showPhotoPos > 0) {
                                    try {
                                        String leftImageUrl = addressTable.getSelectionModel().getSelectedItem().getPhotoList().get(showPhotoPos - 1).getFullUrl();

                                        URL url1 = new URL(leftImageUrl);

                                        HttpURLConnection connection1;
                                        if (proxyCheckBox.isSelected()) {
                                            connection1 = (HttpURLConnection) url1.openConnection(proxy);
                                        } else {
                                            connection1 = (HttpURLConnection) url1.openConnection();
                                        }
                                        String cookie1 = "_ym_uid=1499087433246379276; _ym_isad=1; PHPSESSID=" + PHP_SESS_ID + "; _identity=" + IDENTITY;
                                        connection1.setRequestMethod("GET");
                                        connection1.setRequestProperty("Cookie", cookie);
                                        connection1.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
                                        connection1.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
                                        //connection.connect();
                                        InputStream is1 = connection1.getInputStream();

                                        imageView.setImage(new Image(is1));
                                        is1.close();
                                        showPhotoPos--;
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        Logger.log(e.getMessage());
                                    }
                                }
                            });

                            ImageView right = new ImageView(new Image(Main.class.getResourceAsStream("images/right.png")));
                            right.setOnMouseClicked(event -> {
                                int photoCount = addressTable.getSelectionModel().getSelectedItem().getPhotoList().size();
                                if (showPhotoPos < photoCount - 1) {
                                    try {
                                        String rightImageUrl = addressTable.getSelectionModel().getSelectedItem().getPhotoList().get(showPhotoPos + 1).getFullUrl();

                                        URL url1 = new URL(rightImageUrl);

                                        HttpURLConnection connection1;
                                        if (proxyCheckBox.isSelected()) {
                                            connection1 = (HttpURLConnection) url1.openConnection(proxy);
                                        } else {
                                            connection1 = (HttpURLConnection) url1.openConnection();
                                        }
                                        String cookie1 = "_ym_uid=1499087433246379276; _ym_isad=1; PHPSESSID=" + PHP_SESS_ID + "; _identity=" + IDENTITY;
                                        connection1.setRequestMethod("GET");
                                        connection1.setRequestProperty("Cookie", cookie);
                                        connection1.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
                                        connection1.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
                                        //connection.connect();
                                        InputStream is1 = connection1.getInputStream();

                                        imageView.setImage(new Image(is1));
                                        is1.close();
                                        showPhotoPos++;
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        Logger.log(e.getMessage());
                                    }
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
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        return imageView;
    }

    @Override
    public void setMain(Main main) {
        this.main = main;
        addressTable.setItems(getNstAddresses());
        oblChoice.setItems(getOblList());
        //cityChoice.setItems(getCityList());
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

    private static boolean canContinue = true;

    public void loadDatas() {
        //clearTMAActivityPane();
        clearCheckBoxes();
        getOblList().clear();
        getOblGmList().clear();
        getCityList().clear();
        getNstAddresses().clear();
        canContinue = true;

        sDateFrom = DateUtil.format(rootLayoutController.getDateFromSelected());
        sDateTo = DateUtil.format(rootLayoutController.getDateToSelected());
        pathToSave = new File("resource").getAbsolutePath() + "/" + sDateFrom + "-" + sDateTo + "/";

        String dataFilePath = new File("resource").getAbsolutePath() + "/" + sDateFrom + "-" + sDateTo + "";

        /*if (!new File(dataFilePath).exists()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "За указанные даты не найдено данных. " +
                    "Нажмите 'OK' для начала загрузки фотографий, 'Cancel' для отмены");
            alert.setHeaderText(null);
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        downloadPhotos(0);
                    } catch (AWTException e) {
                        if (driver != null) driver.quit();
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        if (driver != null) driver.quit();
                        e.printStackTrace();
                    } catch (StaleElementReferenceException e) {
                        if (driver != null) driver.quit();
                        e.printStackTrace();
                    }
                } else {
                    canContinue = false;
                    rootLayoutController.showErrorMessage("");
                }
            });
        }*/

        if (!canContinue) return;

        fillOblList();
        rootLayoutController.showErrorMessage("");

        /*clearTMAActivityPane();
        fillUpTMAActivityPane();*/


        /*try {
            getCookieFirefox();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (AWTException e) {
            e.printStackTrace();
        }*/

    }

    private void fillOblList() {

        container.clear();
        getOblList().clear();
        getOblGmList().clear();
        getCityList().clear();
        getNstAddresses().clear();

        List<File> obls = new ArrayList<>();
        if (new File(pathToSave + "1/").exists()) {
            obls = Arrays.asList(new File(pathToSave + "1/").listFiles());
        }
        List<File> oblGms = new ArrayList<>();
        if (new File(pathToSave + "2/").exists()) {
            oblGms = Arrays.asList(new File(pathToSave + "2/").listFiles());
        }

        List<String> savedOblList;
        List<String> savedOblGmList;

        try {
            savedOblList = obls.stream()
                    .filter(file -> file.isFile())
                    .filter(file -> file.getName().endsWith(".dat"))
                    .map(file -> file.getName().replace("_", " ").replace(".dat", ""))
                    .collect(Collectors.toList());
            savedOblGmList = oblGms.stream()
                    .filter(file -> file.isFile())
                    .filter(file -> file.getName().endsWith(".dat"))
                    .map(file -> file.getName().replace("_", " ").replace(".dat", ""))
                    .collect(Collectors.toList());
        } catch (NullPointerException e) {
            /*NOTHING*/
            return;
        }

        savedOblList.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });

        if (savedOblList.size() > 0) {
            getOblList().addAll(savedOblList);
        } else {
            getOblList().addAll("По введенным параметрам нет фото...");
        }
        if (savedOblGmList.size() > 0) {
            getOblGmList().addAll(savedOblGmList);
        } else {

        }
    }

    /*
    Действия при выборе сети
     */
    private void selectedObl(String oblName) {
        oblSelected = oblName;
        rootLayoutController.showErrorMessage("");
        //getCityList().clear();
        getNstAddresses().clear();
        getCheckedRows().clear();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(pathToSave + "1/" + oblName.replace(" ", "_") + ".dat"));
            JAXBContext context = JAXBContext.newInstance(NstContainer.class, NstAddress.class, NstPhoto.class, TMAActivity.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            container = (NstContainer) unmarshaller.unmarshal(reader);
            reader.close();
        } catch (IOException | JAXBException e) {
            Logger.log(e.getMessage());
            e.printStackTrace();
            rootLayoutController.showErrorMessage("Не удается открыть файл.");
        }

        /*List<String> cityList = container.getNstAddresses().stream()
                .filter(nstAddress -> nstAddress.getPhotoList().size() > 0)
                .map(nstAddress -> nstAddress.getCity())
                .distinct()
                .collect(Collectors.toList());

        if (cityList.size() > 0) {
            getCityList().addAll(cityList);
        } else {
            getCityList().addAll("По введенным параметрам нет фото...");
        }*/

        List<NstAddress> addressList = container.getNstAddresses().stream()
                .filter(nstAddress -> nstAddress.getObl().equals(oblSelected))
                //.filter(nstAddress -> nstAddress.getCity().equals(citySelected))
                .filter(nstAddress -> nstAddress.getPhotoList().size() > 0)
                .collect(Collectors.toList());
        nstAddresses.addAll(addressList);
        countStatistics();

        rootLayoutController.showErrorMessage("");
    }

    /*
    Действия при выборе rjkam
     */
    private void selectedCity(String cityName) {
        citySelected = cityName;
        rootLayoutController.showErrorMessage("");
        getNstAddresses().clear();
        getCheckedRows().clear();

        List<NstAddress> addressList = container.getNstAddresses().stream()
                .filter(nstAddress -> nstAddress.getObl().equals(oblSelected))
                .filter(nstAddress -> nstAddress.getCity().equals(citySelected))
                .filter(nstAddress -> nstAddress.getPhotoList().size() > 0)
                .collect(Collectors.toList());
        nstAddresses.addAll(addressList);

        rootLayoutController.showErrorMessage("");
    }

    /*
    Очистка заполнения полей
     */
    @FXML
    public void clearCheckBoxes() {
        visitCountChoiceBox.getSelectionModel().clearSelection();
        noMatrixMZ.setSelected(false);
        havePhotoMZ.setSelected(false);
        bordersPhotoMZ.setSelected(false);
        centerPhotoMZ.setSelected(false);
        _30PhotoMZ.setSelected(false);
        vertPhotoMZ.setSelected(false);
        textCommentMZ.clear();
        commentMZ.getSelectionModel().clearSelection();
        noMatrixKS.setSelected(false);
        havePhotoKS.setSelected(false);
        bordersPhotoKS.setSelected(false);
        centerPhotoKS.setSelected(false);
        _30PhotoKS.setSelected(false);
        vertPhotoKS.setSelected(false);
        textCommentKS.clear();
        commentKS.getSelectionModel().clearSelection();
        noMatrixM.setSelected(false);
        havePhotoM.setSelected(false);
        bordersPhotoM.setSelected(false);
        centerPhotoM.setSelected(false);
        vertPhotoM.setSelected(false);
        textCommentM.clear();
        commentM.getSelectionModel().clearSelection();
        //oos.setSelected(false);
        /*comment.setText("");*/
        changesSavedLabel.setText("");
    }

    @Override
    public void clearCheckedRows() {
        getCheckedRows().clear();
        for (NstAddress addressTT : getNstAddresses()) {
            for (NstPhoto photo : (ArrayList<NstPhoto>) addressTT.getPhotoList()) {
                photo.setChecked(false);
            }
            addressTT.setChecked("0");
        }
    }

    private void setCheckBoxesFromFile(NstAddress nstAddress) {

        if (nstAddress.getVisitCount() > 0) {
            visitCountChoiceBox.getSelectionModel().select(Integer.valueOf(nstAddress.getVisitCount()));
        } else {
            visitCountChoiceBox.getSelectionModel().clearSelection();
        }

        noMatrixMZ.setSelected(nstAddress.isNoMatrixMZ());
        havePhotoMZ.setSelected(nstAddress.isHavePhotoMZ());
        bordersPhotoMZ.setSelected(nstAddress.isBordersPhotoMZ());
        centerPhotoMZ.setSelected(nstAddress.isCenterPhotoMZ());
        _30PhotoMZ.setSelected(nstAddress.is_30PhotoMZ());
        vertPhotoMZ.setSelected(nstAddress.isVertPhotoMZ());
        textCommentMZ.setText(nstAddress.getCommentMZ());

        noMatrixKS.setSelected(nstAddress.isNoMatrixKS());
        havePhotoKS.setSelected(nstAddress.isHavePhotoKS());
        bordersPhotoKS.setSelected(nstAddress.isBordersPhotoKS());
        centerPhotoKS.setSelected(nstAddress.isCenterPhotoKS());
        _30PhotoKS.setSelected(nstAddress.is_30PhotoKS());
        vertPhotoKS.setSelected(nstAddress.isVertPhotoKS());
        textCommentKS.setText(nstAddress.getCommentKS());

        noMatrixM.setSelected(nstAddress.isNoMatrixM());
        havePhotoM.setSelected(nstAddress.isHavePhotoM());
        bordersPhotoM.setSelected(nstAddress.isBordersPhotoM());
        centerPhotoM.setSelected(nstAddress.isCenterPhotoM());
        /*_30PhotoM.setSelected(nstAddress.is_30PhotoM());*/
        vertPhotoM.setSelected(nstAddress.isVertPhotoM());
        textCommentM.setText(nstAddress.getCommentM());

        //oos.setSelected(nstAddress.isOos());
        /*comment.setText(nstAddress.getComment());*/

        /*if (tmaGridPane.getRowConstraints().size() > 1 && nstAddress.getTmaActivityList() != null && nstAddress.getTmaActivityList().size() > 0) {
            int activityCount = (tmaGridPane.getChildren().size() - 5) / 8;
            for (int i = 0; i < activityCount; i++) {
                String tgName = ((Label) tmaGridPane.getChildren().get(i * 8 + 6)).getText();
                String tmaName = ((Label) tmaGridPane.getChildren().get(i * 8 + 7)).getText();
                TMAActivity tmaActivity = new TMAActivity(null, null, tgName, tmaName, null, null, false, false);
                if (nstAddress.getTmaActivityList().contains(tmaActivity)) {
                    TMAActivity savedActivity = nstAddress.getTmaActivityList().get(nstAddress.getTmaActivityList().indexOf(tmaActivity));
                    ((CheckBox) tmaGridPane.getChildren().get(i * 8 + 8)).setSelected(savedActivity.isSalePrice());
                    ((CheckBox) tmaGridPane.getChildren().get(i * 8 + 9)).setSelected(savedActivity.isHasOos());
                }
            }
        }*/
    }

    @FXML
    private void saveAddress() {

        rootLayoutController.showErrorMessage("");

        int addressIndex = addressTable.getSelectionModel().getSelectedIndex();
        if (addressIndex == -1) {
            rootLayoutController.showErrorMessage("Не выбрана точка для сохранения");
            return;
        }

        if (visitCountChoiceBox.getSelectionModel().isEmpty()) {
            rootLayoutController.showErrorMessage("Не выбрано количество посещений");
            return;
        }

        NstAddress addressTT = getNstAddresses().get(addressIndex);

        NstAddress globalNstAddress = container.getNstAddresses().get(container.getNstAddresses().indexOf(addressTT));
        globalNstAddress.setAllCriterias(visitCountChoiceBox.getSelectionModel().getSelectedItem(), noMatrixMZ.isSelected(), havePhotoMZ.isSelected(), bordersPhotoMZ.isSelected(), centerPhotoMZ.isSelected(),
                _30PhotoMZ.isSelected(), vertPhotoMZ.isSelected(), textCommentMZ.getText(),
                noMatrixKS.isSelected(), havePhotoKS.isSelected(), bordersPhotoKS.isSelected(), centerPhotoKS.isSelected(),
                _30PhotoKS.isSelected(), vertPhotoKS.isSelected(), textCommentKS.getText(),
                noMatrixM.isSelected(), havePhotoM.isSelected(), bordersPhotoM.isSelected(), centerPhotoM.isSelected(),
                vertPhotoM.isSelected(), textCommentM.getText()
                /*oos.isSelected(),*//*comment.getText().replace("\n", " ")*/);
        ArrayList<TMAActivity> tmaActivities = new ArrayList<>();
        /*if (tmaGridPane.getRowConstraints().size() > 1) {
            int activityCount = (tmaGridPane.getChildren().size() - 5) / 8;
            for (int i = 0; i < activityCount; i++) {
                String tgName = ((Label) tmaGridPane.getChildren().get(i * 8 + 6)).getText();
                String tmaName = ((Label) tmaGridPane.getChildren().get(i * 8 + 7)).getText();
                boolean salePrice = ((CheckBox) tmaGridPane.getChildren().get(i * 8 + 8)).isSelected();
                boolean oos = ((CheckBox) tmaGridPane.getChildren().get(i * 8 + 9)).isSelected();
                TMAActivity activity = new TMAActivity(null, null, tgName, tmaName, null, null, salePrice, oos);
                tmaActivities.add(activity);
            }
        }*/

        globalNstAddress.setChecked("1");

        globalNstAddress.setTmaActivityList(tmaActivities);

        globalNstAddress.setSavedDate(LocalDate.now());

        getCheckedRows().add(addressTable.getSelectionModel().getSelectedIndex());
        for (NstPhoto photo : (ArrayList<NstPhoto>) globalNstAddress.getPhotoList()) {
            photo.setChecked(true);
        }
        for (Node node : tilePane.getChildren()) {
            VBox vBox = (VBox) node;
            vBox.getChildren().get(1).setStyle("-fx-background-color: lightgreen");
        }

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(pathToSave + "1/" + oblSelected.replace(" ", "_") + ".dat"));
            JAXBContext context = JAXBContext.newInstance(NstContainer.class, NstAddress.class, NstPhoto.class, TMAActivity.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(container, writer);
            writer.flush();
            writer.close();
        } catch (JAXBException | IOException e) {
            Logger.log(e.getMessage());
            e.printStackTrace();
        }

        changesSavedLabel.setText("Изменения сохранены");
        countStatistics();
    }

    private void setMzGroupEnable(boolean isEnable) {
        bordersPhotoMZ.setDisable(!isEnable);
        //centerPhotoMZ.setDisable(!isEnable);
        //_30PhotoMZ.setDisable(!isEnable);
        vertPhotoMZ.setDisable(!isEnable);

        if (!isEnable) {
            bordersPhotoMZ.setSelected(false);
            //centerPhotoMZ.setSelected(false);
            //_30PhotoMZ.setSelected(false);
            vertPhotoMZ.setSelected(false);
        }
    }

    private void setKsGroupEnable(boolean isEnable) {
        bordersPhotoKS.setDisable(!isEnable);
        //centerPhotoKS.setDisable(!isEnable);
        //_30PhotoKS.setDisable(!isEnable);
        vertPhotoKS.setDisable(!isEnable);

        if (!isEnable) {
            bordersPhotoKS.setSelected(false);
            //centerPhotoKS.setSelected(false);
            //_30PhotoKS.setSelected(false);
            vertPhotoKS.setSelected(false);
        }
    }

    private void setMGroupEnable(boolean isEnable) {
        bordersPhotoM.setDisable(!isEnable);
        //centerPhotoM.setDisable(!isEnable);
        /*_30PhotoM.setDisable(!isEnable);*/
        vertPhotoM.setDisable(!isEnable);

        if (!isEnable) {
            bordersPhotoM.setSelected(false);
            //centerPhotoM.setSelected(false);
            /*_30PhotoM.setSelected(false);*/
            vertPhotoM.setSelected(false);
        }
    }

    private void setMzBordersEnable(boolean isEnable) {
        centerPhotoMZ.setDisable(!isEnable);
        _30PhotoMZ.setDisable(!isEnable);
        if (!isEnable) {
            centerPhotoMZ.setSelected(false);
            _30PhotoMZ.setSelected(false);
        }
    }

    private void setKsBordersEnable(boolean isEnable) {
        centerPhotoKS.setDisable(!isEnable);
        _30PhotoKS.setDisable(!isEnable);
        if (!isEnable) {
            centerPhotoKS.setSelected(false);
            _30PhotoKS.setSelected(false);
        }
    }

    private void setMBordersEnable (boolean isEnable) {
        centerPhotoM.setDisable(!isEnable);
        if (!isEnable) {
            centerPhotoM.setSelected(false);
        }
    }

    /*private void clearTMAActivityPane() {
        for (int i = tmaGridPane.getChildren().size() - 1; i > 4; i--) {
            tmaGridPane.getChildren().remove(i);
        }

        for (int i = tmaGridPane.getRowConstraints().size() - 1; i > 0; i--) {
            tmaGridPane.getRowConstraints().remove(i);
        }
    }*/

    /*private void fillUpTMAActivityPane() {
        ApachePoi apachePoi = ApachePoiManager.getInstance();
        ArrayList<TMAActivity> tmaList = null;
        try {
            tmaList = apachePoi.getTMAActivityFromFile(rootLayoutController.getPhotoTypeSelected(), "Тандер",
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
    }*/

    /*private void enableTMAbyTG(String tg, boolean isEnable) {
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
    }*/

    private WebDriver driver;
    private WebElement oblElement;
    private WebElement cityElement;
    private WebElement shopElement;
    private Robot robot;
    private String sDateFrom;
    private String sDateTo;
    private String enDateFrom;
    private String enDateTo;
    private String downloadFilepath;
    private String pathToSave;

    public WebDriver getDriver() {
        return driver;
    }

    List<NstPhoto> allPhotos = new ArrayList<>();
    List<String> oblsToOperate;

    public void setChosenObls(List<String> oblsToDownload) {
        this.oblsToOperate = oblsToDownload;
    }

    private void getCookieFirefox() throws InterruptedException, AWTException {
        System.setProperty("webdriver.gecko.driver", new File("").getAbsolutePath() + "/GeckoDriver/geckodriver.exe");

        FirefoxProfile profile = new FirefoxProfile();
        profile.setAssumeUntrustedCertificateIssuer(false);
        //profile.setEnableNativeEvents(false);

        profile.setPreference("browser.helperApps.neverAsk.saveToDisk", "image/jpeg");
        profile.setPreference("browser.helperApps.alwaysAsk.force", false);



        driver = new FirefoxDriver(profile);

        driver.get("http://server.nsteam.ru/merchandising/");
        Thread.sleep(1000);

        robot = new Robot();

        WebElement element;

        waitForJSandJQueryToLoad();

        element = driver.findElement(By.id("loginform-username"));
        element.sendKeys("nefis");
        element = driver.findElement(By.id("loginform-password"));
        element.sendKeys("6433929966");
        element.submit();

        Thread.sleep(5000);
        waitForJSandJQueryToLoad();

        PHP_SESS_ID = driver.manage().getCookieNamed("PHPSESSID").getValue();
        IDENTITY = driver.manage().getCookieNamed("_identity").getValue();

        driver.quit();
    }

    /*
    * @param downloadType:
    * 0 - obls
    * 1 - shops
    * */
    private void downloadPhotos(int i1) throws AWTException, InterruptedException {

        allPhotos.clear();
        container.clear();

        canContinue = true;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Вы собираетесь загрузить фотографии с сервера НСТ. \n" +
                "Это может занять достаточно много времени (в зависимости от скорости соединения и количества загружаемых областей). " +
                "Запускайте загрузку только если располагаете достаточным временем. \n" +
                "Для загрузки исользуется браузер Google Chrome. Убедитесь, что он у Вас установлен и обновлен до последней версии. \n" +
                "Во время загрузки не совершайте на компьютере никаких действий, иначе работа программы и браузера непредсказуемы. \n" +
                "В следующем окне через несколько секунд выберите только те области, которые нужны для работы. Вы всегда сможете догрузить другие. \n" +
                "Ранее загруженные области в списке областей для загрузки отображаться не будут. Если необходимо их перезагрузить, то сначала удалите область.\n");
        alert.setHeaderText("Внимание! Прочтите внимательно сообщение ниже");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {

            } else {
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION, "Загрузка отменена.");
                alert1.setHeaderText(null);
                alert1.showAndWait();
                canContinue = false;
            }
        });

        if (!canContinue) {
            rootLayoutController.showErrorMessage("");
            return;
        }

        System.setProperty("webdriver.chrome.driver", new File("").getAbsolutePath() + "/ChromeDriver/chromedriver.exe");

        downloadFilepath = new File("resource").getAbsolutePath() + "/" + sDateFrom + "-" + sDateTo + "/tmp_images";
        HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
        chromePrefs.put("profile.default_content_settings.popups", 0);
        chromePrefs.put("download.default_directory", downloadFilepath);
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", chromePrefs);
        DesiredCapabilities cap = DesiredCapabilities.chrome();
        cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        cap.setCapability(ChromeOptions.CAPABILITY, options);

        driver = new ChromeDriver(cap);

        driver.manage().timeouts().setScriptTimeout(1l, TimeUnit.MINUTES);
        driver.manage().timeouts().pageLoadTimeout(1l, TimeUnit.MINUTES);

        driver.get("http://server.nsteam.ru/merchandising/");
        Thread.sleep(1000);
        waitForJSandJQueryToLoad();

        robot = new Robot();

        WebElement element;

        // Авторизация

        element = driver.findElement(By.id("loginform-username"));
        element.sendKeys("nefis");
        element = driver.findElement(By.id("loginform-password"));
        element.sendKeys("6433929966");
        element.submit();

        Thread.sleep(5000);
        waitForJSandJQueryToLoad();

        downloadByShopFormat(1);
        relogin();
        downloadByShopFormat(2);

        driver.quit();

        Alert alert2 = new Alert(Alert.AlertType.INFORMATION, "Данные успешно загружены");
        alert2.setHeaderText(null);
        alert2.showAndWait();
    }

    private void relogin() throws InterruptedException {

        while(true) {
            driver.findElement(By.xpath("//form[@class='navbar-form']/button")).click();

            Thread.sleep(1000);
            waitForJSandJQueryToLoad();

            if (driver.findElements(By.id("loginform-username")).size() != 0) {
                break;
            }
        }

        WebElement element;
        element = driver.findElement(By.id("loginform-username"));
        element.sendKeys("nefis");
        element = driver.findElement(By.id("loginform-password"));
        element.sendKeys("6433929966");
        element.submit();

        Thread.sleep(5000);
        waitForJSandJQueryToLoad();
    }


    private void downloadByShopFormat(int shopFormat) throws InterruptedException {

        WebElement element;
        WebElement photoLink;

        photoLink = driver.findElement(By.linkText("Список фотографий"));
        photoLink.click();
        Thread.sleep(5000);

        waitForJSandJQueryToLoad();

        // Даты отчета

        element = driver.findElement(By.id("selectoroptions-start_date"));
        element.clear();
        enDateFrom = DateUtil.formatInEnStyle(DateUtil.parse(sDateFrom));
        element.sendKeys(enDateFrom);

        ((JavascriptExecutor) driver).executeScript("$('#ui-datepicker-div').css('display', 'none');");

        element = driver.findElement(By.id("selectoroptions-end_date"));
        element.clear();
        enDateTo = DateUtil.formatInEnStyle(DateUtil.parse(sDateTo));
        element.sendKeys(enDateTo);

        ((JavascriptExecutor) driver).executeScript("$('#ui-datepicker-div').css('display', 'none');");

        // Стандартные поля (Для ММ)
        ((JavascriptExecutor) driver).executeScript("$('#form-input-menu').css('display', 'block');");

        driver.findElement(By.xpath("//span[@aria-labelledby='select2-project-container']/../..")).click();
        Thread.sleep(300);
        switch (shopFormat) {
            case 1:
                driver.findElement(By.xpath("//ul[@id='select2-project-results']/li[normalize-space(text())='НЭФИС-БИОПРОДУКТ ТТ']")).click();
                break;
            case 2:
                driver.findElement(By.xpath("//ul[@id='select2-project-results']/li[normalize-space(text())='Нэфис-Биопродукт ГМ']")).click();
                break;
        }
        waitForJSandJQueryToLoad();

        PHP_SESS_ID = driver.manage().getCookieNamed("PHPSESSID").getValue();
        IDENTITY = driver.manage().getCookieNamed("_identity").getValue();

        // Очистка полей, если заполнены

        if (driver.findElements(By.xpath("//span[@id='select2-chain_store-container']/span[@class='select2-selection__clear']")).size() != 0) {
            driver.findElement(By.xpath("//span[@id='select2-chain_store-container']/span[@class='select2-selection__clear']")).click();
            Thread.sleep(50);
            waitForJSandJQueryToLoad();
        }

        if (driver.findElements(By.xpath("//span[@id='select2-region-container']/span[@class='select2-selection__clear']")).size() != 0) {
            driver.findElement(By.xpath("//span[@id='select2-region-container']/span[@class='select2-selection__clear']")).click();
            Thread.sleep(50);
            waitForJSandJQueryToLoad();
        }

        if (driver.findElements(By.xpath("//span[@id='select2-city-container']/span[@class='select2-selection__clear']")).size() != 0) {
            driver.findElement(By.xpath("//span[@id='select2-city-container']/span[@class='select2-selection__clear']")).click();
            Thread.sleep(50);
            waitForJSandJQueryToLoad();
        }

        if (driver.findElements(By.xpath("//span[@id='select2-store-container']/span[@class='select2-selection__clear']")).size() != 0) {
            driver.findElement(By.xpath("//span[@id='select2-store-container']/span[@class='select2-selection__clear']")).click();
            Thread.sleep(50);
            waitForJSandJQueryToLoad();
        }

        driver.findElement(By.xpath("//span[@aria-labelledby='select2-chain_store-container']/../..")).click();
        Thread.sleep(300);
        driver.findElement(By.xpath("//ul[@id='select2-chain_store-results']/li[normalize-space(text())='Магнит']")).click();
        waitForJSandJQueryToLoad();

        // Список доступных областей

        driver.findElement(By.xpath("//span[@aria-labelledby='select2-region-container']/../..")).click();
        Thread.sleep(1000);
        oblElement = driver.findElement(By.xpath("//ul[@id='select2-region-results']"));

        String oblContentS = (String) ((JavascriptExecutor) driver).executeScript("return arguments[0].outerHTML;", oblElement);
        Document oblContent = Jsoup.parse(oblContentS);

        Map<String, ArrayList<String>> oblMap = new TreeMap<>();
        Elements oblElements = oblContent.getElementsByTag("li");

        for (int i = 0; i < oblElements.size(); i++) {
            oblMap.put(oblElements.get(i).text(), new ArrayList<>());
        }

        driver.findElement(By.xpath("//span[@aria-labelledby='select2-region-container']/../..")).click();

        // Отображение окошка с отметкой необходимых областей

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/nst/OblDialog.fxml"));
            AnchorPane oblChoosePane = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Выбор областей");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(main.getPrimaryStage());
            Scene scene = new Scene(oblChoosePane);
            dialogStage.setScene(scene);
            dialogStage.setAlwaysOnTop(true);

            OblDialogController oblDialogController = loader.getController();
            if (shopFormat == 1) {
                oblDialogController.addOblsToScrollPane(new ArrayList<>(oblMap.keySet()), oblList.subList(0, oblList.size()), "Пожалуйста, выберите загружаемые области:");
            } else if (shopFormat == 2) {
                oblDialogController.addOblsToScrollPane(new ArrayList<>(oblMap.keySet()), oblGmList, "Пожалуйста, выберите загружаемые области:");
            }

            oblDialogController.setNstLayoutController(this);
            oblDialogController.setDialogStage(dialogStage);
            dialogStage.showAndWait();

            if (oblsToOperate == null) canContinue = false;

        } catch (IOException e) {
            Logger.log(e.getMessage());
            e.printStackTrace();
            return;
        }

        if (!canContinue) {
            driver.quit();
            rootLayoutController.showErrorMessage("");
            return;
        }

        System.out.println(oblsToOperate);

        Thread.sleep(300);

        Alert alert1 = new Alert(Alert.AlertType.INFORMATION, "Пожалуйста, активируйте окошко браузера");
        alert1.setHeaderText(null);
        alert1.showAndWait();

        Thread.sleep(5000);

        // Перебор областей

        oblElement = driver.findElement(By.xpath("//span[@aria-labelledby='select2-region-container']/../.."));
        //cityElement = driver.findElement(By.id("dropdown_city_chosen"));
        shopElement = driver.findElement(By.xpath("//span[@aria-labelledby='select2-store-container']/../.."));

        int counter = 1;

        for (Map.Entry<String, ArrayList<String>> pair : oblMap.entrySet()) {
            if (!oblsToOperate.contains(pair.getKey())) continue;

            ((JavascriptExecutor) driver).executeScript("$('#form-input-menu').css('display', 'block');");

            // Поочередный выбор областей

            oblElement = driver.findElement(By.xpath("//span[@aria-labelledby='select2-region-container']/../.."));
            oblElement.click();
            Thread.sleep(300);
            driver.findElement(By.xpath("//ul[@id='select2-region-results']/li[normalize-space(text())='" + pair.getKey() + "']")).click();
            waitForJSandJQueryToLoad();

            container.clear();

            // Очистка полей, если заполнены

            if (driver.findElements(By.xpath("//span[@id='select2-city-container']/span[@class='select2-selection__clear']")).size() != 0) {
                driver.findElement(By.xpath("//span[@id='select2-city-container']/span[@class='select2-selection__clear']")).click();
                Thread.sleep(50);
                waitForJSandJQueryToLoad();
            }

            if (driver.findElements(By.xpath("//span[@id='select2-store-container']/span[@class='select2-selection__clear']")).size() != 0) {
                driver.findElement(By.xpath("//span[@id='select2-store-container']/span[@class='select2-selection__clear']")).click();
                Thread.sleep(50);
                waitForJSandJQueryToLoad();
            }

            // Список магазинов в области

            driver.findElement(By.xpath("//span[@aria-labelledby='select2-store-container']/../..")).click();
            Thread.sleep(300);
            shopElement = driver.findElement(By.xpath("//ul[@id='select2-store-results']"));

            String htmlContentS = (String) ((JavascriptExecutor) driver).executeScript("return arguments[0].outerHTML;", shopElement);
            Document htmlContent = Jsoup.parse(htmlContentS);

            Elements jShopElements = htmlContent.getElementsByTag("li");

            List<String> shopList = new ArrayList<>();

            for (int i = 0; i < jShopElements.size(); i++) {
                shopList.add(jShopElements.get(i).text());
            }

            driver.findElement(By.xpath("//span[@aria-labelledby='select2-store-container']/../..")).click();

            ((JavascriptExecutor) driver).executeScript("$('#form-input-menu').find('button').click();");

            // Загрузка фото по области

            try {
                downloadPhotosFromCity(pair.getKey(), null);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
                driver.quit();
                break;
            }

            Thread.sleep(300);

            // все магазины с фото

            List<String> shopWithPhotoList = allPhotos.stream()
                    .map(photo -> photo.getShop())
                    .distinct()
                    .collect(Collectors.toList());

            String oblName = pair.getKey();

            if (shopFormat == 2) {
                oblName = checkForOblRename(oblName);
            }

            // Создание адресов и присваивание фото

            for (String shop : shopList) {
                if (shopWithPhotoList.contains(shop)) {
                    NstPhoto examplePhoto = allPhotos.stream()
                            .filter(nstPhoto -> nstPhoto.getShop().equals(shop))
                            .findFirst()
                            .get();

                    List<NstPhoto> allPhotoInShop = allPhotos.stream()
                            .filter(nstPhoto -> nstPhoto.getShop().equals(shop))
                            .collect(Collectors.toList());

                    List<TMAActivity> tmaActivityList = new ArrayList<>();

                    NstAddress nstAddress = new NstAddress(examplePhoto.getObl(), examplePhoto.getCity(), shop, true, allPhotoInShop, tmaActivityList,
                            "0", 0, false, false, false, false, false, false, null,
                            false, false, false, false, false, false, null,
                            false, false, false, false, false, null, null, shopFormat);

                    if (!container.contains(nstAddress)) {
                        container.add(nstAddress);
                    }
                } else {
                    NstAddress nstAddress = new NstAddress(oblName, null, shop, false, new ArrayList<>(), new ArrayList<>(),
                            "0", 0, false, false, false, false, false, false, null,
                            false, false, false, false, false, false, null,
                            false, false, false, false, false, null, null, shopFormat);

                    if (!container.contains(nstAddress)) {
                        container.add(nstAddress);
                    }
                }
            }

            for (String testName : shopWithPhotoList) {
                if (!shopList.contains(testName)) {
                    System.out.println("нет магазина " + testName);
                }
            }

            new File(pathToSave + "1/").mkdirs();
            new File(pathToSave + "2/").mkdirs();

            System.out.println("[" + counter + "/" + oblMap.size() + "] " + (shopFormat == 1 ? "ММ: " : "ГМ: ") + oblName + ": " + shopList.size() + " магазинов, " + allPhotos.size() + " фотографий.");
            counter++;

            allPhotos.clear();

            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(pathToSave + shopFormat + "/" + oblName.replace(" ", "_") + ".dat"));
                JAXBContext context = JAXBContext.newInstance(NstContainer.class, NstAddress.class, NstPhoto.class, TMAActivity.class);
                Marshaller marshaller = context.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                marshaller.marshal(container, writer);
                writer.flush();
                writer.close();
            } catch (JAXBException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String checkForOblRename(String key) {
        if (key.equals("Каснодарский край") || key.equals("Краснодарский Край") || key.equals("Краснодарский край")) {
            return renameOblInPhotos("Краснодарский край");
        }
        if (key.equals("Курганинская область") || key.equals("Курганская область")) {
            return renameOblInPhotos("Курганская область");
        }
        if (key.equals("Республика Карачаево-Черкессия") || key.equals("Карачаево-Черкесская Республика")) {
            return renameOblInPhotos("Карачаево-Черкесская Республика");
        }
        if (key.equals("Республика Северная Осетия-Алания") || key.equals("Республика Северная Осетия - Алания")) {
            return renameOblInPhotos("Республика Северная Осетия - Алания");
        }
        if (key.equals("Республика Удмуртская") || key.equals("Удмуртская область")
                || key.equals("Удмуртская республика") || key.equals("Удмуртская Республика")) {
            return renameOblInPhotos("Удмуртская Республика");
        }
        if (key.equals("Республика Чувашия") || key.equals("Чувашская Республика")) {
            return renameOblInPhotos("Чувашская Республика");
        }
        if (key.equals("Республики Башкортостан") || key.equals("Республика Башкортостан")) {
            return renameOblInPhotos("Республика Башкортостан");
        }
        if (key.equals("Ульяновская обл") || key.equals("Ульяновская обл.") || key.equals("Ульяновская область")) {
            return renameOblInPhotos("Ульяновская область");
        }
        return key;
    }

    private String renameOblInPhotos(String s) {
        allPhotos.forEach(nstPhoto -> nstPhoto.setObl(s));
        try {
            File oblFile = new File(pathToSave + "2/" + s.replace(" ", "_") + ".dat");
            if (!oblFile.exists()) return s;

            BufferedReader reader = new BufferedReader(new FileReader(oblFile));
            JAXBContext context = JAXBContext.newInstance(NstContainer.class, NstAddress.class, NstPhoto.class, TMAActivity.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            container = (NstContainer) unmarshaller.unmarshal(reader);
            reader.close();
            return s;
        } catch (IOException | JAXBException e) {
            Logger.log(e.getMessage());
            e.printStackTrace();
            rootLayoutController.showErrorMessage("Не удается открыть файл.");
        }
        return s;
    }

    private boolean downloadPhotosFromCity(String obl, String city) throws InterruptedException, IOException {

        Thread.sleep(1000);

        ((JavascriptExecutor) driver).executeScript("$('#w0-togdata-page').click();");

        Thread.sleep(1000);
        waitForJSandJQueryToLoad();
        Thread.sleep(2000);

        if (driver.findElements(By.xpath("//body/div[@id='w1']")).size() != 0) {
            driver.findElement(By.xpath("//body/div[@id='w1']//button[@class='btn btn-warning']")).click();
            Thread.sleep(1000);
            waitForJSandJQueryToLoad();
        }

        WebElement photoTable = driver.findElement(By.tagName("table"));

        String htmlContentS = (String) ((JavascriptExecutor) driver).executeScript("return arguments[0].outerHTML;", photoTable);
        Document htmlContent = Jsoup.parse(htmlContentS);

        Elements photoElems = htmlContent.getElementsByTag("tr");

        if (photoElems.size() == 4 && !photoElems.get(2).hasAttr("data-key")) {
            return true;
        }

        for (int i = 2; i < photoElems.size() - 1; i++) {
            Element element = photoElems.get(i);
            downloadOnePhoto(obl, city, element);
        }

        return true;
    }

    public boolean waitForJSandJQueryToLoad() {

        WebDriverWait wait = new WebDriverWait(driver, 30);

        getDriver().manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);

        // wait for jQuery to load
        ExpectedCondition<Boolean> jQueryLoad = new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    return ((Long) ((JavascriptExecutor) getDriver()).executeScript("return jQuery.active") == 0);
                } catch (Exception e) {
                    // no jQuery present
                    return true;
                }
            }
        };

        // wait for Javascript to load
        ExpectedCondition<Boolean> jsLoad = new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return ((JavascriptExecutor) getDriver()).executeScript("return document.readyState")
                        .toString().equals("complete");
            }
        };

        return wait.until(jQueryLoad) && wait.until(jsLoad);
    }

    private void downloadOnePhoto(String obl, String city, Element photoElement) throws InterruptedException, IOException {
        //String photoUrl = photoElement.findElement(By.tagName("a")).getAttribute("href").replace("&amp;", "&");
        String photoUrl = photoElement.getElementsByTag("a").attr("href");
        String fullPhotoUrl = photoUrl.replace("&sw=1280&sh=600", "");
        String photoDate = photoElement.getElementsByAttributeValue("data-col-seq", "3").text();
        String shopName = photoElement.getElementsByAttributeValue("data-col-seq", "2").text();
        NstPhoto nstPhoto = new NstPhoto(null, obl, city, shopName, photoUrl, fullPhotoUrl, LocalDate.parse(photoDate));
        allPhotos.add(nstPhoto);
    }

    private boolean isStillWaiting(By by) {
        boolean isWaiting = true;
        while (isWaiting) {
            try {
                WebElement photoLink1 = driver.findElement(by);
                isWaiting = false;
            } catch (NoSuchElementException e) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                continue;
            }
        }
        return !isWaiting;
    }

    @FXML
    public void loadNewObls() {
        try {
            downloadPhotos(0);
        } catch (AWTException e) {
            if (driver != null) driver.quit();
            e.printStackTrace();
        } catch (InterruptedException e) {
            if (driver != null) driver.quit();
            e.printStackTrace();
        } catch (StaleElementReferenceException e) {
            if (driver != null) driver.quit();
            e.printStackTrace();
        }
        fillOblList();
    }

    @FXML
    public void load10Percents() {
        try {
            downloadPhotos(1);
        } catch (AWTException e) {
            if (driver != null) driver.quit();
            e.printStackTrace();
        } catch (InterruptedException e) {
            if (driver != null) driver.quit();
            e.printStackTrace();
        } catch (StaleElementReferenceException e) {
            if (driver != null) driver.quit();
            e.printStackTrace();
        }
        fillOblList();
    }

    @FXML
    public void removeObls() {
        canContinue = false;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Внимание! Удаление области удалит также все сохраненные данные по выбранной области " +
                "(фотографии, параметры оценки, комментарии и т.д.) без возможности восстановления.");
        alert.setHeaderText("Вы уверены, что хотите удалить область?");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                canContinue = true;
            }
        });

        if (!canContinue) {
            rootLayoutController.showErrorMessage("");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/nst/OblDialog.fxml"));
            AnchorPane oblChoosePane = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Выбор областей");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(main.getPrimaryStage());
            Scene scene = new Scene(oblChoosePane);
            dialogStage.setScene(scene);
            dialogStage.setAlwaysOnTop(true);

            OblDialogController oblDialogController = loader.getController();

            Set<String> oblSetRes = new TreeSet<>();
            Set<String> oblSet = oblList.subList(0, oblList.size()).stream().collect(Collectors.toSet());
            oblSetRes.addAll(oblSet);

            oblDialogController.addOblsToScrollPane(new ArrayList<>(oblSetRes), new ArrayList<>(), "Пожалуйста, выберите области для удаления:");

            oblDialogController.setNstLayoutController(this);
            oblDialogController.setDialogStage(dialogStage);
            dialogStage.showAndWait();

            if (oblsToOperate == null) canContinue = false;

        } catch (IOException e) {
            Logger.log(e.getMessage());
            e.printStackTrace();
            return;
        }

        if (!canContinue) {
            rootLayoutController.showErrorMessage("");
            return;
        }

        for (String oblToRemove : oblsToOperate) {
            recursiveDeleteOblPhotos(new File(pathToSave + 1 + "/" + oblToRemove.replace(" ", "_") + ".dat"));
            recursiveDeleteOblPhotos(new File(pathToSave + 2 + "/" + oblToRemove.replace(" ", "_") + ".dat"));
        }

        /*List<NstAddress> addressesToRemove = container.getNstAddresses().stream()
                .filter(nstAddress -> oblsToOperate.contains(nstAddress.getObl()))
                .collect(Collectors.toList());

        for (NstAddress nstAddress : addressesToRemove) {
            for (NstPhoto photo : nstAddress.getPhotoList()) {
                new File(photo.getUrl()).delete();
            }
            container.getNstAddresses().remove(nstAddress);
        }

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(pathToSave));
            JAXBContext context = JAXBContext.newInstance(NstContainer.class, NstAddress.class, NstPhoto.class, TMAActivity.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(container, writer);
            writer.flush();
            writer.close();
        } catch (JAXBException | IOException e) {
            e.printStackTrace();
        }*/

        fillOblList();

        Alert alert1 = new Alert(Alert.AlertType.INFORMATION, "Данные успешно удалены");
        alert1.setHeaderText(null);
        alert1.showAndWait();
    }

    @FXML
    public void prepareFileToUpload() {
        ArrayList<String> listFiles = new ArrayList<>();

        listFiles.addAll(Arrays.asList(new File("resource/" + sDateFrom + "-" + sDateTo + "/1").listFiles()).stream()
                .filter(file -> file.getName().endsWith(".dat"))
                .map(file -> file.getAbsolutePath())
                .collect(Collectors.toList()));

        listFiles.addAll(Arrays.asList(new File("resource/" + sDateFrom + "-" + sDateTo + "/2").listFiles()).stream()
                .filter(file -> file.getName().endsWith(".dat"))
                .map(file -> file.getAbsolutePath())
                .collect(Collectors.toList()));

        List<NstAddress> allAddresses = new ArrayList<>();

        for (String oblPath : listFiles) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(oblPath));
                JAXBContext context = JAXBContext.newInstance(NstContainer.class, NstAddress.class, NstPhoto.class, TMAActivity.class);
                Unmarshaller unmarshaller = context.createUnmarshaller();

                NstContainer container = (NstContainer) unmarshaller.unmarshal(reader);
                allAddresses.addAll(container.getNstAddresses());
                reader.close();

            } catch (IOException | JAXBException e) {
                Logger.log(e.getMessage());
                e.printStackTrace();
            }
        }

        Comparator<NstAddress> formatComparator = (o1, o2) -> o1.getShopFormat() - o2.getShopFormat();
        Comparator<NstAddress> oblComparator = (o1, o2) -> o1.getObl().compareTo(o2.getObl());
        Comparator<NstAddress> shopComparator = (o1, o2) -> o1.getName().compareTo(o2.getName());
        Comparator<NstAddress> addressComparator = formatComparator.thenComparing(oblComparator).thenComparing(shopComparator);

        allAddresses.sort(addressComparator);
        try (BufferedWriter writer = new BufferedWriter(new FileWriterWithEncoding(new File("resource/upload_" + sDateFrom + "-" + sDateTo + ".txt"), "UTF-8"))) {
            writer.write("--nst begin--");
            writer.newLine();
            for (NstAddress nstAddress : allAddresses) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(nstAddress.getShopFormat()).append(";")
                        .append(nstAddress.getObl()).append(";")
                        .append(nstAddress.getName()).append(";");
                if (nstAddress.isHasPhotos()) {
                    for (NstPhoto nstPhoto : nstAddress.getPhotoList()) {
                        StringBuilder stringBuilder1 = new StringBuilder();
                        stringBuilder1.append(nstPhoto.getDate()).append(";")
                                .append(nstPhoto.getFullUrl());
                        String record = stringBuilder.toString() + stringBuilder1.toString();
                        writer.write(record);
                        writer.newLine();
                    }
                } else {
                    String record = stringBuilder.toString() + "-;-";
                    writer.write(record);
                    writer.newLine();
                }
            }
            writer.write("--nst end--");
        } catch (IOException e) {
            e.printStackTrace();
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Сводный файл готов");
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    private void recursiveDeleteOblPhotos(File file) {
        if (file == null) return;
        if (file.isFile()) {
            file.delete();
        } else {
            for (File nextFile : file.listFiles()) {
                recursiveDeleteOblPhotos(nextFile);
            }
            file.delete();
        }
    }

    public void closeChromeDriver() {
        driver.quit();
    }

    private void countStatistics() {
        long allTT = container.getNstAddresses().stream()
                .filter(nstAddress -> nstAddress.getPhotoList().size() > 0)
                .count();
        long checkedTT = container.getNstAddresses().stream()
                .filter(nstAddress -> nstAddress.getPhotoList().size() > 0)
                .filter(nstAddress -> nstAddress.getChecked().equals("1"))
                .count();
        LocalDate today = LocalDate.now();
        long checkedToday = container.getNstAddresses().stream()
                .filter(nstAddress -> nstAddress.getPhotoList().size() > 0)
                .filter(nstAddress -> nstAddress.getSavedDate() != null)
                .filter(nstAddress -> nstAddress.getSavedDate().isEqual(today))
                .count();
        statAll.setText(Long.toString(allTT));
        statChecked.setText(Long.toString(checkedTT));
        statCheckedToday.setText(Long.toString(checkedToday));
    }
}
