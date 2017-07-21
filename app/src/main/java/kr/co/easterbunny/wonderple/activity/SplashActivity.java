package kr.co.easterbunny.wonderple.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.JsonObject;

import kr.co.easterbunny.wonderple.R;
import kr.co.easterbunny.wonderple.library.ParentActivity;
import kr.co.easterbunny.wonderple.library.WonderpleLib;
import kr.co.easterbunny.wonderple.library.gcm.GcmUtil;
import kr.co.easterbunny.wonderple.library.gcm.services.RegistrationIntentService;
import kr.co.easterbunny.wonderple.library.util.Definitions.PREFKEY;
import kr.co.easterbunny.wonderple.library.util.JSLog;
import kr.co.easterbunny.wonderple.library.util.NetworkUtil;
import kr.co.easterbunny.wonderple.library.util.PrefUtil;
import kr.co.easterbunny.wonderple.library.util.TextUtil;
import kr.co.easterbunny.wonderple.library.util.Util;
import kr.co.easterbunny.wonderple.model.SignInResult;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends ParentActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        Util.runDelay(2000, new Runnable() {
            @Override
            public void run() {
                checkVisitInfo();
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    public synchronized void checkVisitInfo()    {

        WonderpleLib.getInstance().func01_loadUserDataToFile(SplashActivity.this);
        SignInResult userData = WonderpleLib.getInstance().func01_loadUserDataFromMemory();

        String email = userData.getUser().getEmail();
        String name = userData.getUser().getUsername();
        String password = userData.getUser().getPassword();
        String type = userData.getUser().getSnstype();
        String idnum = userData.getUser().getSnsid();
        String picture = userData.getUser().getImage();

        boolean isVisited = PrefUtil.getInstance().getBooleanPreference(PREFKEY.IS_VISIT_EXPERIENCE_BOOL);

        if (isVisited) {

            if (!"email".equals(type)) {
                snsLoginCheck(this, name, type, idnum, picture);
            } else if ("email".equals(type)){
                emailLoginCheck(this, email, type, password);
            }

        } else {

            moveLoginActivity(this);

        }

    }




}
