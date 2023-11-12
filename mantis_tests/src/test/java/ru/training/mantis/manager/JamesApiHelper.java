package ru.training.mantis.manager;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;

import java.io.IOException;

public class JamesApiHelper extends HelperBase {

    public static final MediaType JSON = MediaType.get("application/json");
    OkHttpClient client;

    public JamesApiHelper(ApplicationManager manager) {
        super(manager);
        client = new OkHttpClient.Builder().build();
    }

    public void addUser(String email, String password) {
        RequestBody body = RequestBody.create(String.format("{\"password\":\"%s\"}", password), JSON);
        Request request = new Request.Builder()
                .url(String.format("%s/users/%s", manager.property("james.apiBaseUrl"), email))
                .put(body)
                .build();
        sendRequest(request);
    }

    public void drainInbox(String email) {
        Request request = new Request.Builder()
                .url(String.format(
                        "%s/users/%s/mailboxes/INBOX/messages", manager.property("james.apiBaseUrl"), email))
                .delete()
                .build();
        sendRequest(request);
    }

    public void deleteUser(String email) {
        Request request = new Request.Builder()
                .url(String.format("%s/users/%s", manager.property("james.apiBaseUrl"), email))
                .delete()
                .build();
        sendRequest(request);
    }

    private void sendRequest(Request request) {
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new RuntimeException("Unexpected code " + response);
            // Если возвращается ответ, то выводим его на консоль для отладочных целей:
            if (response.body() != null) {
                JsonNode node = new ObjectMapper().readTree(response.body().string());
                System.out.println("Response data: \n" + node.toPrettyString());
                if (node.has("taskId")) {
                    String taskId = node.get("taskId").textValue().replaceAll("\"", "");
                    // вызываем вспомогательный метод печати статуса задачи:
                    printTaskDetails(taskId);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void printTaskDetails(String taskId) {
        Request request = new Request.Builder()
                .url(String.format("%s/tasks/%s", manager.property("james.apiBaseUrl"), taskId))
                .get()
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new RuntimeException("Unexpected code " + response);
            if (response.body() != null) {
                JsonNode node = new ObjectMapper().readTree(response.body().string());
                System.out.println(node.toPrettyString());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
