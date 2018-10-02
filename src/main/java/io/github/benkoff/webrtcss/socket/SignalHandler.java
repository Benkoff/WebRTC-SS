package io.github.benkoff.webrtcss.socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.benkoff.webrtcss.domain.WebSocketMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

public class SignalHandler extends TextWebSocketHandler {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ObjectMapper objectMapper = new ObjectMapper();
    private WebSocketSession session;

    public static final String TEXT_TYPE = "text";
    public static final String SIGNAL_TYPE = "signal";
    public static final String JOIN_TYPE = "join";

    @Override
    public void afterConnectionClosed(final WebSocketSession session, final CloseStatus status) throws Exception {
        // webSocket has been closed
    }

    @Override
    public void afterConnectionEstablished(final WebSocketSession session) throws Exception {
        // webSocket has been opened
        this.session = session;

        // very first message from client
//        session.sendMessage(new TextMessage("Connection to the server has been established"));
        sendMessage(
                new WebSocketMessage("Server", TEXT_TYPE, "Connection has been established"));
    }

    @Override
    protected void handleTextMessage(final WebSocketSession session, final TextMessage textMessage) {
        // a message has been received
//        logger.debug("Message received: {}", textMessage.getPayload());

        try {
            WebSocketMessage message = objectMapper.readValue(textMessage.getPayload(), WebSocketMessage.class);
            logger.debug("Message of {} type from {} received", message.getType(), message.getFrom());

            switch (message.getType()) {
                case TEXT_TYPE:
                    logger.debug("Text message: {}", message.getData().toString());
                    break;
                case SIGNAL_TYPE:
                    break;
                case JOIN_TYPE:
                    break;
                default:
                    // TODO handle this
                    logger.debug("Type of received message undefined!");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(WebSocketMessage message) {
        try {
            String json = objectMapper.writeValueAsString(message);
            this.session.sendMessage(new TextMessage(json));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
