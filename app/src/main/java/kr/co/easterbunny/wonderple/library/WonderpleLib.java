package kr.co.easterbunny.wonderple.library;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.gson.JsonObject;

import kr.co.easterbunny.wonderple.library.util.NetworkUtil;
import kr.co.easterbunny.wonderple.model.SignInResult;
import kr.co.easterbunny.wonderple.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by kimjeonghwi on 2016. 7. 14..
 */
public class WonderpleLib
{
    private static WonderpleLib sharedWonderpleLibObj;
    private SharedPreferences.Editor shardPrefEd;
    SharedPreferences sharedPref;

    SignInResult currentUser;

    public static final String LOGIN_TYPE_WONDERPLE = "WonderpleUser";
    public static final String LOGIN_TYPE_FACEBOOK_USER = "FacebookUser";
    public static final String LOGIN_TYPE_KAKAO = "KakaoUser";





    public static WonderpleLib getInstance()
    {
        if(sharedWonderpleLibObj == null)
        {
            sharedWonderpleLibObj = new WonderpleLib();
        }

        return sharedWonderpleLibObj;
    }


    public void func00_runAfterDelay(Runnable runnable, long delayTime)
    {
        Handler mHandler = new Handler();
        mHandler.postDelayed(runnable,delayTime);
    }



    /// 사용자의 데이터를 파일로 저장한다
    public void func01_saveUserDataToFile(Context context, SignInResult signInResult)
    {

        SharedPreferences.Editor editor = getSharedPrefEdit(context);
        if(signInResult != null)
        {


            if(signInResult.user.email != null)
            {
                editor.putString("email",signInResult.user.email);
            }

            if(signInResult.user.username != null)
            {
                editor.putString("username",signInResult.user.username);
            }


            if(signInResult.user.image != null)
            {
                editor.putString("image",signInResult.user.image);
            }

            if(signInResult.user.snsid != null)
            {
                editor.putString("snsid",signInResult.user.snsid);
            }

            if(signInResult.user.snstype != null)
            {
                editor.putString("snstype",signInResult.user.snstype);
            }

            if(signInResult.user.udid != null)
            {
                editor.putString("udid",signInResult.user.udid);
            }

            if(signInResult.user.password != null)
            {
                editor.putString("password",signInResult.user.password);
            }



            editor.commit();



        }
        else
        {
            Log.d("JIN"," func01_saveUserData --> 잘못된 API 호출입니다.");
            return ;
        }
    }


    public SignInResult func01_loadUserDataToFile(Context context)
    {
        SignInResult makedSignInResult = new SignInResult();

        SharedPreferences pref = getSharedPref(context);

        String email = pref.getString("email","");
        String username = pref.getString("username","");
        String image = pref.getString("image","");
        String snsid = pref.getString("snsid","");
        String snstype = pref.getString("snstype","");
        String udid = pref.getString("udid","");
        String password = pref.getString("password","");



        makedSignInResult.user = new User();
        makedSignInResult.user.email = email;
        makedSignInResult.user.username = username;
        makedSignInResult.user.image = image;
        makedSignInResult.user.snsid = snsid;
        makedSignInResult.user.snstype = snstype;
        makedSignInResult.user.udid = udid;
        makedSignInResult.user.password = password;


        Log.d("JIN","func01_loadUserDataToFile 확인 0002 : udid --> "+udid);

        currentUser = makedSignInResult;
        return currentUser;


    }


    public SignInResult func01_loadUserDataFromMemory()
    {
        return currentUser;
    }





    public String func01_loadUsername(Context context)
    {

        if(currentUser.user.username != null  && !"".equals(currentUser.user.username))
        {
            return currentUser.user.username;
        }

        return "";
    }

    public String func01_loadUid(Context context)
    {
        if (currentUser.user.udid != null && !"".equals(currentUser.user.udid)) {
            return currentUser.user.udid;
        }

        return "";
    }


    public String func01_getLoginTypeOfUser()
    {
        if(currentUser.user.snsid != null  && !"".equals(currentUser.user.snsid))
        {
            return LOGIN_TYPE_FACEBOOK_USER;
        }
        else if(currentUser.user.username != null  && !"".equals(currentUser.user.username))
        {
            return LOGIN_TYPE_WONDERPLE;
        }
        else if(currentUser.user.udid != null  && !"".equals(currentUser.user.udid))
        {
            return LOGIN_TYPE_KAKAO;
        }
        return "";
    }


    public void func01_userLogoutRemoveUserData(Context context)
    {
        currentUser = null;
        getSharedPrefEdit(context).putString("sessionToken","");
        getSharedPrefEdit(context).commit();


    }



    public boolean func02_isUserKeppSignInMode(Context context)
    {
        return getSharedPref(context).getBoolean("isUserKeepSignInMode",false);

    }

    public void func02_saveIsUserKeepSignInMode(Context context, boolean isKeep)
    {
        getSharedPrefEdit(context).putBoolean("isUserKeepSignInMode",isKeep);
        getSharedPrefEdit(context).commit();

    }










    public String func03_convertDateFromServerToApp(String dateString)
    {
        if(dateString != null && !"".equals(dateString) && dateString.contains("."))
        {
            String makedString = dateString.replace(".","/");
            return makedString;
        }

        return "";
    }





    public void func04_showSimplDialog(Context context, String message, String buttonMessage, DialogInterface.OnClickListener btnListener)
    {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setMessage(message);
        alertBuilder.setNegativeButton(buttonMessage,btnListener);
        alertBuilder.setCancelable(false);
        alertBuilder.create().show();

    }


    public void func04_showSimplSelect2Dialog(Context context, String message, String yesBtn, String noBtn, DialogInterface.OnClickListener yesBtnListener, DialogInterface.OnClickListener noBtnListener)
    {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setMessage(message);
        alertBuilder.setPositiveButton(yesBtn,yesBtnListener);
        alertBuilder.setNegativeButton(noBtn,noBtnListener);
        alertBuilder.setCancelable(false);
        alertBuilder.create().show();

    }












    public static void func07_setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }






    public static void follow(String uid, String auid) {
        Call<JsonObject> jsonObjectCall = NetworkUtil.getInstace().followApply(uid, auid);
        jsonObjectCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject jsonObject = response.body();
                String message = jsonObject.get("message").toString().replace("\"", "");
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }







    SharedPreferences getSharedPref(Context context)
    {
        if(sharedPref == null)
        {
            sharedPref = context.getSharedPreferences("WonderpleLib" , context.MODE_PRIVATE);
        }

        return sharedPref;

    }

    SharedPreferences.Editor getSharedPrefEdit(Context context)
    {
        if(shardPrefEd == null)
        {
            shardPrefEd = context.getSharedPreferences("WonderpleLib" , context.MODE_PRIVATE).edit();
        }

        return shardPrefEd;
    }
}
