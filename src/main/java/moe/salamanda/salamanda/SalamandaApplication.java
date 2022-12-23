package moe.salamanda.salamanda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootApplication
public class SalamandaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalamandaApplication.class, args);
	}

}
