package kr.co.easterbunny.wonderple.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import kr.co.easterbunny.wonderple.R;
import kr.co.easterbunny.wonderple.databinding.ViewRequestFollowItemBinding;
import kr.co.easterbunny.wonderple.library.util.NetworkUtil;
import kr.co.easterbunny.wonderple.model.LoadFollowNewsResult;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by scona on 2017-06-01.
 */

public class RequestFollowAdapter extends RecyclerView.Adapter<RequestFollowAdapter.BindingHolder> {

    private Context context;
    private List<LoadFollowNewsResult.Follow> followList = new ArrayList<>();
    private String uid;

    public RequestFollowAdapter(Context context, List<LoadFollowNewsResult.Follow> followList, String uid) {
        this.context = context;
        this.followList = followList;
        this.uid = uid;
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_request_follow_item, parent, false);
        return new BindingHolder(view);
    }

    @Override
    public void onBindViewHolder(BindingHolder holder, int position) {
        Glide.with(context)
                .load(followList.get(position).getProfileImage())
                .into(holder.binding.imgProfile);

        holder.binding.tvUsername.setText(followList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return followList == null ? 0 : followList.size();
    }

    public class BindingHolder extends RecyclerView.ViewHolder {

        private ViewRequestFollowItemBinding binding;

        public BindingHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_admit:
                    followConfirm(uid, followList.get(getAdapterPosition()).getAuid());
                    break;

                case R.id.btn_delete:
                    followDelete(uid, followList.get(getAdapterPosition()).getAuid());
                    break;
            }
        }
    }

    private void followConfirm(String uid, String auid) {
        Call<JsonObject> jsonObjectCall = NetworkUtil.getInstace().followConfirm(uid, auid);
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

    private void followDelete(String uid, String auid) {
        Call<JsonObject> jsonObjectCall = NetworkUtil.getInstace().followDelete(uid, auid);
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
