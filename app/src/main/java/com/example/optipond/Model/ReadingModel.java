package com.example.optipond.Model;

public class ReadingModel {
    String waterPercentage;
    String phValue;
    String number;



    public ReadingModel(String waterPercentage, String phValue, String number) {
        this.waterPercentage = waterPercentage;
        this.phValue = phValue;
        this.number = number;
    }

    public String getWaterPercentage() {
        return waterPercentage;
    }

    public void setWaterPercentage(String waterPercentage) {
        this.waterPercentage = waterPercentage;
    }

    public String getPhValue() {
        return phValue;
    }

    public void setPhValue(String phValue) {
        this.phValue = phValue;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
