package kr.co.easterbunny.wonderple.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Random;

import kr.co.easterbunny.wonderple.R;
import kr.co.easterbunny.wonderple.databinding.ViewStaggeredgridItemBinding;
import kr.co.easterbunny.wonderple.library.util.ImageUtil;
import kr.co.easterbunny.wonderple.library.util.JSLog;
import kr.co.easterbunny.wonderple.model.LoadProfileResult;

/**
 * Created by scona on 2017-01-26.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.BindingHolder> {

    private List<LoadProfileResult.PostImages> postImages;

    private Context context;


    public class BindingHolder extends RecyclerView.ViewHolder  {
        private final ViewStaggeredgridItemBinding binding;

        public BindingHolder(View view) {
            super(view);
            binding = DataBindingUtil.bind(view);
        }

        public ViewDataBinding getBinding() {
            return binding;
        }

    }


    public SearchAdapter(Context context, List<LoadProfileResult.PostImages> postImages) {
        this.context = context;
        this.postImages = postImages;
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_staggeredgrid_item, parent, false);
        return new BindingHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchAdapter.BindingHolder holder, int position) {

        if (postImages != null) {
            String positionHeight = postImages.get(position).getRatio();

            holder.binding.postImage.setHeightRatio(Double.parseDouble(positionHeight));
            Glide.with(context)
                    .load(postImages.get(position).getImageUrl())
                    .thumbnail(0.1f)
                    .into(holder.binding.postImage);
        }

    }


    @Override
    public int getItemCount() {
        if (postImages != null) {
            return postImages.size();
        }
        return 0;
    }

    public void add(List<LoadProfileResult.PostImages> nestPostImages) {
        postImages.addAll(nestPostImages);
        notifyDataSetChanged();
    }
}
