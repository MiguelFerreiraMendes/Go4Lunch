package com.miguel.go4lunch_p6.models;

public class Restaurant {


    private String name;

    public Restaurant(String name){
        this.name = name;
    }

    public Restaurant() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
