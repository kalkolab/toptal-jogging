package com.toptal.jogging.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

/**
 * Created by Artem on 08.07.2017.
 */
public class WeeklyRuns {
    private int userId;

    private LocalDate weekStart;
    private LocalDate weekEnd;

    //duration in seconds
    private long duration;

    //distance in km
    private float distance;

    private int numberOfRuns;

    public WeeklyRuns(int userId, LocalDate weekStart, LocalDate weekEnd, int numberOfRuns, long duration, float distance) {
        this.userId = userId;
        this.weekStart = weekStart;
        this.weekEnd = weekEnd;
        this.duration = duration;
        this.distance = distance;
        this.numberOfRuns = numberOfRuns;
    }

    public int getUserId() {
        return userId;
    }

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    public LocalDate getWeekStart() {
        return weekStart;
    }

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    public LocalDate getWeekEnd() {
        return weekEnd;
    }

    public long getDuration() {
        return duration;
    }

    public float getDistance() {
        return distance;
    }

    public int getNumberOfRuns() {
        return numberOfRuns;
    }

    public float getAverageSpeed() {
        return duration == 0 ? 0 : distance / duration * 3600;
    }
}
