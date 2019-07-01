package hci.ri.tempus.model;

import java.util.Objects;

public class TempusSample {

    private String mrn;
    private String personId;
    private String fullName;
    private String gender;
    private String shadowId;
    private String sampleName;
    private String submittedDiagnosis;


    public void setSubmittedDiagnosis(String submittedDiagnosis) {
        this.submittedDiagnosis = submittedDiagnosis;
    }

    public String getMrn() {
        return mrn;
    }

    public void setMrn(String mrn) {
        this.mrn = mrn;
    }
    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getShadowId() {
        return shadowId;
    }

    public void setShadowId(String shadowId) {
        this.shadowId = shadowId;
    }

    public String getSampleName() {
        return sampleName;
    }

    public void setSampleName(String sampleName) {
        this.sampleName = sampleName;
    }

    public String getSubmittedDiagnosis() {
        return submittedDiagnosis;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TempusSample that = (TempusSample) o;
        return getMrn().equals(that.getMrn()) &&
                getPersonId().equals(that.getPersonId()) &&
                getShadowId().equals(that.getShadowId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMrn(), getPersonId(), getShadowId());
    }
}
