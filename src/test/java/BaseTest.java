import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public abstract class BaseTest {
    static WebDriver driver;

    @BeforeAll
    public static void setUp(){
        System.setProperty("webdriver.chrome.driver", "/home/turbolocb/Soft/chrome-driver/chromedriver");
        driver = new ChromeDriver();
    }

    @AfterAll
    static void close(){
        if (driver != null){
            driver.quit();
        }
    }
}
