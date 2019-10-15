package com.trilogyed.eurekaserverstwitter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EurekaServerStwitterApplication {

	public static void main(String[] args) {
		SpringApplication.run(EurekaServerStwitterApplication.class, args);
	}

}
