package kr.co.easterbunny.wonderple.view;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.lang.ref.WeakReference;

import kr.co.easterbunny.wonderple.R;
import kr.co.easterbunny.wonderple.databinding.ViewMediaItemBinding;
import kr.co.easterbunny.wonderple.listener.MediaItemViewListener;
import kr.co.easterbunny.wonderple.modules.ReboundModule;
import kr.co.easterbunny.wonderple.modules.ReboundModuleDelegate;


public class MediaItemView extends RelativeLayout implements ReboundModuleDelegate {

    private ViewMediaItemBinding binding;

    private Uri mCurrentUri;
    private ReboundModule mReboundModule = ReboundModule.getInstance(this);
    private WeakReference<MediaItemViewListener> mWrListener;

    public void setListener(MediaItemViewListener listener) {
        this.mWrListener = new WeakReference<>(listener);
    }

    public MediaItemView(Context context) {
        super(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        binding = ViewMediaItemBinding.inflate(inflater, this, true);
        
    }

    public void bind(Uri uri) {
        mCurrentUri = uri;
        mReboundModule.init(binding.mMediaThumb);
        Glide.with(getContext())
                .load(uri.toString())
                .override(400, 400)
                .placeholder(R.drawable.placeholder_media)
                .error(R.drawable.placeholder_error_media)
                .into(binding.mMediaThumb);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    @Override
    public void onTouchActionUp() {
        mWrListener.get().onClickItem(mCurrentUri);
    }
}
