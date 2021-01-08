package hci.ri.tempus.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "TpsIhcMmr")
public class IhcMmr {
    private long idIhcMmr;
    private String mmrProtein;
    private String mmrResult;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdIhcMmr" , nullable = false, unique = true)
    @JsonIgnore
    public long getIdIhcMmr() { return idIhcMmr; }
    public void setIdIhcMmr(long idIhcMmr) {
        this.idIhcMmr = idIhcMmr;
    }
    public String getProtein() { return mmrProtein; }
    public void setProtein(String mmrProtein) {
        this.mmrProtein = mmrProtein;
    }

    public String getResult() {
        return mmrResult;
    }
    public void setResult(String mmrResult) {
        this.mmrResult = mmrResult;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IhcMmr ihcMmr = (IhcMmr) o;
        return idIhcMmr == ihcMmr.idIhcMmr &&
                mmrProtein.equals(ihcMmr.mmrProtein) &&
                mmrResult.equals(ihcMmr.mmrResult);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idIhcMmr, mmrProtein, mmrResult);
    }
}
