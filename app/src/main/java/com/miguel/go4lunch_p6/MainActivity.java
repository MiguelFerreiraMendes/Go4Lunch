package com.miguel.go4lunch_p6;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.tabs.TabLayout;


public class MainActivity extends AppCompatActivity {

    private ViewPager mPager;
    private String placeAPI = "AIzaSyAfGC10zfgqg54n-hoMT1GhdoJMWFbUcxU";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        // Initialize the SDK
        Places.initialize(getApplicationContext(), placeAPI);

        // Create a new Places client instance
        PlacesClient placesClient = Places.createClient(this);

        this.configureViewPagerandTabs();

    }

    private void configureViewPagerandTabs(){
        // 1 - Get ViewPager from layout
        ViewPager pager = findViewById(R.id.viewpager);
        // 2 - Set Adapter PageAdapter and glue it together
        pager.setAdapter(new FragmentAdapter(getSupportFragmentManager(), this) {
        });
        mPager = pager;
        TabLayout tabs= findViewById(R.id.tab_layout);
        tabs.setupWithViewPager(pager);
    }

}

