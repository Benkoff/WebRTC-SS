package io.github.benkoff.webrtcss.socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.benkoff.webrtcss.domain.Room;
import io.github.benkoff.webrtcss.domain.RoomService;
import io.github.benkoff.webrtcss.domain.WebSocketMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Component
public class SignalHandler extends TextWebSocketHandler {
    @Autowired private RoomService roomService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ObjectMapper objectMapper = new ObjectMapper();

    // session id to room mapping
    private Map<String, Room> roomsBySessionId = new HashMap<>();

    private static final String TEXT_TYPE = "text";
    private static final String SIGNAL_TYPE = "signal";
    private static final String JOIN_TYPE = "join";

    @Override
    public void afterConnectionClosed(final WebSocketSession session, final CloseStatus status) throws Exception {
        logger.debug("[ws] Session has been closed with status {}", status);
        Room room = roomsBySessionId.get(session.getId());
        // remove the client of the closed session from the Room's client list
        Optional<String> client = roomService.getClients(room).entrySet().stream()
                .filter(entry -> Objects.equals(entry.getValue().getId(), session.getId()))
                .map(Map.Entry::getKey)
                .findAny();
        client.ifPresent(c -> roomService.removeClientByName(room, c));
    }

    @Override
    public void afterConnectionEstablished(final WebSocketSession session) throws Exception {
        // webSocket has been opened, send first message to client
        sendMessage(session, new WebSocketMessage("Server", TEXT_TYPE, "Connection has been established"));
    }

    @Override
    protected void handleTextMessage(final WebSocketSession session, final TextMessage textMessage) {
        // a message has been received
        try {
            WebSocketMessage message = objectMapper.readValue(textMessage.getPayload(), WebSocketMessage.class);
            logger.debug("[ws] Message of {} type from {} received", message.getType(), message.getFrom());

            switch (message.getType()) {
                // text message from client has been received
                case TEXT_TYPE:
                    logger.debug("[ws] Text message: {}", message.getData().toString());
                    break;
                // process received signal from client
                case SIGNAL_TYPE:
                    logger.debug("[ws] Signal message: {}", message.getData().toString().substring(0, 20));
                    break;
                // identify user and their opponent
                case JOIN_TYPE:
                    logger.debug("[ws] Join room: {}", message.getData().toString());
                    String userName = message.getFrom();
                    String id = message.getData().toString();
                    Room room = roomService.findRoomByStringId(id)
                            .orElseThrow(() -> new IOException("Invalid room number received!"));
                    roomService.addClient(room, userName, session);
                    roomsBySessionId.put(session.getId(), room);

                    break;
                // something should be wrong with the message received
                default:
                    logger.debug("[ws] Type of received message undefined!");
                    // TODO handle this
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(WebSocketSession session, WebSocketMessage message) {
        try {
            String json = objectMapper.writeValueAsString(message);
            session.sendMessage(new TextMessage(json));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
