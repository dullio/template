package br.com.va4e.gidac;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages="br.com.va4e.gidac")
@EnableJpaRepositories(basePackages = "br.com.va4e.gidac.repository")
public class GidacApplication {

	public static void main(String[] args) {
		SpringApplication.run(GidacApplication.class, args);
	}
}
