package kr.co.easterbunny.wonderple.fragment;


import android.content.Context;
import android.database.Cursor;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import kr.co.easterbunny.wonderple.R;
import kr.co.easterbunny.wonderple.adapter.GalleryAdapter;
import kr.co.easterbunny.wonderple.databinding.FragmentGalleryPickerBinding;
import kr.co.easterbunny.wonderple.event.ClickEvent;
import kr.co.easterbunny.wonderple.library.BaseApplication;
import kr.co.easterbunny.wonderple.library.ParentFragment;
import kr.co.easterbunny.wonderple.library.util.FileUtil;
import kr.co.easterbunny.wonderple.listener.GalleryPickerFragmentListener;
import kr.co.easterbunny.wonderple.listener.GridAdapterListener;
import kr.co.easterbunny.wonderple.model.Session;

import static kr.co.easterbunny.wonderple.library.util.FileUtil.saveBitmap;

/**
 * A simple {@link Fragment} subclass.
 */
public class GalleryPickerFragment extends ParentFragment implements GridAdapterListener{


    private FragmentGalleryPickerBinding binding;

    private GridLayoutManager gridLayoutManager;
    public static GalleryAdapter galleryAdapter;

    private GalleryPickerFragmentListener listener;
    private Session mSession = Session.getInstance();

    private static final int MARGING_GRID = 2;


    public static GalleryPickerFragment newInstance() {
        GalleryPickerFragment frag = new GalleryPickerFragment();
        return frag;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (GalleryPickerFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement GalleryPickerFragmentListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gallery_picker, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding = FragmentGalleryPickerBinding.bind(getView());

        initViews();

        binding.mPreview.setOnClickListener(v -> binding.mAppBarContainer.setExpanded(true, true));

    }


    @Override
    public void onPause() {
        super.onPause();
        Glide.with(getContext()).pauseRequests();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initViews() {
        binding.gallery.setHasFixedSize(true);

        galleryAdapter = new GalleryAdapter(getContext());
        galleryAdapter.setListener(this);

        gridLayoutManager = new GridLayoutManager(getContext(), 4);
        binding.gallery.setLayoutManager(gridLayoutManager);
        binding.gallery.setAdapter(galleryAdapter);
        binding.gallery.addItemDecoration(addItemDecoration());
        galleryAdapter.setItems(getImagesFromGallary());

        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) binding.mAppBarContainer.getLayoutParams();
        lp.height = getResources().getDisplayMetrics().widthPixels;
        binding.mAppBarContainer.setLayoutParams(lp);

        List<Uri> images = getImagesFromGallary();
        displayPreview(images.get(0));
    }


    private RecyclerView.ItemDecoration addItemDecoration() {
        return new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view,
                                       RecyclerView parent, RecyclerView.State state) {
                outRect.left = MARGING_GRID;
                outRect.right = MARGING_GRID;
                outRect.bottom = MARGING_GRID;
                if (parent.getChildLayoutPosition(view) >= 0 && parent.getChildLayoutPosition(view) <= 3) {
                    outRect.top = MARGING_GRID;
                }
            }
        };
    }


    @Subscribe
    public void eventBus(ClickEvent.ClickNextEvent event) {
        correctCameraOrientation(saveBitmap(binding.mPreview.getCroppedImage(),
                FileUtil.getNewFilePath()), getContext());
        mSession.setFileToUpload(saveBitmap(binding.mPreview.getCroppedImage(),
                FileUtil.getNewFilePath()));
        listener.openEditor();
    }


    public List<Uri> getImagesFromGallary() {

        List<Uri> images = new ArrayList<Uri>();


        Cursor imageCursor = null;
        try {
            final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.ImageColumns.ORIENTATION};
            final String orderBy = MediaStore.Images.Media.DATE_ADDED + " DESC";


            imageCursor = BaseApplication.getContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy);
            while (imageCursor.moveToNext()) {
                Uri uri = Uri.parse(imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.DATA)));
                images.add(uri);


            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (imageCursor != null && !imageCursor.isClosed()) {
                imageCursor.close();
            }
        }


        return images;

    }


    private void displayPreview(Uri uri) {

//        Uri uri = galleryAdapter.getItem(position);

        Glide.with(getContext())
                .load(uri.toString())
                .thumbnail(0.1f)
                .dontAnimate()
                .dontTransform()
                .override(600, 600)
                .into(binding.mPreview);
    }

    @Override
    public void onClickMediaItem(Uri uri) {
        displayPreview(uri);
        binding.mAppBarContainer.setExpanded(true, true);
    }
}
