package foto_verif.view.dmp;

/**
 * Created by market6 on 30.11.2016.
 */
public class DmpDescr {
    private boolean correctPhoto;
    private boolean hasKeyWord;
    private boolean prodMZ;
    private boolean prodK;
    private boolean prodLP;
    private boolean prodS;
    private boolean dmpPechka;
    private boolean minSize;
    private boolean tmaProd;
    private boolean hasPrice;
    private boolean filledOver80;
    private boolean placeDMP;
    private String comment;

    public DmpDescr(boolean correctPhoto, boolean hasKeyWord, boolean prodMZ, boolean prodK, boolean prodLP, boolean prodS, boolean dmpPechka,
                    boolean minSize, boolean tmaProd, boolean hasPrice, boolean filledOver80, boolean placeDMP, String comment) {
        this.correctPhoto = correctPhoto;
        this.hasKeyWord = hasKeyWord;
        this.prodMZ = prodMZ;
        this.prodK = prodK;
        this.prodLP = prodLP;
        this.prodS = prodS;
        this.dmpPechka = dmpPechka;
        this.minSize = minSize;
        this.tmaProd = tmaProd;
        this.hasPrice = hasPrice;
        this.filledOver80 = filledOver80;
        this.placeDMP = placeDMP;
        this.comment = comment;
    }

    public DmpDescr() {
    }

    public boolean isCorrectPhoto() {
        return correctPhoto;
    }

    public boolean isHasKeyWord() {
        return hasKeyWord;
    }

    public boolean isProdMZ() {
        return prodMZ;
    }

    public boolean isProdK() {
        return prodK;
    }

    public boolean isProdLP() {
        return prodLP;
    }

    public boolean isProdS() {
        return prodS;
    }

    public boolean isDmpPechka() {
        return dmpPechka;
    }

    public boolean isMinSize() {
        return minSize;
    }

    public boolean isTmaProd() {
        return tmaProd;
    }

    public boolean isHasPrice() {
        return hasPrice;
    }

    public boolean isFilledOver80() {
        return filledOver80;
    }

    public boolean isPlaceDMP() {
        return placeDMP;
    }

    public String getComment() {
        return comment;
    }

    public void setCorrectPhoto(boolean correctPhoto) {
        this.correctPhoto = correctPhoto;
    }

    public void setHasKeyWord(boolean hasKeyWord) {
        this.hasKeyWord = hasKeyWord;
    }

    public void setProdMZ(boolean prodMZ) {
        this.prodMZ = prodMZ;
    }

    public void setProdK(boolean prodK) {
        this.prodK = prodK;
    }

    public void setProdLP(boolean prodLP) {
        this.prodLP = prodLP;
    }

    public void setProdS(boolean prodS) {
        this.prodS = prodS;
    }

    public void setDmpPechka(boolean dmpPechka) {
        this.dmpPechka = dmpPechka;
    }

    public void setMinSize(boolean minSize) {
        this.minSize = minSize;
    }

    public void setTmaProd(boolean tmaProd) {
        this.tmaProd = tmaProd;
    }

    public void setHasPrice(boolean hasPrice) {
        this.hasPrice = hasPrice;
    }

    public void setFilledOver80(boolean filledOver80) {
        this.filledOver80 = filledOver80;
    }

    public void setPlaceDMP(boolean placeDMP) {
        this.placeDMP = placeDMP;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void clearDmpDescr () {
        this.correctPhoto = false;
        this.hasKeyWord = false;
        this.prodMZ = false;
        this.prodK = false;
        this.prodLP = false;
        this.prodS = false;
        this.dmpPechka = false;
        this.minSize = false;
        this.tmaProd = false;
        this.hasPrice = false;
        this.filledOver80 = false;
        this.placeDMP = false;
        this.comment = "";
    }
}
