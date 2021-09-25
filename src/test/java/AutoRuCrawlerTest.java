import data.objects.Auto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AutoRuCrawlerTest extends BaseTest {

    private final Logger logger = LogManager.getLogger(AutoRuCrawlerTest.class);
    private String currentBrand = "Mercedes-Benz";

    @Test
    @DisplayName("должен доскролить до конца страницы и выводить данные")
    void should_ScrollDown_onCurrentUrl() {
        String url = "https://www.drive2.ru/cars/ac/?sort=selling";

        driver.get(url);

        driver.manage().window().maximize();

        List<WebElement> gridList = new ArrayList<>();

        WebElement footer = driver.findElement(By.tagName("footer"));

        Actions actions = new Actions(driver);

        WebDriverWait waiter = new WebDriverWait(driver, 4);

        driver.manage().timeouts().implicitlyWait(4, TimeUnit.SECONDS);

        //check - would exist anyone element on page
        if (
                !driver.findElements(By.cssSelector("div.c-darkening-hover-container:first-child")).isEmpty()
        ) {
            logger.info("page on url: '{}' isn't empty", url);

            int current = 0;

            while (!driver.findElements(By.cssSelector("button[data-action='catalog.morecars']")).isEmpty()) {
                //scroll down
                actions.moveToElement(footer).perform();

                //update list
                //gridList = driver.findElements(By.cssSelector("div.o-grid.o-grid--3.o-grid--equal"));
                gridList = waiter.until(ExpectedConditions
                        .visibilityOfAllElements(driver.findElements(By.cssSelector("div.c-darkening-hover-container"))));

                //to process element
                if (gridList.size() > current) {
                    for (int i = current; i < gridList.size(); i++) {
                        //logger.info("'current' value: " + current + " " + parseElement(gridList.get(i)).getURL());

                        logger.info("number of current element: {}, simulate parse element: {}",
                                current,
                                gridList.get(current).findElement(By.cssSelector("span.c-car-title")).getText());

                        current++;
                    }
                }
            }
        } else {
            logger.warn("page on url: '{}' is empty", url);
        }

        //assertAll work while exist include functions
        Assertions.assertAll(
                () -> assertTrue(driver.findElement(By.tagName("footer")).isDisplayed()),
                () -> assertTrue(driver.findElement(By.tagName("footer")).getRect().getY() > 1000)
        );
    }

    @Test
    @DisplayName("должен доскролить до конца страницы и выводить данные")
    @Disabled
    void should_ScrollDown_onCurrentUrl_When_WeUseAutoRuCrawlerParseElementMethod() {
        AutoRuCrawler crawler = new AutoRuCrawler();

    }

    @Test
    @DisplayName("должен открывать вкладку, парсить содержимое и закрывать вкладку")
    void should_CorrectParsePageWithAuto_in_NewTab() {
        //parse that element
        Assertions.assertDoesNotThrow(() -> {
            AutoRuCrawler crawler = new AutoRuCrawler();
            crawler.setUpDriver();
            crawler.setURL("https://www.drive2.ru/cars/mercedes/?sort=selling");
            crawler.connectToUrl();

            //set handle after connection
            //crawler.setCurrentHandle(crawler.driver.getWindowHandle(), 0);
            crawler.setCurrentBrand(currentBrand);
            crawler.parseElement(crawler.getElementByLocator("div.c-darkening-hover-container"));
            crawler.closeDriver();
        });
    }

    @Test
    @DisplayName("должен собрать информацию об автомобиле на странице")
    void should_getInformationAboutAuto() {
        driver.get("https://www.drive2.ru/r/mercedes/w123/16826/");

        //expected auto
        Auto aTest = new Auto();
        aTest.setURL("https://www.drive2.ru/r/mercedes/w123/16826/");
        aTest.setBrand(currentBrand);
        aTest.setModel("W123");
        aTest.setEngineCapacity(6.0f);
        aTest.setYear(1985);
        aTest.setPrice(1_500_000);

        //get auto info
        Auto a = parseElement(driver.findElement(By.tagName("body")));

        //assert that auto which we received is equal test auto
        Assertions.assertEquals(aTest, a);
        logger.debug("Received auto: {}, and test auto: {}", a, aTest);
    }

    @Test
    @DisplayName("должен открывать ссылку в новом окне")
    void should_openUrlInNewTab() {
        String URL = "https://www.drive2.ru/r/mercedes/w123/16826/";

        //open new tab
        ((JavascriptExecutor) driver).executeScript("window.open('about:blank','_blank');");

        //current tab
        String currentHandle = driver.getWindowHandle();

        //open link in before opened tab
        driver.getWindowHandles().forEach(s -> {
            //switch in new tab
            if (!s.equals(currentHandle)) {
                driver.switchTo().window(s);
            }
        });

        //open url in new tab
        driver.get(URL);

        //assert that current tab isn't first tab
        Assertions.assertNotEquals(driver.getWindowHandle(), currentHandle);

        logger.debug("openUrlInNewTab() successfully completed");
    }

    @Test
    @DisplayName("должен открывать ссылки в новой вкладке и переходить обратно на первую")
    void should_OpenEveryUrlInNewTab_andReturnOnFirstTab() {
        List<String> urls = new ArrayList();
        urls.add("https://www.drive2.ru/r/mazda/3/1264539/");
        urls.add("https://www.drive2.ru/r/mercedes/w123/16826/");
        urls.add("https://www.drive2.ru/r/mazda/3/463727551303385196/");

        WebDriverWait waiter = new WebDriverWait(driver, 4);

        driver.manage().window().maximize();

        //set first tab
        driver.get("https://google.com");

        //current tab
        String firstTab = driver.getWindowHandle();

        //open all urls
        urls.forEach(currentUrl -> {
            //open new tab
            ((JavascriptExecutor) driver).executeScript("window.open('about:blank','_blank');");

            //open link in before opened tab
            driver.getWindowHandles().forEach(s -> {
                //switch in new tab
                if (!s.equals(firstTab)) {
                    driver.switchTo().window(s);
                }
            });

            //open url in new tab
            driver.get(currentUrl);
            waiter.until(driver -> driver.findElement(By.tagName("body")));

            //close tab
            driver.close();

            //switch to first
            driver.switchTo().window(firstTab);
        });

        Assertions.assertEquals(1, driver.getWindowHandles().size());
        logger.debug("should_OpenEveryUrlInNewTab_andReturnOnFirstTab() successfully completed");
    }

    @Test
    @DisplayName("должен находить элемент, который содержит стоимость автомобиля в рублях")
    void should_ReturnTagWhichContainsRussianRubles() {
        String URL = "https://www.drive2.ru/r/mercedes/w123/16826/";

        WebDriverWait waiter = new WebDriverWait(driver, 4);

        driver.get(URL);

        //get auto information block
        WebElement priceElement = waiter.until(
                ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector(".c-car-forsale__price"))
                        .findElements(By.cssSelector("*"))
                        .stream()
                        .filter(element -> element.getText().contains("₽"))
                        .findFirst()
                        .get())
        );

        long price = AutoUtils.getAutoPriceByInputSting(priceElement.getText());

        Assertions.assertEquals(price, 1_500_000);
        logger.debug("should_ReturnTagWhichContainsRussianRubles() successfully completed, because 1 500 000 ₽ == {}", price);
    }

    @Test
    @DisplayName("должен выполняться без ошибок")
    void should_DoesNotThrowExceptionParseAllMethod() {
        Assertions.assertDoesNotThrow(() -> {
            AutoRuCrawler crawler = new AutoRuCrawler();
            crawler.setUpDriver();
            crawler.parseAll();
            crawler.closeDriver();
        });
        logger.debug("should_DoesNotThrowExceptionParseAllMethod() successfully completed");
    }

    //TODO remove/don't remove argument
    private Auto parseElement(WebElement e) {
        WebDriverWait waiter = new WebDriverWait(driver, 4);
        Auto auto = new Auto();

        String URL, model;

        long price;

        float engineCapacity;

        int year;

        //get url of current auto
        URL = driver.getCurrentUrl();

        //get auto information block
        WebElement autoInfo = waiter.until(
                ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("div.c-car-forsale"))));

        //get model and brand string
        String temp =
                AutoUtils.getAutoBrandByString(autoInfo.findElement(By.cssSelector("h3.c-car-forsale__title")).getText());

        //replace brand from general string
        model = AutoUtils.getAutoModelByInputBrand(temp, currentBrand);

        //get price
        price = AutoUtils.getAutoPriceByInputSting(autoInfo.findElement(By.cssSelector(".c-car-forsale__price"))
                .findElements(By.cssSelector("*"))
                .stream()
                .filter(element -> element.getText().contains("₽"))
                .findFirst()
                .get()
                .getText());

        //get engine info
        engineCapacity = AutoUtils.getAutoEngineCapacityByInputString(autoInfo.findElement(By.cssSelector(".c-car-forsale__info"))
                .findElements(By.cssSelector("*"))
                .stream()
                .filter(element -> element.getText().contains("Двигатель"))
                .findFirst()
                .get()
                .getText());

        //get year of building
        year = AutoUtils.getAutoYearByInputString(autoInfo.findElement(By.cssSelector(".c-car-forsale__info"))
                .findElements(By.cssSelector("*"))
                .stream()
                .filter(element -> element.getText().contains("года выпуска"))
                .findFirst()
                .get()
                .getText());

        //set values
        auto.setURL(URL);
        auto.setBrand(currentBrand);
        auto.setModel(model);
        auto.setPrice(price);
        auto.setEngineCapacity(engineCapacity);
        auto.setYear(year);

        return auto;
    }

    //print car names
    private void printCarNames(List<WebElement> gridList) {
        for (WebElement element : gridList
        ) {
            element.findElements(By.cssSelector("span.c-car-title.c-link"))
                    .stream()
                    .map(WebElement::getText)
                    .forEach(System.out::println);
        }
        logger.info("gridlist size is: {}", gridList.size());
    }
}
