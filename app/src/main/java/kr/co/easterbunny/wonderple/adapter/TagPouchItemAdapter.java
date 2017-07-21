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
import kr.co.easterbunny.wonderple.databinding.ViewTagPouchItemBinding;
import kr.co.easterbunny.wonderple.library.util.JSLog;
import kr.co.easterbunny.wonderple.model.CustomBitmapPool;
import kr.co.easterbunny.wonderple.model.LoadTagPouchResult;

/**
 * Created by scona on 2017-05-22.
 */

public class TagPouchItemAdapter extends RecyclerView.Adapter<TagPouchItemAdapter.BindingHolder> {

    private Context context;
    private List<LoadTagPouchResult.FacePart> faceParts = new ArrayList<>();

    public TagPouchItemAdapter(Context context, List<LoadTagPouchResult.FacePart> faceParts) {
        this.context = context;
        this.faceParts = faceParts;
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_tag_pouch_item, parent, false);
        return new BindingHolder(view);
    }

    @Override
    public void onBindViewHolder(BindingHolder holder, int position) {
        if (faceParts != null) {
            JSLog.D("TEST!!!!!!!!", new Throwable());
            Glide.with(context)
                    .load(faceParts.get(position).getImageUrl())
                    .into(holder.binding.imgTagItem);

            int r = Integer.parseInt(faceParts.get(position).getRed());
            int g = Integer.parseInt(faceParts.get(position).getGreen());
            int b = Integer.parseInt(faceParts.get(position).getBlue());

            Glide.with(context)
                    .load(R.drawable.circle_black)
                    .bitmapTransform(new ColorFilterTransformation(new CustomBitmapPool(), Color.rgb(r, g, b)), new CropCircleTransformation(new CustomBitmapPool()))
                    .into(holder.binding.imgItemColor);
        }

    }

    @Override
    public int getItemCount() {
        if (faceParts == null) {
            return 0;
        }
        return faceParts.size();
    }

    public class BindingHolder extends RecyclerView.ViewHolder {

        private ViewTagPouchItemBinding binding;

        public BindingHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        public ViewTagPouchItemBinding getBinding() {
            return binding;
        }
    }

    public void add(List<LoadTagPouchResult.FacePart> addItems) {
        if (addItems != null) {
            faceParts.addAll(addItems);
            notifyDataSetChanged();
        }
        JSLog.D("TEST!!!!!!!", new Throwable());
    }

}
