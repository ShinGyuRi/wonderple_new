package kr.co.easterbunny.wonderple.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import kr.co.easterbunny.wonderple.R;
import kr.co.easterbunny.wonderple.activity.EditorActivity;
import kr.co.easterbunny.wonderple.databinding.FragmentAdjustBinding;
import kr.co.easterbunny.wonderple.event.ClickEvent;
import kr.co.easterbunny.wonderple.event.ConfirmEvent;
import kr.co.easterbunny.wonderple.event.FilterEvent;
import kr.co.easterbunny.wonderple.library.util.Definitions;
import kr.co.easterbunny.wonderple.listener.onKeyBackPressedListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdjustFragment extends Fragment implements onKeyBackPressedListener {

    private FragmentAdjustBinding binding;

    private int filterType = -1;

    public static AdjustFragment newInstance() {
        AdjustFragment frag = new AdjustFragment();
        return frag;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((EditorActivity) activity).setOnKeyBackPressedListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        EventBus.getDefault().register(this);

        return inflater.inflate(R.layout.fragment_adjust, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding = FragmentAdjustBinding.bind(getView());
        binding.setAdjust(this);

        initViews();
    }

    private void initViews() {
    }

    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_rotation:
                filterType = Definitions.FILTER_TYPE.ROTATE;
                EventBus.getDefault().post(new FilterEvent(filterType, 0));
                break;

            case R.id.btn_flip:
                filterType = Definitions.FILTER_TYPE.FLIP;
                EventBus.getDefault().post(new FilterEvent(filterType, 0));
                break;

            case R.id.btn_brightness:
                setBrightness();
                break;

            case R.id.btn_contrast:
                setContrast();
                break;

            case R.id.btn_temperature:
                setTemperature();
                break;

            case R.id.btn_background:
                setDarkBackground();
                break;
        }
    }

    private void setLayoutSeekbar() {
        binding.layoutSeekBar.setVisibility(View.VISIBLE);
        EventBus.getDefault().post(new ClickEvent(true));
    }

    int preProgress = 50;
    int currentProgress = 50;

    private void setBrightness() {
        setLayoutSeekbar();
        binding.sbBrightness.setVisibility(View.VISIBLE);
        filterType = Definitions.FILTER_TYPE.BRIGHTNESS;

        preProgress = binding.sbBrightness.getProgress();
        binding.sbBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                EventBus.getDefault().post(new FilterEvent(filterType, progress));

                currentProgress = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void setContrast() {
        setLayoutSeekbar();
        binding.sbContrast.setVisibility(View.VISIBLE);
        filterType = Definitions.FILTER_TYPE.CONTRAST;

        preProgress = binding.sbContrast.getProgress();
        binding.sbContrast.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                EventBus.getDefault().post(new FilterEvent(filterType, progress));

                currentProgress = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void setTemperature() {
        setLayoutSeekbar();
        binding.sbTemperature.setVisibility(View.VISIBLE);
        filterType = Definitions.FILTER_TYPE.TEMPERATURE;

        preProgress = binding.sbTemperature.getProgress();
        binding.sbTemperature.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                EventBus.getDefault().post(new FilterEvent(filterType, progress));

                currentProgress = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void setDarkBackground() {
        setLayoutSeekbar();
        binding.sbDarkBackground.setVisibility(View.VISIBLE);
        filterType = Definitions.FILTER_TYPE.DARK_BACKGROUND;

        preProgress = binding.sbDarkBackground.getProgress();
        binding.sbDarkBackground.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                EventBus.getDefault().post(new FilterEvent(filterType, progress));

                currentProgress = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Subscribe
    public void confirmEvent(ConfirmEvent confirmEvent) {
        setSeekBarVisibilityGone();

        if (!confirmEvent.isConfirm()) {
            if (filterType == Definitions.FILTER_TYPE.BRIGHTNESS) {
                binding.sbBrightness.setProgress(preProgress);
            } else if (filterType == Definitions.FILTER_TYPE.CONTRAST) {
                binding.sbContrast.setProgress(preProgress);
            } else if (filterType == Definitions.FILTER_TYPE.TEMPERATURE) {
                binding.sbTemperature.setProgress(preProgress);
            } else if (filterType == Definitions.FILTER_TYPE.DARK_BACKGROUND) {
                binding.sbDarkBackground.setProgress(preProgress);
            }
        }
    }

    @Override
    public void onBack() {
        if (binding.layoutSeekBar.getVisibility() == View.VISIBLE) {
            setSeekBarVisibilityGone();
        } else {
            EditorActivity activity = (EditorActivity) getActivity();
            activity.setOnKeyBackPressedListener(null);
            activity.onBackPressed();
        }
    }

    private void setSeekBarVisibilityGone() {
        binding.layoutSeekBar.setVisibility(View.GONE);
        binding.sbBrightness.setVisibility(View.GONE);
        binding.sbContrast.setVisibility(View.GONE);
        binding.sbTemperature.setVisibility(View.GONE);
        binding.sbDarkBackground.setVisibility(View.GONE);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }
}
