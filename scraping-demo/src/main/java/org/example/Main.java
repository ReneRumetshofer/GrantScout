package org.example;

import org.flywaydb.core.Flyway;

public class Main {
    public static void main(String[] args) {
        Flyway flyway = Flyway.configure()
                .dataSource("jdbc:postgresql://localhost:5432/grantscout-scraping", "gs", "Password12345678")
                .load();
        flyway.migrate();

        PageScraping.scrapePdfs("https://www.ffg.at/Breitband2030/GigaApp/2AS");
    }
}