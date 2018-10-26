package com.example.crashymccrashface;

import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

/** The whole idea of this class is to just introduce cpu load. */
@Component
public class LoadGeneratorImpl implements LoadGenerator, InitializingBean {

    private final Logger logger = LoggerFactory.getLogger(LoadGeneratorImpl.class);

    /** For generating random bytes. */
    private final Random random = new Random();

    /** The cipher to use to generate load. */
    private Cipher cipher;

    @Override
    public void afterPropertiesSet() throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128);
        SecretKey aesKey = kgen.generateKey();

        cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, aesKey);
    }

    @Override
    public void generateLoad() {
        int loopCount = random.nextInt(3000) + 200;
        generateLoad(loopCount);
    }

    @Override
    public void generateLoad(long numberOfMilliseonds) {
        Thread myThread = new Thread(new LoadForGivenTime(numberOfMilliseonds), "threadFor" + numberOfMilliseonds);
        myThread.start();
    }

    /**
     * Runs a thread at calculation at least once.
     */
    class LoadForGivenTime implements Runnable {

        private final long timeToStop;

        public LoadForGivenTime(long numberOfMilliseonds) {
            timeToStop = System.currentTimeMillis() + numberOfMilliseonds;
        }

        @Override
        public void run() {
            StopWatch watch = new StopWatch();
            watch.start();
            do {
                byte[] array = new byte[Integer.MAX_VALUE / 12000];
                random.nextBytes(array);
                cipher.update(array);
            } while (System.currentTimeMillis() < timeToStop);

            watch.stop();
            // not sure why, but this doesn't log in the test case
            logger.info("Finished running a load for " + watch.getTotalTimeSeconds());
        }
    }
}
