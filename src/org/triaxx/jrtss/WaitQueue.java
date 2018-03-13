package org.triaxx.jrtss;

import java.util.PriorityQueue;

public class WaitQueue extends PriorityQueue<JobContext> {

    public WaitQueue(PriorityManager priorityManager) {
        super((jc1, jc2) -> priorityManager.compare(jc1.getJob(), jc2.getJob()));
    }

}