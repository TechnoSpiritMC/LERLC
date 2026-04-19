package org.leaf.api.http.dto.v2;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EmptyArrayAsMapDeserializer extends StdDeserializer<Map<Long, String>> {

    public EmptyArrayAsMapDeserializer() {
        super(Map.class);
    }

    @Override
    public Map<Long, String> deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
        if (p.currentToken() == JsonToken.START_ARRAY) {
            while (p.nextToken() != JsonToken.END_ARRAY) {}
            return new HashMap<>();
        }
        Map<Long, String> map = new HashMap<>();
        while (p.nextToken() != JsonToken.END_OBJECT) {
            String key = p.currentName();
            p.nextToken();
            map.put(Long.parseLong(key), p.getText());
        }
        return map;
    }
}
