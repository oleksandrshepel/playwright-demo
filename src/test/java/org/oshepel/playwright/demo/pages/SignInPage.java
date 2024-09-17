package org.oshepel.playwright.demo.pages;

import com.microsoft.playwright.Locator;
import io.qameta.allure.Step;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class SignInPage extends AbstractBasePage {

    private static final String PAGE_HEADER = "Sign-in or Register";
    private static final String USERNAME_LABEL = "Email or Username:";
    private static final String PASSWORD_LABEL = "Password:";

    private final Locator header = playwrightPage.locator(".DTE_Header_Content");
    private final Locator usernameInput = playwrightPage.locator("#DTE_Field_signin-username");
    private final Locator usernameLabel = playwrightPage.locator("//label[@for='DTE_Field_signin-username']");
    private final Locator passwordInput = playwrightPage.locator("#DTE_Field_signin-password");
    private final Locator passwordLabel = playwrightPage.locator("//label[@for='DTE_Field_signin-password']");
    private final Locator submitButton = playwrightPage.locator("//button[text()='Sign-in / Register']");

    @Override
    public void open() {
        //do nothing
    }

    @Override
    @Step("The sign in page header should be displayed")
    public SignInPage shouldBeLoaded() {
        assertThat(header).hasText(PAGE_HEADER);
        return this;
    }

    @Step("Entering the user name: {username}")
    public SignInPage fillInUsername(String username) {
        assertThat(usernameLabel).hasText(USERNAME_LABEL);
        usernameInput.fill(username);
        assertThat(usernameInput).hasValue(username);
        return this;
    }

    @Step("Entering the password: {password}")
    public SignInPage fillInPassword(String password) {
        assertThat(passwordLabel).hasText(PASSWORD_LABEL);
        passwordInput.fill(password);
        assertThat(passwordInput).hasValue(password);
        return this;
    }

    @Step("Clicking on 'Sign-in / Register' button")
    public void submit() {
        submitButton.click();
    }



}
