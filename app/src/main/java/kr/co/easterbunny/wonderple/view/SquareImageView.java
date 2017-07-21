/*
 * Copyright (c) 2016. Ted Park. All Rights Reserved
 */

package kr.co.easterbunny.wonderple.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;


/**
 * Created by Gil on 09/06/2014.
 */
public class SquareImageView extends ImageView {



    public SquareImageView(Context context) {
        super(context);
    }

    public SquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);


    }



    //Squares the thumbnail
    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
         setMeasuredDimension(widthMeasureSpec, widthMeasureSpec);

    }




}
