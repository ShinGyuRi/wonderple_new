package kr.co.easterbunny.wonderple.library.util;

import android.content.Context;
import android.content.SharedPreferences;



import kr.co.easterbunny.wonderple.library.BaseApplication;

/**
 * Created by scona on 2017-01-13.
 */

public class PrefUtil implements SharedPreferences.OnSharedPreferenceChangeListener{
    private SharedPreferences prefs;
    private SharedPreferences.Editor prefEditor;

    private static PrefUtil instance = null;

    public final static String SHARED_PREF = "SHARED_PREFERENCE";

    public static PrefUtil getInstance() {
        if (instance == null) {
            instance = new PrefUtil();
        }
        return instance;
    }

    private PrefUtil() {
        prefs = BaseApplication.getContext().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        prefEditor = prefs.edit();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }

    //put
    public void putPreference(String key, boolean value) {
        prefEditor.putBoolean(key, value);
        prefEditor.commit();
    }

    public void putPreference(String key, String value) {
        prefEditor.putString(key, value);
        prefEditor.commit();
    }

    public void putPreference(String key, int value) {
        prefEditor.putInt(key, value);
        prefEditor.commit();
    }

    public void putPreference(String key, long value) {
        prefEditor.putLong(key, value);
        prefEditor.commit();
    }

    //get
    public boolean getBooleanPreference(String key){
        return prefs.getBoolean(key, false);
    }

    public String getStringPreference(String key){
        return prefs.getString(key, null);
    }

    public int getIntPreference(String key){
        if(prefs.contains(key))
            return prefs.getInt(key, 0);
        else
            return 0;
    }
    public long getLongPreference(String key){
        return prefs.getLong(key,0);
    }

    public void removePreference(String key){
        prefEditor.remove(key);
        prefEditor.commit();
    }
}
