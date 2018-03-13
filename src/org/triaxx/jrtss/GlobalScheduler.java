package org.triaxx.jrtss;

import org.triaxx.jrtss.event.*;

import java.util.HashSet;
import java.util.Set;

public class GlobalScheduler {

    private final int nbProcessors;
    private final PriorityManager priorityManager;

    public GlobalScheduler(PriorityManager priorityManager, int nbProcessors) {
        this.priorityManager = priorityManager;
        this.nbProcessors = nbProcessors;
    }

    public void schedule(Set<Task> taskSet, long duration) {
        SchedulingContext sc = new SchedulingContext(priorityManager, nbProcessors, duration);
        taskSet.forEach(t -> sc.events.offer(new TaskStartedEvent(t.getOffset(), t )));
        while (!sc.events.isEmpty()) {
            Event event = sc.events.poll();
            updateExecution(sc, event.getTime());
            handle(event, sc);
        }
    }

    private void handle(Event event, SchedulingContext sc) {
        event.log();
        switch (event.getType()) {
            case JOB_ACTIVATED:
                handleJobActivated((JobActivatedEvent) event, sc);
                break;
            case JOB_COMPLETED:
                handleJobCompleted((JobCompletedEvent) event, sc);
                break;
            case JOB_CHECK_COMPLETED:
                handleJobCheckCompleted((JobCheckCompletedEvent) event, sc);
                break;
            case JOB_PREEMPTED:
                handleJobPreempted((JobPreemptedEvent) event, sc);
                break;
            case JOB_STARTED:
                handleJobStarted((JobStartedEvent) event, sc);
                break;
            case JOB_STOPPED:
                handleJobStopped((JobStoppedEvent) event, sc);
                break;
            case TASK_STARTED:
                handleTaskStarted((TaskStartedEvent) event, sc);
                break;
            default:
                throw new IllegalArgumentException(event.getType() + " is not handled by " + this.getClass());
        }
    }

    private void handleJobActivated(JobActivatedEvent event, SchedulingContext sc) {
        Job job = event.getJob();
        Task task = job.getTask();
        sc.waitingQueue.offer(new JobContext(job));
        long nextRelease = event.getTime() + task.getPeriod();
        if (nextRelease < sc.duration) {
            long nextDeadline = nextRelease + task.getDeadline();
            Job nextJob = new Job(nextRelease, task.getWcet(), nextDeadline, task);
            sc.events.offer(new JobActivatedEvent(nextRelease, nextJob));
        }
        updateAdmission(sc, event.getTime());
    }

    private void handleJobCheckCompleted(JobCheckCompletedEvent event, SchedulingContext sc) {
        Processor processor = event.getProcessor();
        if (processor.isIdle()) {
            throw new SchedulingException(processor + " should execute " + event.getJob());
        }
        JobContext context = processor.getCurrentContext();
        if (context.getJob().equals(event.getJob()) && context.getRemainingTime() == 0) {
            sc.events.offer(new JobStoppedEvent(event.getTime(), context, processor));
        }
    }

    private void handleJobCompleted(JobCompletedEvent event, SchedulingContext sc) {
        updateAdmission(sc, event.getTime());
    }

    private void handleJobPreempted(JobPreemptedEvent event, SchedulingContext sc) {
        Processor processor = event.getProcessor();
        long time = event.getTime();
        sc.events.offer(new JobStoppedEvent(time, event.getDestinationContext(), processor));
        sc.events.offer(new JobStartedEvent(time, event.getSourceContext(), processor));
    }

    private void handleJobStarted(JobStartedEvent event, SchedulingContext sc) {
        long time = event.getTime();
        Processor processor = event.getProcessor();
        JobContext context = event.getJobContext();
        if (processor.accept(context, time)) {
            sc.events.offer(new JobCheckCompletedEvent(time + context.getRemainingTime(), context, processor));
        }
    }

    private void handleJobStopped(JobStoppedEvent event, SchedulingContext sc) {
        JobContext context = event.getJobContext();
        Processor processor = event.getProcessor();
        if (context.getRemainingTime() > 0) {
            sc.waitingQueue.offer(context);
        } else {
            sc.events.offer(new JobCompletedEvent(event.getTime(), context.getJob()));
        }
        processor.free();
    }

    private void handleTaskStarted(TaskStartedEvent event, SchedulingContext sc) {
        Task task = event.getTask();
        long release = event.getTime();
        long deadline = release + task.getDeadline();
        Job job = new Job(release, task.getWcet(), deadline, task);
        sc.events.offer(new JobActivatedEvent(release, job));
    }

    private void updateAdmission(SchedulingContext sc, long time) {
        for (Processor processor : sc.processors) {
            if (processor.canAccept(sc.waitingQueue.peek(), priorityManager)) {
                JobContext context = sc.waitingQueue.poll();
                if (processor.isIdle()) {
                    sc.events.offer(new JobStartedEvent(time, context, processor));
                } else {
                    sc.events.offer(new JobPreemptedEvent(time, processor.getCurrentContext(), context, processor));
                }
            }
        }
    }

    private void updateExecution(SchedulingContext sc, long time) {
        for (Processor processor : sc.processors) {
            processor.update(time);
        }
    }

    private class SchedulingContext {

        private final EventQueue events;
        private final Set<Processor> processors;
        private final WaitQueue waitingQueue;
        private final long duration;

        private SchedulingContext(PriorityManager priorityManager, int nbProcessors, long duration) {
            events = new EventQueue(priorityManager);
            waitingQueue = new WaitQueue(priorityManager);
            processors = new HashSet<>();
            for (int m = 0; m < nbProcessors; ++m) {
                processors.add(new Processor(m));
            }
            this.duration = duration;
        }

    }

}