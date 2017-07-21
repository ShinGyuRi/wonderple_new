package kr.co.easterbunny.wonderple.library;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.google.gson.JsonObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kr.co.easterbunny.wonderple.R;
import kr.co.easterbunny.wonderple.activity.LoginActivity;
import kr.co.easterbunny.wonderple.activity.MainActivity;
import kr.co.easterbunny.wonderple.activity.PostDetailActivity;
import kr.co.easterbunny.wonderple.activity.PouchActivity;
import kr.co.easterbunny.wonderple.activity.SplashActivity;
import kr.co.easterbunny.wonderple.activity.VisitPageActivity;
import kr.co.easterbunny.wonderple.library.dialog.CommonLoadingDialog;
import kr.co.easterbunny.wonderple.library.listener.TakePictureListener;
import kr.co.easterbunny.wonderple.library.util.Definitions;
import kr.co.easterbunny.wonderple.library.util.Definitions.ACTIVITY_REQUEST_CODE;
import kr.co.easterbunny.wonderple.library.util.FileUtil;
import kr.co.easterbunny.wonderple.library.util.JSLog;
import kr.co.easterbunny.wonderple.library.util.NetworkUtil;
import kr.co.easterbunny.wonderple.library.util.PrefUtil;
import kr.co.easterbunny.wonderple.library.util.TextUtil;
import kr.co.easterbunny.wonderple.library.util.Toaster;
import kr.co.easterbunny.wonderple.model.SignInResult;
import kr.co.easterbunny.wonderple.model.User;
import kr.co.easterbunny.wonderple.view.CustomTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Gyul on 2016-06-16.
 */
public class ParentActivity extends AppCompatActivity {

    private int IMAGE_SIZE = 500;

    private InputMethodManager inputManager;
    private CommonLoadingDialog loading;

    public static ArrayList<Activity> activityList;

    public void takePicture(Bitmap bm) {
    }

    public void onUIRefresh() {
    }

    private TakePictureListener takePickerListener;

    public void setTakePictureListener(TakePictureListener listener) {
        this.takePickerListener = listener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BaseApplication.setCurrentActivity(this);
        if (activityList == null) {
            activityList = new ArrayList<Activity>();
        }
        if(this instanceof SplashActivity == false)
            activityList.add(this);

        if (savedInstanceState != null) {
            activityList = null;
            Intent i = new Intent(BaseApplication.getContext(), SplashActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        }


    }

    public void showLoading() {
        try {
            if (loading == null)
                return;
            loading.show();
        } catch (Exception e) {
        }
    }

    public void hideLoading() {
        try {
            if (loading == null)
                return;
            loading.dismiss();
        } catch (Exception e) {
        }
    }

    public void switchContent(Fragment fragment, int contentId, boolean isHistory, boolean isAni) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (isHistory)
            ft.addToBackStack(null);
        if (isAni) {
            ft.setCustomAnimations(R.anim.left, R.anim.left2, R.anim.right2, R.anim.right);
        }
        ft.replace(contentId, fragment).commit();
    }


    public boolean permissionCheck(String[] reqPermission, int reqCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean isDenied = false;
            for (String req : reqPermission) {
                if (ActivityCompat.checkSelfPermission(this, req) == PackageManager.PERMISSION_DENIED) {
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
        switch (requestCode) {
            case ACTIVITY_REQUEST_CODE.PERMISSION_ABOUT_CAMERA:
                for (int result : grantResults) {
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        showPermissionDialog(R.string.str_permission_message_camera);
                        return;
                    }
                }
                goCamera(0);
                break;
            case ACTIVITY_REQUEST_CODE.PERMISSION_ABOUT_GALLERY:
                goGallery(0);
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    public void showPermissionDialog(int messageRes) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage(messageRes);
        alertDialog.setNegativeButton(R.string.str_close, null);
        alertDialog.setPositiveButton(R.string.str_setting, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            .setData(Uri.parse("package:" + getPackageName()));
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();

                    Intent intent = new Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
                    startActivity(intent);
                }
            }
        });
        alertDialog.show();
    }

    /*******************************
     * 사진관련
     *******************************/

