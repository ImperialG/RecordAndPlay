package com.sinduran.heartrate;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HeartRate {
    private int heartRate;
    private Date time;
    private SimpleDateFormat ft = new SimpleDateFormat ("dd/MM/yyyy 'at' hh:mm:ss a");

    public HeartRate(int heartRate) {
        this.heartRate = heartRate;
        time = new Date();
    }

    public int getHeartRate(){
        return heartRate;
    }

    public String getTime(){
        return ft.format(time);
    }
}
