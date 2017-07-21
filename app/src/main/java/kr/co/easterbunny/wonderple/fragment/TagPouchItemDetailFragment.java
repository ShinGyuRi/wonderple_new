package kr.co.easterbunny.wonderple.fragment;


import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import kr.co.easterbunny.wonderple.R;
import kr.co.easterbunny.wonderple.adapter.TagPouchAdapter;
import kr.co.easterbunny.wonderple.databinding.FragmentTagPouchItemDetailBinding;
import kr.co.easterbunny.wonderple.event.ClickBtnSelectComplete;
import kr.co.easterbunny.wonderple.event.ClickEvent;
import kr.co.easterbunny.wonderple.event.ClickTagItem;
import kr.co.easterbunny.wonderple.library.util.JSLog;
import kr.co.easterbunny.wonderple.model.LoadTagPouchResult;

/**
 * A simple {@link Fragment} subclass.
 */
public class TagPouchItemDetailFragment extends Fragment {

    private FragmentTagPouchItemDetailBinding binding;

    private TagPouchAdapter tagPouchAdapter;
    private GridLayoutManager layoutManager;

    private List<LoadTagPouchResult.FacePart> faceParts = new ArrayList<>();
    private List<LoadTagPouchResult.FacePart> clickItems = new ArrayList<>();

    public TagPouchItemDetailFragment() {

    }

    @SuppressLint("ValidFragment")
    public TagPouchItemDetailFragment(List<LoadTagPouchResult.FacePart> faceParts) {
        this.faceParts = faceParts;
    }

    public static TagPouchItemDetailFragment newInstance(List<LoadTagPouchResult.FacePart> faceParts) {
        TagPouchItemDetailFragment fragment = new TagPouchItemDetailFragment(faceParts);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_tag_pouch_item_detail, container, false);

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tag_pouch_item_detail, container, false);
        View view = binding.getRoot();

        initViews();

        return view;
    }

    private void initViews() {
        binding.rvTagItem.setHasFixedSize(true);

        tagPouchAdapter = new TagPouchAdapter(getContext(), faceParts, new TagPouchAdapter.OnItemCheckListener() {
            @Override
            public void onItemCheck(LoadTagPouchResult.FacePart facePart) {
                clickItems.add(facePart);
            }

            @Override
            public void onItemUncheck(LoadTagPouchResult.FacePart facePart) {
                clickItems.remove(facePart);
            }
        });
        layoutManager = new GridLayoutManager(getContext(), 2);
        binding.rvTagItem.setLayoutManager(layoutManager);
        binding.rvTagItem.setAdapter(tagPouchAdapter);
    }

    @Subscribe
    public void clickBtnSelectComplete(ClickBtnSelectComplete event) {
        EventBus.getDefault().post(new ClickTagItem(true, clickItems, event.getSelection()));
        getActivity().finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
