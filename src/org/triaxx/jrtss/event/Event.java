package org.triaxx.jrtss.event;

public interface Event {

    long getTime();

    EventType getType();

    void log();

}