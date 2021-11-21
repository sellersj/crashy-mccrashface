package com.example.crashymccrashface;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class CrashControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void postToRootIsRedirect() throws Exception {
        mockMvc.perform(post("/")).andDo(print()).andExpect(status().is3xxRedirection())
            .andExpect(content().string(containsString("")));
    }

    @Test
    public void getToRootHasCorrectView() throws Exception {
        mockMvc.perform(get("/")).andDo(print()).andExpect(status().isOk())
            .andExpect(view().name(containsString("home")));
    }

    @Test
    public void getLog() throws Exception {
        mockMvc.perform( //
            get("/log").param("num", "1"))//
            .andDo(print()).andExpect(status().isOk()) //
            .andExpect(content().string(containsString("Logging 1 messages")));
    }

    @Test
    @Disabled("disable for now")
    public void getException(TestInfo testInfo) throws Exception {
        String exceptionParam = "bad_arguments";
        String pramText = "from unit test " + testInfo.getDisplayName();
        String expected = "Thrown on purpose as a test from the controller with text: " + pramText;

        mockMvc.perform(get("/exception", exceptionParam) //
            .param("text", pramText)) //
            .andExpect(status().isBadRequest())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof RuntimeException))
            .andExpect(result -> assertEquals(expected, result.getResolvedException().getMessage()));

    }
}
