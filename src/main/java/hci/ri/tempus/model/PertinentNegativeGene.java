package hci.ri.tempus.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "TpsPertinentNegativeGene")
public class PertinentNegativeGene {
    private long idPertinentNegativeGene;
    private String gene;
    private String symbol;
    private String hgncId;
    private String entrezId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPertinentNegativeGene" , nullable = false, unique = true)
    @JsonIgnore
    public long getIdPertinentNegativeGene() { return idPertinentNegativeGene; }
    public void setIdPertinentNegativeGene(long idPertinentNegativeGene) { this.idPertinentNegativeGene = idPertinentNegativeGene; }

    public String getGene() { return gene; }
    public void setGene(String gene) {
        this.gene = gene;
    }

    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }

    public String getHgncId() { return hgncId; }
    public void setHgncId(String hgncId) { this.hgncId = hgncId; }

    public String getEntrezId() { return entrezId; }
    public void setEntrezId(String entrezId) {
        this.entrezId = entrezId;
    }


}
