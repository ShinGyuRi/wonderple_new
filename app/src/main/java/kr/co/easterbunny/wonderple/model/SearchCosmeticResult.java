package kr.co.easterbunny.wonderple.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scona on 2017-06-12.
 */

public class SearchCosmeticResult {
    @SerializedName("result") private String result;
    @SerializedName("message") private String messgae;
    @SerializedName("item_list") private List<Item> items = new ArrayList<>();
    @SerializedName("brand_list") private List<Brand> brands = new ArrayList<>();

    public String getResult() {
        return result;
    }
    public String getMessgae() {
        return messgae;
    }
    public List<Item> getItems() {
        return items;
    }
    public List<Brand> getBrands() {
        return brands;
    }

    public class Item {
        @SerializedName("cid") private String cid;
        @SerializedName("image") private String imageUrl;
        @SerializedName("title") private String itemName;
        @SerializedName("brand") private String brandName;
        @SerializedName("dupcount") private String colorCount;
        @SerializedName("r") private String r;
        @SerializedName("g") private String g;
        @SerializedName("b") private String b;
        @SerializedName("colorname") private String coloName;
        @SerializedName("colorvalue") private String colorValue;
        @SerializedName("caid") private String caid;
        private String aChar;

        public void setaChar(String aChar) {
            this.aChar = aChar;
        }

        public String getCid() {
            return cid;
        }
        public String getImageUrl() {
            return imageUrl;
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
        public String getaChar() {
            return aChar;
        }
    }

    public class Brand {
        @SerializedName("bid") private String bid;
        @SerializedName("brand") private String brandName;
        @SerializedName("logo") private String logoUrl;
        private String bChar ;

        public void setbChar(String bChar) {
            this.bChar = bChar;
        }

        public String getBid() {
            return bid;
        }
        public String getBrandName() {
            return brandName;
        }
        public String getLogoUrl() {
            return logoUrl;
        }
        public String getbChar() {
            return bChar;
        }
    }

}
