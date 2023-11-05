package ru.training.mantis.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LoginTests extends TestBase {

    @Test
    void canLoginUi() {
        app.session().login("administrator", "aaaaaaaa");
        Assertions.assertTrue(app.session().isLoggedIn());
    }

    @Test
    void canLoginHttp() {
        app.http().login("administrator", "aaaaaaaa");
        Assertions.assertTrue(app.http().isLoggedIn());
    }

}
