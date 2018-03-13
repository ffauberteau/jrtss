package org.triaxx.jrtss;

public class JobContext {

    private final Job job;
    private long remainingTime;

    public JobContext(Job job) {
        this.job = job;
        remainingTime = job.getExecutionTime();
    }

    public void decreaseRemainingTime(long time) {
        remainingTime -= time;
    }

    public Job getJob() {
        return job;
    }

    public long getRemainingTime() {
        return remainingTime;
    }

    @Override
    public String toString() {
        return getJob() + ":" + getRemainingTime();
    }

}