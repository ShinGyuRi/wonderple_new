package kr.co.easterbunny.wonderple.fragment;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zomato.photofilters.SampleFilters;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.ColorOverlaySubfilter;
import com.zomato.photofilters.imageprocessors.subfilters.SaturationSubfilter;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import kr.co.easterbunny.wonderple.R;
import kr.co.easterbunny.wonderple.adapter.EffectAdapter;
import kr.co.easterbunny.wonderple.databinding.FragmentFilterBinding;
import kr.co.easterbunny.wonderple.library.util.FileUtil;
import kr.co.easterbunny.wonderple.listener.EffectAdapterListener;
import kr.co.easterbunny.wonderple.manager.ThumbnailManager;
import kr.co.easterbunny.wonderple.model.Thumbnail;

/**
 * A simple {@link Fragment} subclass.
 */
public class FilterFragment extends Fragment implements EffectAdapterListener{

    private FragmentFilterBinding binding;


    public static FilterFragment newInstance() {
        FilterFragment frag = new FilterFragment();
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_filter, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding = FragmentFilterBinding.bind(getView());

        initViews();
    }

    private void initViews() {
        binding.mEffectChooserRecyclerView.setItemAnimator(new DefaultItemAnimator());
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.mEffectChooserRecyclerView.setLayoutManager(mLayoutManager);

        final EffectAdapter effectAdapter = new EffectAdapter(getContext());
        effectAdapter.setListener(this);
        binding.mEffectChooserRecyclerView.setAdapter(effectAdapter);

        effectAdapter.setItems(getFilters());
    }

    private List<Thumbnail> getFilters() {
        Bitmap bitmap = FileUtil.getBitmapFromFile(getContext(), FileUtil.getTempImageFile(getContext()));

        Thumbnail t1 = new Thumbnail();
        Thumbnail t2 = new Thumbnail();
        Thumbnail t3 = new Thumbnail();
        Thumbnail t4 = new Thumbnail();
        Thumbnail t5 = new Thumbnail();
        Thumbnail t6 = new Thumbnail();
        Thumbnail t7 = new Thumbnail();

        t1.image = bitmap;
        t2.image = bitmap;
        t3.image = bitmap;
        t4.image = bitmap;
        t5.image = bitmap;
        t6.image = bitmap;
        t7.image = bitmap;

        ThumbnailManager.clearThumbs();
        t1.name = "None";
        ThumbnailManager.addThumb(t1);

        t2.name = "StarLit";
        t2.filter = SampleFilters.getStarLitFilter();
        ThumbnailManager.addThumb(t2);

        t3.name = "BlueMess";
        t3.filter = SampleFilters.getBlueMessFilter();
        ThumbnailManager.addThumb(t3);

        t4.name = "AweStruckVibe";
        t4.filter = SampleFilters.getAweStruckVibeFilter();
        ThumbnailManager.addThumb(t4);

        t5.name = "Lime";
        t5.filter = SampleFilters.getLimeStutterFilter();
        ThumbnailManager.addThumb(t5);

        t6.name = "B&W";
        t6.filter = new Filter();
        t6.filter.addSubFilter(new SaturationSubfilter(-100f));
        ThumbnailManager.addThumb(t6);

        t7.name = "Sepia";
        t7.filter = new Filter();
        t7.filter.addSubFilter(new SaturationSubfilter(-100f));
        t7.filter.addSubFilter(new ColorOverlaySubfilter(1, 102, 51, 0));
        ThumbnailManager.addThumb(t7);

        return ThumbnailManager.processThumbs(getContext());
    }

    @Override
    public void applyEffectType(Filter filter) {
        EventBus.getDefault().post(filter);
    }
}
