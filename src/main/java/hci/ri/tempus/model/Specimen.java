package hci.ri.tempus.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.util.Objects;
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "TpsSpecimen")
public class Specimen {

    private long idSpecimen;
    private String tempusSampleId;
    private String collectionDate;
    private String receiptDate;
    private String sampleCategory;
    private String sampleSite;
    private String sampleType;
    private String notes;
    private Integer tumorPercentage;

//private TempusFile tempusFile;

    public Specimen(){
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idSpecimen" , nullable = false, unique = true)
    @JsonIgnore
    public long getIdSpecimen() { return idSpecimen; }
    public void setIdSpecimen(long idSpecimen) { this.idSpecimen = idSpecimen; }

    @JsonIgnore
    @Transient
    @Column(name="tempusSampleId")
    public String getTempusSampleId() { return tempusSampleId; }
    public void setTempusSampleId(String tempusSampleId) { this.tempusSampleId = tempusSampleId; }

    @Column(name="collectionDate")
    public String getCollectionDate() { return collectionDate; }
    public void setCollectionDate(String collectionDate) { this.collectionDate = collectionDate; }

    @JsonIgnore
    @Transient
    public String getReceiptDate(){return this.receiptDate; };
    public void setReceiptDate(String receiptDate){this.receiptDate = receiptDate;};

    @Column(name="sampleCategory")
    public String getSampleCategory() { return sampleCategory; }
    public void setSampleCategory(String sampleCategory) { this.sampleCategory = sampleCategory; }

    @Column(name="sampleSite")
    public String getSampleSite() { return sampleSite; }
    public void setSampleSite(String sampleSite) { this.sampleSite = sampleSite; }

    @Column(name="sampleType")
    public String getSampleType() { return sampleType; }
    public void setSampleType(String sampleType) { this.sampleType = sampleType; }

    @Column(name="notes")
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    @Column(name="tumorPercentage")
    public Integer getTumorPercentage() { return tumorPercentage; }
    public void setTumorPercentage(Integer tumorPercentage) { this.tumorPercentage = tumorPercentage; }


//    @JsonIgnore
//    @ManyToOne(optional = false, fetch = FetchType.LAZY)
//    @JoinColumn(name = "idTempusFile", insertable = true, updatable = true, nullable = false)
//    public TempusFile getTempusFile(){ return this.tempusFile; }
//    public void setTempusFile(TempusFile tempusFile){ this.tempusFile = tempusFile; }




}
