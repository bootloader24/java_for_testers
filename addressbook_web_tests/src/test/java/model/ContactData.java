package model;

public record ContactData(String last_name, String first_name, String address, String email, String phone_home) {

    public ContactData() {
        this("", "", "", "", "");
    }

    public ContactData WithLastnameAndFirstname(String last_name, String first_name) {
        return new ContactData(last_name, first_name, this.address, this.email, this.phone_home);
    }

}
