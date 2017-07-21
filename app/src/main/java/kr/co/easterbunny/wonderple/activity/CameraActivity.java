package kr.co.easterbunny.wonderple.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashSet;

import kr.co.easterbunny.wonderple.R;
import kr.co.easterbunny.wonderple.adapter.ViewPagerAdapter;
import kr.co.easterbunny.wonderple.databinding.ActivityCameraBinding;
import kr.co.easterbunny.wonderple.event.ClickEvent;
import kr.co.easterbunny.wonderple.fragment.CapturePhotoFragment;
import kr.co.easterbunny.wonderple.fragment.GalleryPickerFragment;
import kr.co.easterbunny.wonderple.library.ParentActivity;
import kr.co.easterbunny.wonderple.listener.CapturePhotoFragmentListener;
import kr.co.easterbunny.wonderple.listener.GalleryPickerFragmentListener;
import kr.co.easterbunny.wonderple.model.SourceType;
import kr.co.easterbunny.wonderple.view.ToolbarView;

public class CameraActivity extends ParentActivity implements ToolbarView.OnClickTitleListener,
        ToolbarView.OnClickNextListener, ToolbarView.OnClickBackListener, CapturePhotoFragmentListener, GalleryPickerFragmentListener {

    private ActivityCameraBinding binding;

    private HashSet<SourceType> mSourceTypeSet = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_camera);

        mSourceTypeSet.add(SourceType.Gallery);
        mSourceTypeSet.add(SourceType.Photo);

        initViews();
    }

    private void initViews()    {

        binding.mMainToolbar.setOnClickBackMenuListener(this)
                .setOnClickTitleListener(this)
                .setOnClickNextListener(this);

        final ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), getListFragment());
        binding.mMainViewPager.setAdapter(pagerAdapter);

        binding.mMainTabLayout.addOnTabSelectedListener(getViewPagerOnTabSelectedListener());
        binding.mMainViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.mMainTabLayout));

        binding.mMainViewPager.setCurrentItem(0);
    }

    private ArrayList<Fragment> getListFragment()   {
        ArrayList<Fragment> fragments = new ArrayList<>();

        if (mSourceTypeSet.contains(SourceType.Gallery)) {
            fragments.add(GalleryPickerFragment.newInstance());
            binding.mMainTabLayout.addTab(binding.mMainTabLayout.newTab().setText(R.string.tab_gallery));
        }

        if (mSourceTypeSet.contains(SourceType.Photo)) {
            fragments.add(CapturePhotoFragment.newInstance());
            binding.mMainTabLayout.addTab(binding.mMainTabLayout.newTab().setText(R.string.tab_photo));
        }

        return fragments;
    }

    private TabLayout.ViewPagerOnTabSelectedListener getViewPagerOnTabSelectedListener()    {
        return new TabLayout.ViewPagerOnTabSelectedListener(binding.mMainViewPager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
                displayTitleByTab(tab);
                initNextButtonByTab(tab.getPosition());
            }
        };
    }

    private void displayTitleByTab(TabLayout.Tab tab) {
        if (tab.getText() != null) {
            String title = tab.getText().toString();
            binding.mMainToolbar.setTitle(title);
        }
    }

    private void initNextButtonByTab(int position) {
        switch (position) {
            case 0:
                binding.mMainToolbar.showNext();
                break;
            case 1:
                binding.mMainToolbar.hideNext();
                break;
            default:
                binding.mMainToolbar.hideNext();
                break;
        }
    }

    private void openPhotoEditor() {
        startActivity(new Intent(this, EditorActivity.class));
    }

    @Override
    public void openEditor() {
        openPhotoEditor();
    }

    @Override
    public void onClickBack() {
        onBackPressed();
    }

    @Override
    public void onClickNext() {
        EventBus.getDefault().post(new ClickEvent.ClickNextEvent(true));
    }

    @Override
    public void onClickTitle() {

    }
}
