package page;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;

public class YopmailHomePage extends BasePage {

    private final static String HOMEPAGE_URL = "https://yopmail.com/";
    private final static String GENERATED_EMAIL_ADDRESS_ID = "geny";
    private final static String MAIL_ID = "message";
    private ArrayList<String> tabs;

    @FindBy(id = "accept")
    private WebElement acceptCookies;

    @FindBy(xpath = "//a[@href='email-generator']//div[@class='txtlien']")
    private WebElement chooseRandomEmailGenerator;

    @FindBy(id = GENERATED_EMAIL_ADDRESS_ID)
    private WebElement generatedEmailAddress;

    @FindBy(xpath = "//button[@onclick='egengo();']")
    private WebElement checkInboxButton;

    @FindBy(id = MAIL_ID)
    private WebElement mail;

    @FindBy(xpath = "//h2")
    private WebElement totalCostFromEmail;

    @FindBy(id = "refresh")
    private WebElement refreshMailButton;

    public YopmailHomePage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    public YopmailHomePage openYopmailInNewTab() {
        driver.switchTo().newWindow(WindowType.TAB);
        driver.get(HOMEPAGE_URL);
        clickThis(acceptCookies);
        tabs = new ArrayList<>(driver.getWindowHandles());
        return this;
    }

    public String generateRandomEmailAddress() {
        clickThis(chooseRandomEmailGenerator);
        waitTillElementIsPresent(By.id(GENERATED_EMAIL_ADDRESS_ID));
        return generatedEmailAddress.getText();
    }

    public String receiveEstimatedInfoFromGeneratedEmail() {
        clickThis(checkInboxButton);
        return getMailContent();
    }

    public YopmailHomePage switchToYopmailPage() {
        driver.switchTo().window(tabs.get(1));
        driver.switchTo().defaultContent();
        return this;
    }

    private String getMailContent() {
        waitTillElementIsPresent(By.id(MAIL_ID));
        while (mail.getText().equals("This inbox is empty")) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            clickThis(refreshMailButton);
            waitTillElementIsPresent(By.id(MAIL_ID));
        }
        driver.switchTo().frame("ifmail");
        return totalCostFromEmail.getText();
    }
}
