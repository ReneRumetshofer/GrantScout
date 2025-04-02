package org.example.scraping;

import org.example.scraping.scraping.ffg.FFGCallSearch;
import org.flywaydb.core.Flyway;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class ScrapingApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScrapingApplication.class, args);
    }

}
