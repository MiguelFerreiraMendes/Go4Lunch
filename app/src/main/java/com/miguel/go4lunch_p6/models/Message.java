package com.miguel.go4lunch_p6.models;

import java.util.Date;

public class Message {


    private String message;
    private Date dateCreated;
    private User userSender;
    private String urlImage;

    public Message(String message, Date dateCreated, User userSender, String urlImage) {
        this.message = message;
        this.userSender = userSender;
    }

    public Message(String message, User userSender, String urlImage) {
        this.message = message;
        this.userSender = userSender;
        this.urlImage = urlImage;
    }

    public Message () {}

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public User getUserSender() {
        return userSender;
    }

    public void setUserSender(User userSender) {
        this.userSender = userSender;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }
}
