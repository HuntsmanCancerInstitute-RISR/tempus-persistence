package hci.ri.tempus.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name="TpsFusionVariant")
public class FusionVariant {


    private long idFusionVariant;
    private String gene5;
    private String gene5Display;
    private String gene5hgncId;
    private String gene5entrezId;
    private String gene3;
    private String gene3display;
    private String gene3hgncId;
    private String gene3entrezId;
    private String variantDescription;
    private String fusionType;
    private String structuralVariant;
    private Set therapies;

    public FusionVariant(){
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idFusionVariant" , nullable = false, unique = true)
    @JsonIgnore
    public long getIdFusionVariant() { return idFusionVariant; }
    public void setIdFusionVariant(long idFusionVariant) { this.idFusionVariant = idFusionVariant; }


    public String getGene5() { return gene5;}
    public void setGene5(String gene5) { this.gene5 = gene5; }

    public String getGene5Display() { return gene5Display; }
    public void setGene5Display(String gene5display) { this.gene5Display = gene5display; }

    @JsonProperty("gene5HgncId")
    @Column(name="gene5hgncId")
    public String getGene5HgncId() { return gene5hgncId; }
    public void setGene5HgncId(String gene5hgncId) { this.gene5hgncId = gene5hgncId; }

    @JsonProperty("gene5EntrezId")
    @Column(name="gene5entrezId")
    public String getGene5entrezId() { return gene5entrezId; }
    public void setGene5entrezId(String gene5entrezId) { this.gene5entrezId = gene5entrezId; }

    public String getGene3() { return gene3; }
    public void setGene3(String gene3) { this.gene3 = gene3; }

    public String getGene3Display() { return gene3display; }
    public void setGene3Display(String gene3display) { this.gene3display = gene3display; }

    @JsonProperty("gene3HgncId")
    @Column(name="gene3hgncId")
    public String getGene3hgncId() { return gene3hgncId; }
    public void setGene3hgncId(String gene3hgncId) { this.gene3hgncId = gene3hgncId; }

    @JsonProperty("gene3EntrezId")
    @Column(name="gene3entrezId")
    public String getGene3entrezId() { return gene3entrezId; }
    public void setGene3entrezId(String gene3entrezId) { this.gene3entrezId = gene3entrezId; }

    public String getVariantDescription() { return variantDescription; }
    public void setVariantDescription(String variantDescription) { this.variantDescription = variantDescription; }

    public String getFusionType() { return fusionType; }
    public void setFusionType(String fusionType) { this.fusionType = fusionType; }

    public String getStructuralVariant() { return structuralVariant; }
    public void setStructuralVariant(String structuralVariant) { this.structuralVariant = structuralVariant; }

    @JsonIgnore
    @Transient
    public Set getTherapies() { return therapies; }
    public void setTherapies(Set therapies) { this.therapies = therapies; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FusionVariant that = (FusionVariant) o;
        return idFusionVariant == that.idFusionVariant &&
                Objects.equals(gene5, that.gene5) &&
                Objects.equals(gene5Display, that.gene5Display) &&
                Objects.equals(gene5hgncId, that.gene5hgncId) &&
                Objects.equals(gene5entrezId, that.gene5entrezId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idFusionVariant, gene5, gene5Display, gene5hgncId, gene5entrezId);
    }
}
