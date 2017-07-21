package kr.co.easterbunny.wonderple.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.gson.JsonObject;
import com.kakao.auth.AuthType;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.TalkProtocol;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import kr.co.easterbunny.wonderple.R;
import kr.co.easterbunny.wonderple.databinding.ActivityLoginBinding;
import kr.co.easterbunny.wonderple.library.ParentActivity;
import kr.co.easterbunny.wonderple.library.WonderpleLib;
import kr.co.easterbunny.wonderple.library.util.Definitions;
import kr.co.easterbunny.wonderple.library.util.Definitions.ACTIVITY_REQUEST_CODE;
import kr.co.easterbunny.wonderple.library.util.JSLog;
import kr.co.easterbunny.wonderple.library.util.NetworkUtil;
import kr.co.easterbunny.wonderple.library.util.PrefUtil;
import kr.co.easterbunny.wonderple.model.SignInResult;
import kr.co.easterbunny.wonderple.model.User;
import kr.co.easterbunny.wonderple.modules.PermissionModule;
import kr.co.easterbunny.wonderple.sdk.KakaoSessionCallbackListener;
import kr.co.easterbunny.wonderple.sdk.SessionCallback;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static kr.co.easterbunny.wonderple.library.util.Definitions.SNSTYPE_CODE.FACEBOOK;
import static kr.co.easterbunny.wonderple.library.util.Definitions.SNSTYPE_CODE.KAKAOTALK;

public class LoginActivity extends ParentActivity implements KakaoSessionCallbackListener {


    public static final String TAG = LoginActivity.class.getSimpleName();


    private ActivityLoginBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.setLogin(this);

        PermissionModule permissionModule = new PermissionModule(this);
        permissionModule.checkPermissions();
    }


    public void login(View v) {
        switch (v.getId()) {
            case R.id.btn_facebookLogin:
                facebookLogin();
                break;
            case R.id.btn_kakaoLogin:
                kakaoLogin();
                break;
            case R.id.btn_emailLogin:
                startActivity(new Intent(LoginActivity.this, LoginEmailActivity.class));
                break;
        }
    }





    /***************************************
     * Facebook 로그인
     ***************************************/
    private CallbackManager callbackManager;
    private String facebookReferenceId = null;
    private String facebookImagePath = null;
    private String facebookName = null;
    private String facebookEmail = null;


    public void facebookLogin() {

        LoginManager.getInstance().logOut();

        List<String> permissionNeeds = Arrays.asList("public_profile", "email", "user_birthday", "user_friends");
        LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, permissionNeeds);

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                String myInfoString = String.valueOf(loginResult);

                Log.d("JIN", "페이스북 로그인 성공! ");
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject json, GraphResponse response) {

                        if (response.getError() != null) {
                            // handle error
                            Log.d("JIN", "facebook 그래프 API 내 정보 가져오기 에러 : " + response.getError().getErrorMessage());
                        } else {
                            Log.d("JIN", "페이스북 정보 가져오기 성공!!");
                            try {

                                String jsonresult = String.valueOf(json);
                                Log.d("JIN", "페이스북 로그인 결과 JSON -> String ----> " + jsonresult);


                                String str_email = json.getString("email");
                                final String str_id = json.getString("id");
                                String str_firstname = json.getString("first_name");
                                String str_lastname = json.getString("last_name");


                                /// 페이스북 로그인 및 개인정보 가져오기 성공 --> 프로필 이미지 다운로드 후 사인업 필요
                                final String facebook_user_profile_image_url = "http://graph.facebook.com/" + str_id + "/picture?type=large";


                                facebookEmail = str_email;
                                facebookReferenceId = str_id;
                                facebookName = json.getString("name");
                                facebookImagePath = facebook_user_profile_image_url;


                                facebookLoginCheck();


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                });


                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday,first_name,last_name");
                request.setParameters(parameters);
                request.executeAsync();


            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }


    public void facebookLoginCheck() {
        JSLog.D(facebookEmail + facebookReferenceId + facebookName + facebookImagePath, new Throwable());
        snsLoginCheck(this, facebookName, FACEBOOK, facebookReferenceId, facebookImagePath);
    }


    /**********************************************
     * 카카오톡 로그인
     **********************************************/
    private SessionCallback callBack;
    private UserProfile kakaoUserProfile;


    @Override
    public void onSessionResult(KakaoException exception) {
        if (exception == null) {
            requestMe();
        } else {
        }
    }


    public void kakaoLogin() {
        if (kakaoUserProfile != null) {
            kakaoLoginCheck(kakaoUserProfile);
            return;
        }


        callBack = new SessionCallback(this);
        Session.getCurrentSession().addCallback(callBack);
        Session.getCurrentSession().checkAndImplicitOpen();


        if (Session.getCurrentSession().isOpened()) {
            requestMe();
        } else {
            if (TalkProtocol.existCapriLoginActivityInTalk(this, Session.getCurrentSession().isProjectLogin())) {
                Session.getCurrentSession().open(AuthType.KAKAO_TALK, this);
            } else {
                Session.getCurrentSession().open(AuthType.KAKAO_ACCOUNT, this);
            }
        }
    }


    public void kakaoLoginCheck(UserProfile userProfile) {
        if (userProfile == null) {
            return;
        }
        snsLoginCheck(this, userProfile.getNickname(), KAKAOTALK, userProfile.getId()+"", userProfile.getProfileImagePath());
    }


    /**
     * 카카오톡 프로필 조회
     */
    protected void requestMe() {
        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                JSLog.D(new Throwable());
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                JSLog.D(new Throwable());
            }

            @Override
            public void onSuccess(UserProfile userProfile) {
                JSLog.D(new Throwable());
                //null체크 중요!!!!!! -> null체크 안하면 onSuccess 호출이 여러번 들어오기때문에 회원가입화면이 복수개로 뜬다
                if (kakaoUserProfile == null) {
                    kakaoUserProfile = userProfile;
                    JSLog.D(TAG + "ID:" + userProfile.getId(), null);
                    JSLog.D(TAG + "NICKNAME:" + userProfile.getNickname(), null);
                    JSLog.D(TAG + "PROFILE IMAGE PATH:" + userProfile.getProfileImagePath(), null);
                    kakaoLoginCheck(userProfile);
                }
            }

            @Override
            public void onNotSignedUp() {
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        } else {
            switch (requestCode) {
                case ACTIVITY_REQUEST_CODE.FACEBOOK_CONNECT:
                    if (resultCode == RESULT_OK) {
                        callbackManager.onActivityResult(requestCode, resultCode, data);
                    }
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (callBack != null) Session.getCurrentSession().removeCallback(callBack);
    }
}
