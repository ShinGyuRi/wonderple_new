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
import kr.co.easterbunny.wonderple.adapter.CommentAdapter;
import kr.co.easterbunny.wonderple.databinding.FragmentTalkDetailBinding;
import kr.co.easterbunny.wonderple.library.util.JSLog;
import kr.co.easterbunny.wonderple.library.util.NetworkUtil;
import kr.co.easterbunny.wonderple.model.LoadCommentResult;
import kr.co.easterbunny.wonderple.model.LoadTalkResult;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class TalkDetailFragment extends Fragment {

    private FragmentTalkDetailBinding binding;

    private String uid, cid;

    private List<LoadCommentResult.Comment> talks = new ArrayList<>();

    public TalkDetailFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public TalkDetailFragment(String uid, String cid) {
        this.uid = uid;
        this.cid = cid;
    }

    public static TalkDetailFragment newInstance(String uid, String cid) {
        TalkDetailFragment fragment = new TalkDetailFragment(uid, cid);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_talk_detail, container, false);
        View view = binding.getRoot();

        initViews();

        return view;
    }

    private void initViews() {
        loadTalk();
    }

    private void loadTalk() {
        Call<LoadTalkResult> loadTalkResultCall = NetworkUtil.getInstace().loadTalk(uid, cid);
        loadTalkResultCall.enqueue(new Callback<LoadTalkResult>() {
            @Override
            public void onResponse(Call<LoadTalkResult> call, Response<LoadTalkResult> response) {
                LoadTalkResult loadTalkResult = response.body();
                String message = loadTalkResult.getMessage();

                if ("item talk load done".equals(message)) {
                    talks = loadTalkResult.getTalks();

                    binding.rvTalk.setAdapter(new CommentAdapter(getContext(), talks, uid));
                    binding.rvTalk.setLayoutManager(new LinearLayoutManager(getContext()));
                }
            }

            @Override
            public void onFailure(Call<LoadTalkResult> call, Throwable t) {
                JSLog.E("Error Message: " + t.getMessage(), t);
                JSLog.E("Error Local Message: " + t.getLocalizedMessage(), t);
            }
        });
    }


}
