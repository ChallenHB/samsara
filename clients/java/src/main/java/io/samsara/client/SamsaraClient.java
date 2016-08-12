package io.samsara.client;

import java.io.IOException;
import java.util.Collection;

import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;

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

    public void publishEvent(String url, Collection<Event> events) {
        try {
             HttpResponse reponse = Request.Post(url + "/v1/events")
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Content-Encoding", "identity") // This needs to be gzip if we're doing compression
                    .addHeader("X-Samsara-publishedTimestamp", Long.toString(System.currentTimeMillis()))
                    .addHeader("Accept", "application/json")
                    .body(null)
                    .connectTimeout(connectTimeout)
                    .socketTimeout(socketTimeout)
                    .execute().returnResponse();
        } catch (IOException e) {
            // Add some logging here
        }
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
        return new Thread(new Runnable() { // Don't want to use lambda here to make it compatible with pre-java 8
            @Override
            public void run() {
                while(true) {
                    try {
                        Thread.sleep(publishInterval);
                    } catch(InterruptedException e) {
                        // Might need to add some clean up code, depending on how we use the http component
                        Thread.currentThread().interrupt();
                        break;
                    }
                    flushBuffer();
                }
            }
        });
    }
}
