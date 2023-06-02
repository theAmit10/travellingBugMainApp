package com.travel.travellingbug.ui.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;
import com.travel.travellingbug.ClassLuxApp;
import com.travel.travellingbug.Login;
import com.travel.travellingbug.R;
import com.travel.travellingbug.helper.ConnectionHelper;
import com.travel.travellingbug.helper.CustomDialog;
import com.travel.travellingbug.helper.SharedHelper;
import com.travel.travellingbug.helper.URLHelper;
import com.travel.travellingbug.models.User;
import com.travel.travellingbug.utills.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import es.dmoral.toasty.Toasty;


public class HistoryDetails extends AppCompatActivity {
    public JSONObject jsonObject;
    Activity activity;
    Context context;
    Boolean isInternet;
    ConnectionHelper helper;
    CustomDialog customDialog;
    TextView tripAmount;
    TextView tripDate;
    TextView paymentType;
    Fragment fragment;
    public static FragmentManager fragmentManager;
    TextView tripComments,trip_id;
    TextView tripProviderName;
    TextView tripSource;
    TextView tripDestination;
    TextView lblTitle;
    TextView tripId;
    ImageView tripImg, tripProviderImg, paymentTypeImg;
    RatingBar tripProviderRating;
    LinearLayout sourceAndDestinationLayout;
    ImageView backArrow;
    LinearLayout parentLayout, lnrComments;
    String tag = "";
    Button btnCancelRide, btnStartRide;
    Utilities utils = new Utilities();
    String history_response;


    String booking_id = "", request_id = "", s_date = "", s_time = "", status = "", payment_mode = "", s_address = "", d_address = "", estimated_fare = "", verification_code = "", static_map = "", first_name = "", mobile = "", avatar = "", rating = "";

