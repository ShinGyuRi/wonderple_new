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
import kr.co.easterbunny.wonderple.databinding.FragmentTermsBinding;
import kr.co.easterbunny.wonderple.library.ParentFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class TermsFragment extends ParentFragment {

    private FragmentTermsBinding binding;


    public TermsFragment() {
        // Required empty public constructor
    }

    public static TermsFragment newInstance() {
        TermsFragment frag = new TermsFragment();
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_terms, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding = FragmentTermsBinding.bind(getView());

        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.loadUrl(getString(R.string.policy_service_url));
        binding.webView.setWebViewClient(new WebViewClientClass());
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
