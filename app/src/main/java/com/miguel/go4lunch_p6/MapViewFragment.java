package com.miguel.go4lunch_p6;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;


import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.facebook.FacebookSdk.getApplicationContext;

// A simple {@link Fragment} subclass.
 // Activities that contain this fragment must implement the
 // {@link MapViewFragment.OnFragmentInteractionListener} interface
 // to handle interaction events.

public class MapViewFragment extends Fragment {

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;
    private boolean mLocationPermissionGranted;
    private MapView mMapView;
    private GoogleMap googleMap;
    private PlacesClient mPlaceClient;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Object mLastKnownLocation;
    private double latitude;
    private double longitude;

    public static MapViewFragment newInstance(){
        MapViewFragment fragment = new MapViewFragment();
        return (fragment);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map_view, container, false);

        String apiKey = "AIzaSyAfGC10zfgqg54n-hoMT1GhdoJMWFbUcxU";
        Places.initialize(getApplicationContext(), apiKey);
        mPlaceClient = Places.createClient(getContext());
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

        mMapView = rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);



        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                getLocationPermission();
                updateLocationUI();
                getDeviceLocation();
                getRestaurants(mPlaceClient);

                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        Intent myIntent = new Intent(getContext(), RestaurantDetailsActivity.class);
                        myIntent.putExtra("place_id", marker.getId());
                        getContext().startActivity(myIntent);
                        return true;
                    }
                });

            }
        });

        return rootView;
    }



    private void getRestaurants(PlacesClient placesClient) {


        List<Place.Field> placeFieldsName = Arrays.asList(Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ID);
        FindCurrentPlaceRequest request = FindCurrentPlaceRequest.newInstance(placeFieldsName);

        if (ContextCompat.checkSelfPermission(getContext(), ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Task<FindCurrentPlaceResponse> placeResponse = placesClient.findCurrentPlace(request);
            placeResponse.addOnCompleteListener(new OnCompleteListener<FindCurrentPlaceResponse>() {
                @Override
                public void onComplete(@NonNull Task<FindCurrentPlaceResponse> task) {
                if (task.isSuccessful()){
                    FindCurrentPlaceResponse response = task.getResult();
                    for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()) {
                        for(Place.Type type : placeLikelihood.getPlace().getTypes()){
                            Log.e("test",placeLikelihood.getPlace().getName()+"/"+ type.name());
                        }

                        googleMap.addMarker(new MarkerOptions()
                        .position(placeLikelihood.getPlace().getLatLng()));

                    }
                } else {
                    Exception exception = task.getException();
                    if (exception instanceof ApiException) {
                        ApiException apiException = (ApiException) exception;
                        Log.e("OnFailure", "Place not found: " + apiException.getStatusCode());
                    }
                }
            }});
        } else {
            getLocationPermission();
        }
    }



    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        Log.i("permission", "Permission : " + mLocationPermissionGranted);
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
            mLastKnownLocation = null;
            getLocationPermission();
        }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getDeviceLocation() {
        Log.i("position", "ajustement de la position" + mLocationPermissionGranted);
        try {
            if (mLocationPermissionGranted) {
                Log.i("position", "OnPermissionGranted");
                LocationRequest locationRequest = LocationRequest.create();
                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                locationRequest.setInterval(5);

                mFusedLocationProviderClient.requestLocationUpdates(locationRequest, new LocationCallback(){

                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        Location location = locationResult.getLastLocation();
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        Log.i("locationbias","circle:20000@" + longitude+"," + latitude);
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                new LatLng(latitude,
                                        longitude), 16));
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
}
