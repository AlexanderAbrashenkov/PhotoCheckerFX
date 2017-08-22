package foto_verif;

import foto_verif.model.jdbc.JdbcModel;
import foto_verif.util.Logger;
import foto_verif.view.ChannelLayoutController;
import foto_verif.view.ExcelDialogController;
import foto_verif.view.RootLayoutController1;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.*;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main extends Application {

    private ObservableList<String> netList = FXCollections.observableArrayList();

    private Stage primaryStage;
    private Stage channelLayout;
    private RootLayoutController1 rootLayoutController;
    private ChannelLayoutController channelLayoutController;
    private JdbcModel jdbcModel;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    public ObservableList<String> getNetList() {
        return netList;
    }

    public void setNetList(ObservableList<String> netList) {
        this.netList = netList;
    }

    public JdbcModel getJdbcModel() {
        return jdbcModel;
    }

    public void setJdbcModel(JdbcModel jdbcModel) {
        this.jdbcModel = jdbcModel;
        this.jdbcModel.setMain(this);
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public ChannelLayoutController getChannelLayoutController() {
        return channelLayoutController;
    }

    public ExecutorService getExecutor() {
        return executor;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        this.primaryStage = primaryStage;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("view/RootLayout1.fxml"));

        primaryStage.setOnCloseRequest(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Вы действительно хотите выйти?");
            alert.setHeaderText(null);
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    if (jdbcModel != null)
                        jdbcModel.stop();
                    if (executor != null)
                        executor.shutdownNow();
                    Logger.writeLog();
                } else {
                    event.consume();
                }
            });
        });

        Parent root = (AnchorPane) loader.load();

        this.primaryStage.setTitle("Photo checker");
        this.primaryStage.getIcons().add(new Image("foto_verif/images/1474309109_geek_zombie.png"));
        this.primaryStage.setScene(new Scene(root));
        this.primaryStage.setMaximized(true);
        this.primaryStage.show();

        RootLayoutController1 controller = loader.getController();
        this.rootLayoutController = controller;
        controller.setMain(this);

        showGreetingLayout();

        String path = new File(".").getAbsolutePath();
        if (path.startsWith("X") || path.startsWith("Y") || path.startsWith("Z")) {
            System.exit(0);
        }
    }

    public void removeChannelLayout() {
        try {
            if (channelLayoutController != null) {
                channelLayoutController.getAddresses().clear();
                channelLayoutController.getCheckedRows().clear();
                channelLayoutController = null;
            }
            rootLayoutController.getChildPane().getChildren().remove(0);
        } catch (IndexOutOfBoundsException e) {
            /* NONE */
        }
    }

    public void showGreetingLayout() {
        try {
            try {
                removeChannelLayout();
            } catch (IndexOutOfBoundsException e) {
                /*NONE*/
            }

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/GreetingLayout.fxml"));
            AnchorPane greetingLayout = (AnchorPane) loader.load();

            rootLayoutController.getChildPane().getChildren().add(greetingLayout);
            greetingLayout.prefWidthProperty().bind(rootLayoutController.getChildPane().widthProperty());
            greetingLayout.prefHeightProperty().bind(rootLayoutController.getChildPane().heightProperty());
        } catch (IOException e) {
            Logger.log(e.getMessage());
            e.printStackTrace();
        }
    }

    public void showNKALayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/NKA/NKALayout.fxml"));
            SplitPane nkaLayout = (SplitPane) loader.load();
            try {
                removeChannelLayout();
            } catch (IndexOutOfBoundsException e) {
                /*NONE*/
            }
            rootLayoutController.getChildPane().getChildren().add(nkaLayout);
            nkaLayout.prefHeightProperty().bind(rootLayoutController.getChildPane().heightProperty());
            nkaLayout.prefWidthProperty().bind(rootLayoutController.getChildPane().widthProperty());
            channelLayoutController = loader.getController();
            channelLayoutController.setMain(this);
            channelLayoutController.setRootLayoutController(rootLayoutController);
        } catch (IOException e) {
            Logger.log(e.getMessage());
            e.printStackTrace();
        }
    }

    public void showDMPLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/dmp/DMPLayout.fxml"));
            SplitPane dmpLayout = (SplitPane) loader.load();

            try {
                removeChannelLayout();
            } catch (IndexOutOfBoundsException e) {
                /*NONE*/
            }

            rootLayoutController.getChildPane().getChildren().add(dmpLayout);
            dmpLayout.prefWidthProperty().bind(rootLayoutController.getChildPane().widthProperty());
            dmpLayout.prefHeightProperty().bind(rootLayoutController.getChildPane().heightProperty());
            channelLayoutController = loader.getController();
            channelLayoutController.setMain(this);
            channelLayoutController.setRootLayoutController(rootLayoutController);
        } catch (IOException e) {
            Logger.log(e.getMessage());
            e.printStackTrace();
        }
    }

    public void showDmpNkaLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/dmp_nka/DMPLayoutNka.fxml"));
            SplitPane dmpNkaLayout = (SplitPane) loader.load();

            try {
                removeChannelLayout();
            } catch (IndexOutOfBoundsException e) {
                /*NONE*/
            }

            rootLayoutController.getChildPane().getChildren().add(dmpNkaLayout);
            dmpNkaLayout.prefWidthProperty().bind(rootLayoutController.getChildPane().widthProperty());
            dmpNkaLayout.prefHeightProperty().bind(rootLayoutController.getChildPane().heightProperty());
            channelLayoutController = loader.getController();
            channelLayoutController.setMain(this);
            channelLayoutController.setRootLayoutController(rootLayoutController);
        } catch (IOException e) {
            Logger.log(e.getMessage());
            e.printStackTrace();
        }
    }

    public void showNstLayout() {
        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/nst/NstLayout.fxml"));
            SplitPane nstLayout = (SplitPane) loader.load();

            try {
                removeChannelLayout();
            } catch (IndexOutOfBoundsException e) {
                /*NONE*/
            }

            rootLayoutController.getChildPane().getChildren().add(nstLayout);
            nstLayout.prefWidthProperty().bind(rootLayoutController.getChildPane().widthProperty());
            nstLayout.prefHeightProperty().bind(rootLayoutController.getChildPane().heightProperty());
            channelLayoutController = loader.getController();
            channelLayoutController.setMain(this);
            channelLayoutController.setRootLayoutController(rootLayoutController);
        }catch (IOException e) {
            e.printStackTrace();
            Logger.log(e.getMessage());
        }
    }

    public void showLkaLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/lka/LKALayout.fxml"));
            SplitPane lkaLayout = (SplitPane) loader.load();

            try {
                removeChannelLayout();
            } catch (IndexOutOfBoundsException e) {
                /*NONE*/
            }

            rootLayoutController.getChildPane().getChildren().add(lkaLayout);
            lkaLayout.prefWidthProperty().bind(rootLayoutController.getChildPane().widthProperty());
            lkaLayout.prefHeightProperty().bind(rootLayoutController.getChildPane().heightProperty());
            channelLayoutController = loader.getController();
            channelLayoutController.setMain(this);
            channelLayoutController.setRootLayoutController(rootLayoutController);
        } catch (IOException e) {
            e.printStackTrace();
            Logger.log(e.getMessage());
        }
    }

    public void makeExcelReport() {
        try {
            // Загружаем диалоговое окно
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/ExcelDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Excel Report");
            dialogStage.getIcons().add(new Image("foto_verif/images/1474309109_geek_zombie.png"));
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            //Передаем адресата в контроллер
            ExcelDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);

            controller.setRootLayoutController(rootLayoutController);

            controller.setStartValues();

            // Отображаем диалоговое окно и ждем, пока пользователь его не закроет
            // или закроется программно
            dialogStage.showAndWait();
        } catch (IOException e) {
            Logger.log(e.getMessage());
            e.printStackTrace();
        }
    }

    public void saveAndShowTMATemplate() throws IOException {

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File("."));
        directoryChooser.setTitle("Сохранить в...");
        File selectedDirectory = directoryChooser.showDialog(primaryStage);
        if (selectedDirectory == null) {
            return;
        }

        String reportPath = selectedDirectory.getAbsolutePath() + "/tma-template.xlsx";
        File destFile = new File(reportPath);

        File sourceFile = new File("tma/tma_list.xlsx");
        InputStream is;
        if (sourceFile.exists()) {
            is = new FileInputStream(sourceFile);
        } else {
            is = Main.class.getResourceAsStream("resources/tma-template.xlsx");
        }
        OutputStream os = new FileOutputStream(destFile);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = is.read(buffer)) > 0) {
            os.write(buffer, 0, length);
        }
        is.close();
        os.close();

        Desktop.getDesktop().open(destFile);
    }

    public void importTMAList() throws IOException {
        File destFile = new File("tma/tma_list.xlsx");

        if (!destFile.exists()) {
            new File("tma").mkdirs();
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("."));
        fileChooser.setTitle("Имортировать файл из...");
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile == null) {
            return;
        }

        InputStream is = new FileInputStream(selectedFile);
        OutputStream os = new FileOutputStream(destFile);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = is.read(buffer)) > 0) {
            os.write(buffer, 0, length);
        }
        is.close();
        os.close();
        rootLayoutController.showErrorMessage("Файл успешно импортирован");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
