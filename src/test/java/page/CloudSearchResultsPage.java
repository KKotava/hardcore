package page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class CloudSearchResultsPage extends BasePage {
    private final String terms;

    public CloudSearchResultsPage(WebDriver driver, String terms) {
        super(driver);
        this.terms = terms;
        PageFactory.initElements(driver, this);
    }

    public CloudCalculatorPage clickMatchingResult() {
        String defaultLocatorPattern = "//a/b[contains(text(), '%s')]";
        String locatorForSearch = String.format(defaultLocatorPattern, terms);
        waitTillElementIsPresent(By.xpath(locatorForSearch));
        WebElement matchingResult = driver.findElement(By.xpath(locatorForSearch));
        matchingResult.click();
        return new CloudCalculatorPage(driver);
    }

}
