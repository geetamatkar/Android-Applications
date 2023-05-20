package com.example.weatherapp;

public class WeeklyWeather {

    private static final String TAG = "SevenDayWeather";
    private Integer weekImageView;
    private String weekDescription, weekDateTime;
    private String weekTemperature, weekPrecipitation, weekUvIndex;
    private String weekMorningTemp, weekAfternoonTemp, weekEveningTemp, weekNightTemp;

    WeeklyWeather(String weekDateTime, Integer weekImageView, String weekTemperature, String weekDescription, String weekPrecipitation, String weekUvIndex, String weekMorningTemp, String weekAfternoonTemp, String weekEveningTemp, String weekNightTemp) {
        this.weekDateTime = weekDateTime;
        this.weekTemperature = weekTemperature;
        this.weekDescription = weekDescription;
        this.weekPrecipitation = weekPrecipitation;
        this.weekUvIndex = weekUvIndex;
        this.weekMorningTemp = weekMorningTemp;
        this.weekAfternoonTemp = weekAfternoonTemp;
        this.weekEveningTemp = weekEveningTemp;
        this.weekNightTemp = weekNightTemp;
        this.weekImageView = weekImageView;
    }

    public String getWeekDateTime() {
        return weekDateTime;
    }

    public Integer getWeekImageView() {
        return weekImageView;
    }

    public String getWeekTemperature() {
        return weekTemperature;
    }

    public String getWeekDescription() {
        return weekDescription;
    }

    public String getWeekPrecipitation() {
        return weekPrecipitation;
    }

    public String getWeekUvIndex() {
        return weekUvIndex;
    }

    public String getWeekMorningTemp() {
        return weekMorningTemp;
    }

    public String getWeekAfternoonTemp() {
        return weekAfternoonTemp;
    }

    public String getWeekEveningTemp() {
        return weekEveningTemp;
    }

    public String getWeekNightTemp() {
        return weekNightTemp;
    }
}
