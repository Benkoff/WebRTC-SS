package io.github.benkoff.webrtcss.util;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class Parser {
    public Optional<Long> parseId(String sid) {
        Long id = null;
        try {
            id = Long.valueOf(sid);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Optional.ofNullable(id);
    }
}
