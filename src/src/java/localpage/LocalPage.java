/*
 * Copyright 2021 Automate The Planet Ltd.
 * Author: Teodor Nikolov
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package localpage;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;
import java.time.Duration;

public class LocalPage {
    private final WebDriver driver;
//    private final URL url = getClass().getClassLoader().getResource("checkout/index.html");

    public LocalPage(WebDriver driver) {
        this.driver = driver;
    }

    public WebElement firstName() {
        return driver.findElement(By.id("firstName"));
    }

    public WebElement lastName() {
        return driver.findElement(By.id("lastName"));
    }

    public WebElement username() {
        return driver.findElement(By.id("username"));
    }

    public WebElement email() {
        return driver.findElement(By.id("email"));
    }

    public WebElement address1() {
        return driver.findElement(By.id("address"));
    }

    public WebElement address2() {
        return driver.findElement(By.id("address2"));
    }

    public Select country() {
        return new Select(driver.findElement(By.id("country")));
    }

    public Select state() {
        return new Select(driver.findElement(By.id("state")));
    }

    public WebElement zip() {
        return driver.findElement(By.id("zip"));
    }

    public WebElement cardName() {
        return driver.findElement(By.id("cc-name"));
    }

    public WebElement cardNumber() {
        return driver.findElement(By.id("cc-number"));
    }

    public WebElement cardExpiration() {
        return driver.findElement(By.id("cc-expiration"));
    }

    public WebElement cardCVV() {
        return driver.findElement(By.id("cc-cvv"));
    }

    public WebElement submitButton() {
        return driver.findElement(By.xpath("//button[text()='Continue to checkout']"));
    }

    // very complex XPATH element
    public WebElement promoCode() {
        return driver.findElement(By.xpath("//input[@id='email']/ancestor::form/parent::div/preceding-sibling::div/form/div/input"));
    }

    public void navigate() {
        driver.navigate().to("https://076f-5-53-134-19.ngrok-free.app/checkout/");
        driver.manage().addCookie(new Cookie("abuse_interstitial", "076f-5-53-134-19.ngrok-free.app"));
        driver.navigate().refresh();
    }

    public void fillInfo(ClientInfo clientInfo) {
        firstName().sendKeys(clientInfo.getFirstName());
        lastName().sendKeys(clientInfo.getLastName());
        username().sendKeys(clientInfo.getUsername());
        email().sendKeys(clientInfo.getEmail());
        address1().sendKeys(clientInfo.getAddress1());
        address2().sendKeys(clientInfo.getAddress2());
        country().selectByIndex(clientInfo.getCountry());
        state().selectByIndex(clientInfo.getState());
        zip().sendKeys(clientInfo.getZip());
        cardName().sendKeys(clientInfo.getCardName());
        cardNumber().sendKeys(clientInfo.getCardNumber());
        cardExpiration().sendKeys(clientInfo.getCardExpiration());
        cardCVV().sendKeys(clientInfo.getCardCVV());

        //promoCode().sendKeys("TESTCODE");

//        var wait = new WebDriverWait(driver, Duration.ofSeconds(30));
//        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Continue to checkout']")));
//        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0,500);");
        submitButton().click();
    }

    public void formSent() {
        Assertions.assertTrue(driver.getCurrentUrl().contains("paymentMethod=on"), "Form not sent");
    }
}
