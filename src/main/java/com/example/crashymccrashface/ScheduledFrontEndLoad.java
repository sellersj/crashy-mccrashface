package com.example.crashymccrashface;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

@Component
@ConditionalOnProperty(prefix = "com.example.crashymccrashface", name = "schedules", havingValue = "true", matchIfMissing = true)
public class ScheduledFrontEndLoad {

    private final static Logger logger = LoggerFactory.getLogger(Schedules.class);

    private String hostPort = "http://localhost:8080/";

    @Scheduled(fixedDelay = 5 * 1000, initialDelay = 35 * 1000)
    public void requestsToRoot() {
        logger.info("about to make a http call to the root of the app");
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getForObject(hostPort, String.class);
    }

    @Scheduled(fixedDelay = 37 * 1000, initialDelay = 45 * 1000)
    public void requestsThatWillThrowAnException() {
        logger.info("about to make a http call that should throw an exception");
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new SuppressErrorRestTemplateResponseErrorHandler());
        restTemplate.getForObject(hostPort + "exception", String.class);
    }

    private static class SuppressErrorRestTemplateResponseErrorHandler implements ResponseErrorHandler {

        @Override
        public boolean hasError(ClientHttpResponse response) throws IOException {
            logger.debug("error in reset template");
            return false;
        }

        @Override
        public void handleError(ClientHttpResponse response) throws IOException {
            logger.debug("error in reset template");
        }
    }

}
