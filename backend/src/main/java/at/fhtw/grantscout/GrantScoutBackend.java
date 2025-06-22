package at.fhtw.grantscout;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EntityScan(basePackages = "at.fhtw.grantscout.out.persistence")
public class GrantScoutBackend {

    public static void main(String[] args) {
        SpringApplication.run(GrantScoutBackend.class, args);
    }

}
