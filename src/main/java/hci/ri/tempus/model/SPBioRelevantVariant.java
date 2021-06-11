package hci.ri.tempus.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name="TpsSPBioRelevantVariant")
public class SPBioRelevantVariant {


    private long idSPBioRelevantVariant;
    private String gene;
    private String geneDescription;
    private String display;
    private String hgncId;
    private String entrezId;
    private String gene5;
    private String gene5display;
    private String gene5hgncId;
    private String gene5entrezId;
    private String gene3;
    private String gene3display;
    private String gene3hgncId;
    private String gene3entrezId;
    private String variantType;
    private String variantDescription;
    private String fusionType;
    private String structuralVariant;
    private String mutationEffect;
    private String HGVSp;
    private String HGVSpFull;
    private String HGVSc;
    private String transcript;
    private String nucleotideAlteration;
    private String allelicFraction;
    private Integer coverage;
    private Integer copyNumber;

    public SPBioRelevantVariant(){
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idSPBioRelevantVariant" , nullable = false, unique = true)
    @JsonIgnore
    public long getIdSPBioRelevantVariant() { return idSPBioRelevantVariant; }
    public void setIdSPBioRelevantVariant(long idSPBioRelevantVariant) { this.idSPBioRelevantVariant = idSPBioRelevantVariant; }


    public String getGene() { return gene; }
    public void setGene(String gene) { this.gene = gene; }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name="geneDescription")
    public String getGeneDescription() { return geneDescription; }
    public void setGeneDescription(String geneDescription) {  this.geneDescription = geneDescription; }

    public String getDisplay() { return display; }
    public void setDisplay(String display) { this.display = display; }

    public String getHgncId() { return hgncId; }
    public void setHgncId(String hgncId) { this.hgncId = hgncId; }

    public String getEntrezId() { return entrezId; }
    public void setEntrezId(String entrezId) { this.entrezId = entrezId; }

    public String getGene5() { return gene5;}
    public void setGene5(String gene5) { this.gene5 = gene5; }

    @JsonProperty("gene5Display")
    @JsonAlias({"gene5display"})
    @Column(name="gene5display")
    public String getGene5display() { return gene5display; }
    public void setGene5display(String gene5display) { this.gene5display = gene5display; }

    @JsonProperty("gene5hgncId")
    @Column(name="gene5hgncId")
    public String getGene5hgncId() { return gene5hgncId; }
    public void setGene5hgncId(String gene5hgncId) { this.gene5hgncId = gene5hgncId; }

    @JsonProperty("gene5entrezId")
    @Column(name="gene5entrezId")
    public String getGene5entrezId() { return gene5entrezId; }
    public void setGene5entrezId(String gene5entrezId) { this.gene5entrezId = gene5entrezId; }

    public String getGene3() { return gene3; }
    public void setGene3(String gene3) { this.gene3 = gene3; }

    @JsonProperty("gene3Display")
    @JsonAlias({"gene3display"})
    @Column(name="gene3display")
    public String getGene3display() { return gene3display; }
    public void setGene3display(String gene3display) { this.gene3display = gene3display; }

    @JsonProperty("gene3hgncId")
    @Column(name="gene3hgncId")
    public String getGene3hgncId() { return gene3hgncId; }
    public void setGene3hgncId(String gene3hgncId) { this.gene3hgncId = gene3hgncId; }

    @JsonProperty("gene3entrezId")
    @Column(name="gene3entrezId")
    public String getGene3entrezId() { return gene3entrezId; }
    public void setGene3entrezId(String gene3entrezId) { this.gene3entrezId = gene3entrezId; }

    public String getVariantType() { return variantType; }
    public void setVariantType(String variantType) { this.variantType = variantType; }

    public String getVariantDescription() { return variantDescription; }
    public void setVariantDescription(String variantDescription) { this.variantDescription = variantDescription; }

    public String getFusionType() { return fusionType; }
    public void setFusionType(String fusionType) { this.fusionType = fusionType; }

    public String getStructuralVariant() { return structuralVariant; }
    public void setStructuralVariant(String structuralVariant) { this.structuralVariant = structuralVariant; }

    public String getMutationEffect() { return mutationEffect; }
    public void setMutationEffect(String mutationEffect) { this.mutationEffect = mutationEffect; }

    @JsonProperty("HGVS.p")
    public String getHGVSp() { return HGVSp; }
    public void setHGVSp(String HGVSp) { this.HGVSp = HGVSp; }

    @JsonProperty("HGVS.pFull")
    public String getHGVSpFull() { return HGVSpFull; }
    public void setHGVSpFull(String HGVSpFull) { this.HGVSpFull = HGVSpFull; }

    @JsonProperty("HGVS.c")
    public String getHGVSc() { return HGVSc; }
    public void setHGVSc(String HGVSc) { this.HGVSc = HGVSc; }


    public String getTranscript() { return transcript; }
    public void setTranscript(String transcript) { this.transcript = transcript; }

    public String getNucleotideAlteration() { return nucleotideAlteration; }
    public void setNucleotideAlteration(String nucleotideAlteration) { this.nucleotideAlteration = nucleotideAlteration; }

    public String getAllelicFraction() { return allelicFraction; }
    public void setAllelicFraction(String allelicFraction) { this.allelicFraction = allelicFraction; }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Integer getCoverage() { return coverage; }
    public void setCoverage(Integer coverage) { this.coverage = coverage; }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Integer getCopyNumber() { return copyNumber; }
    public void setCopyNumber(Integer copyNumber) { this.copyNumber = copyNumber; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SPBioRelevantVariant that = (SPBioRelevantVariant) o;
        return idSPBioRelevantVariant == that.idSPBioRelevantVariant &&
                gene.equals(that.gene) &&
                display.equals(that.display) &&
                hgncId.equals(that.hgncId) &&
                entrezId.equals(that.entrezId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idSPBioRelevantVariant, gene, display, hgncId, entrezId);
    }
}
