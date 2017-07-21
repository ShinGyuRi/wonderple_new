package kr.co.easterbunny.wonderple.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scona on 2017-06-02.
 */

public class LoadPouchResult {
    @SerializedName("result") private String result;
    @SerializedName("message") private String message;
    @SerializedName("pouch_list") private List<MyItem> myItemList = new ArrayList<>();
    @SerializedName("favorite_list") private List<Favorite> favoriteList = new ArrayList<>();

    public String getResult() {
        return result;
    }
    public String getMessage() {
        return message;
    }
    public List<MyItem> getMyItemList() {
        return myItemList;
    }
    public List<Favorite> getFavoriteList() {
        return favoriteList;
    }

    public class MyItem {
        @SerializedName("pid") private String pid;
        @SerializedName("cid") private String cid;
        @SerializedName("image") private String image;
        @SerializedName("title") private String itemName;
        @SerializedName("brand") private String brandName;
        @SerializedName("dupcount") private String colorCount;
        @SerializedName("r") private String r;
        @SerializedName("g") private String g;
        @SerializedName("b") private String b;
        @SerializedName("colorname") private String colorName;
        @SerializedName("colorvalue") private String colorValue;
        @SerializedName("tagcount") private String tagCount;
        @SerializedName("rate") private String rate;
        @SerializedName("point") private String wonderCount;
        @SerializedName("caid") private String caid;

        public String getPid() {
            return pid;
        }
        public String getCid() {
            return cid;
        }
        public String getImage() {
            return image;
        }
        public String getItemName() {
            return itemName;
        }
        public String getBrandName() {
            return brandName;
        }
        public String getColorCount() {
            return colorCount;
        }
        public String getR() {
            return r;
        }
        public String getG() {
            return g;
        }
        public String getB() {
            return b;
        }
        public String getColorName() {
            return colorName;
        }
        public String getColorValue() {
            return colorValue;
        }
        public String getTagCount() {
            return tagCount;
        }
        public String getRate() {
            return rate;
        }
        public String getWonderCount() {
            return wonderCount;
        }
        public String getCaid() {
            return caid;
        }
    }

    public class Favorite {
        @SerializedName("favid") private String favid;
        @SerializedName("cid") private String cid;
        @SerializedName("image") private String image;
        @SerializedName("title") private String itemName;
        @SerializedName("brand") private String brandName;
        @SerializedName("dupcount") private String colorCount;
        @SerializedName("r") private String r;
        @SerializedName("g") private String g;
        @SerializedName("b") private String b;
        @SerializedName("colorname") private String coloName;
        @SerializedName("colorvalue") private String colorValue;
        @SerializedName("caid") private String caid;

        public String getFavid() {
            return favid;
        }
        public String getCid() {
            return cid;
        }
        public String getImage() {
            return image;
        }
        public String getItemName() {
            return itemName;
        }
        public String getBrandName() {
            return brandName;
        }
        public String getColorCount() {
            return colorCount;
        }
        public String getR() {
            return r;
        }
        public String getG() {
            return g;
        }
        public String getB() {
            return b;
        }
        public String getColoName() {
            return coloName;
        }
        public String getColorValue() {
            return colorValue;
        }
        public String getCaid() {
            return caid;
        }
    }


}
