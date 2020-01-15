package hci.ri.tempus.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jdk.nashorn.internal.ir.annotations.Immutable;

import javax.persistence.*;

@Entity
@Immutable
@Table(name = "VIEW_TempusLinkedPatient")
public class TempusLinkedPatient {
    private Long idTempusFile;
    private String emrId;
    private String linkMRN;
    private int HCIPersonID;
    private String firstName;
    private String lastName;
    private String sex;
    private String dateOfBirth;
    private String diagnosis;
    private String diagnosisDate;
    private String linkNote;
    private String accessionId;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idTempusFile", updatable = false, nullable = false, unique = true)
    @JsonIgnore
    public Long getIdTempusFile() {
        return idTempusFile;
    }
    public void setIdTempusFile(Long idTempusFile) {
        this.idTempusFile = idTempusFile;
    }

    @JsonIgnore
    public String getEmrId() {
        return emrId;
    }
    public void setEmrId(String emrId) {
        this.emrId = emrId;
    }

    @JsonIgnore
    public String getLinkMRN() {
        return linkMRN;
    }
    public void setLinkMRN(String linkMRN) {
        this.linkMRN = linkMRN;
    }

    @JsonIgnore
    public int getHCIPersonID() {
        return HCIPersonID;
    }
    public void setHCIPersonID(int HCIPersonID) {
        this.HCIPersonID = HCIPersonID;
    }

    @JsonIgnore
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @JsonIgnore
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @JsonIgnore
    public String getSex() {
        return sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }

    @JsonIgnore
    public String getDateOfBirth() {
        return dateOfBirth;
    }
    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @JsonIgnore
    public String getDiagnosis() {
        return diagnosis;
    }
    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    @JsonIgnore
    public String getDiagnosisDate() {
        return diagnosisDate;
    }
    public void setDiagnosisDate(String diagnosisDate) {
        this.diagnosisDate = diagnosisDate;
    }

    @JsonIgnore
    public String getLinkNote() {
        return linkNote;
    }
    public void setLinkNote(String linkNote) {
        this.linkNote = linkNote;
    }

    @JsonIgnore
    public String getAccessionId() {
        return accessionId;
    }
    public void setAccessionId(String accessionId) {
        this.accessionId = accessionId;
    }



}
