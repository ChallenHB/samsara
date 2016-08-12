package io.samsara.client;

import java.io.IOException;
import java.util.Collection;

import com.google.gson.GsonBuilder;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.StringEntity;

/**
 *
 */
public class SamsaraClient {

    private RingBuffer buffer = new DefaultRingBufferImpl();
    private String url;
    private Thread thread;
    private String sourceId;
    private int maxBufferSize = 10;
    private int minBufferSize = 5;
    private int publishInterval = 30000;
    private int connectTimeout = 30000;
    private int socketTimeout = 30000;

    public SamsaraClient(boolean startPublishingThread, String url) {
        this.url = url;
        thread = createPublishingThread();
        if (startPublishingThread) {
            thread.start();
        }
    }

    public HttpResponse publishEvent(String url, Collection<Event> events) {
        try {
             return Request.Post(url + "/v1/events")
                     .addHeader("Content-Type", "application/json")
                     .addHeader("Content-Encoding", "identity") // This needs to be gzip if we're doing compression
                     .addHeader("X-Samsara-publishedTimestamp", Long.toString(System.currentTimeMillis()))
                     .addHeader("Accept", "application/json")
                     .body(new StringEntity(serializeEvents(events)))
                     .connectTimeout(connectTimeout)
                     .socketTimeout(socketTimeout)
                     .execute().returnResponse();
        } catch (IOException e) {
            // Add some logging here
        }
        return null;
    }

    public void recordEvent(Event event) {
        if (event.getSourceId() == null) {
            Event.cloneWithSourceId(event, sourceId);
        }
        if (event.getTimestamp() == null) {
            Event.cloneWithTimestamp(event, System.currentTimeMillis());
        }
        buffer.add(event);
    }

    private void flushBuffer() {

    }

    private Thread createPublishingThread() {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        Thread.sleep(publishInterval);
                    } catch(InterruptedException e) {
                        // Might need to add some clean up code, but for now we don't need it.
                        Thread.currentThread().interrupt();
                        break;
                    }
                    flushBuffer();
                }
            }
        });
    }

    private String serializeEvents(Collection<Event> events) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Event.class, new EventSerializer());
        return builder.create().toJson(events);
    }
}
