package kr.co.easterbunny.wonderple.activity;

import android.animation.ObjectAnimator;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.apradanas.simplelinkabletext.Link;
import com.github.aakira.expandablelayout.ExpandableLayoutListenerAdapter;
import com.github.aakira.expandablelayout.Utils;
import com.google.gson.JsonObject;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import kr.co.easterbunny.wonderple.R;
import kr.co.easterbunny.wonderple.adapter.FragmentPagerAdapter;
import kr.co.easterbunny.wonderple.databinding.ActivityUploadBinding;
import kr.co.easterbunny.wonderple.event.ClickTagItem;
import kr.co.easterbunny.wonderple.fragment.TagPouchItemFragment;
import kr.co.easterbunny.wonderple.library.ParentActivity;
import kr.co.easterbunny.wonderple.library.WonderpleLib;
import kr.co.easterbunny.wonderple.library.util.FileUtil;
import kr.co.easterbunny.wonderple.library.util.JSLog;
import kr.co.easterbunny.wonderple.library.util.NetworkUtil;
import kr.co.easterbunny.wonderple.model.Hashtag;
import kr.co.easterbunny.wonderple.model.ItemType;
import kr.co.easterbunny.wonderple.model.LoadEventDataResult;
import kr.co.easterbunny.wonderple.model.LoadTagPouchResult;
import kr.co.easterbunny.wonderple.model.SearchTagResult;
import kr.co.easterbunny.wonderple.model.Session;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static kr.co.easterbunny.wonderple.library.util.Definitions.FACE.CHEEK;
import static kr.co.easterbunny.wonderple.library.util.Definitions.FACE.CONTOUR;
import static kr.co.easterbunny.wonderple.library.util.Definitions.FACE.EYE;
import static kr.co.easterbunny.wonderple.library.util.Definitions.FACE.EYEBORW;
import static kr.co.easterbunny.wonderple.library.util.Definitions.FACE.LIP;
import static kr.co.easterbunny.wonderple.library.util.Definitions.FACE.SKIN;
import static kr.co.easterbunny.wonderple.library.util.FileUtil.getBitmapFromFile;
import static kr.co.easterbunny.wonderple.library.util.FileUtil.loadImageWithSampleSize;
import static kr.co.easterbunny.wonderple.library.util.ImageUtil.bitmapToByteArray;

public class UploadActivity extends ParentActivity {

    private ActivityUploadBinding binding;

    private Session mSession = Session.getInstance();

    private HashSet<ItemType> itemTypeHashSet = new HashSet<>();

    private String uid = WonderpleLib.getInstance().func01_loadUid(this);
    private String iid;
    private List<String> cid = new ArrayList<>();
    private List<String> coid = new ArrayList<>();
    private List<String> color = new ArrayList<>();
    private int colorCount = 0;
    private String theme;
    private List<String> hashtag = new ArrayList<>();

    private FragmentPagerAdapter pagerAdapter;

    private List<LoadTagPouchResult.FacePart> lips = new ArrayList<>();
    private List<LoadTagPouchResult.FacePart> eyes = new ArrayList<>();
    private List<LoadTagPouchResult.FacePart> eyebrows = new ArrayList<>();
    private List<LoadTagPouchResult.FacePart> cheeks = new ArrayList<>();
    private List<LoadTagPouchResult.FacePart> contours = new ArrayList<>();
    private List<LoadTagPouchResult.FacePart> skins = new ArrayList<>();

