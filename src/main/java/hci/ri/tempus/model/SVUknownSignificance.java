package hci.ri.tempus.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name="TpsSVUknownSignificance")
public class SVUknownSignificance {
    private long idSVUknownSignificance;
    private String gene;
    private String display;
    private String hgncId;
    private String entrezId;
    private String variantType;
    private String variantDescription;
    private String mutationEffect;
    private String HGVSp;
    private String HGVSpFull;
    private String HGVSc;
    private String transcript;
    private String nucleotideAlteration;
    private String allelicFraction;
    private Integer coverage;

    public SVUknownSignificance(){
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idSVUknownSignificance" , nullable = false, unique = true)
    @JsonIgnore
    public long getIdSVUknownSignificance() { return idSVUknownSignificance; }
    public void setIdSVUknownSignificance(long idSVUknownSignificance) {
        this.idSVUknownSignificance = idSVUknownSignificance;
    }

    public String getGene() { return gene; }
    public void setGene(String gene) { this.gene = gene; }

    public String getDisplay() { return display; }
    public void setDisplay(String display) { this.display = display; }

    public String getHgncId() { return hgncId; }
    public void setHgncId(String hgncId) { this.hgncId = hgncId; }

    public String getEntrezId() { return entrezId; }
    public void setEntrezId(String entrezId) { this.entrezId = entrezId; }

    public String getVariantType() { return variantType; }
    public void setVariantType(String variantType) { this.variantType = variantType; }

    public String getVariantDescription() { return variantDescription; }
    public void setVariantDescription(String variantDescription) { this.variantDescription = variantDescription; }

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

    public Integer getCoverage() { return coverage; }
    public void setCoverage(Integer coverage) { this.coverage = coverage; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SVUknownSignificance that = (SVUknownSignificance) o;
        return idSVUknownSignificance == that.idSVUknownSignificance &&
                Objects.equals(gene, that.gene) &&
                Objects.equals(display, that.display) &&
                Objects.equals(hgncId, that.hgncId) &&
                Objects.equals(entrezId, that.entrezId) &&
                Objects.equals(nucleotideAlteration, that.nucleotideAlteration) &&
                Objects.equals(allelicFraction, that.allelicFraction) &&
                Objects.equals(coverage, that.coverage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idSVUknownSignificance, gene, display, hgncId, entrezId);
    }
}
