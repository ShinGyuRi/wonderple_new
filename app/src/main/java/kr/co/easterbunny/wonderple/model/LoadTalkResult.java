package kr.co.easterbunny.wonderple.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scona on 2017-06-08.
 */

public class LoadTalkResult {
    @SerializedName("result") private String result;
    @SerializedName("message") private String message;
    @SerializedName("itemdetail") private LoadDiaryResult.ItemDetail itemDetail;
    @SerializedName("talk_list") private  List<LoadCommentResult.Comment> talks = new ArrayList<>();

    public String getResult() {
        return result;
    }
    public String getMessage() {
        return message;
    }
    public LoadDiaryResult.ItemDetail getItemDetail() {
        return itemDetail;
    }
    public List<LoadCommentResult.Comment> getTalks() {
        return talks;
    }
}
