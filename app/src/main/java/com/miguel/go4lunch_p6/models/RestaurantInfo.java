package com.miguel.go4lunch_p6.models;

public class RestaurantInfo {

    private String id;
    private int like = 0;
    private Boolean isInteressed = false;

    public RestaurantInfo(String id, int like , Boolean isInteressed){
        this.id = id;
        this.like = like;
        this.isInteressed = isInteressed;
    }

    public String getId() {
        return id;
    }

    public RestaurantInfo() {
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getInteressed() {
        return isInteressed;
    }

    public void setInteressed(Boolean interessed) {
        isInteressed = interessed;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }
}
