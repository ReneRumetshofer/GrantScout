package org.example;

import org.flywaydb.core.Flyway;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Flyway flyway = Flyway.configure()
                .dataSource("jdbc:postgresql://localhost:5432/grantscout-scraping", "gs", "Password12345678")
                .load();
        flyway.migrate();

//        PageScraping.scrapePdfs("https://www.ffg.at/Breitband2030/GigaApp/2AS");

        List<String> grantCallUrls = FFGCallSearch.search("https://www.ffg.at/foerderungen?custom_search_date[3]=3");
        grantCallUrls.forEach(System.out::println);
    }
}