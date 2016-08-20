package io.samsara.client;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 *
 */
public class EventSerializer implements JsonSerializer<Event> {
    @Override
    public JsonElement serialize(Event src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject json = new JsonObject();
        json.addProperty("eventName", src.getEventName());
        json.addProperty("sourceId", src.getSourceId());
        json.addProperty("timestamp", src.getTimestamp());
        for (String key: src.getProperties().keySet()) {
            json.addProperty(key, src.getProperties().get(key));
        }
        return json;
    }
}
