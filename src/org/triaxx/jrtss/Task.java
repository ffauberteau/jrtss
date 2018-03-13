package org.triaxx.jrtss;

public class Task {

    private final long deadline;
    private final long offset;
    private final long period;
    private final long wcet;

    public Task(long offset, long wcet, long deadline, long period) {
        this.offset = offset;
        this.wcet = wcet;
        this.deadline = deadline;
        this.period = period;
    }

    public Task(long wcet, long deadline, long period) {
        this(0, wcet, deadline, period);
    }

    public Task(long wcet, long period) {
        this(wcet, period, period);
    }

    public long getDeadline() {
        return deadline;
    }

    public long getOffset() {
        return offset;
    }

    public long getPeriod() {
        return period;
    }

    public long getWcet() {
        return wcet;
    }

    @Override
    public String toString() {
        return "(O=" + getOffset() + ",C=" + getWcet() + ",D=" + getDeadline() + ",P=" + getPeriod() + ')';
    }

}