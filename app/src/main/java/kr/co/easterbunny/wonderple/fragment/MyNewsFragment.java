package kr.co.easterbunny.wonderple.fragment;


import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import kr.co.easterbunny.wonderple.R;
import kr.co.easterbunny.wonderple.adapter.NewsAdapter;
import kr.co.easterbunny.wonderple.databinding.FragmentMyNewsBinding;
import kr.co.easterbunny.wonderple.library.ParentActivity;
import kr.co.easterbunny.wonderple.library.util.NetworkUtil;
import kr.co.easterbunny.wonderple.model.LoadNewsResult;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyNewsFragment extends Fragment {

    private FragmentMyNewsBinding binding;

    private List<LoadNewsResult.News> newsList = new ArrayList<>();

    private String uid;

    private LinearLayoutManager layoutManager;
    private NewsAdapter newsAdapter;

    private RequestFollowFragment requestFollowFragment;

    public MyNewsFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public MyNewsFragment(String uid) {
        this.uid = uid;
    }

    public static MyNewsFragment newInstance(String uid) {
        MyNewsFragment fragment = new MyNewsFragment(uid);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_my_news, container, false);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_news, container, false);
        binding.setMyNews(this);
        View view = binding.getRoot();

        loadNews(uid);
        loadMoreNews(uid);

        return view;
    }

    private void loadNews(String uid) {
        Call<LoadNewsResult> loadNewsResultCall = NetworkUtil.getInstace().loadNews(uid);
        loadNewsResultCall.enqueue(new Callback<LoadNewsResult>() {
            @Override
            public void onResponse(Call<LoadNewsResult> call, Response<LoadNewsResult> response) {
                LoadNewsResult loadNewsResult = response.body();
                String message = loadNewsResult.getMessage();

                if ("news load succeeded".equals(message)) {
                    newsList = loadNewsResult.getNewsList();

                    if (!"0".equals(loadNewsResult.getFollerRequest())) {
                        binding.layoutRequestFollow.setVisibility(View.VISIBLE);
                        binding.tvRequestCount.setText(loadNewsResult.getFollerRequest());
                    }

                    newsAdapter = new NewsAdapter(getContext(), newsList, uid);
                    layoutManager = new LinearLayoutManager(getContext());

                    binding.rvNews.setAdapter(newsAdapter);
                    binding.rvNews.setLayoutManager(layoutManager);
                }
            }

            @Override
            public void onFailure(Call<LoadNewsResult> call, Throwable t) {

            }
        });
    }

    int pastVisibleItems, visibleItemCount, totalItemCount;
    private boolean loading = true;
    int loadCount = 1;

    private void loadMoreNews(String uid) {
        binding.rvNews.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    pastVisibleItems = layoutManager.findFirstVisibleItemPosition();

                    if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                        if (loading) {
                            loading = false;

                            Call<LoadNewsResult> loadNewsResultCall = NetworkUtil.getInstace().loadMoreNews(uid, String.valueOf(loadCount));
                            loadNewsResultCall.enqueue(new Callback<LoadNewsResult>() {
                                @Override
                                public void onResponse(Call<LoadNewsResult> call, Response<LoadNewsResult> response) {
                                    LoadNewsResult loadNewsResult = response.body();
                                    String message = loadNewsResult.getMessage();

                                    if ("more news load succeeded".equals(message)) {
                                        if (loadNewsResult.getNewsList() != null) {
                                            newsAdapter.add(loadNewsResult.getNewsList());
                                        }
                                    }

                                    loadCount++;
                                }

                                @Override
                                public void onFailure(Call<LoadNewsResult> call, Throwable t) {

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

    private void newsRefresh(String uid) {
        Call<JsonObject> jsonObjectCall = NetworkUtil.getInstace().newsRefresh(uid);
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

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_request_follow:
                requestFollowFragment = RequestFollowFragment.newInstance(uid);
                ((ParentActivity) getActivity()).switchContent(requestFollowFragment, R.id.container, true, true);
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        newsRefresh(uid);
    }
}
