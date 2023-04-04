package dev.thrako.receptaria;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ReceptariaApplication {

    public static void main (String[] args) {

        SpringApplication.run(ReceptariaApplication.class, args);
    }

}
