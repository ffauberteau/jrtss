package org.triaxx.jrtss.event;

import org.triaxx.jrtss.Task;

public class TaskStartedEvent extends AbstractEvent {

    private final Task task;

    public TaskStartedEvent(long time, Task task) {
        super(EventType.TASK_STARTED, time);
        this.task = task;
    }

    public Task getTask() {
        return task;
    }

    @Override
    public String getLoggingMessage() {
        return "task " + getTask() + " started";
    }

}