package io.samsara.client;

import jdk.nashorn.internal.runtime.regexp.joni.Config;

/**
 *
 */
public class SamsaraClientConfig {

    private static final int DEFAULT_MAX_BUFFER_SIZE = 10000;
    private static final int DEFAULT_MIN_BUFFER_SIZE = 1000;
    private static final int DEFAULT_PUBLISH_INTERVAL = 30000;
    private static final int DEFAULT_CONNECT_TIMEOUT = 30000;
    private static final int DEFAULT_SOCKET_TIMEOUT = 30000;
    private static final boolean DEFAULT_GZIP_COMPRESSION = true;

    private String url;
    private Integer maxBufferSize;
    private Integer minBufferSize;
    private Integer publishInterval;
    private Integer connectTimeout;
    private Integer socketTimeout;
    private Boolean gzipCompression;

    private SamsaraClientConfig() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getMaxBufferSize() {
        return maxBufferSize;
    }

    public void setMaxBufferSize(int maxBufferSize) {
        this.maxBufferSize = maxBufferSize;
    }

    public int getMinBufferSize() {
        return minBufferSize;
    }

    public void setMinBufferSize(int minBufferSize) {
        this.minBufferSize = minBufferSize;
    }

    public int getPublishInterval() {
        return publishInterval;
    }

    public void setPublishInterval(int publishInterval) {
        this.publishInterval = publishInterval;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public boolean isGzipCompression() {
        return gzipCompression;
    }

    public void setGzipCompression(boolean gzipCompression) {
        this.gzipCompression = gzipCompression;
    }

    public static ConfigBuilder builder() {
        return new ConfigBuilder();
    }

    public static class ConfigBuilder {
        private SamsaraClientConfig instance = new SamsaraClientConfig();

        public ConfigBuilder url(String url) {
            if (url == null) {
                throw new IllegalArgumentException("Url cannot be null");
            }
            instance.url = url;
            return this;
        }

        public ConfigBuilder maxBufferSize(int maxBufferSize) {
            instance.maxBufferSize = maxBufferSize;
            return this;
        }

        public ConfigBuilder minBufferSize(int minBufferSize) {
            instance.minBufferSize = minBufferSize;
            return this;
        }

        public ConfigBuilder publishInterval(int publishInterval) {
            instance.publishInterval = publishInterval;
            return this;
        }

        public ConfigBuilder connectTimeout(int connectTimeout) {
            instance.connectTimeout = connectTimeout;
            return this;
        }

        public ConfigBuilder socketTimeout(int socketTimeout) {
            instance.socketTimeout = socketTimeout;
            return this;
        }

        public ConfigBuilder gzipCompression(boolean gzipCompression) {
            instance.gzipCompression = gzipCompression;
            return this;
        }

        public SamsaraClientConfig build() {
            if (instance.url == null)
                throw new NullPointerException("Url cannot be null");
            if (instance.maxBufferSize == null)
                instance.maxBufferSize = DEFAULT_MAX_BUFFER_SIZE;
            if (instance.minBufferSize == null)
                instance.minBufferSize = DEFAULT_MIN_BUFFER_SIZE;
            if (instance.publishInterval == null)
                instance.publishInterval = DEFAULT_PUBLISH_INTERVAL;
            if (instance.connectTimeout == null)
                instance.connectTimeout = DEFAULT_CONNECT_TIMEOUT;
            if (instance.socketTimeout == null)
                instance.socketTimeout = DEFAULT_SOCKET_TIMEOUT;
            if (instance.gzipCompression == null)
                instance.gzipCompression = DEFAULT_GZIP_COMPRESSION;
            return instance;
        }
    }
}
