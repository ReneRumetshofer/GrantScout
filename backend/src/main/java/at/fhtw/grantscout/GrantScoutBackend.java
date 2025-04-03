package at.fhtw.grantscout;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "at.fhtw.grantscout")
public class GrantScoutBackend {

    public static void main(String[] args) {
        SpringApplication.run(GrantScoutBackend.class, args);
    }

}
