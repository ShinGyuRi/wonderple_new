package kr.co.easterbunny.wonderple.library.gcm.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonObject;

import java.io.IOException;

import kr.co.easterbunny.wonderple.library.WonderpleLib;
import kr.co.easterbunny.wonderple.library.gcm.GcmUtil;
import kr.co.easterbunny.wonderple.library.util.JSLog;
import kr.co.easterbunny.wonderple.library.util.NetworkUtil;
import kr.co.easterbunny.wonderple.model.SignInResult;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by jinsin on 16. 7. 12..
 */
public class RegistrationIntentService extends IntentService {

    private static final String TAG = "RegIntentService";
    private static final String[] TOPICS = {"global"};

    public RegistrationIntentService() {
        super(TAG);
    }

    private String token;


    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            token = FirebaseInstanceId.getInstance().getToken();
            JSLog.E(TAG, "GCM Registration Token: " + token, new Throwable());
            sendRegistrationToServer(token);
        } catch (Exception e) {
            JSLog.E(TAG, "Failed to complete token refresh " + e.getMessage(), new Throwable());
        }
        Intent registrationComplete = new Intent(GcmUtil.REGISTRATION_COMPLETE);
        intent.putExtra("token", token);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    /**
     * Persist registration to third-party servers.
     * <p/>
     * Modify this method to associate the user's GCM registration token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    public void sendRegistrationToServer(String token) {
        WonderpleLib.getInstance().func01_loadUserDataToFile(RegistrationIntentService.this);
        SignInResult userData = WonderpleLib.getInstance().func01_loadUserDataFromMemory();

        String uid = userData.getUser().getUdid();
        String picture = userData.getUser().getImage();

        Call<JsonObject> jsonObjectCall = NetworkUtil.getInstace().pushRegister(uid, token, picture);
        jsonObjectCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject jsonObject = response.body();

                String message = jsonObject.get("message").toString().replace("\"", "");
                JSLog.E("Push token result : "+message, new Throwable());
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    /**
     * Subscribe to any GCM topics of interest, as defined by the TOPICS constant.
     *
     * @param token GCM token
     * @throws IOException if unable to reach the GCM PubSub service
     */
    // [START subscribe_topics]
    private void subscribeTopics(String token) throws IOException {
//        GcmPubSub pubSub = GcmPubSub.getInstance(this);
//        for (String topic : TOPICS) {
//            pubSub.subscribe(token, "/topics/" + topic, null);
//        }
    }

}
