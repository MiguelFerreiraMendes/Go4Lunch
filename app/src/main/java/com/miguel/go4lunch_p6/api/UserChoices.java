package com.miguel.go4lunch_p6.api;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.miguel.go4lunch_p6.models.User;


public class UserChoices {

    private static final String COLLECTION_NAME = "userschoices";


    public static Task<Void> createUser(String uid, String username, String urlPicture) {
        // 1 - Create User object
        User userToCreate = new User(uid, username, urlPicture);
        // 2 - Add a new User Document to Firestore
        return UserChoices.getUsersCollection()
                .document(uid) // Setting uID for Document
                .set(userToCreate); // Setting object for Document
    }

    // --- COLLECTION REFERENCE ---

    public static CollectionReference getUsersCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // --- GET ---

    public static Task<DocumentSnapshot> getUser(String uid){
        return UserChoices.getUsersCollection().document(uid).get();
    }

    // --- UPDATE ---

    public static Task<Void> updateUsername(String username, String uid) {
        return UserChoices.getUsersCollection().document(uid).update("username", username);
    }

    public static Task<Void> updateIsInteressed(String uid, Boolean isInteressed) {
        return UserChoices.getUsersCollection().document(uid).update("isInteressed", isInteressed);
    }

    // --- DELETE ---

    public static Task<Void> deleteUser(String uid) {
        return UserChoices.getUsersCollection().document(uid).delete();
    }
}

