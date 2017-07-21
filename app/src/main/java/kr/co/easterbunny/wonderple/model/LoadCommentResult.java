package kr.co.easterbunny.wonderple.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scona on 2017-03-20.
 */

public class LoadCommentResult {

    @SerializedName("result") private String result;
    @SerializedName("message") private String message;
    @SerializedName("comments") private List<Comment> comments = new ArrayList<>();

    public List<Comment> getComments() {
        return comments;
    }
    public String getResult() {
        return result;
    }
    public String getMessage() {
        return message;
    }

    public class Comment {
        @SerializedName("auid") private String auid;
        @SerializedName("name") private String name;
        @SerializedName("profile_image") private String profileImg;
        @SerializedName("detail") private String detail;
        @SerializedName("commentid") private String commentId;
        @SerializedName("days") private String days;
        @SerializedName("skininfo") private String skinInfo;

        public String getAuid() {
            return auid;
        }
        public String getName() {
            return name;
        }
        public String getProfileImg() {
            return profileImg;
        }
        public String getDetail() {
            return detail;
        }
        public String getCommentId() {
            return commentId;
        }
        public String getDays() {
            return days;
        }
        public String getSkinInfo() {
            return skinInfo;
        }
    }

}
