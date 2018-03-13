package org.triaxx.jrtss.event;

import org.triaxx.jrtss.PriorityManager;

import java.util.Comparator;
import java.util.PriorityQueue;

public class EventQueue extends PriorityQueue<Event> {

    public EventQueue(PriorityManager priorityManager) {
        super(new EventComparator(priorityManager));
    }

    private static class EventComparator implements Comparator<Event> {

        private final PriorityManager priorityManager;

        private EventComparator(PriorityManager priorityManager) {
            this.priorityManager = priorityManager;
        }

        /**
         * Compare two events. The following comparisons are made:
         * 1. time of the events
         * 2. priority of the events (according to the event enumeration order)
         * 3. priority of the jobs (according to the scheduling priority policy)
         *
         * @param e1 The first event.
         * @param e2 The second event.
         * @return 0 if events are considered equivalent, negative number if the first event is prior to the second one
         * and positive number if the first event is after the second one.
         */
        @Override
        public int compare(Event e1, Event e2) {
            long timeDiff = e1.getTime() - e2.getTime();
            if (timeDiff != 0) {
                return (int) timeDiff;
            }
            int ordDiff = e1.getType().ordinal() - e2.getType().ordinal();
            if (ordDiff != 0) {
                return ordDiff;
            }
            if (!(e1 instanceof JobEvent && e2 instanceof JobEvent)) {
                return 0;
            }
            return priorityManager.compare(((JobEvent) e1).getJob(), ((JobEvent) e2).getJob());
        }

    }

}