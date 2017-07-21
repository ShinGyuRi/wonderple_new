package kr.co.easterbunny.wonderple.fragment;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import java.util.Arrays;
import java.util.List;

import kr.co.easterbunny.wonderple.R;
import kr.co.easterbunny.wonderple.activity.PostDetailActivity;
import kr.co.easterbunny.wonderple.adapter.SearchAdapter;
import kr.co.easterbunny.wonderple.databinding.DlgProfilePostBinding;
import kr.co.easterbunny.wonderple.databinding.FragmentMyPostBinding;
import kr.co.easterbunny.wonderple.dialog.ProfilePostDialog;
import kr.co.easterbunny.wonderple.library.ParentActivity;
import kr.co.easterbunny.wonderple.library.WonderpleLib;
import kr.co.easterbunny.wonderple.library.util.JSLog;
import kr.co.easterbunny.wonderple.library.util.NetworkUtil;
import kr.co.easterbunny.wonderple.listener.RecyclerItemClickListener;
import kr.co.easterbunny.wonderple.listener.RecyclerViewOnItemClickListener;
import kr.co.easterbunny.wonderple.model.Cheeses;
import kr.co.easterbunny.wonderple.model.LoadProfileResult;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyPostFragment extends Fragment {

    private FragmentMyPostBinding binding;

    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private SearchAdapter searchAdapter;

    private String uid, auid;
    private List<LoadProfileResult.PostImages> postImages = new ArrayList<>();

    public MyPostFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public MyPostFragment(String uid, String auid, List<LoadProfileResult.PostImages> postImages) {
        this.uid = uid;
        this.auid = auid;
        this.postImages = postImages;
    }

    public static MyPostFragment newInstance(String uid, String auid, List<LoadProfileResult.PostImages> postImages) {
        MyPostFragment frag = new MyPostFragment(uid, auid, postImages);
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_post, container, false);
        View view = binding.getRoot();

        initView();

        return view;
    }

    private void initView() {
        if (postImages == null) {
            binding.imgNoPosting.setVisibility(View.VISIBLE);
        } else {
            binding.rvPostImage.setHasFixedSize(true);

            staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            searchAdapter = new SearchAdapter(getContext(), postImages);
            binding.rvPostImage.setLayoutManager(staggeredGridLayoutManager);
            binding.rvPostImage.setAdapter(searchAdapter);

            loadMoreMyPost();

            binding.rvPostImage.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    ((ParentActivity) getActivity()).movePostDetailActivity(getContext(), uid, auid, postImages.get(position).getIid(), postImages.get(position).getImageUrl());
                }
            }));

            if (uid.equals(auid)) {
                setPostLongClick();
            }
        }
    }

    int pastVisibleItems, visibleItemCount, totalItemCount;
    private boolean loading = true;
    int loadCount = 1;

    private void loadMoreMyPost() {
        binding.rvPostImage.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                                            searchAdapter.add(loadProfileResult.getPostImages());
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


    private void setPostLongClick() {
        binding.rvPostImage.addOnItemTouchListener(new RecyclerViewOnItemClickListener(getContext(), binding.rvPostImage, new RecyclerViewOnItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {

            }

            @Override
            public void onItemLongClick(View v, int position) {
                ProfilePostDialog dialog = new ProfilePostDialog(getContext(), uid, postImages.get(position).getIid());
                dialog.show();
            }
        }));


    }

}
