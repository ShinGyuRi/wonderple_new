package kr.co.easterbunny.wonderple.fragment;


import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import kr.co.easterbunny.wonderple.R;
import kr.co.easterbunny.wonderple.adapter.PouchMyItemAdapter;
import kr.co.easterbunny.wonderple.databinding.FragmentPouchMyItemBinding;
import kr.co.easterbunny.wonderple.model.LoadPouchResult;

/**
 * A simple {@link Fragment} subclass.
 */
public class PouchMyItemFragment extends Fragment {

    private FragmentPouchMyItemBinding binding;

    private List<LoadPouchResult.MyItem> myItems = new ArrayList<>();
    private String uid, profileImage;

    public PouchMyItemFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public PouchMyItemFragment(List<LoadPouchResult.MyItem> myItems, String uid, String profileImage) {
        this.myItems = myItems;
        this.uid = uid;
        this.profileImage = profileImage;
    }

    public static PouchMyItemFragment newInstance(List<LoadPouchResult.MyItem> myItems, String uid, String profileImage) {
        PouchMyItemFragment fragment = new PouchMyItemFragment(myItems, uid, profileImage);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_pouch_my_item, container, false);
        View view = binding.getRoot();

        initViews();

        return view;
    }

    private void initViews() {
        if (myItems != null) {
            binding.layoutNoItem.setVisibility(View.GONE);
        }
        binding.rvPouchItem.setAdapter(new PouchMyItemAdapter(getContext(), myItems, uid, profileImage));
        binding.rvPouchItem.setLayoutManager(new GridLayoutManager(getContext(), 2));
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_item:
                break;
        }
    }


}
