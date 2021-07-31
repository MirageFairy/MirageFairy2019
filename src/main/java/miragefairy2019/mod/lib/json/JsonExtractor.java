package miragefairy2019.mod.lib.json;

import java.util.HashMap;
import java.util.Map;

public class JsonExtractor {

    private Object value;

    public JsonExtractor(Object value) {
        this.value = value;
    }

    public JsonExtractorMap asMap() {
        if (value instanceof Map) {
            return new JsonExtractorMap((Map<?, ?>) value);
        } else {
            return new JsonExtractorMap(new HashMap<>());
        }
    }

    public int toInt(int def) {
        if (value instanceof Number) {
            return ((Number) value).intValue();
        } else {
            return def;
        }
    }

}
