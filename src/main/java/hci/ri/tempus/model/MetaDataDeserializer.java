package hci.ri.tempus.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class MetaDataDeserializer  extends JsonDeserializer<MetaData> {
    @Override
    public MetaData deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        ObjectCodec oc = jp.getCodec();
        JsonNode node = oc.readTree(jp);
        final String schemaVersion = TempusParser.getJsonStringValue(new String[]{"schemaVersion"},node);
        MetaData metaData = new MetaData();
        metaData.setSchemaVersion(schemaVersion);
        return metaData;

    }
}
