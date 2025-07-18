import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.*;
import pages.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeOptions;
import static org.testng.Assert.assertTrue;

public class SauceDemoTest {

    WebDriver driver;
    LoginPage loginPage;
    InventoryPage inventoryPage;
    CartPage cartPage;
    CheckoutPage checkoutPage;
    OverviewPage overviewPage;

    @BeforeClass
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications");
        options.addArguments("--start-maximized");
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});

        options = new ChromeOptions();
        options.setExperimentalOption("prefs", new java.util.HashMap<String, Object>() {{
            put("profile.password_manager_leak_detection", false);
            put("credentials_enable_service", false);
        }});

        driver = new ChromeDriver(options);
        driver.get("https://www.saucedemo.com");

        // Initialize page classes
        loginPage = new LoginPage(driver);
        inventoryPage = new InventoryPage(driver);
        cartPage = new CartPage(driver);
        checkoutPage = new CheckoutPage(driver);
        overviewPage = new OverviewPage(driver);
    }

    @Test
    public void completeCheckoutFlow() {
        loginPage.login("standard_user", "secret_sauce");
        inventoryPage.addToCart();
        cartPage.goToCart();

        checkoutPage.startCheckout();
        checkoutPage.fillInformation("Khwezi", "Test", "1234");

        overviewPage.finishCheckout();
        assertTrue(driver.getPageSource().contains("Thank you for your order"));
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }
}
