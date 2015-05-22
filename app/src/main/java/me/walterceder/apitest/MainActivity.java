package me.walterceder.apitest;

        import java.io.BufferedReader;
        import java.io.IOException;
        import java.io.InputStream;
        import java.io.InputStreamReader;
        import java.util.ArrayList;
        import java.util.Collections;
        import java.util.List;

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
        import android.view.View;
        import android.view.View.OnClickListener;
        import android.widget.TextView;
        import android.widget.Toast;

public class MainActivity extends Activity implements LocationListener {

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
      // getPlaces();
    }



    @Override
    protected void onResume() {
        super.onResume();
        getPlaces();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
     //   getPlaces();
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
       // Log.i("place",lat+" "+lng);
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

    public void getPlaces(){
        intent = new Intent(this,SelectPlace.class);
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
         //       Log.i("url",call);
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
                      //  Log.i("DescEarly", line);
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
                List<locationObj> places = new ArrayList<locationObj>();
                try {
                    JSONArray jArray = new JSONArray(results);
                    //http://stackoverflow.com/questions/9605913/how-to-parse-json-in-android








                    locationObj curr = new locationObj(jArray.getJSONObject(0).getString("inspection_business_name"));
                    for (int i=0; i < jArray.length(); i++)
                    {
                        try {

                            JSONObject oneObject = jArray.getJSONObject(i);
                           // if(oneObject.getString("inspection_business_name").equals(targetName)){
                                if(oneObject.has("inspection_date")&&oneObject.has("inspection_result")&&oneObject.has("violation_description")&&oneObject.has("violation_type")){
                                    if(!curr.getName().equals(oneObject.getString("inspection_business_name"))){
                                        places.add(curr);
                                      //  Log.i("size",curr.getName());
                                        curr = new locationObj(oneObject.getString("inspection_business_name"));
                                    }

                                    curr.addDate(oneObject.getString("inspection_date"));
                                    curr.addResult(oneObject.getString("inspection_result"));
                                    curr.addDesc(oneObject.getString("violation_description"));
                                  //  Log.i("Desc", oneObject.getString("violation_description"));
                                    curr.addType(oneObject.getString("violation_type"));
                                    double tempLat = oneObject.getDouble("latitude");
                                    double tempLng = oneObject.getDouble("longitude");
                                    //old distance calculation
                                    //curr.addDistance(Math.sqrt(Math.pow(lat-tempLat,2)+Math.pow(lng-tempLng,2))/0.000008998719243599958);
                                    //new distance calculation
                                    curr.addDistance(haversine(lat,lng,tempLat,tempLng));

                                }


                            //}
                            // Pulling items from the array


                        } catch (JSONException e) {
                            // Oops
                        }
                    }
                    places.add(curr); //i think this is the one on the end
                   // Log.i("test",places.size()+"");
                    Collections.sort(places);
                  //  Log.i("test",places.size()+"");
                   // Log.i("test",places.get(0).getName());
                    for(int i = 0;i<places.size()&&i<30;i++){
                        intent.putExtra("place"+i,places.get(i));
                    }
                    if(places.size()<30){
                        intent.putExtra("size",places.size());
                    } else{
                        intent.putExtra("size",30);
                    }

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

        /**
         * Calculates the distance in km between two lat/long points
         * using the haversine formula
         */
        public  double haversine(
                double lat1, double lng1, double lat2, double lng2) {
            int r = 6371; // average radius of the earth in km
            double dLat = Math.toRadians(lat2 - lat1);
            double dLon = Math.toRadians(lng2 - lng1);
            double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                    Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                            * Math.sin(dLon / 2) * Math.sin(dLon / 2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            double d = r * c;
          //  Log.i("Distance",d+"");
            //convert ot m
            return d*1000.0;
        }

        private double distance(double lat1, double lon1, double lat2, double lon2) {
              double theta = lon1 - lon2;
              double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
              dist = Math.acos(dist);
            	  dist = rad2deg(dist);
            	  dist = dist * 60 * 1.1515;
            	  //convert to KM
                    dist = dist * 1.609344;
                    //convert to m
                    dist = dist/1000.0;
              //      Log.i("Distance",dist+"");
            	  return (dist);
            	}

                	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
                	/*::  This function converts decimal degrees to radians             :*/
                	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
                private double deg2rad(double deg) {
            	  return (deg * Math.PI / 180.0);
            	}

                	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
                	/*::  This function converts radians to decimal degrees             :*/
                	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
                private double rad2deg(double rad) {
            	  return (rad * 180 / Math.PI);
            	}

    }
}