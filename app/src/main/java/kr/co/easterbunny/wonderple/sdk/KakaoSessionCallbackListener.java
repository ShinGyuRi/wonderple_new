package kr.co.easterbunny.wonderple.sdk;

import com.kakao.util.exception.KakaoException;

/**
 * Created by scona on 2017-01-16.
 */

public interface KakaoSessionCallbackListener {
    public void onSessionResult(KakaoException exception);
}
