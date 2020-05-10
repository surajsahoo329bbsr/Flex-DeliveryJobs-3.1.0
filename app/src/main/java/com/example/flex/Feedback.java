package com.example.flex;

public class Feedback {

    private String feedbackId;
    private String userMail;
    private String feedback;
    private String rating;
    private String date;

    Feedback(String userId, String userMail, String rating, String feedback, String date)
    {
        this.feedbackId=userId;
        this.userMail = userMail;
        this.rating = rating;
        this.feedback = feedback;
        this.date=date;
    }

    public String getFeedbackId() {
        return feedbackId;
    }

    public String getUserMail()
    {
        return userMail;
    }

    public String getFeedback()
    {
        return feedback;
    }

    public String getRating()
    {
        return rating;
    }

    public String getDate() {
        return date;
    }
}