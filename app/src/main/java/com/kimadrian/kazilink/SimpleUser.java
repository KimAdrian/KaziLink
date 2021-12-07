package com.kimadrian.kazilink;

public class SimpleUser {
    private String userName, userPassword;

    public SimpleUser(String userName, String userPassword){
        this.userName = userName;
        this.userPassword = userPassword;

        setUserName(userName);
        setUserPassword(userPassword);
    }

    public void setUserName(String userName){
        this.userName = userName;
    }

    public void setUserPassword(String userPassword){
        this.userPassword = userPassword;
    }
    public String getUserName(){
        return userName;
    }

    public String getUserPassword(){
        return userPassword;
    }
    @Override
    public String toString() {
        return "SimpleUser{" +
                "userName='" + userName + '\'' +
                ", userPassword='" + userPassword + '\'' +
                '}';
    }
}
