package io.github.benkoff.webrtcss.domain;

import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

@Service
public class RoomService {
    // repository substitution since this is a very simple realization
    private final Set<Room> rooms = new TreeSet<>(Comparator.comparing(Room::getId));

    public RoomService() {
    }

    public Set<Room> getRooms() {
        return rooms;
    }
}
