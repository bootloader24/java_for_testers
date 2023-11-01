package ru.training.addressbook.manager.hbm;

import com.ctc.wstx.evt.WstxEventReader;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Date;

@Entity
@Table(name = "addressbook")
public class ContactRecord {
    @Id
    @Column(name = "id")
    public int id;
    @Column(name = "firstname")
    public String firstName;
    public String middlename = "";
    @Column(name = "lastname")
    public String lastName;
    public String nickname = "";
    public String company = "";
    public String title = "";
    public String address;
    @Column(name = "home")
    public String phoneHome;
    @Column(name = "mobile")
    public String phoneMobile;
    @Column(name = "work")
    public String phoneWork;
    public String fax = "";
    public String email;
    public String email2;
    public String email3;
    public String im = "";
    public String im2 = "";
    public String im3 = "";
    public String homepage = "";
    public int bday = 0;
    public String bmonth = "";
    public String byear = "";
    public int aday = 0;
    public String amonth = "";
    public String ayear = "";
    public String address2 = "";
    @Column(name = "phone2")
    public String phoneSecondary;
    public String notes = "";
    public String photo;

    public ContactRecord() {
    }

    public ContactRecord(int id, String lastName, String firstName, String address, String email, String email2, String email3, String phoneHome, String phoneMobile, String phoneWork, String phoneSecondary, String photo) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.address = address;
        this.email = email;
        this.email2 = email2;
        this.email3 = email3;
        this.phoneHome = phoneHome;
        this.phoneMobile = phoneMobile;
        this.phoneWork = phoneWork;
        this.phoneSecondary = phoneSecondary;
        this.photo = photo;
    }
}