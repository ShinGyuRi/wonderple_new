package kr.co.easterbunny.wonderple.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scona on 2017-06-07.
 */

public class LoadDiaryResult {
    @SerializedName("result") private String result;
    @SerializedName("message") private String message;
    @SerializedName("itemdetail") private ItemDetail itemDetail;
    @SerializedName("talk_list") private List<Talk> talks = new ArrayList<>();
    @SerializedName("posting_list") private List<Post> posts = new ArrayList<>();

    public String getResult() {
        return result;
    }
    public String getMessage() {
        return message;
    }
    public ItemDetail getItemDetail() {
        return itemDetail;
    }
    public List<Talk> getTalks() {
        return talks;
    }
    public List<Post> getPosts() {
        return posts;
    }

    public class ItemDetail {
        @SerializedName("image") private String itemImage;
        @SerializedName("brandname") private String brandName;
        @SerializedName("category") private String category;
        @SerializedName("name") private String itemName;
        @SerializedName("colorname") private String colorName;
        @SerializedName("colorvalue") private String colorValue;
        @SerializedName("r") private String r;
        @SerializedName("g") private String g;
        @SerializedName("b") private String b;
        @SerializedName("dupcount") private String colorCount;
        @SerializedName("rate") private String rate;
        @SerializedName("comment") private String talkCount;

        public String getItemImage() {
            return itemImage;
        }
        public String getBrandName() {
            return brandName;
        }
        public String getCategory() {
            return category;
        }
        public String getItemName() {
            return itemName;
        }
        public String getColorName() {
            return colorName;
        }
        public String getColorValue() {
            return colorValue;
        }
        public String getR() {
            return r;
        }
        public String getG() {
            return g;
        }
        public String getB() {
            return b;
        }
        public String getColorCount() {
            return colorCount;
        }
        public String getRate() {
            return rate;
        }
        public String getTalkCount() {
            return talkCount;
        }
    }

    public class Talk {
        @SerializedName("auid") private String auid;
        @SerializedName("name") private String username;
        @SerializedName("profile_image") private String profileImage;
        @SerializedName("detail") private String detail;

        public String getAuid() {
            return auid;
        }
        public String getUsername() {
            return username;
        }
        public String getProfileImage() {
            return profileImage;
        }
        public String getDetail() {
            return detail;
        }
    }

    public class Post {
        @SerializedName("iid") private String iid;
        @SerializedName("url") private String imageUrl;
        @SerializedName("ratio") private String ratio;
        @SerializedName("wondercount") private String wonderCount;

        public String getIid() {
            return iid;
        }
        public String getImageUrl() {
            return imageUrl;
        }
        public String getRatio() {
            return ratio;
        }
        public String getWonderCount() {
            return wonderCount;
        }
    }



}
