package ru.training.mantis.manager;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeUtility;
import okhttp3.*;
import ru.training.mantis.manager.developermail.AddUserResponse;
import ru.training.mantis.manager.developermail.GetIdsResponse;
import ru.training.mantis.manager.developermail.GetMessageResponse;
import ru.training.mantis.model.DeveloperMailUser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.CookieManager;
import java.time.Duration;

public class DeveloperMailHelper extends HelperBase {

    public static final MediaType JSON = MediaType.get("application/json");
    OkHttpClient client;


    public DeveloperMailHelper(ApplicationManager manager) {
        super(manager);
        client = new OkHttpClient.Builder().cookieJar(new JavaNetCookieJar(new CookieManager())).build();
    }


    public DeveloperMailUser addUser() {
        RequestBody body = RequestBody.create("", JSON);
        Request request = new Request.Builder()
                .url("https://www.developermail.com/api/v1/mailbox")
                .put(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new RuntimeException("Unexpected code " + response);
            var responseText = response.body().string();
            JsonNode node = new ObjectMapper().readTree(responseText);
            System.out.println("Response data: \n" + node.toPrettyString());
            var addUserResponse = new ObjectMapper().readValue(responseText, AddUserResponse.class);
            if (!addUserResponse.success()) throw new RuntimeException(addUserResponse.errors().toString());
            return addUserResponse.result();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteUser(DeveloperMailUser user) {
        RequestBody body = RequestBody.create("", JSON);
        Request request = new Request.Builder()
                .url(String.format("https://www.developermail.com/api/v1/mailbox/%s", user.name()))
                .header("X-MailboxToken", user.token())
                .delete(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new RuntimeException("Unexpected code " + response);
            JsonNode node = new ObjectMapper().readTree(response.body().string());
            System.out.println("Response data: \n" + node.toPrettyString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String receive(DeveloperMailUser user, Duration duration) {
        var start = System.currentTimeMillis();
        while (System.currentTimeMillis() < start + duration.toMillis()) {
            try {
                var messageIdsReq = get(String.format("https://www.developermail.com/api/v1/mailbox/%s",
                        user.name()), user.token());
                var messageIdsResp = new ObjectMapper().readValue(messageIdsReq, GetIdsResponse.class);
                if (!messageIdsResp.success()) throw new RuntimeException(messageIdsResp.errors().toString());
                if (messageIdsResp.result().size() > 0) {
                    var messageReq = get(String.format("https://www.developermail.com/api/v1/mailbox/%s/messages/%s",
                            user.name(), messageIdsResp.result().get(0)), user.token());
                    var messageResp = new ObjectMapper().readValue(messageReq, GetMessageResponse.class);
                    if (!messageResp.success()) throw new RuntimeException(messageResp.errors().toString());
                    // возвращаемое значение нужно декодировать из quoted-printable:
                    return new String(MimeUtility.decode(
                            new ByteArrayInputStream(messageResp.result().getBytes()),
                            "quoted-printable").readAllBytes());
                }
            } catch (MessagingException | IOException e) {
                throw new RuntimeException(e);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        throw new RuntimeException("No mail");
    }

    private String get(String url, String token) {
        Request request = new Request.Builder()
                .url(url)
                .header("X-MailboxToken", token)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new RuntimeException("Unexpected code " + response);
            JsonNode node = new ObjectMapper().readTree(response.body().string());
            System.out.print("Response data: \n" + node.toPrettyString());
            return response.body().string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

