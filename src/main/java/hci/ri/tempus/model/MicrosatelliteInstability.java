package hci.ri.tempus.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;
@Entity
@Table(name = "TpsMicrosatelliteInstability")
public class MicrosatelliteInstability {
    private long idMSI;
    private String status;
    private Set therapies;
    private Result result;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idMSI" , nullable = false, unique = true)
    @JsonIgnore
    public long getIdMSI() { return idMSI; }
    public void setIdMSI(long idMSI) { this.idMSI = idMSI; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @JsonIgnore
    @Transient
    public Set getTherapies() { return therapies; }
    public void setTherapies(Set therapies) { this.therapies = therapies; }

    @JsonBackReference("result_msi")
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idResult", nullable = false)
    public Result getResult() { return result; }
    public void setResult(Result result) { this.result = result; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MicrosatelliteInstability that = (MicrosatelliteInstability) o;
        return idMSI == that.idMSI &&
                status.equals(that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idMSI, status);
    }

}
