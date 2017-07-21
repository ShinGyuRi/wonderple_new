package kr.co.easterbunny.wonderple.event;

import com.zomato.photofilters.imageprocessors.Filter;

/**
 * Created by scona on 2017-05-02.
 */

public class FilterEvent {

    private int filterType;
    private int progress;

    public FilterEvent(int filterType, int progress) {
        this.filterType = filterType;
        this.progress = progress;
    }

    public int getFilterType() {
        return filterType;
    }

    public int getProgress() {
        return progress;
    }
}
