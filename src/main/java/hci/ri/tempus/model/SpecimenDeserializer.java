package hci.ri.tempus.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class SpecimenDeserializer extends JsonDeserializer<Specimen> {
    @Override
    public Specimen deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        ObjectCodec oc = jp.getCodec();
        JsonNode node = oc.readTree(jp);
        final Long idSpecimen = new Long(0);
        final String tempusSampleId = TempusParser.getJsonStringValue(new String[]{"tempusSampleId"}, node);
        final String collectionDate =  TempusParser.getJsonStringValue(new String[]{"collectionDate"}, node);
        final String sampleCategory = TempusParser.getJsonStringValue(new String[]{"sampleCategory"}, node);
        final String sampleSite = TempusParser.getJsonStringValue(new String[]{"sampleSite"}, node);
        final String sampleType = TempusParser.getJsonStringValue(new String[]{"sampleType"}, node);
        final String notes = TempusParser.getJsonStringValue(new String[]{"notes"}, node);
        final Integer tumorPercentage = TempusParser.getJsonIntegerValue(new String[]{"institutionData","tumorPercentage"}, node);


        Specimen speci = new Specimen();
        speci.setIdSpecimen(idSpecimen);
        speci.setTempusSampleId(tempusSampleId);
        speci.setCollectionDate(collectionDate);
        speci.setSampleCategory(sampleCategory);
        speci.setSampleSite(sampleSite);
        speci.setSampleType(sampleType);
        speci.setNotes(notes);
        speci.setTumorPercentage(tumorPercentage);

        return speci;
    }

}
