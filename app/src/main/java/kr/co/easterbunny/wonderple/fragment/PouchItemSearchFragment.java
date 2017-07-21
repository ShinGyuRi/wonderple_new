package kr.co.easterbunny.wonderple.fragment;


import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonObject;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import java.util.ArrayList;
import java.util.List;

import kr.co.easterbunny.wonderple.R;
import kr.co.easterbunny.wonderple.adapter.SearchCosmeticAdapter;
import kr.co.easterbunny.wonderple.databinding.FragmentPouchItemSearchBinding;
import kr.co.easterbunny.wonderple.library.util.NetworkUtil;
import kr.co.easterbunny.wonderple.model.SearchCosmeticResult;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class PouchItemSearchFragment extends Fragment {

    private FragmentPouchItemSearchBinding binding;

    private SearchCosmeticAdapter searchCosmeticAdapter;

    private StickyRecyclerHeadersDecoration headersDecoration = null;

    private String uid;

    private List<SearchCosmeticResult.Item> items = new ArrayList<>();

    public PouchItemSearchFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public PouchItemSearchFragment(String uid) {
        this.uid = uid;
    }

    public static PouchItemSearchFragment newInstance(String uid) {
        PouchItemSearchFragment fragment = new PouchItemSearchFragment(uid);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_pouch_item_search, container, false);
        View view = binding.getRoot();

        initViews();

        return view;
    }

    private void initViews() {
        binding.rvSearchResult.setHasFixedSize(true);

        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchCosmetic(s.toString());

                if (s.length() == 0) {
                    binding.rvSearchResult.setVisibility(View.GONE);
                } else {
                    binding.rvSearchResult.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (headersDecoration != null) {
                }
            }
        });


    }

    private synchronized void searchCosmetic(String keyword) {
        Call<SearchCosmeticResult> searchCosmeticResultCall = NetworkUtil.getInstace().searchCosmetic(keyword);
        searchCosmeticResultCall.enqueue(new Callback<SearchCosmeticResult>() {
            @Override
            public void onResponse(Call<SearchCosmeticResult> call, Response<SearchCosmeticResult> response) {
                SearchCosmeticResult searchCosmeticResult = response.body();
                String message = searchCosmeticResult.getMessgae();

                if ("item search done".equals(message)) {
                    if (searchCosmeticResult.getBrands() != null) {
                        items = searchCosmeticResult.getItems();

                        searchCosmeticAdapter = new SearchCosmeticAdapter(getContext(), items, searchCosmeticResult.getBrands());
                        binding.rvSearchResult.setAdapter(searchCosmeticAdapter);
                        binding.rvSearchResult.setLayoutManager(new LinearLayoutManager(getContext()));

                        if (headersDecoration != null) {
                            binding.rvSearchResult.removeItemDecoration(headersDecoration);
                        }
                        headersDecoration = new StickyRecyclerHeadersDecoration(searchCosmeticAdapter);
                        binding.rvSearchResult.addItemDecoration(headersDecoration);
                        headersDecoration.invalidateHeaders();
                    }

                }
            }

            @Override
            public void onFailure(Call<SearchCosmeticResult> call, Throwable t) {

            }
        });
    }

    /*
    여기서 작업을 멈추다..
     */
    private void applyAddItem(String uid) {
//        String cid = items.get(searchCosmeticAdapter.getLastCheckedPos).getCid();
//        Call<JsonObject> jsonObjectCall = NetworkUtil.getInstace().applyPouch(uid, cid);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_item:
                break;
        }
    }

}
