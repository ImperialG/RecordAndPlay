package com.sinduran.androidrecord;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.sinduran.heartrate.R;

public class HistoryActivity extends ListActivity {


    static final String[] HR_DATA = new String[] { "100", "80", "65", "87", "91", "100", "69" };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(new ArrayAdapter<String>(this, R.layout.history_list_item, HR_DATA));
        ListView listView = getListView();
        listView.setTextFilterEnabled(true);

    }
}
