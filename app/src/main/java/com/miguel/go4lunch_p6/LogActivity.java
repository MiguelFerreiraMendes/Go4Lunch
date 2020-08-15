package com.miguel.go4lunch_p6;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.multidex.MultiDex;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.miguel.go4lunch_p6.api.UserHelper;
import java.util.Arrays;

public class LogActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;

    @Override
    protected void attachBaseContext(Context base){
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Nullable
    protected FirebaseUser getCurrentUser(){ return FirebaseAuth.getInstance().getCurrentUser(); }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        startSignInActivity();
    }


    private void startSignInActivity(){
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(
                                Arrays.asList( new AuthUI.IdpConfig.GoogleBuilder().build(),
                                               new AuthUI.IdpConfig.FacebookBuilder().build(),
                                               new AuthUI.IdpConfig.TwitterBuilder().build(),
                                               new AuthUI.IdpConfig.EmailBuilder().build()
                                                ))
                        .setIsSmartLockEnabled(false, true)
                        .setTheme(R.style.Logactivity)
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.handleResponseAfterSignIn(requestCode, resultCode, data);
    }

    
    private void handleResponseAfterSignIn(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) { // SUCCESS
                this.createUserInFirestore();
                Intent intent = new Intent(LogActivity.this, MainActivity.class);
                this.startActivity(intent);
            }
        }
        finish();
    }

    private void createUserInFirestore(){


        if (this.getCurrentUser() != null){
            FirebaseFirestore.getInstance().collection("userschoices").document(getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (!task.getResult().exists()) {
                        String urlPicture = (getCurrentUser().getPhotoUrl() != null) ? getCurrentUser().getPhotoUrl().toString() : null;
                        String username = getCurrentUser().getDisplayName();
                        String uid = getCurrentUser().getUid();

                        UserHelper.createUser(uid, username, urlPicture, "false").addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.i("Error", "Didn't create user in firebase");
                            }
                        });
                    }
                }
            });
        }
    }
}


