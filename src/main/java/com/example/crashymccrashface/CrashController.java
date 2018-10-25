package com.example.crashymccrashface;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CrashController {

    @Autowired
    Leaker leaker;

    @RequestMapping("/leak")
    @ResponseBody
    public String leak() {
        leaker.leakMemory();

        return String.format("I'm not dead yet!\nMemory left: %s of %s", Runtime.getRuntime().freeMemory(),
            Runtime.getRuntime().totalMemory());
    }

    @RequestMapping("/die")
    public void die() {
        System.out
            .println("I've seen things you people wouldn't believe. Attack ships on fire off the shoulder of Orion. "
                + "I watched C-beams glitter in the dark near the Tannhäuser Gate. "
                + "All those moments will be lost in time, like tears in rain. Time to die.");
        System.exit(1);
    }

}
