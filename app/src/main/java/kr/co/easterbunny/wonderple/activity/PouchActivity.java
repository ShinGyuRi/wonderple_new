package kr.co.easterbunny.wonderple.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import kr.co.easterbunny.wonderple.R;
import kr.co.easterbunny.wonderple.adapter.ViewPagerAdapter;
import kr.co.easterbunny.wonderple.databinding.ActivityPouchBinding;
import kr.co.easterbunny.wonderple.fragment.PouchInterestItemFragment;
import kr.co.easterbunny.wonderple.fragment.PouchItemSearchFragment;
import kr.co.easterbunny.wonderple.fragment.PouchMyItemFragment;
import kr.co.easterbunny.wonderple.library.ParentActivity;
import kr.co.easterbunny.wonderple.library.util.NetworkUtil;
import kr.co.easterbunny.wonderple.model.LoadPouchResult;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PouchActivity extends ParentActivity {

    private ActivityPouchBinding binding;

    private HashSet<PouchItemType> pouchItemTypes = new HashSet<>();

    private List<LoadPouchResult.MyItem> myItemList = new ArrayList<>();
    private List<LoadPouchResult.Favorite> favoriteList = new ArrayList<>();

    private String uid, username, profileImage;

    private PouchItemSearchFragment pouchItemSearchFragment;

    public enum PouchItemType {
        MyItem(),
        InterestItem();

        PouchItemType() {}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pouch);
        binding.setPouch(this);

        initViews();
    }

    private void initViews() {
        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        username = intent.getStringExtra("username");
        profileImage = intent.getStringExtra("profileImage");

        binding.tvPouch.setText(getString(R.string.str_pouch, username));

        pouchItemTypes.add(PouchItemType.MyItem);
        pouchItemTypes.add(PouchItemType.InterestItem);

        loadPouch(uid);
    }

    private ArrayList<Fragment> getListFragment(List<LoadPouchResult.MyItem> myItemList, List<LoadPouchResult.Favorite> favoriteList) {
        ArrayList<Fragment> fragments = new ArrayList<>();

        if (pouchItemTypes.contains(PouchItemType.MyItem)) {
            fragments.add((PouchMyItemFragment.newInstance(myItemList, uid, profileImage)));
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText("나의 아이템"));
        }

        if (pouchItemTypes.contains(PouchItemType.InterestItem)) {
            fragments.add(PouchInterestItemFragment.newInstance(favoriteList));
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText("관심 아이템"));
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

    private void loadPouch(String uid) {
        Call<LoadPouchResult> loadPouchResultCall = NetworkUtil.getInstace().loadPouch(uid);
        loadPouchResultCall.enqueue(new Callback<LoadPouchResult>() {
            @Override
            public void onResponse(Call<LoadPouchResult> call, Response<LoadPouchResult> response) {
                LoadPouchResult loadPouchResult = response.body();
                String message = loadPouchResult.getMessage();

                if ("pouch load done".equals(message)) {
                    myItemList = loadPouchResult.getMyItemList();
                    favoriteList = loadPouchResult.getFavoriteList();

                    final ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), getListFragment(myItemList, favoriteList));
                    binding.viewPager.setAdapter(pagerAdapter);
                    binding.viewPager.setOffscreenPageLimit(2);


                    binding.tabLayout.addOnTabSelectedListener(getViewPagerOnTabSelectedListener());
                    binding.viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout));

                    binding.viewPager.setCurrentItem(0);
                }
            }

            @Override
            public void onFailure(Call<LoadPouchResult> call, Throwable t) {

            }
        });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_search_item:
                binding.viewPager.setVisibility(View.GONE);
                pouchItemSearchFragment = PouchItemSearchFragment.newInstance(uid);
                switchContent(pouchItemSearchFragment, R.id.container, true, true);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        binding.viewPager.setVisibility(View.VISIBLE);
        super.onBackPressed();
    }
}
