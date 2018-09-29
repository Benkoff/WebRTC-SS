package io.github.benkoff.webrtcss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@SpringBootApplication
@EnableWebSocket
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
