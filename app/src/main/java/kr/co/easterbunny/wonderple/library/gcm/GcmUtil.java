package kr.co.easterbunny.wonderple.library.gcm;

import android.app.Activity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import kr.co.easterbunny.wonderple.library.BaseApplication;
import kr.co.easterbunny.wonderple.library.util.Debug;


/**
 * Created by jinsin on 16. 7. 12..
 */
public class GcmUtil {

    private  static final String TAG = GcmUtil.class.getSimpleName();

    public static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    public static String REGISTRATION_COMPLETE = "REGISTRATION_COMPLETE";

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    public static boolean checkPlayServices(Activity activity) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(activity.getApplicationContext());
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(activity, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                activity.finish();
            }
            return false;
        }
        return true;
    }


    public boolean checkPlayServicesAvailable() {
        return getPlayServicesAvailabilityResultCode() == ConnectionResult.SUCCESS;
    }

    private int getPlayServicesAvailabilityResultCode() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        return apiAvailability.isGooglePlayServicesAvailable(BaseApplication.getInstance());
    }
}