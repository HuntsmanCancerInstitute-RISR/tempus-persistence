package hci.ri.tempus.model;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "TpsResult")
public class Result {


    private long idResult;
    private float tumorMutationalBurden;
    private int tumorMutationBurdenPercentile;
    private String msiStatus;
    private Set<SPActionableMutation> somaticPotentiallyActionableMutations;
    private Set<SPActionableCPVariant> somaticPotentiallyActionableCopyNumberVariants;
    private Set<SPBioRelevantVariant> somaticBiologicallyRelevantVariants;
    private Set<SVUknownSignificance> somaticVariantsOfUnknownSignificance;
    private InheritedRelevantVariant inheritedRelevantVariants;
    private InheritedUnknownSignificanceVariant inheritedVariantsOfUnknownSignificance;
    private InheritedIncidentalFinding inheritedIncidentalFindings;
    private Set<LowCoverageAmplicon> lowCoverageAmplicons;
    private Set<FusionVariant> fusionVariants;
    private IhcFinding ihcFindings;
    private Set<RnaFinding> rnaFindings;
    private HrdFinding hrdFindings;
    private Set<PertinentNegativeGene> pertinentNegativeGenes;
    private MicrosatelliteInstability msi;

    private TempusFile tempusFile;

    public Result(){
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idResult" , nullable = false, unique = true)
    @JsonIgnore
    public long getIdResult() { return idResult; }
    public void setIdResult(long idResult) { this.idResult = idResult; }

    @Column(name="tumorMutationalBurden")
    public float getTumorMutationalBurden() { return tumorMutationalBurden; }
    public void setTumorMutationalBurden(float tumorMutationalBurden) { this.tumorMutationalBurden = tumorMutationalBurden; }

    @Column(name="tumorMutationBurdenPercentile")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public int getTumorMutationBurdenPercentile() { return tumorMutationBurdenPercentile; }
    public void setTumorMutationBurdenPercentile(int tumorMutationalPercentile) {
        this.tumorMutationBurdenPercentile = tumorMutationalPercentile;
    }
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name="msiStatus")
    public String getMsiStatus() { return msiStatus; }
    public void setMsiStatus(String msiStatus) { this.msiStatus = msiStatus; }

    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL,  orphanRemoval = true)
    @JoinColumn(name="idResult", nullable= false)
    public Set<SPActionableMutation> getSomaticPotentiallyActionableMutations() { return somaticPotentiallyActionableMutations; }
    public void setSomaticPotentiallyActionableMutations(Set<SPActionableMutation> somaticPotentiallyActionableMutations) { this.somaticPotentiallyActionableMutations = somaticPotentiallyActionableMutations; }

    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL,  orphanRemoval = true)
    @JoinColumn(name="idResult", nullable= false)
    public Set<SPActionableCPVariant> getSomaticPotentiallyActionableCopyNumberVariants() { return somaticPotentiallyActionableCopyNumberVariants; }
    public void setSomaticPotentiallyActionableCopyNumberVariants(Set<SPActionableCPVariant> somaticPotentiallyActionableCopyNumberVariants) {
        this.somaticPotentiallyActionableCopyNumberVariants = somaticPotentiallyActionableCopyNumberVariants;
    }


    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL,  orphanRemoval = true)
    @JoinColumn(name="idResult", nullable= false)
    public Set<SPBioRelevantVariant> getSomaticBiologicallyRelevantVariants() { return somaticBiologicallyRelevantVariants; }
    public void setSomaticBiologicallyRelevantVariants(Set<SPBioRelevantVariant> somaticBiologicallyRelevantVariants) {
        this.somaticBiologicallyRelevantVariants = somaticBiologicallyRelevantVariants;
    }


    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL,  orphanRemoval = true)
    @JoinColumn(name="idResult", nullable= false)
    public Set<SVUknownSignificance> getSomaticVariantsOfUnknownSignificance() { return somaticVariantsOfUnknownSignificance; }
    public void setSomaticVariantsOfUnknownSignificance(Set<SVUknownSignificance> somaticVariantsOfUnknownSignificance) {
        this.somaticVariantsOfUnknownSignificance = somaticVariantsOfUnknownSignificance;
    }

    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonManagedReference("result_inheritedVariant")
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL,  orphanRemoval = true, mappedBy = "result")
    public InheritedRelevantVariant getInheritedRelevantVariants() { return inheritedRelevantVariants; }
    public void setInheritedRelevantVariants(InheritedRelevantVariant inheritedRelevantVariants) {
        this.inheritedRelevantVariants = inheritedRelevantVariants;
    }

    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonManagedReference("result_inheritedVariant")
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL,  orphanRemoval = true, mappedBy = "result")
    public InheritedUnknownSignificanceVariant getInheritedVariantsOfUnknownSignificance() {
        return inheritedVariantsOfUnknownSignificance;
    }
    public void setInheritedVariantsOfUnknownSignificance(InheritedUnknownSignificanceVariant inheritedVariantsOfUnknownSignificance) {
        this.inheritedVariantsOfUnknownSignificance = inheritedVariantsOfUnknownSignificance;
    }

    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonManagedReference("result_inheritedVariant")
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL,  orphanRemoval = true, mappedBy = "result")
    public InheritedIncidentalFinding getInheritedIncidentalFindings() {
        return inheritedIncidentalFindings;
    }
    public void setInheritedIncidentalFindings(InheritedIncidentalFinding inheritedIncidentalFindings) {
        this.inheritedIncidentalFindings = inheritedIncidentalFindings;
    }

    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL,  orphanRemoval = true)
    @JoinColumn(name="idResult", nullable= false)
    public Set<LowCoverageAmplicon> getLowCoverageAmplicons() { return lowCoverageAmplicons; }
    public void setLowCoverageAmplicons(Set<LowCoverageAmplicon> lowCoverageAmplicons) {
        this.lowCoverageAmplicons = lowCoverageAmplicons;
    }
    // this is not best practice for performance more queries needed under the hood, although it alleviates  need for the mapped reference
    // on the many to one side
    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL,  orphanRemoval = true)
    @JoinColumn(name="idResult", nullable= false)
    public Set<FusionVariant> getFusionVariants() { return fusionVariants; }
    public void setFusionVariants(Set<FusionVariant> fusionVariants) {
        this.fusionVariants = fusionVariants;
    }


    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonManagedReference("result_ihcfinding")
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL,  orphanRemoval = true, mappedBy = "result")
    public IhcFinding getIhcFindings() { return ihcFindings; }
    public void setIhcFindings(IhcFinding ihcFindings) { this.ihcFindings = ihcFindings; }

    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL,  orphanRemoval = true)
    @JoinColumn(name="idResult", nullable= false)
    public Set<RnaFinding> getRnaFindings() { return rnaFindings; }
    public void setRnaFindings(Set<RnaFinding> rnaFindings) { this.rnaFindings = rnaFindings; }

    // this approach doesn't use a mapped reference, it figures out relationship  from the joinColumn
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonManagedReference("result_hrdfinding")
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL,  orphanRemoval = true, mappedBy = "result")
    public HrdFinding getHrdFindings() { return hrdFindings; }
    public void setHrdFindings(HrdFinding hrdFindings) { this.hrdFindings = hrdFindings; }

    @JsonProperty("microsatelliteInstability")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonManagedReference("result_msi")
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL,  orphanRemoval = true, mappedBy = "result")
    public MicrosatelliteInstability getMsi() { return msi; }
    public void setMsi(MicrosatelliteInstability msi) { this.msi = msi; }

    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL,  orphanRemoval = true)
    @JoinColumn(name="idResult", nullable= false)
    public Set<PertinentNegativeGene> getPertinentNegativeGenes() { return pertinentNegativeGenes; }
    public void setPertinentNegativeGenes(Set<PertinentNegativeGene> pertinentNegativeGenes) {
        this.pertinentNegativeGenes = pertinentNegativeGenes;
    }

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idTempusFile", nullable = false)
    public TempusFile getTempusFile() { return tempusFile; }
    public void setTempusFile(TempusFile tempusFile) { this.tempusFile = tempusFile; }


}
