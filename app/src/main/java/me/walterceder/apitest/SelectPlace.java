package me.walterceder.apitest;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by iguest on 5/13/15.
 */
public class SelectPlace extends ListActivity {

    //LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
    ArrayList<String> listPlaces = new ArrayList<String>();

    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    ArrayAdapter<String> adapter;

    private List<locationObj> places;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        places = new ArrayList<locationObj>();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_place);
        Intent intent = getIntent();
        for(int i = 0;i<intent.getIntExtra("size",10);i++){

            places.add(i,(locationObj) intent.getParcelableExtra("place"+i));
        }

        TextView title = (TextView) findViewById(R.id.selectText);
        title.setText("Select Where You Are");

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listPlaces);
        setListAdapter(adapter);
       // Log.i("size",places.size()+"");
        if(places.get(0)==null){
       //     Log.i("size","??? bad");
        }

       // Log.i("size",places.get(0).getDates().size()+"");

        for(int i = 0; i<places.size();i++){
            ///0.000008998719243599958
            listPlaces.add(places.get(i).getName()+" Distance: "+new DecimalFormat("#.##").format((places.get(i).getDistance()))+" meters");
        }

        adapter.notifyDataSetChanged();



    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

     //   Log.i("click",id+"");
        Intent intent = new Intent(this,Result.class);
        intent.putExtra("thing", places.get((int) id));
        startActivity(intent);
    }
}