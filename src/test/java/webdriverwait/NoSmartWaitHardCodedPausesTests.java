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

public class NoSmartWaitHardCodedPausesTests {
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
        ltOptions.put("build", "Smart Wait Demo - Hard Coded Pauses");
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
        var addToCartFalcon9 = driver.findElement(By.cssSelector("[data-product_id*='28']"));
        addToCartFalcon9.click();
        Thread.sleep(5000);
        var viewCartButton = driver.findElement(By.cssSelector("[class*='added_to_cart wc-forward']"));
        viewCartButton.click();

        var couponCodeTextField = driver.findElement(By.id("coupon_code"));
        couponCodeTextField.clear();
        couponCodeTextField.sendKeys("happybirthday");
        var applyCouponButton = driver.findElement(By.cssSelector("[value*='Apply coupon']"));
        applyCouponButton.click();
        Thread.sleep(5000);
        var messageAlert = driver.findElement(By.cssSelector("[class*='woocommerce-message']"));
        Assertions.assertEquals(messageAlert.getText(), "Coupon code applied successfully.");

        var quantityBox = driver.findElement(By.xpath("//input[@title='Qty']"));
        quantityBox.clear();
        Thread.sleep(500);
        quantityBox.sendKeys("2");

        Thread.sleep(5000);
        var updateCart = driver.findElement(By.cssSelector("[value*='Update cart']"));
        updateCart.click();
        Thread.sleep(5000);
        var totalSpan = driver.findElement(By.xpath("//*[@class='order-total']//span"));
        Assertions.assertEquals("114.00€", totalSpan.getText());

        var proceedToCheckout = driver.findElement(By.cssSelector("[class*='checkout-button button alt wc-forward']"));
        proceedToCheckout.click();

        var billingFirstName = driver.findElement(By.id("billing_first_name"));
        billingFirstName.sendKeys("Anton");
        var billingLastName = driver.findElement(By.id("billing_last_name"));
        billingLastName.sendKeys("Angelov");
        var billingCompany = driver.findElement(By.id("billing_company"));
        billingCompany.sendKeys("Space Flowers");
        var billingCountryWrapper = driver.findElement(By.id("select2-billing_country-container"));
        billingCountryWrapper.click();
        var billingCountryFilter = driver.findElement(By.className("select2-search__field"));
        billingCountryFilter.sendKeys("Germany");
        var germanyOption = driver.findElement(By.xpath("//*[contains(text(),'Germany')]"));
        germanyOption.click();
        var billingAddress1 = driver.findElement(By.id("billing_address_1"));
        billingAddress1.sendKeys("1 Willi Brandt Avenue Tiergarten");
        var billingAddress2 = driver.findElement(By.id("billing_address_2"));
        billingAddress2.sendKeys("Lotzowplatz 17");
        var billingCity = driver.findElement(By.id("billing_city"));
        billingCity.sendKeys("Berlin");
        var billingZip = driver.findElement(By.id("billing_postcode"));
        billingZip.clear();
        billingZip.sendKeys("10115");
        var billingPhone = driver.findElement(By.id("billing_phone"));
        billingPhone.sendKeys("+00498888999281");
        var billingEmail = driver.findElement(By.id("billing_email"));
        billingEmail.sendKeys("info@berlinspaceflowers.com");
//        purchaseEmail = "info@berlinspaceflowers.com";
        Thread.sleep(5000);
        var placeOrderButton = driver.findElement(By.id("place_order"));
        placeOrderButton.click();

        Thread.sleep(10000);
//        var receivedMessage = driver.findElement(By.xpath("/html/body/div[1]/div/div/div/main/div/header/h1"));
//        Assertions.assertEquals(receivedMessage.getText(), "Order received");
    }

    @Test
    public void completePurchaseSuccessfully_whenExistingClient() throws InterruptedException {
        driver.navigate().to("http://demos.bellatrix.solutions/");

        var addToCartFalcon9 = driver.findElement(By.cssSelector("[data-product_id*='28']"));
        addToCartFalcon9.click();
        Thread.sleep(5000);
        var viewCartButton = driver.findElement(By.cssSelector("[class*='added_to_cart wc-forward']"));
        viewCartButton.click();

        var couponCodeTextField = driver.findElement(By.id("coupon_code"));
        couponCodeTextField.clear();
        couponCodeTextField.sendKeys("happybirthday");
        var applyCouponButton = driver.findElement(By.cssSelector("[value*='Apply coupon']"));
        applyCouponButton.click();
        Thread.sleep(5000);
        var messageAlert = driver.findElement(By.cssSelector("[class*='woocommerce-message']"));
        Assertions.assertEquals(messageAlert.getText(), "Coupon code applied successfully.");

        var quantityBox = driver.findElement(By.xpath("//input[@title='Qty']"));
        quantityBox.clear();
        Thread.sleep(500);
        quantityBox.sendKeys("2");
        Thread.sleep(5000);
        var updateCart = driver.findElement(By.cssSelector("[value*='Update cart']"));
        updateCart.click();
        Thread.sleep(5000);
        var totalSpan = driver.findElement(By.xpath("//*[@class='order-total']//span"));
        Assertions.assertEquals(totalSpan.getText(), "114.00€");

        var proceedToCheckout = driver.findElement(By.cssSelector("[class*='checkout-button button alt wc-forward']"));
        proceedToCheckout.click();

        var loginHereLink = driver.findElement(By.linkText("Click here to login"));
        loginHereLink.click();
        Thread.sleep(5000);
        var userName = driver.findElement(By.id("username"));
        userName.sendKeys("info@berlinspaceflowers.com");
        var password = driver.findElement(By.id("password"));
        password.sendKeys(GetUserPasswordFromDb("@purISQzt%%DYBnLCIhaoG6$"));
        var loginButton = driver.findElement(By.xpath("//button[@name='login']"));
        loginButton.click();
        Thread.sleep(5000);
        var placeOrderButton = driver.findElement(By.id("place_order"));
        placeOrderButton.click();

        Thread.sleep(10000);
        var receivedMessage = driver.findElement(By.xpath("//h1[text() = 'Order received']"));
        Assertions.assertEquals(receivedMessage.getText(), "Order received");

//        var orderNumber = driver.findElement(By.xpath("//*[@id='post-7']//li[1]/strong"));
    }

    private String GetUserPasswordFromDb(String userName)
    {
        return "@purISQzt%%DYBnLCIhaoG6$";
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}