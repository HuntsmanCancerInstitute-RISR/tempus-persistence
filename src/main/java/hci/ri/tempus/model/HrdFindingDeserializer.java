package hci.ri.tempus.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.DelegatingDeserializer;

import java.io.IOException;

public class HrdFindingDeserializer extends DelegatingDeserializer {
    public HrdFindingDeserializer(JsonDeserializer<?> delegate){
        super(delegate);
    }
    @Override
    protected JsonDeserializer<?> newDelegatingInstance(JsonDeserializer<?> newDelegate) {
        return new HrdFindingDeserializer(newDelegate);
    }
    @Override
    public HrdFinding deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {
        ObjectCodec oc = jp.getCodec();
        JsonNode node = oc.readTree(jp);
        HrdFinding hrdFinding = new HrdFinding();
        final String hrdResult = TempusParser.getJsonStringValue(new String[]{"hrdResult"}, node);
        hrdFinding.setHrdResult(hrdResult);

        JsonNode lohNode = node.get("loh") != null ? node.get("loh").get("details") : null;
        if(lohNode != null){
            final Double genomeWideLoh = TempusParser.getJsonDoubleValue(new String[]{"genomeWideLoh"}, lohNode);
            final Integer cohortSpecificGenomeWideLohThreshold =
                    TempusParser.getJsonIntegerValue(new String[]{"cohortSpecificGenomeWideLohThreshold"}, lohNode);
            hrdFinding.setGenomeWideLoh(genomeWideLoh);
            hrdFinding.setCohortSpecificGenomeWideLohThreshold(cohortSpecificGenomeWideLohThreshold);
        }
        if(lohNode == null){
            return null;
        }

        return hrdFinding;
    }



}
