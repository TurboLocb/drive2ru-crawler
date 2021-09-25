import data.objects.Auto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.List;

public class AutoRuCrawler extends BaseDriver implements BaseCrawler<Auto> {

    private String URL = "https://www.drive2.ru/cars/?sort=selling";
    private final Logger logger = LogManager.getLogger(AutoRuCrawler.class);

    private String currentBrand;

    public AutoRuCrawler() {}

    @Override
    public void setUpDriver() {
        super.setUpDriver();
    }

    @Override
    public void closeDriver() {
        super.closeDriver();
    }

    @Override
    public void connectToUrl() {
        driver.get(URL);
        logger.debug("current session id: {}", ((ChromeDriver) driver).getSessionId());
    }

    /**
     * @param element include link on page
     *                create link in new tab before parse page
     *                close tab with link after parse
     */
    @Override
    public void parsePage(WebElement element) {
        List<WebElement> gridList = new ArrayList<>();

        //open link in new tab
        openNewPage(element.getAttribute("href"));

        //check - if element exist
        if (!driver.findElements(By.cssSelector("div.c-darkening-hover-container:first-child")).isEmpty()) {
            //slide to footer
            WebElement footer = driver.findElement(By.tagName("footer"));

            //driver.manage().timeouts().implicitlyWait(4, TimeUnit.SECONDS);

            int current = 0;

            while (!driver.findElements(By.cssSelector("button[data-action='catalog.morecars']")).isEmpty()) {
                //scroll down
                actions.moveToElement(footer).perform();

                //update list
                gridList = waiter.until(ExpectedConditions
                        .visibilityOfAllElements(driver.findElements(By.cssSelector("div.c-darkening-hover-container"))));

                //to process element
                if (gridList.size() > current) {
                    for (int i = current; i < gridList.size(); i++) {
                        WebElement e = gridList.get(i);

                        //Get auto info
                        parseElement(e);

                        current++;
                    }
                }
            }
            logger.info("button 'Загрузить больше' doesn't exist already");
        }

        //close tab
        closeCurrentTabAndReturnOnPreviousTab();
    }

    //TODO написать код для парсинга всего drive2.ru
    public void parseAll() {
        logger.info("start parse drive2.ru");

        //open site
        driver.get(URL);

        //open more
        driver.findElement(By.cssSelector("button[data-action='makeslist.more']")).click();

        //wait all brand elements
        List<WebElement> brands = waiter.until(ExpectedConditions
                .visibilityOfAllElements(
                        driver.findElements(By.cssSelector("nav[data-role='makeslist'] span a"))));

        brands.forEach(element -> {
            logger.info("start parse auto brand '{}' by link: {} ", element.getText(), element.getAttribute("href"));

            //set current brand
            currentBrand = element.getText();

            //parse brand page
            parsePage(element);
        });

        logger.info("finish parse drive2.ru");
    }

    @Override
    public WebElement getElementByLocator(String cssSelector) {
        return waiter.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector(cssSelector))));
    }

    /**
     * @param e - is div block with link on some auto
     *          <p>
     *          in this method we open new tab in browser (use openTab();)
     *          and close tab after receive information
     */
    @Override
    public Auto parseElement(WebElement e) {
        Auto auto = new Auto();

        String URL, model;

        long price;

        float engineCapacity;

        int year;

        logger.info("=== begin parse auto ===");

        //open auto by link
        openNewPage(e.findElement(By.cssSelector("a.u-link-area")).getAttribute("href"));

        //get url of current auto
        URL = driver.getCurrentUrl();

        //log link
        logger.info("=== auto url: {} ===", URL);

        //get auto information block
        WebElement autoInfo = waiter.until(
                ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("div.c-car-forsale"))));

        //get model and brand string
        String temp =
                AutoUtils.getAutoBrandByString(autoInfo.findElement(By.cssSelector("h3.c-car-forsale__title")).getText());

        //log temp string
        logger.info("=== auto temp string: {} ===", temp);

        //replace brand from general string
        model = AutoUtils.getAutoModelByInputBrand(temp, currentBrand);

        //log model
        logger.info("=== auto model: {} ===", model);

        //get price
        price = AutoUtils.getAutoPriceByInputSting(autoInfo.findElement(By.cssSelector(".c-car-forsale__price"))
                .findElements(By.cssSelector("*"))
                .stream()
                .filter(element -> element.getText().contains("₽"))
                .findFirst()
                .get()
                .getText());

        //log price
        logger.info("=== auto price: {} ===", price);

        //get engine info
        engineCapacity = AutoUtils.getAutoEngineCapacityByInputString(autoInfo.findElement(By.cssSelector(".c-car-forsale__info"))
                .findElements(By.cssSelector("*"))
                .stream()
                .filter(element -> element.getText().contains("Двигатель"))
                .findFirst()
                .get()
                .getText());

        //log engine capacity
        logger.info("=== auto engine capacity: {} ===", engineCapacity);

        //get year of building
        year = AutoUtils.getAutoYearByInputString(autoInfo.findElement(By.cssSelector(".c-car-forsale__info"))
                .findElements(By.cssSelector("*"))
                .stream()
                .filter(element -> element.getText().contains("года выпуска"))
                .findFirst()
                .get()
                .getText());

        //log year of auto
        logger.info("=== auto year: {} ===", year);

        //set values
        auto.setURL(URL);
        auto.setBrand(currentBrand);
        auto.setModel(model);
        auto.setPrice(price);
        auto.setEngineCapacity(engineCapacity);
        auto.setYear(year);

        //inform about auto
        logger.info("information about auto was collected: {}", auto);

        //close current tab with current auto
        closeCurrentTabAndReturnOnPreviousTab();

        return auto;
    }

    public void closeCurrentTabAndReturnOnPreviousTab() {
        if (!driver.getWindowHandle().equals(tabs.get(tabIndex))) {
            driver.close();
            driver.switchTo().window(tabs.get(tabIndex - 1));
        }
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getCurrentBrand() {
        return currentBrand;
    }

    public void setCurrentBrand(String currentBrand) {
        this.currentBrand = currentBrand;
    }

    public String getCurrentHandle() {
        return driver.getWindowHandle();
    }

    public void setCurrentHandle(String currentHandle, int index) {
        if (tabs.isEmpty()){
            tabs.add(currentHandle);
        }
        this.tabs.set(index, currentHandle);
    }
}
