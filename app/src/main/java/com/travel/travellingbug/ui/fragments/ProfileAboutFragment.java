package com.travel.travellingbug.ui.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;
import com.travel.travellingbug.ClassLuxApp;
import com.travel.travellingbug.R;
import com.travel.travellingbug.helper.ConnectionHelper;
import com.travel.travellingbug.helper.CustomDialog;
import com.travel.travellingbug.helper.SharedHelper;
import com.travel.travellingbug.helper.URLHelper;
import com.travel.travellingbug.helper.VolleyMultipartRequest;
import com.travel.travellingbug.ui.activities.EditProfile;
import com.travel.travellingbug.ui.activities.MainActivity;
import com.travel.travellingbug.ui.activities.PersonalDetailsActivity;
import com.travel.travellingbug.ui.activities.Profile;
import com.travel.travellingbug.ui.activities.ProfilePictureActivity;
import com.travel.travellingbug.ui.activities.SplashScreen;
import com.travel.travellingbug.ui.activities.TravelPreferenceActivity;
import com.travel.travellingbug.ui.activities.TravelPreferenceActivityMain;
import com.travel.travellingbug.ui.activities.UpdateProfile;
import com.travel.travellingbug.ui.activities.VehicleDetailsLicensePlateNumberActivity;
import com.travel.travellingbug.ui.activities.VerifyEmailActivity;
import com.travel.travellingbug.ui.activities.VerifyIdActivity;
import com.travel.travellingbug.ui.activities.VerifyMobileNumberActivity;
import com.travel.travellingbug.utills.Utilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;


public class ProfileAboutFragment extends Fragment {

    TextView verifyId, addMyPreferencesTv, addAMiniBioTv, addVehicleTv, editProfilePictv, editPersonalDetailstv;

    TextView email, first_name, mobile_no;
    TextView txtuserName;
    CircleImageView img_profile;
    GoogleApiClient mGoogleApiClient;

    private static final String TAG = "EditProfile";
    private static final int SELECT_PHOTO = 100;
    public static int deviceHeight;
    public static int deviceWidth;

    //    public Context context = getContext();
//    public Activity activity = getActivity();
    CustomDialog customDialog;
    ConnectionHelper helper;
    Boolean isInternet;
    Button saveBTN;
    //    ImageView backArrow;
    TextView changePasswordTxt;
    EditText service;
    //    ImageView profile_Image;
    Boolean isImageChanged = false;
    Utilities utils = new Utilities();
    Uri uri;


    public ProfileAboutFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        if (SharedHelper.getKey(getContext(), "selectedlanguage").contains("ar")) {
//            activity.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
//        } else {
//            activity.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
//        }
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_about, container, false);

        initComponent(view);

        setTextToComponent();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        deviceHeight = displayMetrics.heightPixels;
        deviceWidth = displayMetrics.widthPixels;


        clickHandlerOnComponenet();

        getProfile();

        return view;
    }

    private void setTextToComponent() {
        txtuserName.setText(SharedHelper.getKey(getContext(), "first_name"));

        if (!SharedHelper.getKey(getContext(), "picture").isEmpty()) {
            Picasso.get().load(SharedHelper.getKey(getContext(), "picture"))
                    .placeholder(R.drawable.ic_dummy_user)
                    .error(R.drawable.ic_dummy_user)
                    .into(img_profile);
        }
    }

    private void clickHandlerOnComponenet() {
        verifyId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), VerifyIdActivity.class);
                startActivity(intent);
            }
        });

        addMyPreferencesTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), TravelPreferenceActivityMain.class);
                startActivity(intent);
            }
        });

        addAMiniBioTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), TravelPreferenceActivity.class);
                startActivity(intent);
            }
        });

        addVehicleTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), VehicleDetailsLicensePlateNumberActivity.class);
                startActivity(intent);
            }
        });


        editProfilePictv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ProfilePictureActivity.class);
                startActivity(intent);
            }
        });

        editPersonalDetailstv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PersonalDetailsActivity.class);
                startActivity(intent);
            }
        });

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
                Intent intent = new Intent(getContext(), UpdateProfile.class);
                intent.putExtra("parameter", "first_name");
                intent.putExtra("value", first_name.getText().toString());
                startActivityForResult(intent, 11);
            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), UpdateProfile.class);
                intent.putExtra("parameter", "email");
                intent.putExtra("value", email.getText().toString());
                startActivityForResult(intent, 11);

            }
        });

        mobile_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), UpdateProfile.class);
                intent.putExtra("parameter", "mobile");
                intent.putExtra("value", mobile_no.getText().toString());
                startActivityForResult(intent, 11);
            }
        });


    }

    private void initComponent(View view) {
        verifyId = view.findViewById(R.id.verifyId);
        addMyPreferencesTv = view.findViewById(R.id.addMyPreferencesTv);
        addAMiniBioTv = view.findViewById(R.id.addAMiniBioTv);
        addVehicleTv = view.findViewById(R.id.addVehicleTv);

        editProfilePictv = view.findViewById(R.id.editProfilePictv);
        editPersonalDetailstv = view.findViewById(R.id.editPersonalDetailstv);


        first_name = view.findViewById(R.id.first_name);
        email = view.findViewById(R.id.email);
        mobile_no = view.findViewById(R.id.mobile_no);
        saveBTN = view.findViewById(R.id.saveBTN);
//        backArrow = view.findViewById(R.id.backArrow);


        helper = new ConnectionHelper(getContext());
        isInternet = helper.isConnectingToInternet();



        txtuserName = view.findViewById(R.id.txtuserName);
        img_profile = view.findViewById(R.id.img_profile);

        setProviderDetails();

    }

    @SuppressLint("ResourceAsColor")
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

        String emailS = SharedHelper.getKey(getContext(), "email");
        if (emailS != null && !emailS.equalsIgnoreCase("null")&& !emailS.equalsIgnoreCase("") && emailS.length() > 0) {
            email.setText(emailS);
            email.setTextColor(getResources().getColor(R.color.dark_gray));
            email.setTypeface(email.getTypeface(), Typeface.BOLD);
            email.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//            txtHeaderMob.setText(mobile);
        } else {
            email.setText("Confirm your email");
            email.setTextColor(getResources().getColor(R.color.green));
            email.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_add, 0, 0, 0);
