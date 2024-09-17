package org.oshepel.playwright.demo.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.oshepel.playwright.demo.pages.LandingPage;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class LoginTest extends BaseUiTest {

    private LandingPage landingPage;

    @Override
    @BeforeMethod
    public void initAndOpenPage() {
        landingPage = new LandingPage();
        landingPage.open();
        landingPage.shouldBeLoaded();
    }

    @DataProvider(name = "sign in")
    public Object[][] usernameData() {
        return new Object[][]{
                {"marrakesh", "p@ssw0rd"}, {"limp_slim@ukr.net", "p@ssw0rd"}
        };
    }

    @Test(description = "Sign in", dataProvider = "sign in")
    @Description("""
            - Open the landing page,
            - Click on the 'Login / Register' link,
            - The Sign-in or Register dialog page should be opened,
            - Type into the 'Email or Username:' input a valid username/email,
            - Type into the 'Password:' input a valid password,
            - Click on the 'Sign-in / Register' button,
            - The 'Login / Register' link should be replaced with the 'Your account' and 'Logout' links,
            """)
    @Severity(SeverityLevel.CRITICAL)
    void testSignInWithValidCredentials(String username, String password) {
        //Arrange
        //Act
        landingPage
                .clickOnButton(LandingPage.ActionButton.SIGNIN)
                .shouldBeLoaded()
                .fillInUsername(username)
                .fillInPassword(password)
                .submit();
        //Assert
        landingPage
                .shouldBeLoaded()
                .shouldNotHave(LandingPage.ActionButton.SIGNIN)
                .shouldHave(LandingPage.ActionButton.YOUR_ACCOUNT)
                .shouldHave(LandingPage.ActionButton.LOGOUT);
    }
}
