package com.miguel.go4lunch_p6.api;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.miguel.go4lunch_p6.models.Restaurant;
import com.miguel.go4lunch_p6.models.RestaurantInfo;
import com.miguel.go4lunch_p6.models.User;

public class UserHelper {

    private static final String COLLECTION_NAME = "userschoices";


    public static Task<Void> createUser(String uid, String username, String urlPicture, String interessed) {
        User userToCreate = new User(uid, username, urlPicture, interessed, "null");
        return UserHelper.getUsersCollection()
                .document(uid)
                .set(userToCreate);
    }

    public static Task<Void> createRestaurantInfo(String uid, String NameRestaurant, int like, Boolean isinteressed){
        RestaurantInfo restaurantInfoToCreate = new RestaurantInfo(NameRestaurant, like , isinteressed);
        Log.i("firebase", " uid = " + uid + "NameRestaurant = " + NameRestaurant + "restaurant tocreate" + restaurantInfoToCreate);
        return UserHelper.getUsersCollection()
                .document(uid)
                .collection("Restaurant")
                .document(NameRestaurant)
                .set(restaurantInfoToCreate); // Setting object for Document
    }

    public static Task<Void> createRestaurant(String NameRestaurant){
        Restaurant restaurantInfoToCreate = new Restaurant(NameRestaurant);
        return FirebaseFirestore.getInstance().collection("Restaurant")
                .document(NameRestaurant)
                .set(restaurantInfoToCreate); // Setting object for Document
    }


    public static CollectionReference getUsersCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }


    public static Task<DocumentSnapshot> getUser(String uid){
        return UserHelper.getUsersCollection().document(uid).get();
    }


    public static Task<Void> updateUsername(String username, String uid) {
        return UserHelper.getUsersCollection().document(uid).update("username", username);
    }

    public static Task<Void> updateIDofRestaurantInteressed(String idRestaurant, String uid) {
        return UserHelper.getUsersCollection().document(uid).update("idOfRestaurantInteressed", idRestaurant);
    }

    public static Task<Void> updateIsInteressed(String uid, String nameRestaurant) {
        return UserHelper.getUsersCollection().document(uid).update("restaurantInteressed", nameRestaurant);
    }

    public static Task<Void> updatelike(String uid, int like, String nameRestaurant) {
        return UserHelper.getUsersCollection().document(uid).collection("Restaurant").document(nameRestaurant).update("like", like);
    }

    public static Task<DocumentSnapshot> getRestaurantinfo(String uid, String nameRestaurant) {
        return UserHelper.getUsersCollection().document(uid).collection("Restaurant").document(nameRestaurant).get();
    }


    public static Task<Void> deleteUser(String uid) {
        return UserHelper.getUsersCollection().document(uid).delete();
    }
}

