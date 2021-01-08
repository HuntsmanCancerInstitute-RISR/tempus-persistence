package hci.ri.tempus.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "TpsSPActionableCPVariant")
public class SPActionableCPVariant {

    private long idSPActionableCPVariant;
    private String gene;
    private String geneDescription;
    private String display;
    private String hgncId;
    private String entrezId;
    private String variantDescription;
    private String variantType;
    private Integer copyNumber;

    public SPActionableCPVariant(){

    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idSPActionableCPVariant" , nullable = false, unique = true)
    @JsonIgnore
    public long getIdSPActionableCPVariant() { return idSPActionableCPVariant; }
    public void setIdSPActionableCPVariant(long idSPActionableCPVariant) { this.idSPActionableCPVariant = idSPActionableCPVariant; }

    @Column(name="gene")
    public String getGene() { return gene; }
    public void setGene(String gene) { this.gene = gene; }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name="geneDescription")
    public String getGeneDescription() { return geneDescription; }
    public void setGeneDescription(String geneDescription) {  this.geneDescription = geneDescription; }

    @Column(name="display")
    public String getDisplay() { return display; }
    public void setDisplay(String display) { this.display = display; }

    @Column(name="hgncId")
    public String getHgncId() { return hgncId; }
    public void setHgncId(String hgncId) { this.hgncId = hgncId; }

    @Column(name="entrezId")
    public String getEntrezId() { return entrezId; }
    public void setEntrezId(String entrezId) { this.entrezId = entrezId; }

    @Column(name="variantDescription")
    public String getVariantDescription() { return variantDescription; }
    public void setVariantDescription(String variantDescription) { this.variantDescription = variantDescription; }

    @Column(name="variantType")
    public String getVariantType() {return variantType; }
    public void setVariantType(String variantType) { this.variantType = variantType; }

    @Column(name="copyNumber")
    public Integer getCopyNumber() { return copyNumber; }
    public void setCopyNumber(Integer copyNumber) { this.copyNumber = copyNumber; }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SPActionableCPVariant that = (SPActionableCPVariant) o;
        return idSPActionableCPVariant == that.idSPActionableCPVariant &&
                Objects.equals(gene, that.gene) &&
                Objects.equals(display, that.display) &&
                Objects.equals(hgncId, that.hgncId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idSPActionableCPVariant, gene, display,hgncId);
    }
}
