package hci.ri.tempus.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "TpsIhcAntigen")
public class IhcAntigen {

    private long idIhcAntigen;
    private String interpretation;
    private Integer percentTumorCellStaining;
    private Integer percentImmuneCellStaining;
    private String tumorProportionScore;
    private String combinedPositiveScore;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idIhcAntigen" , nullable = false, unique = true)
    @JsonIgnore
    public long getIdIhcAntigen() { return idIhcAntigen; }
    public void setIdIhcAntigen(long idIhcAntigen) {
        this.idIhcAntigen = idIhcAntigen;
    }

    public String getInterpretation() { return interpretation; }
    public void setInterpretation(String interpretation) {
        this.interpretation = interpretation;
    }

    public Integer getPercentTumorCellStaining() { return percentTumorCellStaining; }
    public void setPercentTumorCellStaining(Integer percentTumorCellStaining) {
        this.percentTumorCellStaining = percentTumorCellStaining;
    }

    public Integer getPercentImmuneCellStaining() { return percentImmuneCellStaining; }
    public void setPercentImmuneCellStaining(Integer percentImmuneCellStaining) {
        this.percentImmuneCellStaining = percentImmuneCellStaining;
    }

    public String getTumorProportionScore() { return tumorProportionScore; }
    public void setTumorProportionScore(String tumorProportionScore) {
        this.tumorProportionScore = tumorProportionScore;
    }

    public String getCombinedPositiveScore() { return combinedPositiveScore; }
    public void setCombinedPositiveScore(String combinedPositiveScore) {
        this.combinedPositiveScore = combinedPositiveScore;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IhcAntigen that = (IhcAntigen) o;
        return idIhcAntigen == that.idIhcAntigen &&
                interpretation.equals(that.interpretation) &&
                percentTumorCellStaining.equals(that.percentTumorCellStaining) &&
                percentImmuneCellStaining.equals(that.percentImmuneCellStaining);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idIhcAntigen, interpretation, percentTumorCellStaining, percentImmuneCellStaining);
    }
}
