package org.oshepel.playwright.demo.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.oshepel.playwright.demo.config.TestConfig;

import static com.microsoft.playwright.options.WaitUntilState.DOMCONTENTLOADED;

@Slf4j
public abstract class AbstractBasePage implements PageObject {

    protected final Page playwrightPage = getPlaywrightPage();

    protected final Locator pageHeader = playwrightPage.locator("//a[contains(.,'DataTables')]").first();

    private String mask(String input) {
        return input.replaceAll("(?<=://)[^/:@]+?:[^/:@]+?@", "");
    }

    public abstract AbstractBasePage shouldBeLoaded();

    public abstract void open();

    @Step("Then user should see main header elements")
    public AbstractBasePage shouldHaveMainHeaderElements() {
        PlaywrightAssertions.assertThat(pageHeader).isVisible();
        return this;
    }

    public AbstractBasePage refreshPage() {
        playwrightPage.reload();
        return this;
    }

    protected void openPageWithRelativePath(String relativePath) {
        String pageUrl = String.format("%s://%s/%s", TestConfig.SCHEME, TestConfig.TARGET_PATH, relativePath);
        log.info("Opening {}", mask(pageUrl));
        try {
            playwrightPage.navigate(pageUrl, new Page.NavigateOptions().setTimeout(20000).setWaitUntil(DOMCONTENTLOADED));
        } catch (Exception e) {
            throw new RuntimeException(String.format("playwrightPage.navigate(pageUrl) failed to execute. Exception type [%s]",
                    e.getClass().getName()));
        }
    }

}