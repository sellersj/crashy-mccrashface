package com.example.crashymccrashface;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CrashController {

    private final Logger logger = LoggerFactory.getLogger(CrashController.class);

    @Autowired
    private Leaker leaker;

    @Autowired
    private LoadGenerator loadGenerator;

    /** Max time we will run a cpu load. */
    private static final int MAX_LOAD_TIME_SECONDS = 6 * 60;

    @RequestMapping("/")
    @ResponseBody
    public String home() {
        // this is a horrible way to serve html. I'm doing it anyways.
        String html = "<html>" + //
            "<a href='leak'>Fast leak</a><br/>" + //
            "<a href='load?s=1'>Generate a load for 1 second</a><br/>" + //
            "<a href='actuator'>Actuators list</a><br/>" + //
            "<a href='die'>Kill the application</a><br/>" + //
            "</html>";

        return html;
    }

    @RequestMapping("/leak")
    @ResponseBody
    public String leak() {
        leaker.leakMemory();

        return String.format("I'm not dead yet!\nMemory left: %s of %s", Runtime.getRuntime().freeMemory(),
            Runtime.getRuntime().totalMemory());
    }

    @RequestMapping("/load")
    @ResponseBody
    public String load(@RequestParam(value = "s", required = false) int seconds) {

        if (seconds > MAX_LOAD_TIME_SECONDS) {
            return "Load can only be put on for a max of " + MAX_LOAD_TIME_SECONDS + " seconds. Not creating load.";
        }

        loadGenerator.generateLoad(seconds * 1000);
        return String.format("Increasing cpu load for %s seconds.", seconds);
    }

    @RequestMapping("/die")
    public void die() {
        logger.info("I've seen things you people wouldn't believe. Attack ships on fire off the shoulder of Orion. "
            + "I watched C-beams glitter in the dark near the Tannhäuser Gate. "
            + "All those moments will be lost in time, like tears in rain. Time to die.");
        System.exit(1);
    }

}
