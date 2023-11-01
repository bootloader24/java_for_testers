package ru.training.addressbook.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.training.addressbook.model.ContactData;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ContactInfoTests extends TestBase {

    @Test
    void testPhones() {
        if (app.hbm().getContactCount() == 0) {
            app.hbm().createContact(new ContactData(
                    "",
                    "Ivanov",
                    "Andrey",
                    "",
                    "",
                    "",
                    "",
                    "+7-123-456-7890",
                    "+7-656-546-8854",
                    "+7-(989)-558-5544",
                    "8-800-555-3535", ""));
        }
        var contacts = app.hbm().getContactList();
        var expected = contacts.stream().collect(Collectors.toMap(ContactData::id, contact ->
                Stream.of(contact.phoneHome(), contact.phoneMobile(), contact.phoneWork(), contact.phoneSecondary())
                        .filter(s -> s != null && !"".equals(s))
                        .map(s -> s.replaceAll("[() -]+", "")) //удаляем символы, неотображаемые в UI
                        .collect(Collectors.joining("\n"))
        ));
        var phones = app.contacts().getPhones();
        Assertions.assertEquals(expected, phones);
    }

    @Test
    void testContactInfo() {
        if (app.hbm().getContactCount() == 0) {
            app.hbm().createContact(new ContactData(
                    "",
                    "Ivanov",
                    "Andrey",
                    "St. Petersburg,  \n  Lenina, 15/32",
                    "andrey121@gmail.com",
                    "ivanov.a@yandex.ru ",
                    "  andrey.ivanov@sibgiprokommunvodokanal.ru",
                    "+7-123-456-7890",
                    " +7-656-546-8854",
                    "+7-(989)-558-5544 ",
                    "8-800-555-3535",
                    ""));
        }
        var contactsDb = app.hbm().getContactList();
        var rnd = new Random();
        var index = rnd.nextInt(contactsDb.size());
        String id = contactsDb.get(index).id();
        var dataFromHome = app.contacts().getDataFromHome(id);
        var dataFromEditForm = app.contacts().getDataFromEditForm(contactsDb.get(index));
        var expected = new ArrayList<String>();
        // дополнительно удаляем возможные пробелы в начале и конце строк, которые не выводятся в ячейке адреса:
        expected.add(dataFromEditForm.address().replaceAll("(?m)^ +| +$", ""));
        expected.add(Stream.of(dataFromEditForm.email(), dataFromEditForm.email2(), dataFromEditForm.email3())
                .filter(s -> s != null && !s.isEmpty())
                // дополнительно удаляем возможные пробелы в начале строк, которые не выводятся в ячейке email:
                .map(s -> s.replaceAll(" +", ""))
                .collect(Collectors.joining("\n")));
        expected.add(Stream.of(dataFromEditForm.phoneHome(), dataFromEditForm.phoneMobile(), dataFromEditForm.phoneWork(), dataFromEditForm.phoneSecondary())
                .filter(s -> s != null && !s.isEmpty())
                //дополнительно удаляем возможные символы "() -", которые не выводятся в ячейке телефонов:
                .map(s -> s.replaceAll("[() -]+", ""))
                .collect(Collectors.joining("\n")));
        Assertions.assertEquals(expected, dataFromHome);
    }
}
