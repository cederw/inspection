package me.walterceder.apitest;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Result extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Intent intent = getIntent();
        String results = intent.getStringExtra("json");
        EditText et = (EditText)findViewById(R.id.editText2);
        String stuff = "";
        et.setText(results);
        try {
            JSONArray jArray = new JSONArray(results);
            //http://stackoverflow.com/questions/9605913/how-to-parse-json-in-android
            JSONObject name = jArray.getJSONObject(0);
            stuff=stuff+name.getString("inspection_business_name");
            for (int i=0; i < jArray.length(); i++)
            {
                try {
                    JSONObject oneObject = jArray.getJSONObject(i);
                    // Pulling items from the array
                    String oneObjectsItem = oneObject.getString("inspection_date");
                    String oneObjectsItem2 = oneObject.getString("inspection_result");
                    String oneObjectsItem3 = oneObject.getString("violation_description");
                    String oneObjectsItem4 = oneObject.getString("violation_type");

                    stuff = stuff+oneObjectsItem+"\n"+oneObjectsItem2+"\n"+oneObjectsItem3+"\n"+oneObjectsItem4+"\n";
                } catch (JSONException e) {
                    // Oops
                }
            }
            et.setText(stuff);
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
