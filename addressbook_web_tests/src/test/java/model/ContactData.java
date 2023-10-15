package model;

public record ContactData(String id, String lastName, String firstName, String address, String email,
                          String phoneHome, String photo) {

    public ContactData() {
        this("", "", "", "", "", "", "");
    }

    public ContactData withId(String id) {
        return new ContactData(id, this.lastName, this.firstName, this.address, this.email, this.phoneHome, this.photo);
    }

    public ContactData withLastnameAndFirstname(String lastName, String firstName) {
        return new ContactData(this.id, lastName, firstName, this.address, this.email, this.phoneHome, this.photo);
    }

    public ContactData withLastname(String lastName) {
        return new ContactData(this.id, lastName, this.firstName, this.address, this.email, this.phoneHome, this.photo);
    }

    public ContactData withFirstname(String firstName) {
        return new ContactData(this.id, this.lastName, firstName, this.address, this.email, this.phoneHome, this.photo);
    }

    public ContactData withAddress(String address) {
        return new ContactData(this.id, this.lastName, this.firstName, address, this.email, this.phoneHome, this.photo);
    }

    public ContactData withEmail(String email) {
        return new ContactData(this.id, this.lastName, this.firstName, this.address, email, this.phoneHome, this.photo);
    }

    public ContactData withPhoneHome(String phoneHome) {
        return new ContactData(this.id, this.lastName, this.firstName, this.address, this.email, phoneHome, this.photo);
    }

    public ContactData withPhoto(String photo) {
        return new ContactData(this.id, this.lastName, this.firstName, this.address, this.email, this.phoneHome, photo);
    }

}
