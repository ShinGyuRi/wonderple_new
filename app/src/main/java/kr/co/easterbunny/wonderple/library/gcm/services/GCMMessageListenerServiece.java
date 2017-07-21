package kr.co.easterbunny.wonderple.library.gcm.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Objects;

import kr.co.easterbunny.wonderple.R;
import kr.co.easterbunny.wonderple.activity.SplashActivity;


/**
 * Created by jinsin on 16. 7. 12..
 */
public class GCMMessageListenerServiece extends FirebaseMessagingService {

    public static final String TAG = GCMMessageListenerServiece.class.getSimpleName();

    private static final int NOTIFICATION_ID = 91732846;

    @Override
    public void onMessageReceived(RemoteMessage message)
    {
        String from = message.getFrom();
        Map data = message.getData();
//        Log.e(TAG, "from : " + from);
//        Log.e(TAG, "msg : " + data.get("msg"));




        for (Object key : data.keySet())
        {
            Object value = data.get(key);
            Log.i(TAG, String.format("%s : %s (%s)", key, value.toString(), value.getClass().getName()));
        }



    }



}
