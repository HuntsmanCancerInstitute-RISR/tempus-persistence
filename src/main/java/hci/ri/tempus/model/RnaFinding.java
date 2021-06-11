package hci.ri.tempus.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "TpsRnaFinding")
public class RnaFinding {

    private long idRnaFinding;
    private String gene;
    private String hgncId;
    private String entrezId;
    private String mechanism;
    private Object therapies;

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idRnaFinding" , nullable = false, unique = true)
    public long getIdRnaFinding() { return idRnaFinding; }
    public void setIdRnaFinding(long idRnaFinding) {
        this.idRnaFinding = idRnaFinding;
    }

    public String getGene() { return gene; }
    public void setGene(String gene) {
        this.gene = gene;
    }

    public String getHgncId() { return hgncId; }
    public void setHgncId(String hgncId) {
        this.hgncId = hgncId;
    }

    public String getEntrezId() { return entrezId; }
    public void setEntrezId(String entrezId) {
        this.entrezId = entrezId;
    }

    public String getMechanism() { return mechanism; }
    public void setMechanism(String mechanism) {
        this.mechanism = mechanism;
    }
    @JsonIgnore
    @Transient
    public Object getTherapies() { return therapies; }
    public void setTherapies(Object therapies) {
        this.therapies = therapies;
    }
}
