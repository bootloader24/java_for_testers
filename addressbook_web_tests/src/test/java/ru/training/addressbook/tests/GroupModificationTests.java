package ru.training.addressbook.tests;

import ru.training.addressbook.common.CommonFunctions;
import ru.training.addressbook.model.GroupData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;
import java.util.Set;

public class GroupModificationTests extends TestBase {

    @Test
    void canModifyGroup() {
        if (app.hbm().getGroupCount() == 0) {
            app.hbm().createGroup(new GroupData("", "group name", "group header", "group footer"));
        }
        var oldGroups = app.hbm().getGroupList();
        var rnd = new Random();
        var index = rnd.nextInt(oldGroups.size());
        var testData = new GroupData().withName(CommonFunctions.randomString(8));
        app.groups().modifyGroup(oldGroups.get(index), testData);
        var newGroups = app.hbm().getGroupList();
        var expectedList = new ArrayList<>(oldGroups);
        expectedList.set(index, testData.withId(oldGroups.get(index).id()));
//        Comparator<GroupData> compareById = (o1, o2) -> {
//            return Integer.compare(Integer.parseInt(o1.id()), Integer.parseInt(o2.id()));
//        };
//        newGroups.sort(compareById);
//        expectedList.sort(compareById);
//        Assertions.assertEquals(expectedList, newGroups);
        Assertions.assertEquals(Set.copyOf(expectedList), Set.copyOf(newGroups));

        var newUiGroups = app.groups().getList();
//        newUiGroups.sort(compareById);
//        на основе expectedList создаём новый ожидаемый список групп с пустыми значениями полей, которые не читаются из UI
        var expectedUiList = new ArrayList<>(expectedList);
        expectedUiList.replaceAll(groups -> groups.withHeader("").withFooter(""));
//        Assertions.assertEquals(expectedUiList, newUiGroups);
        Assertions.assertEquals(Set.copyOf(expectedUiList), Set.copyOf(newUiGroups));
    }

}
