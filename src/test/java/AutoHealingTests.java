import io.github.bonigarcia.wdm.WebDriverManager;
import localpage.ClientInfo;
import localpage.LocalPage;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;

public class AutoHealingTests {
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




//    private final int WAIT_FOR_ELEMENT_TIMEOUT = 30;
    private WebDriver driver;
    private WebDriverWait webDriverWait;

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
        ltOptions.put("build", "Auto-healing Demo");
        ltOptions.put("name",this.getClass().getName());
        ltOptions.put("platformName", "Windows 10");
        ltOptions.put("autoHeal", true);
        ltOptions.put("selenium_version", "4.0.0");
        capabilities.setCapability("LT:Options", ltOptions);

        driver = new RemoteWebDriver(new URL("https://" + username + ":" + authkey + hub), capabilities);
        driver.manage().window().maximize();
//        webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(WAIT_FOR_ELEMENT_TIMEOUT));
    }

    @Test
    public void assertFormSent_When_ValidInfoInput() {
        var localPage = new LocalPage(driver);
        localPage.navigate();

        var clientInfo = new ClientInfo();
        clientInfo.setFirstName("Anton");
        clientInfo.setLastName("Angelov");
        clientInfo.setUsername("aangelov");
        clientInfo.setEmail("info@berlinspaceflowers.com");
        clientInfo.setAddress1("1 Willi Brandt Avenue Tiergarten");
        clientInfo.setAddress2("Lützowplatz 17");
        clientInfo.setCountry(1);
        clientInfo.setState(1);
        clientInfo.setZip("10115");
        clientInfo.setCardName("Anton Angelov");
        clientInfo.setCardNumber("1234567890123456");
        clientInfo.setCardExpiration("12/23");
        clientInfo.setCardCVV("123");

        localPage.fillInfo(clientInfo);

        localPage.assertions().formSent();
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}