package com.example.flex;

public class DrivingLicense {

    private String licenseId;
    private String userMail;
    private String userName;
    private String LicenseNumber;
    private String userDOB;
    private String userAddress;
    private String LicenseIssueDate;
    private String LicenseExpiryDate;

    //Constructor
    //Function Overloading(Look it up)

    public DrivingLicense() {

    }

    //Another constructor to initialize our variables
    DrivingLicense(String userId, String userMail, String userName, String LicenseNumber, String userDOB, String userAddress, String LicenseIssueDate, String LicenseExpiryDate) {
        this.licenseId=userId;
        this.userMail=userMail;
        this.userName = userName;
        this.LicenseNumber = LicenseNumber;
        this.userDOB = userDOB;
        this.userAddress = userAddress;
        this.LicenseIssueDate=LicenseIssueDate;
        this.LicenseExpiryDate=LicenseExpiryDate;
    }

    public String getLicenseId() {
        return licenseId;
    }

    public String getUserMail() {
        return userMail;
    }

    public String getUserName() {
        return userName;
    }

    public String getLicenseNumber() {
        return LicenseNumber;
    }

    public String getUserDOB() {
        return userDOB;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public String getLicenseIssueDate() {
        return LicenseIssueDate;
    }

    public String getLicenseExpiryDate() {
        return LicenseExpiryDate;
    }

}