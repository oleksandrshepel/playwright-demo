package org.oshepel.playwright.demo.pages;

import com.microsoft.playwright.Locator;
import io.qameta.allure.Step;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class LandingPage extends AbstractBasePage {

    private static final String PAGE_HEADER = "Add advanced interaction controls to your HTML tables the free & easy way";

    private final Locator header = playwrightPage.locator("//div[@class='fw-hero']/h1");

    @Override
    @Step("Opening the landing page")
    public void open() {
        openPageWithRelativePath(StringUtils.EMPTY);
        shouldBeLoaded();
    }

    @Override
    @Step("The landing page header should be displayed")
    public LandingPage shouldBeLoaded() {
        assertThat(header).isVisible();
        var actualText = header.innerText().replaceAll("\n", " ");
        Assert.assertEquals(actualText, PAGE_HEADER);
        return this;
    }

    @Step("Clicking on the button {actionButton.visibleText}")
    public SignInPage clickOnButton(LandingPage.ActionButton actionButton) {
        var locator = playwrightPage.locator(actionButton.getSelector());
        assertThat(locator).hasText(actionButton.getVisibleText());
        locator.click();
        return new SignInPage();
    }

    @Step("The button {actionButton.visibleText} should not be displayed")
    public LandingPage shouldNotHave(LandingPage.ActionButton actionButton) {
        var locator = playwrightPage.locator(actionButton.getSelector());
        assertThat(locator).not().isVisible();
        return this;
    }

    @Step("The button {actionButton.visibleText} should be displayed")
    public LandingPage shouldHave(LandingPage.ActionButton actionButton) {
        var locator = playwrightPage.locator(actionButton.getSelector());
        assertThat(locator).isVisible();
        return this;
    }

    @Getter
    @RequiredArgsConstructor
    public enum ActionButton {
        LOGOUT("a.logout", "Logout"),
        SIGNIN("a.login", "Login / Register"),
        YOUR_ACCOUNT("//a[@href='//datatables.net/account']", "Your account");

        private final String selector;
        private final String visibleText;
    }

}
