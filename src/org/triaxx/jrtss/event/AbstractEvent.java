package org.triaxx.jrtss.event;

public abstract class AbstractEvent implements Event {

    private final EventType type;
    private final long time;

    protected AbstractEvent(EventType type, long time) {
        this.type = type;
        this.time = time;
    }

    @Override
    public long getTime() {
        return time;
    }

    @Override
    public EventType getType() {
        return type;
    }

    @Override
    public final void log() {
        // 19 because it is size of Long.MAX_VALUE
        System.out.printf("[%19s] > %s\n", getTime(), getLoggingMessage());
    }

    protected abstract String getLoggingMessage();

}