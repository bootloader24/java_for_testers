package ru.training.addressbook.tests;

import io.qameta.allure.Allure;
import org.junit.jupiter.api.DisplayName;
import ru.training.addressbook.common.CommonFunctions;
import ru.training.addressbook.model.ContactData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.training.addressbook.model.GroupData;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

@DisplayName("ContactModificationTests: Тесты редактирования контактов")
public class ContactModificationTests extends TestBase {

    @Test
    @DisplayName("canModifyContact: Тест редактирования полей контакта")
    void canModifyContact() {
        Allure.step("Проверка предусловия (должен существовать хотя бы один контакт)", step -> {
            if (app.hbm().getContactCount() == 0) {
                app.hbm().createContact(new ContactData("", "Ivanov", "Andrey",
                        "Lenina, 15", "andrey121@gmail.com", "", "",
                        "+7-123-456-7890", "", "", "", ""));
            }
        });
        var oldContacts = app.hbm().getContactList();
        var rnd = new Random();
        var index = rnd.nextInt(oldContacts.size());
        var testData = new ContactData()
                .withLastnameAndFirstname(CommonFunctions.randomString(8), CommonFunctions.randomString(8))
                .withAddress(CommonFunctions.randomString(8))
                .withEmail(CommonFunctions.randomString(5) + "@" + CommonFunctions.randomString(8))
                .withEmail2("")
                .withEmail3("")
                .withPhoneHome("1234567")
                .withPhoneMobile("")
                .withPhoneWork("")
                .withPhoneSecondary("")
                .withPhoto("");
        app.contacts().modifyContact(oldContacts.get(index), testData);
        var newContacts = app.hbm().getContactList();
        var expectedList = new ArrayList<>(oldContacts);
        expectedList.set(index, testData.withId(oldContacts.get(index).id()));
        Allure.step("Проверка успешности изменения контакта в БД", step -> {
            Assertions.assertEquals(Set.copyOf(expectedList), Set.copyOf(newContacts));
        });

        var newUiContacts = app.contacts().getList();
        // на основе expectedList создаём новый ожидаемый список контактов с пустыми значениями полей, которые не читаются из UI
        var expectedUiList = new ArrayList<>(expectedList);
        expectedUiList.replaceAll(contacts -> contacts.withPhoneHome("").withPhoto(""));
        Allure.step("Проверка успешности изменения контакта в UI", step -> {
            Assertions.assertEquals(Set.copyOf(expectedUiList), Set.copyOf(newUiContacts));
        });
    }

    @Test
    @DisplayName("canAddContactToGroup: Тест добавления контакта в группу")
    void canAddContactToGroup() {
        Allure.step("Проверка предусловия (должен существовать хотя бы один контакт)", step -> {
            if (app.hbm().getContactCount() == 0) {
                app.hbm().createContact(new ContactData("", "Ivanov", "Andrey",
                        "Lenina, 15", "andrey121@gmail.com", "", "", "+7-123-456-7890",
                        "", "", "", ""));
            }
        });
        Allure.step("Проверка предусловия (должна существовать хотя бы одна группа)", step -> {
            if (app.hbm().getGroupCount() == 0) {
                app.hbm().createGroup(new GroupData("", "group name", "group header", "group footer"));
            }
        });
        var group = app.hbm().getGroupList().get(0);
        var contact = app.hbm().getContactList().get(0);
        Allure.step("Проверка предусловия (контакт не должен входить в группу)", step -> {
            if (app.hbm().isExistContactInGroup(group, contact)) {
                app.contacts().removeContactFromGroup(contact, group);
            }
        });
        var oldRelated = app.hbm().getContactsInGroup(group);
        app.contacts().addContactToGroup(contact, group);
        var newRelated = app.hbm().getContactsInGroup(group);
        Allure.step("Проверка успешности изменения количества контактов в группе в БД", step -> {
            Assertions.assertEquals(oldRelated.size() + 1, newRelated.size());
        });

        var expectedList = new ArrayList<>(oldRelated);
        expectedList.add(contact);
        Allure.step("Проверка успешности появления контакта в группе в БД", step -> {
            Assertions.assertEquals(Set.copyOf(expectedList), Set.copyOf(newRelated));
        });

        var newUiContactsInGroup = app.contacts().getList(group);
        var expectedUiList = new ArrayList<>(expectedList);
        expectedUiList.replaceAll(contacts -> contacts.withPhoneHome("").withPhoto(""));
        Allure.step("Проверка успешности появления контакта в группе в UI", step -> {
            Assertions.assertEquals(Set.copyOf(expectedUiList), Set.copyOf(newUiContactsInGroup));
        });
    }

    @Test
    @DisplayName("canRemoveContactFromGroup: Тест удаления контакта из группы")
    void canRemoveContactFromGroup() {
        Allure.step("Проверка предусловия (должен существовать хотя бы один контакт)", step -> {
            if (app.hbm().getContactCount() == 0) {
                app.hbm().createContact(new ContactData("", "Ivanov", "Andrey",
                        "Lenina, 15", "andrey121@gmail.com", "", "", "+7-123-456-7890",
                        "", "", "", ""));
            }
        });
        Allure.step("Проверка предусловия (должна существовать хотя бы одна группа)", step -> {
            if (app.hbm().getGroupCount() == 0) {
                app.hbm().createGroup(new GroupData("", "group name", "group header", "group footer"));
            }
        });
        var group = app.hbm().getGroupList().get(0);
        var contact = app.hbm().getContactList().get(0);
        Allure.step("Проверка предусловия (контакт должен входить в группу)", step -> {
            if (!app.hbm().isExistContactInGroup(group, contact)) {
                app.contacts().addContactToGroup(contact, group);
            }
        });
        var oldRelated = app.hbm().getContactsInGroup(group);
        app.contacts().removeContactFromGroup(contact, group);
        var newRelated = app.hbm().getContactsInGroup(group);
        Allure.step("Проверка успешности изменения количества контактов в группе в БД", step -> {
            Assertions.assertEquals(oldRelated.size() - 1, newRelated.size());
        });

        var expectedList = new ArrayList<>(oldRelated);
        expectedList.remove(0);
        Allure.step("Проверка успешности удаления контакта из группы в БД", step -> {
            Assertions.assertEquals(Set.copyOf(expectedList), Set.copyOf(newRelated));
        });

        var newUiContactsInGroup = app.contacts().getList(group);
        var expectedUiList = new ArrayList<>(expectedList);
        expectedUiList.replaceAll(contacts -> contacts.withPhoneHome("").withPhoto(""));
        Allure.step("Проверка успешности удаления контакта из группы в UI", step -> {
            Assertions.assertEquals(Set.copyOf(expectedUiList), Set.copyOf(newUiContactsInGroup));
        });
    }
}
