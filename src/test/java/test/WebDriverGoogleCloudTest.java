package test;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.*;
import page.CloudCalculatorPage;
import page.CloudHomePage;
import page.YopmailHomePage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebDriverGoogleCloudTest {
    private WebDriver driver;
    private static final String TERMS = "Google Cloud Pricing Calculator";

    @BeforeClass(alwaysRun = true)
    public void browserSetUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*",
                "--disable-blink-features=AutomationControlled");
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
    }

    @Test(description = "Hardcore")
    public void isTotalPriceMatchingManualTestingPrice() {
        CloudCalculatorPage cloudCalculatorPage = new CloudHomePage(driver)
                .openPage()
                .searchForTerms(TERMS)
                .clickMatchingResult()
                .chooseSection()
                .fillForms()
                .addGpu();

        String priceMessageFromCalculatorPage = cloudCalculatorPage.calculate();

        YopmailHomePage yopmailHomePage = new YopmailHomePage(driver)
                .openYopmailInNewTab();

        String emailAddress = yopmailHomePage.generateRandomEmailAddress();

        cloudCalculatorPage.switchToCalculatorPage()
                .sendCalculatedInfoToEmail(emailAddress);

        yopmailHomePage.switchToYopmailPage();

        String priceMessageFromEmail = yopmailHomePage.receiveEstimatedInfoFromGeneratedEmail();

        Assert.assertEquals(getPrice(priceMessageFromCalculatorPage), getPrice(priceMessageFromEmail));
    }

    private String getPrice (String message) {
        String price = "";
        Pattern pattern = Pattern.compile("\\bUSD\\s\\d+\\p{Punct}\\d+\\p{Punct}\\d+\\b");
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            price = matcher.group(0);
        }
        return price;
    }

    @AfterClass(alwaysRun = true)
    public void browserTearDown() {
        driver.quit();
        driver = null;
    }
}
