package kr.co.easterbunny.wonderple.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.MotionEvent;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubfilter;
import com.zomato.photofilters.imageprocessors.subfilters.ColorOverlaySubfilter;
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubfilter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashSet;

import kr.co.easterbunny.wonderple.R;
import kr.co.easterbunny.wonderple.adapter.ViewPagerAdapter;
import kr.co.easterbunny.wonderple.databinding.ActivityEditorBinding;
import kr.co.easterbunny.wonderple.event.ClickEvent;
import kr.co.easterbunny.wonderple.event.ConfirmEvent;
import kr.co.easterbunny.wonderple.event.FilterEvent;
import kr.co.easterbunny.wonderple.fragment.AdjustFragment;
import kr.co.easterbunny.wonderple.fragment.FilterFragment;
import kr.co.easterbunny.wonderple.library.ParentActivity;
import kr.co.easterbunny.wonderple.library.util.Definitions.FILTER_TYPE;
import kr.co.easterbunny.wonderple.library.util.FileUtil;
import kr.co.easterbunny.wonderple.library.util.JSLog;
import kr.co.easterbunny.wonderple.listener.onKeyBackPressedListener;
import kr.co.easterbunny.wonderple.model.EditType;
import kr.co.easterbunny.wonderple.model.Session;
import kr.co.easterbunny.wonderple.view.ToolbarView;

import static kr.co.easterbunny.wonderple.library.util.Definitions.FLIP_TYPE.FLIP_HORIZONTAL;
import static kr.co.easterbunny.wonderple.library.util.FileUtil.flip;
import static kr.co.easterbunny.wonderple.library.util.FileUtil.getBitmapFromFile;
import static kr.co.easterbunny.wonderple.library.util.FileUtil.getTempImageFile;
import static kr.co.easterbunny.wonderple.library.util.FileUtil.rotateImage;
import static kr.co.easterbunny.wonderple.library.util.FileUtil.saveBitmap;

public class EditorActivity extends ParentActivity implements ToolbarView.OnClickTitleListener,
        ToolbarView.OnClickNextListener, ToolbarView.OnClickBackListener {

    static {
        System.loadLibrary("NativeImageProcessor");
    }

    private ActivityEditorBinding binding;

    private Session mSession = Session.getInstance();

    private Filter mCurrentFilter = null;
    private int filterType = -1;

    private HashSet<EditType> mSourceTypeSet = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_editor);
        overridePendingTransition(R.anim.left, R.anim.zoom_out);

        EventBus.getDefault().register(this);

        mSourceTypeSet.add(EditType.Filter);
        mSourceTypeSet.add(EditType.Adjust);

        binding.setEditor(this);

        initViews();
    }

    @Override
    public void onBackPressed() {
        if (mOnKeyBackPressedListener != null) {
            mOnKeyBackPressedListener.onBack();
            binding.editTabLayout.setVisibility(View.VISIBLE);
            binding.layoutConfirm.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
            overridePendingTransition(R.anim.right, R.anim.zoom_out);
        }
    }

    private void initViews() {
        binding.mEditorToolbar.setOnClickBackMenuListener(this)
                .setOnClickTitleListener(this)
                .setOnClickNextListener(this)
                .setTitle(getResources().getString(R.string.toolbar_title_editor))
                .showNext();




//        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) binding.mEffectPreview.getLayoutParams();
//        lp.height = getResources().getDisplayMetrics().widthPixels;
//        binding.mEffectPreview.setLayoutParams(lp);

//        binding.mEffectChooserRecyclerView.setItemAnimator(new DefaultItemAnimator());
//        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
//        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//        binding.mEffectChooserRecyclerView.setLayoutManager(mLayoutManager);
//
//        final EffectAdapter effectAdapter = new EffectAdapter(this);
//        effectAdapter.setListener(this);
//        binding.mEffectChooserRecyclerView.setAdapter(effectAdapter);

//        Glide.with(this)
//                .load(mSession.getFileToUpload())
//                .into(binding.mEffectPreview);

        binding.mEffectPreview.setImageBitmap(getBitmapFromFile(EditorActivity.this, getTempImageFile(EditorActivity.this)));


        Glide.with(this)
                .load(R.drawable.bg_black)
                .into(binding.imgBackground);

        binding.imgBackground.setAlpha(0f);

        binding.mEffectPreview.setOnTouchListener(setOnTouchListener());

        final ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), getListFragment());
        binding.editViewPager.setAdapter(pagerAdapter);

        binding.editTabLayout.addOnTabSelectedListener(getViewPagerOnTabSelectedListener());
        binding.editViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.editTabLayout));

        binding.editViewPager.setCurrentItem(0);

