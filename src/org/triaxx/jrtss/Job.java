package org.triaxx.jrtss;

public class Job {

    private final long executionTime;
    private final long deadline;
    private final long release;
    private final Task task;

    public Job(long release, long executionTime, long deadline, Task task) {
        this.release = release;
        this.executionTime = executionTime;
        this.deadline = deadline;
        this.task = task;
    }

    protected Job(Job job) {
        this(job.getRelease(), job.getExecutionTime(), job.getDeadline(), job.getTask());
    }

    public long getExecutionTime() {
        return executionTime;
    }

    public long getDeadline() {
        return deadline;
    }

    public long getRelease() {
        return release;
    }

    public Task getTask() {
        return task;
    }

    @Override
    public String toString() {
        return "(r=" + getRelease() + ",e=" + getExecutionTime() + ",d=" + getDeadline() + ')';
    }

}