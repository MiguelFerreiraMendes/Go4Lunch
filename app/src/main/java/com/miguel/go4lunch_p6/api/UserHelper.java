package com.miguel.go4lunch_p6.api;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.miguel.go4lunch_p6.models.Restaurant;
import com.miguel.go4lunch_p6.models.User;


public class UserHelper {

    private static final String COLLECTION_NAME = "userschoices";


    public static Task<Void> createUser(String uid, String username, String urlPicture) {
        // 1 - Create User object
        User userToCreate = new User(uid, username, urlPicture);
        // 2 - Add a new User Document to Firestore
        return UserHelper.getUsersCollection()
                .document(uid) // Setting uID for Document
                .set(userToCreate); // Setting object for Document
    }

    public static Task<Void> createRestaurantInfo(String uid, String NameRestaurant, int like, Boolean isinteressed){
        // 1 - Create User object
        Restaurant restaurantToCreate = new Restaurant(NameRestaurant, like , isinteressed);
        // 2 - Add a new User Document to Firestore
        Log.i("firebase", " uid = " + uid + "NameRestaurant = " + NameRestaurant + "restaurant tocreate" + restaurantToCreate);
        return UserHelper.getUsersCollection()
                .document(uid)
                .collection("Restaurant")
                .document(NameRestaurant)
                .set(restaurantToCreate); // Setting object for Document
    }


    // --- COLLECTION REFERENCE ---

    public static CollectionReference getUsersCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // --- GET ---

    public static Task<DocumentSnapshot> getUser(String uid){
        return UserHelper.getUsersCollection().document(uid).get();
    }

    // --- UPDATE ---

    public static Task<Void> updateUsername(String username, String uid) {
        return UserHelper.getUsersCollection().document(uid).update("username", username);
    }

    public static Task<Void> updateIsInteressed(String uid, Boolean isInteressed, String nameRestaurant) {
        return UserHelper.getUsersCollection().document(uid).collection("Restaurant").document(nameRestaurant).update("interessed", isInteressed);
    }

    public static Task<Void> updatelike(String uid, int like, String nameRestaurant) {
        return UserHelper.getUsersCollection().document(uid).collection("Restaurant").document(nameRestaurant).update("like", like);
    }

    public static Task<DocumentSnapshot> getRestaurantinfo(String uid, String nameRestaurant) {
        return UserHelper.getUsersCollection().document(uid).collection("Restaurant").document(nameRestaurant).get();
    }

    // --- DELETE ---

    public static Task<Void> deleteUser(String uid) {
        return UserHelper.getUsersCollection().document(uid).delete();
    }
}

