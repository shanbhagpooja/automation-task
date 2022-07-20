package org.soget.com;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class UITest {
    private WebDriver driver;

    @BeforeMethod
    public void initDriver() throws Exception {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();

        //open url
        driver.manage().window().maximize();
        driver.get("https://www.sogeti.com/");

        //accept cookies
        WebElement acceptCookie = driver.findElement(By.className("acceptCookie"));
        acceptCookie.click();
    }
    @Test
    public void testCase_automation_page () throws Exception{

        //Hover on services link
        Actions action = new Actions(driver);
        WebElement services = driver.findElement(By.xpath("//li[3]/div"));
        action.moveToElement(services).build().perform();

        //Click on Automation link
        WebElement automation = driver.findElement(By.linkText("Automation"));
        automation.click();

        //Timeout for 10second
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        //Verify Automation title and Text
        String title = driver.getTitle();
        Assert.assertEquals(title,"Automation");
        WebElement pageHeading = driver.findElement(By.className("page-heading"));
        Assert.assertEquals(pageHeading.getText(),"Automation");

        //Verify Services and Automation is selected
        action = new Actions(driver);
        services = driver.findElement(By.cssSelector("nav#main-menu  .expanded.has-children.level2"));
        action.moveToElement(services).click().build().perform();
        Assert.assertTrue(services.getAttribute("class").contains("selected"));
        automation = driver.findElement(By.cssSelector("nav#main-menu > .level0 .current.expanded"));
        Assert.assertTrue(automation.getAttribute("class").contains("selected"));

    }

    @Test
    public void testCase_contact_us_page () throws Exception{

        //Hover on services link
        Actions action = new Actions(driver);
        WebElement services = driver.findElement(By.xpath("//li[3]/div"));
        action.moveToElement(services).build().perform();

        //Click on Automation link
        WebElement automation = driver.findElement(By.linkText("Automation"));
        automation.click();

        //Timeout for 10second
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        //Verify automation and contact
        String title = driver.getTitle();
        Assert.assertEquals(title,"Automation");
        WebElement contactUsForm = driver.findElement(By.className("Form__Title"));
        Assert.assertEquals(contactUsForm.getText(),"Contact us:");

        //Generate random number
        int randomNumber = (int) (Math.random()*100+1);

        //Fill the field information
        WebElement firstName = driver.findElement(By.id("4ff2ed4d-4861-4914-86eb-87dfa65876d8"));
        firstName.sendKeys("Test FirstName"+randomNumber);

        WebElement lastName = driver.findElement(By.id("11ce8b49-5298-491a-aebe-d0900d6f49a7"));
        lastName.sendKeys("Test LastName"+randomNumber);

        WebElement email = driver.findElement(By.id("056d8435-4d06-44f3-896a-d7b0bf4d37b2"));
        email.sendKeys("TestEmail"+randomNumber+"@email.com");

        WebElement company = driver.findElement(By.id("703dedb1-a413-4e71-9785-586d609def60"));
        company.sendKeys("Test Company"+randomNumber);

        Select country = new Select(driver.findElement(By.id("e74d82fb-949d-40e5-8fd2-4a876319c45a")));
        country.selectByIndex(2);

        WebElement message = driver.findElement(By.id("88459d00-b812-459a-99e4-5dc6eff2aa19"));
        message.sendKeys("Test message");

        // I agree Checkbox
        WebElement checkBox = driver.findElement(By.xpath("//div[@id='863a18ee-d748-4591-bb64-ef6eae65910e']/fieldset/span/label"));
        checkBox.click();

        //Timeout 10 sec
        Thread.sleep(10000);

        //check captcha
        WebElement iframeSwitch = driver.findElement(By.xpath("//*[@id=\"c0706cb4-c58d-4c20-835a-a327a013a48f\"]/div/div/div/iframe"));
        driver.switchTo().frame(iframeSwitch);
        driver.findElement(By.cssSelector("#recaptcha-anchor")).click();
        driver.switchTo().defaultContent();

        //Timeout 10 sec
        Thread.sleep(10000);

        //Submit form
        WebElement submitButton = driver.findElement(By.id("b35711ee-b569-48b4-8ec4-6476dbf61ef8"));
        submitButton.click();

        //Timeout 10 sec
        Thread.sleep(10000);

        //Success message
        WebElement successMessage = driver.findElement(By.cssSelector("#\\39 9a12a58-3899-4fe1-a5c7-b9065fe635b0 > div.Form__Status > div"));
        Assert.assertEquals(successMessage.getText(),"Thank you for contacting us.");
    }

    @Test
    public void testCase_country_list () throws Exception{

        //Click the dropdown link menu
        WebElement dropdown = driver.findElement(By.cssSelector(".navbar-global > .sprite-global-arrowdown.sprite-header"));
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        dropdown.click();

        // Get all country list
        List<WebElement> links = driver.findElements(By.xpath("//*[@id=\"country-list-id\"]/ul/li"));

        // verify if link present and click on link
        for (WebElement link : links
        ) {
            WebElement url = link.findElement(By.tagName("a"));
            Assert.assertNotNull(url.getAttribute("href"));
            url.click();
        }
    }

    @AfterMethod
    public void quitDriver() throws Exception {
        driver.quit();
    }
}

