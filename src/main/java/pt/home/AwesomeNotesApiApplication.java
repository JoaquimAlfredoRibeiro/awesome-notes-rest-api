package pt.home;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import pt.home.utils.FileStorageProperties;

import lombok.extern.log4j.Log4j2;

@EnableConfigurationProperties({
        FileStorageProperties.class
})
@SpringBootApplication
@Log4j2
public class AwesomeNotesApiApplication {

	public static void main(String[] args) {

		SpringApplication.run(AwesomeNotesApiApplication.class, args);
	}

}
