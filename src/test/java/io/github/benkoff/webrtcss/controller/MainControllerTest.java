package io.github.benkoff.webrtcss.controller;

import io.github.benkoff.webrtcss.domain.Room;
import io.github.benkoff.webrtcss.domain.RoomService;
import io.github.benkoff.webrtcss.util.Parser;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Repeat;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

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
    @Autowired private WebApplicationContext webApplicationContext;
    @Autowired private RoomService service;
    @Autowired private Parser parser;
    private MainController controller;
    private MockMvc mockMvc;

    private Long expectedId;
    private String host;
    private String uuid;
    private Room expectedRoom;
    private MultiValueMap<String, String> map;

    @Before
    public void setup() {
        controller = new MainController(service, parser);
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();

        expectedId = 33L;
        host = UUID.randomUUID().toString();
        uuid = UUID.randomUUID().toString();
        map = new LinkedMultiValueMap<>();
        expectedRoom = new Room(expectedId);
        map.add("id", expectedId.toString());
        map.add("action", "create");
    }

    @Test
    public void shouldReturnMainViewStatusOk_whenDisplayMainPage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("main"))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(content().string(Matchers.containsString("Main Page")));
    }

    @Test
    public void shouldAddRoomWithNumberSelected_whenProcessRoomSelection() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(model().attribute("rooms", Matchers.empty()));
        mockMvc.perform(post("/room").params(map));
        mockMvc.perform(get("/"))
                .andExpect(model().attribute("rooms", Matchers.contains(expectedRoom)));
    }

    @Test
    public void shouldReturnChatRoomOk_whenDisplaySelectedRoom_paramsOk() throws Exception {
        mockMvc.perform(post("/room").params(map));
        mockMvc.perform(get("/room/" + expectedId + "/user/" + uuid))
                .andExpect(status().isOk())
                .andExpect(view().name("chat_room"))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(content().string(Matchers.containsString("Chat Room")));
        mockMvc.perform(get("/"))
                .andExpect(model().attribute("rooms",
                        Matchers.hasItem(
                                Matchers.<Room> hasProperty("id",
                                        Matchers.equalTo(expectedId)))));
    }

    @Test
    public void shouldReturnChatRoomOk_whenDisplaySelectedRoom_withHost() throws Exception {
        mockMvc.perform(post("/room").params(map));
        mockMvc.perform(get("/room/" + expectedId + "/user/" + host))
                .andExpect(status().isOk())
                .andExpect(view().name("chat_room"))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(content().string(Matchers.containsString("Chat Room")));
        mockMvc.perform(get("/"))
                .andExpect(model().attribute("rooms",
                        Matchers.hasItem(
                                Matchers.<Room> hasProperty("id",
                                        Matchers.equalTo(expectedId)))));
    }

    @Test
    public void shouldRedirect_whenDisplaySelectedRoom_withInvalidRoom() throws Exception {
        Long invalidValue = 99L;
        String visitor = UUID.randomUUID().toString();

        mockMvc.perform(post("/room").params(map));
        mockMvc.perform(get("/room/" + invalidValue + "/user/" + visitor))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));
        mockMvc.perform(get("/"))
                .andExpect(model().attribute("rooms",
                        Matchers.hasItem(
                                Matchers.<Room> hasProperty("id",
                                        Matchers.equalTo(expectedId)))))
                .andExpect(model().attribute("rooms",
                        Matchers.not(
                                Matchers.hasItem(
                                        Matchers.<Room> hasProperty("id",
                                                Matchers.equalTo(invalidValue))))));
    }

    @Test
    public void shouldReturnMainView_whenProcessRoomExit() throws Exception {
        //TODO
    }

    @Repeat(10)
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
