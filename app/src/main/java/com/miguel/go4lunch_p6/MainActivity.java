package com.miguel.go4lunch_p6;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.miguel.go4lunch_p6.api.UserHelper;

import java.util.Calendar;


public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, ListViewFragment.OnAdapteurPass {

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private static final int SIGN_OUT_TASK = 10;
    private TextView textInputEditTextUsername;
    private TextView textViewEmail;
    private ImageView ppuser;
    private static final int UPDATE_USERNAME = 30;
    private ListViewAdapter mAdapter = null;
    public static final String CHANNEL_1_ID = "channel1";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menutoolbar, menu);

        MenuItem searchrestaurant = menu.findItem(R.id.menu_activity_main_search);
        SearchView searchView = (SearchView) searchrestaurant.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (mAdapter!= null){
                    mAdapter.getFilter().filter(newText);
                }
                return false;
            }
        });
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_activity_main_search){
        }
        return super.onOptionsItemSelected(item);
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
            Log.i("test", "Totaux" + this.getCurrentUser().getPhotoUrl());
          /*
            Glide.with(MainActivity.this)
                    .load(this.getCurrentUser().getPhotoUrl().toString().trim())
                    .addListener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            Log.e("test", "glide failed");
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            Log.e("test", "glide sucess");
                            return false;
                        }
                    })
                   // .apply(RequestOptions.circleCropTransform())
                    .into(ppuser);
          */
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

    private void startAlarm() {

        Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR, 12);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);

    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel chanel1 = new NotificationChannel(CHANNEL_1_ID, "Channel 1", NotificationManager.IMPORTANCE_HIGH);
            chanel1.setDescription("Channel 1");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(chanel1);
        }
    }

    private void stopAlarm(){
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 1253, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
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
                UserHelper.updateUsername(username, this.getCurrentUser().getUid()).addOnFailureListener(this.onFailureListener()).addOnSuccessListener(this.updateUIAfterRESTRequestsCompleted(UPDATE_USERNAME));
            }
        }
    }

    @Override
    public void onAdapteurPass(ListViewAdapter adapteur) {
        this.mAdapter = adapteur;
    }
}

