package kr.co.easterbunny.wonderple.fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import kr.co.easterbunny.wonderple.R;
import kr.co.easterbunny.wonderple.activity.PostDetailActivity;
import kr.co.easterbunny.wonderple.adapter.WondersItemAdapter;
import kr.co.easterbunny.wonderple.databinding.FragmentMyWonderBinding;
import kr.co.easterbunny.wonderple.library.ParentActivity;
import kr.co.easterbunny.wonderple.library.util.NetworkUtil;
import kr.co.easterbunny.wonderple.listener.RecyclerItemClickListener;
import kr.co.easterbunny.wonderple.model.LoadProfileResult;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyWonderFragment extends Fragment {

    private FragmentMyWonderBinding binding;

    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private WondersItemAdapter wondersItemAdapter;

    private String uid, auid;
    private List<LoadProfileResult.WonderImages> wonderImages = new ArrayList<>();

    public MyWonderFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public MyWonderFragment(String uid, String auid, List<LoadProfileResult.WonderImages> wonderImages) {
        this.uid = uid;
        this.auid = auid;
        this.wonderImages = wonderImages;
    }

    public static MyWonderFragment newInstance(String uid, String auid, List<LoadProfileResult.WonderImages> wonderImages) {
        MyWonderFragment frag = new MyWonderFragment(uid, auid, wonderImages);
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_wonder, container, false);
        View view = binding.getRoot();

        initView();

        return view;
    }

    private void initView() {
        if (wonderImages == null) {
            binding.imgNoWonder.setVisibility(View.VISIBLE);
        } else {
            binding.rvWonderImage.setHasFixedSize(true);

            staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, 1);
            wondersItemAdapter = new WondersItemAdapter(getContext(), wonderImages);
            binding.rvWonderImage.setLayoutManager(staggeredGridLayoutManager);
            binding.rvWonderImage.setAdapter(wondersItemAdapter);

            loadMoreMyWonder();

            binding.rvWonderImage.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    ((ParentActivity) getActivity()).movePostDetailActivity(getContext(), uid, auid, wonderImages.get(position).getIid(), wonderImages.get(position).getImageUrl());
                }
            }));
        }
    }

    int pastVisibleItems, visibleItemCount, totalItemCount;
    private boolean loading = true;
    int loadCount = 1;

    private void loadMoreMyWonder() {
        binding.rvWonderImage.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    visibleItemCount = staggeredGridLayoutManager.getChildCount();
                    totalItemCount = staggeredGridLayoutManager.getItemCount();
                    int[] firstVisibleItems = null;
                    firstVisibleItems = staggeredGridLayoutManager.findFirstVisibleItemPositions(firstVisibleItems);
                    if (firstVisibleItems != null && firstVisibleItems.length > 0) {
                        pastVisibleItems = firstVisibleItems[0];
                    }

                    if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                        if (loading) {
                            loading = false;

                            Call<LoadProfileResult> postImagesCall = NetworkUtil.getInstace().loadMorePost(auid, String.valueOf(loadCount));
                            postImagesCall.enqueue(new Callback<LoadProfileResult>() {
                                @Override
                                public void onResponse(Call<LoadProfileResult> call, Response<LoadProfileResult> response) {
                                    LoadProfileResult loadProfileResult = response.body();
                                    String message = loadProfileResult.getMessage();

                                    if ("more post loaded".equals(message)) {
                                        if (loadProfileResult.getPostImages() != null) {
                                            wondersItemAdapter.add(loadProfileResult.getWonderImages());
                                        }
                                    }

                                    loadCount++;
                                }

                                @Override
                                public void onFailure(Call<LoadProfileResult> call, Throwable t) {

                                }
                            });
                        }
                    } else {
                        loading = true;
                    }
                }
            }
        });
    }
}
