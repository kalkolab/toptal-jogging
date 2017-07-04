package com.toptal.jogging.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * Created by Artem on 03.07.2017.
 */
public class Run {
    private int id;

    private int userId;

    //time in seconds
    private long time;

    //distance in km
    private float distance;

    private Date date;

    private Location location;

    //weather description obtained from weather provider
    private String weather;

    public Run() {
    }

    public Run(long time, float distance, Date date, Location location) {
        this.time = time;
        this.distance = distance;
        this.date = date;
        this.location = location;
    }

    public Run(int id, int userId, long time, float distance, Date date, Location location, String weather) {
        this.id = id;
        this.userId = userId;
        this.time = time;
        this.distance = distance;
        this.date = date;
        this.location = location;
        this.weather = weather;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public float getAverageSpeed() {
        return distance == 0 || time == 0
                ? 0
                :distance/time*3600;
    }
}
