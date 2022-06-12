package nl.fontys.djcollab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class DjCollabApplication {

	public static void main(String[] args) {
		SpringApplication.run(DjCollabApplication.class, args);
	}

}
