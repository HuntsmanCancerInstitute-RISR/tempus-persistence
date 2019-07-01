package hci.ri.tempus.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class CopyNumberVariantDeserializer  extends JsonDeserializer<SPActionableCPVariant> {
    @Override
    public SPActionableCPVariant deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        ObjectCodec oc = jp.getCodec();
        JsonNode node = oc.readTree(jp);
        final Long idVariant = new Long(0);
        final String gene = TempusParser.getJsonStringValue(new String[]{"gene"}, node);
        final String display = TempusParser.getJsonStringValue(new String[]{"display"}, node);
        final String hgncId = TempusParser.getJsonStringValue(new String[]{"hgncId"}, node);
        final String entrezId = TempusParser.getJsonStringValue(new String[]{"entrezId"}, node);
        final String variantDescription = TempusParser.getJsonStringValue(new String[]{"variantDescription"}, node);
        final String variantType = TempusParser.getJsonStringValue(new String[]{"variantType"}, node);
        final Integer copyNumber = TempusParser.getJsonIntegerValue(new String[]{"copyNumber"}, node);

        SPActionableCPVariant cpVariant = new SPActionableCPVariant();
        cpVariant.setIdSPActionableCPVariant(idVariant);
        cpVariant.setGene(gene);
        cpVariant.setDisplay(display);
        cpVariant.setHgncId(hgncId);
        cpVariant.setEntrezId(entrezId);
        cpVariant.setVariantDescription(variantDescription);
        cpVariant.setVariantType(variantType);
        cpVariant.setCopyNumber(copyNumber);
        return cpVariant;
    }
}
