package com.example.hams;
// parent class to log in/out all types of users

public class User{

    private String userName; //username and password are the same
    private String passWord;
    private boolean loggedIn; //variable to check if user is already logged in (might be useless lol idk)

    public User(String user, String pass){
        userName = user;
        passWord = pass;
    }

    public boolean login(String user, String pass){
        if(user.equals(userName) && pass.equals(passWord)){
            System.out.println("Welcome "+userName);
            loggedIn = true;
            return true;
        } else{
            System.out.println("Incorrect username and password combination.");
            loggedIn = false;
            return false;
        }
    }

    public boolean isLoggedIn(){
        return loggedIn;
    }



    public void logout(){
        //will at some point send user back to the login/sign up page
        loggedIn = false;
        System.out.println("Successfully logged out");
    }

    
}