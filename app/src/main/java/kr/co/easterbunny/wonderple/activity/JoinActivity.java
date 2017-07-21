package kr.co.easterbunny.wonderple.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;

import com.google.gson.JsonObject;

import kr.co.easterbunny.wonderple.R;
import kr.co.easterbunny.wonderple.databinding.ActivityJoinBinding;
import kr.co.easterbunny.wonderple.library.ParentActivity;
import kr.co.easterbunny.wonderple.library.WonderpleLib;
import kr.co.easterbunny.wonderple.library.util.JSLog;
import kr.co.easterbunny.wonderple.library.util.NetworkUtil;
import kr.co.easterbunny.wonderple.library.util.SHA256Util;
import kr.co.easterbunny.wonderple.library.util.TextUtil;
import kr.co.easterbunny.wonderple.library.util.Toaster;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static kr.co.easterbunny.wonderple.library.util.Definitions.SNSTYPE_CODE.EMAIL;

public class JoinActivity extends ParentActivity {

    private ActivityJoinBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_join);
        binding.setJoin(this);

        binding.btnConfirm.setEnabled(false);
        binding.tvWarning.setVisibility(View.GONE);
        binding.imgEmailCheck.setVisibility(View.GONE);
        binding.imgNameCheck.setVisibility(View.GONE);
        binding.imgPasswordCheck.setVisibility(View.GONE);
        binding.imgPasswordConfirmCheck.setVisibility(View.GONE);

        isCheck();
        setTextViewColor();
    }

    private void isCheck() {
        binding.etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtil.isEmailCheck(s.toString())) {
                    binding.imgEmailCheck.setVisibility(View.VISIBLE);
                    binding.tvWarning.setVisibility(View.GONE);
                } else if (!TextUtil.isEmailCheck(s.toString())) {
                    binding.imgEmailCheck.setVisibility(View.GONE);
                    binding.tvWarning.setText(getString(R.string.str_warning_email));
                    binding.tvWarning.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.etNickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 2) {
                    binding.imgNameCheck.setVisibility(View.VISIBLE);
                    binding.tvWarning.setVisibility(View.GONE);
                } else if ((s.length() <= 1) || (s.length() >= 20)) {
                    binding.imgNameCheck.setVisibility(View.GONE);
                    binding.tvWarning.setText(getString(R.string.str_warning_nickname));
                    binding.tvWarning.setVisibility(View.VISIBLE);
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
                if (TextUtil.isPassworkdCheck(s.toString())) {
                    binding.imgPasswordCheck.setVisibility(View.VISIBLE);
                    binding.tvWarning.setVisibility(View.GONE);
                } else if (!TextUtil.isPassworkdCheck(s.toString())) {
                    binding.imgPasswordCheck.setVisibility(View.GONE);
                    checkPasswordValidation(s.toString(), binding.tvWarning);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.etPasswordConfirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (binding.etPassword.getText().toString().equals(binding.etPasswordConfirm.getText().toString())) {
                    binding.imgPasswordConfirmCheck.setVisibility(View.VISIBLE);
                    binding.tvWarning.setVisibility(View.GONE);
                } else {
                    binding.imgPasswordConfirmCheck.setVisibility(View.GONE);
                    binding.tvWarning.setText(getString(R.string.str_warning_not_same));
                    binding.tvWarning.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String name = binding.etNickname.getText().toString();
                String passwordConfirm = binding.etPasswordConfirm.getText().toString();

                if (!TextUtil.isNull(name) && binding.etPassword.getText().toString().equals(passwordConfirm)) {
                    binding.btnConfirm.setEnabled(checkValidation(binding.etEmail, binding.etPassword));
                }
            }
        });
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.btn_report:
                requestSignUp();
                break;
        }
    }

    private void setTextViewColor() {

        Spannable spannable = Spannable.Factory.getInstance().newSpannable(getString(R.string.str_signup_policy));
        String text = spannable.toString();

        int start = text.indexOf(getString(R.string.str_signup_policy_event_text));
        int end = start + getString(R.string.str_signup_policy_event_text).length();

        spannable.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                JSLog.D("span onClick TEST!!!!!!!!!!!!!!!!!!!!!", new Throwable());
                startActivity(new Intent(JoinActivity.this, PolicyActivity.class));
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.linkColor = ContextCompat.getColor(JoinActivity.this, R.color.text_policy_event);
                super.updateDrawState(ds);
            }
        }, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        binding.tvPolicy.setText(spannable);
        binding.tvPolicy.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void requestSignUp() {

        String email = binding.etEmail.getText().toString();
        String name = binding.etNickname.getText().toString();
        String password = SHA256Util.getEncrypt(binding.etPassword.getText().toString());
        String type = EMAIL;

        Call<JsonObject> jsonObjectCall = NetworkUtil.getInstace().signUpEmail(email, name, password, type);
        jsonObjectCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject jsonObject = response.body();

                String message = jsonObject.get("message").toString().replace("\"", "");

                if ("successfully added account".equals(message)) {
                    String guide = email+getString(R.string.str_email_certification_guide);

                    WonderpleLib.getInstance().func04_showSimplDialog(JoinActivity.this, guide, getString(R.string.str_confirm), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            JoinActivity.this.finish();
                        }
                    });
                } else if ("this nickname already exists".equals(message)) {
                    Toaster.shortToast(getString(R.string.str_signup_toast_name));
                } else if ("this email already exists".equals(message)) {
                    Toaster.shortToast(getString(R.string.str_signup_toast_email));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }
}
