package kr.co.easterbunny.wonderple.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import kr.co.easterbunny.wonderple.R;
import kr.co.easterbunny.wonderple.databinding.ViewCommentItemBinding;
import kr.co.easterbunny.wonderple.model.CustomBitmapPool;
import kr.co.easterbunny.wonderple.model.LoadDiaryResult;

/**
 * Created by scona on 2017-06-08.
 */

public class ItemTalkAdapter extends RecyclerView.Adapter<ItemTalkAdapter.BindingHolder> {

    private Context context;
    private List<LoadDiaryResult.Talk> talks = new ArrayList<>();

    public ItemTalkAdapter(Context context, List<LoadDiaryResult.Talk> talks) {
        this.context = context;
        this.talks = talks;
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_comment_item, parent, false);
        return new BindingHolder(view);
    }

    @Override
    public void onBindViewHolder(BindingHolder holder, int position) {
        Glide.with(context)
                .load(talks.get(position).getProfileImage())
                .bitmapTransform(new CropCircleTransformation(new CustomBitmapPool()))
                .into(holder.binding.imgProfile);

        holder.binding.tvUsername.setText(talks.get(position).getUsername());
        holder.binding.tvComment.setText(talks.get(position).getDetail());
        holder.binding.tvBeforeDay.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return talks == null ? 0 : talks.size();
    }

    public class BindingHolder extends RecyclerView.ViewHolder {

        private ViewCommentItemBinding binding;

        public BindingHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

}
