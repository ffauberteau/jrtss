package org.triaxx.jrtss.event;

import org.triaxx.jrtss.JobContext;
import org.triaxx.jrtss.Processor;

public class JobPreemptedEvent extends AbstractJobEvent {

    private final JobContext dstContext;
    private final JobContext srcContext;
    private final Processor processor;

    public JobPreemptedEvent(long time, JobContext dstContext, JobContext srcContext, Processor processor) {
        super(EventType.JOB_PREEMPTED, time, srcContext.getJob());
        this.srcContext = srcContext;
        this.dstContext = dstContext;
        this.processor = processor;
    }

    @Override
    public String getLoggingMessage() {
        return "job " + getDestinationContext() + " preempted by " + getSourceContext();
    }

    public JobContext getDestinationContext() {
        return dstContext;
    }

    public JobContext getSourceContext() {
        return srcContext;
    }

    public Processor getProcessor() {
        return processor;
    }
}