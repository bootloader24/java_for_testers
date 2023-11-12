package ru.training.mantis.manager.developermail;

import ru.training.mantis.model.DeveloperMailUser;

public record AddUserResponse(Boolean success, Object errors, DeveloperMailUser result) {
}
