package com.sinduran.androidrecord;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.sinduran.heartrate.R;

public class HistoryActivity extends ListActivity {


    static final String[] HR_DATA = new String[] { "97 bpm - 10/10/15", "80 bpm - 10/10/15", "65 bpm - 9/10/15", "87 bpm - 4/10/15", "91 bpm - 1/10/15" };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(new ArrayAdapter<String>(this, R.layout.history_list_item, HR_DATA));
        ListView listView = getListView();
        listView.setTextFilterEnabled(true);

    }
}
