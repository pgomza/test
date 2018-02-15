package com.horeca.site.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Service
public class TaskSchedulingService {

    private final Set<ScheduledFuture<?>> tasks = Collections.newSetFromMap(new ConcurrentHashMap<>());

    @Autowired
    @Qualifier("threadPoolTaskScheduler")
    private TaskScheduler taskScheduler;

    public ScheduledFuture<?> executeAtTimestamp(Runnable task, Timestamp timestamp) {
        ScheduledFuture<?> scheduledFuture = taskScheduler.schedule(task, new Date(timestamp.getTime()));
        tasks.add(scheduledFuture);
        return scheduledFuture;
    }

    public void cancelAllNotRunningTasks() {
        tasks.forEach(task -> task.cancel(false));
        tasks.clear();
    }
}
