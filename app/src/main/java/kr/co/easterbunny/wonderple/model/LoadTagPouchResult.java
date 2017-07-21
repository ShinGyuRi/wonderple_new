package kr.co.easterbunny.wonderple.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scona on 2017-05-11.
 */

public class LoadTagPouchResult {

    @SerializedName("result") private String result;
    @SerializedName("message") private String message;
    @SerializedName("list_lips") private List<FacePart> lips = new ArrayList<>();
    @SerializedName("list_eyes") private List<FacePart> eyes = new ArrayList<>();
    @SerializedName("list_face") private List<FacePart> faces = new ArrayList<>();
    @SerializedName("list_base") private List<FacePart> bases = new ArrayList<>();

    public String getResult() {
        return result;
    }
    public String getMessage() {
        return message;
    }
    public List<FacePart> getLips() {
        return lips;
    }
    public List<FacePart> getEyes() {
        return eyes;
    }
    public List<FacePart> getFaces() {
        return faces;
    }
    public List<FacePart> getBases() {
        return bases;
    }

    public class FacePart {
        @SerializedName("cid") private String cid;
        @SerializedName("image") private String imageUrl;
        @SerializedName("thename") private String itemName;
        @SerializedName("thebrand") private String brandName;
        @SerializedName("dupcount") private String colorCount;
        @SerializedName("r") private String red;
        @SerializedName("g") private String green;
        @SerializedName("b") private String blue;
        @SerializedName("colorname") private String colorName;
        @SerializedName("colorvalue") private String colorValue;
        @SerializedName("caid") private String caid;

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
        public String getRed() {
            return red;
        }
        public String getGreen() {
            return green;
        }
        public String getBlue() {
            return blue;
        }
        public String getColorName() {
            return colorName;
        }
        public String getColorValue() {
            return colorValue;
        }
        public String getCaid() {
            return caid;
        }
    }


}
