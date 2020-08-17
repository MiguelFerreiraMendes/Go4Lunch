package com.miguel.go4lunch_p6;

import android.content.Context;
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

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.miguel.go4lunch_p6.models.User;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.facebook.FacebookSdk.getApplicationContext;

public class MapViewFragment extends Fragment implements CallRestaurant.Callbacks, Filterable {

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;
    private boolean mLocationPermissionGranted;
    private MapView mMapView;
    private GoogleMap googleMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private double latitude;
    private double longitude;
    private String position;
    private String formattedDate;
    private String myuserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private List<Marker> listmarker = new ArrayList<>();
    private List<Marker> listfilterfull;


    public static MapViewFragment newInstance() {
        MapViewFragment fragment = new MapViewFragment();
        return (fragment);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        rlp.setMargins(0, 0, 0, 80);
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        formattedDate = df.format(c);
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                googleMap.setIndoorEnabled(false);

                getLocationPermission();
                getDeviceLocation();
                updateLocationUI();
            }
        });

        return rootView;
    }


    private void executehttprequestwithretrofit(String location) {
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
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getDeviceLocation() {
        try {
            if (mLocationPermissionGranted) {
                LocationRequest locationRequest = LocationRequest.create();
                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

                mFusedLocationProviderClient.requestLocationUpdates(locationRequest, new LocationCallback() {

                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        Location location = locationResult.getLastLocation();
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        position = latitude + "," + longitude;
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                new LatLng(latitude,
                                        longitude), 16));
                        mFusedLocationProviderClient.removeLocationUpdates(this);
                        executehttprequestwithretrofit(position);
                    }
                }, Looper.getMainLooper());
            }
        } catch (SecurityException e) {
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
        FragmentTransaction fragmentTransaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.listviewfragment, newfragment);
        fragmentTransaction.commit();
        assert nearbySearch != null;
        checkpeopleinteressed(nearbySearch);

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Intent myIntent = new Intent(getContext(), RestaurantDetailsActivity.class);
                myIntent.putExtra("place_id", marker.getSnippet());
                Objects.requireNonNull(getContext()).startActivity(myIntent);
                return true;
            }
        });
    }


    @Override
    public void onFailure() {

    }

    private void checkpeopleinteressed(final JsonResponse mJsonResponse) {


        for (int i = 0; i < mJsonResponse.getResult().size(); i++) {
            String position = mJsonResponse.getResult().get(i).getGeometry().getLocation().getLat() + "," + mJsonResponse.getResult().get(i).getGeometry().getLocation().getLng();
            String[] latlong = position.split(",");
            double latitude = Double.parseDouble(latlong[0]);
            double longitude = Double.parseDouble(latlong[1]);
            final LatLng location = new LatLng(latitude, longitude);
            final int finalI = i;

            final int finalI1 = i;

            FirebaseFirestore.getInstance().collection("Restaurant").document(mJsonResponse.getResult().get(i).getName()).collection(formattedDate).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        List<User> list = new ArrayList<>();
                        for (DocumentSnapshot document : task.getResult()) {
                            User user = document.toObject(User.class);
                            list.add(user);
                        }

                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).getId().equals(myuserID)) {
                                list.remove(i);
                            }
                        }
                        if (list.size() > 0) {
                            listmarker.add(
                                    googleMap.addMarker(new MarkerOptions()
                                            .position(location)
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                                            .title(mJsonResponse.getResult().get(finalI1).getName())
                                            .snippet(mJsonResponse.getResult().get(finalI).getPlace_id())));
                        } else {
                            listmarker.add(
                                    googleMap.addMarker(new MarkerOptions()
                                            .position(location)
                                            .title(mJsonResponse.getResult().get(finalI1).getName())
                                            .snippet(mJsonResponse.getResult().get(finalI).getPlace_id())));
                        }
                        listfilterfull = new ArrayList<>(listmarker);
                    }
                }
            });
        }
    }

    @Override
    public Filter getFilter() {
        return listfilter;
    }

    private Filter listfilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            final List<Marker> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(listfilterfull);
            } else {
                final String filterPattern = constraint.toString().toLowerCase().trim();
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        for (Marker marker : listfilterfull) {
                            if (marker.getTitle().toLowerCase().contains(filterPattern)) {
                                filteredList.add(marker);
                            }
                        }
                    }
                });
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            listmarker.clear();
            googleMap.clear();
            listmarker.addAll((List) results.values);

            for (int i = 0; i < listmarker.size(); i++){
                googleMap.addMarker(new MarkerOptions()
                        .position(listmarker.get(i).getPosition())
                        .snippet(listmarker.get(i).getSnippet())
                        .title(listmarker.get(i).getTitle()));
            }
        }
    };
}
