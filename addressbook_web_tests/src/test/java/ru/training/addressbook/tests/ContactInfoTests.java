package ru.training.addressbook.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.training.addressbook.common.CommonFunctions;
import ru.training.addressbook.model.ContactData;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ContactInfoTests extends TestBase{

    @Test
    void testPhones() {
        if (app.hbm().getContactCount() == 0) {
            app.hbm().createContact(new ContactData(
                    "",
                    "Ivanov",
                    "Andrey",
                    "",
                    "",
                    "+7-123-456-7890",
                    "+7-656-546-8854",
                    "+7-(989)-558-5544",
                    "8-800-555-3535",
                    ""));
        }
        var contacts = app.hbm().getContactList();
        var expected = contacts.stream().collect(Collectors.toMap(ContactData::id, contact ->
            Stream.of(contact.phoneHome(), contact.phoneMobile(), contact.phoneWork(), contact.phoneSecondary())
                    .filter(s -> s != null && ! "".equals(s))
                    .map(s -> s.replaceAll("[() -]+", "")) //удаляем символы, неотображаемые в UI
                    .collect(Collectors.joining("\n"))
        ));
        var phones = app.contacts().getPhones();
        Assertions.assertEquals(expected, phones);
    }
}
