package at.fhtw.grantscout.out.scraping.ffg;

import com.microsoft.playwright.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FFGPdfScraping {

    public static List<Path> scrapePdfs(String url) {
        List<Path> savedFiles = new ArrayList<>();

        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(true));
            BrowserContext context = browser.newContext();
            Page page = context.newPage();
            page.navigate(url);

            // Extract all PDF links
            List<Locator> pdfLinks = new ArrayList<>();
            page.locator("article a").all().forEach(link -> {
                String linkDestination = link.getAttribute("href");
                if (linkDestination == null) {
                    return;
                }

                if (linkDestination.startsWith("https://fdoc.ffg.at/") || linkDestination.endsWith(".pdf")) {
                    pdfLinks.add(link);
                }
            });

            // Make dir download in user.dir if not exists
            Path downloadDir = Paths.get(System.getProperty("user.dir"), "download");
            if (!downloadDir.toFile().exists()) {
                boolean creationSuccess = downloadDir.toFile().mkdir();
                if (!creationSuccess) {
                    throw new IllegalStateException("Could not create download directory");
                }
            }

            for (Locator pdfLink : pdfLinks) {
                Download download = page.waitForDownload(pdfLink::click);
                Path savePath = Paths.get(downloadDir.toString(), download.suggestedFilename());
                download.saveAs(savePath);
                savedFiles.add(savePath);
            }

            browser.close();
        }

        return savedFiles;
    }

}
