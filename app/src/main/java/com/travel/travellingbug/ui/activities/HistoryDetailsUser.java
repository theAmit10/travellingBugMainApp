package com.travel.travellingbug.ui.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;
import com.travel.travellingbug.ClassLuxApp;
import com.travel.travellingbug.R;
import com.travel.travellingbug.helper.ConnectionHelper;
import com.travel.travellingbug.helper.CustomDialog;
import com.travel.travellingbug.helper.SharedHelper;
import com.travel.travellingbug.helper.URLHelper;
import com.travel.travellingbug.models.Driver;
import com.travel.travellingbug.ui.activities.login.SignUp;
import com.travel.travellingbug.utills.MyBoldTextView;
import com.travel.travellingbug.utills.MyButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import es.dmoral.toasty.Toasty;


public class HistoryDetailsUser extends AppCompatActivity {

    public JSONObject jsonObject;
    Activity activity;
    Context context;
    Boolean isInternet;
    ConnectionHelper helper;
    CustomDialog customDialog;
    MyBoldTextView tripAmount;
    MyBoldTextView tripDate;
    MyBoldTextView paymentType;
    MyBoldTextView booking_id;
    MyBoldTextView tripComments;
    MyBoldTextView tripProviderName;
    MyBoldTextView tripSource;
    MyBoldTextView lblTotalPrice;
    MyBoldTextView lblBookingID;
    MyBoldTextView tripDestination;
    MyBoldTextView lblTitle;
    MyBoldTextView lblBasePrice;
    MyBoldTextView lblDistancePrice;
    MyBoldTextView lblTaxPrice;
    ImageView tripImg, tripProviderImg, paymentTypeImg;
    RatingBar tripProviderRating;
    LinearLayout sourceAndDestinationLayout, lnrComments, lnrUpcomingLayout;
    View viewLayout;
    ImageView backArrow;
    LinearLayout parentLayout;
    LinearLayout profileLayout;
    LinearLayout lnrInvoice, lnrInvoiceSub;
    String tag = "";
    MyButton btnCancelRide;
    Driver driver;
    String reason = "";

    String request_id = "";
    String user_id = "";

