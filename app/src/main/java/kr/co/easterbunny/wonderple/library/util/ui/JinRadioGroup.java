package kr.co.easterbunny.wonderple.library.util.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * Created by scona on 2017-05-25.
 */

public class JinRadioGroup extends LinearLayout {

    private ArrayList<View> mCheckables = new ArrayList<View>();

    public JinRadioGroup(Context context) {
        super(context);
    }

    public JinRadioGroup(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public JinRadioGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void addView(View child, int index,
                        android.view.ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        parseChild(child);
    }

    public void parseChild(final View child)
    {
        if(child instanceof Checkable)
        {
            mCheckables.add(child);
            child.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {
                    for(int i = 0; i < mCheckables.size();i++)
                    {
                        Checkable view = (Checkable) mCheckables.get(i);
                        if(view == v)
                        {
                            ((Checkable)view).setChecked(true);
                        }
                        else
                        {
                            ((Checkable)view).setChecked(false);
                        }
                    }
                }
            });
        }
        else if(child instanceof ViewGroup)
        {
            parseChildren((ViewGroup)child);
        }
    }

    public void parseChildren(final ViewGroup child)
    {
        for (int i = 0; i < child.getChildCount();i++)
        {
            parseChild(child.getChildAt(i));
        }
    }
}
