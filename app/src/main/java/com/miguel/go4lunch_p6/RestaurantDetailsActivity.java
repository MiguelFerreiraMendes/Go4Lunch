package com.miguel.go4lunch_p6;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import controler.WorkmatesAdapter;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.miguel.go4lunch_p6.api.UserHelper;
import com.miguel.go4lunch_p6.models.JsonResponseDetails;
import com.miguel.go4lunch_p6.models.RestaurantInfo;
import com.miguel.go4lunch_p6.models.User;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class RestaurantDetailsActivity extends AppCompatActivity implements CallRestaurant.CallbacksDetails {

    private ImageView mImageview;
    private TextView name;
    private TextView adress;
    private LinearLayout call;
    private LinearLayout website;
    private LinearLayout like;
    private FloatingActionButton fab;
    private User mUser;
    private static final int REQUEST_CALL = 1;
    private JsonResponseDetails mJsonResponse;
    private String myuserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private String formattedDate;


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

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        formattedDate = df.format(c);
        getUser();

        final JsonResponseDetails jsonResponseDetails = getIntent().getExtras().getParcelable("json");
        if (jsonResponseDetails != null) {
            getfirebasedata(jsonResponseDetails);
            setparameters(jsonResponseDetails);
            checkworkmates();
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
                Toast.makeText(this, R.string.permissionDenied, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getUser(){
        UserHelper.getUsersCollection().document(myuserID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                mUser = documentSnapshot.toObject(User.class);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
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
            Toast.makeText(this, R.string.NoPhonenumberavailable, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResponse(@Nullable final JsonResponseDetails details) {
        getfirebasedata(details);
        setparameters(details);
        checkworkmates();
    }

    private void getfirebasedata(final JsonResponseDetails jsonResponseDetails) {
        DocumentReference docIdRef = UserHelper.getUsersCollection().document(myuserID).collection("Restaurant").document(jsonResponseDetails.getResult().getName());
        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (!document.exists()) {
                        UserHelper.createRestaurantInfo(myuserID, jsonResponseDetails.getResult().getName(), 0, false);
                    }
                }
            }
        });

        DocumentReference docIdRef2 = FirebaseFirestore.getInstance().collection("Restaurant").document(jsonResponseDetails.getResult().getName());
        docIdRef2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (!document.exists()) {
                        UserHelper.createRestaurant(jsonResponseDetails.getResult().getName());
                    }
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
                        Intent intent = new Intent(getBaseContext(), WebViewActivity.class);
                        intent.putExtra("url", details.getResult().getWebsite());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getBaseContext().startActivity(intent);
                    } catch (NullPointerException e) {
                        Toast.makeText(getBaseContext(), R.string.noWebsiteFound, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        FirebaseFirestore.getInstance().collection("Restaurant").document(mJsonResponse.getResult().getName()).collection(formattedDate).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<User> list = new ArrayList<>();
                    for (DocumentSnapshot document : task.getResult()) {
                        User user = document.toObject(User.class);
                        list.add(user);

                    }
                    setrecyclerview(list);
                }
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUser.getRestaurantInteressed().equals("false")) {
                    FirebaseFirestore.getInstance().collection("Restaurant").document(mJsonResponse.getResult().getName()).collection(formattedDate).document(myuserID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            DocumentSnapshot documentSnapshot1 = task.getResult();
                            if (!documentSnapshot1.exists()) {

                                FirebaseFirestore.getInstance().collection("Restaurant").document(mJsonResponse.getResult().getName()).collection(formattedDate).document(myuserID).set(mUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorgrey)));
                                        fab.setColorFilter(getResources().getColor(R.color.quantum_googgreen400));
                                    }
                                });
                            }
                        }
                    });
                    UserHelper.updateIsInteressed(myuserID, mJsonResponse.getResult().getName());
                    getUser();
                    UserHelper.updateIDofRestaurantInteressed(mJsonResponse.getResult().getPlace_id(), myuserID);
                } else {
                    if (mUser.getRestaurantInteressed().equals(mJsonResponse.getResult().getName())) {
                        Toast.makeText(getBaseContext(), R.string.alreadysignupthisrest, Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getBaseContext(), R.string.alreadysignupanotherrest, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RetrieveLike(details);
            }
        });


        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (details.getResult().getFormattedPhoneNumber() != null) {
                    makePhoneCall(details.getResult().getFormattedPhoneNumber());
                } else {
                    Toast.makeText(getBaseContext(), R.string.noPhoneAvailable, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public void onFailure() {
    }

    private void RetrieveLike(final JsonResponseDetails jsonResponseDetails) {
        final DocumentReference docIdRef = UserHelper.getUsersCollection().document(myuserID).collection("Restaurant").document(jsonResponseDetails.getResult().getName());
        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    RestaurantInfo restaurantInfo = document.toObject(RestaurantInfo.class);
                    if (restaurantInfo.getLike() == 0) {
                        UserHelper.updatelike(myuserID, 1, jsonResponseDetails.getResult().getName());
                        Toast.makeText(getBaseContext(), R.string.likeRegistered, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getBaseContext(), R.string.alreadtlikethisrest, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }


    private void checkworkmates() {

        FirebaseFirestore.getInstance().collection("Restaurant").document(mJsonResponse.getResult().getName()).collection(formattedDate).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<User> list = new ArrayList<>();
                    for (DocumentSnapshot document : task.getResult()) {
                        User user = document.toObject(User.class);
                        list.add(user);
                    }
                    setrecyclerview(list);
                }
            }
        });
    }

    private void setrecyclerview(List<User> listuserinteressed) {

        if (listuserinteressed != null){
            for (int i = 0; i < listuserinteressed.size(); i++ ){
                if (listuserinteressed.get(i).getId().equals(myuserID)){
                    listuserinteressed.remove(i);
                }
            }
            RecyclerView recyclerView = findViewById(R.id.recyclerViewWorkmate);
            recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false));
            WorkmatesAdapter mondapteur = new WorkmatesAdapter(listuserinteressed, getBaseContext(), "a");
            recyclerView.setAdapter(mondapteur);
        }
    }
}