//    public void gotoCameraActivity(int typeFramActivity) {
//        Intent i = new Intent(this, CameraActivity.class);
//        i.putExtra(Definitions.INTENT_KEY.FROM_ACTIVITY, typeFramActivity);
//        Util.moveActivity(this, i, -1, -1, false, false, ACTIVITY_REQUEST_CODE.CAMERA_ACT);
//    }

    public int cropRatio;

    public void goGallery(int cropRatio) {
        String[] permission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        boolean isValid = permissionCheck(permission, ACTIVITY_REQUEST_CODE.PERMISSION_ABOUT_GALLERY);
        if (isValid == false) return;
        this.cropRatio = cropRatio;
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType(MediaStore.Images.Media.CONTENT_TYPE);
        i.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, ACTIVITY_REQUEST_CODE.PICK_GALLERY);
    }

    public void goCamera(int cropRatio) {
        String[] permission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        boolean isValid = permissionCheck(permission, ACTIVITY_REQUEST_CODE.PERMISSION_ABOUT_CAMERA);
        if (isValid == false) return;
        this.cropRatio = cropRatio;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(FileUtil.getTempImageFile(this)));
        intent.putExtra("return-data", true);
        startActivityForResult(intent, ACTIVITY_REQUEST_CODE.PICK_CAMERA);
    }

    private void takePictureFromGallery()
    {
        startActivityForResult(
                Intent.createChooser(
                        new Intent(Intent.ACTION_GET_CONTENT)
                                .setType("image/*"), "Choose an image"),
                ACTIVITY_REQUEST_CODE.PICK_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ACTIVITY_REQUEST_CODE.PICK_GALLERY && resultCode == Activity.RESULT_OK) {
            if (data == null) return;
            Uri uri = data.getData();
            FileUtil.copyUriToFile(this, uri, FileUtil.getTempImageFile(this));
            correctCameraOrientation(FileUtil.getTempImageFile(this));
            if (cropRatio > 0) jyCrop();
            else doFinalProcess();
        } else if (requestCode == ACTIVITY_REQUEST_CODE.PICK_CAMERA && resultCode == Activity.RESULT_OK) {
            correctCameraOrientation(FileUtil.getTempImageFile(this));
            if (cropRatio > 0) jyCrop();
            else doFinalProcess();
        } else if (requestCode == ACTIVITY_REQUEST_CODE.PICK_CROP && resultCode == Activity.RESULT_OK) {
            doFinalProcess();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void doFinalProcess() {
        Bitmap bm = BitmapFactory.decodeFile(FileUtil.getTempImageFile(this).getAbsolutePath());
        if (takePickerListener != null) {
            takePickerListener.takePicture(bm);
        } else {
            takePicture(bm);
        }
    }

    public void jyCrop() {
    }

    public void correctCameraOrientation(File imgFile) {
        Bitmap bitmap = FileUtil.loadImageWithSampleSize(imgFile, IMAGE_SIZE);
        try {
            ExifInterface exif = new ExifInterface(imgFile.getAbsolutePath());
            int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int exifRotateDegree = exifOrientationToDegrees(exifOrientation);
            bitmap = FileUtil.rotateImage(bitmap, exifRotateDegree);
            FileUtil.saveBitmapToFile(bitmap, this);
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

    public String getAddress(Context context, double lat, double lng){
        String address = null;

        //위치정보를 활용하기 위한 구글 API 객체
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

        //주소 목록을 담기 위한 HashMap
        List<Address> list = null;

        try{
            list = geocoder.getFromLocation(lat, lng, 1);
        } catch(Exception e){
            e.printStackTrace();
        }

        if(list == null){
            Log.e("getAddress", "주소 데이터 얻기 실패");
            return null;
        }

        if(list.size() > 0){
            Address addr = list.get(0);
            address =
//                    addr.getCountryName() + " "
                    addr.getAdminArea() + " "
                            + addr.getLocality() + " "
                            + addr.getThoroughfare();
//                    + addr.getFeatureName();
        }

        return address;



    }

    public void showImageAlert() {
        String[] imageChoice = new String[2];
        imageChoice[0] = getString(R.string.str_take_picture_from_camera);
        imageChoice[1] = getString(R.string.str_take_picture_from_gallery);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(imageChoice, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (which == 0) {
                    goCamera(0);
                } else if (which == 1) {
                    goGallery(0);
                }
            }
        });
        builder.show();
    }

    public boolean checkGpsService() {

        String gps = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        Log.d(gps, "aaaa");

        if (!(gps.matches(".*gps.*") && gps.matches(".*network.*"))) {

            // GPS OFF 일때 Dialog 표시
            AlertDialog.Builder gsDialog = new AlertDialog.Builder(this);
            gsDialog.setTitle(getString(R.string.str_check_gps_title));
            gsDialog.setMessage(getString(R.string.str_check_gps_message));
            gsDialog.setPositiveButton(getString(R.string.str_ok), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // GPS설정 화면으로 이동
                    Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    startActivity(intent);
                }
            })
                    .setNegativeButton(getString(R.string.str_cancel), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    }).create().show();
            return false;

        } else {
            return true;
        }
    }


    public void snsLoginCheck(Activity activity, String name, String type, String idnum, String picture) {


        Call<JsonObject> jsonObjectCall = NetworkUtil.getInstace().loginCheck(name, type, idnum);
        jsonObjectCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject jsonObject = response.body();
                String message = jsonObject.get("message").toString().replace("\"", "");

                if ("account does not exist".equals(message)) {
                    JSLog.D("message: "+message, new Throwable());

                    if (getString(R.string.activity_splash).equals(activity.getClass().getSimpleName()))
                        moveLoginActivity(activity);
                    else if (getString(R.string.activity_login).equals(activity.getClass().getSimpleName()))
                        requestSNSLogin(activity, name, type, picture, idnum);

                } else if ("login successfully checked".equals(message)) {
                    JSLog.D("message: "+message, new Throwable());
                    moveMainActivity(activity);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toaster.shortToast("네트워크 상태를 확인해주세요");
            }
        });
    }

    public void emailLoginCheck(Activity activity, String email, String type, String password) {

        Call<JsonObject> jsonObjectCall = NetworkUtil.getInstace().loginCheckEmail(email, type, password);
        jsonObjectCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject jsonObject = response.body();
                String message = jsonObject.get("message").toString().replace("\"", "");

                if ("login successfully checked".equals(message)) {
                    JSLog.D("message: " + message, new Throwable());
                    moveMainActivity(activity);
                } else {
                    JSLog.D("message: "+ message, new Throwable());
                    moveLoginActivity(activity);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toaster.shortToast("네트워크 상태를 확인해주세요");
                JSLog.E(t.getLocalizedMessage(), t);
                JSLog.E(t.getMessage(), t);
            }
        });
    }


    public void requestSNSLogin(Activity activity, String name, String type, String picture, String referenceId) {

        SignInResult signInResult = new SignInResult();
        User user = new User();

        Call<JsonObject> jsonObjectCall = NetworkUtil.getInstace().signUp(name, type, picture, referenceId);
        jsonObjectCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject jsonObject = response.body();
                String result = jsonObject.get("result").toString().replace("\"", "");
                String message = jsonObject.get("message").toString().replace("\"", "");

                signInResult.setResult(result);
                signInResult.setMessage(message);

                if ("successfully added sns account".equals(message)) {

                    String uid = jsonObject.get("uid").toString().replace("\"", "");

                    user.setEmail(null);
                    user.setImage(picture);
                    user.setSnsid(referenceId);
                    user.setSnstype(type);
                    user.setUdid(uid);
                    user.setUsername(name);
                    user.setPassword(null);
                    signInResult.setUser(user);

                    WonderpleLib.getInstance().func01_saveUserDataToFile(activity, signInResult);

                    moveMainActivity(activity);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }



    public void moveLoginActivity(Activity activity) {

        Intent intent = new Intent(activity, LoginActivity.class);
        startActivity(intent);
        activity.finish();

    }



    public void moveMainActivity(Activity activity) {

        Intent intent = new Intent(activity, MainActivity.class);
        startActivity(intent);
        activity.finish();

    }

    public void movePageActivity(Context context, String uid, String auid) {

        Intent intent = new Intent(context, VisitPageActivity.class);
        intent.putExtra("uid", uid);
        intent.putExtra("auid", auid);
        context.startActivity(intent);

    }

    public void movePostDetailActivity(Context context, String uid, String auid, String iid, String imgUrl) {
        Intent intent = new Intent(context, PostDetailActivity.class);

        intent.putExtra("uid", uid);
        intent.putExtra("auid", auid);
        intent.putExtra("iid", iid);
        intent.putExtra("imgUrl", imgUrl);

        startActivity(intent);
    }

    public void movePouchActivity(Context context, String uid, String username, String profileImage) {
        Intent intent = new Intent(context, PouchActivity.class);

        intent.putExtra("uid", uid);
        intent.putExtra("username", username);
        intent.putExtra("profileImage", profileImage);

        startActivity(intent);
    }

    public boolean checkValidation(EditText etEmail, EditText etPassword) {
        String inputEmail = etEmail.getText().toString();
        String inputPW = etPassword.getText().toString();
        if (!TextUtil.isNull(inputEmail) && !TextUtil.isNull(inputPW)) {
            return true;
        }
        return false;
    }

    public boolean checkPasswordValidation(String inputPW, CustomTextView tvWarning) {
        if (TextUtils.isEmpty(inputPW)) {
            tvWarning.setVisibility(View.VISIBLE);
            tvWarning.setText(R.string.str_warning_pw_min_8);
            return false;
        }
        if (inputPW.length() < 8 || inputPW.length() > 15) {    //|| inputPW.length()>16{
            tvWarning.setVisibility(View.VISIBLE);
            tvWarning.setText(R.string.str_warning_pw_min_8);
            return false;
        }
        if (inputPW.contains(" ")) {
            tvWarning.setVisibility(View.VISIBLE);
            tvWarning.setText(R.string.str_warning_pw_rule_01);
            return false;
        }
        if (TextUtil.isPassworkdCheck(inputPW) == false) {
            tvWarning.setVisibility(View.VISIBLE);
            tvWarning.setText(R.string.str_warning_pw_rule_02);
            return false;
        }
        return true;
    }

    public ArrayList<int[]> getSpans(String body, char prefix) {
        ArrayList<int[]> spans = new ArrayList<>();

        Pattern pattern = Pattern.compile(prefix + "\\w+");
        Matcher matcher = pattern.matcher(body);

        while (matcher.find()) {
            int[] currentSpan = new int[2];
            currentSpan[0] = matcher.start();
            currentSpan[1] = matcher.end();
            spans.add(currentSpan);
        }

        return spans;
    }




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
//                    ((ParentAct) getActivity()).goCamera(0);
//                } else if (which == 1) {
//                    ((ParentAct) getActivity()).goGallery(0);
//                }
//            }
//        });
//        builder.show();
//    }
}