//        effectAdapter.setItems(getFilters());

    }

    private ArrayList<Fragment> getListFragment() {
        ArrayList<Fragment> fragments = new ArrayList<>();

        if (mSourceTypeSet.contains(EditType.Filter)) {
            fragments.add(FilterFragment.newInstance());
            binding.editTabLayout.addTab(binding.editTabLayout.newTab().setText(R.string.tab_filter));
        }

        if (mSourceTypeSet.contains(EditType.Adjust)) {
            fragments.add(AdjustFragment.newInstance());
            binding.editTabLayout.addTab(binding.editTabLayout.newTab().setText(R.string.tab_adjust));
        }
        return fragments;
    }

    private TabLayout.ViewPagerOnTabSelectedListener getViewPagerOnTabSelectedListener()    {
        return new TabLayout.ViewPagerOnTabSelectedListener(binding.editViewPager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
            }
        };
    }

    private View.OnTouchListener setOnTouchListener() {
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int action = motionEvent.getAction();
                if (mCurrentFilter != null) {
                    switch (action) {
                        case MotionEvent.ACTION_DOWN:
                            binding.mEffectPreview.setImageBitmap(getBitmapFromFile(EditorActivity.this, getTempImageFile(EditorActivity.this)));
                            binding.imgBackground.setAlpha(0f);
                            break;
                        case MotionEvent.ACTION_UP:
                            binding.mEffectPreview.setImageBitmap(mCurrentFilter.processFilter(getBitmapFromFile(EditorActivity.this, mSession.getFileToUpload())));
                            break;
                        default:
                            break;
                    }
                }

                return true;
            }
        };
    }

    @Override
    public void onClickBack() {
        this.onBackPressed();
    }

    @Override
    public void onClickNext() {
        Intent intent = new Intent(this, UploadActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClickTitle() {

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel:
                binding.mEffectPreview.setImageBitmap(getBitmapFromFile(this, mSession.getFileToUpload()));
                binding.layoutConfirm.setVisibility(View.GONE);
                binding.editTabLayout.setVisibility(View.VISIBLE);
                EventBus.getDefault().post(new ConfirmEvent(false));
                break;

            case R.id.btn_confirm:
                if (filterType == FILTER_TYPE.DARK_BACKGROUND) {
                    mSession.setFileToUpload(saveBitmap(((GlideBitmapDrawable) binding.mEffectPreview.getDrawable()).getBitmap(), FileUtil.getNewFilePath()));
                }else
                    mSession.setFileToUpload(saveBitmap(((BitmapDrawable) binding.mEffectPreview.getDrawable()).getBitmap(), FileUtil.getNewFilePath()));
                binding.mEffectPreview.setImageBitmap(getBitmapFromFile(this, mSession.getFileToUpload()));
                binding.layoutConfirm.setVisibility(View.GONE);
                binding.editTabLayout.setVisibility(View.VISIBLE);
                EventBus.getDefault().post(new ConfirmEvent(true));
                break;
        }

    }

    @Subscribe
    public void eventBus(Filter filter) {
        if (filter != mCurrentFilter) {
            mCurrentFilter = filter;
            binding.mEffectPreview.setImageBitmap(filter.processFilter(getBitmapFromFile(EditorActivity.this, getTempImageFile(this))));
        }
    }

    @Subscribe
    public void filterEventBus(FilterEvent filterEvent) {

        int progress = filterEvent.getProgress();
        filterType = filterEvent.getFilterType();

        if (filterEvent.getFilterType() == FILTER_TYPE.BRIGHTNESS) {
            Filter brightnessFilter = new Filter();
            brightnessFilter.addSubFilter(new BrightnessSubfilter(progress - 50));

            mCurrentFilter = brightnessFilter;
            binding.mEffectPreview.setImageBitmap(brightnessFilter.processFilter(getBitmapFromFile(EditorActivity.this, mSession.getFileToUpload())));
        }

        if (filterEvent.getFilterType() == FILTER_TYPE.CONTRAST) {
            if ((float) (progress+50) / 50 > 1) {
                Filter contrastFilter = new Filter();
                contrastFilter.addSubFilter(new ContrastSubfilter((float) (progress + 50) / 50));

                mCurrentFilter = contrastFilter;
                binding.mEffectPreview.setImageBitmap(contrastFilter.processFilter(getBitmapFromFile(EditorActivity.this, mSession.getFileToUpload())));
            }
        }

        if (filterEvent.getFilterType() == FILTER_TYPE.TEMPERATURE) {
            Filter temperatureFilter = new Filter();

            if (progress > 50) {
                temperatureFilter.addSubFilter(new ColorOverlaySubfilter(1, progress - 50, (progress - 50) / 2, 0f));
            } else if (progress < 50) {
                temperatureFilter.addSubFilter(new ColorOverlaySubfilter(1, 0f, (progress - 50) * -1 / 2, (progress - 50) * -1));
            }

            mCurrentFilter = temperatureFilter;
            binding.mEffectPreview.setImageBitmap(temperatureFilter.processFilter(getBitmapFromFile(EditorActivity.this, mSession.getFileToUpload())));
        }

        if (filterEvent.getFilterType() == FILTER_TYPE.DARK_BACKGROUND) {
            mCurrentFilter = new Filter();
            binding.imgBackground.setAlpha((float) progress * 0.6f / 100);
        }

        if (filterEvent.getFilterType() == FILTER_TYPE.ROTATE) {
            binding.mEffectPreview.setImageBitmap(rotateImage(((BitmapDrawable) binding.mEffectPreview.getDrawable()).getBitmap(), 90));
        }

        if (filterEvent.getFilterType() == FILTER_TYPE.FLIP) {
            binding.mEffectPreview.setImageBitmap(flip(((BitmapDrawable) binding.mEffectPreview.getDrawable()).getBitmap(), FLIP_HORIZONTAL));
        }
    }

    @Subscribe
    public void clickedSeekBar(ClickEvent clickEvent) {
        if (clickEvent.isClicked()) {
            binding.layoutConfirm.setVisibility(View.VISIBLE);
            binding.editTabLayout.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private onKeyBackPressedListener mOnKeyBackPressedListener;

    public void setOnKeyBackPressedListener(onKeyBackPressedListener listener) {
        mOnKeyBackPressedListener = listener;
    }
}
