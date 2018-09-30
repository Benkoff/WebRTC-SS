package io.github.benkoff.webrtcss.controller;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MainControllerTest {
    @Autowired private MockMvc mockMvc;

    @Test
    public void shouldReturnMainViewStatusOk_whenDisplayMainPage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("main"))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(content().string(Matchers.containsString("Main Page")));
    }

    @Test
    public void should_whenProcessRoomSelection() throws Exception {
        Long expectedValue = 33L;
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("id", expectedValue.toString());

        mockMvc.perform(post("/room").params(map))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));
        //TODO with room processing complete
    }

    @Test
    public void shouldReturnMainViewStatusOk_whenRequestRandomRoomNumber() throws Exception {
        mockMvc.perform(get("/room/random"))
                .andExpect(status().isOk())
                .andExpect(view().name("main"))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(content().string(Matchers.containsString("Main Page")))
                .andExpect(model().attribute("id", Matchers.greaterThan(-1L)))
                .andExpect(model().attribute("id", Matchers.lessThan(100L)));
    }

    @Test
    public void shouldReturnStreamViewStatusOk_whenDisplaySampleSdpOffer() throws Exception {
        mockMvc.perform(get("/offer"))
                .andExpect(status().isOk())
                .andExpect(view().name("sdp_offer"))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(content().string(Matchers.containsString("SDP Offer")));
    }

    @Test
    public void shouldReturnStreamViewStatusOk_whenDisplaySampleStreaming() throws Exception {
        mockMvc.perform(get("/stream"))
                .andExpect(status().isOk())
                .andExpect(view().name("streaming"))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(content().string(Matchers.containsString("Sample Streaming")));
    }
}
