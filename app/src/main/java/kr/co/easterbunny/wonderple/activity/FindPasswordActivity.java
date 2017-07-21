package kr.co.easterbunny.wonderple.activity;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.google.gson.JsonObject;

import kr.co.easterbunny.wonderple.R;
import kr.co.easterbunny.wonderple.databinding.ActivityFindPasswordBinding;
import kr.co.easterbunny.wonderple.library.util.NetworkUtil;
import kr.co.easterbunny.wonderple.library.util.TextUtil;
import kr.co.easterbunny.wonderple.library.util.Toaster;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FindPasswordActivity extends AppCompatActivity {

    private ActivityFindPasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_find_password);
        binding.setFind(this);

        binding.tvWarning.setVisibility(View.GONE);
        binding.imgEmailCheck.setVisibility(View.GONE);
        binding.btnConfirm.setEnabled(false);

        isCheck();
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
                    binding.btnConfirm.setEnabled(true);
                } else if (!TextUtil.isEmailCheck(s.toString())) {
                    binding.imgEmailCheck.setVisibility(View.GONE);
                    binding.tvWarning.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_report:
                requestPassword();
        }
    }

    private void requestPassword() {
        String email = binding.etEmail.getText().toString();

        Call<JsonObject> jsonObjectCall = NetworkUtil.getInstace().pwRequest(email);
        jsonObjectCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject jsonObject = response.body();

                String message = jsonObject.get("message").toString().replace("\"", "");

                if ("complete".equals(message)) {
                    FindPasswordActivity.this.finish();
                } else if ("account does not exist".equals(message)) {
                    Toaster.shortToast(getString(R.string.str_find_password_toast_email));
                } else if ("this is an account before email verification".equals(message)) {
                    Toaster.shortToast(getString(R.string.str_find_password_toast_certification));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }
}
