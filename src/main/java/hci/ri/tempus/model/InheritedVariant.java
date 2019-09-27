package hci.ri.tempus.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "TpsInheritedVariant")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="variantCategory", discriminatorType = DiscriminatorType.STRING)
public class InheritedVariant {

    private long idInheritedVariant;
    private String gene;
    private String display;
    private String hgncId;
    private String entrezId;
    private String mutationEffect;
    private String HGVSp;
    private String HGVSpFull;
    private String HGVSc;
    private String transcript;
    private String variantDescription;
    private String clinicalSignificance;
    private String disease;
    private String allelicFraction;
    private Integer coverage;
    private String chromosome;
    private String ref;
    private String alt;
    private String pos;
    private Result result;

    public InheritedVariant(){
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idInheritedVariant" , nullable = false, unique = true)
    @JsonIgnore
    public long getIdInheritedVariant() { return idInheritedVariant; }
    public void setIdInheritedVariant(long idInheritedVariant) {
        this.idInheritedVariant = idInheritedVariant;
    }

    public String getGene() { return gene; }
    public void setGene(String gene) { this.gene = gene; }

    public String getDisplay() { return display; }
    public void setDisplay(String display) { this.display = display; }

    public String getHgncId() { return hgncId; }
    public void setHgncId(String hgncId) { this.hgncId = hgncId; }

    public String getEntrezId() { return entrezId; }
    public void setEntrezId(String entrezId) { this.entrezId = entrezId; }

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

    public String getVariantDescription() { return variantDescription; }
    public void setVariantDescription(String variantDescription) { this.variantDescription = variantDescription; }

    public String getClinicalSignificance() { return clinicalSignificance; }
    public void setClinicalSignificance(String clinicalSignificance) { this.clinicalSignificance = clinicalSignificance; }

    public String getDisease() { return disease; }
    public void setDisease(String disease) { this.disease = disease; }

    public String getAllelicFraction() { return allelicFraction; }
    public void setAllelicFraction(String allelicFraction) { this.allelicFraction = allelicFraction; }

    public Integer getCoverage() { return coverage; }
    public void setCoverage(Integer coverage) { this.coverage = coverage; }

    public String getChromosome() { return chromosome; }
    public void setChromosome(String chromosome) { this.chromosome = chromosome; }

    public String getRef() { return ref; }
    public void setRef(String ref) { this.ref = ref; }

    public String getAlt() { return alt; }
    public void setAlt(String alt) { this.alt = alt; }

    public String getPos() { return pos; }
    public void setPos(String pos) { this.pos = pos; }

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idResult", nullable = false)
    public Result getResult() {return result; }
    public void setResult(Result result) {
        this.result = result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InheritedVariant that = (InheritedVariant) o;
        return idInheritedVariant == that.idInheritedVariant &&
                Objects.equals(gene, that.gene) &&
                Objects.equals(display, that.display) &&
                Objects.equals(hgncId, that.hgncId) &&
                Objects.equals(entrezId, that.entrezId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idInheritedVariant, gene, display, hgncId, entrezId);
    }
}
