package ru.training.mantis.manager;

import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.Configuration;
import io.swagger.client.api.IssuesApi;
import io.swagger.client.api.UserApi;
import io.swagger.client.auth.ApiKeyAuth;
import io.swagger.client.model.Identifier;
import io.swagger.client.model.Issue;
import io.swagger.client.model.User;
import io.swagger.client.model.UserAddResponse;
import ru.training.mantis.model.UserData;
import ru.training.mantis.model.IssueData;

public class MantisRestApiHelper extends HelperBase {

    public MantisRestApiHelper(ApplicationManager manager) {
        super(manager);
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        ApiKeyAuth Authorization = (ApiKeyAuth) defaultClient.getAuthentication("Authorization");
        Authorization.setApiKey(manager.property("apiKey"));
    }

    public UserAddResponse createUser(UserData userData) {
        UserApi apiInstance = new UserApi();
        User user = new User(); // User | The user to add.
        user.setUsername(userData.username());
        user.setEmail(userData.email());
        try {
            UserAddResponse response = apiInstance.userAdd(user);
            System.out.println("Response of user creation: \n" + response);
            return response;
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteUser(Long id) {
        UserApi apiInstance = new UserApi();
        try {
            apiInstance.userDelete(id);
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void createIssue(IssueData issueData) {
        Issue issue = new Issue();
        issue.setSummary(issueData.summary());
        issue.setDescription(issueData.description());
        var projectId = new Identifier();
        projectId.setId(issueData.project());
        issue.setProject(projectId);
        var categoryId = new Identifier();
        categoryId.setId(issueData.category());
        issue.setCategory(categoryId);

        IssuesApi apiInstance = new IssuesApi();
        try {
            apiInstance.issueAdd(issue);
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
    }
}
