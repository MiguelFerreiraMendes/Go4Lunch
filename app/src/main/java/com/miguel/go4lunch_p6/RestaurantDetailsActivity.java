package com.miguel.go4lunch_p6;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;


public class RestaurantDetailsActivity extends AppCompatActivity implements CallRestaurant.Callbacks {

    private PlacesClient mPlaceClient;
    private ImageView mImageview;
    private TextView name;
    private TextView adress;
    private static final String fields = "formatted_address, icon, name, rating, opening_hours, website, formatted_phone_number";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);

        String apiKey = "AIzaSyAfGC10zfgqg54n-hoMT1GhdoJMWFbUcxU";
        Places.initialize(getApplicationContext(), apiKey);
        mPlaceClient = Places.createClient(this);
        mImageview = findViewById(R.id.imageView);
        name = findViewById(R.id.textViewName);
        adress = findViewById(R.id.adress);



        Bundle bundle = getIntent().getExtras();
        String id = bundle.getString("place_id");
        executehttprequestwithretrofit(id , "");

}
    private void executehttprequestwithretrofit(String placeid, String fields){
        CallRestaurant.fetchRestaurantDetails(this, placeid, fields);
    }


    @Override
    public void onResponse(@Nullable JsonResponse details) {
        name.setText(details.getResult().getName());

    }

    @Override
    public void onFailure() {

    }
}

