package kr.co.easterbunny.wonderple.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import kr.co.easterbunny.wonderple.R;
import kr.co.easterbunny.wonderple.adapter.WonderpleListAdapter;
import kr.co.easterbunny.wonderple.databinding.ActivityWonderpleListBinding;
import kr.co.easterbunny.wonderple.library.ParentActivity;
import kr.co.easterbunny.wonderple.library.util.Definitions.LIST_TYPE;
import kr.co.easterbunny.wonderple.library.util.NetworkUtil;
import kr.co.easterbunny.wonderple.model.LoadWonderResult;
import kr.co.easterbunny.wonderple.model.LoadWonderpleResult;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WonderpleListActivity extends ParentActivity {

    private ActivityWonderpleListBinding binding;

    private String iid, uid, listType;

    private List<LoadWonderpleResult.Wonderple> wonderples = new ArrayList<>();
    private List<LoadWonderResult.Wonder> wonders = new ArrayList<>();
    private WonderpleListAdapter wonderpleListAdapter;
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_wonderple_list);
        binding.setWonderpleList(this);

        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        iid = intent.getStringExtra("iid");
        uid = intent.getStringExtra("uid");
        listType = intent.getStringExtra("listType");

        layoutManager = new LinearLayoutManager(this);
        binding.rvWonderple.setLayoutManager(layoutManager);

        if (LIST_TYPE.WONDERPLE.equals(listType)) {
            loadWonderpleList();
            loadMoreWonderple();
        } else if (LIST_TYPE.WONDER.equals(listType)) {
            binding.tvTitle.setText("원더한 사람");
            loadWonder();
            loadMoreWonder();
        }
    }

    private void loadWonderpleList() {
        Call<LoadWonderpleResult> loadWonderpleResultCall = NetworkUtil.getInstace().loadWonderple(iid, uid);
        loadWonderpleResultCall.enqueue(new Callback<LoadWonderpleResult>() {
            @Override
            public void onResponse(Call<LoadWonderpleResult> call, Response<LoadWonderpleResult> response) {
                LoadWonderpleResult loadWonderpleResult = response.body();
                String message = loadWonderpleResult.getMessage();

                if ("wonderple list is loaded".equals(message)) {
                    if (loadWonderpleResult.getWonderples() != null) {
                        wonderples = loadWonderpleResult.getWonderples();

                        wonderpleListAdapter = new WonderpleListAdapter(WonderpleListActivity.this, wonderples, uid, LIST_TYPE.WONDERPLE);
                        binding.rvWonderple.setAdapter(wonderpleListAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<LoadWonderpleResult> call, Throwable t) {

            }
        });
    }

    int pastVisibleItems, visibleItemCount, totalItemCount;
    private boolean loading = true;
    int loadCount = 1;

    private void loadMoreWonderple() {
        binding.rvWonderple.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    pastVisibleItems = layoutManager.findFirstVisibleItemPosition();

                    if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                        if (loading) {
                            loading = false;

                            Call<LoadWonderpleResult> loadWonderpleResultCall = NetworkUtil.getInstace().loadMoreWonderple(iid, uid, String.valueOf(loadCount));
                            loadWonderpleResultCall.enqueue(new Callback<LoadWonderpleResult>() {
                                @Override
                                public void onResponse(Call<LoadWonderpleResult> call, Response<LoadWonderpleResult> response) {
                                    LoadWonderpleResult loadWonderpleResult = response.body();
                                    String message = loadWonderpleResult.getMessage();

                                    if ("more wonderple list is loaded".equals(message)) {
                                        if (loadWonderpleResult.getWonderples() != null) {
                                            wonderpleListAdapter.addWonderple(loadWonderpleResult.getWonderples());
                                        }
                                    }

                                    loadCount++;
                                }

                                @Override
                                public void onFailure(Call<LoadWonderpleResult> call, Throwable t) {

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

    private void loadWonder() {
        Call<LoadWonderResult> loadWonderResultCall = NetworkUtil.getInstace().loadWonder(iid, uid);
        loadWonderResultCall.enqueue(new Callback<LoadWonderResult>() {
            @Override
            public void onResponse(Call<LoadWonderResult> call, Response<LoadWonderResult> response) {
                LoadWonderResult loadWonderResult = response.body();
                String message = loadWonderResult.getMessage();

                if ("wonder people list is loaded".equals(message)) {
                    if (loadWonderResult.getWonders() != null) {
                        wonders = loadWonderResult.getWonders();

                        wonderpleListAdapter = new WonderpleListAdapter(WonderpleListActivity.this, uid, wonders, LIST_TYPE.WONDER);
                        binding.rvWonderple.setAdapter(wonderpleListAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<LoadWonderResult> call, Throwable t) {

            }
        });
    }

    private void loadMoreWonder() {
        binding.rvWonderple.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    pastVisibleItems = layoutManager.findFirstVisibleItemPosition();

                    if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                        if (loading) {
                            loading = false;

                            Call<LoadWonderResult> loadWonderResultCall = NetworkUtil.getInstace().loadMoreWonder(iid, uid, String.valueOf(loadCount));
                            loadWonderResultCall.enqueue(new Callback<LoadWonderResult>() {
                                @Override
                                public void onResponse(Call<LoadWonderResult> call, Response<LoadWonderResult> response) {
                                    LoadWonderResult loadWonderResult = response.body();
                                    String message = loadWonderResult.getMessage();

                                    if ("more wonder people list is loaded".equals(message)) {
                                        if (loadWonderResult.getWonders() != null) {
                                            wonderpleListAdapter.addWonder(loadWonderResult.getWonders());
                                        }
                                    }
                                    loadCount++;
                                }

                                @Override
                                public void onFailure(Call<LoadWonderResult> call, Throwable t) {

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

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                onBackPressed();
                break;
        }
    }
}
