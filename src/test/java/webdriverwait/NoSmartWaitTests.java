/*
 * Copyright 2021 Automate The Planet Ltd.
 * Author: Anton Angelov
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package webdriverwait;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;

public class NoSmartWaitTests {
//    private WebDriver driver;
//
//    @BeforeAll
//    public static void setUpClass() {
//        WebDriverManager.chromedriver().setup();
//    }
//
//    @BeforeEach
//    public void setUp() {
//        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--ignore-certificate-errors");
//        driver = new ChromeDriver(options);
//        driver.manage().window().maximize();
//    }




    private final int WAIT_FOR_ELEMENT_TIMEOUT = 30;
    private WebDriver driver;
    private static String purchaseEmail;
    private static String purchaseOrderNumber;

    @BeforeAll
    public static void setUpClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void setUp() throws MalformedURLException {
        String username = System.getenv("LT_USERNAME");
        String authkey = System.getenv("LT_ACCESSKEY");
        String hub = "@hub.lambdatest.com/wd/hub";

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("browserName", "Chrome");
        capabilities.setCapability("browserVersion", "latest");
        HashMap<String, Object> ltOptions = new HashMap<String, Object>();
        ltOptions.put("user", username);
        ltOptions.put("accessKey", authkey);
        ltOptions.put("build", "Smart Wait Demo - TURNED OFF");
        ltOptions.put("name",this.getClass().getName());
        ltOptions.put("platformName", "Windows 10");
//        ltOptions.put("autoHeal", true);
//        ltOptions.put("smartWait", 30);
        ltOptions.put("selenium_version", "4.0.0");
        capabilities.setCapability("LT:Options", ltOptions);

        driver = new RemoteWebDriver(new URL("https://" + username + ":" + authkey + hub), capabilities);
        driver.manage().window().maximize();
    }

    @Test
    public void completePurchaseSuccessfully_whenNewClient() throws InterruptedException {
        driver.navigate().to("http://demos.bellatrix.solutions/");
        var addToCartFalcon9 = waitAndFindElement(By.cssSelector("[data-product_id*='28']"));
        addToCartFalcon9.click();
        var viewCartButton = waitAndFindElement(By.cssSelector("[class*='added_to_cart wc-forward']"));
        viewCartButton.click();

        var couponCodeTextField = waitAndFindElement(By.id("coupon_code"));
        couponCodeTextField.clear();
        couponCodeTextField.sendKeys("happybirthday");
        var applyCouponButton = waitAndFindElement(By.cssSelector("[value*='Apply coupon']"));
        applyCouponButton.click();
        waitForAjax();
        var messageAlert = waitAndFindElement(By.cssSelector("[class*='woocommerce-message']"));
        Assertions.assertEquals(messageAlert.getText(), "Coupon code applied successfully.");

        var quantityBox = waitAndFindElement(By.cssSelector("[class*='input-text qty text']"));
        quantityBox.clear();
        quantityBox.sendKeys("2");

        var updateCart = waitAndFindElement(By.cssSelector("[value*='Update cart']"));
        updateCart.click();
        waitForAjax();
        var totalSpan = waitAndFindElement(By.xpath("//*[@class='order-total']//span"));
        Assertions.assertEquals("114.00€", totalSpan.getText());

        var proceedToCheckout = waitAndFindElement(By.cssSelector("[class*='checkout-button button alt wc-forward']"));
        proceedToCheckout.click();

        var billingFirstName = waitAndFindElement(By.id("billing_first_name"));
        billingFirstName.sendKeys("Anton");
        var billingLastName = waitAndFindElement(By.id("billing_last_name"));
        billingLastName.sendKeys("Angelov");
        var billingCompany = waitAndFindElement(By.id("billing_company"));
        billingCompany.sendKeys("Space Flowers");
        var billingCountryWrapper = waitAndFindElement(By.id("select2-billing_country-container"));
        billingCountryWrapper.click();
        var billingCountryFilter = waitAndFindElement(By.className("select2-search__field"));
        billingCountryFilter.sendKeys("Germany");
        var germanyOption = waitAndFindElement(By.xpath("//*[contains(text(),'Germany')]"));
        germanyOption.click();
        var billingAddress1 = waitAndFindElement(By.id("billing_address_1"));
        billingAddress1.sendKeys("1 Willi Brandt Avenue Tiergarten");
        var billingAddress2 = waitAndFindElement(By.id("billing_address_2"));
        billingAddress2.sendKeys("Lotzowplatz 17");
        var billingCity = waitAndFindElement(By.id("billing_city"));
        billingCity.sendKeys("Berlin");
        var billingZip = waitAndFindElement(By.id("billing_postcode"));
        billingZip.clear();
        billingZip.sendKeys("10115");
        var billingPhone = waitAndFindElement(By.id("billing_phone"));
        billingPhone.sendKeys("+00498888999281");
        var billingEmail = waitAndFindElement(By.id("billing_email"));
        billingEmail.sendKeys("info@berlinspaceflowers.com");
        purchaseEmail = "info@berlinspaceflowers.com";

        // This pause will be removed when we introduce a logic for waiting for AJAX requests.
        waitForAjax();
        var placeOrderButton = waitAndFindElement(By.id("place_order"));
        placeOrderButton.click();

//        waitForAjax();
//        var receivedMessage = waitAndFindElement(By.xpath("/html/body/div[1]/div/div/div/main/div/header/h1"));
//        Assertions.assertEquals(receivedMessage.getText(), "Order received");
    }

    @Test
    public void completePurchaseSuccessfully_whenExistingClient() throws InterruptedException {
        driver.navigate().to("http://demos.bellatrix.solutions/");

        var addToCartFalcon9 = waitAndFindElement(By.cssSelector("[data-product_id*='28']"));
        addToCartFalcon9.click();
        var viewCartButton = waitAndFindElement(By.cssSelector("[class*='added_to_cart wc-forward']"));
        viewCartButton.click();

        var couponCodeTextField = waitAndFindElement(By.id("coupon_code"));
        couponCodeTextField.clear();
        couponCodeTextField.sendKeys("happybirthday");
        var applyCouponButton = waitAndFindElement(By.cssSelector("[value*='Apply coupon']"));
        applyCouponButton.click();
        var messageAlert = waitAndFindElement(By.cssSelector("[class*='woocommerce-message']"));
        waitForAjax();
        Assertions.assertEquals(messageAlert.getText(), "Coupon code applied successfully.");

        var quantityBox = waitAndFindElement(By.cssSelector("[class*='input-text qty text']"));
        quantityBox.clear();
        quantityBox.sendKeys("2");
        var updateCart = waitAndFindElement(By.cssSelector("[value*='Update cart']"));
        updateCart.click();
        waitForAjax();
        var totalSpan = waitAndFindElement(By.xpath("//*[@class='order-total']//span"));
        Assertions.assertEquals(totalSpan.getText(), "114.00€");

        var proceedToCheckout = waitAndFindElement(By.cssSelector("[class*='checkout-button button alt wc-forward']"));
        proceedToCheckout.click();

        var loginHereLink = waitAndFindElement(By.linkText("Click here to login"));
        loginHereLink.click();
        var userName = waitAndFindElement(By.id("username"));
        userName.sendKeys(purchaseEmail);
        var password = waitAndFindElement(By.id("password"));
        password.sendKeys(GetUserPasswordFromDb(purchaseEmail));
        var loginButton = waitAndFindElement(By.xpath("//button[@name='login']"));
        loginButton.click();

        // This pause will be removed when we introduce a logic for waiting for AJAX requests.
        waitForAjax();
        var placeOrderButton = waitAndFindElement(By.id("place_order"));
        placeOrderButton.click();

        var receivedMessage = waitAndFindElement(By.xpath("//h1[text() = 'Order received']"));
        Assertions.assertEquals(receivedMessage.getText(), "Order received");

//        var orderNumber = waitAndFindElement(By.xpath("//*[@id='post-7']//li[1]/strong"));
//        purchaseOrderNumber = orderNumber.getText();
    }

    private String GetUserPasswordFromDb(String userName)
    {
        return "@purISQzt%%DYBnLCIhaoG6$";
    }

    private void waitToBeClickable(By by) {
        var webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(30));
        webDriverWait.until(ExpectedConditions.elementToBeClickable(by));
    }

    private WebElement waitAndFindElement(By by) {
        var webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(30));
        return webDriverWait.until(ExpectedConditions.presenceOfElementLocated(by));
    }

    private List<WebElement> waitAndFindElements(By by) {
        var webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(30));
        return webDriverWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(by));
    }

    public void waitForAjax() {
        var webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(30));
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
        webDriverWait.until(d -> (Boolean) javascriptExecutor.executeScript("return window.jQuery != undefined && jQuery.active == 0"));
    }

    public void waitUntilPageLoadsCompletely() {
        var webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(30));
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
        webDriverWait.until(d -> javascriptExecutor.executeScript("return document.readyState").toString().equals("complete"));
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}