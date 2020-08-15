package com.miguel.go4lunch_p6;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;
import controler.FragmentAdapter;
import controler.ListViewAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.miguel.go4lunch_p6.api.UserHelper;
import com.miguel.go4lunch_p6.models.User;

import java.util.List;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, ListViewFragment.OnAdapteurPass, MapViewFragment.OnDataPass {

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private static final int SIGN_OUT_TASK = 10;
    private TextView textInputEditTextUsername;
    private TextView textViewEmail;
    private ImageView ppuser;
    private static final int UPDATE_USERNAME = 30;
    private ListViewAdapter mAdapter = null;
    private String myuserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private User currentUser;
    private List<Marker> mMarkerList;
    private ViewPager mViewPager;
    private MapViewFragment mFragmentMapView;
    int position;


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
        UserHelper.getUsersCollection().document(myuserID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                currentUser = task.getResult().toObject(User.class);
            }
        });
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
                mFragmentMapView = (MapViewFragment) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + mViewPager.getCurrentItem());
                if (mAdapter!= null){
                    mAdapter.getFilter().filter(newText);
                }
                if (mFragmentMapView != null && mFragmentMapView.isVisible()) {
                    mFragmentMapView.getFilter().filter(newText);
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

    public void dataChanged() {
        mViewPager.getAdapter().notifyDataSetChanged();
    }

    private void configureViewPagerandTabs(){
        mViewPager = findViewById(R.id.viewpager);
        mViewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(), this) {
        });
        TabLayout tabs= findViewById(R.id.tab_layout);
        tabs.setupWithViewPager(mViewPager);
        this.position = mViewPager.getCurrentItem();
    }

    private void configureNavigationView(){
        this.navigationView = findViewById(R.id.activity_main_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void updateUIWhenCreating(){
        if (this.getCurrentUser() != null){
        if (this.getCurrentUser().getPhotoUrl() != null) {
            Glide.with(ppuser)
                    .load(this.getCurrentUser().getPhotoUrl())
                    .apply(RequestOptions.circleCropTransform())
                    .into(ppuser);
        }

        String email = TextUtils.isEmpty(this.getCurrentUser().getEmail()) ? getString(R.string.info_no_email_found) : this.getCurrentUser().getEmail();
        String username = TextUtils.isEmpty(this.getCurrentUser().getDisplayName()) ? getString(R.string.info_no_username_found) : this.getCurrentUser().getDisplayName();
        this.textInputEditTextUsername.setText(username);
        this.textViewEmail.setText(email);
    }
}

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        switch (id){
            case R.id.your_lunch:
                if (currentUser.getIdOfRestaurantInteressed() != null){
                Intent intent = new Intent(MainActivity.this , RestaurantDetailsActivity.class);
                intent.putExtra("place_id", currentUser.getIdOfRestaurantInteressed());
                startActivity(intent);
                }
                break;
            case R.id.settings:
                Intent intent = new Intent(MainActivity.this , NotificationActivity.class);
                startActivity(intent);
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
                if (origin == SIGN_OUT_TASK) {
                    Intent intent = new Intent(MainActivity.this, LogActivity.class);
                    startActivity(intent);
                    finish();
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

    @Override
    public void onDataPass(List<Marker> data) {
        this.mMarkerList = data;
    }

}

