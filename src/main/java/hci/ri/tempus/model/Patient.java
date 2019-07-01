package hci.ri.tempus.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.Objects;


@Entity
@Table(name = "TpsPatient")
public class Patient {

    private long idPatient;
    private String firstName;
    private String lastName;
    private String tempusId;
    private String emrId;
    private String sex;
    private String dateOfBirth;
    private String diagnosis;
    private String diagnosisDate;
    private Integer idPerson;
    private String idBSTShadow;
    private String comment;
    private TempusFile tempusFile;

    public Patient(){
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPatient" , nullable = false, unique = true)
    @JsonIgnore
    public long getIdPatient() {
        return idPatient;
    }

    public void setIdPatient(long idPatient) {
        this.idPatient = idPatient;
    }

    @Column(name="firstName")
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name="lastName")
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Column(name="tempusId")
    public String getTempusId() {
        return tempusId;
    }
    public void setTempusId(String tempusId) {
        this.tempusId = tempusId;
    }

    @Column(name="emrId")
    public String getEmr_id() {
        return emrId;
    }
    public void setEmr_id(String emrId) {
        this.emrId = emrId;
    }

    @Column(name="sex")
    public String getSex() {
        return sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }

    @JsonProperty("DoB")
    @Column(name="dateOfBirth")
    public String getDoB() {
        return dateOfBirth;
    }
    public void setDoB(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Column(name="diagnosis")
    public String getDiagnosis() {
        return diagnosis;
    }
    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    @Column(name="diagnosisDate")
    public String getDiagnosisDate() {
        return diagnosisDate;
    }
    public void setDiagnosisDate(String diagnosisDate) {
        this.diagnosisDate = diagnosisDate;
    }

    @JsonIgnore
    public Integer getIdPerson() { return idPerson; }
    public void setIdPerson(Integer idPerson) {
        this.idPerson = idPerson;
    }

    @JsonIgnore
    public String getIdBSTShadow() { return idBSTShadow; }
    public void setIdBSTShadow(String idBSTShadow) {
        this.idBSTShadow = idBSTShadow;
    }

    @JsonIgnore
    public String getComment() { return comment; }
    public void setComment(String comment) {
        this.comment = comment;
    }

    @JsonBackReference("tempusFile_patient")
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idTempusFile", nullable = false)
    public TempusFile getTempusFile() {
        return tempusFile;
    }
    public void setTempusFile(TempusFile tempusFile) {
        this.tempusFile = tempusFile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Patient patient = (Patient) o;
        return getIdPatient() == patient.getIdPatient() &&
                getFirstName().equals(patient.getFirstName()) &&
                getLastName().equals(patient.getLastName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdPatient(), getFirstName(), getLastName(), getTempusId(), getEmr_id(), getSex(),
                getDoB(), getDiagnosis(), getDiagnosisDate());
    }
}