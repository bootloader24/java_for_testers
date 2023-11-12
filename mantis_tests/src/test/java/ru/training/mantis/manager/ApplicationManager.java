package ru.training.mantis.manager;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;

import java.time.Duration;
import java.util.Properties;

public class ApplicationManager {
    private WebDriver driver;
    private String browser;
    private Properties properties;
    private SessionHelper sessionHelper;
    private HttpSessionHelper httpSessionHelper;
    private JamesCliHelper jamesCliHelper;
    private JamesApiHelper jamesApiHelper;
    private MailHelper mailHelper;
    private UserRegistrationHelper userRegistrationHelper;
    private MantisRestApiHelper mantisRestApiHelper;

    public void init(String browser, Properties properties) {
        this.browser = browser;
        this.properties = properties;
    }

    public WebDriver driver() {
        if (driver == null) {
            if ("firefox".equals(browser)) {
                driver = new FirefoxDriver();
            } else if ("chrome".equals(browser)) {
                driver = new ChromeDriver();
            } else if ("safari".equals(browser)) {
                driver = new SafariDriver();
            } else {
                throw new IllegalArgumentException(String.format("Unknown browser: %a", browser));
            }
            driver.manage().timeouts().implicitlyWait(Duration.ofMillis(2000));
            Runtime.getRuntime().addShutdownHook(new Thread(driver::quit));
            driver.get(properties.getProperty("web.baseUrl"));
            driver.manage().window().setSize(new Dimension(926, 691));
        }
        return driver;
    }

    public SessionHelper session() {
        if (sessionHelper == null) {
            sessionHelper = new SessionHelper(this);
        }
        return sessionHelper;
    }

    public HttpSessionHelper http() {
        if (httpSessionHelper == null) {
            httpSessionHelper = new HttpSessionHelper(this);
        }
        return httpSessionHelper;
    }

    public JamesCliHelper jamesCli() {
        if (jamesCliHelper == null) {
            jamesCliHelper = new JamesCliHelper(this);
        }
        return jamesCliHelper;
    }

    public JamesApiHelper jamesApi() {
        if (jamesApiHelper == null) {
            jamesApiHelper = new JamesApiHelper(this);
        }
        return jamesApiHelper;
    }

    public MailHelper mail() {
        if (mailHelper == null) {
            mailHelper = new MailHelper(this);
        }
        return mailHelper;
    }

    public UserRegistrationHelper registration() {
        if (userRegistrationHelper == null) {
            userRegistrationHelper = new UserRegistrationHelper(this);
        }
        return userRegistrationHelper;
    }

    public MantisRestApiHelper mantisRestApi() {
        if (mantisRestApiHelper == null) {
            mantisRestApiHelper = new MantisRestApiHelper(this);
        }
        return mantisRestApiHelper;
    }
    public String property(String name) {
        return properties.getProperty(name);
    }
}
