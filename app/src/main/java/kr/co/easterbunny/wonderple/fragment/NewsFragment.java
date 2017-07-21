package kr.co.easterbunny.wonderple.fragment;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashSet;

import kr.co.easterbunny.wonderple.R;
import kr.co.easterbunny.wonderple.adapter.ViewPagerAdapter;
import kr.co.easterbunny.wonderple.databinding.FragmentNewsBinding;
import kr.co.easterbunny.wonderple.library.ParentFragment;

public class NewsFragment extends ParentFragment {

    private FragmentNewsBinding binding;

    private HashSet<NewsType> newsTypes = new HashSet<>();

    private String uid;

    public enum NewsType {
        MyNews(),
        BeautyNews();

        NewsType () {}
    }

    public NewsFragment() {
    }

    @SuppressLint("ValidFragment")
    public NewsFragment(String uid) {
        this.uid = uid;
    }

    public static NewsFragment newInstance(String uid) {
        NewsFragment fragment = new NewsFragment(uid);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_news, container, false);
        View view = binding.getRoot();

        newsTypes.add(NewsType.BeautyNews);
        newsTypes.add(NewsType.MyNews);

        initViews();

        return view;
    }

    private void initViews() {
        final ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getChildFragmentManager(), getListFragment());
        binding.viewPager.setAdapter(pagerAdapter);

        binding.tabLayout.addOnTabSelectedListener(getViewPagerOnTabSelectedListener());
        binding.viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout));

        binding.viewPager.setCurrentItem(1);
    }

    private ArrayList<Fragment> getListFragment() {
        ArrayList<Fragment> fragments = new ArrayList<>();

        if (newsTypes.contains(NewsType.BeautyNews)) {
            fragments.add(BeautyNewsFragment.newInstance());
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText("뷰티 소식"));
        }

        if (newsTypes.contains(NewsType.MyNews)) {
            fragments.add(MyNewsFragment.newInstance(uid));
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText("내 소식"));
        }

        return fragments;
    }

    private TabLayout.ViewPagerOnTabSelectedListener getViewPagerOnTabSelectedListener() {
        return new TabLayout.ViewPagerOnTabSelectedListener(binding.viewPager)  {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
            }
        };
    }
}
