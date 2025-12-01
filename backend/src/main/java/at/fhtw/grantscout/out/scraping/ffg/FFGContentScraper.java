package at.fhtw.grantscout.out.scraping.ffg;

import at.fhtw.grantscout.core.ports.out.ForContentScraping;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FFGContentScraper implements ForContentScraping {

    @Override
    public String scrapeContent(String url) {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(true));
            BrowserContext context = browser.newContext();

            // Open call page
            Page callPage = context.newPage();
            callPage.navigate(url);
            callPage.waitForLoadState(LoadState.NETWORKIDLE);

            // Get title/subtitle/content elements
            List<ElementHandle> callTitle = callPage.locator("article h1").elementHandles();
            List<ElementHandle> callSubtitle = callPage.locator("article h2").elementHandles();
            List<ElementHandle> callContents = callPage.locator("article").elementHandles();

            StringBuilder fileContent = new StringBuilder();
            for (ElementHandle title : callTitle) {
                fileContent.append("Title: ").append(title.innerText().trim()).append(System.lineSeparator());
            }
            for (ElementHandle subtitle : callSubtitle) {
                fileContent.append("Subtitle: ").append(subtitle.innerText().trim()).append(System.lineSeparator());
            }
            for (ElementHandle content : callContents) {
                fileContent.append("Content: ").append(content.innerText().trim()).append(System.lineSeparator());
            }

            callPage.close();
            browser.close();

            return fileContent.toString();
        }
    }
}

