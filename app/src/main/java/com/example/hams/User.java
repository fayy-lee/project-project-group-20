package com.example.hams;
// parent class to log in/out all types of users

public class User{

    private String userName; //username and password are the same
    private String passWord;
    private boolean loggedIn; //variable to check if user is already logged in (might be useless lol idk)
    String status;


    public User(){
        status = "Pending";
    }
    public User(String user, String pass){
        userName = user;
        passWord = pass;
        status = "Pending"; //automatically pending
    }

    public String getUserName(){
        return userName;
    }

    public String getPassWord(){
        return passWord;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public boolean login(String user, String pass){
        //check if the account is activated
        if(status.equals("Pending")){
            System.out.println("Account pending approval. Unable to login at this time.");
            return false;
        }
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

    public String getStatus(){
        return status;
    }

    public void setStatus(String stat){
        status = stat;
    }



    public void logout(){
        //will at some point send user back to the login/sign up page
        loggedIn = false;
        System.out.println("Successfully logged out");
    }

    
}