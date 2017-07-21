package kr.co.easterbunny.wonderple.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.lang.ref.WeakReference;
import java.util.List;

import kr.co.easterbunny.wonderple.R;
import kr.co.easterbunny.wonderple.databinding.ViewMediaItemBinding;
import kr.co.easterbunny.wonderple.library.util.JSLog;
import kr.co.easterbunny.wonderple.listener.GridAdapterListener;
import kr.co.easterbunny.wonderple.listener.MediaItemViewListener;
import kr.co.easterbunny.wonderple.modules.ReboundModule;
import kr.co.easterbunny.wonderple.modules.ReboundModuleDelegate;
import kr.co.easterbunny.wonderple.view.MediaItemView;

/**
 * Created by scona on 2017-02-02.
 */

public class GalleryAdapter extends RecyclerViewAdapterBase<Uri, MediaItemView> implements MediaItemViewListener {


    private Context context;

    private WeakReference<GridAdapterListener> mWrListener;

    public GalleryAdapter(Context context) {
        this.context = context;
    }


    @Override
    protected MediaItemView onCreateItemView(ViewGroup parent, int viewType) {
        return new MediaItemView(context);
    }


    @Override
    public void onBindViewHolder(ViewWrapper<MediaItemView> holder, int position) {
        MediaItemView itemView = holder.getView();
        itemView.setListener(this);
        itemView.bind(mItems.get(position));
    }


    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public Uri getItem(int position) {
        return mItems.get(position);
    }

    public void setListener(GridAdapterListener listener) {
        this.mWrListener = new WeakReference<>(listener);
    }

    @Override
    public void onClickItem(Uri uri) {
        mWrListener.get().onClickMediaItem(uri);
    }
}
