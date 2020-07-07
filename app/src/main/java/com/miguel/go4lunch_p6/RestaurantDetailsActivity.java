package com.miguel.go4lunch_p6;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.miguel.go4lunch_p6.api.UserHelper;
import com.miguel.go4lunch_p6.models.JsonResponseDetails;
import com.miguel.go4lunch_p6.models.Restaurant;

import java.util.Map;


public class RestaurantDetailsActivity extends AppCompatActivity implements CallRestaurant.CallbacksDetails {

    private ImageView mImageview;
    private TextView name;
    private TextView adress;
    private LinearLayout call;
    private LinearLayout website;
    private LinearLayout like;
    private FloatingActionButton fab;
    private static final int REQUEST_CALL = 1;
    private JsonResponseDetails mJsonResponse;
    private SharedPreferences mSharedPreferences;
    private String myuserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);
        String apiKey = "AIzaSyAfGC10zfgqg54n-hoMT1GhdoJMWFbUcxU";
        Places.initialize(getApplicationContext(), apiKey);
        mSharedPreferences = this.getSharedPreferences(myuserID, MODE_PRIVATE);
        mImageview = findViewById(R.id.imageView);
        name = findViewById(R.id.textViewName);
        adress = findViewById(R.id.adress);
        call = findViewById(R.id.buttonCall);
        website = findViewById(R.id.buttonwebsite);
        like = findViewById(R.id.buttonLike);
        fab = findViewById(R.id.fab);

        final JsonResponseDetails jsonResponseDetails = getIntent().getExtras().getParcelable("json");
        if (jsonResponseDetails != null) {
            getfirebasedata(jsonResponseDetails);
            setparameters(jsonResponseDetails);
        } else {
            Bundle bundle = getIntent().getExtras();
            String id = bundle.getString("place_id");
            executehttprequestwithretrofit(id);
        }
    }


    private void executehttprequestwithretrofit(String placeid) {
        CallRestaurant.fetchRestaurantDetails(this, placeid);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall("tel:" + mJsonResponse.getResult().getFormattedPhoneNumber());
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void makePhoneCall(String phonenumber) {
        if (phonenumber.length() > 0) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
            } else {
                String dial = "tel:" + phonenumber;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }
        } else {
            Toast.makeText(this, "No phone number available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResponse(@Nullable final JsonResponseDetails details) {
        Log.i("position", "lat lng : " + details.getResult());
        Log.i("position", "lat lng : " + details.getResult().getGeometry().getLocation());
        getfirebasedata(details);
        setparameters(details);
    }

    private void getfirebasedata (final JsonResponseDetails jsonResponseDetails) {
        DocumentReference docIdRef = UserHelper.getUsersCollection().document(myuserID).collection("Restaurant").document(jsonResponseDetails.getResult().getName());
        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (!document.exists()) {
                        UserHelper.createRestaurantInfo(myuserID, jsonResponseDetails.getResult().getName(), 0, false);
                        Log.i("docref", "found");
                    }
                } else {
                    Log.d("docref", "Failed with: ", task.getException());
                }
            }
        });
    }

    private void setparameters(final JsonResponseDetails details) {
        mJsonResponse = details;
        name.setText(details.getResult().getName());
        adress.setText(details.getResult().getFormattedAddress());
        try {
            String photo = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + details.getResult().getPhotos().get(0).getPhotoreference() + "&sensor=false&key=AIzaSyAfGC10zfgqg54n-hoMT1GhdoJMWFbUcxU";
            Glide.with(this).load(photo).into(mImageview);
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            String Urlphoto = details.getResult().getIcon();
            Glide.with(this).load(Urlphoto).into(mImageview);
        }
        website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (details.getResult().getWebsite() == null) {
                    Toast.makeText(getBaseContext(), "No Website found", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        Log.i("url", "url du website = " + details.getResult().getWebsite().toString());
                        Intent intent = new Intent(getBaseContext(), WebViewActivity.class);
                        intent.putExtra("url", details.getResult().getWebsite().toString().trim());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getBaseContext().startActivity(intent);
                    } catch (NullPointerException e) {
                        Toast.makeText(getBaseContext(), "No Website found", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSharedPreferences.getString("lastrestaurant", "").equals("")){
                UserHelper.updateIsInteressed(myuserID, true, details.getResult().getName());
                mSharedPreferences.edit()
                        .putString("lastrestaurant", details.getResult().getName())
                        .apply();
                Log.i("test", "CLICK");
                fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorgrey)));
                fab.setColorFilter(getResources().getColor(R.color.quantum_googgreen400));
            }else{
                UserHelper.updateIsInteressed(myuserID, true, details.getResult().getName());
                UserHelper.updateIsInteressed(myuserID, false, mSharedPreferences.getString("lastrestaurant", ""));
                    mSharedPreferences.edit()
                            .putString("lastrestaurant", details.getResult().getName())
                            .apply();
                fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorgrey)));
                fab.setColorFilter(getResources().getColor(R.color.quantum_googgreen400));
                }
            }
        });
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RetrieveLike(details);

           //     RetrieveLike(details.getResult().getName());

            //    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
            //    DatabaseReference uidRef = rootRef.child(myuserID);
            //    ValueEventListener valueEventListener = new ValueEventListener() {
            //        @Override
            //        public void onDataChange(DataSnapshot dataSnapshot) {
            //            if(dataSnapshot.child("like").getValue(int.class).equals(1)) {
            //                Toast.makeText(getBaseContext(), "You already liked this restaurant", Toast.LENGTH_SHORT).show();
            //            } else if (dataSnapshot.child("like").getValue(int.class).equals(1)) {
            //                UserHelper.updatelike(myuserID, 1, details.getResult().getName());
            //                Toast.makeText(getBaseContext(), "Like successfully register", Toast.LENGTH_SHORT).show();
            //            }
            //        }
//
            //        @Override
            //        public void onCancelled(@NonNull DatabaseError databaseError) {
            //            Log.d("DataBaseError", databaseError.getMessage());
            //        }
            //    };
            //    uidRef.addListenerForSingleValueEvent(valueEventListener);
                //               if (UserHelper.getRestaurantinfo(myuserID, "" == 1){
                //                   Toast.makeText(getBaseContext(), "You already liked this restaurant", Toast.LENGTH_SHORT).show();
                //               }else{
                //                   UserHelper.updatelike(myuserID, 1, details.getResult().getName());
                //                   Toast.makeText(getBaseContext(), "Like successfully register", Toast.LENGTH_SHORT).show();
                //               }
                //           }
                //       });
            }});


        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (details.getResult().getFormattedPhoneNumber() != null) {
                    makePhoneCall(details.getResult().getFormattedPhoneNumber());
                } else {
                    Toast.makeText(getBaseContext(), "No Phone number available", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public void onFailure() {
        Log.i("reponse", "response = failed");

    }

    private void RetrieveLike(final JsonResponseDetails jsonResponseDetails){
        final DocumentReference docIdRef = UserHelper.getUsersCollection().document(myuserID).collection("Restaurant").document(jsonResponseDetails.getResult().getName());
        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
         //           DocumentSnapshot document = task.getResult();
         //           Log.i("test",  "" + docIdRef.get());
         //           Map<String, Object> documentdata = document.getData();
         //           Log.i("test",  "" + documentdata.get(Restaurant.class));
                } else {
                    Log.d("docref", "Failed with: ", task.getException());
                }
            }
        });
    }
}

