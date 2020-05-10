package com.example.flex;

public class User {

    private String userId;
    private String userName;
    private String userMail;
    private String userPhone;
    private String userUpiId;
    private int userDLFlag ;
    private int userPhotoFlag ;

    //Constructor
    //Function Overloading(Look it up)

    //Another constructor to initialize our varibales
    User(String userId, String userName, String userMail, String userPhone, String userUpiId, int userDLFlag, int userPhotoFlag) {
        this.userId = userId;
        this.userName = userName;
        this.userMail = userMail;
        this.userPhone = userPhone;
        this.userDLFlag = userDLFlag;
        this.userUpiId = userUpiId;
        this.userPhotoFlag = userPhotoFlag;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserMail() {
        return userMail;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public String getUserUpiId() {return userUpiId;}

    public int getUserDLFlag() { return userDLFlag; }

    public int getUserPhotoFlag() { return userPhotoFlag; }
}