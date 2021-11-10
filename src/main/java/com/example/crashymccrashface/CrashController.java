package com.example.crashymccrashface;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.unit.DataSize;
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

    /**
     * @return the thymeleaf model view name
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home() {
        return "home";
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String homeAsAPost() {
        return "redirect:/";
    }

    @RequestMapping(value = "/leak", produces = { MediaType.TEXT_PLAIN_VALUE })
    @ResponseBody
    public String leak() {
        leaker.leakMemory();

        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        long xms = memoryBean.getHeapMemoryUsage().getInit();
        long xmx = memoryBean.getHeapMemoryUsage().getMax();

        return String.format("I'm not dead yet!\n" //
            + "Init %s and max memory %s \n" //
            + "Memory free %s of %s total \n" //
            + "Percent left %.2f %%", //
            DataSize.ofBytes(xms), //
            DataSize.ofBytes(xmx), //
            DataSize.ofBytes(Runtime.getRuntime().freeMemory()), //
            DataSize.ofBytes(Runtime.getRuntime().totalMemory()), //
            ((double) Runtime.getRuntime().totalMemory() / xmx) * 100.0);
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
