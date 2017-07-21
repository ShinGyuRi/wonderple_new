package kr.co.easterbunny.wonderple.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scona on 2017-04-14.
 */

public class LoadProfileResult {
    @SerializedName("result") private String result;
    @SerializedName("message") private String message;
    @SerializedName("postcount") private String postCount;
    @SerializedName("wonderpostcount") private String wonderPostCount;
    @SerializedName("followercount") private String followerCount;
    @SerializedName("followingcount") private String followingCount;
    @SerializedName("desctext") private String descText;
    @SerializedName("name") private String userName;
    @SerializedName("profile_image") private String profileImage;
    @SerializedName("realname") private String realName;
    @SerializedName("skintone") private String skinTone;
    @SerializedName("post_images") private List<PostImages> postImages = new ArrayList<>();
    @SerializedName("wonderpost_images") private List<WonderImages> wonderImages = new ArrayList<>();
    @SerializedName("followcheck") private String followCheck;
    @SerializedName("sharestatus") private String shareStauts;

    public String getResult() {
        return result;
    }
    public String getMessage() {
        return message;
    }
    public String getPostCount() {
        return postCount;
    }
    public String getWonderPostCount() {
        return wonderPostCount;
    }
    public String getFollowerCount() {
        return followerCount;
    }
    public String getFollowingCount() {
        return followingCount;
    }
    public String getDescText() {
        return descText;
    }
    public String getUserName() {
        return userName;
    }
    public String getProfileImage() {
        return profileImage;
    }
    public String getRealName() {
        return realName;
    }
    public String getSkinTone() {
        return skinTone;
    }
    public List<PostImages> getPostImages() {
        return postImages;
    }
    public List<WonderImages> getWonderImages() {
        return wonderImages;
    }
    public String getFollowCheck() {
        return followCheck;
    }
    public String getShareStauts() {
        return shareStauts;
    }

    public void setShareStauts(String shareStauts) {
        this.shareStauts = shareStauts;
    }

    public class PostImages {
        @SerializedName("iid") private String iid;
        @SerializedName("url") private String imageUrl;
        @SerializedName("ratio") private String ratio;

        public String getIid() {
            return iid;
        }
        public String getImageUrl() {
            return imageUrl;
        }
        public String getRatio() {
            return ratio;
        }
    }

    public class WonderImages {
        @SerializedName("iid") private String iid;
        @SerializedName("url") private String ImageUrl;
        @SerializedName("ratio") private String ratio;
        @SerializedName("wondercount") private String wonderCount;
        @SerializedName("name") private String userName;
        @SerializedName("profile_image") private String profileImage;

        public String getIid() {
            return iid;
        }
        public String getImageUrl() {
            return ImageUrl;
        }
        public String getRatio() {
            return ratio;
        }
        public String getWonderCount() {
            return wonderCount;
        }
        public String getUserName() {
            return userName;
        }
        public String getProfileImage() {
            return profileImage;
        }
    }
}
