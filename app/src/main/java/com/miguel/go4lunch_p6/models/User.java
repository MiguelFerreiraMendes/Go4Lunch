package com.miguel.go4lunch_p6.models;

public class User {

    private String id;
    private String username;
    private String urlPicture;
    private String restaurantInteressed;
    private String idOfRestaurantInteressed;

    public User() {}


    public User(String id, String username, String urlPicture, String restaurantInteressed, String idofrest){
        this.id = id;
        this.username = username;
        this.urlPicture = urlPicture;
        this.restaurantInteressed = restaurantInteressed;
        this.idOfRestaurantInteressed = idofrest;
    }


    public String getRestaurantInteressed() {
        return restaurantInteressed;
    }

    public void setRestaurantInteressed(String restaurantInteressed) {
        this.restaurantInteressed = restaurantInteressed;
    }
    public String getIdOfRestaurantInteressed() {
        return idOfRestaurantInteressed;
    }

    public void setIdOfRestaurantInteressed(String idOfRestaurantInteressed) {
        this.idOfRestaurantInteressed = idOfRestaurantInteressed;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUrlPicture() {
        return urlPicture;
    }

    public void setUrlPicture(String urlPicture) {
        this.urlPicture = urlPicture;
    }


}
