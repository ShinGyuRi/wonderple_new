package kr.co.easterbunny.wonderple.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by scona on 2017-03-15.
 */

public class LoadPostResult {

    @SerializedName("result") private String result;
    @SerializedName("message") private String message;
    @SerializedName("post_data") private PostData postData;
    @SerializedName("tag_item") private List<TagItem> tagItems = new ArrayList<>();
    @SerializedName("wonder_category") private List<WonderCategory> wonderCategories = new ArrayList<>();

    public class PostData {

        @SerializedName("user") private User user;
        @SerializedName("ratio") private String ratio;
        @SerializedName("uploadtime") private String uploadTime;
        @SerializedName("desctext") private String descText;
        @SerializedName("wonderplecount") private String wonderpleCount;
        @SerializedName("checkwonderple") private String checkWonderple;
        @SerializedName("wondercount") private String wonderCount;
        @SerializedName("checkwonder") private String checkWonder;
        @SerializedName("commentcount") private String commentCount;
        @SerializedName("checkcomment") private String checkComment;
        @SerializedName("sameuser") private String sameUser;
        @SerializedName("relationship") private String relationship;

        public class User {

            @SerializedName("uid") private String uid;
            @SerializedName("name") private String name;
            @SerializedName("profile_image") private String imgProfile;

            public String getUid() {
                return uid;
            }
            public String getName() {
                return name;
            }
            public String getImgProfile() {
                return imgProfile;
            }
        }

        public User getUser() {
            return user;
        }
        public String getRatio() {
            return ratio;
        }
        public String getUploadTime() {
            return uploadTime;
        }
        public String getDescText() {
            return descText;
        }
        public String getWonderpleCount() {
            return wonderpleCount;
        }
        public String getCheckWonderple() {
            return checkWonderple;
        }
        public String getWonderCount() {
            return wonderCount;
        }
        public String getCheckWonder() {
            return checkWonder;
        }
        public String getCommentCount() {
            return commentCount;
        }
        public String getCheckComment() {
            return checkComment;
        }
        public String getSameUser() {
            return sameUser;
        }
        public String getRelationship() {
            return relationship;
        }
    }

    public class TagItem implements Serializable {
        @SerializedName("cid") private String cid;
        @SerializedName("image") private String image;
        @SerializedName("r") private String r;
        @SerializedName("g") private String g;
        @SerializedName("b") private String b;
        @SerializedName("category") private String category;
        @SerializedName("dupcount") private String dupcount;

        public String getCid() {
            return cid;
        }
        public String getImage() {
            return image;
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
        public String getCategory() {
            return category;
        }
        public String getDupcount() {
            return dupcount;
        }
    }

    public class WonderCategory {
        @SerializedName("wondercategory") String wonderCategory;

        public String getWonderCategory() {
            return wonderCategory;
        }
    }



    public String getResult() {
        return result;
    }
    public String getMessage() {
        return message;
    }
    public PostData getPostData() {
        return postData;
    }
    public List<TagItem> getTagItems() {
        return tagItems;
    }
    public List<WonderCategory> getWonderCategories() {
        return wonderCategories;
    }
}
