package org.example;

import com.microsoft.playwright.*;

public class Main {
    public static void main(String[] args) {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(true));
            BrowserContext context = browser.newContext();
            Page page = context.newPage();

            String url = "https://orf.at";
            page.navigate(url);

            // Extract all headlines from ORF (top row with images)
            page.locator("div.oon-grid-texts-container div.oon-grid-texts-headline h1").all().forEach(link -> {
                String linkText = link.textContent().trim();
                linkText = linkText.replace("\n\s\s\s\s\s\s\n\s\s\s\s\s\s", " ");
                System.out.println("Headline: " + linkText.trim());
            });

            browser.close();
        }
    }
}