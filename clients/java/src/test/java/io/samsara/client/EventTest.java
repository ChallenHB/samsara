package io.samsara.client;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 *
 */
public class EventTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void testEventBuildWithProperties() {
        Event event = Event.builder()
                              .eventName("event-name-test")
                              .sourceId("test-source-id")
                              .timestamp(12345)
                              .prop("test-property", "test-value")
                              .build();
        assertEquals("event-name-test", event.getEventName());
        assertEquals("test-source-id", event.getSourceId());
        assertEquals(new Long(12345), event.getTimestamp());
        assertEquals("test-value", event.getProperty("test-property"));
    }

    @Test
    public void testPopulateTimestamp() {
        Event event = Event.builder()
                              .eventName("event-name-test")
                              .sourceId("test-sourceId")
                              .build();
        assertNotNull(event.getTimestamp());
    }

    @Test
    public void testThrowExceptionNullSourceIdArgument() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Source Id cannot be null");
        Event.builder()
                .sourceId(null);
    }

    @Test
    public void testThrowExceptionNullEventNameArgument() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Event Name cannot be null");
        Event.builder()
                .eventName(null);
    }

    @Test
    public void testThrowExceptionNullPropertyName() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Property name cannot be null or empty");
        Event.builder()
                .prop(null, "test-property-value");

    }

    @Test
    public void testThrowExceptionEmptyPropertyName() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Property name cannot be null or empty");
        Event.builder()
                .prop("", "test-property-value");
    }

    @Test
    public void testThrowExceptionNullPropertyValue() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Property value cannot be null");
        Event.builder()
                .prop("test-property-name", null);
    }

    @Test
    public void testThrowExceptionOnBuildNullSourceId() {
        exception.expect(NullPointerException.class);
        exception.expectMessage("Source Id cannot be null");
        Event.builder()
                .eventName("test-event-name")
                .build();
    }

    @Test
    public void testThrowExceptionOnBuildNullEventName() {
        exception.expect(NullPointerException.class);
        exception.expectMessage("Event Name cannot be null");
        Event.builder()
                .sourceId("test-source-id")
                .build();
    }
}
