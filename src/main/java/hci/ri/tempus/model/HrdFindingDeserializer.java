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
        final String brcaDoubleHit = TempusParser.getJsonStringValue(new String[]{"BRCADoubleHit"}, node );
        final String hrdAnalysisType = TempusParser.getJsonStringValue(new String[]{"hrdAnalysisType"}, node );

        hrdFinding.setHrdResult(hrdResult);
        hrdFinding.setBrcaDoubleHit(brcaDoubleHit);
        hrdFinding.setHrdAnalysisType(hrdAnalysisType);

        JsonNode lohNode = node.get("loh") != null ? node.get("loh").get("details") : null;
        JsonNode rnaExpressionNode = node.get("RNAExpression") != null ? node.get("RNAExpression").get("details") : null;
        if(lohNode != null){
            //legacy properties
            final Double genomeWideLoh = TempusParser.getJsonDoubleValue(new String[]{"genomeWideLoh"}, lohNode);
            final Integer cohortSpecificGenomeWideLohThreshold =
                    TempusParser.getJsonIntegerValue(new String[]{"cohortSpecificGenomeWideLohThreshold"}, lohNode);
            //1.4.2 json has different data type then old fields(above)
            final String percentGenomeWideLoh = TempusParser.getJsonStringValue(new String[]{"percentageGenomeWideLoh"}, lohNode);
            final String percentCohortSpecificGenomeWideLohThreshold =
                    TempusParser.getJsonStringValue(new String[]{"percentageCohortSpecificGenomeWideLohThreshold"}, lohNode);

            hrdFinding.setPercentGenomeWideLoh(percentGenomeWideLoh);
            hrdFinding.setPercentCohortSpecificGenomeWideLohThreshold(percentCohortSpecificGenomeWideLohThreshold);
            hrdFinding.setGenomeWideLoh(genomeWideLoh);
            hrdFinding.setCohortSpecificGenomeWideLohThreshold(cohortSpecificGenomeWideLohThreshold);
        }
        //1.4.2 adds these properties
        if(rnaExpressionNode != null){
            final String hrdScore = TempusParser.getJsonStringValue(new String[]{"hrdScore"}, rnaExpressionNode);
            final String threshold = TempusParser.getJsonStringValue(new String[]{"threshold"}, rnaExpressionNode);
            hrdFinding.setRnaExpressionHrdScore(hrdScore);
            hrdFinding.setRnaExpressionThreshold(threshold);

        }
        if(lohNode == null && rnaExpressionNode == null ){
            return null;
        }


        return hrdFinding;
    }



}
