package hci.ri.tempus.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class MetaData {
    private String schemaVersion;
    private String traceId;
    private String institutionId;
    private String id;
    private String schemaName;

    public MetaData(){}

    public String getSchemaVersion() { return schemaVersion; }
    public void setSchemaVersion(String schemaVersion) {
        this.schemaVersion = schemaVersion;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getInstitutionId() {
        return institutionId;
    }

    public void setInstitutionId(String institutionId) {
        this.institutionId = institutionId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }



}
