package kr.co.easterbunny.wonderple.library.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import kr.co.easterbunny.wonderple.library.BaseApplication;

/**
 * Created by scona on 2017-01-13.
 */

public class VersionUtils {

    public static int getAppVersion() {
        return getAppPackageInfo().versionCode;
    }

    public static String getAppVersionName() {
        return getAppPackageInfo().versionName;
    }

    private static PackageInfo getAppPackageInfo() {
        Context context = BaseApplication.getInstance();
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }
}
