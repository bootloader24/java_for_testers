package ru.training.addressbook.tests;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.training.addressbook.common.CommonFunctions;
import ru.training.addressbook.model.ContactData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.training.addressbook.model.GroupData;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ContactCreationTests extends TestBase {

    public static List<ContactData> contactProvider() throws IOException {
        var result = new ArrayList<ContactData>();
//        for (var lastName : List.of("", "somelastname")) {
//            for (var firstName : List.of("", "somefirstname")) {
//                for (var address : List.of("", "someaddress")) {
//                    for (var email : List.of("", "some@email")) {
//                        for (var phoneHome : List.of("", "123-456-789")) {
//                            result.add(new ContactData()
//                                    .withLastname(lastName)
//                                    .withFirstname(firstName)
//                                    .withAddress(address)
//                                    .withEmail(email)
//                                    .withPhoneHome(phoneHome)
//                                    .withPhoto(""));
//                        }
//                    }
//                }
//            }
//        }
        var json = Files.readString(Paths.get("contacts.json"));
        ObjectMapper mapper = new ObjectMapper();
        var value = mapper.readValue(json, new TypeReference<List<ContactData>>() {});
        result.addAll(value);
        return result;
    }

    public static List<ContactData> singleRandomContactProvider() throws IOException {
        return List.of(new ContactData().withLastnameAndFirstname(CommonFunctions.randomString(10), CommonFunctions.randomString(10))
                .withAddress(CommonFunctions.randomString(10))
                .withEmail(CommonFunctions.randomString(10))
                .withPhoneHome(CommonFunctions.randomString(10))
                .withPhoto(CommonFunctions.randomFile("src/test/resources/images")));

    }

    public static List<ContactData> negativeContactProvider() {
        var result = new ArrayList<ContactData>(List.of(new ContactData("", "first' name",
                "", "", "", "", "")));
        return result;
    }

    @ParameterizedTest
    @MethodSource("singleRandomContactProvider")
    void canCreateContact(ContactData contact) {
        var oldContacts = app.hbm().getContactList();
        app.contacts().createContact(contact);
        var newContacts = app.hbm().getContactList();
        Comparator<ContactData> compareById = (o1, o2) -> {
            return Integer.compare(Integer.parseInt(o1.id()), Integer.parseInt(o2.id()));
        };
        newContacts.sort(compareById);
        var maxId = newContacts.get(newContacts.size() - 1).id();
        var expectedList = new ArrayList<>(oldContacts);
        expectedList.add(contact.withId(maxId).withPhoto(""));
        expectedList.sort(compareById);
        Assertions.assertEquals(expectedList, newContacts);

        var newUiContacts = app.contacts().getList();
        newUiContacts.sort(compareById);
        // на основе expectedList создаём новый ожидаемый список контактов с пустыми значениями полей, которые не читаются из UI
        var expectedUiList = new ArrayList<>(expectedList);
        // phone не будем читать, так как в UI не отображаются символы пробела, дефиса и круглых скобок
        // здесь потребуется дополнительная обработка этого поля в ожидаемом значении
        expectedUiList.replaceAll(contacts -> contacts.withPhoneHome("").withPhoto(""));
        Assertions.assertEquals(expectedUiList, newUiContacts);
    }

    @ParameterizedTest
    @MethodSource("singleRandomContactProvider")
    void canCreateContactInGroup(ContactData contact) {
        if (app.hbm().getGroupCount() == 0) {
            app.hbm().createGroup(new GroupData("", "group name", "group header", "group footer"));
        }
        var group = app.hbm().getGroupList().get(0);
        var oldRelated = app.hbm().getContactsInGroup(group);
        app.contacts().createContact(contact, group);
        var newRelated = app.hbm().getContactsInGroup(group);
        Assertions.assertEquals(oldRelated.size() + 1, newRelated.size()); // простая проверка по количеству

        Comparator<ContactData> compareById = (o1, o2) -> {
            return Integer.compare(Integer.parseInt(o1.id()), Integer.parseInt(o2.id()));
        };
        newRelated.sort(compareById);
        var maxId = newRelated.get(newRelated.size() - 1).id();
        var expectedList = new ArrayList<>(oldRelated);
        expectedList.add(contact.withId(maxId).withPhoto(""));
        expectedList.sort(compareById);
        Assertions.assertEquals(expectedList, newRelated); // проверка со сравнением списков в БД

        var newUiContactsInGroup = app.contacts().getList(group);
        newUiContactsInGroup.sort(compareById);
        var expectedUiList = new ArrayList<>(expectedList);
        expectedUiList.replaceAll(contacts -> contacts.withPhoneHome("").withPhoto(""));
        Assertions.assertEquals(expectedUiList, newUiContactsInGroup); // проверка с чтением списка контактов из UI в выбранной группе
    }

    @ParameterizedTest
    @MethodSource("contactProvider")
    public void canCreateMultipleContacts(ContactData contact) {
        var oldContacts = app.contacts().getList();
        app.contacts().createContact(contact);
        var newContacts = app.contacts().getList();
        Comparator<ContactData> compareById = (o1, o2) -> {
            return Integer.compare(Integer.parseInt(o1.id()), Integer.parseInt(o2.id()));
        };
        newContacts.sort(compareById);
        var expectedList = new ArrayList<>(oldContacts);
        expectedList.add(contact
                .withId(newContacts.get(newContacts.size() - 1).id())
                .withPhoneHome("")
                .withPhoto(""));
        expectedList.sort(compareById);
        Assertions.assertEquals(expectedList, newContacts);
    }

    @ParameterizedTest
    @MethodSource("negativeContactProvider")
    public void canNotCreateContact(ContactData contact) {
        var oldContacts = app.contacts().getList();
        app.contacts().createContact(contact);
        var newContacts = app.contacts().getList();
        Assertions.assertEquals(newContacts, oldContacts);
    }

}
