package at.fhtw.grantscout.out.search.ffg;

import at.fhtw.grantscout.core.ports.out.ForCallSearch;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FFGCallSearch implements ForCallSearch {

    private Logger logger = LoggerFactory.getLogger(FFGCallSearch.class);

    @Override
    public List<String> search(String url) {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(true));
            BrowserContext context = browser.newContext();
            Page page = context.newPage();
            page.navigate(url);
            page.waitForLoadState(LoadState.NETWORKIDLE);

            // Scrape result count
            Locator resultCountLocator = page.locator("header div").filter(new Locator.FilterOptions().setHasText("Ergebnisse"));
            String rawResultCountText = resultCountLocator.innerText();
            String resultCountText = rawResultCountText.substring(0, rawResultCountText.indexOf("Ergebnisse")).trim();
            int resultCount = Integer.parseInt(resultCountText);

            // Go though pages (10 per page) and get all call links
            int pageCount = resultCount / 10;
            if (resultCount % 10 != 0) {
                pageCount++;
            }
            logger.debug("FFG: {} pages found", pageCount);
            List<String> callLinks = new ArrayList<>();
            for (int i = 0; i < pageCount; i++) {
                if (i == 3) {
                    logger.debug("FFG: Stopping after 3 pages for testing purposes");
                    break;
                }

                logger.debug("FFG Search - page {}", i + 1);
                String pageUrl = url + "&page=" + i;
                page.navigate(pageUrl);
                page.waitForLoadState(LoadState.NETWORKIDLE);

                page.locator("div.list-item article a").all().forEach(link -> {
                    String linkDestination = link.getAttribute("href");
                    if (linkDestination == null) {
                        return;
                    }
                    callLinks.add("https://www.ffg.at" + linkDestination);
                });
            }

            page.close();
            browser.close();

            return callLinks;
        }
    }

}
