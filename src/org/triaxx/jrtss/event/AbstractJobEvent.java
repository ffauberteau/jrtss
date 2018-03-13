package org.triaxx.jrtss.event;

import org.triaxx.jrtss.Job;

public abstract class AbstractJobEvent extends AbstractEvent implements JobEvent {

    private final Job job;

    protected AbstractJobEvent(EventType type, long time, Job job) {
        super(type, time);
        this.job = job;
    }

    @Override
    public Job getJob() {
        return job;
    }

}