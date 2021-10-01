package cz.petrfilip.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"cz.petrfilip.*"})
public class MultiplayerServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MultiplayerServerApplication.class, args);
	}

}