    Button btnViewInvoice, btnCall;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SharedHelper.getKey(this, "selectedlanguage").contains("ar")) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }

        setContentView(R.layout.activity_history_details_user);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        findViewByIdAndInitialize();


        try {
            Intent intent = getIntent();
            String post_details = intent.getStringExtra("post_value");
            tag = intent.getStringExtra("tag");
            request_id = getIntent().getStringExtra("request_id");
            user_id = getIntent().getStringExtra("user_id");
            jsonObject = new JSONObject(post_details);
        } catch (Exception e) {
            jsonObject = null;
        }

        if (jsonObject != null) {

            if (!tag.equalsIgnoreCase("past_trips")) {
                btnCancelRide.setVisibility(View.GONE);
                lnrComments.setVisibility(View.VISIBLE);
                lnrUpcomingLayout.setVisibility(View.GONE);
                getRequestDetails();
                lblTitle.setText(getString(R.string.past_trips));
            } else {
                lnrUpcomingLayout.setVisibility(View.VISIBLE);
                btnViewInvoice.setVisibility(View.GONE);
                btnCancelRide.setVisibility(View.VISIBLE);
                lnrComments.setVisibility(View.GONE);
//                getUpcomingDetails();
                getRequestDetails();
                lblTitle.setText(getString(R.string.upcomeng_rides));
            }
        }
        profileLayout.setOnClickListener(v -> {
            Intent intent = new Intent(HistoryDetailsUser.this, ShowProfile.class);
            intent.putExtra("driver", driver);
            startActivity(intent);
        });

        backArrow.setOnClickListener(view -> onBackPressed());
    }

    public void findViewByIdAndInitialize() {
        activity = HistoryDetailsUser.this;
        context = HistoryDetailsUser.this;
        helper = new ConnectionHelper(activity);
        isInternet = helper.isConnectingToInternet();
        parentLayout = findViewById(R.id.parentLayout);
        profileLayout = findViewById(R.id.profile_detail_layout);
        lnrInvoice = findViewById(R.id.lnrInvoice);
        lnrInvoiceSub = findViewById(R.id.lnrInvoiceSub);
        parentLayout.setVisibility(View.GONE);
        backArrow = findViewById(R.id.backArrow);
        tripAmount = findViewById(R.id.tripAmount);
        tripDate = findViewById(R.id.tripDate);
        paymentType = findViewById(R.id.paymentType);
        booking_id = findViewById(R.id.booking_id);
        paymentTypeImg = findViewById(R.id.paymentTypeImg);
        tripProviderImg = findViewById(R.id.tripProviderImg);
        tripImg = findViewById(R.id.tripImg);
        tripComments = findViewById(R.id.tripComments);
        tripProviderName = findViewById(R.id.tripProviderName);
        tripProviderRating = findViewById(R.id.tripProviderRating);
        tripSource = findViewById(R.id.tripSource);
        tripDestination = findViewById(R.id.tripDestination);
        lblBookingID = findViewById(R.id.lblBookingID);
        lblBasePrice = findViewById(R.id.lblBasePrice);
        lblTaxPrice = findViewById(R.id.lblTaxPrice);
        lblDistancePrice = findViewById(R.id.lblDistancePrice);
        lblTotalPrice = findViewById(R.id.lblTotalPrice);
        lblTitle = findViewById(R.id.lblTitle);
        btnCancelRide = findViewById(R.id.btnCancelRide);
        sourceAndDestinationLayout = findViewById(R.id.sourceAndDestinationLayout);
        lnrComments = findViewById(R.id.lnrComments);
        // viewLayout = (View) findViewById(R.id.ViewLayout);

        lnrUpcomingLayout = findViewById(R.id.lnrUpcomingLayout);
        btnViewInvoice = findViewById(R.id.btnViewInvoice);
        btnCall = findViewById(R.id.btnCall);

        btnCancelRide.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setIcon(R.drawable.app_logo_org)
                    .setTitle(R.string.app_name)
                    .setMessage(getString(R.string.cencel_request))
                    .setCancelable(false)
                    .setPositiveButton("YES", (dialog, id) -> {
                        dialog.dismiss();
                        showreasonDialog();
                    })
                    .setNegativeButton("NO", (dialog, id) -> dialog.dismiss());
            AlertDialog alert = builder.create();
            alert.show();
        });

        btnViewInvoice.setOnClickListener(v -> lnrInvoice.setVisibility(View.VISIBLE));

        lnrInvoice.setOnClickListener(v -> lnrInvoice.setVisibility(View.GONE));

        lnrInvoiceSub.setOnClickListener(v -> {
        });

        btnCall.setOnClickListener(v -> {
            if (driver.getMobile() != null && !driver.getMobile().equalsIgnoreCase("null") && driver.getMobile().length() > 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 2);
                } else {
                    Intent intentCall = new Intent(Intent.ACTION_CALL);
                    intentCall.setData(Uri.parse("tel:" + driver.getMobile()));
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    startActivity(intentCall);
                }
            } else {
                displayMessage(getString(R.string.user_no_mobile));
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Permission Granted
            //Toast.makeText(SignInActivity.this, "PERMISSION_GRANTED", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + driver.getMobile()));
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            startActivity(intent);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 1);
            }
        }
    }

    private void updateStatusForSingleUserRide(String rideId, String status) {
        customDialog = new CustomDialog(context);
        customDialog.setCancelable(false);
        if (customDialog != null)
            customDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, URLHelper.CHANGE_STATUS_BY_USER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if ((customDialog != null) && (customDialog.isShowing()))
                    customDialog.dismiss();
                finish();

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (response != null) {
                        System.out.println("data : " + jsonObject.toString());
                        Toast.makeText(HistoryDetailsUser.this, "Successfully cancelled", Toast.LENGTH_SHORT).show();


                    }

                } catch (JSONException e) {
                    displayMessage(e.toString());
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error Found", Toast.LENGTH_SHORT).show();
                if ((customDialog != null) && (customDialog.isShowing()))
                    customDialog.dismiss();
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

    private void showreasonDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.cancel_dialog, null);
        final EditText reasonEtxt = view.findViewById(R.id.reason_etxt);
        Button submitBtn = view.findViewById(R.id.submit_btn);
        builder.setIcon(R.drawable.app_logo_org)
                .setTitle(R.string.app_name)
                .setView(view)
                .setCancelable(true);
        submitBtn.setOnClickListener(v -> {
            reason = reasonEtxt.getText().toString();
//            cancelRequest();

            updateStatusForSingleUserRide(request_id, "CANCELLED");

        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void getRequestDetails() {

        customDialog = new CustomDialog(context);
        customDialog.setCancelable(false);
        if (customDialog != null)
            customDialog.show();

//        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URLHelper.GET_HISTORY_DETAILS_API + "?request_id=" + jsonObject.optString("id"), response -> {
//
//            Log.v("GetPaymentList Details", response.toString());
//            if (response != null && response.length() > 0) {
//                if (response.optJSONObject(0) != null) {
//                    Picasso.get().load(response.optJSONObject(0).optString("static_map")).placeholder(R.drawable.placeholder).error(R.drawable.placeholder).into(tripImg);
//                    Log.e("History Details", "onResponse: Currency" + SharedHelper.getKey(context, "currency"));
//                    JSONObject providerObj = response.optJSONObject(0).optJSONObject("provider");
//                    if (providerObj != null) {
//                        driver = new Driver();
//                        driver.setFname(providerObj.optString("first_name"));
//                        driver.setLname(providerObj.optString("last_name"));
//                        driver.setMobile(providerObj.optString("mobile"));
//                        driver.setEmail(providerObj.optString("email"));
//                        driver.setImg(providerObj.optString("avatar"));
//                        driver.setRating(providerObj.optString("rating"));
//                    }
//                    if (response.optJSONObject(0).optString("booking_id") != null &&
//                            !response.optJSONObject(0).optString("booking_id").equalsIgnoreCase("")) {
//                        booking_id.setText(response.optJSONObject(0).optString("booking_id"));
//                        lblBookingID.setText(response.optJSONObject(0).optString("booking_id"));
//                    }
//                    String form;
//                    if (tag.equalsIgnoreCase("past_trips")) {
//                        form = response.optJSONObject(0).optString("assigned_at");
//                    } else {
//                        form = response.optJSONObject(0).optString("schedule_at");
//                    }
//                    if (response.optJSONObject(0).optJSONObject("payment") != null && response.optJSONObject(0).optJSONObject("payment").optString("total") != null &&
//                            !response.optJSONObject(0).optJSONObject("payment").optString("total").equalsIgnoreCase("")) {
//                        tripAmount.setText(SharedHelper.getKey(context, "currency") + "" + response.optJSONObject(0).optJSONObject("payment").optString("total"));
//                        response.optJSONObject(0).optJSONObject("payment");
//                        lblBasePrice.setText((SharedHelper.getKey(context, "currency") + ""
//                                + response.optJSONObject(0).optJSONObject("payment").optString("fixed")));
//                        lblDistancePrice.setText((SharedHelper.getKey(context, "currency") + ""
//                                + response.optJSONObject(0).optJSONObject("payment").optString("distance")));
//                        lblTaxPrice.setText((SharedHelper.getKey(context, "currency") + ""
//                                + response.optJSONObject(0).optJSONObject("payment").optString("tax")));
//                        lblTotalPrice.setText((SharedHelper.getKey(context, "currency") + ""
//                                + response.optJSONObject(0).optJSONObject("payment").optString("total" +
//                                "")));
//                    } else {
//                        tripAmount.setVisibility(View.GONE);
//                    }
//                    try {
//                        tripDate.setText(getDate(form) + "th " + getMonth(form) + " " + getYear(form) + "\n" + getTime(form));
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//
//                    paymentType.setText(response.optJSONObject(0).optString("payment_mode"));
//                    if (response.optJSONObject(0).optString("payment_mode").equalsIgnoreCase("CASH")) {
//                        paymentTypeImg.setImageResource(R.drawable.money1);
//                    } else {
//                        paymentTypeImg.setImageResource(R.drawable.visa_icon);
//                    }
//                    Picasso.get().load(URLHelper.BASE + "storage/app/public/" + response.optJSONObject(0).optJSONObject("provider").optString("avatar"))
//                            .placeholder(R.drawable.ic_dummy_user).error(R.drawable.ic_dummy_user).into(tripProviderImg);
//                    if (response.optJSONObject(0).optJSONObject("rating") != null &&
//                            !response.optJSONObject(0).optJSONObject("rating").optString("provider_comment").equalsIgnoreCase("")) {
//                        tripComments.setText(response.optJSONObject(0).optJSONObject("rating").optString("provider_comment", ""));
//                    } else {
//                        tripComments.setText(getString(R.string.no_comments));
//                    }
//                    if (response.optJSONObject(0).optJSONObject("provider").optString("rating") != null
//                            && !response.optJSONObject(0).optJSONObject("provider").optString("rating").equalsIgnoreCase("")) {
//                        tripProviderRating.setRating(Float.parseFloat(response.optJSONObject(0).optJSONObject("provider").optString("rating")));
//                    } else {
//                        tripProviderRating.setRating(0);
//                    }
//                    tripProviderName.setText(response.optJSONObject(0).optJSONObject("provider").optString("first_name"));
//                    if (response.optJSONObject(0).optString("s_address") == null || response.optJSONObject(0).optString("d_address") == null || response.optJSONObject(0).optString("d_address").equals("") || response.optJSONObject(0).optString("s_address").equals("")) {
//                        sourceAndDestinationLayout.setVisibility(View.GONE);
//                        //   viewLayout.setVisibility(View.GONE);
//                    } else {
//                        tripSource.setText(response.optJSONObject(0).optString("s_address"));
//                        tripDestination.setText(response.optJSONObject(0).optString("d_address"));
//                    }
//
//                }
//            }
//            if ((customDialog != null) && (customDialog.isShowing()))
//                customDialog.dismiss();
//            parentLayout.setVisibility(View.VISIBLE);
//
//        }, error -> {
//            if ((customDialog != null) && (customDialog.isShowing()))
//                customDialog.dismiss();
//            displayMessage(getString(R.string.something_went_wrong));
//        }) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("X-Requested-With", "XMLHttpRequest");
//                headers.put("Authorization", "" + SharedHelper.getKey(context, "token_type") + " " + SharedHelper.getKey(context, "access_token"));
//                return headers;
//            }
//        };
//
//        ClassLuxApp.getInstance().addToRequestQueue(jsonArrayRequest);


        // Started


        StringRequest request = new StringRequest(Request.Method.POST, URLHelper.UPCOMMING_TRIPS_DETAILS_ONE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {
                    JSONArray jsonArray = new JSONArray(response);
                    System.out.println("size : " + jsonArray.length());
                    System.out.println("data : " + jsonArray);
                    System.out.println("data : " + jsonArray.getString(0));

                    Log.v("GetPaymentList Details", jsonArray.toString());
                    if (jsonArray != null && jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            if (jsonArray.optJSONObject(i) != null) {
//                            Picasso.get().load(jsonArray.optJSONObject(0).optString("static_map")).placeholder(R.drawable.placeholder).error(R.drawable.placeholder).into(tripImg);
                                Log.e("History Details", "onResponse: Currency" + SharedHelper.getKey(context, "currency"));



                                JSONObject providerObj = jsonArray.optJSONObject(i).optJSONObject("provider");
                                if (providerObj != null) {
                                    driver = new Driver();
                                    driver.setFname(providerObj.optString("first_name"));
                                    driver.setLname(providerObj.optString("last_name"));
                                    driver.setMobile(providerObj.optString("mobile"));
                                    driver.setEmail(providerObj.optString("email"));
                                    driver.setImg(providerObj.optString("avatar"));
                                    driver.setRating(providerObj.optString("rating"));

                                    Picasso.get().load(URLHelper.BASE + "storage/app/public/" + providerObj.optString("avatar"))
                                            .placeholder(R.drawable.ic_dummy_user).error(R.drawable.ic_dummy_user).into(tripProviderImg);


                                }
                                if (jsonArray.optJSONObject(i).optString("booking_id") != null &&
                                        !jsonArray.optJSONObject(i).optString("booking_id").equalsIgnoreCase("")) {
                                    booking_id.setText(jsonArray.optJSONObject(i).optString("booking_id"));
                                    lblBookingID.setText(jsonArray.optJSONObject(i).optString("booking_id"));
                                }

                                if(jsonArray.optJSONObject(i).optString("status").equalsIgnoreCase("CANCELLED")){
                                    try {
                                        btnCancelRide.setText("Service Cancelled");
                                        btnCall.setVisibility(View.GONE);
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }


                                JSONArray filterJsonArray = jsonArray.optJSONObject(i).optJSONArray("filters");
                                for(int j=0; j<filterJsonArray.length(); j++){
                                    JSONObject filterJsonObj = filterJsonArray.getJSONObject(i);
                                    System.out.println("filter cancelled status user_id given 1 : "+user_id);

                                    if(filterJsonObj.optString("user_id").equalsIgnoreCase(user_id)){
                                        System.out.println("filter cancelled status user_id given 2 : "+user_id);
                                        System.out.println("filter cancelled status user_id found 2 : "+filterJsonObj.optString("user_id"));
                                        if(filterJsonObj.optString("status").equalsIgnoreCase("CANCELLED")){
                                            try {
                                                btnCancelRide.setText("Service Cancelled");
                                                btnCall.setVisibility(View.GONE);
                                                System.out.println("filter cancelled status : "+filterJsonObj.optString("status"));
                                                System.out.println("filter cancelled status user_id given : "+user_id);
                                                System.out.println("filter cancelled status user_id found : "+filterJsonObj.optString("user_id"));
                                                Toast.makeText(getApplicationContext(), "Service Cancelled", Toast.LENGTH_SHORT).show();
                                            }catch (Exception e){
                                                e.printStackTrace();
                                            }
                                        }

                                    }

                                }



                                String form;
                                if (tag.equalsIgnoreCase("past_trips")) {
                                    form = jsonArray.optJSONObject(i).optString("schedule_at");
                                } else {
                                    form = jsonArray.optJSONObject(i).optString("schedule_at");
                                }
                                if (jsonArray.optJSONObject(i).optJSONObject("payment") != null && jsonArray.optJSONObject(i).optJSONObject("payment").optString("total") != null &&
                                        !jsonArray.optJSONObject(i).optJSONObject("payment").optString("total").equalsIgnoreCase("")) {
                                    tripAmount.setText(SharedHelper.getKey(context, "currency") + "" + jsonArray.optJSONObject(i).optJSONObject("payment").optString("total"));
                                    jsonArray.optJSONObject(i).optJSONObject("payment");
                                    lblBasePrice.setText((SharedHelper.getKey(context, "currency") + ""
                                            + jsonArray.optJSONObject(i).optJSONObject("payment").optString("fixed")));
                                    lblDistancePrice.setText((SharedHelper.getKey(context, "currency") + ""
                                            + jsonArray.optJSONObject(i).optJSONObject("payment").optString("distance")));
                                    lblTaxPrice.setText((SharedHelper.getKey(context, "currency") + ""
                                            + jsonArray.optJSONObject(i).optJSONObject("payment").optString("tax")));
                                    lblTotalPrice.setText((SharedHelper.getKey(context, "currency") + ""
                                            + jsonArray.optJSONObject(i).optJSONObject("payment").optString("total" +
                                            "")));
                                } else {
                                    tripAmount.setVisibility(View.GONE);
                                }
                                try {
                                    tripDate.setText(getDate(form) + "th " + getMonth(form) + " " + getYear(form) + "\n" + getTime(form));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                paymentType.setText(jsonArray.optJSONObject(i).optString("payment_mode"));
                                if (jsonArray.optJSONObject(i).optString("payment_mode").equalsIgnoreCase("CASH")) {
                                    paymentTypeImg.setImageResource(R.drawable.money1);
                                } else {
                                    paymentTypeImg.setImageResource(R.drawable.visa_icon);
                                }


//                                JSONArray filterJsonArray = jsonArray.optJSONObject(i).optJSONArray("filters");

//                                if(filterJsonArray.length() > 0){
//
//                                }





//                                Picasso.get().load(URLHelper.BASE + "storage/app/public/" + jsonArray.optJSONObject(i).optJSONObject("provider").optString("avatar"))
//                                        .placeholder(R.drawable.ic_dummy_user).error(R.drawable.ic_dummy_user).into(tripProviderImg);
//

                                if (jsonArray.optJSONObject(i).optJSONObject("rating") != null &&
                                        !jsonArray.optJSONObject(i).optJSONObject("rating").optString("provider_comment").equalsIgnoreCase("")) {
                                    tripComments.setText(jsonArray.optJSONObject(i).optJSONObject("rating").optString("provider_comment", ""));
                                } else {
                                    tripComments.setText(getString(R.string.no_comments));
                                }

                                if (jsonArray.optJSONObject(i).optJSONObject("provider").optString("rating") != null
                                        && !jsonArray.optJSONObject(i).optJSONObject("provider").optString("rating").equalsIgnoreCase("")) {
                                    tripProviderRating.setRating(Float.parseFloat(jsonArray.optJSONObject(i).optJSONObject("provider").optString("rating")));
                                } else {
                                    tripProviderRating.setRating(i);
                                }

                                tripProviderName.setText(jsonArray.optJSONObject(i).optJSONObject("provider").optString("first_name"));



                                if (jsonArray.optJSONObject(i).optString("s_address") == null || jsonArray.optJSONObject(i).optString("d_address") == null || jsonArray.optJSONObject(i).optString("d_address").equals("") || jsonArray.optJSONObject(i).optString("s_address").equals("")) {
                                    sourceAndDestinationLayout.setVisibility(View.GONE);
                                    //   viewLayout.setVisibility(View.GONE);
                                } else {
                                    tripSource.setText(jsonArray.optJSONObject(i).optString("s_address"));
                                    tripDestination.setText(jsonArray.optJSONObject(i).optString("d_address"));
                                }

                            }
                        }

                    }
                    if ((customDialog != null) && (customDialog.isShowing()))
                        customDialog.dismiss();
                    parentLayout.setVisibility(View.VISIBLE);


//                ###########################


//                    if (response != null) {
//                        System.out.println("data : "+jsonArray.getString(0));
//                        upcomingsAdapter = new FindRidesActivity.UpcomingsAdapter(jsonArray);
//                        //  recyclerView.setHasFixedSize(true);
//                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext().getApplicationContext());
//                        recyclerView.setLayoutManager(mLayoutManager);
//                        recyclerView.setItemAnimator(new DefaultItemAnimator());
//                        if (upcomingsAdapter != null && upcomingsAdapter.getItemCount() > 0) {
//                            recyclerView.setVisibility(View.VISIBLE);
//                            errorLayout.setVisibility(View.GONE);
//                            recyclerView.setAdapter(upcomingsAdapter);
//                        } else {
////                    errorLayout.setVisibility(View.VISIBLE);
//                            recyclerView.setVisibility(View.GONE);
//                        }
//
//                    } else {
//                        errorLayout.setVisibility(View.VISIBLE);
//                        recyclerView.setVisibility(View.GONE);
//                    }

//                    for(int i=0 ; i<jsonArray.length(); i++){
//                        JSONObject jsonObject = jsonArray.getJSONObject(i);
//                        System.out.println("data : "+jsonObject.toString());
//                        System.out.println("data : "+jsonObject.getString("s_address"));
//                    }

                } catch (JSONException e) {
//                    throw new RuntimeException(e);
                    displayMessage(e.toString());
//                    errorLayout.setVisibility(View.VISIBLE);
//                    recyclerView.setVisibility(View.GONE);
                }

//                Toast.makeText(FindRidesActivity.this, "Data Found succesfully..", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(HistoryDetailsUser.this, "Error Found", Toast.LENGTH_SHORT).show();
            }

        }) {


            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("request_id", request_id);
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

    public void getUpcomingDetails() {

        customDialog = new CustomDialog(context);
        customDialog.setCancelable(false);
        if (customDialog != null)
            customDialog.show();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URLHelper.UPCOMING_TRIP_DETAILS + "?request_id=" + jsonObject.optString("id"), response -> {

            Log.v("GetPaymentList", response.toString());
            if (response != null && response.length() > 0) {
                if (response.optJSONObject(0) != null) {
                    Picasso.get().load(response.optJSONObject(0).optString("static_map")).placeholder(R.drawable.placeholder).error(R.drawable.placeholder).into(tripImg);
//                    tripDate.setText(response.optJSONObject(0).optString("assigned_at"));
                    paymentType.setText(response.optJSONObject(0).optString("payment_mode"));
                    String form = response.optJSONObject(0).optString("schedule_at");
                    JSONObject providerObj = response.optJSONObject(0).optJSONObject("provider");

                    tripProviderRating.setRating(Float.parseFloat(providerObj.optString("rating")));


                    if (response.optJSONObject(0).optString("booking_id") != null &&
                            !response.optJSONObject(0).optString("booking_id").equalsIgnoreCase("")) {
                        booking_id.setText(response.optJSONObject(0).optString("booking_id"));
                    }
                    if (providerObj != null) {
                        driver = new Driver();
                        driver.setFname(providerObj.optString("first_name"));
                        driver.setLname(providerObj.optString("last_name"));
                        driver.setMobile(providerObj.optString("mobile"));
                        driver.setEmail(providerObj.optString("email"));
                        driver.setImg(providerObj.optString("avatar"));
                        driver.setRating(providerObj.optString("rating"));
                    }
                    try {
                        tripDate.setText(getDate(form) + "th " + getMonth(form) + " " + getYear(form) + "\n" + getTime(form));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (response.optJSONObject(0).optString("payment_mode").equalsIgnoreCase("CASH")) {
                        paymentTypeImg.setImageResource(R.drawable.money1);
                    } else {
                        paymentTypeImg.setImageResource(R.drawable.visa_icon);
                    }



                    if (response.optJSONObject(0).optJSONObject("provider") != null) {
                        if (response.optJSONObject(0).optJSONObject("provider").optString("avatar") != null)
                            Picasso.get().load(URLHelper.BASE + "storage/app/public/" + response.optJSONObject(0).optJSONObject("provider").optString("avatar"))
                                    .placeholder(R.drawable.car_select).error(R.drawable.car_select).into(tripProviderImg);
                        tripProviderRating.setRating(Float.parseFloat(response.optJSONObject(0).optJSONObject("provider").optString("rating")));
                        tripProviderName.setText(response.optJSONObject(0).optJSONObject("provider").optString("first_name"));
                    } else {
                        btnCall.setVisibility(View.GONE);
                        tripProviderName.setText("Not assigned yet");
                        tripProviderImg.setBackground(getDrawable(R.drawable.ic_dummy_user));
                    }
                    if (response.optJSONObject(0).optString("s_address") == null || response.optJSONObject(0).optString("d_address") == null || response.optJSONObject(0).optString("d_address").equals("") || response.optJSONObject(0).optString("s_address").equals("")) {
                        sourceAndDestinationLayout.setVisibility(View.GONE);
                        // viewLayout.setVisibility(View.GONE);
                    } else {
                        tripSource.setText(response.optJSONObject(0).optString("s_address"));
                        tripDestination.setText(response.optJSONObject(0).optString("d_address"));
                    }

                    try {
                        JSONObject serviceObj = response.optJSONObject(0).optJSONObject("service_type");
                        if (serviceObj != null) {
//                            holder.car_name.setText(serviceObj.optString("name"));
                            if (tag.equalsIgnoreCase("past_trips")) {
                                tripAmount.setText(SharedHelper.getKey(context, "currency") + serviceObj.optString("price"));
                            } else {
                                tripAmount.setVisibility(View.GONE);
                            }
                            Picasso.get().load(serviceObj.optString("image"))
                                    .placeholder(R.drawable.ic_dummy_user).error(R.drawable.ic_dummy_user)
                                    .into(tripProviderImg);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
                if ((customDialog != null) && (customDialog.isShowing()))
                    customDialog.dismiss();
                parentLayout.setVisibility(View.VISIBLE);

            }
        }, error -> {
            if ((customDialog != null) && (customDialog.isShowing()))
                customDialog.dismiss();
            displayMessage(getString(R.string.something_went_wrong));
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("X-Requested-With", "XMLHttpRequest");
                headers.put("Authorization", "" + SharedHelper.getKey(context, "token_type") + " " + SharedHelper.getKey(context, "access_token"));
                return headers;
            }
        };

        ClassLuxApp.getInstance().addToRequestQueue(jsonArrayRequest);
    }


    public void displayMessage(String toastString) {
        Toasty.info(this, toastString, Toasty.LENGTH_SHORT, true).show();
    }

    public void GoToBeginActivity() {
        Intent mainIntent = new Intent(activity, SignUp.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainIntent);
        activity.finish();
    }

    @Override
    public void onBackPressed() {
        if (lnrInvoice.getVisibility() == View.VISIBLE) {
            lnrInvoice.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }

    public void cancelRequest() {

        customDialog = new CustomDialog(context);
        customDialog.setCancelable(false);
        if (customDialog != null)
            customDialog.show();
        JSONObject object = new JSONObject();
        try {
            object.put("request_id", jsonObject.optString("id"));
            object.put("cancel_reason", reason);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLHelper.CANCEL_REQUEST_API, object, response -> {
            Log.v("CancelRequestResponse", response.toString());
            if ((customDialog != null) && (customDialog.isShowing()))
                customDialog.dismiss();
            finish();
        }, error -> {
            if ((customDialog != null) && (customDialog.isShowing()))
                customDialog.dismiss();
            displayMessage(getString(R.string.something_went_wrong));
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("X-Requested-With", "XMLHttpRequest");
                headers.put("Authorization", "" + SharedHelper.getKey(context, "token_type") + " " + SharedHelper.getKey(context, "access_token"));
                return headers;
            }
        };

        ClassLuxApp.getInstance().addToRequestQueue(jsonObjectRequest);
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
}
