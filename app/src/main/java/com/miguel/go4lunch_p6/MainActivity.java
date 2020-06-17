package com.miguel.go4lunch_p6;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.miguel.go4lunch_p6.api.UserChoices;


public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private static final int SIGN_OUT_TASK = 10;
    private TextView textInputEditTextUsername;
    private TextView textViewEmail;
    private ImageView ppuser;
    private static final int UPDATE_USERNAME = 30;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        Log.i("test toolbar" , "" + toolbar.getTitle());
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        this.configureNavigationView();
        this.configureDrawerLayout();
        this.configureViewPagerandTabs();
        View navhearderview = navigationView.getHeaderView(0);
        this.ppuser = navhearderview.findViewById(R.id.imageViewNav);
        this.textInputEditTextUsername = navhearderview.findViewById(R.id.NameNav);
        this.textViewEmail = navhearderview.findViewById(R.id.AdressNav);
        this.updateUIWhenCreating();
        updateUsernameInFirebase();

    }

    private void configureViewPagerandTabs(){
        // 1 - Get ViewPager from layout
        ViewPager pager = findViewById(R.id.viewpager);
        // 2 - Set Adapter PageAdapter and glue it together
        pager.setAdapter(new FragmentAdapter(getSupportFragmentManager(), this) {
        });
        TabLayout tabs= findViewById(R.id.tab_layout);
        tabs.setupWithViewPager(pager);
    }

    private void configureNavigationView(){
        this.navigationView = findViewById(R.id.activity_main_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void updateUIWhenCreating(){
        if (this.getCurrentUser() != null){
        //Get picture URL from Firebase
        if (this.getCurrentUser().getPhotoUrl() != null) {
            Log.i("test", "" + this.getCurrentUser().getPhotoUrl());
            Glide.with(MainActivity.this)
                    .load(this.getCurrentUser().getPhotoUrl())
                    .apply(RequestOptions.circleCropTransform())
                    .into(ppuser);
        }

        //Get email & username from Firebase
        String email = TextUtils.isEmpty(this.getCurrentUser().getEmail()) ? getString(R.string.info_no_email_found) : this.getCurrentUser().getEmail();
        String username = TextUtils.isEmpty(this.getCurrentUser().getDisplayName()) ? getString(R.string.info_no_username_found) : this.getCurrentUser().getDisplayName();
        //Update views with data
        this.textInputEditTextUsername.setText(username);
        this.textViewEmail.setText(email);
    }
}


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // Handle Navigation Item Click
        int id = menuItem.getItemId();
        switch (id){
            case R.id.your_lunch:
                break;
            case R.id.settings:
                break;
            case R.id.logout:
                this.signOutUserFromFirebase();
                break;
            default:
                break;
        }
        this.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void signOutUserFromFirebase(){
        AuthUI.getInstance()
                .signOut(this)
                .addOnSuccessListener(this, this.updateUIAfterRESTRequestsCompleted(SIGN_OUT_TASK));
    }

    private OnSuccessListener<Void> updateUIAfterRESTRequestsCompleted(final int origin){
        return new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                switch (origin) {
                    case SIGN_OUT_TASK :
                        Intent intent = new Intent(MainActivity.this, LogActivity.class);
                        startActivity(intent);
                        finish();

                    case UPDATE_USERNAME :

                }
            }
        };
    }



    private void configureDrawerLayout(){
        this.drawerLayout = findViewById(R.id.drawerlayout);
        drawerLayout.setBackgroundColor(getResources().getColor(R.color.quantum_orange400));
        Toolbar toolbar = findViewById(R.id.toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    protected OnFailureListener onFailureListener(){
        return new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), getString(R.string.error_unknown_error), Toast.LENGTH_LONG).show();
            }
        };
    }

    private void updateUsernameInFirebase(){

        String username = this.textInputEditTextUsername.getText().toString();

        if (this.getCurrentUser() != null){
            if (!username.isEmpty() &&  !username.equals(getString(R.string.info_no_username_found))){
                UserChoices.updateUsername(username, this.getCurrentUser().getUid()).addOnFailureListener(this.onFailureListener()).addOnSuccessListener(this.updateUIAfterRESTRequestsCompleted(UPDATE_USERNAME));
            }
        }
    }
}

