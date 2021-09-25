import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseDriver {

    WebDriver driver;
    Actions actions;
    WebDriverWait waiter;

    List<String> tabs = new ArrayList<>();
    int tabIndex;

    public BaseDriver() {
    }

    void setUpDriver() {
        driver = WebDriverFabric.createDriver(Driver.Chrome);
        actions = new Actions(driver);
        waiter = new WebDriverWait(driver, 4);

        driver.manage().window().maximize();
        System.out.println("session id: " + ((ChromeDriver) driver).getSessionId());
    }


    //TODO: создать новый алгоритм работы с вкладками
    void openNewPage(String tabURL){
        //open new tab
        ((JavascriptExecutor) driver).executeScript("window.open('about:blank','_blank');");

        //open link in before opened tab
        //switch in new tab
        driver.getWindowHandles().forEach(s -> {
            if (tabs.isEmpty()) {
                tabs.add(s);
            }

            for (int i = 0; i < tabs.size(); i++) {
                String tab = tabs.get(i);
                if (!s.equals(tab)) {
                    //add new tab in tabs list
                    tabs.add(s);

                    //set current tab index
                    tabIndex = tabs.indexOf(s);

                    //switch to tab
                    driver.switchTo().window(s);
                }
            }
        });

        //open url in new tab
        driver.get(tabURL);
    }

    void closeDriver() {
        if (driver != null) {
            driver.quit();
        }
    }
}
