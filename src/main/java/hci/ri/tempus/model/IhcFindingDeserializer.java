package hci.ri.tempus.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.DelegatingDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;

import javax.json.JsonArray;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class IhcFindingDeserializer extends DelegatingDeserializer {
    public IhcFindingDeserializer(JsonDeserializer<?> delegate){
        super(delegate);
    }
    @Override
    protected JsonDeserializer<?> newDelegatingInstance(JsonDeserializer<?> newDelegate) {
        return new IhcFindingDeserializer(newDelegate);
    }

        @Override
        public IhcFinding deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            ObjectCodec oc = jp.getCodec();
            JsonNode rootNode = oc.readTree(jp);
            JsonNode antigenNode = rootNode.get("antigen");
            JsonNode mmrNode = rootNode.get("mmr");
            IhcFinding ihcFinding = new IhcFinding();
            ihcFinding.setIdIhcFinding(0);

            if(antigenNode != null){
                final String antigenName = TempusParser.getJsonStringValue(new String[]{"name"}, antigenNode);
                final String antigenPdl1clone =  TempusParser.getJsonStringValue(new String[]{"pdl1clone"}, antigenNode);
                ihcFinding.setAntigenName(antigenName);
                ihcFinding.setAntigenPdl1clone(antigenPdl1clone);

                JsonNode antiInterpretNode = antigenNode.get("interpretations");
                if(antiInterpretNode != null ){
                    Set<IhcAntigen> antiInterpretations = new HashSet<IhcAntigen>();
                    if(antiInterpretNode.isArray()){
                        for(JsonNode aiNode : antiInterpretNode){
                            IhcAntigen a = new IhcAntigen();
                            final String interpretation = TempusParser.getJsonStringValue(new String[]{"interpretation"}, aiNode);
                            final Integer percentTumorCellStaining =  TempusParser.getJsonIntegerValue(new String[]{"percentTumorCellStaining"}, aiNode);
                            final Integer percentImmuneCellStaining =  TempusParser.getJsonIntegerValue(new String[]{"percentImmuneCellStaining"}, aiNode);
                            final String tumorProportionScore =  TempusParser.getJsonStringValue(new String[]{"tumorProportionScore"}, aiNode);
                            final String combinedPositiveScore =  TempusParser.getJsonStringValue(new String[]{"combinedPositiveScore"}, aiNode);
                            a.setIdIhcAntigen(0);
                            a.setInterpretation(interpretation);
                            a.setPercentTumorCellStaining(percentTumorCellStaining);
                            a.setPercentImmuneCellStaining(percentImmuneCellStaining);
                            a.setTumorProportionScore(tumorProportionScore);
                            a.setCombinedPositiveScore(combinedPositiveScore);
                            antiInterpretations.add(a);
                        }
                    }
                    ihcFinding.setIhcFindingAntigens(antiInterpretations);
                }
            }
            if(mmrNode != null){
                final String mmrInterpretation = TempusParser.getJsonStringValue(new String[]{"interpretation"}, mmrNode);
                ihcFinding.setMmrInterpretation(mmrInterpretation);
                JsonNode mmrDetailNode = mmrNode.get("details");
                if(mmrDetailNode != null) {
                    Set<IhcMmr> mmrDetails = new HashSet<>();
                    if(mmrDetailNode.isArray()){
                        for(JsonNode mdNode : mmrDetailNode){
                            IhcMmr m = new IhcMmr();
                            final String mmrProtein = TempusParser.getJsonStringValue(new String[]{"protein"}, mdNode);
                            final String mmrResult = TempusParser.getJsonStringValue(new String[]{"result"}, mdNode);
                            m.setProtein(mmrProtein);
                            m.setResult(mmrResult);
                            m.setIdIhcMmr(0);
                            mmrDetails.add(m);
                        }
                    }
                    ihcFinding.setIhcFindingMmrs(mmrDetails);
                }
            }
            if(mmrNode == null &&  antigenNode == null ){
                return null;
            }
            return ihcFinding;
        }


}
