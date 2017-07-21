package kr.co.easterbunny.wonderple.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scona on 2017-03-21.
 */

public class LoadMentionUserListResult {

    @SerializedName("result") private String result;
    @SerializedName("message") private String message;
    @SerializedName("users") private List<Users> users = new ArrayList<>();

    public String getResult() {
        return result;
    }
    public String getMessage() {
        return message;
    }
    public List<Users> getUsers() {
        return users;
    }

    public class Users {
        @SerializedName("user") private String user;
        @SerializedName("name") private String name;
        @SerializedName("profile_image") private String profileImg;

        public String getUser() {
            return user;
        }
        public String getName() {
            return name;
        }
        public String getProfileImg() {
            return profileImg;
        }
    }

}
