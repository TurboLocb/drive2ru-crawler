import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.io.IOException;

@DisplayName("Тест создания скриншотов")
public class ScreenshotTest extends BaseTest {

    private final Logger logger = LogManager.getLogger("ScreenshotLogger");

    @Test
    @DisplayName("должен делать скриншот")
    void takeScreenshot() {
        driver.get("https://yandex.ru");

        //wait element
        //driver.manage().timeouts().implicitlyWait(4, TimeUnit.SECONDS);
        WebElement element = driver.findElement(By.cssSelector("div.rows__row.rows__row_main"));

        if (element.isDisplayed()) {
            logger.info("element found in point {}", element.getRect().getPoint().toString());
        }

        //screenshot
        String base64 = element.getScreenshotAs(OutputType.BASE64);
        savePng(base64);
    }



    private void savePng(String input) {
        File file = OutputType.FILE.convertFromBase64Png(input);
        String fileName = "target/" + System.currentTimeMillis() + ".png";
        try {
            FileUtils.copyFile(file, new File(fileName));
            logger.info("image saved {}", fileName);
        } catch (IOException ioException) {
            //ioException.printStackTrace();
            logger.error("save png error {}", ioException.getMessage());
        }
    }


}
