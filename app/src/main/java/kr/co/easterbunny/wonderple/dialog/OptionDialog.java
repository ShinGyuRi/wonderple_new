package kr.co.easterbunny.wonderple.dialog;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import com.google.gson.JsonObject;

import kr.co.easterbunny.wonderple.R;
import kr.co.easterbunny.wonderple.databinding.DlgOptionBinding;
import kr.co.easterbunny.wonderple.library.WonderpleLib;
import kr.co.easterbunny.wonderple.library.util.NetworkUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by scona on 2017-03-23.
 */

public class OptionDialog extends Dialog {

    private DlgOptionBinding binding;

    private Context context;

    String uid = null;
    String auid = null;
    String iid = null;

    public OptionDialog(@NonNull Context context, String uid, String auid, String iid) {
        super(context);
        this.context = context;
        this.uid = uid;
        this.auid = auid;
        this.iid = iid;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dlg_option, null, false);
        setContentView(binding.getRoot());
        binding.setOption(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_follow:
                WonderpleLib.getInstance().follow(uid, auid);
                dismiss();
                break;

            case R.id.btn_report:
                displayReport();
                cancel();
                break;
        }
    }


    private void displayReport() {
        Call<JsonObject> jsonObjectCall = NetworkUtil.getInstace().checkCall(iid, uid);
        jsonObjectCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject jsonObject = response.body();
                String message = jsonObject.get("message").toString().replace("\"", "");

                if ("a callin is selected".equals(message)) {
                    ReviseOptionDialog dialog = new ReviseOptionDialog(context, iid, uid);
                    dialog.show();

                } else {
                    ReportDialog dialog = new ReportDialog(context, iid, uid, "0");
                    dialog.show();
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }
}
