package com.sinduran.heartrate;

import java.util.Date;

public class HeartRate {
    private int heartRate;
    private Date time;

    public HeartRate(int heartRate) {
        this.heartRate = heartRate;
        time = new Date();
    }

    public int getHeartRate(){
        return heartRate;
    }

    public String getTime(){
        return time.toString();
    }
}
