package ru.training.addressbook.tests;

import io.qameta.allure.Allure;
import org.junit.jupiter.api.DisplayName;
import ru.training.addressbook.model.ContactData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Random;

@DisplayName("ContactRemovalTests: Тесты удаления контактов")
public class ContactRemovalTests extends TestBase {

    @Test
    @DisplayName("canRemoveContact: Тест удаления одного контакта")
    public void canRemoveContact() {
        Allure.step("Проверка предусловия (должен существовать хотя бы один контакт)", step -> {
            if (app.hbm().getContactCount() == 0) {
                app.hbm().createContact(new ContactData("", "Ivanov", "Andrey",
                        "Lenina, 15", "andrey121@gmail.com", "", "", "+7-123-456-7890",
                        "", "", "", ""));
            }
        });
        var oldContacts = app.hbm().getContactList();
        var rnd = new Random();
        var index = rnd.nextInt(oldContacts.size());
        app.contacts().removeContact(oldContacts.get(index));
        var newContacts = app.hbm().getContactList();
        var expectedList = new ArrayList<>(oldContacts);
        expectedList.remove(index);
        Allure.step("Проверка успешности удаления контакта в БД", step -> {
            Assertions.assertEquals(expectedList, newContacts);
        });
    }

    @Test
    @DisplayName("canRemoveAllContactsAtOnce: Тест удаления сразу всех контактов")
    public void canRemoveAllContactsAtOnce() {
        Allure.step("Проверка предусловия (должен существовать хотя бы один контакт)", step -> {
            if (app.hbm().getContactCount() == 0) {
                app.hbm().createContact(new ContactData("", "Ivanov", "Andrey",
                        "Lenina, 15", "andrey121@gmail.com", "", "", "+7-123-456-7890",
                        "", "", "", ""));
            }
        });
        app.contacts().removeAllContacts();
        Allure.step("Проверка успешности удаления всех контактов в БД", step -> {
            Assertions.assertEquals(0, app.hbm().getContactCount());
        });
    }
}
