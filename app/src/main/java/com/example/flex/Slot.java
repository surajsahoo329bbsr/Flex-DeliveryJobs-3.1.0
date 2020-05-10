package com.example.flex;


import androidx.annotation.NonNull;

public class Slot {

    private String userMail;
    private String slotId;
    private int slotFlag;
    private String showDate;
    private String showStartTime;
    private String showWorkHours;
    private String transactionDateTime;
    private String transactionMoney;

    public Slot() {

    }

    Slot(String userMail, String userId, int slotFlag, String showDate, String showStartTime, String showWorkHours) {

        this.userMail=userMail;
        this.slotId=userId;
        this.slotFlag=slotFlag;
        this.showDate=showDate;
        this.showStartTime=showStartTime;
        this.showWorkHours=showWorkHours;

    }

    public String getUserMail() {
        return userMail;
    }

    public String getSlotId() {
        return slotId;
    }

    public int getSlotFlag() {
        return slotFlag;
    }

    public String getShowDate() {
        return showDate;
    }

    public String getShowStartTime() {
        return showStartTime;
    }

    public String getShowWorkHours() {
        return showWorkHours;
    }

    public String getTransactionDateTime() {
        return transactionDateTime;
    }

    public void setTransactionDateTime(String transactionDateTime) {
        this.transactionDateTime = transactionDateTime;
    }

    public String getTransactionMoney() {
        return transactionMoney;
    }

    public void setTransactionMoney(String transactionMoney) {
        this.transactionMoney = transactionMoney;
    }

    @NonNull
    @Override
    public String toString() {

        char[] dateArr= showDate.toCharArray();
        char[] modDateArr=new char[showDate.length()];
        int count=0;

        for (int i=0; i < dateArr.length; i++) {
            if (dateArr[i] == '-')
                count++;
            if (count == 2)
                break;

            modDateArr[i]=dateArr[i];
        }

        String modTime = String.valueOf(modDateArr);
        return modTime + ", " + showStartTime + " | " + showWorkHours;
    }
}