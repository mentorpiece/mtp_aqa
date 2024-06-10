package com.mtp.aqa.self.ui;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Selenide.open;

/**
 * A class to contain the first steps in GUI-based test automation.
 *
 * A SELENIDE-based one.
 */
public class SelenideProbeTest {

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
    public void selenideProbesTest() {
        // https://selenide.org/quick-start.html
        open("http://192.168.40.100:8000");
/*        $(By.xpath("...")).setValue("admin");
        // ...
        $("#submit").click();
        $(".loading_progress").should(disappear);
        $("#username").shouldHave(text("John"));*/
    }
}
