package com.example.crashymccrashface;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "com.example.crashymccrashface", name = "schedules", havingValue = "true", matchIfMissing = true)
public class Schedules {

    private final Logger logger = LoggerFactory.getLogger(Schedules.class);

    @Autowired
    private Leaker leaker;

    @Autowired
    private LoadGenerator loadGenerator;

    @Scheduled(fixedRate = 5000, initialDelay = 5000)
    public void sloooowLeak() {
        leaker.slowLeak();
    }

    /** Leak until we die every morning. */
    @Scheduled(cron = "0 0 6 * * *")
    public void hemorrhage() {
        logger.info("About to leak memory until dead, using schedule task");
        leaker.hemorrhage();
    }

    @Scheduled(fixedRate = 5000, initialDelay = 15000)
    public void cpuLoad() {
        loadGenerator.generateLoad();
    }

}
