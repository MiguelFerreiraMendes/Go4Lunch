package com.miguel.go4lunch_p6.models;

public class User {

    private String id;
    private String username;
    private String urlPicture;

    public User() {}


    public User(String id, String username, String urlPicture){
        this.id = id;
        this.username = username;
        this.urlPicture = urlPicture;
        //this.isInteressed = false;
    }


    //public Boolean getInteressed() {
     //   return isInteressed;
    //}

    //public void setInteressed(Boolean interessed) {
      //  isInteressed = interessed;
    //}

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
