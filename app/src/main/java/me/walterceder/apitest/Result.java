package me.walterceder.apitest;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


public class Result extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Intent intent = getIntent();
        locationObj results = (locationObj)intent.getParcelableExtra("thing");
        EditText et = (EditText)findViewById(R.id.editText2);
        String stuff = results.getName();
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
            stuff = stuff+dates.get(i)+"\n"+result.get(i)+"\n"+desc.get(i)+"\n"+type.get(i)+"\n";
        }
        et.setText(stuff);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_result, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
