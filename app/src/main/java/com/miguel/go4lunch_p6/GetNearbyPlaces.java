package com.miguel.go4lunch_p6;

import android.os.AsyncTask;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetNearbyPlaces extends AsyncTask<Object,String,String> {

    private GoogleMap mMap;
    private String url;
    private InputStream is;
    private BufferedReader mBufferedReader;
    private StringBuilder mStringBuilder;
    private String Data;

    @Override
    protected String doInBackground(Object... params) {
        mMap = (GoogleMap)params[0];
        url = (String)params[1];

        try{
            URL myurl = new URL(url);
            HttpURLConnection httpURLConnection = (HttpURLConnection)myurl.openConnection();
            httpURLConnection.connect();
            is = httpURLConnection.getInputStream();
            mBufferedReader = new BufferedReader(new InputStreamReader(is));

            String line = "";
            mStringBuilder = new StringBuilder();


            while((line = mBufferedReader.readLine())!= null)
            {
                mStringBuilder.append(line);

            }

            Data = mStringBuilder.toString();


        } catch(IOException e){
            e.printStackTrace();
        }


        return Data;
    }



    @Override
    protected void onPostExecute(String s){
        try {
            JSONObject parentObject = new JSONObject(s);
            JSONArray resultsArray = parentObject.getJSONArray("results");

            for(int i = 0; i<resultsArray.length(); i++)
            {
                JSONObject jsonObject = resultsArray.getJSONObject(i);
                JSONObject locationObject = jsonObject.getJSONObject("geometry").getJSONObject("location");

                String latitude = locationObject.getString("lat");
                String longitude = locationObject.getString("lng");

                JSONObject nameObject = resultsArray.getJSONObject(i);

                String nameRestaurant = nameObject.getString("name");
                String victiny = nameObject.getString("vicinity");
                String rating = nameObject.getString("rating");

                LatLng latLng = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.title(victiny);
                markerOptions.position(latLng);

                mMap.addMarker(markerOptions);


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}