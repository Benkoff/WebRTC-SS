package io.github.benkoff.webrtcss.domain;

import java.util.Objects;

public class WebSocketMessage {
    private String from;
    private String type;
    private Object data;

    public WebSocketMessage() {
    }

    public WebSocketMessage(final String from, final String type, final Object data) {
        this.from = from;
        this.type = type;
        this.data = data;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(final String from) {
        this.from = from;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public Object getData() {
        return data;
    }

    public void setData(final Object data) {
        this.data = data;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final WebSocketMessage message = (WebSocketMessage) o;
        return Objects.equals(getFrom(), message.getFrom()) &&
                Objects.equals(getType(), message.getType()) &&
                Objects.equals(getData(), message.getData());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getFrom(), getType(), getData());
    }

    @Override
    public String toString() {
        return "WebSocketMessage{" +
                "from='" + from + '\'' +
                ", type='" + type + '\'' +
                ", data=" + data.toString() +
                '}';
    }
}
