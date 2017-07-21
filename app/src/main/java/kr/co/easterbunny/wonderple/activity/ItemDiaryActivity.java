package kr.co.easterbunny.wonderple.activity;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import jp.wasabeef.glide.transformations.ColorFilterTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import kr.co.easterbunny.wonderple.R;
import kr.co.easterbunny.wonderple.adapter.ItemTalkAdapter;
import kr.co.easterbunny.wonderple.adapter.RecommendPostAdapter;
import kr.co.easterbunny.wonderple.databinding.ActivityItemDiaryBinding;
import kr.co.easterbunny.wonderple.fragment.TalkDetailFragment;
import kr.co.easterbunny.wonderple.library.ParentActivity;
import kr.co.easterbunny.wonderple.library.util.NetworkUtil;
import kr.co.easterbunny.wonderple.model.CustomBitmapPool;
import kr.co.easterbunny.wonderple.model.LoadDiaryResult;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemDiaryActivity extends ParentActivity {

    private ActivityItemDiaryBinding binding;

    private LoadDiaryResult.ItemDetail itemDetail;
    private List<LoadDiaryResult.Post> posts = new ArrayList<>();
    private List<LoadDiaryResult.Talk> talks = new ArrayList<>();

    private String uid, cid, profileImage;

    private TalkDetailFragment talkDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_item_diary);
        binding.setItemDiary(this);

        Intent intent = getIntent();

        uid = intent.getStringExtra("uid");
        cid = intent.getStringExtra("cid");
        profileImage = intent.getStringExtra("profileImage");

        loadDiary(uid, cid);
    }

    private void loadDiary(String uid, String cid) {
        Call<LoadDiaryResult> loadDiaryResultCall = NetworkUtil.getInstace().loadDiary(uid, cid);
        loadDiaryResultCall.enqueue(new Callback<LoadDiaryResult>() {
            @Override
            public void onResponse(Call<LoadDiaryResult> call, Response<LoadDiaryResult> response) {
                LoadDiaryResult loadDiaryResult = response.body();
                String message = loadDiaryResult.getMessage();

                if ("diary load done".equals(message)) {
                    itemDetail = loadDiaryResult.getItemDetail();
                    posts = loadDiaryResult.getPosts();
                    talks = loadDiaryResult.getTalks();

                    initViews(itemDetail);
                    setRecommendPost(posts);
                    setItemTalkPreview(talks);
                }
            }

            @Override
            public void onFailure(Call<LoadDiaryResult> call, Throwable t) {

            }
        });
    }

    int randomAndroidColor;

    private void initViews(LoadDiaryResult.ItemDetail itemDetail) {
        binding.tvItemName.setSelected(true);

        int r = Integer.parseInt(itemDetail.getR());
        int g = Integer.parseInt(itemDetail.getG());
        int b = Integer.parseInt(itemDetail.getB());

        Glide.with(this)
                .load(itemDetail.getItemImage())
                .into(binding.imgItem);

        Glide.with(this)
                .load(R.drawable.posting_tagbox)
                .bitmapTransform(new ColorFilterTransformation(new CustomBitmapPool(), Color.rgb(r, g, b)), new CropCircleTransformation(new CustomBitmapPool()))
                .into(binding.imgItemColor);

        binding.tvBrandName.setText("[" + itemDetail.getBrandName() + "]");
        binding.tvItemName.setText(itemDetail.getItemName());
        binding.tvColorName.setText(itemDetail.getColorName());
        binding.ratingBar.setRating(Integer.parseInt(itemDetail.getRate()));

        int[] androidColors = getResources().getIntArray(R.array.item_talk_color);
        randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];
        binding.viewItemTalkPreviewCover.setBackgroundColor(randomAndroidColor);

    }

    private void setRecommendPost(List<LoadDiaryResult.Post> posts) {
        int postCount = 0;
        int wonderSum = 0;

        if (posts != null) {
            postCount = posts.size();
            for (int i=0; i<posts.size(); i++) {
                wonderSum = wonderSum + Integer.parseInt(posts.get(i).getWonderCount());
            }

            binding.rvRecommendPost.setHasFixedSize(true);
            binding.rvRecommendPost.setAdapter(new RecommendPostAdapter(this, posts, uid));
            binding.rvRecommendPost.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        }

        binding.tvCountPostWonder.setText(getString(R.string.str_count_post_wonder, postCount, wonderSum));
    }

    private void setItemTalkPreview(List<LoadDiaryResult.Talk> talkPreview) {
        binding.rvItemTalkPreview.setAdapter(new ItemTalkAdapter(this, talkPreview));
        binding.rvItemTalkPreview.setLayoutManager(new LinearLayoutManager(this));

        Glide.with(this)
                .load(profileImage)
                .bitmapTransform(new CropCircleTransformation(new CustomBitmapPool()))
                .into(binding.imgProfile);

        int countTalk = 0;

        if (talks != null)
            countTalk = talks.size();

        binding.tvCountTalk.setText(getString(R.string.str_count_talk, countTalk));
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.view_item_talk_preview_cover:
                onClickTalkPreview();
                break;
        }
    }

    private void onClickTalkPreview() {
        int colorFrom = ContextCompat.getColor(this, R.color.white);
        int colorTo = randomAndroidColor;
        setToolbarColor(colorFrom, colorTo);
        binding.tvTitle.setTextColor(ContextCompat.getColor(this, R.color.white));

        talkDetailFragment = TalkDetailFragment.newInstance(uid, cid);
        switchContent(talkDetailFragment, R.id.container, true, true);
    }

    private void setToolbarColor(int colorFrom, int colorTo) {
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(250);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                binding.viewToolbar.setBackgroundColor((int) animation.getAnimatedValue());
            }
        });
        colorAnimation.start();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            binding.tvTitle.setTextColor(ContextCompat.getColor(this, R.color.black));

            int colorTo = ContextCompat.getColor(this, R.color.white);
            int colorFrom = randomAndroidColor;
            setToolbarColor(colorFrom, colorTo);
        }
        super.onBackPressed();
    }
}
