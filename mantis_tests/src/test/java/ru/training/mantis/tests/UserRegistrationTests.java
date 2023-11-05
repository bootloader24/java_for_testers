package ru.training.mantis.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.training.mantis.common.CommonFunctions;

import java.time.Duration;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class UserRegistrationTests extends TestBase {

    public static Stream<String> randomUserProvider() {
        Supplier<String> randomUser = () -> CommonFunctions.randomString(10);
        return Stream.generate(randomUser).limit(1);
    }

    @ParameterizedTest
    @MethodSource("randomUserProvider")
    void canRegisterUser(String username) {
        var email = String.format("%s@localhost", username);
        var password = "password";
        // создаём пользователя на почтовом сервере:
        app.jamesCli().addUser(email, password);
        // через Selenium в браузере заполняем форму и отправляем запрос на регистрацию:
        app.registration().initRegistration(username, email);
        // ждём почту и забираем письмо:
        var message = app.mail().receive(email, password, Duration.ofSeconds(10)).get(0);
        // из письма извлекаем ссылку:
        var url = app.mail().extractUrl(message);
        // подчищаем папку INBOX на почтовом сервере:
        app.mail().drain(email, password);
        // через Selenium в браузере открываем ссылку и завершаем регистрацию:
        app.registration().completeRegistration(url, username, password);
        // проверяем http-запросами, что пользователь может залогиниться в mantis:
        app.http().login(username, password);
        Assertions.assertTrue(app.http().isLoggedIn());
    }
}
