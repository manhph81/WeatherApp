package com.example.myapplication;

public class Weather {
    public static String date;
    public static String state;
    public static String status;
    public static String image;
    public static String maxTemp;
    public static  String minTemp;
    public Weather(String date, String status, String image, String maxTemp, String minTemp){
        this.date = date;
        this.status = status;
        this.image = image;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
    }
}
