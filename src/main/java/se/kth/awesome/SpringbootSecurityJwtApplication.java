package se.kth.awesome;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * Sample application for demonstrating security with JWT Tokens
 * 
 * @author vladimir.stankovic
 *
 * Aug 3, 2016
 */
@SpringBootApplication
@EnableConfigurationProperties
public class SpringbootSecurityJwtApplication {

	public static int steps = 1;

	public static void main(String[] args) {
		SpringApplication.run(SpringbootSecurityJwtApplication.class, args);
	}
}
