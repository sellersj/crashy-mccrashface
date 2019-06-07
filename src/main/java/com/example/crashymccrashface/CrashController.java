package com.example.crashymccrashface;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CrashController {

    private final Logger logger = LoggerFactory.getLogger(CrashController.class);

    @Autowired
    private Leaker leaker;

    @Autowired
    private LoadGenerator loadGenerator;

    @Autowired
    private LogGenerator logGenerator;

    /** Max time we will run a cpu load. */
    private static final int MAX_LOAD_TIME_SECONDS = 6 * 60;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public String home() {
        // this is a horrible way to serve html. I'm doing it anyways.
        String html = "<html>" + //
            "<a href='leak'>Fast leak</a><br/>" + //
            "<a href='load?s=1'>Generate a load for 1 second</a><br/>" + //
            "<a href='sleep?s=15'>Sleep the thread for 15 second</a><br/>" + //
            "<a href='actuator'>Actuators list</a><br/>" + //
            "<a href='exception'>Throws an error</a><br/>" + //
            "<a href='log?num=42'>Log some messages</a><br/>" + //
            "<p><form action='/' method='post'><input type='submit' value='Post to context root'></form><p>" + //
            //
            "<br/><a href='hemorrhage'>Scheduled task that will leak until crashes</a><br/>" + //
            "<br/><a href='die'>Kill the application</a><br/>" + //
            // these are so if someone hits the main page it will generate these stats
            "<img src='this-image-does-not-exist.gif' alt='this generates a 404' height='1' width='1'>" + //
            "<img src='exception?text=from-image-tag' alt='this generates an exception' height='1' width='1'>" + //
            "<img src='sleep?s=1' alt='sleep baby sleep' height='1' width='1'>" + //
            "</html>";

        return html;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String homeAsAPost() {
        return "redirect:/";
    }

    @RequestMapping("/leak")
    @ResponseBody
    public String leak() {
        leaker.leakMemory();

        return String.format("I'm not dead yet!\nMemory left: %s of %s", Runtime.getRuntime().freeMemory(),
            Runtime.getRuntime().totalMemory());
    }

    @RequestMapping("/hemorrhage")
    @ResponseBody
    public String hemorrhage() {
        leaker.hemorrhage();
        return "This will leak every minute until the app crashes. Goodbye.";
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

    @RequestMapping("/sleep")
    @ResponseBody
    public String sleep(@RequestParam(value = "s", required = false) int seconds) {
        int max = 30;
        if (seconds > max) {
            return "Requests can only sleep for a max of " + max + " seconds. Not sleeping.";
        }

        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            logger.error("Could not sleep thread", e);
        }

        return String.format("Done sleeping request for %s seconds.", seconds);
    }

    @RequestMapping("/exception")
    public void ohGreatWhatHaveWeHereAnExceptionalCase(@RequestParam(value = "text", required = false) String text) {
        throw new RuntimeException("Thrown on purpose as a test from the controller with text: " + text);
    }

    @RequestMapping("/log")
    @ResponseBody
    public String logSomeMessages(@RequestParam(value = "num", required = false) int num) {
        logGenerator.generateLogs(num);
        return String.format("Logging %s messages.", num);
    }

    @RequestMapping("/die")
    public void die() {
        logger.info("I've seen things you people wouldn't believe. Attack ships on fire off the shoulder of Orion. "
            + "I watched C-beams glitter in the dark near the Tannhäuser Gate. "
            + "All those moments will be lost in time, like tears in rain. Time to die.");
        System.exit(1);
    }

}
