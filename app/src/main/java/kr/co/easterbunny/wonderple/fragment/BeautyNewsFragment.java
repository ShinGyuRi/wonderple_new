package kr.co.easterbunny.wonderple.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kr.co.easterbunny.wonderple.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BeautyNewsFragment extends Fragment {


    public BeautyNewsFragment() {
        // Required empty public constructor
    }

    public static BeautyNewsFragment newInstance() {
        BeautyNewsFragment fragment = new BeautyNewsFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_beauty_news, container, false);
    }

}
