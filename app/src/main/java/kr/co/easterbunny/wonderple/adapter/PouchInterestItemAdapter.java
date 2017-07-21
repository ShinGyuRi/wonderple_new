package kr.co.easterbunny.wonderple.adapter;

import android.content.Context;
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
import kr.co.easterbunny.wonderple.databinding.ViewTagPouchBinding;
import kr.co.easterbunny.wonderple.model.CustomBitmapPool;
import kr.co.easterbunny.wonderple.model.LoadPouchResult;

/**
 * Created by scona on 2017-06-07.
 */

public class PouchInterestItemAdapter extends RecyclerView.Adapter<PouchInterestItemAdapter.BindingHolder> {

    private Context context;
    private List<LoadPouchResult.Favorite> favorites = new ArrayList<>();

    public PouchInterestItemAdapter(Context context, List<LoadPouchResult.Favorite> favorites) {
        this.context = context;
        this.favorites = favorites;
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_tag_pouch, parent, false);

        return new BindingHolder(view);
    }

    @Override
    public void onBindViewHolder(BindingHolder holder, int position) {
        int r = Integer.parseInt(favorites.get(position).getR());
        int g = Integer.parseInt(favorites.get(position).getG());
        int b = Integer.parseInt(favorites.get(position).getB());

        Glide.with(context)
                .load(favorites.get(position).getImage())
                .into(holder.binding.imgTagItem);

        Glide.with(context)
                .load(R.drawable.posting_tagbox)
                .bitmapTransform(new ColorFilterTransformation(new CustomBitmapPool(), Color.rgb(r, g, b)), new CropCircleTransformation(new CustomBitmapPool()))
                .into(holder.binding.imgItemColor);

        holder.binding.tvBrandName.setText("[" + favorites.get(position).getBrandName() + "]");
        holder.binding.tvItemName.setText(favorites.get(position).getItemName());
        holder.binding.tvColorName.setText(favorites.get(position).getColoName());
    }

    @Override
    public int getItemCount() {
        return favorites == null ? 0 : favorites.size();
    }

    public class BindingHolder extends RecyclerView.ViewHolder {

        private ViewTagPouchBinding binding;

        public BindingHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

}
