package com.miguel.go4lunch_p6;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.android.libraries.places.api.Places;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.miguel.go4lunch_p6.models.JsonResponseDetails;


public class RestaurantDetailsActivity extends AppCompatActivity implements CallRestaurant.CallbacksDetails {

    private ImageView mImageview;
    private TextView name;
    private TextView adress;
    private LinearLayout call;
    private LinearLayout website;
    private LinearLayout like;
    private JsonResponseDetails mJsonResponse;
    private FloatingActionButton fab;
    private static final int REQUEST_CALL = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);

        String apiKey = "AIzaSyAfGC10zfgqg54n-hoMT1GhdoJMWFbUcxU";
        Places.initialize(getApplicationContext(), apiKey);
        mImageview = findViewById(R.id.imageView);
        name = findViewById(R.id.textViewName);
        adress = findViewById(R.id.adress);
        call = findViewById(R.id.buttonCall);
        website = findViewById(R.id.buttonwebsite);
        like = findViewById(R.id.buttonLike);
        fab = findViewById(R.id.fab);
        final JsonResponseDetails jsonResponseDetails = getIntent().getExtras().getParcelable("json");
        if (jsonResponseDetails != null) {
            name.setText(jsonResponseDetails.getResult().getName());
            adress.setText(jsonResponseDetails.getResult().getFormattedAddress());
            Glide.with(this).load(jsonResponseDetails.getResult().getIcon()).into(mImageview);
            website.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(jsonResponseDetails.getResult().getWebsite() == null){
                        Toast.makeText(getBaseContext(), "No Website found", Toast.LENGTH_SHORT).show();
                    }else {
                        Log.i("url", "url du website = " + jsonResponseDetails.getResult().getWebsite());
                        Intent intent = new Intent(getBaseContext(), WebViewActivity.class);
                        intent.putExtra("url", jsonResponseDetails.getResult().getWebsite());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getBaseContext().startActivity(intent);
                    }
                }
            });
            like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("test", "CLICK");
                    fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorgrey)));
                    fab.setImageResource(R.drawable.ic_baseline_check_circle_24green);
                }
            });
            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (jsonResponseDetails.getResult().getFormattedPhoneNumber() != null) {
                        makePhoneCall(jsonResponseDetails.getResult().getFormattedPhoneNumber());
                    }else{
                        Toast.makeText(getBaseContext(),"No Phone number available", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Bundle bundle = getIntent().getExtras();
            String id = bundle.getString("place_id");
            executehttprequestwithretrofit(id);
        }
    }

    private void executehttprequestwithretrofit(String placeid){
        CallRestaurant.fetchRestaurantDetails(this, placeid);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall("tel:" + mJsonResponse.getResult().getFormattedPhoneNumber());
            }else{
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void makePhoneCall(String phonenumber){
        if (phonenumber.length() > 0){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CALL_PHONE}, REQUEST_CALL);
            } else {
                String dial = "tel:" + phonenumber;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }
        }else {
            Toast.makeText(this, "No phone number available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResponse(@Nullable final JsonResponseDetails details) {
        Log.i("position", "lat lng : " + details.getResult());
        Log.i("position", "lat lng : " + details.getResult().getGeometry().getLocation());
        mJsonResponse = details;
        name.setText(details.getResult().getName());
        adress.setText(details.getResult().getFormattedAddress());
        Glide.with(this).load(details.getResult().getIcon()).into(mImageview);
        website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(details.getResult().getWebsite() == null){
                    Toast.makeText(getBaseContext(), "No Website found", Toast.LENGTH_SHORT).show();
                }else {
                    try {
                        Log.i("url", "url du website = " + details.getResult().getWebsite());
                        Intent intent = new Intent(getBaseContext(), WebViewActivity.class);
                        intent.putExtra("url", details.getResult().getWebsite());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getBaseContext().startActivity(intent);
                    }catch (NullPointerException e){
                        Toast.makeText(getBaseContext(), "No Website found", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("test", "CLICK");
                fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorgrey)));
                fab.setImageResource(R.drawable.ic_baseline_check_circle_24green);
            }
        });
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (details.getResult().getFormattedPhoneNumber() != null) {
                    makePhoneCall(details.getResult().getFormattedPhoneNumber());
                }else{
                    Toast.makeText(getBaseContext(),"No Phone number available", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onFailure() {
        Log.i("reponse", "response = failed");

    }

}

