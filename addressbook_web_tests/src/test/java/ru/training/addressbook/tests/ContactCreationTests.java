package ru.training.addressbook.tests;

import ru.training.addressbook.model.ContactData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ContactCreationTests extends TestBase {

    public static List<ContactData> contactProvider() {
        var result = new ArrayList<ContactData>();
        for (var lastName : List.of("", "somelastname")) {
            for (var firstName : List.of("", "somefirstname")) {
                for (var address : List.of("", "someaddress")) {
                    for (var email : List.of("", "some@email")) {
                        for (var phoneHome : List.of("", "123-456-789")) {
                            result.add(new ContactData()
                                    .withLastname(lastName)
                                    .withFirstname(firstName)
                                    .withAddress(address)
                                    .withEmail(email)
                                    .withPhoneHome(phoneHome)
                                    .withPhoto(""));
                        }
                    }
                }
            }
        }
        for (int i = 0; i < 4; i++) {
            result.add(new ContactData()
                    .withLastname(randomString(i * 10))
                    .withFirstname(randomString(i * 10))
                    .withAddress(randomString(i * 10))
                    .withEmail(randomString(i * 10))
                    .withPhoneHome(randomString(i * 10))
                    .withPhoto(""));
        }
        return result;
    }

    public static List<ContactData> negativeContactProvider() {
        var result = new ArrayList<ContactData>(List.of(new ContactData("", "first' name",
                "", "", "", "", "")));
        return result;
    }

    @Test
    void canCreateContact() {
        var contact = new ContactData().withLastnameAndFirstname(randomString(10), randomString(10))
                .withAddress(randomString(10))
                .withEmail(randomString(10))
                .withPhoneHome(randomString(10))
                .withPhoto(randomFile("src/test/resources/images"));
        app.contacts().createContact(contact);
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
        expectedList.add(contact.withId(newContacts.get(newContacts.size() - 1)
                .id())
                .withAddress("")
                .withEmail("")
                .withPhoneHome("")
                .withPhoto(""));
        expectedList.sort(compareById);
        Assertions.assertEquals(newContacts, expectedList);
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
