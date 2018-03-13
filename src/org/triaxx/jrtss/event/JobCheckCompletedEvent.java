package org.triaxx.jrtss.event;

import org.triaxx.jrtss.JobContext;
import org.triaxx.jrtss.Processor;

public class JobCheckCompletedEvent extends AbstractJobEvent {

    private final Processor processor;

    public JobCheckCompletedEvent(long time, JobContext context, Processor processor) {
        super(EventType.JOB_CHECK_COMPLETED, time, context.getJob());
        this.processor = processor;
    }

    public Processor getProcessor() {
        return processor;
    }

    @Override
    public String getLoggingMessage() {
        return "job " + getJob() + " is completed on " + processor + '?';
    }

}