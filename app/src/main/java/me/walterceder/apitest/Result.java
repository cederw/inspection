package me.walterceder.apitest;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Result extends Activity {

    //LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
    ArrayList<String> listItems=new ArrayList<String>();

    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Intent intent = getIntent();
        locationObj results = (locationObj)intent.getParcelableExtra("thing");
        TextView title = (TextView)findViewById(R.id.textView);
        String stuff = results.getName();
        title.setText(stuff);
        //adapter=new ArrayAdapter<String>(this,
       //         android.R.layout.simple_list_item_1,
       //        listItems);
       // setListAdapter(adapter);


        List dates = results.getDates();
        List result = results.getResults();
        List desc = results.getDesc();
        List type = results.getType();
        Log.i("dates",dates.size()+"");
        Log.i("result",result.size()+"");
        Log.i("desc",desc.size()+"");
        Log.i("type",type.size()+"");
        //sizes are not always the same

        for(int i = 0; i< 20;i++){

           // listItems.add((String)dates.get(i));
           // adapter.notifyDataSetChanged();

        }

    }







}
