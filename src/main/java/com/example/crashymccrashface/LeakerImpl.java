package com.example.crashymccrashface;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class LeakerImpl implements Leaker {

    private final List<byte[]> memoryLeak = new ArrayList<>();

    /*
     * (non-Javadoc)
     *
     * @see com.example.crashymccrashface.Leaker#leakMemory()
     */
    @Override
    public void leakMemory() {
        leak(1000);
    }

    @Override
    public void slowLeak() {
        leak(1);
    }

    private void leak(int loopSize) {
        System.out.println("Before memory: " + getMemory());
        for (int i = 0; i < loopSize; i++) {

            // TODO figure out how to use the VM args ExitOnOutOfMemoryError

            try {
                memoryLeak.add(new byte[1024]);
            } catch (OutOfMemoryError e) {
                System.out.println("Killing self due to OOM");
                System.exit(1);
            }
        }
        System.out.println("After memory : " + getMemory());
    }

    private long getMemory() {
        return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }

}
