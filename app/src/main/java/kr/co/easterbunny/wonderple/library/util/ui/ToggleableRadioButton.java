package kr.co.easterbunny.wonderple.library.util.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * Created by scona on 2017-05-25.
 */

public class ToggleableRadioButton extends android.support.v7.widget.AppCompatRadioButton {
    public ToggleableRadioButton(Context context) {
        super(context);
    }

    public ToggleableRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ToggleableRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void toggle() {
        if (isChecked()) {
            if (getParent() instanceof RadioGroup) {
                ((RadioGroup) getParent()).clearCheck();
            }
        } else {
            setChecked(true);
        }
    }
}
