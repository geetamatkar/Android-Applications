package com.example.weatherapp;

public class CurrentWeather {

    private static final String TAG = "DailyWeather";
    private Integer image;
    private String day,dateTime;
    private String temper, constrains;
    CurrentWeather(String day, String dateTime, Integer image, String temper, String constrains) {
        this.day = day;
        this.dateTime = dateTime;
        this.image = image;
        this.temper = temper;
        this.constrains = constrains;
    }
    public Integer getImage() {
        return image;
    }

    public String getDay() {
        return day;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getTemper() {
        return temper;
    }

    public String getConstrains() {
        return constrains;
    }
}
