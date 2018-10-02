package io.github.benkoff.webrtcss.socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import io.github.benkoff.webrtcss.domain.Room;
import io.github.benkoff.webrtcss.domain.RoomService;
import io.github.benkoff.webrtcss.domain.WebSocketMessage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SignalHandlerTest {
    @Autowired private RoomService service;
    @Autowired private SignalHandler handler;

    private String name;
    private WebSocketSession session;
    private Room room;

    @Before
    public void setup() {
        Long id = 1L;
        name = UUID.randomUUID().toString();
        session = mock(WebSocketSession.class);
        room = new Room(id);
        service.addRoom(room);
    }

    @Test
    public void shouldRemoveClient_whenConnectionClosed() throws Exception {
        WebSocketMessage message = new WebSocketMessage(name, "join", room.getId().toString());
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(message);
        TextMessage textMessage = new TextMessage(json);

        handler.handleTextMessage(session, textMessage);

//        assertThat(service.getClients(room))
//                .containsValue(session);

        handler.afterConnectionClosed(session, CloseStatus.NORMAL);

        assertThat(service.getClients(room))
                .isEmpty();
    }

    @After
    public void teardown() {
        name = null;
        session = null;
        room = null;
    }
}
