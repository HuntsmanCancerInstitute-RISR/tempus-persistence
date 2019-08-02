package hci.ri.tempus.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class MetaData {
    private String copyright;
    private String schemaVersion;
    private Object manualEdits;

    public MetaData(){}

    @JsonIgnore
    public String getCopyright() { return copyright; }
    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }


    public String getSchemaVersion() { return schemaVersion; }
    public void setSchemaVersion(String schemaVersion) {
        this.schemaVersion = schemaVersion;
    }

    @JsonIgnore
    public Object getManualEdits() { return manualEdits; }
    public void setManualEdits(Object manualEdits) { this.manualEdits = manualEdits;
    }
}
