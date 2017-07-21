package kr.co.easterbunny.wonderple.activity;

import android.databinding.DataBindingUtil;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashSet;

import kr.co.easterbunny.wonderple.R;
import kr.co.easterbunny.wonderple.adapter.ViewPagerAdapter;
import kr.co.easterbunny.wonderple.databinding.ActivityPolicyBinding;
import kr.co.easterbunny.wonderple.fragment.CapturePhotoFragment;
import kr.co.easterbunny.wonderple.fragment.GalleryPickerFragment;
import kr.co.easterbunny.wonderple.fragment.PolicyFragment;
import kr.co.easterbunny.wonderple.fragment.PrivacyFragment;
import kr.co.easterbunny.wonderple.fragment.TermsFragment;
import kr.co.easterbunny.wonderple.model.PolicyType;
import kr.co.easterbunny.wonderple.model.SourceType;

public class PolicyActivity extends AppCompatActivity {

    private ActivityPolicyBinding binding;

    private HashSet<PolicyType> mSourceTypeSet = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_policy);

        mSourceTypeSet.add(PolicyType.Privacy);
        mSourceTypeSet.add(PolicyType.Term);
        mSourceTypeSet.add(PolicyType.Policy);

        initViews();
    }

    private void initViews()    {

        final ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), getListFragment());
        binding.mMainViewPager.setAdapter(pagerAdapter);

        binding.mMainTabLayout.addOnTabSelectedListener(getViewPagerOnTabSelectedListener());
        binding.mMainViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.mMainTabLayout));

        binding.mMainViewPager.setCurrentItem(0);
    }

    private ArrayList<Fragment> getListFragment()   {
        ArrayList<Fragment> fragments = new ArrayList<>();

        if (mSourceTypeSet.contains(PolicyType.Privacy)) {
            fragments.add(PrivacyFragment.newInstance());
            binding.mMainTabLayout.addTab(binding.mMainTabLayout.newTab().setText(R.string.tab_privacy));
        }

        if (mSourceTypeSet.contains(PolicyType.Term)) {
            fragments.add(TermsFragment.newInstance());
            binding.mMainTabLayout.addTab(binding.mMainTabLayout.newTab().setText(R.string.tab_terms_use));
        }

        if (mSourceTypeSet.contains(PolicyType.Policy)) {
            fragments.add(PolicyFragment.newInstance());
            binding.mMainTabLayout.addTab(binding.mMainTabLayout.newTab().setText(R.string.tab_policy));
        }

        return fragments;
    }

    private TabLayout.ViewPagerOnTabSelectedListener getViewPagerOnTabSelectedListener()    {
        return new TabLayout.ViewPagerOnTabSelectedListener(binding.mMainViewPager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
            }
        };
    }



}