    String current_trip_user_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SharedHelper.getKey(this, "selectedlanguage").contains("ar")) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }


        setContentView(R.layout.activity_history_details);
        findViewByIdAndInitialize();

        getIntentData();

        try {
            Intent intent = getIntent();
            String post_details = intent.getExtras().getString("post_value");
            tag = intent.getExtras().getString("tag");
            jsonObject = new JSONObject(post_details);
        } catch (Exception e) {
            jsonObject = null;
            e.printStackTrace();
        }

        if (jsonObject != null) {

            if (tag.equalsIgnoreCase("past_trips")) {
                btnCancelRide.setVisibility(View.GONE);
                btnStartRide.setVisibility(View.GONE);
                lnrComments.setVisibility(View.VISIBLE);
                getRequestDetails();
                lblTitle.setText(getString(R.string.past_trips));
            } else {
                btnCancelRide.setVisibility(View.VISIBLE);
                btnStartRide.setVisibility(View.VISIBLE);
                lnrComments.setVisibility(View.GONE);
                getUpcomingDetails();
                lblTitle.setText(getString(R.string.upcoming_rides));
            }
        }

        SetDetailInComponent();

        backArrow.setOnClickListener(view -> onBackPressed());
    }

    private void SetDetailInComponent() {


        String replacedStr = static_map.replaceAll("google_map_key", getResources().getString(R.string.google_map_api));
        Picasso.get().load(replacedStr)
                .placeholder(R.drawable.car_select).error(R.drawable.car_select).into(tripImg);

        System.out.println("static_map "+static_map);
        System.out.println("static_map replacedStr "+replacedStr);

        Picasso.get().load(URLHelper.BASE + "storage/app/public/" + avatar)
                .placeholder(R.drawable.car_select).error(R.drawable.car_select).into(tripProviderImg);

        tripProviderName.setText(first_name);

        tripProviderRating.setRating(Float.parseFloat(rating));

        tripDate.setText(s_date);

        tripSource.setText(s_address);

        tripDestination.setText(d_address);

        trip_id.setText(booking_id);

        paymentType.setText(payment_mode);

        tripAmount.setText(estimated_fare);


    }

    private void getIntentData() {

        request_id = getIntent().getStringExtra("request_id");
        s_address = getIntent().getStringExtra("s_address");
        d_address = getIntent().getStringExtra("d_address");
        s_date = getIntent().getStringExtra("s_date");
        s_time = getIntent().getStringExtra("s_time");
        status = getIntent().getStringExtra("status");
        payment_mode = getIntent().getStringExtra("payment_mode");
        estimated_fare = getIntent().getStringExtra("estimated_fare");
        verification_code = getIntent().getStringExtra("verification_code");
        static_map = getIntent().getStringExtra("static_map");
        first_name = getIntent().getStringExtra("first_name");
        rating = getIntent().getStringExtra("rating");
        avatar = getIntent().getStringExtra("avatar");
        booking_id = getIntent().getStringExtra("booking_id");
        current_trip_user_id = getIntent().getStringExtra("current_trip_user_id");

    }

    private void updateStatusForSingleUserRide(String rideId, String status){

        StringRequest request = new StringRequest(Request.Method.POST, URLHelper.UPDATE_SINGLE_USER_RIDE_STATUS_BY_PROVIDER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (response != null) {
                        System.out.println("data : "+jsonObject.toString());
                        if(jsonObject.optString("id") != null){
                            System.out.println("STATUS UPDATED OF REQUEST ID : "+jsonObject.optString("id"));
                        }
                    }

                } catch (JSONException e) {
                    displayMessage(e.toString());
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error Found", Toast.LENGTH_SHORT).show();
            }

        }) {


            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("rideid", rideId);
                params.put("status", status);
                return params;
            }
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("X-Requested-With", "XMLHttpRequest");
                headers.put("Authorization", "Bearer " + SharedHelper.getKey(getApplicationContext(), "access_token"));
                return headers;
            }

        };

        ClassLuxApp.getInstance().addToRequestQueue(request);


    }

    public void findViewByIdAndInitialize() {
        activity = HistoryDetails.this;
        context = HistoryDetails.this;
        helper = new ConnectionHelper(activity);
        isInternet = helper.isConnectingToInternet();
        parentLayout = findViewById(R.id.parentLayout);
        parentLayout.setVisibility(View.GONE);
        tripAmount = findViewById(R.id.tripAmount);
        tripDate = findViewById(R.id.tripDate);
        paymentType = findViewById(R.id.paymentType);
        paymentTypeImg = findViewById(R.id.paymentTypeImg);
        tripProviderImg = findViewById(R.id.tripProviderImg);
        tripImg = findViewById(R.id.tripImg);
        tripComments = findViewById(R.id.tripComments);
        tripProviderName = findViewById(R.id.tripProviderName);
        tripProviderRating = findViewById(R.id.tripProviderRating);
        tripSource = findViewById(R.id.tripSource);
        tripDestination = findViewById(R.id.tripDestination);
        lblTitle = findViewById(R.id.lblTitle);
        tripId = findViewById(R.id.trip_id);
        sourceAndDestinationLayout = findViewById(R.id.sourceAndDestinationLayout);

        btnCancelRide = findViewById(R.id.btnCancelRide);
        btnStartRide = findViewById(R.id.btnStartRide);
        trip_id = findViewById(R.id.trip_id);
        lnrComments = findViewById(R.id.lnrComments);
        backArrow = findViewById(R.id.backArrow);

        LayerDrawable drawable = (LayerDrawable) tripProviderRating.getProgressDrawable();
        drawable.getDrawable(0).setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
        drawable.getDrawable(1).setColorFilter(Color.parseColor("#FFAB00"), PorterDuff.Mode.SRC_ATOP);
        drawable.getDrawable(2).setColorFilter(Color.parseColor("#FFAB00"), PorterDuff.Mode.SRC_ATOP);


        btnCancelRide.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(getString(R.string.cencel_request))
                    .setCancelable(false)
                    .setPositiveButton("YES", (dialog, id) -> {
                        dialog.dismiss();
                        cancelRequest();
                    })
                    .setNegativeButton("NO", (dialog, id) -> dialog.dismiss());
            AlertDialog alert = builder.create();
            alert.show();
        });

        btnStartRide.setOnClickListener(view -> {

            updateStatusForSingleUserRide(request_id,"STARTED");
            Toast.makeText(getApplication(), "Start Ride", Toast.LENGTH_SHORT).show();
            String res = null;

            try {
                JSONArray array = new JSONArray(history_response);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    res = obj.toString();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.e("TAG", "RES: " + res);
            JSONArray array = new JSONArray();
            JSONObject req = new JSONObject();
            try {
                JSONObject object = (JSONObject) new JSONTokener(res).nextValue();
                req.put("request", object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            array.put(req);
            Log.e("TAG", "REQ: " + array);
            Log.e("History", "Details REQ: " + array);
            Intent i = new Intent(getApplication(), MainActivity.class);

//            HistoryDetails.this.runOnUiThread(() -> {
//                fragment = new YourRideFragment();
//                FragmentManager manager = getSupportFragmentManager();
//                @SuppressLint("CommitTransaction")
//                FragmentTransaction transaction = manager.beginTransaction();
//                transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.slide_out_right);
//                transaction.replace(R.id.content, fragment);
//                transaction.commit();
//                fragmentManager = getSupportFragmentManager();
//            });
//            GoToFragment();
            i.putExtra("datas", array.toString());
            i.putExtra("current_trip_request_id", request_id);
            i.putExtra("current_trip_user_id", current_trip_user_id);
            i.putExtra("type", "SCHEDULED");
            startActivity(i);
            finish();
        });
    }

    public void GoToFragment() {
        HistoryDetails.this.runOnUiThread(() -> {
//            drawer.closeDrawers();
            FragmentManager manager = getSupportFragmentManager();
            @SuppressLint("CommitTransaction")
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.content, fragment);
            transaction.commit();
        });
    }


    private void setDetails(JSONArray response) {
        if (response != null && response.length() > 0) {
            Picasso.get().load(response.optJSONObject(0).optString("static_map"))
                    .placeholder(R.drawable.placeholder).error(R.drawable.placeholder).into(tripImg);
            if (!response.optJSONObject(0).optString("payment").equalsIgnoreCase("null")) {
                Log.e("History Details", "onResponse: Currency" + SharedHelper.getKey(context, "currency"));
                //tripAmount.setText(SharedHelper.getKey(context, "currency") + "" + response.optJSONObject(0).optJSONObject("payment").optString("total"));
            } else {
                //tripAmount.setText(SharedHelper.getKey(context, "currency") + "" + "0");
            }
            String form;
            if (tag.equalsIgnoreCase("past_trips")) {
                form = response.optJSONObject(0).optString("assigned_at");
            } else {
                form = response.optJSONObject(0).optString("schedule_at");
            }
            try {
                tripDate.setText(getDate(form) + "th " + getMonth(form) + " " + getYear(form) + "\n" + getTime(form));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            tripId.setText(response.optJSONObject(0).optString("booking_id"));
            if (response.optJSONObject(0).optString("payment_mode").equals("CASH")) {
                paymentType.setText(getString(R.string.cash));
            } else {
                paymentType.setText(getString(R.string.card));
            }

            if (response.optJSONObject(0).optString("payment_mode").equalsIgnoreCase("CASH")) {
                paymentTypeImg.setImageResource(R.drawable.credit_card);
            } else {
                paymentTypeImg.setImageResource(R.drawable.visa_icon);
            }

//            if (response.optJSONObject(0).optJSONObject("user").optString("picture").startsWith("http"))
//                Picasso.get().load(response.optJSONObject(0).optJSONObject("user").optString("picture")).placeholder(R.drawable.ic_dummy_user).error(R.drawable.ic_dummy_user).into(tripProviderImg);
//            else
//                Picasso.get().load(URLHelper.BASE + "storage/app/public/" + response.optJSONObject(0).optJSONObject("user").optString("picture")).placeholder(R.drawable.ic_dummy_user).error(R.drawable.ic_dummy_user).into(tripProviderImg);
            final JSONArray res = response;
            tripProviderImg.setOnClickListener(v -> {
                JSONObject jsonObject = res.optJSONObject(0).optJSONObject("user");

                User user = new User();
                user.setFirstName(jsonObject.optString("first_name"));
//                    user.setLastName(jsonObject.optString("last_name"));
                user.setEmail(jsonObject.optString("email"));
                if (jsonObject.optString("picture").startsWith("http"))
                    user.setImg(jsonObject.optString("picture"));
                else
                    user.setImg(URLHelper.BASE + "storage/app/public/" + jsonObject.optString("picture"));
                user.setRating(jsonObject.optString("rating"));
                user.setMobile(jsonObject.optString("mobile"));
                Intent intent = new Intent(context, ShowProfile.class);
                intent.putExtra("user", user);
                startActivity(intent);
            });

            if (response.optJSONObject(0).optJSONObject("user").optString("rating") != null &&
                    !response.optJSONObject(0).optJSONObject("user").optString("rating").equalsIgnoreCase(""))
                tripProviderRating.setRating(Float.parseFloat(response.optJSONObject(0).optJSONObject("user").optString("rating")));
            else {
                tripProviderRating.setRating(0);
            }

            /*if (!response.optJSONObject(0).optString("rating").equalsIgnoreCase("null") &&
                    !response.optJSONObject(0).optJSONObject("rating").optString("user_comment").equalsIgnoreCase("")) {
                tripComments.setText(response.optJSONObject(0).optJSONObject("rating").optString("user_comment"));
            } else {
                tripComments.setText(getString(R.string.no_comments));
            }*/
            tripProviderName.setText(response.optJSONObject(0).optJSONObject("user").optString("first_name"));
            if (response.optJSONObject(0).optString("s_address") == null || response.optJSONObject(0).optString("d_address") == null || response.optJSONObject(0).optString("d_address").equals("") || response.optJSONObject(0).optString("s_address").equals("")) {
                sourceAndDestinationLayout.setVisibility(View.GONE);

            } else {
                tripSource.setText(response.optJSONObject(0).optString("s_address"));
                tripDestination.setText(response.optJSONObject(0).optString("d_address"));
            }
            parentLayout.setVisibility(View.VISIBLE);
        }
    }

    public void getRequestDetails() {

        customDialog = new CustomDialog(context);
        customDialog.setCancelable(false);
        customDialog.show();

        JsonArrayRequest jsonArrayRequest = new
                JsonArrayRequest(URLHelper.GET_HISTORY_DETAILS_API + "?request_id=" +
                        jsonObject.optString("id"), response -> {

                    Log.e("TAG", "RESPONSE: " + response);
                    utils.print("Get Trip details", response.toString());
                    if (response != null && response.length() > 0) {
                        Picasso.get().load(response.optJSONObject(0).optString("static_map"))
                                .placeholder(R.drawable.placeholder).error(R.drawable.placeholder).into(tripImg);
                        if (!response.optJSONObject(0).optString("payment").equalsIgnoreCase("null")) {
                            Log.e("History Details", "onResponse: Currency" + SharedHelper.getKey(context, "currency"));
                            tripAmount.setText(SharedHelper.getKey(context, "currency") + "" + response.optJSONObject(0).optJSONObject("payment").optString("total"));
                        } else {
                            tripAmount.setText(SharedHelper.getKey(context, "currency") + "" + "0");
                        }
                        String form;
                        if (tag.equalsIgnoreCase("past_trips")) {
                            form = response.optJSONObject(0).optString("assigned_at");
                        } else {
                            form = response.optJSONObject(0).optString("schedule_at");
                        }
                        try {
                            tripDate.setText(getDate(form) + "th " + getMonth(form) + " " + getYear(form) + "\n" + getTime(form));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if (response.optJSONObject(0).optString("payment_mode").equalsIgnoreCase("CASH")) {
                            paymentType.setText(getString(R.string.cash));
                        } else {
                            paymentType.setText(getString(R.string.card));
                        }

                        if (response.optJSONObject(0).optString("payment_mode").equalsIgnoreCase("CASH")) {
                            paymentTypeImg.setImageResource(R.drawable.money1);
                        } else {
                            paymentTypeImg.setImageResource(R.drawable.visa_icon);
                        }

                        if (response.optJSONObject(0).optJSONObject("user").optString("picture") != null) {
                            if (response.optJSONObject(0).optJSONObject("user").optString("picture").startsWith("http"))
                                Picasso.get().load(response.optJSONObject(0).optJSONObject("user").optString("picture")).placeholder(R.drawable.ic_dummy_user).error(R.drawable.ic_dummy_user).into(tripProviderImg);
                            else
                                Picasso.get().load(URLHelper.BASE + "storage/app/public/" + response.optJSONObject(0).optJSONObject("user").optString("picture")).placeholder(R.drawable.ic_dummy_user).error(R.drawable.ic_dummy_user).into(tripProviderImg);
                        }


                        final JSONArray res = response;
                        tripProviderImg.setOnClickListener(v -> {
                            JSONObject jsonObject = res.optJSONObject(0).optJSONObject("user");

                            User user = new User();
                            user.setFirstName(jsonObject.optString("first_name"));
                            //                            user.setLastName(jsonObject.optString("last_name"));
                            user.setEmail(jsonObject.optString("email"));
                            if (jsonObject.optString("picture").startsWith("http"))
                                user.setImg(jsonObject.optString("picture"));
                            else
                                user.setImg(URLHelper.BASE + "storage/app/public/" + jsonObject.optString("picture"));
                            user.setRating(jsonObject.optString("rating"));
                            user.setMobile(jsonObject.optString("mobile"));
                            Intent intent = new Intent(context, ShowProfile.class);
                            intent.putExtra("user", user);
                            startActivity(intent);
                        });

                        tripId.setText(response.optJSONObject(0).optString("booking_id"));

                        if (response.optJSONObject(0).optJSONObject("user").optString("rating") != null &&
                                !response.optJSONObject(0).optJSONObject("user").optString("rating").equalsIgnoreCase(""))
                            tripProviderRating.setRating(Float.parseFloat(response.optJSONObject(0).optJSONObject("user").optString("rating")));
                        else {
                            tripProviderRating.setRating(0);
                        }

                        if (!response.optJSONObject(0).optString("rating").equalsIgnoreCase("null") &&
                                !response.optJSONObject(0).optJSONObject("rating").optString("user_comment").equalsIgnoreCase("")
                                && response.optJSONObject(0).optString("rating") != null) {
                            if (response.optJSONObject(0).optJSONObject("rating").optString("user_comment") != null &&
                                    response.optJSONObject(0).optJSONObject("rating").optString("user_comment") != "null") {
                                tripComments.setText(response.optJSONObject(0).optJSONObject("rating").optString("user_comment"));
                            } else {
                                tripComments.setText(getString(R.string.no_comments));
                            }
                        } else {
                            tripComments.setText(getString(R.string.no_comments));
                        }
                        tripProviderName.setText(response.optJSONObject(0).optJSONObject("user").optString("first_name"));
                        if (response.optJSONObject(0).optString("s_address") == null || response.optJSONObject(0).optString("d_address") == null || response.optJSONObject(0).optString("d_address").equals("") || response.optJSONObject(0).optString("s_address").equals("")) {
                            sourceAndDestinationLayout.setVisibility(View.GONE);

                        } else {
                            tripSource.setText(response.optJSONObject(0).optString("s_address"));
                            tripDestination.setText(response.optJSONObject(0).optString("d_address"));
                        }
                        parentLayout.setVisibility(View.VISIBLE);
                    }
                    customDialog.dismiss();

                }, error -> {
                    customDialog.dismiss();
                    displayMessage(getString(R.string.something_went_wrong));
                }) {
                    @Override
                    public Map<String, String> getHeaders() {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("X-Requested-With", "XMLHttpRequest");
                        headers.put("Authorization", "Bearer " + SharedHelper.getKey(context, "access_token"));
                        utils.print("Token", "" + SharedHelper.getKey(context, "access_token"));
                        return headers;
                    }
                };

        ClassLuxApp.getInstance().addToRequestQueue(jsonArrayRequest);
    }


    public void cancelRequest() {

        customDialog = new CustomDialog(context);
        customDialog.setCancelable(false);
        customDialog.show();
        JSONObject object = new JSONObject();
        try {
            object.put("id", jsonObject.optString("id"));
            utils.print("", "request_id" + jsonObject.optString("id"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new
                JsonObjectRequest(Request.Method.POST, URLHelper.CANCEL_REQUEST_API, object,
                        response -> {
                            utils.print("CancelRequestResponse", response.toString());
                            customDialog.dismiss();
                            finish();
                        }, error -> {
                    customDialog.dismiss();
                    displayMessage(getString(R.string.something_went_wrong));
                }) {
                    @Override
                    public Map<String, String> getHeaders() {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("X-Requested-With", "XMLHttpRequest");
                        headers.put("Authorization", "Bearer " + SharedHelper.getKey(context, "access_token"));
                        utils.print("", "Access_Token" + SharedHelper.getKey(context, "access_token"));
                        return headers;
                    }
                };

        ClassLuxApp.getInstance().addToRequestQueue(jsonObjectRequest);
    }


    public void displayMessage(String toastString) {
        Toasty.info(this, toastString, Toast.LENGTH_SHORT, true).show();
    }

    public void GoToBeginActivity() {
        SharedHelper.putKey(activity, "loggedIn", getString(R.string.False));
        Intent mainIntent = new Intent(activity, Login.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainIntent);
        activity.finish();
    }

    public void getUpcomingDetails() {

        customDialog = new CustomDialog(context);
        customDialog.setCancelable(false);
        customDialog.show();

        Toast.makeText(getApplicationContext(), "ID : "+jsonObject.optString("id"), Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(), "Intent ID : "+request_id, Toast.LENGTH_SHORT).show();

        JsonArrayRequest jsonArrayRequest = new
                JsonArrayRequest(URLHelper.UPCOMING_TRIP_DETAILS + "?request_id=" +
                        jsonObject.optString("id"),
                        response -> {
//                            setDetails(response);

                            history_response = response.toString();
                            Log.e("Trip Details", history_response);
                            utils.print("Trip Details", response.toString());
                            customDialog.dismiss();
                            parentLayout.setVisibility(View.VISIBLE);

                        }, error -> {
                    customDialog.dismiss();
                    displayMessage(getString(R.string.something_went_wrong));
                }) {
                    @Override
                    public Map<String, String> getHeaders() {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("X-Requested-With", "XMLHttpRequest");
                        headers.put("Authorization", "Bearer " + SharedHelper.getKey(context, "access_token"));
                        return headers;
                    }
                };

        ClassLuxApp.getInstance().addToRequestQueue(jsonArrayRequest);



//
//        StringRequest request = new StringRequest(Request.Method.POST, URLHelper.UPCOMMING_TRIPS_DETAILS_ONE, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//
//
//                try {
//                    JSONArray jsonArray = new JSONArray(response);
//                    System.out.println("size : " + jsonArray.length());
//                    System.out.println("data : " + jsonArray);
//                    System.out.println("data : " + jsonArray.getString(0));
//
//                    history_response = jsonArray.toString();
//                    Log.e("Trip Details", history_response);
//                    utils.print("Trip Details", jsonArray.toString());
//                    customDialog.dismiss();
//                    parentLayout.setVisibility(View.VISIBLE);
//
//                    Log.v("GetPaymentList Details", jsonArray.toString());
//                    if (jsonArray != null && jsonArray.length() > 0) {
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            if (jsonArray.optJSONObject(i) != null) {
//
//
//
//
//
//                            }
//                        }
//
//                    }
//                    if ((customDialog != null) && (customDialog.isShowing()))
//                        customDialog.dismiss();
//                    parentLayout.setVisibility(View.VISIBLE);
//
//                } catch (JSONException e) {
//
//                    displayMessage(e.toString());
//
//                }
//
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(HistoryDetails.this, "Error Found", Toast.LENGTH_SHORT).show();
//            }
//
//        }) {
//
//
//            @Override
//            public Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<>();
//                params.put("request_id", jsonObject.optString("id"));
//                return params;
//            }
//
//            @Override
//            public Map<String, String> getHeaders() {
//                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("X-Requested-With", "XMLHttpRequest");
//                headers.put("Authorization", "Bearer " + SharedHelper.getKey(getApplicationContext(), "access_token"));
//                return headers;
//            }
//
//        };
//
//        ClassLuxApp.getInstance().addToRequestQueue(request);



    }


    private String getMonth(String date) throws ParseException {
        Date d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        String monthName = new SimpleDateFormat("MMM").format(cal.getTime());
        return monthName;
    }

    private String getDate(String date) throws ParseException {
        Date d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        String dateName = new SimpleDateFormat("dd").format(cal.getTime());
        return dateName;
    }

    private String getYear(String date) throws ParseException {
        Date d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        String yearName = new SimpleDateFormat("yyyy").format(cal.getTime());
        return yearName;
    }

    private String getTime(String date) throws ParseException {
        Date d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        String timeName = new SimpleDateFormat("hh:mm a").format(cal.getTime());
        return timeName;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
