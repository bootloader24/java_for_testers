package ru.training.addressbook.model;

public record ContactData(String id,
                          String lastName,
                          String firstName,
                          String address,
                          String email,
                          String phoneHome,
                          String phoneMobile,
                          String phoneWork,
                          String phoneSecondary,
                          String photo
) {

    public ContactData() {
        this("", "", "", "", "", "", "", "", "", "");
    }

    public ContactData withId(String id) {
        return new ContactData(id, this.lastName, this.firstName, this.address, this.email, this.phoneHome, this.phoneMobile, this.phoneWork, this.phoneSecondary, this.photo);
    }

    public ContactData withLastnameAndFirstname(String lastName, String firstName) {
        return new ContactData(this.id, lastName, firstName, this.address, this.email, this.phoneHome, this.phoneMobile, this.phoneWork, this.phoneSecondary, this.photo);
    }

    public ContactData withLastname(String lastName) {
        return new ContactData(this.id, lastName, this.firstName, this.address, this.email, this.phoneHome, this.phoneMobile, this.phoneWork, this.phoneSecondary, this.photo);
    }

    public ContactData withFirstname(String firstName) {
        return new ContactData(this.id, this.lastName, firstName, this.address, this.email, this.phoneHome, this.phoneMobile, this.phoneWork, this.phoneSecondary, this.photo);
    }

    public ContactData withAddress(String address) {
        return new ContactData(this.id, this.lastName, this.firstName, address, this.email, this.phoneHome, this.phoneMobile, this.phoneWork, this.phoneSecondary, this.photo);
    }

    public ContactData withEmail(String email) {
        return new ContactData(this.id, this.lastName, this.firstName, this.address, email, this.phoneHome, this.phoneMobile, this.phoneWork, this.phoneSecondary, this.photo);
    }

    public ContactData withPhoneHome(String phoneHome) {
        return new ContactData(this.id, this.lastName, this.firstName, this.address, this.email, phoneHome, this.phoneMobile, this.phoneWork, this.phoneSecondary, this.photo);
    }

    public ContactData withPhoneMobile(String phoneMobile) {
        return new ContactData(this.id, this.lastName, this.firstName, this.address, this.email, this.phoneHome, phoneMobile, this.phoneWork, this.phoneSecondary, this.photo);
    }

    public ContactData withPhoneWork(String phoneWork) {
        return new ContactData(this.id, this.lastName, this.firstName, this.address, this.email, this.phoneHome, this.phoneMobile, phoneWork, this.phoneSecondary, this.photo);
    }

    public ContactData withPhoneSecondary(String phoneSecondary) {
        return new ContactData(this.id, this.lastName, this.firstName, this.address, this.email, this.phoneHome, this.phoneMobile, this.phoneWork, phoneSecondary, this.photo);
    }

    public ContactData withPhoto(String photo) {
        return new ContactData(this.id, this.lastName, this.firstName, this.address, this.email, this.phoneHome, this.phoneWork, this.phoneWork, this.phoneSecondary, photo);
    }

}
