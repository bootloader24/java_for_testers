package ru.training.addressbook.manager;

import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.JavascriptExecutor;

import ru.training.addressbook.model.ContactData;
import org.openqa.selenium.By;
import ru.training.addressbook.model.GroupData;

import java.util.*;
import java.util.stream.Collectors;

public class ContactHelper extends HelperBase {

    public ContactHelper(ApplicationManager manager) {
        super(manager);
    }

    public int getCount() {
        openHomePage();
        return manager.driver.findElements(By.name("selected[]")).size();
    }

    @Step("Создание контакта в UI")
    public void createContact(ContactData contact) {
        openAddNewPage();
        fillContactForm(contact);
        submitContactCreation();
    }

    @Step("Создание контакта в UI в группе \"{group.name}\"")
    public void createContact(ContactData contact, GroupData group) {
        openAddNewPage();
        fillContactForm(contact);
        selectGroup(group);
        submitContactCreation();
    }

    private void selectGroup(GroupData group) {
        new Select(manager.driver.findElement(By.name("new_group"))).selectByValue(group.id());
    }

    @Step("Удаление контакта в UI")
    public void removeContact(ContactData contact) {
        openHomePage();
        selectContact(contact);
        removeSelectedContact();
    }

    private void fillContactForm(ContactData contact) {
        type(By.name("firstname"), contact.firstName());
        type(By.name("lastname"), contact.lastName());
        type(By.name("address"), contact.address());
        type(By.name("home"), contact.phoneHome());
        type(By.name("email"), contact.email());
        attach(By.name("photo"), contact.photo());
    }

    public void openAddNewPage() {
        if (!manager.isElementPresent(By.name("theform"))) {
            manager.driver.findElement(By.linkText("add new")).click();
        }
    }

    public void openHomePage() {
        manager.driver.findElement(By.linkText("home")).click();
    }

    private void submitContactCreation() {
        manager.driver.findElement(By.name("submit")).click();
        manager.driver.findElement(By.cssSelector("div.msgbox"));
    }

    private void selectContact(ContactData contact) {
        click(By.cssSelector(String.format("input[value='%s']", contact.id())));
    }

    private void removeSelectedContact() {
        manager.driver.findElement(By.xpath("//input[@value=\'Delete\']")).click();
        manager.driver.switchTo().alert().accept();
        manager.driver.findElement(By.cssSelector("div.msgbox"));
    }

    @Step("Удаление всех контактов в UI")
    public void removeAllContacts() {
        openHomePage();
        selectAllContacts();
        removeSelectedContact();
    }

    private void selectAllContacts() {
        manager.driver
                .findElements(By.name("selected[]"))
                .forEach(WebElement::click);
    }

    @Step("Получение списка всех контактов с главной страницы UI")
    public List<ContactData> getList() {
        openHomePage();
        return getListFromSelectedGroup();
    }

