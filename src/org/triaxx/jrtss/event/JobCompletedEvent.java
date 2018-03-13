package org.triaxx.jrtss.event;

import org.triaxx.jrtss.Job;

public class JobCompletedEvent extends AbstractJobEvent {

    public JobCompletedEvent(long time, Job job) {
        super(EventType.JOB_COMPLETED, time, job);
    }

    @Override
    public String getLoggingMessage() {
        return "job " + getJob() + " completed";
    }

}