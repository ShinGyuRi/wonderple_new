package kr.co.easterbunny.wonderple.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import kr.co.easterbunny.wonderple.R;
import kr.co.easterbunny.wonderple.databinding.FragmentPrivacyBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class PrivacyFragment extends Fragment {

    private FragmentPrivacyBinding binding;


    public PrivacyFragment() {
        // Required empty public constructor
    }

    public static PrivacyFragment newInstance() {
        PrivacyFragment frag = new PrivacyFragment();
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_privacy, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding = FragmentPrivacyBinding.bind(getView());

        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.loadUrl(getString(R.string.policy_privacy_url));
        binding.webView.setWebViewClient(new PrivacyFragment.WebViewClientClass());
        binding.webView.setVerticalScrollBarEnabled(true);
    }

    private class WebViewClientClass extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
