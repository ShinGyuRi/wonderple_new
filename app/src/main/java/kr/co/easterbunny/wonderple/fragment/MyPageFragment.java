package kr.co.easterbunny.wonderple.fragment;


import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.bumptech.glide.Glide;
import com.facebook.share.model.AppInviteContent;
import com.facebook.share.widget.AppInviteDialog;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import kr.co.easterbunny.wonderple.R;
import kr.co.easterbunny.wonderple.databinding.FragmentMyPageBinding;
import kr.co.easterbunny.wonderple.library.ParentActivity;
import kr.co.easterbunny.wonderple.library.ParentFragment;
import kr.co.easterbunny.wonderple.library.WonderpleLib;
import kr.co.easterbunny.wonderple.library.listener.TakePictureListener;
import kr.co.easterbunny.wonderple.library.util.FileUtil;
import kr.co.easterbunny.wonderple.library.util.NetworkUtil;
import kr.co.easterbunny.wonderple.model.CustomBitmapPool;
import kr.co.easterbunny.wonderple.model.LoadProfileResult;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyPageFragment extends ParentFragment {

    private FragmentMyPageBinding binding;

    private Fragment myPostFragment, myWonderFragment;

    private String uid, username, profileImg;

    private List<LoadProfileResult.PostImages> postImages = new ArrayList<>();
    private List<LoadProfileResult.WonderImages> wonderImages = new ArrayList<>();

    public MyPageFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public MyPageFragment(String uid) {
        this.uid = uid;
    }

    public static MyPageFragment newInstance(String uid) {
        MyPageFragment frag = new MyPageFragment(uid);
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_page, container, false);
        View view = binding.getRoot();
        binding.setMyPage(this);
        binding.layoutUserProfile.setUserProfile(this);

        initViews();

        return view;
    }

    private void initViews() {
        ((ParentActivity) getActivity()).setTakePictureListener(takePictureListener);

        binding.layoutUserProfile.btnFollow.setVisibility(View.GONE);
        binding.layoutUserProfile.btnBack.setVisibility(View.GONE);
        loadProfileData();

        binding.layoutUserProfile.btnPosting.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    myPostFragment = MyPostFragment.newInstance(uid, uid, postImages);
                    switchContent(myPostFragment, R.id.container);
                }
            }
        });

        binding.layoutUserProfile.btnWonder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    myWonderFragment = MyWonderFragment.newInstance(uid, uid, wonderImages);
                    switchContent(myWonderFragment, R.id.container);
                }
            }
        });

        binding.layoutUserProfile.btnPouch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ((ParentActivity) getActivity()).movePouchActivity(getContext(), uid, username, profileImg);
                }
            }
        });
    }

    private TakePictureListener takePictureListener = new TakePictureListener() {
        @Override
        public void takePicture(Bitmap bm) {
            Glide.with(getContext())
                    .load(FileUtil.getTempImageFile(getContext()))
                    .skipMemoryCache(true)
                    .bitmapTransform(new CropCircleTransformation(new CustomBitmapPool()))
                    .into(binding.layoutUserProfile.imgProfile);
        }
    };

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_edit_profile:
                break;

            case R.id.img_profile:
                ((ParentActivity) getActivity()).showImageAlert();
                break;

            case R.id.btn_add_friend:
                addFriend();
                break;
        }
    }

    private void loadProfileData() {
        Call<LoadProfileResult> loadProfileResultCall = NetworkUtil.getInstace().loadProfile(uid);
        loadProfileResultCall.enqueue(new Callback<LoadProfileResult>() {
            @Override
            public void onResponse(Call<LoadProfileResult> call, Response<LoadProfileResult> response) {
                LoadProfileResult loadProfileResult = response.body();
                String message = loadProfileResult.getMessage();

                if ("profile loaded".equals(message)) {
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
                        Glide.with(getContext())
                                .load(profileImg)
                                .bitmapTransform(new CropCircleTransformation(new CustomBitmapPool()))
                                .into(binding.layoutUserProfile.imgProfile);
                    }

                    if ("".equals(realname))    binding.layoutUserProfile.tvRealname.setVisibility(View.GONE);
                    else binding.layoutUserProfile.tvRealname.setText(realname);

                    if ("".equals(skintone))    binding.layoutUserProfile.tvSkinTone.setVisibility(View.GONE);
                    else binding.layoutUserProfile.tvSkinTone.setText(skintone);

                    if ("".equals(descText))    binding.layoutUserProfile.tvDesc.setVisibility(View.GONE);
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

                    binding.layoutUserProfile.btnPosting.setChecked(true);
                    myPostFragment = MyPostFragment.newInstance(uid, uid, postImages);
                    getChildFragmentManager().beginTransaction().replace(R.id.container, myPostFragment).commit();
                }
            }

            @Override
            public void onFailure(Call<LoadProfileResult> call, Throwable t) {

            }
        });
    }

    private void addFriend() {
        String appLinkUrl, previewImageUrl;

        appLinkUrl = "https://fb.me/1854952048076626";
        previewImageUrl = "http://2.bp.blogspot.com/-99shOruuadw/VQsG2T233sI/AAAAAAAAEi0/noFTxUBh_rg/s1600/appscripts.png";

        if (AppInviteDialog.canShow()) {
            AppInviteContent content = new AppInviteContent.Builder()
                    .setApplinkUrl(appLinkUrl)
                    .setPreviewImageUrl(previewImageUrl)
                    .build();
            AppInviteDialog.show(this, content);
        }
    }

    @Override
    public void onPause() {
        binding.layoutUserProfile.radioGroup.clearCheck();
        binding.layoutUserProfile.btnPosting.setChecked(true);
        super.onPause();
    }
}
