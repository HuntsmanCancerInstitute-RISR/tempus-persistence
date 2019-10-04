package hci.ri.tempus.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class ReportDeserializer extends JsonDeserializer<Report> {

        @Override
        public Report deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            ObjectCodec oc = jp.getCodec();
            JsonNode node = oc.readTree(jp);
            final Long idReport = new Long(0);
            final String reportId = TempusParser.getJsonStringValue(new String[]{"reportId"}, node);
            final String signout_date = TempusParser.getJsonStringValue(new String[]{"signoutDate"}, node);
            final String bioInfPipeline = TempusParser.getJsonStringValue(new String[]{"bioInfPipeline"}, node);
            final String notes = TempusParser.getJsonStringValue(new String[]{"notes"}, node);
            final String reportStatus = TempusParser.getJsonStringValue(new String[]{"workflow","reportStatus"}, node);

            Report report = new Report();
            report.setIdReport(idReport);
            report.setReportId(reportId);
            report.setSignout_date(signout_date);
            report.setBioInfPipeline(bioInfPipeline);
            report.setNotes(notes);
            report.setReportStatus(reportStatus);
            return report;
        }


}
