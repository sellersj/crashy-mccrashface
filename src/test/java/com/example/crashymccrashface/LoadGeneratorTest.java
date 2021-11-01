package com.example.crashymccrashface;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LoadGeneratorTest {

    @Autowired
    private LoadGenerator loadGenerator;

    @Test
    public void generateLoadSmokeTest() {
        long start = System.currentTimeMillis();
        loadGenerator.generateLoad(1);
        long end = System.currentTimeMillis();
        assertTrue(end > start,
            String.format("should have taken some time to run the tests. start: %s end %s", start, end));
    }

}
