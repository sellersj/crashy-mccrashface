package com.example.crashymccrashface;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LoadGeneratorTest {

    @Autowired
    private LoadGenerator loadGenerator;

    @Test
    public void generateLoadSmokeTest() {
        loadGenerator.generateLoad(1);
    }

}
