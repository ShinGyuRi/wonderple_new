package kr.co.easterbunny.wonderple.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.zomato.photofilters.imageprocessors.Filter;

import java.lang.ref.WeakReference;

import kr.co.easterbunny.wonderple.listener.EffectAdapterListener;
import kr.co.easterbunny.wonderple.listener.EffectItemViewListener;
import kr.co.easterbunny.wonderple.model.Thumbnail;
import kr.co.easterbunny.wonderple.view.EffectItemView;

/**
 * Created by scona on 2017-02-08.
 */

public class EffectAdapter extends RecyclerViewAdapterBase<Thumbnail, EffectItemView> implements EffectItemViewListener {


    private Context context;

    private WeakReference<EffectAdapterListener> mWrListener;


    public EffectAdapter(Context context) {
        this.context = context;
    }

    @Override
    protected EffectItemView onCreateItemView(ViewGroup parent, int viewType) {
        return new EffectItemView(context);
    }


    @Override
    public void onBindViewHolder(ViewWrapper<EffectItemView> holder, int position) {
        EffectItemView effectItemView = holder.getView();
        effectItemView.setListener(this);
        effectItemView.bind(mItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public Thumbnail getItem(int position) {
        return mItems.get(position);
    }

    public void setListener(EffectAdapterListener listener) {
        this.mWrListener = new WeakReference<>(listener);
    }

    @Override
    public void onClickEffectType(Filter filter) {
        mWrListener.get().applyEffectType(filter);
    }
}
