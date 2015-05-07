package me.walterceder.apitest;

        import java.io.BufferedReader;
        import java.io.IOException;
        import java.io.InputStream;
        import java.io.InputStreamReader;

        import org.apache.http.HttpEntity;
        import org.apache.http.HttpResponse;
        import org.apache.http.client.HttpClient;
        import org.apache.http.client.methods.HttpGet;
        import org.apache.http.impl.client.DefaultHttpClient;
        import org.apache.http.protocol.BasicHttpContext;
        import org.apache.http.protocol.HttpContext;
        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import android.app.Activity;
        import android.content.Intent;
        import android.location.Criteria;
        import android.location.Location;
        import android.location.LocationListener;
        import android.location.LocationManager;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.provider.Settings;
        import android.util.Log;
        import android.view.View;
        import android.view.View.OnClickListener;
        import android.widget.TextView;
        import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener, LocationListener {

    private TextView latituteField;
    private TextView longitudeField;
    private LocationManager locationManager;
    private String provider = "";
    private double lat;
    private double lng;
    private int offset = 0;
    private boolean searching = true; //if the search is ongoing
    private double lat5= 0.0000018;
    private double lng5= 0.0000018;
    Intent intent;


    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        intent = new Intent(this,Result.class);
       // findViewById(R.id.button).setOnClickListener(this);
       // latituteField = (TextView) findViewById(R.id.latituteField);
      //  longitudeField = (TextView) findViewById(R.id.longitudeField);
         locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);



        boolean enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

// check if enabled and if not send user to the GSP settings
// Better solution would be to display a dialog and suggesting to
// go to the settings
        if (!enabled) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
        Location location = locationManager.getLastKnownLocation(provider);

        // Initialize the locationObj fields
        if (location != null) {
            System.out.println("Provider " + provider + " has been selected.");
            onLocationChanged(location);
        } else {
       //     latituteField.setText("Location not available");
       //     longitudeField.setText("Location not available");
        }
        new LongRunningGetIO().execute();
    }

    @Override

    public void onClick(View arg0) {

    //    Button b = (Button)findViewById(R.id.button);

      //  b.setClickable(false);
     //   b.setText("Loading");


       //     new LongRunningGetIO().execute();



    }

    @Override
    protected void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }
    @Override
    public void onLocationChanged(Location location) {
         lat =  (location.getLatitude());
         lng = (location.getLongitude());
        lng5 = 1/((111111.0*Math.cos(lng))*5);
       // latituteField.setText(String.valueOf(lat));
        //longitudeField.setText(String.valueOf(lng));
    }
    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();

    }
    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }
    private class LongRunningGetIO extends AsyncTask <Void, Void, String> {
        protected String getASCIIContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException {

            InputStream in = entity.getContent();

            StringBuffer out = new StringBuffer();
            int n = 1;
            while (n>0) {
                byte[] b = new byte[4096];

                n =  in.read(b);

                if (n>0) out.append(new String(b, 0, n));

            }

            return out.toString();

        }
//http://dev.socrata.com/docs/queries.html
        //http://dev.socrata.com/foundry/#/data.kingcounty.gov/f29f-zza5

        //https://data.kingcounty.gov/resource/f29f-zza5.json?$where=latitude%20%3E%2047.6250635431%20AND%20latitude%20%3C%2047.7250635431
        @Override

        protected String doInBackground(Void... params) {


                    HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            String text = null;
            InputStream inputStream = null;
            //while(text==null||text.equals("[ ]\n")){
                //double minLat = lat-(lat5*offset);
               // double maxLat = lat+(lat5*offset);
               // double minLng = lng-(lng5*offset);
               // double maxLng = lng+(lng5*offset);
                double minLat = lat-(1.0/169);
                double maxLat = lat+(1.0/169);
                double minLng = lng+(1.0/169);
                double maxLng = lng-(1.0/169);
                offset++;
                String call = "https://data.kingcounty.gov/resource/f29f-zza5.json?$where=latitude%20%3E%20"+minLat+"%20AND%20latitude%20%3C%20"+maxLat+"%20AND%20longitude%20%3C%20"+minLng+"%20AND%20longitude%20%3E%20"+maxLng;
                Log.i("url",call);
                HttpGet httpGet = new HttpGet(call);
                try {

                    HttpResponse response = httpClient.execute(httpGet, localContext);

                    HttpEntity entity = response.getEntity();

                    //text = getASCIIContentFromEntity(entity);

                    inputStream = entity.getContent();
                    // json is UTF-8 by default
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();

                    String line = null;
                    while ((line = reader.readLine()) != null)
                    {
                        sb.append(line + "\n");
                        Log.i("DescEarly", line);
                    }
                    text = sb.toString();


                } catch (Exception e) {
                    return e.getLocalizedMessage();

                }
            //}




            return text;

        }

        protected void onPostExecute(String results) {
            if (results!=null) {
                String stuff = "";

                try {
                    JSONArray jArray = new JSONArray(results);
                    double[] coords = new double[jArray.length()*2];
                    //http://stackoverflow.com/questions/9605913/how-to-parse-json-in-android
                    int extra = jArray.length();

                    for (int i=0; i < jArray.length(); i++)
                    {
                        try {

                            JSONObject oneObject = jArray.getJSONObject(i);
                            // Pulling items from the array
                            coords[i] = oneObject.getDouble("latitude");
                            coords[i+extra] = oneObject.getDouble("longitude");

                        } catch (JSONException e) {
                            // Oops
                        }
                    }

                    double distance = 10000;

                    int marker = 0;
                    for(int i=0; i < jArray.length(); i++){

                        double tempD = Math.sqrt(Math.pow(lat-coords[i],2)+Math.pow(lng-coords[i+extra],2));
                        if(tempD<distance){
                            distance=  tempD;
                            marker = i;
                        }

                    }
                    JSONObject name = jArray.getJSONObject(marker);
                    String targetName=name.getString("inspection_business_name");
                    locationObj curr = new locationObj(targetName);
                    for (int i=0; i < jArray.length(); i++)
                    {
                        try {

                            JSONObject oneObject = jArray.getJSONObject(i);
                            if(oneObject.getString("inspection_business_name").equals(targetName)){
                                if(oneObject.has("inspection_date")&&oneObject.has("inspection_result")&&oneObject.has("violation_description")&&oneObject.has("violation_type")){
                                    curr.addDate(oneObject.getString("inspection_date"));
                                    curr.addResult(oneObject.getString("inspection_result"));
                                    curr.addDesc(oneObject.getString("violation_description"));
                                    Log.i("Desc", oneObject.getString("violation_description"));
                                    curr.addType(oneObject.getString("violation_type"));
                                }


                            }
                            // Pulling items from the array


                        } catch (JSONException e) {
                            // Oops
                        }
                    }
                    intent.putExtra("thing",curr);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                //intent.putExtra("json",results);
                //startActivity(intent);
                //!!!!!!
                //EditText et = (EditText)findViewById(R.id.editText);

                //et.setText(results);

            }

         //   Button b = (Button)findViewById(R.id.button);

          // b.setClickable(true);

        }

    }
}