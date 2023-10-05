package tests;

import model.ContactData;
import org.junit.jupiter.api.Test;

public class ContactRemovalTests extends TestBase {
    @Test
    public void canRemoveContact() {
        if (!app.contacts().isContactPresent()) {
            app.contacts().createContact(new ContactData("Ivanov", "Andrey", "Lenina, 15", "andrey121@gmail.com", "+7-123-456-7890"));
        }
        app.contacts().removeContact();
    }

}
