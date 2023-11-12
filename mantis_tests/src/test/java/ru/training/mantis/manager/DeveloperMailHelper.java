package ru.training.mantis.manager;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import ru.training.mantis.manager.developermail.AddUserResponse;
import ru.training.mantis.model.DeveloperMailUser;

import java.io.IOException;
import java.net.CookieManager;

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
            if (!response.isSuccessful())  throw new RuntimeException("Unexpected code " + response);
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
            if (!response.isSuccessful())  throw new RuntimeException("Unexpected code " + response);
            JsonNode node = new ObjectMapper().readTree(response.body().string());
            System.out.println("Response data: \n" + node.toPrettyString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

