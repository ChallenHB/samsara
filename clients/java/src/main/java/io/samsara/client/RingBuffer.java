package io.samsara.client;

import java.util.Collection;
import java.util.List;

/**
 *
 * Design Question: Do we want to allow the user to be able to create their own implementations of this interface,
 * and pass it to the SamsaraClient? This could cause issues if Client users provide implementations that are not
 * actually thread safe.
 *
 *
 */
public interface RingBuffer {

    public boolean add(Event event);

    public boolean remove(List<Event> event);

    public Collection<Event> returnAll();

    public int size();
}
