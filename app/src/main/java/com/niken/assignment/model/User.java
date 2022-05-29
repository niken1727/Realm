package com.niken.assignment.model;

import io.realm.RealmObject;

public class User extends RealmObject {
    private String name = "";
    private String profileImage = "";
    private String email = "";
    private String phoneNumber = "";
    private String gender = "";
    private String typeOfDevice = "";
    private Long createdDate = 0L;

    public User() {

    }

    public User(String name, String profileImage, String email, String phoneNumber, String gender, String typeOfDevice, Long createdDate) {
        this.name = name;
        this.profileImage = profileImage;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.typeOfDevice = typeOfDevice;
        this.createdDate = createdDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getTypeOfDevice() {
        return typeOfDevice;
    }

    public void setTypeOfDevice(String typeOfDevice) {
        this.typeOfDevice = typeOfDevice;
    }

    public Long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", profileImage='" + profileImage + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", gender='" + gender + '\'' +
                ", typeOfDevice='" + typeOfDevice + '\'' +
                ", createdDate='" + createdDate + '\'' +
                '}';
    }
}
