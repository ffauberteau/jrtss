package org.triaxx.jrtss.event;

import org.triaxx.jrtss.Job;

public interface JobEvent extends Event {

    Job getJob();

}