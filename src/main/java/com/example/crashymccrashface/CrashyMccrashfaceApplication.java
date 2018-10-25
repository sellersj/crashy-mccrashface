package com.example.crashymccrashface;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CrashyMccrashfaceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrashyMccrashfaceApplication.class, args);
    }

}
