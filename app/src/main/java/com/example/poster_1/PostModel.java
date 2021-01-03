package com.example.poster_1;

import com.google.firebase.firestore.ServerTimestamp;

import java.io.Serializable;
import java.util.Date;

public class PostModel implements Serializable {


    private String userID;
    private String userName;
    private String postText;
    private String postType;
    @ServerTimestamp
    private Date postedTime;
    private String quotedPostID;


    public PostModel() {
    }

    public PostModel(String userID, String userName, String postText, String postType, Date postedTime) {
        this.userID = userID;
        this.userName = userName;
        this.postText = postText;
        this.postType = postType;
        this.postedTime = postedTime;
    }

    public PostModel(String userID, String userName, String postText, String postType, Date postedTime, String quotedPostID) {
        this.userID = userID;
        this.userName = userName;
        this.postText = postText;
        this.postType = postType;
        this.postedTime = postedTime;
        this.quotedPostID = quotedPostID;
    }

    public String getQuotedPostID() {
        return quotedPostID;
    }

    public void setQuotedPostID(String quotedPostID) {
        this.quotedPostID = quotedPostID;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String getPostType() {
        return postType;
    }

    public String getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }

    public String getPostText() {
        return postText;
    }

    public Date getPostedTime() {
        return postedTime;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }

    public void setPostedTime(Date postedTime) {
        this.postedTime = postedTime;
    }
}
