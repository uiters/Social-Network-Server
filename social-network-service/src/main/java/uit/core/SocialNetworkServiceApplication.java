package uit.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class SocialNetworkServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SocialNetworkServiceApplication.class, args);
	}

}
