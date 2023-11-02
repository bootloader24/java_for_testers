package ru.training.addressbook.tests;

import ru.training.addressbook.common.CommonFunctions;
import ru.training.addressbook.model.ContactData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.training.addressbook.model.GroupData;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

public class ContactModificationTests extends TestBase {

    @Test
    void canModifyContact() {
        if (app.hbm().getContactCount() == 0) {
            app.hbm().createContact(new ContactData("", "Ivanov", "Andrey",
                    "Lenina, 15", "andrey121@gmail.com", "", "",
                    "+7-123-456-7890", "", "", "", ""));
        }
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
        Assertions.assertEquals(Set.copyOf(expectedList), Set.copyOf(newContacts));

        var newUiContacts = app.contacts().getList();
        // на основе expectedList создаём новый ожидаемый список контактов с пустыми значениями полей, которые не читаются из UI
        var expectedUiList = new ArrayList<>(expectedList);
        expectedUiList.replaceAll(contacts -> contacts.withPhoneHome("").withPhoto(""));
        Assertions.assertEquals(Set.copyOf(expectedUiList), Set.copyOf(newUiContacts));
    }

    @Test
    void canAddContactToGroup() {
        if (app.hbm().getContactCount() == 0) {
            app.hbm().createContact(new ContactData("", "Ivanov", "Andrey",
                    "Lenina, 15", "andrey121@gmail.com", "", "", "+7-123-456-7890",
                    "", "", "", ""));
        }
        if (app.hbm().getGroupCount() == 0) {
            app.hbm().createGroup(new GroupData("", "group name", "group header", "group footer"));
        }
        var group = app.hbm().getGroupList().get(0);
        var contact = app.hbm().getContactList().get(0);

        if (app.hbm().isExistContactInGroup(group, contact)) {
            app.contacts().removeContactFromGroup(contact, group);
        }
        ;
        var oldRelated = app.hbm().getContactsInGroup(group);
        app.contacts().addContactToGroup(contact, group);
        var newRelated = app.hbm().getContactsInGroup(group);
        Assertions.assertEquals(oldRelated.size() + 1, newRelated.size()); // простая проверка по количеству

        var expectedList = new ArrayList<>(oldRelated);
        expectedList.add(contact);
        Assertions.assertEquals(Set.copyOf(expectedList), Set.copyOf(newRelated)); // проверка со сравнением списков в БД

        var newUiContactsInGroup = app.contacts().getList(group);
        var expectedUiList = new ArrayList<>(expectedList);
        expectedUiList.replaceAll(contacts -> contacts.withPhoneHome("").withPhoto(""));
        Assertions.assertEquals(Set.copyOf(expectedUiList), Set.copyOf(newUiContactsInGroup)); // проверка с чтением списка контактов из UI в выбранной группе
    }

    @Test
    void canRemoveContactFromGroup() {
        if (app.hbm().getContactCount() == 0) {
            app.hbm().createContact(new ContactData("", "Ivanov", "Andrey",
                    "Lenina, 15", "andrey121@gmail.com", "", "", "+7-123-456-7890",
                    "", "", "", ""));
        }
        if (app.hbm().getGroupCount() == 0) {
            app.hbm().createGroup(new GroupData("", "group name", "group header", "group footer"));
        }
        var group = app.hbm().getGroupList().get(0);
        var contact = app.hbm().getContactList().get(0);

        if (!app.hbm().isExistContactInGroup(group, contact)) {
            app.contacts().addContactToGroup(contact, group);
        }
        var oldRelated = app.hbm().getContactsInGroup(group);
        app.contacts().removeContactFromGroup(contact, group);
        var newRelated = app.hbm().getContactsInGroup(group);
        Assertions.assertEquals(oldRelated.size() - 1, newRelated.size()); // простая проверка по количеству

        var expectedList = new ArrayList<>(oldRelated);
        expectedList.remove(0);
        Assertions.assertEquals(Set.copyOf(expectedList), Set.copyOf(newRelated)); // проверка со сравнением списков в БД

        var newUiContactsInGroup = app.contacts().getList(group);
        var expectedUiList = new ArrayList<>(expectedList);
        expectedUiList.replaceAll(contacts -> contacts.withPhoneHome("").withPhoto(""));
        Assertions.assertEquals(Set.copyOf(expectedUiList), Set.copyOf(newUiContactsInGroup)); // проверка с чтением списка контактов из UI в выбранной группе
    }
}
