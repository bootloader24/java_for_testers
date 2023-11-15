package ru.training.mantis.tests;

import org.junit.jupiter.api.Test;
import ru.training.mantis.common.CommonFunctions;
import ru.training.mantis.model.IssueData;

public class IssueCreationTests extends TestBase {

    @Test
    void canCreateIssueViaRest() {
        app.mantisRestApi().createIssue(new IssueData()
                .withSummary(CommonFunctions.randomString(10))
                .withDescription(CommonFunctions.randomString(20))
                .withProject(1L)
                .withCategory(1L));
    }

    @Test
    void canCreateIssueViaSoap() {
        app.mantisSoapApi().createIssue(new IssueData()
                .withSummary(CommonFunctions.randomString(10))
                .withDescription(CommonFunctions.randomString(20))
                .withProject(1L)
                .withCategory(1L));
    }
}