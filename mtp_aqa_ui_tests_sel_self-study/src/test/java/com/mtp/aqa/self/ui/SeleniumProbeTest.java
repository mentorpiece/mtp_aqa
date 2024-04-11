package com.mtp.aqa.self.ui;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

/**
 * A class to contain the first steps in GUI-based test automation.
 *
 * A Selenium-based one.
 */
public class SeleniumProbeTest {

    private WebDriver driver;

    @BeforeClass
    public void setupClass() {
        WebDriverManager.firefoxdriver().setup();
    }

    @BeforeMethod
    public void setup() {
        // Initialize the browser's driver and launch the browser.
        // See also: https://www.selenium.dev/documentation/
        driver = new FirefoxDriver();

    }

    @AfterMethod
    public void teardown() {
        if (null != driver) {
            // Close the driver and browser.
            driver.quit();
        }
    }

    /**
     *
     * <pre>
     *     Scenario:
     *     1.
     * </pre>
     */
    @Test(description = "The very first steps. Just a place for experiments.")
    public void seleniumProbesTest() {
        // Configure the wait rule. Just in case.
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(7));

        // Open the initial page. NOTE: Use real FTB instance address!
        driver.get("http://192.168.40.100:8000"); // TODO(student): Use properties!

        // Do the tasks
        //driver.findElement(By. .... )).click();

        // Examples:
        // Wait for the element to be rendered.
        //wait.until(presenceOfElementLocated(By.cssSelector("....")));
    }
}
