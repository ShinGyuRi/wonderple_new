package kr.co.easterbunny.wonderple.library.util;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * Created by scona on 2017-03-02.
 */

public class SHA256Util {


    public static String getEncrypt(String str) {


        byte hash[];

        try{

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(str.getBytes("UTF-8"));

            hash = digest.digest();


        }catch(NoSuchAlgorithmException e){
            e.printStackTrace();
            hash = null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            hash = null;
        }

        return Base64.encodeToString(hash, Base64.DEFAULT);
    }



}
