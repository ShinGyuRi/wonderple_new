package kr.co.easterbunny.wonderple.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.zomato.photofilters.imageprocessors.Filter;

import java.io.ByteArrayOutputStream;
import java.lang.ref.WeakReference;

import kr.co.easterbunny.wonderple.databinding.ViewEffectItemBinding;
import kr.co.easterbunny.wonderple.listener.EffectItemViewListener;
import kr.co.easterbunny.wonderple.model.Thumbnail;
import kr.co.easterbunny.wonderple.modules.ReboundModule;
import kr.co.easterbunny.wonderple.modules.ReboundModuleDelegate;

/**
 * Created by scona on 2017-02-13.
 */

public class EffectItemView extends LinearLayout implements ReboundModuleDelegate{

    private ViewEffectItemBinding binding;

    private Context context;


    private ReboundModule mReboundModule = ReboundModule.getInstance(this);
    private WeakReference<EffectItemViewListener> mWrListener;
    private Filter mFilter;

    public EffectItemView(Context context) {
        super(context);
        this.context = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        binding = ViewEffectItemBinding.inflate(inflater, this, true);
    }

    public void bind(Thumbnail thumbnail) {
        mReboundModule.init(binding.mEffectTypeView);
        binding.mEffectName.setText(thumbnail.name);

        // TODO change text color if isSelected

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        thumbnail.image.compress(Bitmap.CompressFormat.PNG, 100, stream);

        Glide.with(context)
                .load(stream.toByteArray())
                .centerCrop()
                .fitCenter()
                .into(binding.mEffectTypeView);

//        binding.mEffectTypeView.setImageBitmap(thumbnail.image);
        mFilter = thumbnail.filter;
    }

    public void setListener(EffectItemViewListener listener) {
        this.mWrListener = new WeakReference<>(listener);
    }

    @Override
    public void onTouchActionUp() {
        mWrListener.get().onClickEffectType(mFilter);
    }
}
