package ru.training.mantis.tests;

import io.swagger.client.model.UserAddResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.training.mantis.common.CommonFunctions;
import ru.training.mantis.model.UserData;

import java.time.Duration;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class UserRegistrationTests extends TestBase {

    public static Stream<String> randomUserProvider() {
        Supplier<String> randomUser = () -> CommonFunctions.randomString(10);
        return Stream.generate(randomUser).limit(5);
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

    @ParameterizedTest
    @MethodSource("randomUserProvider")
    void canRegisterUserViaApi(String username) {
        var email = String.format("%s@localhost", username);
        var password = "password";
        // создаём пользователя на почтовом сервере с помощью API:
        app.jamesApi().addUser(email, password);
        // через REST API отправляем запрос на регистрацию
        // и чтобы после теста можно было удалить пользователя по его id, сохраняем результат в переменную:
        UserAddResponse addResponse = app.mantisRestApi()
                .createUser(new UserData().withUserName(username).withEmail(email));
        // ждём почту и забираем письмо:
        var message = app.mail().receive(email, password, Duration.ofSeconds(10)).get(0);
        // из письма извлекаем ссылку:
        var url = app.mail().extractUrl(message);
        // подчищаем папку INBOX на почтовом сервере:
        app.jamesApi().drainInbox(email);
        // через Selenium в браузере открываем ссылку и завершаем регистрацию:
        app.registration().completeRegistration(url, username, password);
        // проверяем http-запросами, что пользователь может залогиниться в mantis:
        app.http().login(username, password);
        Assertions.assertTrue(app.http().isLoggedIn());

        // после теста удаляем пользователя из Mantis и его почтовый ящик из James через API
        app.mantisRestApi().deleteUser(addResponse.getUser().getId());
        app.jamesApi().deleteUser(email);
    }
}
