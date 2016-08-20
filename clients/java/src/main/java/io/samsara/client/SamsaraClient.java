package io.samsara.client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.zip.GZIPOutputStream;

import com.google.gson.GsonBuilder;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class SamsaraClient {

    private static final Logger logger = LoggerFactory.getLogger(SamsaraClient.class);

    private RingBuffer buffer = new DefaultRingBufferImpl();
    private Thread thread;
    private SamsaraClientConfig config;

    public SamsaraClient(String url, boolean startPublishingThread) {
        this(SamsaraClientConfig.builder().url(url).build(), startPublishingThread);
    }

    public SamsaraClient(SamsaraClientConfig config, boolean startPublishingThread) {
        this.config = config;
        this.thread = new Thread(this::publishWithInterval);
        this.buffer = new DefaultRingBufferImpl(config.getMaxBufferSize());
        if (startPublishingThread) {
            thread.start();
        }
    }

    public boolean publishEvent(String url, Collection<Event> events) throws IOException {
        HttpEntity entity;
        try {
            entity = createEntity(events, config.isGzipCompression());
        } catch (IOException e) {
            throw e;
        }
        try {
            return Request.Post(url + "/v1/events")
                           .addHeader("Content-Type", "application/json")
                           .addHeader("Content-Encoding", config.isGzipCompression() ? "gzip" : "identity")
                           .addHeader("X-Samsara-publishedTimestamp", Long.toString(System.currentTimeMillis()))
                           .addHeader("Accept", "application/json")
                           .body(entity)
                           .connectTimeout(config.getConnectTimeout())
                           .socketTimeout(config.getSocketTimeout())
                           .execute().handleResponse((final HttpResponse response) -> {
                                StatusLine status = response.getStatusLine();
                                if (status.getStatusCode() != HttpStatus.SC_ACCEPTED) {
                                    throw new HttpResponseException(status.getStatusCode(), status.getReasonPhrase());
                                }
                                return true;
                            });
        } catch (IOException e) {
            logger.error("Error connecting to Samsara server");
            throw e;
        }
    }

    public void recordEvent(Event event) {
        buffer.add(event);
    }

    public void flushBuffer() throws IOException {
        Collection<Event> events = buffer.getSnapshot();
        if (publishEvent(config.getUrl(), events)) {
            buffer.remove(events);
        }
    }

    private void publishWithInterval() {
        logger.info("Starting publishing thread");
        while (true) {
            try {
                Thread.sleep(config.getPublishInterval());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
            if (buffer.size() >= config.getMinBufferSize()) {
                try {
                    flushBuffer();
                } catch (IOException e) {
                    logger.error("Shutting down publishing thread");
                    logger.error(e.getMessage(), e);
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }

    private HttpEntity createEntity(Collection<Event> events, boolean gzipCompress) throws IOException {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Event.class, new EventSerializer());
        String json = builder.create().toJson(events);
        System.out.println(json);
        if (gzipCompress) {
            ByteArrayOutputStream inputStream = new ByteArrayOutputStream(json.length());
            try (GZIPOutputStream gzipStream = new GZIPOutputStream(inputStream)) {
                gzipStream.write(json.getBytes("utf-8"));
            } catch (IOException e) {
                logger.error("Error during gzip compression");
                throw e;
            }
            return new ByteArrayEntity(inputStream.toByteArray());
        }
        return new StringEntity(json);
    }

}
