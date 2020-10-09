package hci.ri.tempus.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "TpsLowCoverageAmplicon")
public class LowCoverageAmplicon {

    private long idLowCoverageAmplicon;
    private String amplicon;
    private String display;
    private String hgncId;
    private String entrezId;
    private Integer coverage;

    public LowCoverageAmplicon(){

    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idLowCoverageAmplicon" , nullable = false, unique = true)
    @JsonIgnore
    public long getIdLowCoverageAmplicon() { return idLowCoverageAmplicon; }
    public void setIdLowCoverageAmplicon(long idLowCoverageAmplicon) {
        this.idLowCoverageAmplicon = idLowCoverageAmplicon;
    }

    public String getAmplicon() { return amplicon; }
    public void setAmplicon(String amplicon) { this.amplicon = amplicon; }

    public String getDisplay() { return display; }
    public void setDisplay(String display) { this.display = display; }

    public String getHgncId() { return hgncId; }
    public void setHgncId(String hgncId) { this.hgncId = hgncId; }

    public String getEntrezId() { return entrezId; }
    public void setEntrezId(String entrezId) { this.entrezId = entrezId; }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Integer getCoverage() { return coverage; }
    public void setCoverage(Integer coverage) { this.coverage = coverage; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LowCoverageAmplicon that = (LowCoverageAmplicon) o;
        return idLowCoverageAmplicon == that.idLowCoverageAmplicon &&
                Objects.equals(amplicon, that.amplicon) &&
                Objects.equals(hgncId, that.hgncId) &&
                Objects.equals(entrezId, that.entrezId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idLowCoverageAmplicon, amplicon, hgncId, entrezId);
    }
}
