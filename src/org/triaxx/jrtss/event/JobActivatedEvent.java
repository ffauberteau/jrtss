package org.triaxx.jrtss.event;

import org.triaxx.jrtss.Job;

public class JobActivatedEvent extends AbstractJobEvent {

    public JobActivatedEvent(long time, Job job) {
        super(EventType.JOB_ACTIVATED, time, job);
    }

    @Override
    public String getLoggingMessage() {
        return "job " + getJob() + " activated";
    }

}