package kr.co.easterbunny.wonderple.library.util;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

/**
 * Created by scona on 2017-03-24.
 */

public class AnimationUtil {

    public static void setAnimation(View view) {
        Animation animation = new AlphaAnimation(0, 1);
        animation.setDuration(500);
        view.setAnimation(animation);
    }
}
