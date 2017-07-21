package kr.co.easterbunny.wonderple.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.JsonObject;

import kr.co.easterbunny.wonderple.library.util.NetworkUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by scona on 2017-03-27.
 */

public class ParentDialog extends Dialog {
    public ParentDialog(@NonNull Context context) {
        super(context);
    }

    public void sendReport(String iid, String uid, String reasons, String theCase) {
        Call<JsonObject> jsonObjectCall = NetworkUtil.getInstace().callImage(iid, uid, reasons, theCase);
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

    public void sendCommentReport(String cid, String uid, String reasons, String theCase) {
        Call<JsonObject> jsonObjectCall = NetworkUtil.getInstace().callInComment(cid, uid, reasons, theCase);
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
}
