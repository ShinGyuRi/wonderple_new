package kr.co.easterbunny.wonderple.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import kr.co.easterbunny.wonderple.R;
import kr.co.easterbunny.wonderple.databinding.ActivityMainBinding;
import kr.co.easterbunny.wonderple.fragment.NewsFragment;
import kr.co.easterbunny.wonderple.fragment.SearchFragment;
import kr.co.easterbunny.wonderple.fragment.HomeFragment;
import kr.co.easterbunny.wonderple.fragment.MyPageFragment;
import kr.co.easterbunny.wonderple.library.ParentActivity;
import kr.co.easterbunny.wonderple.library.WonderpleLib;
import kr.co.easterbunny.wonderple.library.gcm.GcmUtil;
import kr.co.easterbunny.wonderple.library.gcm.services.RegistrationIntentService;
import kr.co.easterbunny.wonderple.library.util.Definitions;
import kr.co.easterbunny.wonderple.library.util.JSLog;
import kr.co.easterbunny.wonderple.library.util.PrefUtil;
import kr.co.easterbunny.wonderple.model.SignInResult;

public class MainActivity extends ParentActivity {

    public static final String TAG = MainActivity.class.getSimpleName();


    private ActivityMainBinding binding;


    public Fragment homeFragment, searchFragment, myPageFragment, newsFragment;

    protected SignInResult userData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setSwitchContent(this);

        //Receive Token
        if (GcmUtil.checkPlayServices(this)) {  startService(new Intent(this, RegistrationIntentService.class));    }

        PrefUtil.getInstance().putPreference(Definitions.PREFKEY.IS_VISIT_EXPERIENCE_BOOL, true);

        WonderpleLib.getInstance().func01_loadUserDataToFile(MainActivity.this);
        userData = WonderpleLib.getInstance().func01_loadUserDataFromMemory();

        setInitFrag();
    }


    @Override
    protected void onResume() {
        super.onResume();
        getSupportFragmentManager().addOnBackStackChangedListener(backStackChangedListener);
    }

    public void setInitFrag()  {
        homeFragment = HomeFragment.newInstance(userData.getUser().getUdid());
        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment, "HOME").addToBackStack("HOME").commit();
        binding.btnHome.setEnabled(false);
    }

    public void bottomBtnClick(View v) {
        switch (v.getId()) {
            case R.id.btn_home:
                initNaviButton(v);
                if(homeFragment == null)
                    homeFragment = HomeFragment.newInstance(userData.getUser().getUdid());
                switchContent(homeFragment, "HOME");
                break;

            case R.id.btn_search:
                initNaviButton(v);
                if (searchFragment == null)
                    searchFragment = SearchFragment.newInstance();
                switchContent(searchFragment, "SEARCH");
                break;

            case R.id.btn_camera:
                startActivity(new Intent(this, CameraActivity.class));
                break;

            case R.id.btn_news:
                initNaviButton(v);
                if (newsFragment == null)
                    newsFragment = NewsFragment.newInstance(userData.getUser().getUdid());
                switchContent(newsFragment, "NEWS");
                break;

            case R.id.btn_my_page:
                initNaviButton(v);
                if (myPageFragment == null)
                    myPageFragment = MyPageFragment.newInstance(userData.getUser().getUdid());
                switchContent(myPageFragment, "MY_PAGE");
                break;
        }
    }


    private void initNaviButton(View v) {
        binding.btnHome.setEnabled(true);
        binding.btnSearch.setEnabled(true);
        binding.btnNews.setEnabled(true);
        binding.btnMyPage.setEnabled(true);
        v.setEnabled(false);
    }

    public void switchContent(Fragment fragment, String tag) {
        FragmentManager fm = getSupportFragmentManager();

        JSLog.D("back stack cnt:" + fm.getBackStackEntryCount(),new Throwable());

        boolean fragmentPopped = fm.popBackStackImmediate(tag, 0);

        if (!fragmentPopped) {
            FragmentTransaction ft = fm.beginTransaction();
            ft.addToBackStack(tag);
            ft.replace(R.id.container, fragment, tag).commit();
        }

    }


    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1){
            finish();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            {
                MainActivity.this.finishAffinity();
            }
            else
            {
                System.exit(0);
            }
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        getSupportFragmentManager().removeOnBackStackChangedListener(backStackChangedListener);
    }

    private FragmentManager.OnBackStackChangedListener backStackChangedListener = new FragmentManager.OnBackStackChangedListener() {
        @Override
        public void onBackStackChanged() {
            String currentTab = getSupportFragmentManager().findFragmentById(R.id.container).getTag();
            JSLog.D(currentTab, new Throwable());
            if(currentTab==null) return;
            switch(currentTab){
                case "HOME":
                    initNaviButton(binding.btnHome);
                    break;
                case "SEARCH":
                    initNaviButton(binding.btnSearch);
                    break;
                case "NEWS":
                    initNaviButton(binding.btnNews);
                    break;
                case "MY_PAGE":
                    initNaviButton(binding.btnMyPage);
                    break;
            }
        }
    };
}
