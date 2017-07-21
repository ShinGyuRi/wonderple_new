package kr.co.easterbunny.wonderple.library.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.SparseArray;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;

/**
 * Created by Gyul on 2016-06-16.
 */
public class ImageUtil {

    public static Bitmap getBitmapFromUrl(String imgUrl, int scaleWidth,
                                          int scaleHeight) {
        Bitmap bm = null;
        Bitmap tmpBm = null;

        URL url = null;
        URLConnection conn = null;
        BufferedInputStream bin = null;

        try {
            url = new URL(imgUrl);
            conn = url.openConnection();
            bin = new BufferedInputStream(conn.getInputStream());
            tmpBm = BitmapFactory.decodeStream(bin);

            if (scaleWidth == 0 || scaleHeight == 0) {
                bm = Bitmap.createScaledBitmap(tmpBm, tmpBm.getWidth(),
                        tmpBm.getHeight(), false);

            } else {
                bm = Bitmap.createScaledBitmap(tmpBm, scaleWidth, scaleHeight,
                        false);

            }

        } catch (Exception err) {
            bm = null;
        } finally {
            tmpBm = null;

            if (bin != null) {
                try {
                    bin.close();
                } catch (Exception err) {
                }
                bin = null;
            }
            conn = null;
            url = null;
        }

        return bm;
    }

    public static boolean isBitmapCheck(Bitmap bm){
        if(bm!=null && bm.getWidth()>0 && bm.getHeight()>0){
            return true;
        }
        return false;
    }

    public static Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = null;
        if (bitmap != null) {
            output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);

            final int color = 0xff424242;
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
            canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getWidth() / 2, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);
            // Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
            // return _bmp;
        }

        return output;
    }

    public static Bitmap scaleCenterCrop(Bitmap source, int newHeight, int newWidth) {
        int sourceWidth = source.getWidth();
        int sourceHeight = source.getHeight();

        // Compute the scaling factors to fit the new height and width,
        // respectively.
        // To cover the final image, the final scaling will be the bigger
        // of these two.
        float xScale = (float) newWidth / sourceWidth;
        float yScale = (float) newHeight / sourceHeight;
        float scale = Math.max(xScale, yScale);

        // Now get the size of the source bitmap when scaled
        float scaledWidth = scale * sourceWidth;
        float scaledHeight = scale * sourceHeight;

        // Let's find out the upper left coordinates if the scaled bitmap
        // should be centered in the new size give by the parameters
        float left = (newWidth - scaledWidth) / 2;
        float top = (newHeight - scaledHeight) / 2;

        // The target rectangle for the new, scaled version of the source bitmap
        // will now be
        RectF targetRect = new RectF(left, top, left + scaledWidth, top + scaledHeight);

        // Finally, we create a new bitmap of the specified size and draw our
        // new,
        // scaled bitmap onto it.
        if(source==null	) return null;
        Bitmap dest = Bitmap.createBitmap(newWidth, newHeight, source.getConfig());
        Canvas canvas = new Canvas(dest);
        canvas.drawBitmap(source, null, targetRect, null);

        return dest;
    }

    public static Bitmap scaleSquareCrop(Bitmap source, int newWidth, int newHeight) {
        int sourceWidth = source.getWidth();
        int sourceHeight = source.getHeight();

        Debug.showDebug("sourceWidth"+sourceWidth);
        Debug.showDebug("sourceHeight"+sourceHeight);
        Debug.showDebug("newWidth"+newWidth);
        Debug.showDebug("newHeight"+newHeight);


        int[] nPixels	=	new int[newWidth * newHeight];
        source.getPixels(nPixels, 0, newWidth, 0, 0, newWidth, newHeight);
        Bitmap resizeBm	=	Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);
        resizeBm.setPixels(nPixels, 0, newWidth, 0, 0, newWidth, newHeight);


//		float xScale = (float) newWidth / sourceWidth;
//		float yScale = (float) newHeight / sourceHeight;
//		float scale = Math.max(xScale, yScale);
//
//		// Now get the size of the source bitmap when scaled
//		float scaledWidth = scale * sourceWidth;
//		float scaledHeight = scale * sourceHeight;
//
//		float left = 0;
//		float top = 0;
//
//
//		RectF targetRect = new RectF(left, top, left + newWidth, top + newHeight);
//
//
//		Bitmap dest = Bitmap.createBitmap(newWidth, newHeight, source.getConfig());
//		Canvas canvas = new Canvas(dest);
//		canvas.drawColor(Color.RED);
//		canvas.drawBitmap(source, null, targetRect, null);


        return resizeBm;
    }

    public static byte[] bitmapToByteArray( Bitmap bitmap ) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream() ;
        bitmap.compress( Bitmap.CompressFormat.JPEG, 100, stream) ;
        byte[] byteArray = stream.toByteArray() ;
        return byteArray ;
    }

    public static Bitmap imageViewToBitmap(ImageView imageView) {
        Drawable d = imageView.getDrawable();
        Bitmap bitmap = ((BitmapDrawable)d).getBitmap();

        return bitmap;
    }

    public static Bitmap blurBitmap(Context context, Bitmap bitmap, float blurRadius) {
        if (bitmap.getConfig() == null) {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                bitmap.setConfig(Bitmap.Config.ARGB_8888);
            } else {
                bitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            }
        }


        // Let's create an empty bitmap with the same size of the bitmap we want to blur
        Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        // Instantiate a new Renderscript
        RenderScript rs = RenderScript.create(context);

        // Create an Intrinsic Blur Script using the Renderscript
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

        //Create the in/out Allocations with the Renderscript and the in/out bitmaps
        Allocation allIn = Allocation.createFromBitmap(rs, bitmap);
        Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);

        //Set the radius of the blur
        blurScript.setRadius(blurRadius);

        //Perform the Renderscript
        blurScript.setInput(allIn);
        blurScript.forEach(allOut);

        //Copy the final bitmap created by the out Allocation to the outBitmap
        allOut.copyTo(outBitmap);

        //recycle the original bitmap
        bitmap.recycle();

        //After finishing everything, we destroy the Renderscript.
        rs.destroy();

        return outBitmap;
    }

    private static final SparseArray<Double> sPositionHeightRatios = new SparseArray<Double>();
    private static final Random mRandom = new Random();

    public static double getPositionRatio(final int position, double ratio) {
//        double ratio = sPositionHeightRatios.get(position, 0.0);
        // if not yet done generate and stash the columns height
        // in our real world scenario this will be determined by
        // some match based on the known height and width of the image
        // and maybe a helpful way to get the column height!
        if (ratio == 0) {
            ratio = sPositionHeightRatios.get(position, 0.0);
        }
//        ratio = getRandomHeightRatio();
        sPositionHeightRatios.append(position, ratio);
        JSLog.D("getPositionRatio:" + position + " ratio:" + ratio, new Throwable());
        return ratio;
    }

    public static double getRandomHeightRatio() {
        return (mRandom.nextDouble() / 2.0) + 1.0; // height will be 1.0 - 1.5 the width
    }
}
