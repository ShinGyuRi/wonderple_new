package kr.co.easterbunny.wonderple.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scona on 2017-04-06.
 */

public class LoadWonderpleResult {

    @SerializedName("result") private String result;
    @SerializedName("message") private String message;
    @SerializedName("wonderple") private List<Wonderple> wonderples = new ArrayList<>();

    public String getResult() {
        return result;
    }
    public String getMessage() {
        return message;
    }
    public List<Wonderple> getWonderples() {
        return wonderples;
    }

    public class Wonderple {
        @SerializedName("uid") private String uid;
        @SerializedName("name") private String name;
        @SerializedName("profile_image") private String profileImage;
        @SerializedName("followcheck") private String followCheck;
        @SerializedName("sharestatus") private String shareStatus;
        @SerializedName("realname") private String realName;

        public String getUid() {
            return uid;
        }
        public String getName() {
            return name;
        }
        public String getProfileImage() {
            return profileImage;
        }
        public String getFollowCheck() {
            return followCheck;
        }
        public String getShareStatus() {
            return shareStatus;
        }
        public String getRealName() {
            return realName;
        }

        public void setFollowCheck(String followCheck) {
            this.followCheck = followCheck;
        }
    }

}
