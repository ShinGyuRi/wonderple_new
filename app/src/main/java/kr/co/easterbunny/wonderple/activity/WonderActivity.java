package kr.co.easterbunny.wonderple.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.CompoundButton;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import kr.co.easterbunny.wonderple.R;
import kr.co.easterbunny.wonderple.adapter.FaceItemAdapter;
import kr.co.easterbunny.wonderple.adapter.TagItemAdapter;
import kr.co.easterbunny.wonderple.databinding.ActivityWonderBinding;
import kr.co.easterbunny.wonderple.library.util.Definitions.FACE;
import kr.co.easterbunny.wonderple.library.util.NetworkUtil;
import kr.co.easterbunny.wonderple.model.LoadPostResult;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WonderActivity extends AppCompatActivity {

    private ActivityWonderBinding binding;

    private String imageUrl, iid, uid, auid;
    private List<LoadPostResult.TagItem> tagItems = new ArrayList<>();
    private List<LoadPostResult.TagItem> lipItems = new ArrayList<>();
    private List<LoadPostResult.TagItem> eyeItems = new ArrayList<>();
    private List<LoadPostResult.TagItem> eyebrowItems = new ArrayList<>();
    private List<LoadPostResult.TagItem> cheekItems = new ArrayList<>();
    private List<LoadPostResult.TagItem> contourItems = new ArrayList<>();
    private List<LoadPostResult.TagItem> skinItems = new ArrayList<>();

    private List<String> tags = new ArrayList<>();

    private FaceItemAdapter faceItemAdapter;
    private TagItemAdapter tagItemAdapter;

    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener;
    int tagCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_wonder);
        binding.setWonder(this);

        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        imageUrl = intent.getStringExtra("imageUrl");
        iid = intent.getStringExtra("iid");
        uid = intent.getStringExtra("uid");
        auid = intent.getStringExtra("auid");
        tagItems = (List<LoadPostResult.TagItem>) intent.getSerializableExtra("tagItems");

        binding.btnWonder.setEnabled(false);

        tagItemAdapter = new TagItemAdapter(this);

        Glide.with(this)
                .load(imageUrl)
                .into(binding.imgWonderPost);

        binding.rvLip.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.rvEye.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.rvEyebrow.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.rvCheek.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.rvContour.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.rvSkin.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        for (int i=0; i<tagItems.size(); i++) {
            String category = tagItems.get(i).getCategory();

            if (FACE.LIP.equals(category))  lipItems.add(tagItems.get(i));
            else if (FACE.EYE.equals(category)) eyeItems.add(tagItems.get(i));
            else if (FACE.EYEBORW.equals(category)) eyebrowItems.add(tagItems.get(i));
            else if (FACE.CHEEK.equals(category))   cheekItems.add(tagItems.get(i));
            else if (FACE.CONTOUR.equals(category)) contourItems.add(tagItems.get(i));
            else if (FACE.SKIN.equals(category))    skinItems.add(tagItems.get(i));
        }

        faceItemAdapter = new FaceItemAdapter(this, lipItems);
        binding.rvLip.setAdapter(faceItemAdapter);
        faceItemAdapter = new FaceItemAdapter(this, eyeItems);
        binding.rvEye.setAdapter(faceItemAdapter);
        faceItemAdapter = new FaceItemAdapter(this, eyebrowItems);
        binding.rvEyebrow.setAdapter(faceItemAdapter);
        faceItemAdapter = new FaceItemAdapter(this, cheekItems);
        binding.rvCheek.setAdapter(faceItemAdapter);
        faceItemAdapter = new FaceItemAdapter(this, contourItems);
        binding.rvContour.setAdapter(faceItemAdapter);
        faceItemAdapter = new FaceItemAdapter(this, skinItems);
        binding.rvSkin.setAdapter(faceItemAdapter);

        if (lipItems.size() == 0) { binding.btnLip.setEnabled(false); }
        if (eyeItems.size() == 0) { binding.btnEye.setEnabled(false); }
        if (eyebrowItems.size() == 0) { binding.btnEyebrow.setEnabled(false); }
        if (cheekItems.size() == 0) { binding.btnCheek.setEnabled(false); }
        if (contourItems.size() == 0) { binding.btnContour.setEnabled(false); }
        if (skinItems.size() == 0) { binding.btnSkin.setEnabled(false); }

        onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                clickCheckBox(isChecked);

                if (tagCount > 0) {
                    binding.btnWonder.setEnabled(true);
                } else if (tagCount == 0) {
                    binding.btnWonder.setEnabled(false);
                }
            }
        };

        binding.btnLip.setOnCheckedChangeListener(onCheckedChangeListener);
        binding.btnEye.setOnCheckedChangeListener(onCheckedChangeListener);
        binding.btnEyebrow.setOnCheckedChangeListener(onCheckedChangeListener);
        binding.btnCheek.setOnCheckedChangeListener(onCheckedChangeListener);
        binding.btnContour.setOnCheckedChangeListener(onCheckedChangeListener);
        binding.btnSkin.setOnCheckedChangeListener(onCheckedChangeListener);

        clickBtnAllCheck();
    }

    private void clickBtnAllCheck() {
        binding.btnAllCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (binding.btnLip.isEnabled()) binding.btnLip.setChecked(true);
                    if (binding.btnEye.isEnabled()) binding.btnEye.setChecked(true);
                    if (binding.btnEyebrow.isEnabled()) binding.btnEyebrow.setChecked(true);
                    if (binding.btnCheek.isEnabled()) binding.btnCheek.setChecked(true);
                    if (binding.btnContour.isEnabled()) binding.btnContour.setChecked(true);
                    if (binding.btnSkin.isEnabled()) binding.btnSkin.setChecked(true);
                } else {
                    binding.btnLip.setChecked(false);
                    binding.btnEye.setChecked(false);
                    binding.btnEyebrow.setChecked(false);
                    binding.btnCheek.setChecked(false);
                    binding.btnContour.setChecked(false);
                    binding.btnSkin.setChecked(false);
                }
            }
        });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                onBackPressed();
                break;

            case R.id.btn_wonder:
                applyWonder();
                break;
        }
    }

    private void applyWonder() {
        if (binding.btnLip.isChecked()) tags.add(FACE.LIP);
        if (binding.btnEye.isChecked()) tags.add(FACE.EYE);
        if (binding.btnEyebrow.isChecked()) tags.add(FACE.EYEBORW);
        if (binding.btnCheek.isChecked()) tags.add(FACE.CHEEK);
        if (binding.btnContour.isChecked()) tags.add(FACE.CONTOUR);
        if (binding.btnSkin.isChecked()) tags.add(FACE.SKIN);

        Call<JsonObject> jsonObjectCall = NetworkUtil.getInstace().applyWonder(iid, uid, auid, String.valueOf(tagCount), tags);
        jsonObjectCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject jsonObject = response.body();
                String message = jsonObject.get("message").toString().replace("\"", "");

                if ("wonder applied".equals(message)) {
                    WonderActivity.this.finish();
                    tagItemAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    private void clickCheckBox(boolean isChecked) {
        if (isChecked) {
            if (tagCount < 6) {
                tagCount++;
            }
        } else {
            if (tagCount > 0) {
                tagCount--;
            }
        }
    }
}
