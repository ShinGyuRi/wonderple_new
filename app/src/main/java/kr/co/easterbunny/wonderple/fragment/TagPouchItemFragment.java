package kr.co.easterbunny.wonderple.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import kr.co.easterbunny.wonderple.R;
import kr.co.easterbunny.wonderple.activity.TagPouchActivity;
import kr.co.easterbunny.wonderple.adapter.TagPouchItemAdapter;
import kr.co.easterbunny.wonderple.databinding.FragmentTagPouchItemBinding;
import kr.co.easterbunny.wonderple.event.ClickTagItem;
import kr.co.easterbunny.wonderple.library.util.JSLog;
import kr.co.easterbunny.wonderple.listener.Updateable;
import kr.co.easterbunny.wonderple.model.ItemType;
import kr.co.easterbunny.wonderple.model.LoadTagPouchResult;

import static kr.co.easterbunny.wonderple.library.util.Definitions.FACE.EYE;
import static kr.co.easterbunny.wonderple.library.util.Definitions.FACE.LIP;

public class TagPouchItemFragment extends Fragment implements Updateable{

    private FragmentTagPouchItemBinding binding;
    private ItemType itemType;
    private List<LoadTagPouchResult.FacePart> faceParts = new ArrayList<>();

    private TagPouchItemAdapter tagPouchItemAdapter;
    private LinearLayoutManager layoutManager;

    public TagPouchItemFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public TagPouchItemFragment(ItemType itemType, List<LoadTagPouchResult.FacePart> faceParts) {
        this.itemType = itemType;
        this.faceParts = faceParts;
    }

    // TODO: Rename and change types and number of parameters
    public static TagPouchItemFragment newInstance(ItemType itemType, List<LoadTagPouchResult.FacePart> faceParts) {
        TagPouchItemFragment fragment = new TagPouchItemFragment(itemType, faceParts);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_tag_pouch_item, container, false);

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tag_pouch_item, container, false);
        View view = binding.getRoot();
        binding.setTagPouchItem(this);

        tagPouchItemAdapter = new TagPouchItemAdapter(getContext(), faceParts);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.rvTagItem.setAdapter(tagPouchItemAdapter);
        binding.rvTagItem.setLayoutManager(layoutManager);

        return view;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_add_item:
                Intent intent = new Intent(getContext(), TagPouchActivity.class);
                intent.putExtra("itemType", itemType);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void update(ItemType itemType, List<LoadTagPouchResult.FacePart> faceParts) {
        JSLog.D("TEST!!!!!!!!", new Throwable());
//        tagPouchItemAdapter.add(faceParts);
        if (this.itemType == itemType) {
            this.faceParts.addAll(faceParts);
            tagPouchItemAdapter.notifyDataSetChanged();
        }
    }

    public int getRVItemCount() {
        if (tagPouchItemAdapter == null) {
            return 0;
        }
        return tagPouchItemAdapter.getItemCount();
    }
}
