package ru.training.addressbook.tests;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.DisplayName;
import ru.training.addressbook.common.CommonFunctions;
import ru.training.addressbook.model.ContactData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.training.addressbook.model.GroupData;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;

@DisplayName("ContactCreationTests: Тесты создания контактов")
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

    public static Stream<ContactData> RandomContactsProvider() {
        Supplier<ContactData> randomContact = () -> new ContactData()
        .withLastnameAndFirstname(CommonFunctions.randomString(10), CommonFunctions.randomString(10))
                .withAddress(CommonFunctions.randomString(10))
                .withEmail(CommonFunctions.randomString(10))
                .withPhoneHome(CommonFunctions.randomString(10))
                .withPhoto(CommonFunctions.randomFile("src/test/resources/images"));
        return Stream.generate(randomContact).limit(1);
    }

    public static List<ContactData> negativeContactProvider() {
        var result = new ArrayList<ContactData>(List.of(new ContactData("", "first' name",
                "", "", "", "", "", "", "", "",
                "", "")));
        return result;
    }

    @ParameterizedTest
    @MethodSource("RandomContactsProvider")
    @DisplayName("canCreateContact: Тест создания контакта со случайными значениями полей")
    void canCreateContact(ContactData contact) {
        var oldContacts = app.hbm().getContactList();
        app.contacts().createContact(contact);
        var newContacts = app.hbm().getContactList();
        var extraContacts = newContacts.stream().filter(c -> ! oldContacts.contains(c)).toList();
        var newId = extraContacts.get(0).id();
        var expectedList = new ArrayList<>(oldContacts);
        expectedList.add(contact.withId(newId).withPhoto(""));
        Allure.step("Проверка успешности появления контакта в БД", step -> {
            Assertions.assertEquals(Set.copyOf(expectedList), Set.copyOf(newContacts));
        });

        var newUiContacts = app.contacts().getList();
        // на основе expectedList создаём новый ожидаемый список контактов с пустыми значениями полей, которые не читаются из UI
        var expectedUiList = new ArrayList<>(expectedList);
        // phone не будем читать, так как в UI не отображаются символы пробела, дефиса и круглых скобок
        // здесь потребуется дополнительная обработка этого поля в ожидаемом значении
        expectedUiList.replaceAll(contacts -> contacts.withPhoneHome("").withPhoto(""));
        Allure.step("Проверка успешности появления контакта в UI", step -> {
            Assertions.assertEquals(Set.copyOf(expectedUiList), Set.copyOf(newUiContacts));
        });
    }

    @ParameterizedTest
    @MethodSource("RandomContactsProvider")
    @DisplayName("canCreateContactInGroup: Тест создания контакта в группе")
    void canCreateContactInGroup(ContactData contact) {
        Allure.step("Проверка предусловия (должна существовать хотя бы одна группа)", step -> {
            if (app.hbm().getGroupCount() == 0) {
                app.hbm().createGroup(new GroupData("", "group name", "group header", "group footer"));
            }
        });
        var group = app.hbm().getGroupList().get(0);
        var oldRelated = app.hbm().getContactsInGroup(group);
        app.contacts().createContact(contact, group);
        var newRelated = app.hbm().getContactsInGroup(group);
        Allure.step("Проверка успешности изменения количества контактов в группе в БД", step -> {
            Assertions.assertEquals(oldRelated.size() + 1, newRelated.size());
        });

        var extraRelated = newRelated.stream().filter(r -> ! oldRelated.contains(r)).toList();
        var changedId = extraRelated.get(0).id();
        var expectedList = new ArrayList<>(oldRelated);
        expectedList.add(contact.withId(changedId).withPhoto(""));
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

    @ParameterizedTest
    @MethodSource("contactProvider")
    @DisplayName("canCreateMultipleContacts: Тест создания множества контактов с данными из json-файла")
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
        Allure.step("Проверка успешности появления контакта в UI", step -> {
            Assertions.assertEquals(expectedList, newContacts);
        });
    }

    @ParameterizedTest
    @MethodSource("negativeContactProvider")
    @DisplayName("canNotCreateContact: Тест невозможности создания контакта с невалидными символами в поле lastname")
    public void canNotCreateContact(ContactData contact) {
        var oldContacts = app.contacts().getList();
        app.contacts().createContact(contact);
        var newContacts = app.contacts().getList();

        Allure.step("Проверка отсутствия появления контакта в UI", step -> {
            Assertions.assertEquals(newContacts, oldContacts);
        });
    }
}
