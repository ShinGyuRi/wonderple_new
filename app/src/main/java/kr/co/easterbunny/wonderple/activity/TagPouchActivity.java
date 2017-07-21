package kr.co.easterbunny.wonderple.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.View;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import kr.co.easterbunny.wonderple.R;
import kr.co.easterbunny.wonderple.adapter.SpinnerAdapter;
import kr.co.easterbunny.wonderple.adapter.ViewPagerAdapter;
import kr.co.easterbunny.wonderple.databinding.ActivityTagPouchBinding;
import kr.co.easterbunny.wonderple.event.ClickBtnSelectComplete;
import kr.co.easterbunny.wonderple.event.ClickEvent;
import kr.co.easterbunny.wonderple.fragment.TagPouchItemDetailFragment;
import kr.co.easterbunny.wonderple.library.ParentActivity;
import kr.co.easterbunny.wonderple.library.WonderpleLib;
import kr.co.easterbunny.wonderple.library.util.JSLog;
import kr.co.easterbunny.wonderple.library.util.NetworkUtil;
import kr.co.easterbunny.wonderple.model.ItemType;
import kr.co.easterbunny.wonderple.model.LoadTagPouchResult;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TagPouchActivity extends ParentActivity {

    private ActivityTagPouchBinding binding;


    private List<LoadTagPouchResult.FacePart> lips = new ArrayList<>();
    private List<LoadTagPouchResult.FacePart> eyes = new ArrayList<>();
    private List<LoadTagPouchResult.FacePart> faces = new ArrayList<>();
    private List<LoadTagPouchResult.FacePart> bases = new ArrayList<>();

    private String uid = WonderpleLib.getInstance().func01_loadUid(this);

    private HashSet<FacePart> facePartHashSet = new HashSet<>();

    private SpinnerAdapter spinnerAdapter;

    public enum FacePart {
        Lip(),
        Eye(),
        Face(),
        Base();

        FacePart()  {}
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tag_pouch);
        binding.setTagPouch(this);

        initViews();
    }

    private void initViews() {
//        binding.btnSelectComplete.setEnabled(false);
        Intent intent = getIntent();
        ItemType itemType = (ItemType) intent.getSerializableExtra("itemType");

        facePartHashSet.add(FacePart.Lip);
        facePartHashSet.add(FacePart.Eye);
        facePartHashSet.add(FacePart.Face);
        facePartHashSet.add(FacePart.Base);

        initSpinner();
        loadTagPouch(uid, itemType);
    }

    private void initSpinner() {
        List<Integer> icon = new ArrayList<>();
        icon.add(R.drawable.wonder_lips);
        icon.add(R.drawable.wonder_eyes);
        icon.add(R.drawable.wonder_eyesbraw);
        icon.add(R.drawable.wonder_cheek);
        icon.add(R.drawable.wonder_contouring);
        icon.add(R.drawable.wonder_skin);

        List<String> text = new ArrayList<>();
        text.add("입술 태그");
        text.add("눈 태그");
        text.add("눈썹 태그");
        text.add("볼 태그");
        text.add("윤곽 태그");
        text.add("피부표현 태그");

        spinnerAdapter = new SpinnerAdapter(this, icon, text);

        binding.spFacePart.setAdapter(spinnerAdapter);

    }

    private ArrayList<Fragment> getListFragment() {
        ArrayList<Fragment> fragments = new ArrayList<>();

        if (facePartHashSet.contains(FacePart.Lip)) {
            fragments.add(TagPouchItemDetailFragment.newInstance(lips));
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText("LIPS"));
        }

        if (facePartHashSet.contains(FacePart.Eye)) {
            fragments.add(TagPouchItemDetailFragment.newInstance(eyes));
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText("EYES"));
        }

        if (facePartHashSet.contains(FacePart.Face)) {
            fragments.add(TagPouchItemDetailFragment.newInstance(faces));
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText("FACE"));
        }

        if (facePartHashSet.contains(FacePart.Base)) {
            fragments.add(TagPouchItemDetailFragment.newInstance(bases));
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText("BASE"));
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

    private void loadTagPouch(String uid, ItemType itemType) {
        Call<LoadTagPouchResult> resultCall = NetworkUtil.getInstace().loadTagPouch(uid);
        resultCall.enqueue(new Callback<LoadTagPouchResult>() {
            @Override
            public void onResponse(Call<LoadTagPouchResult> call, Response<LoadTagPouchResult> response) {
                LoadTagPouchResult result = response.body();
                String message = result.getMessage();

                if ("tag pouch load succeeded".equals(message)) {
                    if (result.getLips() != null) {
                        lips = result.getLips();
                    }
                    if (result.getEyes() != null) {
                        eyes = result.getEyes();
                    }
                    if (result.getFaces() != null) {
                        faces = result.getFaces();
                    }
                    if (result.getBases() != null) {
                        bases = result.getBases();
                    }

                    final ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), getListFragment());
                    binding.viewPager.setAdapter(pagerAdapter);

                    binding.tabLayout.addOnTabSelectedListener(getViewPagerOnTabSelectedListener());
                    binding.viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout));

                    if (itemType == ItemType.Lip) {
                        binding.viewPager.setCurrentItem(0);
                        binding.spFacePart.setSelection(0);
                    } else if (itemType == ItemType.Eye || itemType == ItemType.Eyebrow) {
                        binding.viewPager.setCurrentItem(1);
                        if (itemType == ItemType.Eye)
                            binding.spFacePart.setSelection(1);
                        else if (itemType == ItemType.Eyebrow)
                            binding.spFacePart.setSelection(2);
                    } else if (itemType == ItemType.Cheek || itemType == ItemType.Contour) {
                        binding.viewPager.setCurrentItem(2);
                        if (itemType == ItemType.Cheek)
                            binding.spFacePart.setSelection(3);
                        else if (itemType == ItemType.Contour)
                            binding.spFacePart.setSelection(4);
                    } else if (itemType == ItemType.Skin) {
                        binding.viewPager.setCurrentItem(3);
                        binding.spFacePart.setSelection(5);
                    }
                }
            }

            @Override
            public void onFailure(Call<LoadTagPouchResult> call, Throwable t) {

            }
        });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_select_complete:
                JSLog.D("selected Item Position: "+binding.spFacePart.getSelectedItemPosition(), new Throwable());
                EventBus.getDefault().post(new ClickBtnSelectComplete(true, binding.spFacePart.getSelectedItemPosition()));
                break;
        }
    }
}
