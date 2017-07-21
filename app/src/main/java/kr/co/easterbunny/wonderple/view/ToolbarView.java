package kr.co.easterbunny.wonderple.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import java.lang.ref.WeakReference;

import kr.co.easterbunny.wonderple.R;

/**
 * Created by scona on 2017-01-31.
 */

public class ToolbarView extends RelativeLayout implements View.OnClickListener{


    CustomTextView mTitle;
    CustomTextView mIconBack;
    CustomTextView mIconNext;


    private WeakReference<OnClickBackListener> mWrBackMenuListener;
    private WeakReference<OnClickNextListener> mWrNextListener;
    private WeakReference<OnClickTitleListener> mWrTitleListener;



    public ToolbarView(Context context) {
        super(context);
        init(context);
    }

    public ToolbarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    private void init(Context context)  {
        View view = View.inflate(context, R.layout.view_toolbar, this);
        mIconNext = (CustomTextView) view.findViewById(R.id.mIconNext);
        mIconBack = (CustomTextView) view.findViewById(R.id.mIconBack);
        mTitle = (CustomTextView) view.findViewById(R.id.mTitle);
        mIconBack.setOnClickListener(this);
        mIconNext.setOnClickListener(this);
        mTitle.setOnClickListener(this);
    }

    public ToolbarView setOnClickBackMenuListener(OnClickBackListener listener) {
        this.mWrBackMenuListener = new WeakReference<>(listener);
        return this;
    }

    public ToolbarView setOnClickTitleListener(OnClickTitleListener listener) {
        this.mWrTitleListener = new WeakReference<>(listener);
        return this;
    }

    public ToolbarView setOnClickNextListener(OnClickNextListener listener) {
        this.mWrNextListener = new WeakReference<>(listener);
        return this;
    }

    public ToolbarView setTitle(String title) {
        mTitle.setText(title);
        return this;
    }

    public ToolbarView hideNext() {
        mIconNext.setVisibility(GONE);
        return this;
    }

    public ToolbarView showNext() {
        mIconNext.setVisibility(VISIBLE);
        return this;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mIconBack:
                mWrBackMenuListener.get().onClickBack();
                break;
            case R.id.mTitle:
                mWrTitleListener.get().onClickTitle();
                break;
            case R.id.mIconNext:
                mWrNextListener.get().onClickNext();
                break;
        }
    }

    public interface OnClickBackListener {
        void onClickBack();
    }

    public interface OnClickNextListener {
        void onClickNext();
    }

    public interface OnClickTitleListener {
        void onClickTitle();
    }
}