    TagPouchItemFragment lipFragment;
    TagPouchItemFragment eyeFragment;
    TagPouchItemFragment eyebrowFragment;
    TagPouchItemFragment cheekFragment;
    TagPouchItemFragment contourFragment;
    TagPouchItemFragment skinFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_upload);
        binding.setUpload(this);

        itemTypeHashSet.add(ItemType.Lip);
        itemTypeHashSet.add(ItemType.Eye);
        itemTypeHashSet.add(ItemType.Eyebrow);
        itemTypeHashSet.add(ItemType.Cheek);
        itemTypeHashSet.add(ItemType.Contour);
        itemTypeHashSet.add(ItemType.Skin);


        initViews();
    }

    private void initViews() {
        lipFragment = TagPouchItemFragment.newInstance(ItemType.Lip, lips);
        eyeFragment = TagPouchItemFragment.newInstance(ItemType.Eye, eyes);
        eyebrowFragment = TagPouchItemFragment.newInstance(ItemType.Eyebrow, eyebrows);
        cheekFragment = TagPouchItemFragment.newInstance(ItemType.Cheek, cheeks);
        contourFragment = TagPouchItemFragment.newInstance(ItemType.Contour, contours);
        skinFragment = TagPouchItemFragment.newInstance(ItemType.Skin, skins);

        binding.imgPost.setImageBitmap(FileUtil.getBitmapFromFile(this, mSession.getFileToUpload()));

        pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager(), getListFragment());
        binding.viewPager.setAdapter(pagerAdapter);

        binding.tabLayout.addOnTabSelectedListener(getViewPagerOnTabSeletedListener());
        binding.viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout));

        binding.viewPager.setCurrentItem(0);

        setLinkableEdittext();
        initTagColorLayout();
        initTagThemeLayout();
        initBtnColor();
        initRadioButtonTheme();
        loadEvent(uid);
    }

    private void setLinkableEdittext() {
        Link linkHashtag = new Link(Pattern.compile("(#\\w+)"))
                .setUnderlined(false)
                .setTextColor(ContextCompat.getColor(this, R.color.link));

        Link linkUsername = new Link(Pattern.compile("(@\\w+)"))
                .setUnderlined(false)
                .setTextColor(ContextCompat.getColor(this, R.color.link));

        List<Link> links = new ArrayList<>();
        links.add(linkHashtag);
        links.add(linkUsername);

        binding.etDesc.addLinks(links);

        setDescTextWatcher();
    }

    private void setDescTextWatcher() {
        binding.etDesc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String desc = s.toString();
                ArrayList<int[]> hashtagSpans = getSpans(desc, '#');

                SpannableString descContent = new SpannableString(desc);

                if (hashtagSpans != null) {
                    for (int i=0; i<hashtagSpans.size(); i++) {
                        int[] span = hashtagSpans.get(i);
                        int hashTagStart = span[0];
                        int hashTagEnd = span[1];

                        searchHashtag(descContent.subSequence(hashTagStart + 1, hashTagEnd).toString());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private ArrayList<Fragment> getListFragment() {
        ArrayList<Fragment> fragments = new ArrayList<>();

        if (itemTypeHashSet.contains(ItemType.Lip)) {
            fragments.add(lipFragment);
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText("입술"));
        }

        if (itemTypeHashSet.contains(ItemType.Eye)) {
            fragments.add(eyeFragment);
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText("눈"));
        }

        if (itemTypeHashSet.contains(ItemType.Eyebrow)) {
            fragments.add(eyebrowFragment);
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText("눈썹"));
        }

        if (itemTypeHashSet.contains(ItemType.Cheek)) {
            fragments.add(cheekFragment);
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText("볼"));
        }

        if (itemTypeHashSet.contains(ItemType.Contour)) {
            fragments.add(contourFragment);
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText("윤곽"));
        }

        if (itemTypeHashSet.contains(ItemType.Skin)) {
            fragments.add(skinFragment);
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText("피부표현"));
        }

        return fragments;
    }

    private TabLayout.ViewPagerOnTabSelectedListener getViewPagerOnTabSeletedListener() {
        return new TabLayout.ViewPagerOnTabSelectedListener(binding.viewPager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
            }
        };
    }

    private void initTagColorLayout() {
        binding.elTagColor.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        binding.elTagColor.setInterpolator(Utils.createInterpolator(Utils.LINEAR_OUT_SLOW_IN_INTERPOLATOR));
        binding.elTagColor.setListener(new ExpandableLayoutListenerAdapter() {
            @Override
            public void onPreOpen() {
                createRotateAnimator(binding.btnTagColorMenu, 0f, 180f).start();
            }

            @Override
            public void onPreClose() {
                super.onPreClose();
                createRotateAnimator(binding.btnTagColorMenu, 180f, 0f).start();
            }
        });

        binding.btnTagColorMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.elTagColor.toggle();
            }
        });
    }

    private void initTagThemeLayout() {
        binding.elTagTheme.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        binding.elTagTheme.setInterpolator(Utils.createInterpolator(Utils.LINEAR_OUT_SLOW_IN_INTERPOLATOR));
        binding.elTagTheme.setListener(new ExpandableLayoutListenerAdapter() {
            @Override
            public void onPreOpen() {
                createRotateAnimator(binding.btnTagThemeMenu, 0f, 180f).start();
            }

            @Override
            public void onPreClose() {
                super.onPreClose();
                createRotateAnimator(binding.btnTagThemeMenu, 180f, 0f).start();
            }
        });

        binding.btnTagThemeMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.elTagTheme.toggle();
            }
        });
    }

    public ObjectAnimator createRotateAnimator(final View target, final float from, final float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(target, "rotation", from, to);
        animator.setDuration(300);
        animator.setInterpolator(Utils.createInterpolator(Utils.LINEAR_INTERPOLATOR));
        return animator;
    }

    private void loadEvent(String uid) {

        Call<LoadEventDataResult> resultCall = NetworkUtil.getInstace().loadEvent(uid);
        resultCall.enqueue(new Callback<LoadEventDataResult>() {
            @Override
            public void onResponse(Call<LoadEventDataResult> call, Response<LoadEventDataResult> response) {
                LoadEventDataResult result = response.body();
                String message = result.getMessage();

                if ("current event loaded".equals(message)) {
                    List<LoadEventDataResult.Event> eventDataResults;
                    eventDataResults = result.getEvents();

                    if (eventDataResults == null) {
                        binding.layoutContest.setVisibility(View.GONE);
                    } else if (eventDataResults.size() == 1) {
                        binding.tvTagTheme1.setText(eventDataResults.get(0).getEventTitle());
                        binding.tvTagTheme2.setVisibility(View.GONE);
                        binding.radioButton2.setVisibility(View.GONE);
                        binding.tvTagTheme3.setVisibility(View.GONE);
                        binding.radioButton3.setVisibility(View.GONE);
                    } else if (eventDataResults.size() == 2) {
                        binding.tvTagTheme1.setText(eventDataResults.get(0).getEventTitle());
                        binding.tvTagTheme2.setText(eventDataResults.get(1).getEventTitle());
                        binding.tvTagTheme3.setVisibility(View.GONE);
                        binding.radioButton3.setVisibility(View.GONE);
                    } else if (eventDataResults.size() == 3) {
                        binding.tvTagTheme1.setText(eventDataResults.get(0).getEventTitle());
                        binding.tvTagTheme2.setText(eventDataResults.get(1).getEventTitle());
                        binding.tvTagTheme3.setText(eventDataResults.get(2).getEventTitle());
                    }
                }
            }

            @Override
            public void onFailure(Call<LoadEventDataResult> call, Throwable t) {

            }
        });
    }

    private void initBtnColor() {
        binding.btnColor1.setOnCheckedChangeListener(onCheckedChangeListener(binding.btnColor1));
        binding.btnColor2.setOnCheckedChangeListener(onCheckedChangeListener(binding.btnColor2));
        binding.btnColor3.setOnCheckedChangeListener(onCheckedChangeListener(binding.btnColor3));
        binding.btnColor4.setOnCheckedChangeListener(onCheckedChangeListener(binding.btnColor4));
        binding.btnColor5.setOnCheckedChangeListener(onCheckedChangeListener(binding.btnColor5));
        binding.btnColor6.setOnCheckedChangeListener(onCheckedChangeListener(binding.btnColor6));
        binding.btnColor7.setOnCheckedChangeListener(onCheckedChangeListener(binding.btnColor7));
        binding.btnColor8.setOnCheckedChangeListener(onCheckedChangeListener(binding.btnColor8));
        binding.btnColor9.setOnCheckedChangeListener(onCheckedChangeListener(binding.btnColor9));
        binding.btnColor10.setOnCheckedChangeListener(onCheckedChangeListener(binding.btnColor10));
        binding.btnColor11.setOnCheckedChangeListener(onCheckedChangeListener(binding.btnColor11));
        binding.btnColor12.setOnCheckedChangeListener(onCheckedChangeListener(binding.btnColor12));
        binding.btnColor13.setOnCheckedChangeListener(onCheckedChangeListener(binding.btnColor13));
        binding.btnColor14.setOnCheckedChangeListener(onCheckedChangeListener(binding.btnColor14));
        binding.btnColor15.setOnCheckedChangeListener(onCheckedChangeListener(binding.btnColor15));
        binding.btnColor16.setOnCheckedChangeListener(onCheckedChangeListener(binding.btnColor16));
        binding.btnColor17.setOnCheckedChangeListener(onCheckedChangeListener(binding.btnColor17));
        binding.btnColor18.setOnCheckedChangeListener(onCheckedChangeListener(binding.btnColor18));
        binding.btnColor19.setOnCheckedChangeListener(onCheckedChangeListener(binding.btnColor19));
        binding.btnColor20.setOnCheckedChangeListener(onCheckedChangeListener(binding.btnColor20));
        binding.btnColor21.setOnCheckedChangeListener(onCheckedChangeListener(binding.btnColor21));
        binding.btnColor22.setOnCheckedChangeListener(onCheckedChangeListener(binding.btnColor22));
        binding.btnColor23.setOnCheckedChangeListener(onCheckedChangeListener(binding.btnColor23));
        binding.btnColor24.setOnCheckedChangeListener(onCheckedChangeListener(binding.btnColor24));
        binding.btnColor25.setOnCheckedChangeListener(onCheckedChangeListener(binding.btnColor25));
        binding.btnColor26.setOnCheckedChangeListener(onCheckedChangeListener(binding.btnColor26));
        binding.btnColor27.setOnCheckedChangeListener(onCheckedChangeListener(binding.btnColor27));
        binding.btnColor28.setOnCheckedChangeListener(onCheckedChangeListener(binding.btnColor28));
        binding.btnColor29.setOnCheckedChangeListener(onCheckedChangeListener(binding.btnColor29));
        binding.btnColor30.setOnCheckedChangeListener(onCheckedChangeListener(binding.btnColor30));
        binding.btnColor31.setOnCheckedChangeListener(onCheckedChangeListener(binding.btnColor31));
        binding.btnColor32.setOnCheckedChangeListener(onCheckedChangeListener(binding.btnColor32));
        binding.btnColor33.setOnCheckedChangeListener(onCheckedChangeListener(binding.btnColor33));

        binding.btnColor1.setTag("0");
        binding.btnColor2.setTag("1");
        binding.btnColor3.setTag("2");
        binding.btnColor4.setTag("3");
        binding.btnColor5.setTag("4");
        binding.btnColor6.setTag("5");
        binding.btnColor7.setTag("6");
        binding.btnColor8.setTag("7");
        binding.btnColor9.setTag("8");
        binding.btnColor10.setTag("9");
        binding.btnColor11.setTag("10");
        binding.btnColor12.setTag("11");
        binding.btnColor13.setTag("12");
        binding.btnColor14.setTag("13");
        binding.btnColor15.setTag("14");
        binding.btnColor16.setTag("15");
        binding.btnColor17.setTag("16");
        binding.btnColor18.setTag("17");
        binding.btnColor19.setTag("18");
        binding.btnColor20.setTag("19");
        binding.btnColor21.setTag("20");
        binding.btnColor22.setTag("21");
        binding.btnColor23.setTag("22");
        binding.btnColor24.setTag("23");
        binding.btnColor25.setTag("24");
        binding.btnColor26.setTag("25");
        binding.btnColor27.setTag("26");
        binding.btnColor28.setTag("27");
        binding.btnColor29.setTag("28");
        binding.btnColor30.setTag("29");
        binding.btnColor31.setTag("30");
        binding.btnColor32.setTag("31");
        binding.btnColor33.setTag("32");
    }

    private CheckBox.OnCheckedChangeListener onCheckedChangeListener(CheckBox checkBox) {
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkBox.setBackground(checkBox.getButtonDrawable());
                    checkBox.setButtonDrawable(R.drawable.color_select);
                    colorCount++;
                    color.add(checkBox.getTag().toString());
                } else if (!isChecked) {
                    checkBox.setButtonDrawable(checkBox.getBackground());
                    checkBox.setBackground(null);
                    colorCount--;
                    color.remove(checkBox.getTag().toString());
                }
            }
        };
    }

    private void initRadioButtonTheme() {
        binding.radioButton1.setOnCheckedChangeListener(onCheckedChangeListener(binding.tvTagTheme1));
        binding.radioButton2.setOnCheckedChangeListener(onCheckedChangeListener(binding.tvTagTheme2));
        binding.radioButton3.setOnCheckedChangeListener(onCheckedChangeListener(binding.tvTagTheme3));
        binding.radioButton4.setOnCheckedChangeListener(onCheckedChangeListener(binding.tvTagTheme4));
        binding.radioButton5.setOnCheckedChangeListener(onCheckedChangeListener(binding.tvTagTheme5));
        binding.radioButton6.setOnCheckedChangeListener(onCheckedChangeListener(binding.tvTagTheme6));
        binding.radioButton7.setOnCheckedChangeListener(onCheckedChangeListener(binding.tvTagTheme7));
        binding.radioButton8.setOnCheckedChangeListener(onCheckedChangeListener(binding.tvTagTheme8));
        binding.radioButton9.setOnCheckedChangeListener(onCheckedChangeListener(binding.tvTagTheme9));
        binding.radioButton10.setOnCheckedChangeListener(onCheckedChangeListener(binding.tvTagTheme10));
        binding.radioButton11.setOnCheckedChangeListener(onCheckedChangeListener(binding.tvTagTheme11));
        binding.radioButton12.setOnCheckedChangeListener(onCheckedChangeListener(binding.tvTagTheme12));
        binding.radioButton13.setOnCheckedChangeListener(onCheckedChangeListener(binding.tvTagTheme13));
        binding.radioButton14.setOnCheckedChangeListener(onCheckedChangeListener(binding.tvTagTheme14));
        binding.radioButton15.setOnCheckedChangeListener(onCheckedChangeListener(binding.tvTagTheme15));
        binding.radioButton16.setOnCheckedChangeListener(onCheckedChangeListener(binding.tvTagTheme16));
        binding.radioButton17.setOnCheckedChangeListener(onCheckedChangeListener(binding.tvTagTheme17));
        binding.radioButton18.setOnCheckedChangeListener(onCheckedChangeListener(binding.tvTagTheme18));
        binding.radioButton19.setOnCheckedChangeListener(onCheckedChangeListener(binding.tvTagTheme19));
        binding.radioButton20.setOnCheckedChangeListener(onCheckedChangeListener(binding.tvTagTheme20));
        binding.radioButton21.setOnCheckedChangeListener(onCheckedChangeListener(binding.tvTagTheme21));
        binding.radioButton22.setOnCheckedChangeListener(onCheckedChangeListener(binding.tvTagTheme22));
        binding.radioButton23.setOnCheckedChangeListener(onCheckedChangeListener(binding.tvTagTheme23));
        binding.radioButton24.setOnCheckedChangeListener(onCheckedChangeListener(binding.tvTagTheme24));
    }

    private RadioButton.OnCheckedChangeListener onCheckedChangeListener(TextView tvTagTheme) {
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    theme = tvTagTheme.getText().toString();
                }
            }
        };
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_complete:
                uploadImage();
                break;
        }
    }

    private void uploadImage() {
        Map<String, RequestBody> map = new HashMap<>();
        RequestBody largeImage = RequestBody.create(MediaType.parse("image/*"), bitmapToByteArray(loadImageWithSampleSize(mSession.getFileToUpload(), 750)));
        map.put("file1\"; filename=\""+uid, largeImage);

        float ratio = getImageRatio(getBitmapFromFile(this, mSession.getFileToUpload()));
//        Map<String, RequestBody> map1 = new HashMap<>();
        RequestBody smallImage = RequestBody.create(MediaType.parse("image/*"), bitmapToByteArray(loadImageWithSampleSize(mSession.getFileToUpload(), 300)));
        map.put("file2\"; filename=\"" + ratio, smallImage);

        Call<JsonObject> jsonObjectCall = NetworkUtil.getInstace().uploadImage(map);
        jsonObjectCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject jsonObject = response.body();
                String message = jsonObject.get("message").toString().replace("\"", "");

                JSLog.E("Error Body: " + response.errorBody(), new Throwable());
                JSLog.E("response message: " + response.message(), new Throwable());

                if ("file upload successed".equals(message)) {
                    iid = jsonObject.get("iid").toString().replace("\"", "");
                    uploadData(iid);
                    uploadHashtag(iid);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                JSLog.E("Error message: " + t.getMessage(), t);
                JSLog.E("Error local message: " + t.getLocalizedMessage(), t);
            }
        });
    }

    private void uploadData(String iid) {
        String desc = binding.etDesc.getText().toString();
        String tagCount = String.valueOf(lipFragment.getRVItemCount() + eyeFragment.getRVItemCount() + eyebrowFragment.getRVItemCount() + cheekFragment.getRVItemCount() + contourFragment.getRVItemCount() + skinFragment.getRVItemCount());
        Call<JsonObject> jsonObjectCall = NetworkUtil.getInstace().uploadData(uid, iid, desc, tagCount, cid, coid, String.valueOf(colorCount), color, theme);
        jsonObjectCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject jsonObject = response.body();
                String message = jsonObject.get("message").toString().replace("\"", "");
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    private void uploadHashtag(String iid) {
        String desc = binding.etDesc.getText().toString();
        ArrayList<int[]> hashtagSpans = getSpans(desc, '#');

        SpannableString descContent = new SpannableString(desc);

        if (hashtagSpans != null) {
            for (int i=0; i<hashtagSpans.size(); i++) {
                int[] span = hashtagSpans.get(i);
                int hashTagStart = span[0];
                int hashTagEnd = span[1];

                hashtag.add(descContent.subSequence(hashTagStart + 1, hashTagEnd).toString());
            }
        }

        String tagCount = String.valueOf(hashtag.size());

        Call<JsonObject> jsonObjectCall = NetworkUtil.getInstace().uploadHashtag(iid, tagCount, hashtag);
        jsonObjectCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject jsonObject = response.body();
                String message = jsonObject.get("message").toString().replace("\"", "");
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    private float getImageRatio(Bitmap bitmap) {
        float width = bitmap.getWidth();
        float height = bitmap.getHeight();

        return width / height;
    }

    private void searchHashtag(String keyword) {
        Call<SearchTagResult> searchTagResultCall = NetworkUtil.getInstace().searchTag(keyword);
        searchTagResultCall.enqueue(new Callback<SearchTagResult>() {
            @Override
            public void onResponse(Call<SearchTagResult> call, Response<SearchTagResult> response) {
                SearchTagResult searchTagResult = response.body();
                String message = searchTagResult.getMessage();

                if ("tag search loaded".equals(message)) {

                }
            }

            @Override
            public void onFailure(Call<SearchTagResult> call, Throwable t) {

            }
        });
    }


    @Subscribe
    public void getTagItem(ClickTagItem clickTagItem) {
        if (clickTagItem.getPosition() == Integer.parseInt(LIP) - 1) {
            lips = clickTagItem.getClickItems();
            pagerAdapter.update(ItemType.Lip, lips);
            coid.add(LIP);
        } else if (clickTagItem.getPosition() == Integer.parseInt(EYE)-1) {
            eyes = clickTagItem.getClickItems();
            pagerAdapter.update(ItemType.Eye, eyes);
            coid.add(EYE);
        } else if (clickTagItem.getPosition() == Integer.parseInt(EYEBORW) - 1) {
            eyebrows = clickTagItem.getClickItems();
            pagerAdapter.update(ItemType.Eyebrow, eyebrows);
            coid.add(EYEBORW);
        } else if (clickTagItem.getPosition() == Integer.parseInt(CHEEK) - 1) {
            cheeks = clickTagItem.getClickItems();
            pagerAdapter.update(ItemType.Cheek, cheeks);
            coid.add(CHEEK);
        } else if (clickTagItem.getPosition() == Integer.parseInt(CONTOUR) - 1) {
            contours = clickTagItem.getClickItems();
            pagerAdapter.update(ItemType.Contour, contours);
            coid.add(CONTOUR);
        } else if (clickTagItem.getPosition() == Integer.parseInt(SKIN) - 1) {
            skins = clickTagItem.getClickItems();
            pagerAdapter.update(ItemType.Skin, skins);
            coid.add(SKIN);
        }

        for (int i=0; i<clickTagItem.getClickItems().size(); i++) {
            cid.add(clickTagItem.getClickItems().get(i).getCid());
            JSLog.D("cid list: "+cid.get(i).toString(), new Throwable());
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
