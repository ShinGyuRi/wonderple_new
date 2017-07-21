package kr.co.easterbunny.wonderple.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scona on 2017-05-29.
 */

public class LoadNewsResult {
    @SerializedName("result") private String result;
    @SerializedName("message") private String message;
    @SerializedName("name") private String myName;
    @SerializedName("profile_image") private String myProfileImage;
    @SerializedName("followrequest") private String follerRequest;
    @SerializedName("eventcheck") private String eventCheck;
    @SerializedName("info") private String info;
    @SerializedName("currentevent") private String currentEvent;
    @SerializedName("news_list") private List<News> newsList = new ArrayList<>();

    public String getResult() {
        return result;
    }
    public String getMessage() {
        return message;
    }
    public String getMyName() {
        return myName;
    }
    public String getMyProfileImage() {
        return myProfileImage;
    }
    public String getFollerRequest() {
        return follerRequest;
    }
    public String getEventCheck() {
        return eventCheck;
    }
    public String getInfo() {
        return info;
    }
    public String getCurrentEvent() {
        return currentEvent;
    }
    public List<News> getNewsList() {
        return newsList;
    }

    public class News {
        @SerializedName("nid") private String nid;
        @SerializedName("type") private String type;
        @SerializedName("days") private String days;
        @SerializedName("checkstatus") private String checkStatus;
        @SerializedName("news_uid") private String newsUid;
        @SerializedName("news_name") private String newsName;
        @SerializedName("news_profile_image") private String newsProfileImage;
        @SerializedName("countfollow") private String followCheck;
        @SerializedName("sharestatus") private String shareStatus;
        @SerializedName("news_iid") private String newsIid;
        @SerializedName("news_image") private String newsImage;
        @SerializedName("imageusername") private String imageUsername;
        @SerializedName("imageuserimage") private String imageUserImage;

        public String getNid() {
            return nid;
        }
        public String getType() {
            return type;
        }
        public String getDays() {
            return days;
        }
        public String getCheckStatus() {
            return checkStatus;
        }
        public String getNewsUid() {
            return newsUid;
        }
        public String getNewsName() {
            return newsName;
        }
        public String getNewsProfileImage() {
            return newsProfileImage;
        }
        public String getFollowCheck() {
            return followCheck;
        }
        public String getShareStatus() {
            return shareStatus;
        }
        public String getNewsIid() {
            return newsIid;
        }
        public String getNewsImage() {
            return newsImage;
        }
        public String getImageUsername() {
            return imageUsername;
        }
        public String getImageUserImage() {
            return imageUserImage;
        }

        public void setFollowCheck(String followCheck) {
            this.followCheck = followCheck;
        }
    }

}
