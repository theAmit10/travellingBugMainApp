package com.travel.travellingbug.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;

public class PersonalDetailsActivity extends AppCompatActivity {

    TextView email, first_name, mobile_no;

    private static final String TAG = "EditProfile";
    private static final int SELECT_PHOTO = 100;
    public static int deviceHeight;
    public static int deviceWidth;
    public Context context = PersonalDetailsActivity.this;
    public Activity activity = PersonalDetailsActivity.this;
    CustomDialog customDialog;
    ConnectionHelper helper;
    Boolean isInternet;
    Button saveBTN;
    ImageView backArrow;
    TextView changePasswordTxt;
    EditText service;
//    ImageView profile_Image;
    Boolean isImageChanged = false;
    Utilities utils = new Utilities();
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SharedHelper.getKey(this, "selectedlanguage").contains("ar")) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
        setContentView(R.layout.activity_personal_details);

        findViewByIdandInitialization();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        deviceHeight = displayMetrics.heightPixels;
        deviceWidth = displayMetrics.widthPixels;

        backArrow.setOnClickListener(view -> {
//                GoToMainActivity();
            onBackPressed();
        });

        componenetClickHandler();

        getProfile();



    }

    private void componenetClickHandler() {

        saveBTN.setOnClickListener(view -> {

            Pattern ps = Pattern.compile(".*[0-9].*");
            Matcher firstName = ps.matcher(first_name.getText().toString());
//                Matcher lastName = ps.matcher(last_name.getText().toString());


            if (email.getText().toString().equals("") || email.getText().toString().length() == 0) {
                displayMessage(getString(R.string.email_validation));
            } else if (mobile_no.getText().toString().equals("") || mobile_no.getText().toString().length() == 0) {
                displayMessage(getString(R.string.mobile_number_empty));
            } else if (mobile_no.getText().toString().length() < 10 || mobile_no.getText().toString().length() > 20) {
                displayMessage(getString(R.string.mobile_number_validation));
            } else if (first_name.getText().toString().equals("") || first_name.getText().toString().length() == 0) {
                displayMessage(getString(R.string.first_name_empty));
            } else if (firstName.matches()) {
                displayMessage(getString(R.string.first_name_no_number));
            } else {
                if (isInternet) {
                    updateProfile();
                } else {
                    displayMessage(getString(R.string.something_went_wrong_net));
                }
            }


        });


        first_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonalDetailsActivity.this, UpdateProfile.class);
                intent.putExtra("parameter", "first_name");
                intent.putExtra("value", first_name.getText().toString());
                startActivityForResult(intent, 1);
            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonalDetailsActivity.this, UpdateProfile.class);
                intent.putExtra("parameter", "email");
                intent.putExtra("value", email.getText().toString());
                startActivityForResult(intent, 1);

            }
        });

        mobile_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonalDetailsActivity.this, UpdateProfile.class);
                intent.putExtra("parameter", "mobile");
                intent.putExtra("value", mobile_no.getText().toString());
                startActivityForResult(intent, 1);
            }
        });
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

    public void updateProfile() {
        if (isImageChanged) {
            updateProfileWithImage();
        } else {
            updateProfileWithoutImage();
        }
    }

    private void updateProfileWithoutImage() {
        customDialog = new CustomDialog(context);
        customDialog.setCancelable(false);
        customDialog.show();
        VolleyMultipartRequest volleyMultipartRequest = new
                VolleyMultipartRequest(Request.Method.POST, URLHelper.USER_PROFILE_API,
                        response -> {
                            customDialog.dismiss();
                            String res = new String(response.data);
                            try {
                                JSONObject jsonObject = new JSONObject(res);
                                SharedHelper.putKey(context, "id", jsonObject.optString("id"));
                                SharedHelper.putKey(context, "first_name", jsonObject.optString("first_name"));
                                SharedHelper.putKey(context, "last_name", jsonObject.optString("last_name"));
                                SharedHelper.putKey(context, "email", jsonObject.optString("email"));
                                if (jsonObject.optString("avatar").equals("") || jsonObject.optString("avatar") == null) {
                                    SharedHelper.putKey(context, "picture", "");
                                } else {
                                    if (jsonObject.optString("avatar").startsWith("http"))
                                        SharedHelper.putKey(context, "picture", jsonObject.optString("avatar"));
                                    else
                                        SharedHelper.putKey(context, "picture", URLHelper.BASE + "storage/app/public/" + jsonObject.optString("avatar"));
                                }
                                SharedHelper.putKey(context, "sos", jsonObject.optString("sos"));
                                SharedHelper.putKey(context, "gender", jsonObject.optString("gender"));
                                SharedHelper.putKey(context, "mobile", jsonObject.optString("mobile"));
                                //                        SharedHelper.putKey(context, "wallet_balance", jsonObject.optString("wallet_balance"));
                                //                        SharedHelper.putKey(context, "payment_mode", jsonObject.optString("payment_mode"));
                                GoToMainActivity();
                                Toast.makeText(PersonalDetailsActivity.this, getString(R.string.update_success), Toast.LENGTH_SHORT).show();
                                //displayMessage(getString(R.string.update_success));

                            } catch (JSONException e) {
                                e.printStackTrace();
                                displayMessage(getString(R.string.something_went_wrong));
                            }


                        }, error -> {
                    if ((customDialog != null) && customDialog.isShowing())
                        customDialog.dismiss();
                    displayMessage(getString(R.string.something_went_wrong));
                }) {
                    @Override
                    public Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("first_name", first_name.getText().toString());
                        params.put("last_name", "");
                        params.put("email", email.getText().toString());
                        params.put("mobile", mobile_no.getText().toString());
                        params.put("avatar", "");
                        return params;
                    }

                    @Override
                    public Map<String, String> getHeaders() {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("X-Requested-With", "XMLHttpRequest");
                        headers.put("Authorization", "Bearer " + SharedHelper.getKey(context, "access_token"));
                        return headers;
                    }
                };
        ClassLuxApp.getInstance().addToRequestQueue(volleyMultipartRequest);
    }

    private void updateProfileWithImage() {
//
//        ProgressDialog progressDialog = new ProgressDialog(PersonalDetailsActivity.this);
//        progressDialog.setCancelable(false);
//        progressDialog.setTitle("Uploading");
//        progressDialog.show();
//
//        VolleyMultipartRequest volleyMultipartRequest = new
//                VolleyMultipartRequest(Request.Method.POST, URLHelper.USER_PROFILE_API,
//                        response -> {
//
//                            progressDialog.dismiss();
//
//                            String res = new String(response.data);
//                            utils.print("ProfileUpdateRes", "" + res);
//                            try {
//                                JSONObject jsonObject = new JSONObject(res);
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
//
//                                Toast.makeText(PersonalDetailsActivity.this, getString(R.string.update_success), Toast.LENGTH_SHORT).show();
//                                //displayMessage(getString(R.string.update_success));
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                                displayMessage(getString(R.string.something_went_wrong));
//                            }
//
//
//                        }, error -> {
//                    //                if ((customDialog != null) && customDialog.isShowing())
//                    progressDialog.dismiss();
//                    displayMessage(getString(R.string.something_went_wrong));
//                }) {
//                    @Override
//                    public Map<String, String> getParams() {
//                        Map<String, String> params = new HashMap<>();
//                        params.put("first_name", first_name.getText().toString());
//                        params.put("last_name", "");
//                        params.put("email", email.getText().toString());
//                        params.put("mobile", mobile_no.getText().toString());
//
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
//
//                    @Override
//                    protected Map<String, VolleyMultipartRequest.DataPart> getByteData() {
//                        Map<String, VolleyMultipartRequest.DataPart> params = new HashMap<>();
//                        params.put("avatar", new VolleyMultipartRequest.DataPart("userImage.jpg", AppHelper.getFileDataFromDrawable(profile_Image.getDrawable()), "image/jpeg"));
//                        return params;
//                    }
//                };
//        ClassLuxApp.getInstance().addToRequestQueue(volleyMultipartRequest);

    }

    private void findViewByIdandInitialization() {
        Utilities.hideKeyboard(activity);

        email = findViewById(R.id.email);
        first_name = findViewById(R.id.first_name);
        mobile_no = findViewById(R.id.mobile_no);

        saveBTN = findViewById(R.id.saveBTN);
        backArrow = findViewById(R.id.backArrow);

        helper = new ConnectionHelper(context);
        isInternet = helper.isConnectingToInternet();
        setProviderDetails();

    }

    private void setProviderDetails() {
//        if (!SharedHelper.getKey(context, "picture").equalsIgnoreCase("")
//                && SharedHelper.getKey(context, "picture") != null
//                && !SharedHelper.getKey(context, "picture").equalsIgnoreCase("null")) {
//            Picasso.get()
//                    .load(SharedHelper.getKey(context, "picture"))
//                    .placeholder(R.drawable.ic_dummy_user)
//                    .error(R.drawable.ic_dummy_user)
//                    .into(profile_Image);
//        } else {
//            Picasso.get()
//                    .load(R.drawable.ic_dummy_user)
//                    .placeholder(R.drawable.ic_dummy_user)
//                    .error(R.drawable.ic_dummy_user)
//                    .into(profile_Image);
//        }

        email.setText(SharedHelper.getKey(context, "email"));
        first_name.setText(SharedHelper.getKey(context, "first_name"));
//        txtHeaderName.setText(SharedHelper.getKey(context, "first_name"));
//        last_name.setText(SharedHelper.getKey(context, "last_name"));
        String mobile = SharedHelper.getKey(context, "mobile");
        if (mobile != null && !mobile.equalsIgnoreCase("null") && mobile.length() > 0) {
            mobile_no.setText(mobile);
//            txtHeaderMob.setText(mobile);
        } else {
            mobile_no.setText("");
//            txtHeaderMob.setText("");
        }
//
//        if (SharedHelper.getKey(context, "service") != null
//                && !SharedHelper.getKey(context, "service").equalsIgnoreCase("null")
//                && SharedHelper.getKey(context, "service").length() > 0)
//            service.setText(SharedHelper.getKey(context, "service"));
//        else
//            service.setText(getString(R.string.no_services));

    }

    public void GoToMainActivity() {
        Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainIntent);
        finish();
    }

    public void displayMessage(String toastString) {
        Toasty.info(this, toastString, Toast.LENGTH_SHORT, true).show();
    }

    public void GoToBeginActivity() {
        SharedHelper.putKey(getApplicationContext(), "loggedIn", getString(R.string.False));
        Intent mainIntent = new Intent(getApplicationContext(), Log.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainIntent);
        finish();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK && data != null && data.getData() != null) {

//            uri = data.getData();
//            try {
//                isImageChanged = true;
//
////                Bitmap resizeImg = getBitmapFromUri(this, uri);
////                if (resizeImg != null) {
////                    Bitmap reRotateImg = AppHelper.modifyOrientation(resizeImg, AppHelper.getPath(this, uri));
////                    profile_Image.setImageBitmap(reRotateImg);
////                    updateProfileWithImage();
////                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("result");
                if (SharedHelper.getKey(PersonalDetailsActivity.this, "mobile") != null) {
                    mobile_no.setText(SharedHelper.getKey(PersonalDetailsActivity.this, "mobile"));
//                    txtHeaderMob.setText(SharedHelper.getKey(EditProfile.this, "mobile"));
                }
                if (SharedHelper.getKey(PersonalDetailsActivity.this, "email") != null) {
                    email.setText(SharedHelper.getKey(PersonalDetailsActivity.this, "email"));
                }
                if (SharedHelper.getKey(PersonalDetailsActivity.this, "first_name") != null) {
                    first_name.setText(SharedHelper.getKey(PersonalDetailsActivity.this, "first_name"));
//                    txtHeaderName.setText(SharedHelper.getKey(EditProfile.this, "first_name"));
                }
            }

        }
    }

}