package kr.co.easterbunny.wonderple.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.ColorFilterTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import kr.co.easterbunny.wonderple.R;
import kr.co.easterbunny.wonderple.databinding.ViewTagPouchBinding;
import kr.co.easterbunny.wonderple.event.ClickEvent;
import kr.co.easterbunny.wonderple.event.ClickTagItem;
import kr.co.easterbunny.wonderple.model.CustomBitmapPool;
import kr.co.easterbunny.wonderple.model.LoadTagPouchResult;

/**
 * Created by scona on 2017-05-11.
 */

public class TagPouchAdapter extends RecyclerView.Adapter<TagPouchAdapter.BindingHolder> {

    private Context context;
    private List<LoadTagPouchResult.FacePart> faceParts = new ArrayList<>();

    public interface OnItemCheckListener {
        void onItemCheck(LoadTagPouchResult.FacePart facePart);
        void onItemUncheck(LoadTagPouchResult.FacePart facePart);
    }

    @NonNull
    private OnItemCheckListener onItemCheckListener;


    public class BindingHolder extends RecyclerView.ViewHolder{
        private ViewTagPouchBinding binding;

        public BindingHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        public ViewTagPouchBinding getBinding() {
            return binding;
        }

        public void onClick(View view) {

        }
    }

    public TagPouchAdapter(Context context, List<LoadTagPouchResult.FacePart> faceParts, @NonNull OnItemCheckListener onItemCheckListener) {
        this.context = context;
        this.faceParts = faceParts;
        this.onItemCheckListener = onItemCheckListener;
    }


    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_tag_pouch, parent, false);
        return new BindingHolder(view);
    }

    @Override
    public void onBindViewHolder(BindingHolder holder, int position) {
        if (faceParts != null) {
            final LoadTagPouchResult.FacePart currentItem = faceParts.get(position);

            String imgUrl = faceParts.get(position).getImageUrl();
            String brandname = faceParts.get(position).getBrandName();
            String itemName = faceParts.get(position).getItemName();
            int red = Integer.parseInt(faceParts.get(position).getRed());
            int green = Integer.parseInt(faceParts.get(position).getGreen());
            int blue = Integer.parseInt(faceParts.get(position).getBlue());
            String colorName = faceParts.get(position).getColorName();

            Glide.with(context)
                    .load(imgUrl)
                    .into(holder.binding.imgTagItem);

            holder.binding.tvBrandName.setText("[" + brandname + "]");
            holder.binding.tvItemName.setText(itemName);

            Glide.with(context)
                    .load(R.drawable.circle_black)
                    .bitmapTransform(new ColorFilterTransformation(new CustomBitmapPool(), Color.rgb(red, green, blue)), new CropCircleTransformation(new CustomBitmapPool()))
                    .into(holder.binding.imgItemColor);

            holder.binding.tvColorName.setText(colorName);

            holder.binding.btnSelectBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        holder.binding.btnSelectBox.setBackgroundResource(R.drawable.itemtag_p);
                        onItemCheckListener.onItemCheck(currentItem);
                    } else {
                        holder.binding.btnSelectBox.setBackgroundResource(0);
                        onItemCheckListener.onItemUncheck(currentItem);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (faceParts != null) {
            return faceParts.size();
        }
        return 0;
    }

}
