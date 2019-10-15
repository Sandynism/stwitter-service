package com.trilogyed.configserverstwitter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class ConfigServerStwitterApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConfigServerStwitterApplication.class, args);
	}

}
