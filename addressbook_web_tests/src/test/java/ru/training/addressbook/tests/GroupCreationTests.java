package ru.training.addressbook.tests;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.training.addressbook.common.CommonFunctions;
import ru.training.addressbook.model.GroupData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GroupCreationTests extends TestBase {

    public static List<GroupData> groupProvider() throws IOException {
        var result = new ArrayList<GroupData>();
//        for (var name : List.of("", "group name")) {
//            for (var header : List.of("", "group header")) {
//                for (var footer : List.of("", "group footer")) {
//                    result.add(new GroupData().withName(name).withHeader(header).withFooter(footer));
//                }
//            }
//        }

//        first json variant:
//        ObjectMapper mapper = new ObjectMapper();
//        var value = mapper.readValue(new File("groups.json"), new TypeReference<List<GroupData>>() {});

//        second json variant:
        var json = Files.readString(Paths.get("groups.json"));
        ObjectMapper mapper = new ObjectMapper();
        var value = mapper.readValue(json, new TypeReference<List<GroupData>>() {});

//        third json variant (by-line reading):
//        var json = "";
//        try (var reader = new FileReader("groups.json");
//        var bufreader = new BufferedReader(reader)
//        ) {
//            var line = bufreader.readLine();
//            while (line != null) {
//                json = json + line;
//                line = bufreader.readLine();
//            }
//        }
//        ObjectMapper mapper = new ObjectMapper();
//        var value = mapper.readValue(json, new TypeReference<List<GroupData>>() {});

//        YAML variant:
//        var mapper = new YAMLMapper();
//        var value = mapper.readValue(new File("groups.yaml"), new TypeReference<List<GroupData>>() {});

//        XML variant:
//        var mapper = new XmlMapper();
//        var value = mapper.readValue(new File("groups.xml"), new TypeReference<List<GroupData>>() {});

        result.addAll(value);
        return result;
    }

    public static Stream<GroupData> RandomGroupsProvider() {
        Supplier<GroupData> randomGroup = () -> new GroupData()
                .withName(CommonFunctions.randomString(10))
                .withHeader(CommonFunctions.randomString(20))
                .withFooter(CommonFunctions.randomString(30));
        return Stream.generate(randomGroup).limit(1);
    }

    public static List<GroupData> negativeGroupProvider() {
        var result = new ArrayList<GroupData>(List.of(
                new GroupData("", "group' name", "", "")));
        return result;
    }

    @ParameterizedTest
    @MethodSource("groupProvider")
    public void canCreateMultipleGroups(GroupData group) {
        var oldGroups = app.groups().getList();
        app.groups().createGroup(group);
        var newGroups = app.groups().getList();
        Comparator<GroupData> compareById = (o1, o2) -> {
            return Integer.compare(Integer.parseInt(o1.id()), Integer.parseInt(o2.id()));
        };
        newGroups.sort(compareById);
        var expectedList = new ArrayList<>(oldGroups);
        expectedList.add(group.withId(newGroups.get(newGroups.size() - 1).id()).withHeader("").withFooter(""));
        expectedList.sort(compareById);
        Assertions.assertEquals(expectedList, newGroups);
    }

    @ParameterizedTest
    @MethodSource("RandomGroupsProvider")
    public void canCreateGroup(GroupData group) {
        var oldGroups = app.hbm().getGroupList();
        app.groups().createGroup(group);
        var newGroups = app.hbm().getGroupList();
//        Comparator<GroupData> compareById = (o1, o2) -> {
//            return Integer.compare(Integer.parseInt(o1.id()), Integer.parseInt(o2.id()));
//        };
//        newGroups.sort(compareById);
//        var maxId = newGroups.get(newGroups.size() - 1).id();
        var extraGroups = newGroups.stream().filter(g -> ! oldGroups.contains(g)).toList();
        var newId = extraGroups.get(0).id();
        var expectedList = new ArrayList<>(oldGroups);
//        expectedList.add(group.withId(maxId));
        expectedList.add(group.withId(newId));
//        expectedList.sort(compareById);
//        Assertions.assertEquals(expectedList, newGroups);
        Assertions.assertEquals(Set.copyOf(expectedList), Set.copyOf(newGroups));

        var newUiGroups = app.groups().getList();
//        newUiGroups.sort(compareById);
//        на основе expectedList создаём новый ожидаемый список групп с пустыми значениями полей, которые не читаются из UI
        var expectedUiList = new ArrayList<>(expectedList);
        expectedUiList.replaceAll(groups -> groups.withHeader("").withFooter(""));
        Assertions.assertEquals(Set.copyOf(expectedUiList), Set.copyOf(newUiGroups));
    }

    @ParameterizedTest
    @MethodSource("negativeGroupProvider")
    public void canNotCreateGroup(GroupData group) {
        var oldGroups = app.groups().getList();
        app.groups().createGroup(group);
        var newGroups = app.groups().getList();
        Assertions.assertEquals(newGroups, oldGroups);
    }
}