package com.example.streamapi_data_filtering;


import java.time.LocalDate;


public class Record {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String gender;
    private String country;
    private String domainName;
    private LocalDate birthDate;

    public Record(int id, String firstName, String lastName, String email, String gender, String country, String domainName, String birthDate) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.gender = gender;
        this.country = country;
        this.domainName = domainName;
        this.birthDate = LocalDate.parse(birthDate);
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }

    public String getCountry() {
        return country;
    }

    public String getDomainName() {
        return domainName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }
}

