package hci.ri.tempus.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class VariantDeserializer extends JsonDeserializer<Variant> {
    @Override
    public Variant deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        ObjectCodec oc = jp.getCodec();
        JsonNode node = oc.readTree(jp);
        final Long idVariant = new Long(0);
        final String mutationEffect = TempusParser.getJsonStringValue(new String[]{"mutationEffect"},node);
        final String HGVSp = TempusParser.getJsonStringValue(new String[]{"HGVS.p"},node);
        final String HGVSpFull = TempusParser.getJsonStringValue(new String[]{"HGVS.pFull"},node);
        final String HGVSc = TempusParser.getJsonStringValue(new String[]{"HGVS.c"},node);
        final String transcript = TempusParser.getJsonStringValue(new String[]{"transcript"},node);
        final String nucleotideAlteration = TempusParser.getJsonStringValue(new String[]{"nucleotideAlteration"},node);
        final String referenceGenome = TempusParser.getJsonStringValue(new String[]{"referenceGenome"},node);
        final String allelicFraction = TempusParser.getJsonStringValue(new String[]{"allelicFraction"},node);
        final String variantDescription = TempusParser.getJsonStringValue(new String[]{"variantDescription"},node);
        final Integer coverage = TempusParser.getJsonIntegerValue(new String[]{"coverage"},node);

        Variant variant = new Variant();
        variant.setIdVariant(idVariant);
        variant.setMutationEffect(mutationEffect);
        variant.setHGVSp(HGVSp);
        variant.setHGVSpFull(HGVSpFull);
        variant.setHGVSc(HGVSc);
        variant.setTranscript(transcript);
        variant.setNucleotideAlteration(nucleotideAlteration);
        variant.setReferenceGenome(referenceGenome);
        variant.setAllelicFraction(allelicFraction);
        variant.setVariantDescription(variantDescription);
        variant.setCoverage(coverage);
        return variant;
    }
}
