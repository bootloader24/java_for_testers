import org.junit.jupiter.api.Test;

public class GroupRemovalTests extends TestBase {

    @Test
    public void canRemoveGroup() {
        openGroupsPage();
        if (!isGroupPresent()) {
            createGroup("group1", "group1 header", "group1 footer");
        }
        removeGroup();
    }

}
