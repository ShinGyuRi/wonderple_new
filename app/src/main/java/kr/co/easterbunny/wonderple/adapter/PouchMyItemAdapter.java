package kr.co.easterbunny.wonderple.adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.ColorFilterTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import kr.co.easterbunny.wonderple.R;
import kr.co.easterbunny.wonderple.activity.ItemDiaryActivity;
import kr.co.easterbunny.wonderple.databinding.ViewPouchMyItemBinding;
import kr.co.easterbunny.wonderple.library.WonderpleLib;
import kr.co.easterbunny.wonderple.model.CustomBitmapPool;
import kr.co.easterbunny.wonderple.model.LoadPouchResult;

/**
 * Created by scona on 2017-06-02.
 */

public class PouchMyItemAdapter extends RecyclerView.Adapter<PouchMyItemAdapter.BindingHolder> {

    private Context context;
    private List<LoadPouchResult.MyItem> myItems = new ArrayList<>();
    private String uid, profileImage;

    public PouchMyItemAdapter(Context context, List<LoadPouchResult.MyItem> myItems, String uid, String profileImage) {
        this.context = context;
        this.myItems = myItems;
        this.uid = uid;
        this.profileImage = profileImage;
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_pouch_my_item, parent, false);
        return new BindingHolder(view);
    }

    @Override
    public void onBindViewHolder(BindingHolder holder, int position) {
        int r = Integer.parseInt(myItems.get(position).getR());
        int g = Integer.parseInt(myItems.get(position).getG());
        int b = Integer.parseInt(myItems.get(position).getB());

        Glide.with(context)
                .load(myItems.get(position).getImage())
                .into(holder.binding.imgItem);

        Glide.with(context)
                .load(R.drawable.posting_tagbox)
                .bitmapTransform(new ColorFilterTransformation(new CustomBitmapPool(), Color.rgb(r, g, b)), new CropCircleTransformation(new CustomBitmapPool()))
                .into(holder.binding.imgItemColor);

        holder.binding.tvBrandName.setText("[" + myItems.get(position).getBrandName() + "]");
        holder.binding.tvItemName.setText(myItems.get(position).getItemName());
        holder.binding.tvColorName.setText(myItems.get(position).getColorName());
        holder.binding.ratingBar.setRating(Integer.parseInt(myItems.get(position).getRate()));

        if (WonderpleLib.getInstance().func01_loadUid(context).equals(uid)) {
            holder.binding.ratingBar.setIsIndicator(false);
        }
    }

    @Override
    public int getItemCount() {
        return myItems == null ? 0 : myItems.size();
    }

    public class BindingHolder extends RecyclerView.ViewHolder {

        private ViewPouchMyItemBinding binding;

        public BindingHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            binding.setPouchMyItem(this);
        }

        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.img_item:
                    Intent intent = new Intent(context, ItemDiaryActivity.class);

                    intent.putExtra("uid", uid);
                    intent.putExtra("cid", myItems.get(getAdapterPosition()).getCid());
                    intent.putExtra("profileImage", profileImage);

                    context.startActivity(intent);
            }
        }
    }

}
