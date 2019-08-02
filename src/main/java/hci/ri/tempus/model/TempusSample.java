package hci.ri.tempus.model;

import java.util.Objects;

public class TempusSample{

    private String mrn;
    private String personId;
    private String fullName;
    private String gender;
    private String shadowId;
    private String sampleName;
    private String sampleSubType;
    private String testType;
    private String tissueType;
    private String submittedDiagnosis;
    TempusSample(){
        this.mrn = "null";
        this.personId = "null";
        this.fullName = "null";
        this.gender = "null";
        this.shadowId = "null";
        this.sampleName = "null";
        this.sampleSubType = "null";
        this.testType = "null";
        this.tissueType = "null";
        this.submittedDiagnosis = "null";
    }
    TempusSample(String sampleName){
        this.mrn = "null";
        this.personId = "null";
        this.fullName = "null";
        this.gender = "null";
        this.shadowId = "null";
        this.sampleName = sampleName;
        this.sampleSubType = "null";
        this.testType = "null";
        this.tissueType = "null";
        this.submittedDiagnosis = "null";
    }


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

    public String getSampleSubType() {
        return sampleSubType;
    }

    public void setSampleSubType(String sampleSubType) {
        this.sampleSubType = sampleSubType;
    }

    public String getTestType() {
        return testType;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }

    public String getTissueType() {
        return tissueType;
    }

    public void setTissueType(String tissueType) {
        this.tissueType = tissueType;
    }

    public String getSubmittedDiagnosis() {
        return submittedDiagnosis;
    }

    @Override
    public String toString(){
        StringBuilder strBuild = new StringBuilder();
        strBuild.append(mrn);
        strBuild.append("\t");
        strBuild.append(personId);
        strBuild.append("\t");
        strBuild.append(fullName);
        strBuild.append("\t");
        strBuild.append(gender);
        strBuild.append("\t");
        strBuild.append(shadowId);
        strBuild.append("\t");
        strBuild.append(sampleName);
        strBuild.append("\t");
        strBuild.append(sampleSubType);
        strBuild.append("\t");
        strBuild.append(testType);
        strBuild.append("\t");
        strBuild.append(tissueType);
        strBuild.append("\t");
        strBuild.append( submittedDiagnosis);
        return strBuild.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TempusSample that = (TempusSample) o;
        return getMrn().equals(that.getMrn()) &&
                getFullName().equals(that.fullName) &&
                getPersonId().equals(that.getPersonId()) &&
                getShadowId().equals(that.getShadowId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMrn(), getPersonId(), getShadowId());
    }
}
