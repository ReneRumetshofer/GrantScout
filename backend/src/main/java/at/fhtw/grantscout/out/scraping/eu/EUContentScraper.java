package at.fhtw.grantscout.out.scraping.eu;

import at.fhtw.grantscout.core.ports.out.ForContentScraping;
import com.microsoft.playwright.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EUContentScraper implements ForContentScraping {

    @Override
    public String scrapeContent(String url) {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
            BrowserContext context = browser.newContext();

            // Open call page
            Page callPage = context.newPage();
            callPage.navigate(url);

            callPage.waitForSelector(".eui-page-content", new Page.WaitForSelectorOptions().setTimeout(10000));

            // Get title/subtitle/content elements
            List<ElementHandle> callTitle = callPage.locator(".eui-common-header__label-text").elementHandles();
            List<ElementHandle> callSubtitle = callPage.locator(".eui-common-header__sub-label-text").elementHandles();
            List<ElementHandle> callContents = callPage.locator(".eui-page-content").elementHandles();

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
