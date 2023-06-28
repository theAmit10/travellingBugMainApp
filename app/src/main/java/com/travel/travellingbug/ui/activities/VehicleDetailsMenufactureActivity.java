package com.travel.travellingbug.ui.activities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

public class VehicleDetailsMenufactureActivity extends AppCompatActivity {

    String license_number = "";
    String service_name = "";
    String service_model = "";
    String service_type = "";
    String service_color = "";
    String service_manufacture = "";

    String service_ac = "";

    String service_seat = "";

    EditText vehicleModelETL, vehicleAcETL, vehicleSeatETL;

    ImageView backArrow, vehicleImage;

    FloatingActionButton floatingActionButton;

    TextView updateProfile;


    public Context context = VehicleDetailsMenufactureActivity.this;

    public Activity activity = VehicleDetailsMenufactureActivity.this;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_details_menufacture);

        license_number = getIntent().getStringExtra("license_number");
        service_name = getIntent().getStringExtra("service_name");
        service_model = getIntent().getStringExtra("service_model");
        service_type = getIntent().getStringExtra("service_type");
        service_color = getIntent().getStringExtra("service_color");

        service_manufacture = SharedHelper.getKey(getApplicationContext(), "service_make");

        initComponent();
        vehicleModelETL.setText(SharedHelper.getKey(getApplicationContext(), "service_make"));
        vehicleAcETL.setText(SharedHelper.getKey(getApplicationContext(), "service_ac"));
        vehicleSeatETL.setText(SharedHelper.getKey(getApplicationContext(), "service_capacity"));

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        deviceHeight = displayMetrics.heightPixels;
        deviceWidth = displayMetrics.widthPixels;

        clickHandlerOnComponent();


    }

    private void initComponent() {

        vehicleModelETL = findViewById(R.id.vehicleManufactureETL);
        vehicleSeatETL = findViewById(R.id.vehicleSeatETL);
        vehicleAcETL = findViewById(R.id.vehicleAcETL);

        backArrow = findViewById(R.id.backArrow);

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Utilities.hideKeyboard(activity);
        vehicleImage = findViewById(R.id.vehicleImage);
//        btnTakePicture = findViewById(R.id.btnTakePicture);
        updateProfile = findViewById(R.id.choosePicturetv);

        helper = new ConnectionHelper(context);
        isInternet = helper.isConnectingToInternet();
        setProviderDetails();

    }

    private void setProviderDetails() {
        if (!SharedHelper.getKey(context, "service_image").equalsIgnoreCase("")
                && SharedHelper.getKey(context, "service_image") != null
                && !SharedHelper.getKey(context, "service_image").equalsIgnoreCase("null")) {
            Picasso.get()
                    .load(SharedHelper.getKey(context, "service_image"))
                    .placeholder(R.drawable.car_select)
                    .error(R.drawable.car_select)
                    .into(vehicleImage);
        } else {
            Picasso.get()
                    .load(R.drawable.car_select)
                    .placeholder(R.drawable.car_select)
                    .error(R.drawable.car_select)
                    .into(vehicleImage);
        }


    }

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

    private void updateProfileWithoutImage() {
        ProgressDialog progressDialog = new ProgressDialog(VehicleDetailsMenufactureActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Uploading");
        progressDialog.show();

        VolleyMultipartRequest volleyMultipartRequest = new
                VolleyMultipartRequest(Request.Method.POST, URLHelper.VEHICEL_ADD_AND_UPDATE,
                        response -> {

                            progressDialog.dismiss();

                            String res = new String(response.data);
                            utils.print("VEHICEL_ADD", "" + res);
                            try {
                                JSONObject jsonObject = new JSONObject(res);


                                Toast.makeText(VehicleDetailsMenufactureActivity.this, getString(R.string.update_success), Toast.LENGTH_SHORT).show();


                                showConfirmDialog();


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
                        params.put("model", service_model);
                        params.put("year", service_manufacture);
                        params.put("make", service_manufacture);
                        params.put("ac", "No");
                        params.put("vehicle_name", service_name);
                        params.put("service_number", license_number);
                        params.put("service_color", service_color);
                        params.put("capacity", "3");

                        System.out.println("MANUFACTURE PARAM : " + params.toString());


                        return params;
                    }

                    @Override
                    public Map<String, String> getHeaders() {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("X-Requested-With", "XMLHttpRequest");
                        headers.put("Authorization", "Bearer " + SharedHelper.getKey(context, "access_token"));
                        System.out.println("access token : " + SharedHelper.getKey(context, "access_token"));
                        return headers;
                    }

                };
        ClassLuxApp.getInstance().addToRequestQueue(volleyMultipartRequest);
    }

    private void callSuccess() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result", "result");
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    private void showConfirmDialog() {

//        AlertDialog alertDialog = new AlertDialog.Builder(FirstActivity.getInstance()).create();
        Dialog confirmDialog = new Dialog(VehicleDetailsMenufactureActivity.this);
        confirmDialog.setContentView(R.layout.schedule_dialog);

        TextView tvDone = confirmDialog.findViewById(R.id.tvDone);
        TextView bookingStatusTitleTv = confirmDialog.findViewById(R.id.bookingStatusTitleTv);
        TextView bookingStatusSubTitleTv = confirmDialog.findViewById(R.id.bookingStatusSubTitleTv);
        TextView tvDriverMsg = confirmDialog.findViewById(R.id.tvDriverMsg);

        bookingStatusTitleTv.setText("Successful");

        bookingStatusSubTitleTv.setText("Your Vehicle successfully Updated ");

        tvDriverMsg.setText("");

        confirmDialog.show();
        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog.dismiss();
                SharedHelper.putKey(getApplicationContext(), "vehicle_add", "yes");
                Intent intent = new Intent(getApplicationContext(), HomeScreenActivity.class);
                startActivity(intent);
            }
        });
    }

    public void displayMessage(String toastString) {
        Log.e("displayMessage", "" + toastString);
        Toasty.info(this, toastString, Toast.LENGTH_SHORT, true).show();
    }

    private void clickHandlerOnComponent() {


        service_manufacture = vehicleModelETL.getText().toString();
        floatingActionButton = findViewById(R.id.floatingActionButton);

        vehicleModelETL.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                service_manufacture = vehicleModelETL.getText().toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                service_manufacture = vehicleModelETL.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        vehicleAcETL.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                service_ac = vehicleAcETL.getText().toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                service_ac = vehicleAcETL.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        vehicleSeatETL.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                service_seat = vehicleSeatETL.getText().toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                service_seat = vehicleSeatETL.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        updateProfile.setOnClickListener(view -> {

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

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
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

    public void updateProfile() {
        if (isImageChanged) {
            updateProfileWithImage();
        } else {
            updateProfileWithoutImage();
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
            System.out.println("image uri : " + uri.toString());
            System.out.println("image uri path : " + data.getData().getPath());
            try {
                isImageChanged = true;
                //bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
                Bitmap resizeImg = getBitmapFromUri(this, uri);
                if (resizeImg != null) {
                    Bitmap reRotateImg = AppHelper.modifyOrientation(resizeImg, AppHelper.getPath(this, uri));
                    vehicleImage.setImageBitmap(reRotateImg);
//                    updateProfileWithImage();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void updateProfileWithImage() {

        ProgressDialog progressDialog = new ProgressDialog(VehicleDetailsMenufactureActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Uploading");
        progressDialog.show();

        VolleyMultipartRequest volleyMultipartRequest = new
                VolleyMultipartRequest(Request.Method.POST, URLHelper.VEHICEL_ADD_AND_UPDATE,
                        response -> {

                            progressDialog.dismiss();

                            String res = new String(response.data);
                            utils.print("VEHICEL_ADD", "" + res);
                            try {
                                JSONObject jsonObject = new JSONObject(res);


//                                SharedHelper.putKey(context, "id", jsonObject.optString("id"));
//                                SharedHelper.putKey(context, "first_name", jsonObject.optString("first_name"));
//                                SharedHelper.putKey(context, "last_name", jsonObject.optString("last_name"));
//                                SharedHelper.putKey(context, "sos", jsonObject.optString("sos"));
//                                SharedHelper.putKey(context, "email", jsonObject.optString("email"));
//                                if (jsonObject.optString("avatar").equals("") || jsonObject.optString("avatar") == null) {
//                                    SharedHelper.putKey(context, "picture", "");
//                                } else {
//                                    if (jsonObject.optString("avatar").startsWith("http"))
//                                        SharedHelper.putKey(context, "picture", jsonObject.optString("avatar"));
//                                    else
//                                        SharedHelper.putKey(context, "picture", URLHelper.BASE + "storage/app/public/" + jsonObject.optString("avatar"));
//                                }
//
//                                SharedHelper.putKey(context, "gender", jsonObject.optString("gender"));
//                                SharedHelper.putKey(context, "mobile", jsonObject.optString("mobile"));
//
//
//                                if (!SharedHelper.getKey(context, "picture").equalsIgnoreCase("")
//                                        && SharedHelper.getKey(context, "picture") != null
//                                        && !SharedHelper.getKey(context, "picture").equalsIgnoreCase("null")) {
//                                    Picasso.get()
//                                            .load(SharedHelper.getKey(context, "picture"))
//                                            .placeholder(R.drawable.ic_dummy_user)
//                                            .error(R.drawable.ic_dummy_user)
//                                            .into(profile_Image);
//                                } else {
//                                    Picasso.get()
//                                            .load(R.drawable.ic_dummy_user)
//                                            .placeholder(R.drawable.ic_dummy_user)
//                                            .error(R.drawable.ic_dummy_user)
//                                            .into(profile_Image);
//                                }

                                Toast.makeText(VehicleDetailsMenufactureActivity.this, getString(R.string.update_success), Toast.LENGTH_SHORT).show();


                                showConfirmDialog();
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
                        params.put("model", service_model);
                        params.put("year", service_manufacture);
                        params.put("make", service_manufacture);
                        params.put("ac", service_ac);
                        params.put("vehicle_name", service_name);
                        params.put("service_number", license_number);
                        params.put("service_color", service_color);
                        params.put("capacity", service_seat);

                        System.out.println("MANUFACTURE PARAM : " + params.toString());


                        return params;
                    }

                    @Override
                    public Map<String, String> getHeaders() {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("X-Requested-With", "XMLHttpRequest");
                        headers.put("Authorization", "Bearer " + SharedHelper.getKey(context, "access_token"));
                        System.out.println("access token : " + SharedHelper.getKey(context, "access_token"));
                        return headers;
                    }

                    @Override
                    protected Map<String, VolleyMultipartRequest.DataPart> getByteData() {
                        Map<String, VolleyMultipartRequest.DataPart> params = new HashMap<>();
                        params.put("vehicle_image", new VolleyMultipartRequest.DataPart("userImage.jpg", AppHelper.getFileDataFromDrawable(vehicleImage.getDrawable()), "image/jpeg"));
                        System.out.println("MANUFACTURE PARAM : " + params.toString());
                        return params;
                    }
                };
        ClassLuxApp.getInstance().addToRequestQueue(volleyMultipartRequest);

    }


}