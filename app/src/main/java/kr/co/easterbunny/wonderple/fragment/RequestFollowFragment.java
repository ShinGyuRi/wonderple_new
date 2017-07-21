package kr.co.easterbunny.wonderple.fragment;


import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import kr.co.easterbunny.wonderple.R;
import kr.co.easterbunny.wonderple.adapter.RequestFollowAdapter;
import kr.co.easterbunny.wonderple.databinding.FragmentRequestFollowBinding;
import kr.co.easterbunny.wonderple.library.util.NetworkUtil;
import kr.co.easterbunny.wonderple.model.LoadFollowNewsResult;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class RequestFollowFragment extends Fragment {

    private FragmentRequestFollowBinding binding;

    private String uid;

    private List<LoadFollowNewsResult.Follow> followList = new ArrayList<>();

    public RequestFollowFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public RequestFollowFragment(String uid) {
        this.uid = uid;
    }

    public static RequestFollowFragment newInstance(String uid) {
        RequestFollowFragment followFragment = new RequestFollowFragment(uid);
        return followFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_request_follow, container, false);

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_request_follow, container, false);
        View view = binding.getRoot();

        initViews();
        loadFollowNews(uid);

        return view;
    }

    private void initViews() {
    }

    private void loadFollowNews(String uid) {
        Call<LoadFollowNewsResult> loadFollowNewsResultCall = NetworkUtil.getInstace().loadFollowNews(uid);
        loadFollowNewsResultCall.enqueue(new Callback<LoadFollowNewsResult>() {
            @Override
            public void onResponse(Call<LoadFollowNewsResult> call, Response<LoadFollowNewsResult> response) {
                LoadFollowNewsResult loadFollowNewsResult = response.body();
                String message = loadFollowNewsResult.getMessage();

                if ("temp follow load done".equals(message)) {
                    followList = loadFollowNewsResult.getFollowList();

                    if (followList != null) {
                        binding.rvFollowList.setAdapter(new RequestFollowAdapter(getContext(), followList, uid));
                        binding.rvFollowList.setLayoutManager(new LinearLayoutManager(getContext()));
                    }
                }
            }

            @Override
            public void onFailure(Call<LoadFollowNewsResult> call, Throwable t) {

            }
        });
    }

}
