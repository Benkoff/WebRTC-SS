package io.github.benkoff.webrtcss.service;

import io.github.benkoff.webrtcss.domain.Room;
import io.github.benkoff.webrtcss.util.Parser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

@Service
public class RoomService {
    private final Parser parser;
    // repository substitution since this is a very simple realization
    private final Set<Room> rooms = new TreeSet<>(Comparator.comparing(Room::getId));

    @Autowired
    public RoomService(Parser parser) {
        this.parser = parser;
    }

    public Set<Room> getRooms() {
        final TreeSet<Room> defensiveCopy = new TreeSet<>(Comparator.comparing(Room::getId));
        defensiveCopy.addAll(rooms);

        return defensiveCopy;
    }

    public Boolean addRoom(Room room) {
        return rooms.add(room);
    }

    public Optional<Room> findRoomByStringId(String sid) {
        return rooms.stream().filter(r -> r.getId().equals(parser.parseId(sid).get())).findFirst();
    }
}
