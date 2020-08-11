package nl.maastrichtuniversity.ids.eureka.etl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class EurekaEtlApplication {

	public static void main(String[] args) {
		SpringApplication.run(EurekaEtlApplication.class, args);

	}

}
