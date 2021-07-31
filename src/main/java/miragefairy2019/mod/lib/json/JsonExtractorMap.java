package miragefairy2019.mod.lib.json;

import java.util.Map;
import java.util.function.Consumer;

public class JsonExtractorMap {

    private Map<?, ?> value;

    public JsonExtractorMap(Map<?, ?> value) {
        this.value = value;
    }

    public JsonExtractorMap get(String key, Consumer<JsonExtractor> consumer) {
        consumer.accept(new JsonExtractor(value.get(key)));
        return this;
    }

}
