package com.example.myditail.modelclass;

public class FireModel {
    String name,surname,gender,age,contact,email,password,link;



    public FireModel(String name, String surname, String gender, String age, String contact, String email, String password, String s) {
        this.name = name;
        this.surname = surname;
        this.gender = gender;
        this.age = age;
        this.contact = contact;
        this.email = email;
        this.password = password;
        link = s;
    }

    String key;
    public FireModel(String name, String surname, String gender, String age, String contact, String email, String password, String link, String key) {
        this.name = name;
        this.surname = surname;
        this.gender = gender;
        this.age = age;
        this.contact = contact;
        this.email = email;
        this.password = password;
        this.link = link;
        this.key = key;
    }

    public FireModel(String name1, String surname1, String gender, String age1, String contact1, String email1, String password1) {
        this.name = name1;
        this.surname = surname1;
        this.gender = gender;
        this.age = age1;
        this.contact = contact1;
        this.email = email1;
        this.password = password1;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
