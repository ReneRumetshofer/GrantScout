package org.example.scraping.scraping;

import org.example.parsing.Parser;
import org.example.scraping.persistence.repositories.CallRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ScrapingJob {

    private final CallRepository callRepository;

    public ScrapingJob(CallRepository callRepository) {
        this.callRepository = callRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initScraping() throws IOException {
//        List<String> grantCallUrls = FFGCallSearch.search("https://www.ffg.at/foerderungen?custom_search_date[3]=3");
//        grantCallUrls.forEach(System.out::println);
//        List<Path> scrapedPdfs = FFGPdfScraping.scrapePdfs(grantCallUrls.get(0));
        //List<Path> scrapedPdfs = FFGPdfScraping.scrapePdfs("https://www.ffg.at/Breitband2030/GigaApp/2AS");

        new Parser().parse("""
                BBA2030: GigaApp fördert Leuchtturmprojekte mit überregionaler Sichtbarkeit und Vorbildfunktion für unterschiedliche vertikale Business-Segmente wie z.B. Energie, Industrie, Mobilität, Tourismus oder Land- und Forstwirtschaft. Die Ausschreibung ist offen und nicht auf bestimmte Business-Segmente eingegrenzt.
                Es stehen dabei Problemstellungen im Mittelpunkt, die den Digitalisierungsprozess vorantreiben und aktuelle Herausforderungen wie z.B. Resilienz oder die Steigerung der Energieeffizienz aus den unterschiedlichen Business-Segmenten adressieren.
                Gefördert wird die Entwicklung innovativer digitaler Anwendungen (z.B. Hardware und/oder Software) und Diensten auf Basis der gigabitfähigen Infrastruktur. Das heißt, dass die neuen Anwendungen und Dienste eine qualitativ hochwertige Internetanbindung über LWL-Infrastruktur und/oder 5G benötigen.
                
                Wer wird gefördert?
                Im Bereich kooperativer F&E-Projekte sind Unternehmen, Forschungseinrichtungen und sonstige Organisationen einreichberechtigt. Die Kriterien der Zusammensetzung eines Konsortiums sind im Instrumentenleitfaden beschrieben. Die Konsortialführung erfolgt jedenfalls durch ein Unternehmen.
                
                Wie hoch ist die Förderung?
                Die Förderungssumme pro Projekt liegt zwischen ca. 100.000 € und 2 Mio. €. Die Förderungsquote richtet sich nach Unternehmensart und Größe der Konsortialpartner und der Forschungskategorie.
                
                Was sind die Einreichkriterien?
                Die ausschreibungsspezifischen Einreichkriterien sind im Ausschreibungsleitfaden zusammengefasst. Weitere geltenden Regelungen sind im entsprechenden Instrumentenleitfaden ersichtlich.
                
                Wie erfolgt die Einreichung?
                Die Einreichung erfolgt über das eCall-System der FFG im Rahmen eines online-Antrags, in dem der Projektinhalt und die –kosten dargestellt werden.\s
                
                Die Einreichung ist laufend bis zum letzten Einreichstichtag möglich. Für den 3. Einreichstichtag stehen ca. 13,5 Mio. Euro Förderungsbudget zur Verfügung.
                
                Einreichstichtag 29.04.2024 - geschlossen
                Einreichstichtag 07.10.2024 - geschlossen
                Einreichstichtag 28.04.2025 - 12:00 Uhr
                Einreichstichtag 31.10.2025 - 12:00 Uhr - Sind die Budgetmittel nach dem 3. Einreichstichtag aufgebraucht, entfällt der 4. Einreichstichtag
                Wann gibt es eine Entscheidung?
                Die Förderentscheidung wird voraussichtlich im Quartal nach dem jeweiligen Einreichstichtag bekanntgegeben.
                """);

        /*scrapedPdfs.forEach(System.out::println);
        List<Path> extractDirPaths = FFGPdfParsing.parsePdf(scrapedPdfs);
        extractDirPaths.forEach(System.out::println);

        for (Path extractDirPath : extractDirPaths) {
            AIParsing.initParsing(extractDirPath);
        }*/

    }

}
