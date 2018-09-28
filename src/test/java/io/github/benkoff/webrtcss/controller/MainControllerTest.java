package io.github.benkoff.webrtcss.controller;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MainControllerTest {
    @Autowired private MockMvc mockMvc;

    @Test
    public void shouldReturnMainViewStatusOk_whenRootPageRequested() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("main"))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(content().string(Matchers.containsString("Main Page")));
    }

    @Test
    public void shouldReturnStreamViewStatusOk_whenStreamRequested() throws Exception {
        mockMvc.perform(get("/stream"))
                .andExpect(status().isOk())
                .andExpect(view().name("streaming"))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(content().string(Matchers.containsString("Sample Streaming")));
    }
}
