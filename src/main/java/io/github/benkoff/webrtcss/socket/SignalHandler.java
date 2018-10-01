package io.github.benkoff.webrtcss.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class SignalHandler extends TextWebSocketHandler {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    // Jackson JSON converter
//    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // webSocket has been closed
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // webSocket has been opened
        //TODO Save this session to send messages outside

        // very first message from client
        session.sendMessage(new TextMessage("Connection to the server has been established"));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) throws Exception {
        // a message has been received
        logger.debug("Message received: {}", textMessage.getPayload());
    }
}
