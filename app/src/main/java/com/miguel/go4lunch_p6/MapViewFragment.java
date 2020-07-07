package com.miguel.go4lunch_p6;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.facebook.FacebookSdk.getApplicationContext;

public class MapViewFragment extends Fragment implements CallRestaurant.Callbacks {

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;
    private boolean mLocationPermissionGranted;
    private MapView mMapView;
    private GoogleMap googleMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private double latitude;
    private double longitude;
    private String position;
    private JsonResponse mJsonResponse;



    public static MapViewFragment newInstance(){
        MapViewFragment fragment = new MapViewFragment();
        return (fragment);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map_view, container, false);

        String apiKey = "AIzaSyAfGC10zfgqg54n-hoMT1GhdoJMWFbUcxU";
        Places.initialize(getApplicationContext(), apiKey);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        mMapView = rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        View locationButton = ((View) mMapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        rlp.setMargins(0, 1500, 0, 0);



        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                Log.i("loop", "new map");
                googleMap = mMap;
                googleMap.setIndoorEnabled(false);

                getLocationPermission();
                updateLocationUI();
                getDeviceLocation();
            }
        });

        return rootView;
    }




    private void executehttprequestwithretrofit(String location){
        Log.i("position", "position dans le execute with retrofit" + position);
        CallRestaurant.fetchRestaurant(this, location);
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    private void updateLocationUI() {
        try {
            if (mLocationPermissionGranted) {
                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
            googleMap.setMyLocationEnabled(false);
            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
            getLocationPermission();
        }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getDeviceLocation() {
        try {
            if (mLocationPermissionGranted) {
                LocationRequest locationRequest = LocationRequest.create();
                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

                mFusedLocationProviderClient.requestLocationUpdates(locationRequest, new LocationCallback(){

                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        Location location = locationResult.getLastLocation();
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        position = latitude + "," + longitude;
                        Log.i("position", "position dans le getdevice" + position);
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                new LatLng(latitude,
                                        longitude), 16));
                        mFusedLocationProviderClient.removeLocationUpdates(this);
                        executehttprequestwithretrofit(position);
                    }
                }, Looper.getMainLooper());
            }
        } catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onResponse(@Nullable final JsonResponse nearbySearch) {
        Bundle args = new Bundle();
        args.putParcelable("json", nearbySearch);
        args.putString("position", position);
        ListViewFragment newfragment = new ListViewFragment();
        newfragment.setArguments(args);
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.listviewfragment, newfragment);
        fragmentTransaction.commit();

        for (int i = 0; i < nearbySearch.getResult().size(); i++) {
            Log.i("loop", "numéro de boucle  + " + i + nearbySearch.getResult().size());
        Log.i("test", "name resto = " +nearbySearch.getResult().get(i).getName());
        String position = nearbySearch.getResult().get(i).getGeometry().getLocation().getLat() + "," + nearbySearch.getResult().get(i).getGeometry().getLocation().getLng();
        String[] latlong = position.split(",");
        double latitude = Double.parseDouble(latlong[0]);
        double longitude = Double.parseDouble(latlong[1]);
        LatLng location = new LatLng(latitude, longitude);
        googleMap.addMarker(new MarkerOptions()
                .position(location)
                .snippet(nearbySearch.getResult().get(i).getPlace_id()));

        Log.i("loop", "marker added + " + location);

            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    Intent myIntent = new Intent(getContext(), RestaurantDetailsActivity.class);
                    myIntent.putExtra("place_id", marker.getSnippet());
                    Log.i("placeID", marker.getSnippet());
                    getContext().startActivity(myIntent);
                    return true;
                }
            });
    }
    }

    @Override
    public void onFailure() {
        Log.e("RETROFIT ERROR","OnFailure");

    }
}
