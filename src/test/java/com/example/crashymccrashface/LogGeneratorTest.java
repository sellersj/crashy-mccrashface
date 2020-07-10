package com.example.crashymccrashface;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LogGeneratorTest {

    @Autowired
    private LogGenerator logGenerator;

    @Test
    public void generateLoadSmokeTest() {
        logGenerator.generateLogs();
    }
}
