package ru.training.addressbook.manager;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.JavascriptExecutor;

import ru.training.addressbook.model.ContactData;
import org.openqa.selenium.By;
import ru.training.addressbook.model.GroupData;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class ContactHelper extends HelperBase {

    public ContactHelper(ApplicationManager manager) {
        super(manager);
    }

    public int getCount() {
        openHomePage();
        return manager.driver.findElements(By.name("selected[]")).size();
    }

    public void createContact(ContactData contact) {
        openAddNewPage();
        fillContactForm(contact);
        submitContactCreation();
    }

    public void createContact(ContactData contact, GroupData group) {
        openAddNewPage();
        fillContactForm(contact);
        selectGroup(group);
        submitContactCreation();
    }

    private void selectGroup(GroupData group) {
        new Select(manager.driver.findElement(By.name("new_group"))).selectByValue(group.id());
    }

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

    public List<ContactData> getList() {
        openHomePage();
        return getListFromSelectedGroup();
    }

    public List<ContactData> getList(GroupData groupdata) {
        openHomePage();
        try {
            new Select(manager.driver.findElement(By.name("group"))).selectByValue(groupdata.id());
            return getListFromSelectedGroup();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    private ArrayList<ContactData> getListFromSelectedGroup() {
        var contacts = new ArrayList<ContactData>();
        var tableRows = manager.driver.findElements(By.name("entry"));
        for (var tableRow : tableRows) {
            var id = tableRow.findElement(By.cssSelector("td:nth-child(1).center>input")).getAttribute("value");
            var lastName = tableRow.findElement(By.cssSelector("td:nth-child(2)")).getText();
            var firstName = tableRow.findElement(By.cssSelector("td:nth-child(3)")).getText();
            var address = tableRow.findElement(By.cssSelector("td:nth-child(4)")).getText();
            var email = tableRow.findElement(By.cssSelector("td:nth-child(5)")).getText();
            // поле phone не читаем, так как отображение отличается от хранимого значения, нужны дополнительные преобразования ожидания
            // поле photo не читаем, так как оно не отображается в списке контактов
            contacts.add(new ContactData().withId(id).withLastnameAndFirstname(lastName, firstName).withAddress(address).withEmail(email));
        }
        return contacts;
    }

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
        JavascriptExecutor js = (JavascriptExecutor)manager.driver;
        js.executeScript("arguments[0].click();", element);
    }

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
}