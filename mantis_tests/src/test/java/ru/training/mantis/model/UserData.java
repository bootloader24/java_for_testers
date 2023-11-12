package ru.training.mantis.model;

// здесь не стал добавлять все поля, так как они не обязательны при создании пользователя
public record UserData(String username, String email) {

    public UserData() {
        this("", "");
    }

    public UserData withUserName(String username) {
        return new UserData(username, this.email);
    }

    public UserData withEmail(String email) {
        return new UserData(this.username, email);
    }

}
