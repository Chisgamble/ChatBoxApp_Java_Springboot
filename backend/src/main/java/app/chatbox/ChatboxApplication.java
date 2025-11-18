package app.chatbox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import app.chatbox.repository.UserRepository;

@SpringBootApplication
public class ChatboxApplication {

	public static void main(String[] args) {

        SpringApplication.run(ChatboxApplication.class, args);

	}

}
