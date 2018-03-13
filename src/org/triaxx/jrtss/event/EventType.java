package org.triaxx.jrtss.event;

public enum EventType {

    TASK_STARTED,

    JOB_CHECK_COMPLETED,

    JOB_PREEMPTED,

    JOB_COMPLETED,

    JOB_STOPPED,

    /**
     * JOB_STARTED is prior to JOB_ACTIVATED to avoid multiple activation on same processor.
     */
    JOB_STARTED,

    JOB_ACTIVATED;

}