package foto_verif.view.lka;

/**
 * Created by market6 on 09.03.2017.
 */
public class LkaCriteria {
    private int id;
    private String criteria1Text;
    private double criteria1Mz;
    private double criteria1K;
    private double criteria1S;
    private String criteria2Text;

    public LkaCriteria(int id, String criteria1Text, double criteria1Mz, double criteria1K, double criteria1S, String criteria2Text) {
        this.id = id;
        this.criteria1Text = criteria1Text;
        this.criteria1Mz = criteria1Mz;
        this.criteria1K = criteria1K;
        this.criteria1S = criteria1S;
        this.criteria2Text = criteria2Text;
    }

    public int getId() {
        return id;
    }

    public String getCriteria1Text() {
        return criteria1Text;
    }

    public double getCriteria1Mz() {
        return criteria1Mz;
    }

    public double getCriteria1K() {
        return criteria1K;
    }

    public double getCriteria1S() {
        return criteria1S;
    }

    public String getCriteria2Text() {
        return criteria2Text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LkaCriteria that = (LkaCriteria) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "LkaCriteria{" +
                "id=" + id +
                ", criteria1Text='" + criteria1Text + '\'' +
                ", criteria1Mz='" + criteria1Mz + '\'' +
                ", criteria1K='" + criteria1K + '\'' +
                ", criteria1S='" + criteria1S + '\'' +
                ", criteria2Text='" + criteria2Text + '\'' +
                '}';
    }
}
