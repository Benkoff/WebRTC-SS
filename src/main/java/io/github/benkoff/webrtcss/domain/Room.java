package io.github.benkoff.webrtcss.domain;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class Room {
    @NotNull private Long id;
    private String hostName;
    private String visitorName;

    public Room(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getVisitorName() {
        return visitorName;
    }

    public void setVisitorName(String visitorName) {
        this.visitorName = visitorName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return Objects.equals(getId(), room.getId()) &&
                Objects.equals(getHostName(), room.getHostName()) &&
                Objects.equals(getVisitorName(), room.getVisitorName());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId(), getHostName(), getVisitorName());
    }
}
