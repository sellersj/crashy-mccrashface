package com.example.crashymccrashface;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Schedules {

    @Autowired
    private Leaker leaker;

    @Autowired
    private LoadGenerator loadGenerator;

    @Scheduled(fixedRate = 5000, initialDelay = 5000)
    @ConditionalOnProperty(prefix = "com.example.crashymccrashface.schedule", name = "memleak", havingValue = "true", matchIfMissing = true)
    public void sloooowLeak() {
        leaker.slowLeak();
    }

    @Scheduled(fixedRate = 5000, initialDelay = 15000)
    @ConditionalOnProperty(prefix = "com.example.crashymccrashface.schedule", name = "cpuLoad", havingValue = "true", matchIfMissing = true)
    public void cpuLoad() {
        loadGenerator.generateLoad();
    }

}
