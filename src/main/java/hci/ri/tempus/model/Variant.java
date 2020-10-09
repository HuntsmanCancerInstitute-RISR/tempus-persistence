package hci.ri.tempus.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "TpsVariant")
public class Variant {

    private long idVariant;
    private String mutationEffect;
    private String HGVSp;
    private String HGVSpFull;
    private String HGVSc;
    private String transcript;
    private String nucleotideAlteration;
    private String referenceGenome;
    private String allelicFraction;
    private String variantDescription;
    private Integer coverage;

    public Variant(){

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idVariant" , nullable = false, unique = true)
    @JsonIgnore
    public long getIdVariant() { return idVariant; }
    public void setIdVariant(long idVariant) { this.idVariant = idVariant; }

    @Column(name="mutationEffect")
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

    @Column(name="transcript")
    public String getTranscript() { return transcript; }
    public void setTranscript(String transcript) { this.transcript = transcript; }

    @Column(name="nucleotideAlteration")
    public String getNucleotideAlteration() { return nucleotideAlteration; }
    public void setNucleotideAlteration(String nucleotideAlteration) { this.nucleotideAlteration = nucleotideAlteration; }

    @Column(name="referenceGenome")
    public String getReferenceGenome() { return referenceGenome; }
    public void setReferenceGenome(String referenceGenome) { this.referenceGenome = referenceGenome; }

    @Column(name="allelicFraction")
    public String getAllelicFraction() { return allelicFraction; }
    public void setAllelicFraction(String allelicFraction) { this.allelicFraction = allelicFraction; }

    @Column(name="variantDescription")
    public String getVariantDescription() { return variantDescription; }
    public void setVariantDescription(String variantDescription) { this.variantDescription = variantDescription; }


    @Column(name="coverage")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Integer getCoverage() { return coverage; }
    public void setCoverage(Integer coverage) { this.coverage = coverage; }






    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Variant variant = (Variant) o;
        return idVariant == variant.idVariant &&
                Objects.equals(mutationEffect, variant.mutationEffect) &&
                Objects.equals(nucleotideAlteration, variant.nucleotideAlteration) &&
                Objects.equals(referenceGenome, variant.referenceGenome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idVariant, mutationEffect, nucleotideAlteration, referenceGenome);
    }
}
