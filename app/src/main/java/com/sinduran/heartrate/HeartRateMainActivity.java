package com.sinduran.heartrate;

import android.app.Activity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rick on 02/11/2015.
 */
public class HeartRateMainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_alt);

        LineChart chart = (LineChart) findViewById(R.id.hrChart);

        LineData data = new LineData(getXAxis(), getYAxis());
        chart.setData(data);
        chart.invalidate();
    }

    private LineDataSet getYAxis() {
        List<Entry> entries = new ArrayList<>();
        for(int i = 0 ; i < 3 ; ++i){
            entries.add(new Entry((float)(100 * Math.random()), i));
        }
        LineDataSet lineDataSet = new LineDataSet(entries, "1");
        return lineDataSet;
    }

    private String[] getXAxis() {
        return new String[]{"10", "20", "30"};
    }
}
