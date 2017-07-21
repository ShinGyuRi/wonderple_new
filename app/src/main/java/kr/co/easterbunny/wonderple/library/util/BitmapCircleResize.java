package kr.co.easterbunny.wonderple.library.util;

import android.content.Context;
import android.graphics.Bitmap;

import com.squareup.picasso.Transformation;

/**
 * Created by Gyul on 2016-06-22.
 */
public class BitmapCircleResize implements Transformation {

    private Context mContext;
    private	int width;

    public BitmapCircleResize(Context context, int width){
        mContext	=	context;
        this.width	=	width;
    }

    @Override
    public String key() {
        return "square()";
    }

    @Override
    public Bitmap transform(Bitmap arg0) {
        Bitmap loadedImage=	null;
        if (arg0 != null) {
            loadedImage = ImageUtil.scaleCenterCrop(arg0, width, width);
            loadedImage = ImageUtil.getCroppedBitmap(loadedImage);
            if (arg0 != loadedImage) {
                arg0.recycle();
            }
        }

        return loadedImage;
    }

}
