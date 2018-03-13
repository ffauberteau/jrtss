package org.triaxx.jrtss;

public class Processor {

    private final int index;

    private JobContext currentContext;
    private long lastEventTime;

    public Processor(int index) {
        this.index = index;
    }

    boolean accept(JobContext context, long time) {
        if (isIdle()) {
            this.currentContext = context;
            lastEventTime = time;
            return true;
        }
        return false;
    }

    /**
     * Test if the processor can accept {@code job} according to the {@code priorityManager}. A job can be accepted in
     * a processor if this processor is not running another job for which the priority is greater than or equal to the
     * one of {@code job}.
     *
     * @param jobContext      The job context to test.
     * @param priorityManager The priority manager that compares jobs.
     * @return true if {@code job} can be accepted on this processor and false otherwise.
     */
    boolean canAccept(JobContext jobContext, PriorityManager priorityManager) {
        if (jobContext == null) {
            return false;
        }
        if (currentContext == null) {
            return true;
        }
        if (priorityManager.compare(jobContext.getJob(), currentContext.getJob()) < 0) {
            return true;
        }
        return false;
    }

    void free() {
        currentContext = null;
    }

    JobContext getCurrentContext() {
        return currentContext;
    }

    boolean isIdle() {
        if (currentContext == null) {
            return true;
        }
        return false;
    }

    void update(long time) {
        if (currentContext != null) {
            currentContext.decreaseRemainingTime(time - lastEventTime);
            lastEventTime = time;
            long remainingTime = currentContext.getRemainingTime();
            if (remainingTime < 0) {
                throw new ExecutionOverrunException(currentContext.getJob() + " overruns its execution at time "
                        + time + ": " + remainingTime);
            }
            if (time > currentContext.getJob().getDeadline()) {
                throw new DeadlineMissedException(currentContext.getJob() + " misses its deadline");
            }
        }
    }

    @Override
    public String toString() {
        return "processor" + index;
    }

}