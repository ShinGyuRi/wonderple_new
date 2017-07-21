package kr.co.easterbunny.wonderple.model;

import android.animation.TimeInterpolator;

/**
 * Created by scona on 2017-05-16.
 */

public class ExpandItem {
    public final String description;
    public final int colorId1;
    public final int colorId2;
    public final TimeInterpolator interpolator;

    public ExpandItem(String description, int colorId1, int colorId2, TimeInterpolator interpolator) {
        this.description = description;
        this.colorId1 = colorId1;
        this.colorId2 = colorId2;
        this.interpolator = interpolator;
    }
}
