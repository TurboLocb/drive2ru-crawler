import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class WebDriverFabric {
    public static WebDriver createDriver(Driver enumDriver){
        if (enumDriver.equals(Driver.Firefox)){
            return new FirefoxDriver();
        }
        return new ChromeDriver();
    }
}
enum Driver{
    Chrome,
    Firefox
}