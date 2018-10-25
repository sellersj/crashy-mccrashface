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
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

/** The whole idea of this class is to just introduce cpu load. */
@Component
public class LoadGeneratorImpl implements LoadGenerator, InitializingBean {

    private Logger logger = LoggerFactory.getLogger(LoadGeneratorImpl.class);

    /** For generating random bytes. */
    private Random random = new Random();

    /** The secret key. */
    private SecretKey aesKey;

    @Override
    public void afterPropertiesSet() throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128);
        aesKey = kgen.generateKey();
    }

    @Override
    public void generateLoad() {
        int loopCount = random.nextInt(500) + 500;
        generateLoad(loopCount);
    }

    @Override
    public void generateLoad(int numberOfLoops) {

        StopWatch watch = new StopWatch();
        watch.start();
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, aesKey);

            for (int i = 0; i < numberOfLoops; i++) {
                byte[] array = new byte[Integer.MAX_VALUE / 12000];
                random.nextBytes(array);
                cipher.update(array);
            }

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
        watch.stop();
        logger.info("Load of " + numberOfLoops + " of loops took " + watch.getTotalTimeSeconds());
    }
}
