package kr.co.easterbunny.wonderple.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Gyul on 2016-06-22.
 */
public class SignInResult {

    @SerializedName("result")
    public String result;

    @SerializedName("message")
    public String message;

    @SerializedName("user")
    public User user;


    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
