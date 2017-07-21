package kr.co.easterbunny.wonderple.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Gyul on 2016-06-22.
 */
public class User {

    @SerializedName("email")
    public String email;

    @SerializedName("password")
    public String password;

    @SerializedName("username")
    public String username;

    @SerializedName("image")
    public String image;

    @SerializedName("snsid")
    public String snsid;

    @SerializedName("snstype")
    public String snstype;

    @SerializedName("udid")
    public String udid;


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSnsid() {
        return snsid;
    }

    public void setSnsid(String snsid) {
        this.snsid = snsid;
    }

    public String getSnstype() {
        return snstype;
    }

    public void setSnstype(String snstype) {
        this.snstype = snstype;
    }

    public String getUdid() {
        return udid;
    }

    public void setUdid(String udid) {
        this.udid = udid;
    }
}