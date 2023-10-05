import model.ContactData;
import org.junit.jupiter.api.Test;

public class ContactCreationTests extends TestBase {

    @Test
    public void canCreateContact() {
        openAddNewPage();
        createContact(new ContactData("Ivanov", "Andrey", "Lenina, 15", "andrey121@gmail.com", "+7-123-456-7890"));
    }

    @Test
    public void canCreateContactWithFirstnameAndLastname() {
        openAddNewPage();
        createContact(new ContactData().WithLastnameAndFirstname("Petrov", "Sergey"));
    }

}
