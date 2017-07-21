package kr.co.easterbunny.wonderple.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.List;

import kr.co.easterbunny.wonderple.R;
import kr.co.easterbunny.wonderple.databinding.ViewStaggeredgridItemBinding;
import kr.co.easterbunny.wonderple.databinding.ViewStaggeredgridWonderItemBinding;
import kr.co.easterbunny.wonderple.model.LoadProfileResult;

/**
 * Created by scona on 2017-04-18.
 */

public class WondersItemAdapter extends RecyclerView.Adapter<WondersItemAdapter.BindingHolder> {

    private List<LoadProfileResult.WonderImages> wonderImages;

    private Context context;

    public class BindingHolder extends RecyclerView.ViewHolder {
        private final ViewStaggeredgridWonderItemBinding binding;

        public BindingHolder(View view) {
            super(view);
            binding = DataBindingUtil.bind(view);
        }

        public ViewStaggeredgridWonderItemBinding getBinding() {
            return binding;
        }
    }

    public WondersItemAdapter(Context context, List<LoadProfileResult.WonderImages> wonderImages) {
        this.context = context;
        this.wonderImages = wonderImages;
    }


    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_staggeredgrid_wonder_item, parent, false);
        return new BindingHolder(view);
    }

    @Override
    public void onBindViewHolder(BindingHolder holder, int position) {
        if (wonderImages != null) {
            String positionHeight = wonderImages.get(position).getRatio();

            holder.binding.wonderImage.setHeightRatio(Double.parseDouble(positionHeight));
            Glide.with(context)
                    .load(wonderImages.get(position).getImageUrl())
                    .thumbnail(0.1f)
                    .into(holder.binding.wonderImage);

            holder.binding.tvWonderCount.setText(wonderImages.get(position).getWonderCount());
        }
    }

    @Override
    public int getItemCount() {
        if (wonderImages != null) {
            return wonderImages.size();
        }
        return 0;
    }

    public void add(List<LoadProfileResult.WonderImages> nestPostImages) {
        wonderImages.addAll(nestPostImages);
        notifyDataSetChanged();
    }
}
