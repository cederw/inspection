package me.walterceder.apitest;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iguest on 5/13/15.
 */
public class SelectList extends ListActivity {

    //LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
    ArrayList<String> listItems = new ArrayList<String>();

    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        List<locationObj> places = new ArrayList<locationObj>();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Intent intent = getIntent();
        for(int i = 0;i<intent.getIntExtra("size",10);i++){

            places.add((locationObj) intent.getParcelableExtra("thing"+i));
        }

        TextView title = (TextView) findViewById(R.id.textView);
        title.setText("Select Where You Are");

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        setListAdapter(adapter);

        for(int i = 0; i<places.size();i++){
            listItems.add(places.get(i).getName()+" Distance: "+(places.get(i).getDistance()/0.000008998719243599958)+" meters");
        }

        adapter.notifyDataSetChanged();

    }
}