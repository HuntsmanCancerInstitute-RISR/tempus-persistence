package hci.ri.tempus.model;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import java.util.Objects;
@Entity
@Table(name = "TpsHrdFinding")
public class HrdFinding {
    private long idHrdFinding;
    private String hrdResult;
    private Double genomeWideLoh;
    private Integer cohortSpecificGenomeWideLohThreshold;
    private Result result;
    private String percentGenomeWideLoh;
    private String percentCohortSpecificGenomeWideLohThreshold;
    private String brcaDoubleHit;
    private String hrdAnalysisType;
    private String rnaExpressionHrdScore;
    private String rnaExpressionThreshold;



    public HrdFinding() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idHrdFinding" , nullable = false, unique = true)
    @JsonIgnore
    public long getIdHrdFinding() {
        return idHrdFinding;
    }
    public void setIdHrdFinding(long idHrdFinding) {
        this.idHrdFinding = idHrdFinding;
    }

    public String getHrdResult() {
        return hrdResult;
    }
    public void setHrdResult(String hrdResult) {
        this.hrdResult = hrdResult;
    }


    public Double getGenomeWideLoh() {
        return genomeWideLoh;
    }
    public void setGenomeWideLoh(Double genomeWideLoh) {
        this.genomeWideLoh = genomeWideLoh;
    }

    public Integer getCohortSpecificGenomeWideLohThreshold() {
        return cohortSpecificGenomeWideLohThreshold;
    }
    public void setCohortSpecificGenomeWideLohThreshold(Integer cohortSpecificGenomeWideLohThreshold) {
        this.cohortSpecificGenomeWideLohThreshold = cohortSpecificGenomeWideLohThreshold;
    }

    public String getPercentGenomeWideLoh() { return percentGenomeWideLoh;}
    public void setPercentGenomeWideLoh(String percentGenomeWideLoh) {
        this.percentGenomeWideLoh = percentGenomeWideLoh;
    }

    public String getPercentCohortSpecificGenomeWideLohThreshold() {
        return percentCohortSpecificGenomeWideLohThreshold;
    }
    public void setPercentCohortSpecificGenomeWideLohThreshold(String percentCohortSpecificGenomeWideLohThreshold) {
        this.percentCohortSpecificGenomeWideLohThreshold = percentCohortSpecificGenomeWideLohThreshold;
    }

    public String getBrcaDoubleHit() {return brcaDoubleHit;}
    public void setBrcaDoubleHit(String brcaDoubleHit) {
        this.brcaDoubleHit = brcaDoubleHit;
    }

    public String getHrdAnalysisType() {return hrdAnalysisType;}
    public void setHrdAnalysisType(String hrdAnalysisType) {
        this.hrdAnalysisType = hrdAnalysisType;
    }

    public String getRnaExpressionHrdScore() {return rnaExpressionHrdScore;}
    public void setRnaExpressionHrdScore(String rnaExpressionHrdScore) {
        this.rnaExpressionHrdScore = rnaExpressionHrdScore;
    }
    // this would need @JsonProperty annotation if we didn't have a deserializer since the property is called 'threshold'
    public String getRnaExpressionThreshold() {return rnaExpressionThreshold;}
    public void setRnaExpressionThreshold(String rnaExpressionThreshold) {
        this.rnaExpressionThreshold = rnaExpressionThreshold;
    }

    @JsonBackReference("result_hrdfinding")
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idResult", nullable = false)
    public Result getResult() {return result; }
    public void setResult(Result result) {
        this.result = result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HrdFinding that = (HrdFinding) o;
        return idHrdFinding == that.idHrdFinding &&
                Objects.equals(hrdResult, that.hrdResult) &&
                Objects.equals(genomeWideLoh, that.genomeWideLoh);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idHrdFinding, hrdResult, genomeWideLoh);
    }


}
