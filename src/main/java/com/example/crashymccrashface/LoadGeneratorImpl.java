package com.example.crashymccrashface;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

/** The whole idea of this class is to just introduce cpu load. */
@Component
public class LoadGeneratorImpl implements LoadGenerator {

    private final Logger logger = LoggerFactory.getLogger(LoadGeneratorImpl.class);

    /** For generating random bytes. */
    private final Random random = new Random();

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
            try {
                KeyGenerator kgen = KeyGenerator.getInstance("AES");

                kgen.init(128);
                SecretKey aesKey = kgen.generateKey();

                Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
                cipher.init(Cipher.ENCRYPT_MODE, aesKey);

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

            } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
                throw new RuntimeException("Something went wrong initalizing crypto", e);
            }
        }
    }
}
