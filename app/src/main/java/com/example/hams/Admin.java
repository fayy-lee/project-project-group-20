package com.example.hams;
public class Admin extends User{

    //one specific set of Admin credentials
    private static String userName = "Admin20";
    private static String passWord = "AdminPass123";

    public Admin() {
        super(userName, passWord);
    }

    public String getUser(){
        return userName;
    }

    public String getPass(){
        return passWord;
    }

    
}
