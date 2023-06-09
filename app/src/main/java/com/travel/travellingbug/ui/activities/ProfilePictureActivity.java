package com.travel.travellingbug.ui.activities;

import static com.travel.travellingbug.ClassLuxApp.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;
import com.travel.travellingbug.ClassLuxApp;
import com.travel.travellingbug.R;
import com.travel.travellingbug.helper.AppHelper;
import com.travel.travellingbug.helper.ConnectionHelper;
import com.travel.travellingbug.helper.CustomDialog;
import com.travel.travellingbug.helper.SharedHelper;
import com.travel.travellingbug.helper.URLHelper;
import com.travel.travellingbug.helper.VolleyMultipartRequest;
import com.travel.travellingbug.utills.Utilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class ProfilePictureActivity extends AppCompatActivity {


    ImageView profile_Image, backArrow;
    TextView updateProfile;
    Button btnTakePicture;

    public Context context = ProfilePictureActivity.this;

    public Activity activity = ProfilePictureActivity.this;

    Utilities utils = new Utilities();

    Uri uri;

    CustomDialog customDialog;
    ConnectionHelper helper;
    Boolean isInternet;

    Boolean isImageChanged = false;

    private static final int SELECT_PHOTO = 100;

    public static int deviceHeight;
    public static int deviceWidth;


    private static final String TAG = "EditProfile";


    private static Bitmap getBitmapFromUri(@NonNull Context context, @NonNull Uri uri) throws IOException {
        Log.e(TAG, "getBitmapFromUri: Resize uri" + uri);
        ParcelFileDescriptor parcelFileDescriptor =
                context.getContentResolver().openFileDescriptor(uri, "r");
        assert parcelFileDescriptor != null;
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        Log.e(TAG, "getBitmapFromUri: Height" + deviceHeight);
        Log.e(TAG, "getBitmapFromUri: width" + deviceWidth);
        int maxSize = Math.min(deviceHeight, deviceWidth);
        if (image != null) {
            Log.e(TAG, "getBitmapFromUri: Width" + image.getWidth());
            Log.e(TAG, "getBitmapFromUri: Height" + image.getHeight());
            int inWidth = image.getWidth();
            int inHeight = image.getHeight();
            int outWidth;
            int outHeight;
            if (inWidth > inHeight) {
                outWidth = maxSize;
                outHeight = (inHeight * maxSize) / inWidth;
            } else {
                outHeight = maxSize;
                outWidth = (inWidth * maxSize) / inHeight;
            }
            return Bitmap.createScaledBitmap(image, outWidth, outHeight, false);
        } else {
            Toast.makeText(context, context.getString(R.string.valid_image), Toast.LENGTH_SHORT).show();
            return null;
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SharedHelper.getKey(this, "selectedlanguage").contains("ar")) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
        setContentView(R.layout.activity_profile_picture);

        initComponent();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        deviceHeight = displayMetrics.heightPixels;
        deviceWidth = displayMetrics.widthPixels;

        backArrow.setOnClickListener(view -> {
//                GoToMainActivity();
            onBackPressed();
        });

        onClickHandlerOnComponent();

        getProfile();
    }

    private void onClickHandlerOnComponent() {
        btnTakePicture.setOnClickListener(view -> {

            if (checkStoragePermission()) {
                goToImageIntent();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
                }
            } else {
                goToImageIntent();

            }

        });

        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInternet) {
                    updateProfile();
                } else {
                    displayMessage(getString(R.string.something_went_wrong_net));
                }
            }
        });
    }

    private void initComponent() {
        Utilities.hideKeyboard(activity);
        profile_Image = findViewById(R.id.profile_image);
        btnTakePicture = findViewById(R.id.btnTakePicture);
        updateProfile = findViewById(R.id.choosePicturetv);
        backArrow = findViewById(R.id.backArrow);

        helper = new ConnectionHelper(context);
        isInternet = helper.isConnectingToInternet();
        setProviderDetails();

    }


    public void getProfile() {

        if (isInternet) {
            JSONObject object = new JSONObject();
            JsonObjectRequest jsonObjectRequest = new
                    JsonObjectRequest(Request.Method.GET, URLHelper.USER_PROFILE_API,
                            object, response -> {
                        Log.v("GetProfile", response.toString());
                        SharedHelper.putKey(context, "id", response.optString("id"));
                        SharedHelper.putKey(context, "first_name", response.optString("first_name"));
                        SharedHelper.putKey(context, "last_name", response.optString("last_name"));
                        SharedHelper.putKey(context, "email", response.optString("email"));
                        SharedHelper.putKey(context, "picture", URLHelper.BASE + "storage/app/public/" + response.optString("picture"));
                        SharedHelper.putKey(context, "gender", response.optString("gender"));
                        SharedHelper.putKey(context, "sos", response.optString("sos"));
                        SharedHelper.putKey(context, "mobile", response.optString("mobile"));
                        SharedHelper.putKey(context, "refer_code", response.optString("refer_code"));
                        SharedHelper.putKey(context, "wallet_balance", response.optString("wallet_balance"));
                        SharedHelper.putKey(context, "payment_mode", response.optString("payment_mode"));
                        SharedHelper.putKey(context, "currency", response.optString("currency"));
                        //                    SharedHelper.putKey(context, "currency", response.optString("payment_mode"));
                        SharedHelper.putKey(context, "loggedIn", getString(R.string.True));
                        if (response.optString("avatar").startsWith("http"))
                            SharedHelper.putKey(context, "picture", response.optString("avatar"));
                        else
                            SharedHelper.putKey(context, "picture", URLHelper.BASE + "storage/app/public/" + response.optString("avatar"));

                        if (response.optJSONObject("service") != null) {
                            try {
                                JSONObject service = response.optJSONObject("service");
                                if (service.optJSONObject("service_type") != null) {
                                    JSONObject serviceType = service.optJSONObject("service_type");
                                    SharedHelper.putKey(context, "service", serviceType.optString("name"));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        setProviderDetails();


                    }, error -> {
                        displayMessage(getString(R.string.something_went_wrong));
                    }) {
                        @Override
                        public Map<String, String> getHeaders() {
                            HashMap<String, String> headers = new HashMap<String, String>();
                            headers.put("X-Requested-With", "XMLHttpRequest");
                            Log.e(TAG, "getHeaders: Token" + SharedHelper.getKey(context, "access_token") + SharedHelper.getKey(context, "token_type"));
                            headers.put("Authorization", "" + "Bearer" + " " + SharedHelper.getKey(context, "access_token"));
                            return headers;
                        }
                    };

            ClassLuxApp.getInstance().addToRequestQueue(jsonObjectRequest);
        } else {
            displayMessage(getString(R.string.something_went_wrong_net));
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private boolean checkStoragePermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100)
            for (int grantResult : grantResults)
                if (grantResult == PackageManager.PERMISSION_GRANTED)
                    goToImageIntent();
    }

    public void goToImageIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PHOTO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK && data != null && data.getData() != null) {

            uri = data.getData();
            try {
                isImageChanged = true;
                //bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
                Bitmap resizeImg = getBitmapFromUri(this, uri);
                if (resizeImg != null) {
                    Bitmap reRotateImg = AppHelper.modifyOrientation(resizeImg, AppHelper.getPath(this, uri));
                    profile_Image.setImageBitmap(reRotateImg);
                    updateProfileWithImage();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        if (requestCode == 1) {
//            if (resultCode == Activity.RESULT_OK) {
//                String result = data.getStringExtra("result");
//                if (SharedHelper.getKey(EditProfile.this, "mobile") != null) {
//                    mobile_no.setText(SharedHelper.getKey(EditProfile.this, "mobile"));
//                    txtHeaderMob.setText(SharedHelper.getKey(EditProfile.this, "mobile"));
//                }
//                if (SharedHelper.getKey(EditProfile.this, "email") != null) {
//                    email.setText(SharedHelper.getKey(EditProfile.this, "email"));
//                }
//                if (SharedHelper.getKey(EditProfile.this, "first_name") != null) {
//                    first_name.setText(SharedHelper.getKey(EditProfile.this, "first_name"));
//                    txtHeaderName.setText(SharedHelper.getKey(EditProfile.this, "first_name"));
//                }
//            }
//
//        }
    }

    public void updateProfile() {
        if (isImageChanged) {
            updateProfileWithImage();
        } else {
            updateProfileWithoutImage();
        }
    }

    private void updateProfileWithoutImage() {
//        customDialog = new CustomDialog(context);
//        customDialog.setCancelable(false);
//        customDialog.show();
//        VolleyMultipartRequest volleyMultipartRequest = new
//                VolleyMultipartRequest(Request.Method.POST, URLHelper.USER_PROFILE_API,
//                        response -> {
//                            customDialog.dismiss();
//                            String res = new String(response.data);
//                            try {
//                                JSONObject jsonObject = new JSONObject(res);
//                                SharedHelper.putKey(context, "id", jsonObject.optString("id"));
//                                SharedHelper.putKey(context, "first_name", jsonObject.optString("first_name"));
//                                SharedHelper.putKey(context, "last_name", jsonObject.optString("last_name"));
//                                SharedHelper.putKey(context, "email", jsonObject.optString("email"));
//                                if (jsonObject.optString("avatar").equals("") || jsonObject.optString("avatar") == null) {
//                                    SharedHelper.putKey(context, "picture", "");
//                                } else {
//                                    if (jsonObject.optString("avatar").startsWith("http"))
//                                        SharedHelper.putKey(context, "picture", jsonObject.optString("avatar"));
//                                    else
//                                        SharedHelper.putKey(context, "picture", URLHelper.BASE + "storage/app/public/" + jsonObject.optString("avatar"));
//                                }
//                                SharedHelper.putKey(context, "sos", jsonObject.optString("sos"));
//                                SharedHelper.putKey(context, "gender", jsonObject.optString("gender"));
//                                SharedHelper.putKey(context, "mobile", jsonObject.optString("mobile"));
//                                //                        SharedHelper.putKey(context, "wallet_balance", jsonObject.optString("wallet_balance"));
//                                //                        SharedHelper.putKey(context, "payment_mode", jsonObject.optString("payment_mode"));
//                                GoToMainActivity();
//                                Toast.makeText(ProfilePictureActivity.this, getString(R.string.update_success), Toast.LENGTH_SHORT).show();
//                                //displayMessage(getString(R.string.update_success));
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                                displayMessage(getString(R.string.something_went_wrong));
//                            }
//
//
//                        }, error -> {
//                    if ((customDialog != null) && customDialog.isShowing())
//                        customDialog.dismiss();
//                    displayMessage(getString(R.string.something_went_wrong));
//                }) {
//                    @Override
//                    public Map<String, String> getParams() {
//                        Map<String, String> params = new HashMap<>();
//                        params.put("first_name", first_name.getText().toString());
//                        params.put("last_name", "");
//                        params.put("email", email.getText().toString());
//                        params.put("mobile", mobile_no.getText().toString());
//                        params.put("avatar", "");
//                        return params;
//                    }
//
//                    @Override
//                    public Map<String, String> getHeaders() {
//                        HashMap<String, String> headers = new HashMap<String, String>();
//                        headers.put("X-Requested-With", "XMLHttpRequest");
//                        headers.put("Authorization", "Bearer " + SharedHelper.getKey(context, "access_token"));
//                        return headers;
//                    }
//                };
//        ClassLuxApp.getInstance().addToRequestQueue(volleyMultipartRequest);
    }

    private void updateProfileWithImage() {

        ProgressDialog progressDialog = new ProgressDialog(ProfilePictureActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Uploading");
        progressDialog.show();

        VolleyMultipartRequest volleyMultipartRequest = new
                VolleyMultipartRequest(Request.Method.POST, URLHelper.USER_PROFILE_API,
                        response -> {

                            progressDialog.dismiss();

                            String res = new String(response.data);
                            utils.print("ProfileUpdateRes", "" + res);
                            try {
                                JSONObject jsonObject = new JSONObject(res);
                                SharedHelper.putKey(context, "id", jsonObject.optString("id"));
                                SharedHelper.putKey(context, "first_name", jsonObject.optString("first_name"));
                                SharedHelper.putKey(context, "last_name", jsonObject.optString("last_name"));
                                SharedHelper.putKey(context, "sos", jsonObject.optString("sos"));
                                SharedHelper.putKey(context, "email", jsonObject.optString("email"));
                                if (jsonObject.optString("avatar").equals("") || jsonObject.optString("avatar") == null) {
                                    SharedHelper.putKey(context, "picture", "");
                                } else {
                                    if (jsonObject.optString("avatar").startsWith("http"))
                                        SharedHelper.putKey(context, "picture", jsonObject.optString("avatar"));
                                    else
                                        SharedHelper.putKey(context, "picture", URLHelper.BASE + "storage/app/public/" + jsonObject.optString("avatar"));
                                }

                                SharedHelper.putKey(context, "gender", jsonObject.optString("gender"));
                                SharedHelper.putKey(context, "mobile", jsonObject.optString("mobile"));


                                if (!SharedHelper.getKey(context, "picture").equalsIgnoreCase("")
                                        && SharedHelper.getKey(context, "picture") != null
                                        && !SharedHelper.getKey(context, "picture").equalsIgnoreCase("null")) {
                                    Picasso.get()
                                            .load(SharedHelper.getKey(context, "picture"))
                                            .placeholder(R.drawable.ic_dummy_user)
                                            .error(R.drawable.ic_dummy_user)
                                            .into(profile_Image);
                                } else {
                                    Picasso.get()
                                            .load(R.drawable.ic_dummy_user)
                                            .placeholder(R.drawable.ic_dummy_user)
                                            .error(R.drawable.ic_dummy_user)
                                            .into(profile_Image);
                                }

                                Toast.makeText(ProfilePictureActivity.this, getString(R.string.update_success), Toast.LENGTH_SHORT).show();
                                //displayMessage(getString(R.string.update_success));

                            } catch (JSONException e) {
                                e.printStackTrace();
                                displayMessage(getString(R.string.something_went_wrong));
                            }


                        }, error -> {
                    //                if ((customDialog != null) && customDialog.isShowing())
                    progressDialog.dismiss();
                    displayMessage(getString(R.string.something_went_wrong));
                }) {
                    @Override
                    public Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("first_name", SharedHelper.getKey(getApplicationContext(),"first_name"));
                        params.put("last_name", "");
                        params.put("email",  SharedHelper.getKey(getApplicationContext(),"email"));
                        params.put("mobile",  SharedHelper.getKey(getApplicationContext(),"mobile"));

                        return params;
                    }

                    @Override
                    public Map<String, String> getHeaders() {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("X-Requested-With", "XMLHttpRequest");
                        headers.put("Authorization", "Bearer " + SharedHelper.getKey(context, "access_token"));
                        return headers;
                    }

                    @Override
                    protected Map<String, VolleyMultipartRequest.DataPart> getByteData() {
                        Map<String, VolleyMultipartRequest.DataPart> params = new HashMap<>();
                        params.put("avatar", new VolleyMultipartRequest.DataPart("userImage.jpg", AppHelper.getFileDataFromDrawable(profile_Image.getDrawable()), "image/jpeg"));
                        return params;
                    }
                };
        ClassLuxApp.getInstance().addToRequestQueue(volleyMultipartRequest);

    }

    public void findViewByIdandInitialization() {

        Utilities.hideKeyboard(activity);
//        email = findViewById(R.id.email);
//        service = findViewById(R.id.service);
//        first_name = findViewById(R.id.first_name);
//        last_name = (EditText) findViewById(R.id.last_name);
//        mobile_no = findViewById(R.id.mobile_no);
//        txtHeaderName = findViewById(R.id.txtHeaderName);
//        txtHeaderMob = findViewById(R.id.txtHeaderMob);
//        saveBTN = findViewById(R.id.saveBTN);
//        changePasswordTxt = findViewById(R.id.changePasswordTxt);
//        backArrow = findViewById(R.id.backArrow);
//        profile_Image = findViewById(R.id.img_profile);
//        layoutUpdatePwd = findViewById(R.id.layoutUpdatePwd);
//        layoutService = findViewById(R.id.layoutService);
//        layoutMobile = findViewById(R.id.layoutMobile);
//        layoutAddress = findViewById(R.id.layoutAddress);
//        layoutName = findViewById(R.id.layoutName);
//
//        layoutUpdatePwd.setOnClickListener(this);
//        layoutService.setOnClickListener(this);
//        layoutMobile.setOnClickListener(this);
//        layoutAddress.setOnClickListener(this);
//        layoutName.setOnClickListener(this);
//
//        helper = new ConnectionHelper(context);
//        isInternet = helper.isConnectingToInternet();
//        setProviderDetails();
        //Assign current profile values to the edittext
        //Glide.with(activity).load(SharedHelper.getKey(context,"picture")).placeholder(R.drawable.ic_dummy_user).error(R.drawable.ic_dummy_user).into(profile_Image);
    }

    private void setProviderDetails() {
        if (!SharedHelper.getKey(context, "picture").equalsIgnoreCase("")
                && SharedHelper.getKey(context, "picture") != null
                && !SharedHelper.getKey(context, "picture").equalsIgnoreCase("null")) {
            Picasso.get()
                    .load(SharedHelper.getKey(context, "picture"))
                    .placeholder(R.drawable.ic_dummy_user)
                    .error(R.drawable.ic_dummy_user)
                    .into(profile_Image);
        } else {
            Picasso.get()
                    .load(R.drawable.ic_dummy_user)
                    .placeholder(R.drawable.ic_dummy_user)
                    .error(R.drawable.ic_dummy_user)
                    .into(profile_Image);
        }

//        email.setText(SharedHelper.getKey(context, "email"));
//        first_name.setText(SharedHelper.getKey(context, "first_name"));
//        txtHeaderName.setText(SharedHelper.getKey(context, "first_name"));
////        last_name.setText(SharedHelper.getKey(context, "last_name"));
//        String mobile = SharedHelper.getKey(context, "mobile");
//        if (mobile != null && !mobile.equalsIgnoreCase("null") && mobile.length() > 0) {
//            mobile_no.setText(mobile);
//            txtHeaderMob.setText(mobile);
//        } else {
//            mobile_no.setText("");
//            txtHeaderMob.setText("");
//        }
//
//        if (SharedHelper.getKey(context, "service") != null
//                && !SharedHelper.getKey(context, "service").equalsIgnoreCase("null")
//                && SharedHelper.getKey(context, "service").length() > 0)
//            service.setText(SharedHelper.getKey(context, "service"));
//        else
//            service.setText(getString(R.string.no_services));

    }

    public void GoToMainActivity() {
        Intent mainIntent = new Intent(activity, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainIntent);
        activity.finish();
    }

    public void displayMessage(String toastString) {
        Toasty.info(this, toastString, Toast.LENGTH_SHORT, true).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void GoToBeginActivity() {
        SharedHelper.putKey(activity, "loggedIn", getString(R.string.False));
        Intent mainIntent = new Intent(activity, Log.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainIntent);
        activity.finish();
    }




}