package org.triaxx.jrtss.event;

import org.triaxx.jrtss.JobContext;
import org.triaxx.jrtss.Processor;

public class JobStoppedEvent extends AbstractJobEvent {

    private final JobContext context;
    private final Processor processor;

    public JobStoppedEvent(long time, JobContext context, Processor processor) {
        super(EventType.JOB_STOPPED, time, context.getJob());
        this.context = context;
        this.processor = processor;
    }

    public JobContext getJobContext() {
        return context;
    }

    @Override
    public String getLoggingMessage() {
        return "job " + getJobContext() + " stopped on " + getProcessor();
    }

    public Processor getProcessor() {
        return processor;
    }
}
