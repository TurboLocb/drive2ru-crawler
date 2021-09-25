import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class MainForFun {
    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "/home/turbolocb/Soft/chrome-driver/chromedriver");

        WebDriver driver = WebDriverFabric.createDriver(Driver.Chrome);

        driver.get("https://alphabetonline.ru/");

        List<WebElement> list = driver.findElements(By.cssSelector(".table tr td:nth-child(2)"));

        StringBuilder sb = new StringBuilder();

        list.forEach(e -> {
            sb.append(e.getText());
        });

        System.out.println(sb.toString());

        driver.quit();
    }
}
