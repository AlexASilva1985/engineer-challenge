package br.com.engineerchallenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class engineerChallengeApplication {

	public static void main(String[] args) {
		SpringApplication.run(engineerChallengeApplication.class, args);
	}

}

