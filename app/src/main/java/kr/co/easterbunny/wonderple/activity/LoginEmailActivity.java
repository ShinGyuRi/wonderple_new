package kr.co.easterbunny.wonderple.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import com.google.gson.JsonObject;

import kr.co.easterbunny.wonderple.R;
import kr.co.easterbunny.wonderple.databinding.ActivityLoginEmailBinding;
import kr.co.easterbunny.wonderple.library.ParentActivity;
import kr.co.easterbunny.wonderple.library.WonderpleLib;
import kr.co.easterbunny.wonderple.library.util.JSLog;
import kr.co.easterbunny.wonderple.library.util.NetworkUtil;
import kr.co.easterbunny.wonderple.library.util.SHA256Util;
import kr.co.easterbunny.wonderple.library.util.TextUtil;
import kr.co.easterbunny.wonderple.model.SignInResult;
import kr.co.easterbunny.wonderple.model.User;
import kr.co.easterbunny.wonderple.view.CustomTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static kr.co.easterbunny.wonderple.library.util.Definitions.SNSTYPE_CODE.EMAIL;

public class LoginEmailActivity extends ParentActivity {

    private ActivityLoginEmailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login_email);
        binding.setLoginEmail(this);

        binding.tvWarning.setVisibility(View.GONE);
        binding.imgEmailCheck.setVisibility(View.GONE);
        binding.imgPasswordCheck.setVisibility(View.GONE);
        binding.btnLogin.setEnabled(false);

        isCheck();
    }

    private void isCheck() {
        binding.etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!(s.length() == 0)) {
//                    binding.imgEmailCheck.setVisibility(View.VISIBLE);


                    if (TextUtil.isEmailCheck(s.toString())) {
                        binding.imgEmailCheck.setVisibility(View.VISIBLE);
                    }
                    else if (!TextUtil.isEmailCheck(s.toString())) {
                        binding.imgEmailCheck.setVisibility(View.GONE);
                    }


                } else {
                    binding.imgEmailCheck.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!(s.length() == 0)) {
//                    binding.imgPasswordCheck.setVisibility(View.VISIBLE);


                    if (TextUtil.isPassworkdCheck(s.toString())) {
                        binding.imgPasswordCheck.setVisibility(View.VISIBLE);
                    }
                    else if (!TextUtil.isPassworkdCheck(s.toString())) {
                        binding.imgPasswordCheck.setVisibility(View.GONE);
                    }


                } else {
                    binding.imgPasswordCheck.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                binding.btnLogin.setEnabled(checkValidation(binding.etEmail, binding.etPassword));
            }
        });
    }



    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.btn_login:
                String password = binding.etPassword.getText().toString();
                if (checkPasswordValidation(password, binding.tvWarning)) {
                    email = binding.etEmail.getText().toString();
                    type = EMAIL;
                    encPassword = SHA256Util.getEncrypt(password).trim();
                    emailLogin();
                }
                break;
            case R.id.btn_join:
                startActivity(new Intent(this, JoinActivity.class));
                break;
            case R.id.tv_forgot_password:
                startActivity(new Intent(this, FindPasswordActivity.class));
                break;
        }
    }


    /**********************************************
     * 이메일 로그인
     **********************************************/
    private String email;
    private String type;
    private String encPassword;


    //테스트중
    public void emailLogin() {

        SignInResult signInResult = new SignInResult();
        User user = new User();

        JSLog.D("encPassword ::::: " + encPassword, new Throwable());

        Call<JsonObject> jsonObjectCall = NetworkUtil.getInstace().loginCheckEmail(email, type, encPassword);
        jsonObjectCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JSLog.D(new Throwable());
                JsonObject jsonObject = response.body();

                String result = jsonObject.get("result").toString().replace("\"", "");
                String message = jsonObject.get("message").toString().replace("\"", "");

                signInResult.setResult(result);
                signInResult.setMessage(message);

                if ("login successfully checked".equals(message)) {

                    JsonObject userObject = jsonObject.get("user").getAsJsonObject();

                    String uid = userObject.get("uid").toString().replace("\"", "");

                    user.setEmail(email);
                    user.setImage(null);
                    user.setSnsid(null);
                    user.setSnstype(type);
                    user.setUdid(uid);
                    user.setUsername(null);
                    user.setPassword(encPassword);
                    signInResult.setUser(user);

                    WonderpleLib.getInstance().func01_saveUserDataToFile(LoginEmailActivity.this, signInResult);

                    moveMainActivity(LoginEmailActivity.this);

                } else if ("this is an account before email verification".equals(message)) {
                } else if ("account does not exist".equals(message)) {
                } else if ("your email verification period has expired".equals(message)) {
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                JSLog.D(new Throwable());
            }
        });
    }
}
