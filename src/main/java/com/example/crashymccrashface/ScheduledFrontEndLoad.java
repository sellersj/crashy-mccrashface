package com.example.crashymccrashface;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@ConditionalOnProperty(prefix = "com.example.crashymccrashface", name = "schedules", havingValue = "true", matchIfMissing = true)
public class ScheduledFrontEndLoad {

    private final static Logger logger = LoggerFactory.getLogger(Schedules.class);

    /** Where this app can hit itself. */
    private String hostPort = "http://localhost:8080/";

    /** For generating random bytes. */
    private final Random random = new Random();

    @Scheduled(fixedDelay = 5, initialDelay = 35, timeUnit = TimeUnit.SECONDS)
    public void requestsToRoot() {
        logger.info("about to make a http call to the root of the app");
        RestClient restClient = RestClient.create();
        restClient.get().uri(hostPort).retrieve().body(String.class);
    }

    @Scheduled(fixedDelay = 45, initialDelay = 35, timeUnit = TimeUnit.SECONDS)
    public void requestsToSlowUrl() {
        int delay = random.nextInt(5) + 2;
        logger.info("about to make a http call to the slow url with a delay for " + delay + " seconds");

        RestClient restClient = RestClient.create();
        restClient.get().uri(hostPort + "sleep?s=" + delay).retrieve().body(String.class);
    }

    @Scheduled(fixedDelay = 37, initialDelay = 45, timeUnit = TimeUnit.SECONDS)
    public void requestsThatWillThrowAnException() {
        logger.info("about to make a http call that should throw an exception");
        RestClient restClient = RestClient.create();
        restClient.get().uri(hostPort + "exception").retrieve().toEntity(String.class);
        // the above line throws an exception so nothing under will be reached
    }

}
