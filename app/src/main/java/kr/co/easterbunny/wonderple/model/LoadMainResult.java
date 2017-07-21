package kr.co.easterbunny.wonderple.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scona on 2017-03-09.
 */

public class LoadMainResult {

    @SerializedName("post_image")
    public List<PostImage> postImages = new ArrayList<>();
    public List<PostImage> getPostImages() {
        return postImages;
    }

    @SerializedName("result") private String result;
    @SerializedName("message") private String message;

    public String getResult() {
        return result;
    }
    public String getMessage() {
        return message;
    }

    public class PostImage {

        @SerializedName("id") private String id;
        @SerializedName("name") private String ImageUrl;
        @SerializedName("ratio") private String ratio;
        @SerializedName("random_num") private String randomNum;
        @SerializedName("user") private User user;

        public String getId() {
            return id;
        }
        public String getImageUrl() {
            return ImageUrl;
        }
        public String getRatio() {
            return ratio;
        }
        public String getRandomNum() {
            return randomNum;
        }
        public User getUser() {
            return user;
        }

        public class User {

            @SerializedName("uid")  private String uid;
            @SerializedName("name") private String name;
            @SerializedName("profile_image") private String profileImage;

            public String getUid() {
                return uid;
            }
            public String getName() {
                return name;
            }
            public String getProfileImage() {
                return profileImage;
            }
        }


    }

}
