package kr.co.easterbunny.wonderple.sdk;

import com.kakao.auth.ISessionCallback;
import com.kakao.util.exception.KakaoException;

import kr.co.easterbunny.wonderple.library.util.JSLog;

/**
 * Created by scona on 2017-01-16.
 */

public class SessionCallback implements ISessionCallback {


    private KakaoSessionCallbackListener kakaoSessionCallbackListener;

    public SessionCallback() {
    }

    public SessionCallback(KakaoSessionCallbackListener kakaoSessionCallbackListener) {
        this.kakaoSessionCallbackListener = kakaoSessionCallbackListener;
    }

    @Override
    public void onSessionOpened() {
        JSLog.D("KakaoTalk Login onSessionOpened()", null);
        if (kakaoSessionCallbackListener != null)
            kakaoSessionCallbackListener.onSessionResult(null);
    }

    @Override
    public void onSessionOpenFailed(KakaoException exception) {
        JSLog.D("KakaoTalk Login onSessionOpenedFailed()", null);
        if (kakaoSessionCallbackListener != null) {
            if (exception == null) exception = new KakaoException("Session Close");
            kakaoSessionCallbackListener.onSessionResult(exception);
        }

    }
}
