package ru.training.addressbook.tests;

import ru.training.addressbook.model.ContactData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.training.addressbook.model.GroupData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class ContactModificationTests extends TestBase {

    @Test
    void canModifyContact() {
        if (app.hbm().getContactCount() == 0) {
            app.hbm().createContact(new ContactData("", "Ivanov", "Andrey",
                    "Lenina, 15", "andrey121@gmail.com", "+7-123-456-7890", ""));
        }
        var oldContacts = app.hbm().getContactList();
        var rnd = new Random();
        var index = rnd.nextInt(oldContacts.size());
        var testData = new ContactData()
                .withLastnameAndFirstname("new lastname", "new firstname")
                .withAddress("new address")
                .withEmail("new@email")
                .withPhoneHome("1234567")
                .withPhoto("");
        app.contacts().modifyContact(oldContacts.get(index), testData);
        var newContacts = app.hbm().getContactList();
        var expectedList = new ArrayList<>(oldContacts);
        expectedList.set(index, testData.withId(oldContacts.get(index).id()));
        Comparator<ContactData> compareById = (o1, o2) -> {
            return Integer.compare(Integer.parseInt(o1.id()), Integer.parseInt(o2.id()));
        };
        newContacts.sort(compareById);
        expectedList.sort(compareById);
        Assertions.assertEquals(expectedList, newContacts);

        var newUiContacts = app.contacts().getList();
        newUiContacts.sort(compareById);
        // на основе expectedList создаём новый ожидаемый список контактов с пустыми значениями полей, которые не читаются из UI
        var expectedUiList = new ArrayList<>(expectedList);
        expectedUiList.replaceAll(contacts -> contacts.withPhoneHome("").withPhoto(""));
        Assertions.assertEquals(expectedUiList, newUiContacts);
    }

    @Test
    void canAddContactToGroup() {
        if (app.hbm().getContactCount() == 0) {
            app.hbm().createContact(new ContactData("", "Ivanov", "Andrey",
                    "Lenina, 15", "andrey121@gmail.com", "+7-123-456-7890", ""));
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

        Comparator<ContactData> compareById = (o1, o2) -> {
            return Integer.compare(Integer.parseInt(o1.id()), Integer.parseInt(o2.id()));
        };
        newRelated.sort(compareById);
        var expectedList = new ArrayList<>(oldRelated);
        expectedList.add(contact);
        expectedList.sort(compareById);
        Assertions.assertEquals(expectedList, newRelated); // проверка со сравнением списков в БД

        var newUiContactsInGroup = app.contacts().getList(group);
        newUiContactsInGroup.sort(compareById);
        var expectedUiList = new ArrayList<>(expectedList);
        expectedUiList.replaceAll(contacts -> contacts.withPhoneHome("").withPhoto(""));
        Assertions.assertEquals(expectedUiList, newUiContactsInGroup); // проверка с чтением списка контактов из UI в выбранной группе
    }

    @Test
    void canRemoveContactFromGroup() {
        if (app.hbm().getContactCount() == 0) {
            app.hbm().createContact(new ContactData("", "Ivanov", "Andrey",
                    "Lenina, 15", "andrey121@gmail.com", "+7-123-456-7890", ""));
        }
        if (app.hbm().getGroupCount() == 0) {
            app.hbm().createGroup(new GroupData("", "group name", "group header", "group footer"));
        }
        var group = app.hbm().getGroupList().get(0);
        var contact = app.hbm().getContactList().get(0);

        if (!app.hbm().isExistContactInGroup(group, contact)) {
            app.contacts().addContactToGroup(contact, group);
        }
        ;
        var oldRelated = app.hbm().getContactsInGroup(group);
        app.contacts().removeContactFromGroup(contact, group);
        var newRelated = app.hbm().getContactsInGroup(group);
        Assertions.assertEquals(oldRelated.size() - 1, newRelated.size()); // простая проверка по количеству

        Comparator<ContactData> compareById = (o1, o2) -> {
            return Integer.compare(Integer.parseInt(o1.id()), Integer.parseInt(o2.id()));
        };
        var expectedList = new ArrayList<>(oldRelated);
        expectedList.remove(0);
        expectedList.sort(compareById);
        if (!newRelated.isEmpty()) { // если новый список связанных контактов пуст, то проверка не имеет смысла
            newRelated.sort(compareById);
            Assertions.assertEquals(expectedList, newRelated); // проверка со сравнением списков в БД
        }

        var newUiContactsInGroup = app.contacts().getList(group);
        newUiContactsInGroup.sort(compareById);
        var expectedUiList = new ArrayList<>(expectedList);
        expectedUiList.replaceAll(contacts -> contacts.withPhoneHome("").withPhoto(""));
        Assertions.assertEquals(expectedUiList, newUiContactsInGroup); // проверка с чтением списка контактов из UI в выбранной группе
    }

}
