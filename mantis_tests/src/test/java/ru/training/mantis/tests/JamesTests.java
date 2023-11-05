package ru.training.mantis.tests;

import org.junit.jupiter.api.Test;
import ru.training.mantis.common.CommonFunctions;

public class JamesTests extends TestBase {
    @Test
    void canCreateUser() {
        app.jamesCli().addUser(String.format("%s@localhost", CommonFunctions.randomString(7)), "password");
    }
}
