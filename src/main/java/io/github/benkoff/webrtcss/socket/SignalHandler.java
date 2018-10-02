package io.github.benkoff.webrtcss.socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class SignalHandler extends TextWebSocketHandler {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionClosed(final WebSocketSession session, final CloseStatus status) throws Exception {
        // webSocket has been closed
    }

    @Override
    public void afterConnectionEstablished(final WebSocketSession session) throws Exception {
        // webSocket has been opened
        //TODO Save this session to send messages outside

        // very first message from client
        session.sendMessage(new TextMessage("Connection to the server has been established"));
    }

    @Override
    protected void handleTextMessage(final WebSocketSession session, final TextMessage textMessage) throws Exception {
        // a message has been received
        logger.debug("Message received: {}", textMessage.getPayload());
    }
}
