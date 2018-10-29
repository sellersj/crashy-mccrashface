package com.example.crashymccrashface;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LeakerImpl implements Leaker {

    private static final int DEFAULT_MEM_LEAK_STEP = 1024;

    private final Logger logger = LoggerFactory.getLogger(LeakerImpl.class);

    private final List<byte[]> memoryLeak = new ArrayList<>();

    /*
     * (non-Javadoc)
     *
     * @see com.example.crashymccrashface.Leaker#leakMemory()
     */
    @Override
    public void leakMemory() {
        leak(1000, -1, DEFAULT_MEM_LEAK_STEP);
    }

    @Override
    public void slowLeak() {
        leak(1, -1, DEFAULT_MEM_LEAK_STEP);
    }

    @Override
    public void hemorrhage() {
        // delay 15 seconds so that the steps of memory will show up in metric tools
        leak(Integer.MAX_VALUE, 15, DEFAULT_MEM_LEAK_STEP * DEFAULT_MEM_LEAK_STEP);
    }

    private void leak(int loopSize, int delay, int memStep) {
        Thread myThread = new Thread(new LeakThread(loopSize, delay, memStep), "memoryLeak");
        myThread.start();
    }

    class LeakThread implements Runnable {

        private final int loopSize;

        private final int delay;

        private final int memLeakStep;

        public LeakThread(int loopSize, int delay, int leakStep) {
            this.loopSize = loopSize;
            this.delay = delay;
            this.memLeakStep = leakStep;
        }

        @Override
        public void run() {

            for (int i = 0; i < loopSize; i++) {

                // TODO figure out how to use the VM args ExitOnOutOfMemoryError
                try {
                    memoryLeak.add(new byte[memLeakStep]);

                    if (delay > 0) {
                        try {
                            Thread.sleep(1000 * delay);
                        } catch (InterruptedException e) {
                            throw new RuntimeException("Could not sleep the thread", e);
                        }
                    }
                } catch (OutOfMemoryError e) {
                    logger.info("Killing self due to OOM");
                    System.exit(1);
                }
            }
            logger.info("After memory : " + getMemory());
        }
    }

    private long getMemory() {
        return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }

}
