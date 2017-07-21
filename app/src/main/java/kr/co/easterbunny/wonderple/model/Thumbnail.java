package kr.co.easterbunny.wonderple.model;

import android.graphics.Bitmap;

import com.zomato.photofilters.imageprocessors.Filter;

public class Thumbnail {

    public Bitmap image;
    public Filter filter;
    public String name;

    public Thumbnail() {
        image = null;
        filter = new Filter();
    }

}
