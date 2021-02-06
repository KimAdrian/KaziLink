package com.kimadrian.kazilink;

public class User {
    public String userName, email, phoneNumber, profession, userDescription, imageUrl;

    public User(){//A constructor if one wants to create a User Object without any parameters.
    }
    //A constructor that will receive be used to create a user with all the details as the parameters.
    public User(String userName, String email, String phoneNumber, String profession, String userDescription, String imageUrl){
        this.userName = userName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.profession = profession;
        this.userDescription = userDescription;
        this.imageUrl = imageUrl;
    }

    //Getters and setters for the instance variables.
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getUserDescription() {
        return userDescription;
    }

    public void setUserDescription(String userDescription) {
        this.userDescription = userDescription;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
