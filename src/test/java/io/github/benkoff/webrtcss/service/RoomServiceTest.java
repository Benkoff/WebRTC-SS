package io.github.benkoff.webrtcss.service;

import io.github.benkoff.webrtcss.domain.Room;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RoomServiceTest {
    @Autowired RoomService service;

    @Test
    public void shouldReturnRoom_whenFindRoomByStringId() {
        Room expectedRoom = new Room(1L);
        expectedRoom.setHostName(UUID.randomUUID().toString());
        expectedRoom.setVisitorName(UUID.randomUUID().toString());

        service.addRoom(expectedRoom);
        Room actualRoom = service.findRoomByStringId(Long.valueOf(1L).toString()).get();

        assertThat(actualRoom)
                .isNotNull()
                .isEqualToComparingFieldByFieldRecursively(expectedRoom);
    }
}
