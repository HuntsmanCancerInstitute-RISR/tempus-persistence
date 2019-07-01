package hci.ri.tempus.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "TpsReport")
public class Report {

    private long idReport;
    private String reportId;
    private String reportStatus;
    private String signing_pathologist;
    private String signout_date;
    private String bioInfPipeline;
    private String notes;
    private TempusFile tf;


    public Report(){
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idReport" , nullable = false, unique = true)
    @JsonIgnore
    public long getIdReport() { return idReport; }
    public void setIdReport(long idReport) { this.idReport = idReport; }

    @Column(name="reportId")
    public String getReportId() { return reportId; }
    public void setReportId(String reportId) { this.reportId = reportId; }

    @Column(name="reportStatus")
    public String getReportStatus() { return reportStatus; }
    public void setReportStatus(String reportStatus) { this.reportStatus = reportStatus; }

    @JsonIgnore
    @Transient
    public String getSigning_pathologist() { return signing_pathologist; }
    public void setSigning_pathologist(String signing_pathologist) { this.signing_pathologist = signing_pathologist; }

    @Column(name="signout_date")
    public String getSignout_date() { return signout_date; }
    public void setSignout_date(String signout_date) { this.signout_date = signout_date; }

    @Column(name="bioInfPipeline")
    public String getBioInfPipeline() { return bioInfPipeline; }
    public void setBioInfPipeline(String bioInfPipeline) { this.bioInfPipeline = bioInfPipeline; }

    @Column(name="notes")
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }


    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idTempusFile", nullable = false)
    public TempusFile getTempusFile() { return tf; }
    public void setTempusFile(TempusFile tf) { this.tf = tf; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Report report = (Report) o;
        return idReport == report.idReport &&
                Objects.equals(reportId, report.reportId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idReport, reportId, reportStatus);
    }
}
