package io.samsara.client;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class Event {

    private String sourceId;
    private String eventName;
    private Long timestamp;
    private Map<String, String> additionalFields = new HashMap<>();

    private Event() {
    }

    public Event(String sourceId, String eventName, Long timestamp, Map<String, String> additionalFields) {
        this.sourceId = sourceId;
        this.eventName = eventName;
        this.timestamp = timestamp;
        this.additionalFields = additionalFields;
    }

    public static EventBuilder builder() {
        return new EventBuilder();
    }

    public String getSourceId() {
        return sourceId;
    }

    public String getEventName() {
        return eventName;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public Map<String, String> getAdditionalFields() {
        return additionalFields;
    }

    public static Event cloneWithSourceId(Event event, String sourceId) {
        return new Event(sourceId, event.getEventName(), event.getTimestamp(), event.getAdditionalFields());
    }

    public static Event cloneWithTimestamp(Event event, long timestamp) {
        return new Event(event.getSourceId(), event.getEventName(), timestamp, event.getAdditionalFields());
    }

    public static class EventBuilder {
        private Event instance = new Event();

        private EventBuilder() {
        }

        public EventBuilder sourceId(String sourceId) {
            if (sourceId == null) {
                throw new IllegalArgumentException("Source Id cannot be null");
            }
            instance.sourceId = sourceId;
            return this;
        }

        public EventBuilder eventName(String eventName) {
            if (eventName == null) {
                throw new IllegalArgumentException("Event Name cannot be null");
            }
            instance.eventName = eventName;
            return this;
        }

        public EventBuilder time(long timestamp) {
            instance.timestamp = timestamp;
            return this;
        }

        public EventBuilder newField(String name, String value) {
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Field name cannot be null or empty");
            }
            if (value == null) {
                throw new IllegalArgumentException("Field value cannot be null");
            }
            instance.additionalFields.put(name, value);
            return this;
        }

        public Event build() {
            if (instance.eventName == null) {
                throw new IllegalArgumentException("Event Name cannot be null"); // TODO: this might not be the correct exception, placeholder for now.
            }
            else if (instance.sourceId == null) {
                throw new IllegalArgumentException("Event Name cannot be null"); // TODO: this might not be the correct exception, placeholder for now.
            }
            return instance;
        }

    }
}
