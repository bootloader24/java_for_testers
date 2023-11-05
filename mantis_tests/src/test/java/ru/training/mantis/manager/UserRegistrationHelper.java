package ru.training.mantis.manager;

import org.openqa.selenium.By;

public class UserRegistrationHelper extends HelperBase {

    public UserRegistrationHelper(ApplicationManager manager) {
        super(manager);
    }

    public void initRegistration(String username, String email) {
        if (isElementPresent(By.className("login-container"))) {
            click(By.xpath("//a[@href='signup_page.php']"));
            type(By.name("username"), username);
            type(By.name("email"), email);
            click(By.xpath("//input[@type='submit']"));
        }
    }

    public void completeRegistration(String url, String username, String password) {
        manager.driver().navigate().to(url);
        type(By.name("realname"), username);
        type(By.id("password"), password);
        type(By.id("password-confirm"), password);
        click(By.xpath("//button[@type='submit']"));
    }
}
