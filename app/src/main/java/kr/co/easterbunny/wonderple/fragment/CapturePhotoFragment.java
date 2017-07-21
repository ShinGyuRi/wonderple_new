package kr.co.easterbunny.wonderple.fragment;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.RelativeLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import kr.co.easterbunny.wonderple.R;
import kr.co.easterbunny.wonderple.databinding.FragmentCapturePhotoBinding;
import kr.co.easterbunny.wonderple.library.ParentFragment;
import kr.co.easterbunny.wonderple.library.util.FileUtil;
import kr.co.easterbunny.wonderple.library.util.JSLog;
import kr.co.easterbunny.wonderple.listener.CapturePhotoFragmentListener;
import kr.co.easterbunny.wonderple.model.Session;
import kr.co.easterbunny.wonderple.view.cameraview.CameraView;
import kr.co.easterbunny.wonderple.view.cameraview.base.AspectRatio;

/**
 * A simple {@link Fragment} subclass.
 */
public class CapturePhotoFragment extends ParentFragment {

    private FragmentCapturePhotoBinding binding;

    private static final Interpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();
    private static final Interpolator DECELERATE_INTERPOLATOR = new DecelerateInterpolator();

    private CapturePhotoFragmentListener listener;
    private Handler mBackgroundHandler;
    private Session mSession = Session.getInstance();

    private int mCurrentFlash;


    private static final int[] FLASH_OPTIONS = {
            CameraView.FLASH_AUTO,
            CameraView.FLASH_OFF,
            CameraView.FLASH_ON,
    };

    private static final int[] FLASH_ICONS = {
            R.drawable.ic_flash_auto,
            R.drawable.ic_flash_off,
            R.drawable.ic_flash_on,
    };


    public CapturePhotoFragment() {
        // Required empty public constructor
    }

    public static CapturePhotoFragment newInstance() {
        CapturePhotoFragment frag = new CapturePhotoFragment();
        return frag;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (CapturePhotoFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement CapturePhotoFragmentListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_capture_photo, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding = FragmentCapturePhotoBinding.bind(getView());

        initViews();

        binding.setCapturePhoto(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.mCameraPhotoView.start();
    }

    @Override
    public void onPause() {
        binding.mCameraPhotoView.stop();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mBackgroundHandler != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                mBackgroundHandler.getLooper().quitSafely();
            } else {
                mBackgroundHandler.getLooper().quit();
            }
            mBackgroundHandler = null;
        }
    }

    public void capturePhoto(View v) {
        switch (v.getId()) {
            case R.id.mBtnTakePhoto:
                onTakePhotoClick();
                break;
            case R.id.mSwitchCamera:
                onSwitchCamera();
                break;
            case R.id.mFlashPhoto:
                onChangeFlashState();
                break;
        }
    }

    public void onTakePhotoClick() {
        binding.mCameraPhotoView.takePicture();
        animateShutter();
    }

    public void onSwitchCamera() {
        if (binding.mCameraPhotoView != null) {
            int facing = binding.mCameraPhotoView.getFacing();
            binding.mCameraPhotoView.setFacing(facing == CameraView.FACING_FRONT ?
                    CameraView.FACING_BACK : CameraView.FACING_FRONT);
        }
    }

    public void onChangeFlashState() {
        if (binding.mCameraPhotoView != null) {
            mCurrentFlash = (mCurrentFlash + 1) % FLASH_OPTIONS.length;
            binding.mFlashPhoto.setImageResource(FLASH_ICONS[mCurrentFlash]);
            binding.mCameraPhotoView.setFlash(FLASH_OPTIONS[mCurrentFlash]);
        }
    }


    private void animateShutter() {
        binding.mShutter.setVisibility(View.VISIBLE);
        binding.mShutter.setAlpha(0.f);

        ObjectAnimator alphaInAnim = ObjectAnimator.ofFloat(binding.mShutter, "alpha", 0f, 0.8f);
        alphaInAnim.setDuration(100);
        alphaInAnim.setStartDelay(100);
        alphaInAnim.setInterpolator(ACCELERATE_INTERPOLATOR);

        ObjectAnimator alphaOutAnim = ObjectAnimator.ofFloat(binding.mShutter, "alpha", 0.8f, 0f);
        alphaOutAnim.setDuration(200);
        alphaOutAnim.setInterpolator(DECELERATE_INTERPOLATOR);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(alphaInAnim, alphaOutAnim);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                binding.mShutter.setVisibility(View.GONE);
            }
        });
        animatorSet.start();
    }


    private void initViews() {
        if (binding.mCameraPhotoView != null) {
            binding.mCameraPhotoView.addCallback(mCallback);
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) binding.mCameraPhotoView.getLayoutParams();
            lp.height = getResources().getDisplayMetrics().widthPixels;
            binding.mCameraPhotoView.setLayoutParams(lp);
            binding.mCameraPhotoView.setAspectRatio(new AspectRatio(1,1));
        }
    }

    private Handler getBackgroundHandler() {
        if (mBackgroundHandler == null) {
            HandlerThread thread = new HandlerThread("background");
            thread.start();
            mBackgroundHandler = new Handler(thread.getLooper());
        }
        return mBackgroundHandler;
    }

    private CameraView.Callback mCallback
            = new CameraView.Callback() {

        @Override
        public void onCameraOpened(CameraView cameraView) {
        }

        @Override
        public void onCameraClosed(CameraView cameraView) {
        }

        @Override
        public void onPictureTaken(CameraView cameraView, final byte[] data) {
            getBackgroundHandler().post(new Runnable() {
                @Override
                public void run() {
                    File dirDest = FileUtil.getLocalDir();
                    File file;
                    if (dirDest.exists()) {
                        file = new File(FileUtil.getNewFilePath());
                    } else {
                        if (dirDest.mkdir()) {
                            file = new File(FileUtil.getNewFilePath());
                        } else {
                            file = null;
                        }
                    }
                    OutputStream os = null;
                    if (file != null) {
                        try {
                            os = new FileOutputStream(file);
                            os.write(data);
                            os.close();
                        } catch (IOException e) {
                            // Cannot write
                            JSLog.W("Cannot write to "+file, new Throwable());
                        } finally {
                            if (os != null) {
                                try {
                                    os.close();
                                } catch (IOException e) {
                                    // Ignore
                                }
                            }
                        }
                        correctCameraOrientation(file, getContext());
                        mSession.setFileToUpload(file);
                        listener.openEditor();
                    }
                }
            });
        }
    };


    private int IMAGE_SIZE = 500;

    public void correctCameraOrientation(File imgFile, Context context) {
        Bitmap bitmap = FileUtil.loadImageWithSampleSize(imgFile, IMAGE_SIZE);
        try {
            ExifInterface exif = new ExifInterface(imgFile.getAbsolutePath());
            int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int exifRotateDegree = exifOrientationToDegrees(exifOrientation);
            bitmap = FileUtil.rotateImage(bitmap, exifRotateDegree);
            FileUtil.saveBitmapToFile(bitmap, context);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }
}
