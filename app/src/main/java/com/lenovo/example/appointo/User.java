package com.lenovo.example.appointo;

public class User {

    String userName,email,password,type;

    public User(){

    }

    public User(String userName, String email, String password, String type) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.type = type;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getType() {
        return type;
    }
}
