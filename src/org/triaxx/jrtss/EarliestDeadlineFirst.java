package org.triaxx.jrtss;

public class EarliestDeadlineFirst implements PriorityManager {

    /**
     * Compare the priority of two jobs according to the EDF policy.
     *
     * @param j1 The first job.
     * @param j2 The second job.
     * @return 0 if both jobs have the same absolute deadline, negative number if the absolute deadline of the first job
     * is prior to the one of the second job and positive number if the absolute deadline of the first job is after the
     * one of the second job.
     */
    @Override
    public int compare(Job j1, Job j2) {
        return (int) (j1.getDeadline() - j2.getDeadline());
    }

}