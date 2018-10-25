package com.example.crashymccrashface;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Schedules {

    @Autowired
    private Leaker leaker;

    @Scheduled(fixedRate = 5000)
    public void sloooowLeak() {
        leaker.slowLeak();
    }

}
