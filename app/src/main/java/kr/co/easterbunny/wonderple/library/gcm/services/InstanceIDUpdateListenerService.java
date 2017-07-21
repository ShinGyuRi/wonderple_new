package kr.co.easterbunny.wonderple.library.gcm.services;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import kr.co.easterbunny.wonderple.library.gcm.GcmUtil;

/**
 * Created by jinsin on 16. 7. 12..
 */
public class InstanceIDUpdateListenerService extends FirebaseInstanceIdService {
    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. This call is initiated by the
     * InstanceID provider.
     */

    String TAG = InstanceIDUpdateListenerService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {

        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
    }
}
