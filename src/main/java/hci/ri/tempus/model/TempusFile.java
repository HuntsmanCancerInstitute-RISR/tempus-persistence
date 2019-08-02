package hci.ri.tempus.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "TpsTempusFile")
public class TempusFile {

    private long idTempusFile;
    private Order order;
    private Patient patient;
    private Set<Specimen> specimens;
    private Report report;
    private Result results;
    private MetaData metadata;
    private Object lab;
    private Object clinicalTrials;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idTempusFile" , nullable = false, unique = true)
    @JsonIgnore
    public long getIdTempusFile() { return idTempusFile; }
    public void setIdTempusFile(long idTempusFile) { this.idTempusFile = idTempusFile; }

    @Transient
    public MetaData getMetadata() { return metadata; }
    public void setMetadata(MetaData metadata) { this.metadata = metadata; }

    @JsonManagedReference("tempusFile_order")
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "tempusFile")
    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }

    @JsonManagedReference("tempusFile_patient")
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "tempusFile")
    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }

    //no parent reference in child  its all managed here. also in joinColumn have to set nullable to false or else
    // jpa doesn't set the foreign key on the child
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL,  orphanRemoval = true)
    @JoinColumn(name="idTempusFile", nullable= false)
    public Set<Specimen> getSpecimens() { return specimens; }
    public void setSpecimens(Set<Specimen> specimens) { this.specimens = specimens; }

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL,  orphanRemoval = true, mappedBy = "tempusFile")
    public Report getReport() { return report; }
    public void setReport(Report report) {
        this.report = report;
        if(report != null){
            this.report.setTempusFile(this);
        }

    }

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL,  orphanRemoval = true, mappedBy = "tempusFile")
    public Result getResults() { return results; }
    public void setResults(Result results) {
        this.results = results;
        if(this.results != null){
            this.results.setTempusFile(this);
        }
    }
    @JsonIgnore
    @Transient
    public Object getLab() {
        return lab;
    }
    public void setLab(Object lab) {
        this.lab = lab;
    }

    @JsonIgnore
    @Transient
    public Object getClinicalTrials() { return clinicalTrials; }
    public void setClinicalTrials(Object clinicalTrials) { this.clinicalTrials = clinicalTrials; }
}
