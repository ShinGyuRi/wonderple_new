package kr.co.easterbunny.wonderple.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scona on 2017-05-17.
 */

public class LoadEventDataResult {

    @SerializedName("result") private String result;
    @SerializedName("message") private String message;
    @SerializedName("events") private List<Event> events = new ArrayList<>();

    public String getResult() {
        return result;
    }
    public String getMessage() {
        return message;
    }
    public List<Event> getEvents() {
        return events;
    }

    public class Event {
        @SerializedName("eventtitle") private String eventTitle;

        public String getEventTitle() {
            return eventTitle;
        }
    }

}
