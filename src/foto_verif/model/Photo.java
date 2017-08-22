package foto_verif.model;

import foto_verif.util.LocalDateAdapter;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;

/**
 * Created by market6 on 29.09.2016.
 */
public class Photo implements Comparable<Photo> {

    private LocalDate date;
    private LocalDate dateAdded;
    private String url;
    private String comment;
    private boolean isChecked;

    public Photo(LocalDate date, LocalDate dateAdded, String url, String comment, boolean isChecked) {
        this.date = date;
        this.dateAdded = dateAdded;
        this.url = url;
        this.comment = comment;
        this.isChecked = isChecked;
    }

    public Photo() {
    }

    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    public LocalDate getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(LocalDate dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Photo)) return false;

        Photo photo = (Photo) o;

        if (!url.equals(photo.url)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return url.hashCode();
    }

    @Override
    public int compareTo(Photo o) {
        return date.compareTo(o.getDate());
    }
}
