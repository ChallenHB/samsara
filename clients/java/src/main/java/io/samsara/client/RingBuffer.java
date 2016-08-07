package io.samsara.client;

import java.util.List;

/**
 *
 */
public interface RingBuffer {

    public void add(Event event);

    public void remove(List<Event> event);

}
