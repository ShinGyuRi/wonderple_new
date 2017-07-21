package kr.co.easterbunny.wonderple.fragment;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Arrays;

import kr.co.easterbunny.wonderple.R;
import kr.co.easterbunny.wonderple.adapter.SearchAdapter;
import kr.co.easterbunny.wonderple.databinding.FragmentSearchBinding;
import kr.co.easterbunny.wonderple.library.ParentFragment;
import kr.co.easterbunny.wonderple.model.Cheeses;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends ParentFragment {


    private FragmentSearchBinding binding;

    private StaggeredGridLayoutManager staggeredGridLayoutManager;


    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance()    {
        SearchFragment frag = new SearchFragment();
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false);
        View view = binding.getRoot();

//        binding.rvPostImage.setHasFixedSize(true);
//
//        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, 1);
//        binding.rvPostImage.setLayoutManager(staggeredGridLayoutManager);
//        binding.rvPostImage.setAdapter(new SearchAdapter(getContext(), Arrays.asList(Cheeses.postImage)));

        return view;
    }


}
