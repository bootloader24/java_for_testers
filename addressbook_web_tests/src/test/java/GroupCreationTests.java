import org.junit.jupiter.api.Test;

public class GroupCreationTests extends TestBase {

    @Test
    public void canCreateGroup() {
        openGroupsPage();
        createGroup("group1", "group1 header", "group1 footer");
    }

    @Test
    public void canCreateGroupWithEmptyName() {
        openGroupsPage();
        createGroup("", "", "");
    }
}
