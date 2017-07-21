package kr.co.easterbunny.wonderple.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scona on 2017-06-01.
 */

public class LoadFollowNewsResult {
    @SerializedName("result") private String result;
    @SerializedName("message") private String message;
    @SerializedName("follow_list") private List<Follow> followList = new ArrayList<>();

    public String getResult() {
        return result;
    }
    public String getMessage() {
        return message;
    }
    public List<Follow> getFollowList() {
        return followList;
    }

    public class Follow {
        @SerializedName("auid") private String auid;
        @SerializedName("name") private String name;
        @SerializedName("profile_image") private String profileImage;

        public String getAuid() {
            return auid;
        }
        public String getName() {
            return name;
        }
        public String getProfileImage() {
            return profileImage;
        }
    }

}
