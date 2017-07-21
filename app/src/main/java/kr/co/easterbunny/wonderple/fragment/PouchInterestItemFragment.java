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
import kr.co.easterbunny.wonderple.adapter.PouchInterestItemAdapter;
import kr.co.easterbunny.wonderple.databinding.FragmentPouchInterestItemBinding;
import kr.co.easterbunny.wonderple.model.LoadPouchResult;

/**
 * A simple {@link Fragment} subclass.
 */
public class PouchInterestItemFragment extends Fragment {

    private FragmentPouchInterestItemBinding binding;

    private List<LoadPouchResult.Favorite> favorites = new ArrayList<>();

    public PouchInterestItemFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public PouchInterestItemFragment(List<LoadPouchResult.Favorite> favorites) {
        this.favorites = favorites;
    }

    public static PouchInterestItemFragment newInstance(List<LoadPouchResult.Favorite> favorites) {
        PouchInterestItemFragment fragment = new PouchInterestItemFragment(favorites);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_pouch_interest_item, container, false);
        View view = binding.getRoot();

        initViews();

        return view;
    }

    private void initViews() {
        if (favorites != null) {
            binding.imgNoItem.setVisibility(View.GONE);
        }
        binding.rvPouchItem.setAdapter(new PouchInterestItemAdapter(getContext(), favorites));
        binding.rvPouchItem.setLayoutManager(new GridLayoutManager(getContext(), 2));
    }

}