//            txtHeaderMob.setText("");
        }


        String first_nameS = SharedHelper.getKey(getContext(), "first_name");
        if (first_nameS != null && !first_nameS.equalsIgnoreCase("null") && first_nameS.length() > 0) {
            first_name.setText(first_nameS);
            first_name.setTextColor(getResources().getColor(R.color.dark_gray));
            first_name.setTypeface(first_name.getTypeface(), Typeface.BOLD);
            first_name.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//            txtHeaderMob.setText(mobile);
        } else {
            first_name.setText("Add name");
            first_name.setTextColor(getResources().getColor(R.color.green));
            first_name.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_add, 0, 0, 0);
//            txtHeaderMob.setText("");
        }

//        email.setText(SharedHelper.getKey(getContext(), "email"));

//        txtHeaderName.setText(SharedHelper.getKey(context, "first_name"));
//        last_name.setText(SharedHelper.getKey(context, "last_name"));
        String mobile = SharedHelper.getKey(getContext(), "mobile");

        if (mobile != null && !mobile.equalsIgnoreCase("null") && mobile.length() > 0) {
            mobile_no.setText(mobile);
            mobile_no.setTextColor(getResources().getColor(R.color.dark_gray));
            mobile_no.setTypeface(mobile_no.getTypeface(), Typeface.BOLD);
            mobile_no.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//            txtHeaderMob.setText(mobile);
        } else {
            mobile_no.setText("Add phone number");
            mobile_no.setTextColor(getResources().getColor(R.color.green));
            mobile_no.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_add, 0, 0, 0);
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
        Intent mainIntent = new Intent(getContext(), MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainIntent);
        getActivity().finish();
    }


    public void GoToBeginActivity() {
        SharedHelper.putKey(getContext(), "loggedIn", getString(R.string.False));
        Intent mainIntent = new Intent(getContext(), Log.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainIntent);
        getActivity().finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (!SharedHelper.getKey(getContext(), "picture").equalsIgnoreCase("")
                    && SharedHelper.getKey(getContext(), "picture") != null
                    && !SharedHelper.getKey(getContext(), "picture").equalsIgnoreCase("null")) {
                Picasso.get()
                        .load(SharedHelper.getKey(getContext(), "picture"))
                        .placeholder(R.drawable.ic_dummy_user)
                        .error(R.drawable.ic_dummy_user)
                        .into(img_profile);
            } else {
                Picasso.get()
                        .load(R.drawable.ic_dummy_user)
                        .placeholder(R.drawable.ic_dummy_user)
                        .error(R.drawable.ic_dummy_user)
                        .into(img_profile);
            }


            if (SharedHelper.getKey(getContext(), "first_name") != null) {
                txtuserName.setText(SharedHelper.getKey(getContext(), "first_name"));
            }
        }
        if (requestCode == 11) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("result");
                if (SharedHelper.getKey(getContext(), "mobile") != null) {
                    mobile_no.setText(SharedHelper.getKey(getContext(), "mobile"));
//                    txtHeaderMob.setText(SharedHelper.getKey(EditProfile.this, "mobile"));
                }
                if (SharedHelper.getKey(getContext(), "email") != null) {
                    email.setText(SharedHelper.getKey(getContext(), "email"));
                }
                if (SharedHelper.getKey(getContext(), "first_name") != null) {
                    first_name.setText(SharedHelper.getKey(getContext(), "first_name"));
//                    txtHeaderName.setText(SharedHelper.getKey(EditProfile.this, "first_name"));
                }
            }

        }


    }

    public void getProfile() {

        if (isInternet) {
            JSONObject object = new JSONObject();
            JsonObjectRequest jsonObjectRequest = new
                    JsonObjectRequest(Request.Method.GET, URLHelper.USER_PROFILE_API,
                            object, response -> {
                        Log.v("GetProfile", response.toString());
                        SharedHelper.putKey(getContext(), "id", response.optString("id"));
                        SharedHelper.putKey(getContext(), "first_name", response.optString("first_name"));
                        SharedHelper.putKey(getContext(), "last_name", response.optString("last_name"));
                        SharedHelper.putKey(getContext(), "email", response.optString("email"));
                        SharedHelper.putKey(getContext(), "picture", URLHelper.BASE + "storage/app/public/" + response.optString("picture"));
                        SharedHelper.putKey(getContext(), "gender", response.optString("gender"));
                        SharedHelper.putKey(getContext(), "sos", response.optString("sos"));
                        SharedHelper.putKey(getContext(), "mobile", response.optString("mobile"));
                        SharedHelper.putKey(getContext(), "refer_code", response.optString("refer_code"));
                        SharedHelper.putKey(getContext(), "wallet_balance", response.optString("wallet_balance"));
                        SharedHelper.putKey(getContext(), "payment_mode", response.optString("payment_mode"));
                        SharedHelper.putKey(getContext(), "currency", response.optString("currency"));


                        //                    SharedHelper.putKey(context, "currency", response.optString("payment_mode"));
                        SharedHelper.putKey(getContext(), "rating", response.optString("rating"));
                        SharedHelper.putKey(getContext(), "status", response.optString("status"));
                        SharedHelper.putKey(getContext(), "ulatitude", response.optString("latitude"));
                        SharedHelper.putKey(getContext(), "ulongitude", response.optString("longitude"));
                        SharedHelper.putKey(getContext(), "udevice_token", response.optString("device_token"));


                        SharedHelper.putKey(getContext(), "loggedIn", getString(R.string.True));
                        if (response.optString("avatar").startsWith("http"))
                            SharedHelper.putKey(getContext(), "picture", response.optString("avatar"));
                        else
                            SharedHelper.putKey(getContext(), "picture", URLHelper.BASE + "storage/app/public/" + response.optString("avatar"));

                        if (response.optJSONObject("service") != null) {
                            try {
                                JSONObject service = response.optJSONObject("service");

                                SharedHelper.putKey(getContext(), "service_id", service.optString("id"));
                                SharedHelper.putKey(getContext(), "service_status", service.optString("status"));
                                SharedHelper.putKey(getContext(), "service_number", service.optString("service_number"));
                                SharedHelper.putKey(getContext(), "service_model", service.optString("service_model"));
                                SharedHelper.putKey(getContext(), "service_capacity", service.optString("service_capacity"));
                                SharedHelper.putKey(getContext(), "service_year", service.optString("service_year"));
                                SharedHelper.putKey(getContext(), "service_make", service.optString("service_make"));
                                SharedHelper.putKey(getContext(), "service_name", service.optString("service_name"));
                                SharedHelper.putKey(getContext(), "service_ac", service.optString("service_ac"));
                                SharedHelper.putKey(getContext(), "service_color", service.optString("service_color"));


                                if (service.optJSONObject("service_type") != null) {
                                    JSONObject serviceType = service.optJSONObject("service_type");
                                    SharedHelper.putKey(getContext(), "service", serviceType.optString("name"));
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
                            Log.e(TAG, "getHeaders: Token" + SharedHelper.getKey(getContext(), "access_token") + SharedHelper.getKey(getContext(), "token_type"));
                            headers.put("Authorization", "" + "Bearer" + " " + SharedHelper.getKey(getContext(), "access_token"));
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
        customDialog = new CustomDialog(getContext());
        customDialog.setCancelable(false);
        customDialog.show();
        VolleyMultipartRequest volleyMultipartRequest = new
                VolleyMultipartRequest(Request.Method.POST, URLHelper.USER_PROFILE_API,
                        response -> {
                            customDialog.dismiss();
                            String res = new String(response.data);
                            try {
                                JSONObject jsonObject = new JSONObject(res);
                                SharedHelper.putKey(getContext(), "id", jsonObject.optString("id"));
                                SharedHelper.putKey(getContext(), "first_name", jsonObject.optString("first_name"));
                                SharedHelper.putKey(getContext(), "last_name", jsonObject.optString("last_name"));
                                SharedHelper.putKey(getContext(), "email", jsonObject.optString("email"));
                                if (jsonObject.optString("avatar").equals("") || jsonObject.optString("avatar") == null) {
                                    SharedHelper.putKey(getContext(), "picture", "");
                                } else {
                                    if (jsonObject.optString("avatar").startsWith("http"))
                                        SharedHelper.putKey(getContext(), "picture", jsonObject.optString("avatar"));
                                    else
                                        SharedHelper.putKey(getContext(), "picture", URLHelper.BASE + "storage/app/public/" + jsonObject.optString("avatar"));
                                }
                                SharedHelper.putKey(getContext(), "sos", jsonObject.optString("sos"));
                                SharedHelper.putKey(getContext(), "gender", jsonObject.optString("gender"));
                                SharedHelper.putKey(getContext(), "mobile", jsonObject.optString("mobile"));
                                //                        SharedHelper.putKey(context, "wallet_balance", jsonObject.optString("wallet_balance"));
                                //                        SharedHelper.putKey(context, "payment_mode", jsonObject.optString("payment_mode"));
                                GoToMainActivity();
                                Toast.makeText(getContext(), getString(R.string.update_success), Toast.LENGTH_SHORT).show();
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
                        headers.put("Authorization", "Bearer " + SharedHelper.getKey(getContext(), "access_token"));
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


    private void showLogoutDialog() {
        if (!getActivity().isFinishing()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(getString(R.string.logout));
            builder.setMessage(getString(R.string.exit_confirm));
            builder.setPositiveButton(R.string.yes,
                    (dialog, which) -> logout());
            builder.setNegativeButton(R.string.no, (dialog, which) -> {
                //Reset to previous seletion menu in navigation
                dialog.dismiss();
            });
            builder.setCancelable(false);
            final AlertDialog dialog = builder.create();
            //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            dialog.setOnShowListener(arg -> {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                        .setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                        .setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
            });
            dialog.show();
        }
    }

//    private boolean isMyServiceRunning(Class<?> serviceClass) {
//        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
//            if (serviceClass.getName().equals(service.service.getClassName())) {
//                return true;
//            }
//        }
//        return false;
//    }

    public void logout() {
        JSONObject object = new JSONObject();
        try {
            object.put("id", SharedHelper.getKey(getContext(), "id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new
                JsonObjectRequest(Request.Method.POST,
                        URLHelper.LOGOUT,
                        object,
                        response -> {
                            if (SharedHelper.getKey(getContext(), "login_by").equals("facebook"))
                                LoginManager.getInstance().logOut();
                            if (SharedHelper.getKey(getContext(), "login_by").equals("google"))
                                signOut();
                            if (!SharedHelper.getKey(getContext(), "account_kit_token").equalsIgnoreCase("")) {
                                Log.e("MainActivity", "Account kit logout: " + SharedHelper.getKey(getContext(), "account_kit_token"));
                                SharedHelper.putKey(getContext(), "account_kit_token", "");
                            }


                            //SharedHelper.putKey(context, "email", "");
                            SharedHelper.clearSharedPreferences(getContext());
                            Intent goToLogin = new Intent(getContext(), SplashScreen.class);
                            goToLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(goToLogin);
//                            finishAffinity();
                        }, error -> {
                    displayMessage(getString(R.string.something_went_wrong));
                }) {
                    @Override
                    public java.util.Map<String, String> getHeaders() {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("X-Requested-With", "XMLHttpRequest");
                        Log.e("getHeaders: Token", SharedHelper.getKey(getContext(), "access_token") + SharedHelper.getKey(getContext(), "token_type"));
                        headers.put("Authorization", "" + "Bearer" + " " + SharedHelper.getKey(getContext(), "access_token"));
                        return headers;
                    }
                };

        ClassLuxApp.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    public void displayMessage(String toastString) {
        Toasty.info(getContext(), toastString, Toast.LENGTH_SHORT, true).show();
    }

    private void signOut() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                //taken from google api console (Web api client id)
//                .requestIdToken("795253286119-p5b084skjnl7sll3s24ha310iotin5k4.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();
        mGoogleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(@Nullable Bundle bundle) {

                FirebaseAuth.getInstance().signOut();
                if (mGoogleApiClient.isConnected()) {
                    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(status -> {
                        if (status.isSuccess()) {
                            Log.d("MainAct", "Google User Logged out");
                           /* Intent intent = new Intent(LogoutActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();*/
                        }
                    });
                }
            }

            @Override
            public void onConnectionSuspended(int i) {
                Log.d("MAin", "Google API Client Connection Suspended");
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


}