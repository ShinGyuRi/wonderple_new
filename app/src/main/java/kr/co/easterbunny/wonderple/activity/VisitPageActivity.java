package kr.co.easterbunny.wonderple.activity;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.CompoundButton;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import kr.co.easterbunny.wonderple.R;
import kr.co.easterbunny.wonderple.databinding.ActivityVisitPageBinding;
import kr.co.easterbunny.wonderple.fragment.MyPostFragment;
import kr.co.easterbunny.wonderple.fragment.MyWonderFragment;
import kr.co.easterbunny.wonderple.library.ParentActivity;
import kr.co.easterbunny.wonderple.library.WonderpleLib;
import kr.co.easterbunny.wonderple.library.util.NetworkUtil;
import kr.co.easterbunny.wonderple.model.CustomBitmapPool;
import kr.co.easterbunny.wonderple.model.LoadProfileResult;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by scona on 2017-04-17.
 */

public class VisitPageActivity extends ParentActivity {

    private ActivityVisitPageBinding binding;

    private Fragment myPostFragment, myWonderFragment;

    private String uid, auid, username, profileImg;

    private List<LoadProfileResult.PostImages> postImages = new ArrayList<>();
    private List<LoadProfileResult.WonderImages> wonderImages = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_visit_page);
        binding.layoutUserProfile.setVisitProfile(this);

        initView();
    }

    private void initView() {
        Intent intent = getIntent();

        uid = intent.getStringExtra("uid");
        auid = intent.getStringExtra("auid");

        binding.layoutUserProfile.btnEditProfile.setVisibility(View.GONE);
        binding.layoutUserProfile.btnAddFriend.setVisibility(View.GONE);
        binding.layoutUserProfile.rvBadge.setVisibility(View.GONE);

        loadVisitProfile();

        binding.layoutUserProfile.btnPosting.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    myPostFragment = MyPostFragment.newInstance(uid, auid, postImages);
                    switchContent(myPostFragment, R.id.container, false, false);
                }
            }
        });

        binding.layoutUserProfile.btnWonder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    myWonderFragment = MyWonderFragment.newInstance(uid, auid, wonderImages);
                    switchContent(myWonderFragment, R.id.container, false, false);
                }
            }
        });

        binding.layoutUserProfile.btnPouch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    movePouchActivity(VisitPageActivity.this, auid, username, profileImg);
                }
            }
        });
    }


    private void loadVisitProfile() {
        Call<LoadProfileResult> loadProfileResultCall = NetworkUtil.getInstace().loadVisitProfile(uid, auid);
        loadProfileResultCall.enqueue(new Callback<LoadProfileResult>() {
            @Override
            public void onResponse(Call<LoadProfileResult> call, Response<LoadProfileResult> response) {
                LoadProfileResult loadProfileResult = response.body();
                String message = loadProfileResult.getMessage();

                if ("visit profile loaded".equals(message)) {
                    postImages = loadProfileResult.getPostImages();
                    wonderImages = loadProfileResult.getWonderImages();
                    String postCount = loadProfileResult.getPostCount();
                    String wonderPostCount = loadProfileResult.getWonderPostCount();
                    String followerCount = loadProfileResult.getFollowerCount();
                    String followingCount = loadProfileResult.getFollowingCount();
                    String descText = loadProfileResult.getDescText();
                    username = loadProfileResult.getUserName();
                    profileImg = loadProfileResult.getProfileImage();
                    String realname = loadProfileResult.getRealName();
                    String skintone = loadProfileResult.getSkinTone();

                    binding.layoutUserProfile.tvUsername.setText(username);

                    if (!"".equals(profileImg)) {
                        Glide.with(VisitPageActivity.this)
                                .load(profileImg)
                                .bitmapTransform(new CropCircleTransformation(new CustomBitmapPool()))
                                .into(binding.layoutUserProfile.imgProfile);
                    }

                    setFollowStatus(loadProfileResult);

                    if ("".equals(realname))
                        binding.layoutUserProfile.tvRealname.setVisibility(View.GONE);
                    else binding.layoutUserProfile.tvRealname.setText(realname);

                    if ("".equals(skintone))
                        binding.layoutUserProfile.tvSkinTone.setVisibility(View.GONE);
                    else binding.layoutUserProfile.tvSkinTone.setText(skintone);

                    if ("".equals(descText))
                        binding.layoutUserProfile.tvDesc.setVisibility(View.GONE);
                    else binding.layoutUserProfile.tvDesc.setText(descText);

                    binding.layoutUserProfile.numPosting.setText(postCount);
                    binding.layoutUserProfile.numWonders.setText(wonderPostCount);
                    binding.layoutUserProfile.numFollower.setText(followerCount);
                    binding.layoutUserProfile.numFollowing.setText(followingCount);

//                    Fragment fragment = new MyPostFragment(uid, postImages);
//                    FragmentManager fragmentManager = getChildFragmentManager();
//                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                    fragmentTransaction.add(R.id.container, fragment);
//                    fragmentTransaction.commit();

                    myPostFragment = MyPostFragment.newInstance(uid, auid, postImages);
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, myPostFragment).commit();
                }
            }

            @Override
            public void onFailure(Call<LoadProfileResult> call, Throwable t) {

            }
        });
    }

    private void setFollowStatus(LoadProfileResult loadProfileResult) {
        String followCheck = loadProfileResult.getFollowCheck();
        String shareStatus = loadProfileResult.getShareStauts();

        if ("0".equals(followCheck)) {
            binding.layoutUserProfile.btnPosting.setEnabled(true);
            binding.layoutUserProfile.btnWonder.setEnabled(true);
            binding.layoutUserProfile.btnPouch.setEnabled(true);

            binding.layoutUserProfile.btnFollow.setBackgroundResource(R.drawable.wonderplace_following);
            binding.layoutUserProfile.btnFollow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    binding.layoutUserProfile.btnFollow.setBackgroundResource(R.drawable.wonderplace_follower);
                    WonderpleLib.getInstance().follow(uid, auid);
                    loadProfileResult.setShareStauts("X");
                }
            });
        } else if ("1".equals(followCheck)) {
            binding.layoutUserProfile.btnPosting.setEnabled(false);
            binding.layoutUserProfile.btnWonder.setEnabled(false);
            binding.layoutUserProfile.btnPouch.setEnabled(false);

            binding.imgPrivate.setVisibility(View.VISIBLE);
            binding.layoutUserProfile.btnFollow.setBackgroundResource(R.drawable.wonderplace_request);
            binding.layoutUserProfile.btnFollow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    binding.layoutUserProfile.btnFollow.setBackgroundResource(R.drawable.wonderplace_follower);
                    WonderpleLib.getInstance().follow(uid, auid);
                    loadProfileResult.setShareStauts("X");
                }
            });
        } else {
            binding.layoutUserProfile.btnPosting.setEnabled(false);
            binding.layoutUserProfile.btnWonder.setEnabled(false);
            binding.layoutUserProfile.btnPouch.setEnabled(false);
            if ("0".equals(shareStatus)) {
                binding.layoutUserProfile.btnFollow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        binding.layoutUserProfile.btnFollow.setBackgroundResource(R.drawable.wonderplace_following);
                        WonderpleLib.getInstance().follow(uid, auid);
                        loadProfileResult.setShareStauts("0");
                    }
                });
            } else if ("1".equals(shareStatus) || "2".equals(shareStatus) || "3".equals(shareStatus)) {
                binding.imgPrivate.setVisibility(View.VISIBLE);
                binding.layoutUserProfile.btnFollow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        binding.layoutUserProfile.btnFollow.setBackgroundResource(R.drawable.wonderplace_request);
                        WonderpleLib.getInstance().follow(uid, auid);
                        loadProfileResult.setShareStauts("1");
                    }
                });
            }
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                onBackPressed();
                break;
        }
    }

    @Override
    protected void onPause() {
        binding.layoutUserProfile.radioGroup.clearCheck();
        binding.layoutUserProfile.btnPosting.setChecked(true);
        super.onPause();
    }
}
