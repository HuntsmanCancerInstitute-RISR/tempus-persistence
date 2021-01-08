package hci.ri.tempus.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "TpsIhcFinding")
public class IhcFinding {
    private long idIhcFinding;
    private String antigenName;
    private String antigenPdl1clone;
    private String mmrInterpretation;
    private Set<IhcAntigen> ihcFindingAntigens;
    private Set<IhcMmr> ihcFindingMmrs;
    private Result result;


    public IhcFinding(){
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idIhcFinding" , nullable = false, unique = true)
    @JsonIgnore
    public long getIdIhcFinding() {
        return idIhcFinding;
    }
    public void setIdIhcFinding(long idTpsIhcFinding) {
        this.idIhcFinding = idTpsIhcFinding;
    }

    public String getAntigenName() {
        return antigenName;
    }
    public void setAntigenName(String antigenName) {
        this.antigenName = antigenName;
    }

    public String getAntigenPdl1clone() {
        return antigenPdl1clone;
    }
    public void setAntigenPdl1clone(String antigenPdl1clone) {
        this.antigenPdl1clone = antigenPdl1clone;
    }

    public String getMmrInterpretation() {
        return mmrInterpretation;
    }
    public void setMmrInterpretation(String mmrInterpretation) {
        this.mmrInterpretation = mmrInterpretation;
    }

    @JsonProperty("antigen")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL,  orphanRemoval = true)
    @JoinColumn(name="idIhcFinding", nullable= false)
    public Set<IhcAntigen> getIhcFindingAntigens() { return ihcFindingAntigens; }
    public void setIhcFindingAntigens(Set<IhcAntigen> ihcFindingAntigens) {
        this.ihcFindingAntigens = ihcFindingAntigens;
    }

    @JsonProperty("mmr")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL,  orphanRemoval = true)
    @JoinColumn(name="idIhcFinding", nullable= false)
    public Set<IhcMmr> getIhcFindingMmrs() { return ihcFindingMmrs; }
    public void setIhcFindingMmrs(Set<IhcMmr> ihcFindingMmrs) {
        this.ihcFindingMmrs = ihcFindingMmrs;
    }

    @JsonBackReference("result_ihcfinding")
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
        IhcFinding that = (IhcFinding) o;
        return idIhcFinding == that.idIhcFinding &&
                Objects.equals(antigenName, that.antigenName) &&
                Objects.equals(antigenPdl1clone, that.antigenPdl1clone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idIhcFinding, antigenName, antigenPdl1clone);
    }



}
