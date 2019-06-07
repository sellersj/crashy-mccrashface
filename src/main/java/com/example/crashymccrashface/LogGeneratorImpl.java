package com.example.crashymccrashface;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/** This will generate a bunch of log messages. */
@Component
public class LogGeneratorImpl implements LogGenerator {

    private final Logger logger = LoggerFactory.getLogger(LogGeneratorImpl.class);

    /** For generating random bytes. */
    private final Random random = new Random();

    @Override
    public void generateLogs() {
        int loopCount = random.nextInt(100) + 10;
        generateLogs(loopCount);
    }

    @Override
    public void generateLogs(int numberOfMessages) {

        boolean warnOrError = random.nextBoolean();

        for (int i = 1; i < numberOfMessages; i++) {

            String message = String.format("This is a log spike message [%s / %s]", i, numberOfMessages);

            if (warnOrError) {
                logger.warn(message);
            } else {
                logger.error(message);
            }
        }
    }

}
