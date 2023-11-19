package ru.training.addressbook.tests;

import io.qameta.allure.Allure;
import org.junit.jupiter.api.DisplayName;
import ru.training.addressbook.model.GroupData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Random;

@DisplayName("GroupRemovalTests: Тесты удаления групп")
public class GroupRemovalTests extends TestBase {

    @Test
    @DisplayName("canRemoveGroup: Тест удаления одной группы")
    public void canRemoveGroup() {
//        if (app.groups().getCount() == 0) {
//            app.groups().createGroup(new GroupData("", "group name", "group header", "group footer"));
//        }
        Allure.step("Проверка предусловия (должна существовать хотя бы одна группа)", step -> {
            if (app.hbm().getGroupCount() == 0) {
                app.hbm().createGroup(new GroupData("", "group name", "group header", "group footer"));
            }
        });
        var oldGroups = app.hbm().getGroupList();
        var rnd = new Random();
        var index = rnd.nextInt(oldGroups.size());
        app.groups().removeGroup(oldGroups.get(index));
        var newGroups = app.hbm().getGroupList();
        var expectedList = new ArrayList<>(oldGroups);
        expectedList.remove(index);
        Allure.step("Проверка успешности удаления группы в БД", step -> {
            Assertions.assertEquals(expectedList, newGroups);
        });
    }

    @Test
    @DisplayName("canRemoveAllGroupsAtOnce: Тест удаления сразу всех групп")
    void canRemoveAllGroupsAtOnce() {
        Allure.step("Проверка предусловия (должна существовать хотя бы одна группа)", step -> {
            if (app.hbm().getGroupCount() == 0) {
                app.hbm().createGroup(new GroupData("", "group name", "group header", "group footer"));
            }
        });
        app.groups().removeAllGroups();
        Allure.step("Проверка успешности удаления всех групп в БД", step -> {
            Assertions.assertEquals(0, app.hbm().getGroupCount());
        });
    }
}
