package com.kimadrian.kazilink;

public class User {
    public String userName, email, phoneNumber, profession;

    public User(){//A constructor if one wants to create a User Object without any parameters.

    }
    //A constructor that will receive be used to create a user with all the details as the parameters.
    public User(String userName, String email, String phoneNumber, String profession){
        this.userName = userName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.profession = profession;
    }
}
