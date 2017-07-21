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

import kr.co.easterbunny.wonderple.R;
import kr.co.easterbunny.wonderple.databinding.ViewStaggeredgridItemBinding;
import kr.co.easterbunny.wonderple.library.ParentActivity;
import kr.co.easterbunny.wonderple.library.WonderpleLib;
import kr.co.easterbunny.wonderple.model.LoadDiaryResult;

/**
 * Created by scona on 2017-06-08.
 */

public class RecommendPostAdapter extends RecyclerView.Adapter<RecommendPostAdapter.BindingHolder> {

    private Context context;
    private List<LoadDiaryResult.Post> posts = new ArrayList<>();
    private String uid;

    public RecommendPostAdapter(Context context, List<LoadDiaryResult.Post> posts, String uid) {
        this.context = context;
        this.posts = posts;
        this.uid = uid;
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_staggeredgrid_item, parent, false);

        return new BindingHolder(view);
    }

    @Override
    public void onBindViewHolder(BindingHolder holder, int position) {
        String positionHeight = posts.get(position).getRatio();

        holder.binding.postImage.setHeightRatio(Double.parseDouble(positionHeight));
        Glide.with(context)
                .load(posts.get(position).getImageUrl())
                .thumbnail(0.1f)
                .into(holder.binding.postImage);
    }

    @Override
    public int getItemCount() {
        return posts == null ? 0 : posts.size();
    }

    public class BindingHolder extends RecyclerView.ViewHolder {

        private ViewStaggeredgridItemBinding binding;

        public BindingHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            binding.setStaggeredGridItem(this);
        }

        public void onClick(View view) {
            switch (view.getId()) {
               case R.id.post_image:
                   ((ParentActivity) context).movePostDetailActivity(context, WonderpleLib.getInstance().func01_loadUid(context), uid, posts.get(getAdapterPosition()).getIid(), posts.get(getAdapterPosition()).getImageUrl());
                   break;
            }
        }
    }

}
