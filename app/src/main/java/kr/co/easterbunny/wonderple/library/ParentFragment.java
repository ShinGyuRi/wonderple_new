package kr.co.easterbunny.wonderple.library;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.File;
import java.io.IOException;

import kr.co.easterbunny.wonderple.R;
import kr.co.easterbunny.wonderple.library.listener.TakePictureListener;
import kr.co.easterbunny.wonderple.library.util.FileUtil;

/**
 * Created by Gyul on 2016-06-16.
 */
public abstract class ParentFragment extends Fragment {
    private InputMethodManager inputManager;

    public Fragment homeFragment;
    public Fragment couponFragment;

    private int IMAGE_SIZE = 800;

    public void onUIRefresh(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

//    private TakePictureListener takePickerListener;
//
//    public void setTakePictureListener(TakePictureListener listener) {
//        this.takePickerListener = listener;
//    }
//
//    public void showImageAlert() {
//        String[] imageChoice = new String[2];
//        imageChoice[0] = getString(R.string.str_take_picture_from_camera);
//        imageChoice[1] = getString(R.string.str_take_picture_from_gallery);
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setItems(imageChoice, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                if (which == 0) {
//                    ((ParentActivity) getActivity()).goCamera(0);
//                } else if (which == 1) {
//                    ((ParentActivity) getActivity()).goGallery(0);
//                }
//            }
//        });
//        builder.show();
//    }

//    public void showPickerDialog() {
//        DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
//            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                String date = getString(R.string.str_birthday_format, year, (monthOfYear + 1), dayOfMonth);
//                txtBirthday.setText(date);
//
//                Calendar calendar = Calendar.getInstance();
//                calendar.set(year, monthOfYear, dayOfMonth);
//                SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
//                inputBirthday = format.format(calendar.getTime());
//
//            }
//        };
//
//        Calendar cal = Calendar.getInstance();
//        cal.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR) - 14);
//        cal.set(Calendar.MONTH, Calendar.JANUARY);
//        cal.set(Calendar.DAY_OF_MONTH, 1);
//        DatePickerDialog alert = new DatePickerDialog(getActivity(), mDateSetListener, 1980, 0, 1);
//        alert.getDatePicker().setMaxDate(cal.getTime().getTime());
//        alert.show();
//    }

//    public boolean checkValidation() {
//        inputName = editName.getText().toString();
//        inputPW = editPassword.getText().toString();
//        inputIntroduce = editIntroduce.getText().toString();
//        inputPhone = editPhone.getText().toString();
//
//        if (!TextUtil.isNull(inputIntroduce)) {
//            if (inputIntroduce.length() > 48) {
//                txtIntroduceWarning.setVisibility(View.VISIBLE);
//                editIntroduce.removeTextChangedListener(this);
//                editIntroduce.setText(inputIntroduce.substring(0, inputIntroduce.length() - 1));
//                editIntroduce.setSelection(editIntroduce.getText().toString().length());
//                editIntroduce.addTextChangedListener(this);
//            } else {
//                txtIntroduceWarning.setVisibility(View.GONE);
//            }
//        } else {
//            txtIntroduceWarning.setVisibility(View.GONE);
//        }
//
//        if (!TextUtil.isNull(inputName)
//                && !TextUtil.isNull(inputPW)
//                && !TextUtil.isNull(emailAddr)
//                && !TextUtil.isNull(inputIntroduce)
//                && !TextUtil.isNull(inputPhone)
//                && !TextUtil.isNull(inputCountry)
//                && !TextUtil.isNull(inputBirthday)
//                && cbUsePolicy.isChecked() == true
//                && cbPrivacyPolicy.isChecked() == true
//                ) {
//            return true;
//        }
//
//        return false;
//    }

//    public boolean checkPasswordValidation(String inputPW) {
//        if (TextUtils.isEmpty(inputPW)) {
//            Toast.makeText(getContext(), R.string.str_warning_pw_min_8, Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        if (inputPW.length() < 8) {    //|| inputPW.length()>16{
//            Toast.makeText(getContext(), R.string.str_warning_pw_min_8, Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        if (inputPW.contains(" ")) {
//            Toast.makeText(getContext(), R.string.str_warning_pw_rule_01, Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        if (TextUtil.isPassworkdCheck(inputPW) == false) {
//            Toast.makeText(getContext(), R.string.str_warning_pw_rule_02, Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        return true;
//    }

    public void setCutOffBackgroundTouch(View view) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                return true;
            }
        });
    }


    public void showLoading() {
        if (getActivity() != null && getActivity() instanceof ParentActivity)
            ((ParentActivity) getActivity()).showLoading();
    }

    public void hideLoading() {
        if (getActivity() != null && getActivity() instanceof ParentActivity)
            ((ParentActivity) getActivity()).hideLoading();
    }



    public boolean permissionCheck(String[] reqPermission, int reqCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean isDenied = false;
            for (String req : reqPermission) {
                if (ActivityCompat.checkSelfPermission(getContext(), req) == PackageManager.PERMISSION_DENIED) {
                    isDenied = true;
                    break;
                }
            }
            if (isDenied) {
                requestPermissions(reqPermission, reqCode);
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//		for(String permission: permissions)
//			JYLog.D(requestCode +" permission: "+ permission, new Throwable());
//		for(int loginResult : grantResults)
//			JYLog.D(requestCode +" loginResult: "+ loginResult, new Throwable());
//		switch (requestCode){
//			case Definitions.ACTIVITY_REQUEST_CODE.PERMISSION_ABOUT_CAMERA:
//				for(int loginResult : grantResults){
//					if(loginResult != PackageManager.PERMISSION_GRANTED){
//						showPermissionDialog(R.string.str_permission_message_camera);
//						return;
//					}
//				}
//				goCamera(0);
//				break;
//			case Definitions.ACTIVITY_REQUEST_CODE.PERMISSION_ABOUT_GALLERY:
//				goGallery(0);
//				break;
//		}
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


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

    public void switchContent(Fragment fragment, int contentId) {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.replace(contentId, fragment).commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
