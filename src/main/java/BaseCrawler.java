import org.openqa.selenium.WebElement;

public interface BaseCrawler<T> {

    void connectToUrl();

    void parsePage(WebElement e);

    T parseElement(WebElement e);

    WebElement getElementByLocator(String cssLocator);

}
