package org.oshepel.playwright.demo.tests;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import com.microsoft.playwright.Tracing;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.oshepel.playwright.demo.config.PlaywrightConfig;
import org.oshepel.playwright.demo.pages.AbstractBasePage;
import org.testng.IHookCallBack;
import org.testng.IHookable;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Slf4j
public abstract class BaseUiTest implements IHookable {

	private static final boolean TRACE_ENABLED = true;

	protected static ThreadLocal<PlaywrightConfig> PLAYWRIGHT_THREAD_LOCAL = new ThreadLocal<>();

	protected static void setPlaywrightConfig(PlaywrightConfig playwrightConfig) {
		PLAYWRIGHT_THREAD_LOCAL.set(playwrightConfig);
	}

	public static PlaywrightConfig getPlaywrightConfig() {
		return PLAYWRIGHT_THREAD_LOCAL.get();
	}

	protected static void closePlaywright() {
		if (Objects.nonNull(getPlaywrightConfig())) {
			PLAYWRIGHT_THREAD_LOCAL.get().getPlaywright().close();
			PLAYWRIGHT_THREAD_LOCAL.remove();
		}
	}

	@Override
	public void run(IHookCallBack callBack, ITestResult testResult) {
		try {
			callBack.runTestMethod(testResult);
			if (testResult.getThrowable() == null) {
				log.info("Test passed");
				if (TRACE_ENABLED) {
					getPlaywrightConfig().getBrowserContext().tracing().stop();
				}
			} else {
				failTest(testResult);
			}
		}
		catch (PlaywrightException e) {
			failTest(testResult);
		}
		catch (Exception e) {
			log.error("Test configuration issue");
			throw new RuntimeException(e);
		}
	}

	@BeforeSuite
	public void setupPreconditions() {
		log.debug("Setting preconditions");
		PlaywrightAssertions.setDefaultAssertionTimeout(10000);
	}

	@BeforeMethod
	@Parameters({"browserName", "headless"})
	public void setupBrowserConfigs(@Optional("chrome") String browserName, @Optional("false") boolean headless) {
		setPlaywrightConfig(new PlaywrightConfig(browserName, headless, TRACE_ENABLED));
	}

	@AfterMethod(alwaysRun = true)
	public void closeBrowserContext(ITestResult result) {
		log.info("Closing the page");
		closePlaywright();
	}

	public abstract void initAndOpenPage();

	protected void openPage(AbstractBasePage page) {
		try {
			page.open();
		}
		catch (PlaywrightException e) {
			page.refreshPage();
			page.open();
		}
	}

    @SneakyThrows
	private void addTraceToAllureReport(String methodName) {
		if (TRACE_ENABLED) {
			log.warn("Stop tracing for the test {}", methodName);
			var tracePath = getTraceFilePath(methodName);
			getPlaywrightConfig().getBrowserContext().tracing().stop(new Tracing.StopOptions().setPath(tracePath));
			Allure.addAttachment("trace", Files.newInputStream(tracePath));
		} else {
			Allure.addAttachment("source.html", "text/html", getPlaywrightConfig().getPage().content());
		}
	}

	private Path getTraceFilePath(String methodName) {
		return Paths.get("target", "traces", methodName.concat(".zip"));
	}

	@Attachment(value = "Failure in method {0}", type = "image/png")
	private byte[] addScreenshotToAllureReport(String methodName) {
		byte[] screenshot = getPlaywrightConfig().getPage().screenshot(new Page.ScreenshotOptions().setFullPage(true));
		return screenshot;
	}

	private void failTest(ITestResult testResult) {
		log.error("Test failed");
		var methodName = testResult.getMethod().getMethodName();
		addScreenshotToAllureReport(testResult.getMethod().getMethodName());
		addTraceToAllureReport(methodName);
	}

}