    @Step("Получение списка контактов в группе \"{groupdata.name}\" с главной страницы UI")
    public List<ContactData> getList(GroupData groupdata) {
        openHomePage();
        try {
            new Select(manager.driver.findElement(By.name("group"))).selectByValue(groupdata.id());
            return getListFromSelectedGroup();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    private List<ContactData> getListFromSelectedGroup() {
        var tableRows = manager.driver.findElements(By.name("entry"));
        return tableRows.stream()
                .map(tableRow -> {
                    var id = tableRow.findElement(By.cssSelector("td:nth-child(1).center>input")).getAttribute("value");
                    var lastName = tableRow.findElement(By.cssSelector("td:nth-child(2)")).getText();
                    var firstName = tableRow.findElement(By.cssSelector("td:nth-child(3)")).getText();
                    var address = tableRow.findElement(By.cssSelector("td:nth-child(4)")).getText();
                    var email = tableRow.findElement(By.cssSelector("td:nth-child(5)")).getText();
                    // поле phone не читаем, так как отображение отличается от хранимого значения, нужны дополнительные преобразования ожидания
                    // поле photo не читаем, так как оно не отображается в списке контактов
                    return new ContactData().withId(id).withLastnameAndFirstname(lastName, firstName)
                            .withAddress(address).withEmail(email);
                })
                .collect(Collectors.toList());
    }

    @Step("Редактирование контакта в UI")
    public void modifyContact(ContactData contact, ContactData modifiedContact) {
        openHomePage();
        initContactModification(contact);
        fillContactForm(modifiedContact);
        submitContactModification();
    }

    private void submitContactModification() {
        click(By.name("update"));
        manager.driver.findElement(By.cssSelector("div.msgbox"));
    }

    private void initContactModification(ContactData contact) {
        /*При длинных значениях в полях контактов кнопка редактирования "уезжает" вправо за пределы видимой области,
        из-за этого обычный метод click() не срабатывает, выкидывая исключение:
        "Element is not clickable at point (x,y) because another element obscures it"
        Для обхода этой проблемы нашлось решение в виде использования JavascriptExecutor
        https://stackoverflow.com/questions/37879010/selenium-debugging-element-is-not-clickable-at-point-x-y */

        WebElement element = manager.driver.findElement(By.xpath("//a[@href='edit.php?id=" + contact.id() + "']"));
        JavascriptExecutor js = (JavascriptExecutor) manager.driver;
        js.executeScript("arguments[0].click();", element);
    }

    @Step("Добавление контакта в группу в UI")
    public void addContactToGroup(ContactData contact, GroupData group) {
        openHomePage();
        selectContact(contact);
        selectGroupForAddContact(group);
    }

    private void selectGroupForAddContact(GroupData group) {
        new Select(manager.driver.findElement(By.name("to_group"))).selectByValue(group.id());
        manager.driver.findElement(By.name("add")).click();
        manager.driver.findElement(By.cssSelector("div.msgbox"));
    }

    @Step("Исключение контакта из группы в UI")
    public void removeContactFromGroup(ContactData contact, GroupData group) {
        openHomePage();
        selectGroupForShowContacts(group);
        selectContact(contact);
        manager.driver.findElement(By.name("remove")).click();
        manager.driver.findElement(By.cssSelector("div.msgbox"));
    }

    private void selectGroupForShowContacts(GroupData group) {
        new Select(manager.driver.findElement(By.name("group"))).selectByValue(group.id());
    }

    @Step("Получение телефонов выбранного контакта с главной страницы UI")
    public String getPhones(ContactData contact) {
        openHomePage();
        return manager.driver.findElement(By.xpath(
                String.format("//input[@id='%s']/../../td[6]", contact.id()))).getText();
    }

    @Step("Получение телефонов всех контактов с главной страницы UI")
    public Map<String, String> getPhones() {
        openHomePage();
        var result = new HashMap<String, String>();
        List<WebElement> tableRows = manager.driver.findElements(By.name("entry"));
        for (WebElement tableRow : tableRows) {
            var id = tableRow.findElement(By.tagName("input")).getAttribute("id");
            var phones = tableRow.findElements(By.tagName("td")).get(5).getText();
            result.put(id, phones);
        }
        return result;
    }

    @Step("Получение адреса, телефонов и email'ов выбранного контакта с главной страницы UI")
    public ArrayList<String> getDataFromHome(String id) {
        openHomePage();
        var result = new ArrayList<String>();
        WebElement tableRow = manager.driver.findElement(By.xpath(String.format("//input[@id='%s']/../..", id)));
        result.add(tableRow.findElements(By.tagName("td")).get(3).getText());
        result.add(tableRow.findElements(By.tagName("td")).get(4).getText());
        result.add(tableRow.findElements(By.tagName("td")).get(5).getText());
        return result;
    }

    @Step("Получение адреса, телефонов и email'ов выбранного контакта с формы редактирования в UI")
    public ContactData getDataFromEditForm(ContactData contact) {
        openHomePage();
        initContactModification(contact);
        var address = manager.driver.findElement(By.name("address")).getText();
        var email = manager.driver.findElement(By.name("email")).getAttribute("value");
        var email2 = manager.driver.findElement(By.name("email2")).getAttribute("value");
        var email3 = manager.driver.findElement(By.name("email3")).getAttribute("value");
        var phoneHome = manager.driver.findElement(By.name("home")).getAttribute("value");
        var phoneMobile = manager.driver.findElement(By.name("mobile")).getAttribute("value");
        var phoneWork = manager.driver.findElement(By.name("work")).getAttribute("value");
        var phoneSecondary = manager.driver.findElement(By.name("phone2")).getAttribute("value");
        return new ContactData()
                .withAddress(address)
                .withEmail(email)
                .withEmail2(email2)
                .withEmail3(email3)
                .withPhoneHome(phoneHome)
                .withPhoneMobile(phoneMobile)
                .withPhoneWork(phoneWork)
                .withPhoneSecondary(phoneSecondary);
    }
}