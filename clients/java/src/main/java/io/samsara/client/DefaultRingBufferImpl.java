package io.samsara.client;

import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * The default implementation of the RingBuffer interface. Currently backed by ConcurrentLinkedList
 * Things to think about:
 *  - Do we want to provide different strategies for evicting the most recent Event from the Queue.
 *  -
 */
public class DefaultRingBufferImpl implements RingBuffer {

    private final static int DEFAULT_MAX_SIZE = 100;

    private final int maxSize;
    private Queue<Event> queue = new ConcurrentLinkedQueue<>();

    public DefaultRingBufferImpl() {
        this.maxSize = DEFAULT_MAX_SIZE;
    }

    public DefaultRingBufferImpl(int maxSize) {
        this.maxSize = maxSize;
    }

    @Override
    public boolean add(Event event) {
        if (queue.size() == maxSize) {
            queue.remove();
        }
        queue.add(event);
        return true;
    }

    @Override
    public boolean remove(List<Event> events) {
        return queue.removeAll(events);
    }

    @Override
    public Collection<Event> returnAll() {
        return queue;
    }

    @Override
    public int size() {
        return queue.size();
    }
}
