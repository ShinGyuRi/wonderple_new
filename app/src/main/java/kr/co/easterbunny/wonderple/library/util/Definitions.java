package kr.co.easterbunny.wonderple.library.util;

/**
 * Created by Gyul on 2016-06-16.
 */
public class Definitions {

    public interface PREFKEY    {
        String IS_VISIT_EXPERIENCE_BOOL = "IS_VISIT_EXPERIENCE_BOOL";
    }

    public interface ACTIVITY_REQUEST_CODE {
        int PICK_GALLERY = 1;
        int PICK_CAMERA = 2;
        int PICK_CROP = 3;
        int PICK_FILE = 4;

        int EMAIL_LOGIN = 10;
        int IS_LOGOUT = 11;
        int SNS_ACCOUNT_INPUT = 12;
        int FACEBOOK_CONNECT = 64206;
        int FACEBOOK_SHARE = 64207;

        int PG21_TODAY_MISSION_UPLOAD = 20;
        int PG21_TODAY_MISSION_UPLOAD_COMPLETE = 22;

        int PERMISSION_ABOUT_GALLERY = 100;
        int PERMISSION_ABOUT_CAMERA = 101;
        int PERMISSION_CONTACT = 102;
        int PERMISSION_CUSTOM_CAMERA = 103;
        int PERMISSION_LOCATION = 104;

        int CAMERA_ACT = 500;

    }


    public interface AUTH_CHANNEL {
        String FACEBOOK = "FA";
        String KAKAOTALK = "KA";
        String EMAIL = "EM";
        String TWITTER = "TW";
        String LINE = "LI";
        String WECHAT = "WI";
        String WATSAPP = "WA";
        String INSTAGRAM = "IN";
    }


    public interface FACE {
        String LIP = "1";
        String EYE = "2";
        String EYEBORW = "3";
        String CHEEK = "4";
        String CONTOUR = "5";
        String SKIN = "6";
    }



    public interface MAIN_TAB {
        String HOME = "HOME";
        String COUPON = "COUPON";
    }

    public interface SNSTYPE_CODE    {
        String FACEBOOK = "facebook";
        String KAKAOTALK = "kakao";
        String EMAIL = "email";
    }

    public interface OS {
        String ANDROID = "android";
    }

    public interface LIST_TYPE {
        String WONDERPLE = "wonderple";
        String WONDER = "wonder";
    }

    public interface FILTER_TYPE    {
        int BRIGHTNESS = 0;
        int CONTRAST = 1;
        int TEMPERATURE = 2;
        int DARK_BACKGROUND = 3;
        int ROTATE = 4;
        int FLIP = 5;
    }

    public interface FLIP_TYPE {
        int FLIP_VERTICAL = 1;
        int FLIP_HORIZONTAL = 2;
    }

    public interface NEWS_TYPE {
        String LIKE = "l";
        String WONDER = "w";
        String FOLLOWED = "f";
        String APPROVAL_FOLLOW = "f2";
        String ITEM_TALK = "i";
        String POST = "c";
    }



}
