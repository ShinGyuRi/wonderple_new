package kr.co.easterbunny.wonderple.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scona on 2017-05-31.
 */

public class SearchTagResult {
    @SerializedName("result") private String result;
    @SerializedName("message") private String message;
    @SerializedName("searchedtag") private List<SearchTag> hashtagList = new ArrayList<>();

    public String getResult() {
        return result;
    }
    public String getMessage() {
        return message;
    }
    public List<SearchTag> getHashtagList() {
        return hashtagList;
    }

    public class SearchTag {
        @SerializedName("hid") private String hid;
        @SerializedName("theword") private String theWord;
        @SerializedName("count") private String count;

        public String getHid() {
            return hid;
        }
        public String getTheWord() {
            return theWord;
        }
        public String getCount() {
            return count;
        }
    }

}
