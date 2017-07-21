package kr.co.easterbunny.wonderple.library.gcm.managers;

import android.content.Context;

/**
 * Created by jinsin on 16. 7. 12..
 */
public class PreferenceManager extends BasePreferenceManager{

    private static PreferenceManager instance;


    public static PreferenceManager instance(Context context)
    {
        if (instance == null)
        {
            synchronized (PreferenceManager.class)
            {
                if (instance == null)
                {
                    instance = new PreferenceManager(context);
                }
            }
        }
        return instance;
    }


    private PreferenceManager(Context context)
    {
        super(context);
    }

    private static final String SENT_TOKEN_TO_SERVER = "sent_token_to_server";

    public void sentToken(boolean sent)
    {
        put(SENT_TOKEN_TO_SERVER, sent);
    }

    public boolean sentToken()
    {
        return get(SENT_TOKEN_TO_SERVER, false);
    }
}
