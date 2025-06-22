package at.fhtw.grantscout.out.search.eu;

import at.fhtw.grantscout.core.ports.out.ForCallSearch;
import com.microsoft.playwright.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class EUCallSearch implements ForCallSearch {

    private final Logger logger = LoggerFactory.getLogger(EUCallSearch.class);

    @Override
    public List<String> search(String baseUrl) {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
            BrowserContext context = browser.newContext();

            Page page = context.newPage();
            page.navigate(baseUrl);
            waitForContent(page);

            // Scrape result count
            Locator resultCountLocator = page.locator("div.row.eui-u-mb-s strong");
            String resultCountText = resultCountLocator.innerText();
            int resultCount = Integer.parseInt(resultCountText);

            // Go though pages (10 per page) and get all call links
            int pageCount = resultCount / 10;
            if (resultCount % 10 != 0) {
                pageCount++;
            }
            Set<String> callUrls = new HashSet<>();
            for (int i = 0; i < pageCount; i++) {
                if(i == 2) break;

                logger.debug("EU Search - page {}", i + 1);
                String pageUrl = baseUrl + "&pageNumber=" + i + 1;
                page.navigate(pageUrl);
                waitForContent(page);

                // Get call links
                page.locator(".eui-card-header__title-container-title a.eui-u-text-link")
                        .elementHandles()
                        .forEach(call -> {
                            String relativeUrl = call.getAttribute("href");
                            if (relativeUrl != null && !relativeUrl.isEmpty()) {
                                String fullCallUrl = "https://ec.europa.eu" + relativeUrl;
                                if (fullCallUrl.contains("?")) {
                                    fullCallUrl = fullCallUrl.substring(0, fullCallUrl.indexOf("?"));
                                }
                                callUrls.add(fullCallUrl);
                            }
                        });
            }

            page.close();
            browser.close();
            return new ArrayList<>(callUrls);
        }
    }

    private void waitForContent(Page page) {
        page.waitForSelector(".eui-card-header__title-container-title a.eui-u-text-link");
    }

}
