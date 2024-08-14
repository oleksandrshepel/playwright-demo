package org.oshepel.playwright.demo.config;

import com.microsoft.playwright.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Optional;

import java.nio.file.Path;

import static org.oshepel.playwright.demo.config.TestConfig.DOWNLOAD_PATH;

@Slf4j
@Data
public class PlaywrightConfig {

    private final Playwright playwright;
    private final Browser browser;
    private final Page page;
    private final BrowserContext browserContext;
    private final String browserName;
    private final Boolean traceEnabled;
    private final Boolean headless;

    public PlaywrightConfig(@Optional("chrome") String browserName, @Optional("false") Boolean headless,
                            @Optional("false") Boolean traceEnabled) {
        this.browserName = browserName.toLowerCase();
        this.headless = headless;
        this.playwright = createPlaywright();
        this.browser = createBrowser();
        this.browserContext = createBrowserContext(this.browser);
        this.traceEnabled = traceEnabled;
        if (this.traceEnabled) {
            startTraceRecording();
        }
        this.page = this.browserContext.newPage();
        log.info("Playwright configs have been set");
    }

    public Browser createBrowser() {
        Path downloadPath = Path.of(DOWNLOAD_PATH);
        switch (this.browserName) {
            case "chrome":
                return playwright.chromium().launch(new BrowserType.LaunchOptions()
                        .setHeadless(this.headless)
                        .setDownloadsPath(downloadPath)
                        .setDevtools(false)
                        .setChannel("chrome"));
            case "firefox":
                return playwright.firefox().launch(new BrowserType.LaunchOptions()
                        .setHeadless(this.headless)
                        .setDownloadsPath(downloadPath)
                        .setDevtools(false)
                        .setChannel("firefox"));
            default:
                throw new IllegalArgumentException(String.format("Browser %s not supported", this.browserName));
        }
    }

    public Playwright createPlaywright() {
        return Playwright.create();
    }

    private void startTraceRecording() {
        this.browserContext.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(false));
    }

    public BrowserContext createBrowserContext(Browser browser) {
        Browser.NewContextOptions options = new Browser.NewContextOptions()
                .setViewportSize(1920, 1080)
                .setIgnoreHTTPSErrors(true);
        return browser.newContext(options);
    }
}