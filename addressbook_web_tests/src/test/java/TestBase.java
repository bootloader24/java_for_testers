import model.ContactData;
import model.GroupData;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class TestBase {
    protected static WebDriver driver;

    @BeforeEach
    public void setUp() {
        if (driver == null) {
            driver = new FirefoxDriver();
            Runtime.getRuntime().addShutdownHook(new Thread(driver::quit));
            driver.get("http://localhost/addressbook");
            driver.manage().window().setSize(new Dimension(926, 691));
            driver.findElement(By.name("user")).sendKeys("admin");
            driver.findElement(By.name("pass")).sendKeys("secret");
            driver.findElement(By.xpath("//input[@value=\'Login\']")).click();
        }
    }

    protected boolean isElementPresent(By locator) {
        try {
            driver.findElement(locator);
            return true;
        } catch (NoSuchElementException exception) {
            return false;
        }
    }

    protected boolean isItemPresent() {
        return isElementPresent(By.name("selected[]"));
    }

    protected void createGroup(GroupData group) {
        driver.findElement(By.name("new")).click();
        driver.findElement(By.name("group_name")).click();
        driver.findElement(By.name("group_name")).sendKeys(group.name());
        driver.findElement(By.name("group_header")).click();
        driver.findElement(By.name("group_header")).sendKeys(group.header());
        driver.findElement(By.name("group_footer")).click();
        driver.findElement(By.name("group_footer")).sendKeys(group.footer());
        driver.findElement(By.name("submit")).click();
        driver.findElement(By.linkText("groups")).click();
    }

    protected void openGroupsPage() {
        if (!isElementPresent(By.name("new"))) {
            driver.findElement(By.linkText("groups")).click();
        }
    }

    protected void removeGroup() {
        driver.findElement(By.name("selected[]")).click();
        driver.findElement(By.name("delete")).click();
        driver.findElement(By.linkText("group page")).click();
    }

    protected void openAddNewPage() {
        if (!isElementPresent(By.name("theform"))) {
            driver.findElement(By.linkText("add new")).click();
        }
    }

    protected void createContact(ContactData contact) {
        driver.findElement(By.name("firstname")).click();
        driver.findElement(By.name("firstname")).sendKeys(contact.first_name());
        driver.findElement(By.name("lastname")).click();
        driver.findElement(By.name("lastname")).sendKeys(contact.last_name());
        driver.findElement(By.name("address")).click();
        driver.findElement(By.name("address")).sendKeys(contact.address());
        driver.findElement(By.name("home")).click();
        driver.findElement(By.name("home")).sendKeys(contact.phone_home());
        driver.findElement(By.name("email")).click();
        driver.findElement(By.name("email")).sendKeys(contact.email());
        driver.findElement(By.name("submit")).click();
        driver.findElement(By.linkText("home page")).click();
    }

    protected void openHomePage() {
        if (!isElementPresent(By.name("searchstring"))) {
            driver.findElement(By.linkText("home")).click();
        }
    }

    protected void removeContact() {
        driver.findElement(By.name("selected[]")).click();
        driver.findElement(By.xpath("//input[@value=\'Delete\']")).click();
        driver.switchTo().alert().accept();
    }


}
