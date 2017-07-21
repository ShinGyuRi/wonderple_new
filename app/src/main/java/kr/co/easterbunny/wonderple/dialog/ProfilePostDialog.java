package kr.co.easterbunny.wonderple.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import com.google.gson.JsonObject;

import kr.co.easterbunny.wonderple.R;
import kr.co.easterbunny.wonderple.databinding.DlgProfilePostBinding;
import kr.co.easterbunny.wonderple.library.WonderpleLib;
import kr.co.easterbunny.wonderple.library.util.NetworkUtil;
import kr.co.easterbunny.wonderple.library.util.Toaster;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by scona on 2017-04-20.
 */

public class ProfilePostDialog extends ParentDialog {
    private DlgProfilePostBinding binding;

    private Context context;
    private String uid, iid;

    public ProfilePostDialog(@NonNull Context context, String uid, String iid) {
        super(context);
        this.context = context;
        this.uid = uid;
        this.iid = iid;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dlg_profile_post, null, false);
        setContentView(binding.getRoot());
        binding.setProfilePost(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_post_delete:
                postDelete();
                break;

            case R.id.btn_set_profile:
                setProfile();
                break;
        }
    }

    private void postDelete() {
        dismiss();
        WonderpleLib.getInstance().func04_showSimplSelect2Dialog(context, context.getString(R.string.str_do_remove_post), context.getString(R.string.str_confirm), context.getString(R.string.str_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Call<JsonObject> jsonObjectCall = NetworkUtil.getInstace().deleteImage(iid);
                jsonObjectCall.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        JsonObject jsonObject = response.body();
                        String message = jsonObject.get("messgae").toString().replace("\"", "");

                        if ("image deleted".equals(message)) {
                            Toaster.shortToast("삭제가 완료되었습니다.");
                        }

                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                    }
                });
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }

    private void setProfile() {
        dismiss();
        WonderpleLib.getInstance().func04_showSimplSelect2Dialog(context, context.getString(R.string.str_do_set_profile_image), context.getString(R.string.str_confirm), context.getString(R.string.str_cancel), new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String typeOf = "2";
                Call<JsonObject> jsonObjectCall = NetworkUtil.getInstace().setProfile(uid, iid, typeOf);
                jsonObjectCall.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        JsonObject jsonObject = response.body();
                        String message = jsonObject.get("message").toString().replace("\"", "");
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                    }
                });
            }
        }, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }
}
