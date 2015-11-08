package com.sinduran.androidrecord;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.sinduran.heartrate.HeartRateDatabaseAdapter;
import com.sinduran.heartrate.R;

public class HistoryActivity extends ListActivity {

    HeartRateDatabaseAdapter heartRateDatabaseAdapter;
    private String[] HR_DATA;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        heartRateDatabaseAdapter = new HeartRateDatabaseAdapter(this);
        HR_DATA = heartRateDatabaseAdapter.getAllData();
        setListAdapter(new ArrayAdapter<String>(this, R.layout.history_list_item, HR_DATA));
        ListView listView = getListView();
        listView.setTextFilterEnabled(true);

    }
}
