package io.github.benkoff.webrtcss.domain;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class Room {
    @NotNull private Long id;
    private Long hostId;
    private Long visitorId;

    public Room(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getHostId() {
        return hostId;
    }

    public void setHostId(Long hostId) {
        this.hostId = hostId;
    }

    public Long getVisitorId() {
        return visitorId;
    }

    public void setVisitorId(Long visitorId) {
        this.visitorId = visitorId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return Objects.equals(getId(), room.getId()) &&
                Objects.equals(getHostId(), room.getHostId()) &&
                Objects.equals(getVisitorId(), room.getVisitorId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId(), getHostId(), getVisitorId());
    }
}
