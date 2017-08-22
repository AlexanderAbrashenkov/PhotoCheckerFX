package foto_verif.view.dmp;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

/**
 * Created by market6 on 09.11.2016.
 */
public class CriteriasLayoutController {
    @FXML
    private CheckBox allOK;
    @FXML
    private CheckBox correctPhoto;
    @FXML
    private CheckBox hasKeyWord;
    @FXML
    private Label productLabel;
    @FXML
    private CheckBox prodMZ;
    @FXML
    private CheckBox prodK;
    @FXML
    private CheckBox prodLP;
    @FXML
    private CheckBox prodS;
    @FXML
    private CheckBox dmpPechka;
    @FXML
    private Label criteriaLabel;
    @FXML
    private CheckBox minSize;
    @FXML
    private CheckBox tmaProd;
    @FXML
    private CheckBox hasPrice;
    @FXML
    private CheckBox filledOver80;
    @FXML
    private CheckBox placeDMP;
    @FXML
    private TextArea comment;

    @FXML
    private void initialize() {
        // если все ОК
        allOK.selectedProperty().addListener((observable, oldValue, newValue) -> {
            checkAll(newValue);
        });

        // Отслеживание выбора наличия корректных фото
        correctPhoto.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue && hasKeyWord.isSelected()) {
                setCriteriasEnable(true);
            } else {
                setCriteriasEnable(false);
            }
        });

        hasKeyWord.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue && correctPhoto.isSelected()) {
                setCriteriasEnable(true);
            } else {
                setCriteriasEnable(false);
            }
        });
    }

    /*
    Очистка заполнения полей
     */
    @FXML
    public void clearCheckBoxesCurrDMP() {
        allOK.setSelected(false);
        correctPhoto.setSelected(false);
        hasKeyWord.setSelected(false);
        prodMZ.setSelected(false);
        prodK.setSelected(false);
        prodLP.setSelected(false);
        prodS.setSelected(false);
        dmpPechka.setSelected(false);
        minSize.setSelected(false);
        tmaProd.setSelected(false);
        hasPrice.setSelected(false);
        filledOver80.setSelected(false);
        placeDMP.setSelected(false);
        comment.setText("");
    }

    public void setCheckBoxesFromFileCurrDMP(String datas, String comm) {
        correctPhoto.setSelected(datas.substring(0,1).equals("1"));
        hasKeyWord.setSelected(datas.substring(1,2).equals("1"));
        prodMZ.setSelected(datas.substring(2,3).equals("1"));
        prodK.setSelected(datas.substring(3,4).equals("1"));
        prodLP.setSelected(datas.substring(4,5).equals("1"));
        prodS.setSelected(datas.substring(5,6).equals("1"));

        minSize.setSelected(datas.substring(6,7).equals("1"));
        tmaProd.setSelected(datas.substring(7,8).equals("1"));
        hasPrice.setSelected(datas.substring(8,9).equals("1"));
        filledOver80.setSelected(datas.substring(9,10).equals("1"));
        placeDMP.setSelected(datas.substring(10,11).equals("1"));

        if (comm == null)
            comm = "";
        comment.setText(comm);
    }

    public void setCheckBoxesFromDmpDescr(DmpDescr dmpDescr) {
        correctPhoto.setSelected(dmpDescr.isCorrectPhoto());
        hasKeyWord.setSelected(dmpDescr.isHasKeyWord());
        prodMZ.setSelected(dmpDescr.isProdMZ());
        prodK.setSelected(dmpDescr.isProdK());
        prodLP.setSelected(dmpDescr.isProdLP());
        prodS.setSelected(dmpDescr.isProdS());
        dmpPechka.setSelected(dmpDescr.isDmpPechka());

        minSize.setSelected(dmpDescr.isMinSize());
        tmaProd.setSelected(dmpDescr.isTmaProd());
        hasPrice.setSelected(dmpDescr.isHasPrice());
        filledOver80.setSelected(dmpDescr.isFilledOver80());
        placeDMP.setSelected(dmpDescr.isPlaceDMP());
        comment.setText(dmpDescr.getComment());
    }

    private void setCriteriasEnable (boolean isEnable) {
        productLabel.setDisable(!isEnable);
        prodMZ.setDisable(!isEnable);
        prodK.setDisable(!isEnable);
        prodLP.setDisable(!isEnable);
        prodS.setDisable(!isEnable);
        dmpPechka.setDisable(!isEnable);
        criteriaLabel.setDisable(!isEnable);
        minSize.setDisable(!isEnable);
        tmaProd.setDisable(!isEnable);
        hasPrice.setDisable(!isEnable);
        filledOver80.setDisable(!isEnable);
        placeDMP.setDisable(!isEnable);

        if (!isEnable) {
            prodMZ.setSelected(false);
            prodK.setSelected(false);
            prodLP.setSelected(false);
            prodS.setSelected(false);
            dmpPechka.setSelected(false);
            minSize.setSelected(false);
            tmaProd.setSelected(false);
            hasPrice.setSelected(false);
            filledOver80.setSelected(false);
            placeDMP.setSelected(false);
            comment.setText("");
        }
    }

    private void checkAll(boolean isChecked) {
        if (isChecked) {
            correctPhoto.setSelected(true);
            hasKeyWord.setSelected(true);
            minSize.setSelected(true);
            tmaProd.setSelected(true);
            hasPrice.setSelected(true);
            filledOver80.setSelected(true);
            placeDMP.setSelected(true);
        } else {
            correctPhoto.setSelected(false);
            hasKeyWord.setSelected(false);
            minSize.setSelected(false);
            tmaProd.setSelected(false);
            hasPrice.setSelected(false);
            filledOver80.setSelected(false);
            placeDMP.setSelected(false);
        }
    }

    public String[] getDataDMP() {
        String datas = Integer.toString(correctPhoto.isSelected()?1:0) + (hasKeyWord.isSelected()?1:0) + (prodMZ.isSelected()?1:0) +
                    (prodK.isSelected()?1:0) + (prodLP.isSelected()?1:0) + (prodS.isSelected()?1:0) +
                    (minSize.isSelected()?1:0) + (tmaProd.isSelected()?1:0) + (hasPrice.isSelected()?1:0) +
                    (filledOver80.isSelected()?1:0) + (placeDMP.isSelected()?1:0);
        String comm = comment.getText().replace("\n", " ");
        return new String[] {datas, comm};
    }

    public DmpDescr getDmpDescr() {
        DmpDescr dmpDescr = new DmpDescr(correctPhoto.isSelected(), hasKeyWord.isSelected(), prodMZ.isSelected(),
                prodK.isSelected(), prodLP.isSelected(), prodS.isSelected(), dmpPechka.isSelected(),
                minSize.isSelected(), tmaProd.isSelected(), hasPrice.isSelected(), filledOver80.isSelected(), placeDMP.isSelected(),
                comment.getText().replace("\n", " "));
        return dmpDescr;
    }

    public boolean isPhotoGood () {
        return correctPhoto.isSelected() && hasKeyWord.isSelected();
    }

    public boolean isTgSelected () {
        return prodMZ.isSelected() || prodK.isSelected() || prodLP.isSelected() || prodS.isSelected();
    }
}