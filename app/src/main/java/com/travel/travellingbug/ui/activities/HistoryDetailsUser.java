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
import android.widget.TextView;
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
import com.facebook.shimmer.ShimmerFrameLayout;
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
    TextView tripDate;
    MyBoldTextView paymentType;
    MyBoldTextView booking_id;
    MyBoldTextView tripComments;
    TextView tripProviderName;
    MyBoldTextView tripSource;
    MyBoldTextView lblTotalPrice;
    MyBoldTextView lblBookingID;
    MyBoldTextView tripDestination;
    TextView lblTitle;
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
    Button btnCancelRide;
    Driver driver;
    String reason = "";

    String request_id = "";
    String user_id = "";

    Button btnViewInvoice, btnCall;

    TextView trackVehicleTv;

    private ShimmerFrameLayout mFrameLayout;

    String flowValue= "",request_id_from_trip="",noofseats="";





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
            request_id_from_trip = getIntent().getStringExtra("request_id_from_trip");
            flowValue = getIntent().getStringExtra("flowValue");




            jsonObject = new JSONObject(post_details);
        } catch (Exception e) {
            jsonObject = null;
            e.printStackTrace();
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
                lblTitle.setText("RIDE DETAILS");
            }
        }
//        profileLayout.setOnClickListener(v -> {
//            Intent intent = new Intent(HistoryDetailsUser.this, ShowProfile.class);
//            intent.putExtra("driver", driver);
//            startActivity(intent);
//        });

        backArrow.setOnClickListener(view -> onBackPressed());


        trackVehicleTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HistoryDetailsUser.this, TrackActivity.class);
                intent.putExtra("flowValue", flowValue);
                intent.putExtra("request_id_from_trip", request_id_from_trip);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
            }
        });
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
        trackVehicleTv = findViewById(R.id.trackVehicleTv);
        mFrameLayout = findViewById(R.id.shimmerLayout);



        btnCancelRide.setOnClickListener(v -> {
            if(btnCancelRide.getText().toString().equalsIgnoreCase("service cancelled")){
                Toast.makeText(activity, "Ride has been already cancelled ", Toast.LENGTH_SHORT).show();
//                Toast.makeText(activity, "you cannot cancel ride ", Toast.LENGTH_SHORT).show();
            }else{
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
            }


        });

//        btnViewInvoice.setOnClickListener(v -> lnrInvoice.setVisibility(View.VISIBLE));

        btnViewInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lnrInvoice.setVisibility(View.VISIBLE);
//                Toast.makeText(HistoryDetailsUser.this, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        lnrInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lnrInvoice.setVisibility(View.GONE);
//                Toast.makeText(HistoryDetailsUser.this, "Clicked 2", Toast.LENGTH_SHORT).show();
            }
        });

//        lnrInvoice.setOnClickListener(v -> lnrInvoice.setVisibility(View.GONE));

//        lnrInvoiceSub.setOnClickListener(v -> {
//        });

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


    @Override
    public void onPause() {
        mFrameLayout.stopShimmer();
        super.onPause();
    }

    @Override
    public void onResume() {
        mFrameLayout.startShimmer();
        super.onResume();
    }

    public void getRequestDetails() {
        mFrameLayout.startShimmer();

        customDialog = new CustomDialog(context);
        customDialog.setCancelable(false);
        if (customDialog != null)
            customDialog.show();

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
                                        trackVehicleTv.setVisibility(View.GONE);
                                        btnCall.setVisibility(View.GONE);
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }

                                if(jsonArray.optJSONObject(i).optString("status").equalsIgnoreCase("COMPLETED")){
                                    try {
                                        btnCancelRide.setText("Service Cancelled");
                                        trackVehicleTv.setVisibility(View.GONE);
                                        btnCancelRide.setVisibility(View.GONE);
                                        btnViewInvoice.setVisibility(View.VISIBLE);
                                        btnCall.setVisibility(View.GONE);
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }

                                tripProviderImg.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(HistoryDetailsUser.this, DriverProfileActivity.class);
                                        intent.putExtra("user_id",providerObj.optString("id"));
                                        intent.putExtra("request_id",request_id);
                                        startActivity(intent);
                                    }
                                });


                                // getting taken seat
//                                JSONArray filterJsonArray = jsonObjectTrip.optJSONArray("filters");
//                                if (filterJsonArray != null && filterJsonArray.length() > 0) {
//                                    for (int j = 0; j < filterJsonArray.length(); j++) {
//                                        JSONObject filterJsonObject = filterJsonArray.optJSONObject(j);
//                                        System.out.println("j" + filterJsonObject.optString("user_id"));
//                                        System.out.println("j" + SharedHelper.getKey(getContext(), "id"));
//                                        if (filterJsonObject.optString("user_id").equalsIgnoreCase(SharedHelper.getKey(getContext(), "id"))) {
//                                            holder.availableSeat.setText(filterJsonObject.optString("noofseats")+" Seat");
//                                            // for fare details
//                                            try {
//                                                Double fares = Double.valueOf(jsonObjectTrip.optString("fare"));
//                                                int no_of_seat = Integer.parseInt(filterJsonObject.optString("noofseats"));
//                                                Double c_fare = fares * no_of_seat;
//                                                String calculated_fare = URLHelper.INR_SYMBOL + c_fare;
//
//                                                holder.fare.setText(calculated_fare);
//
//                                            }catch (Exception e){
//                                                e.printStackTrace();
//                                            }
//                                        }
//                                    }
//                                }





                                JSONArray filterJsonArray = jsonArray.optJSONObject(i).optJSONArray("filters");
                                for(int j=0; j<filterJsonArray.length(); j++){
                                    JSONObject filterJsonObj = filterJsonArray.getJSONObject(j);
                                    if(filterJsonObj.optString("user_id").equalsIgnoreCase(user_id)){
                                        noofseats = filterJsonObj.optString("noofseats");

//                                        holder.availableSeat.setText(filterJsonObject.optString("noofseats")+" Seat");

                                        lblBasePrice.setText(URLHelper.INR_SYMBOL + jsonArray.optJSONObject(i).optString("fare"));
                                        lblDistancePrice.setText(jsonArray.optJSONObject(i).optString("distance") + " KM");
                                        lblTaxPrice.setText(filterJsonObj.optString("noofseats"));
                                        paymentTypeImg.setImageResource(R.drawable.money1);


                                        // for fare details
                                        try {
                                            Double fares = Double.valueOf(jsonArray.optJSONObject(i).optString("fare"));
                                            int no_of_seat = Integer.parseInt(filterJsonObj.optString("noofseats"));
                                            Double c_fare = fares * no_of_seat;
                                            String calculated_fare = URLHelper.INR_SYMBOL + c_fare;

                                            tripAmount.setText(calculated_fare);
                                            lblTotalPrice.setText(calculated_fare);



                                        }catch (Exception e){
                                            e.printStackTrace();
                                        }



                                        if(filterJsonObj.optString("status").equalsIgnoreCase("CANCELLED")){
                                            try {
                                                btnCancelRide.setText("Service Cancelled");
                                                btnCall.setVisibility(View.GONE);
                                                trackVehicleTv.setVisibility(View.GONE);
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


//                                // getting fare details
//                                StringRequest request = new StringRequest(Request.Method.GET, URLHelper.ESTIMATED_FARE_AND_DISTANCE + "?s_latitude=" + jsonArray.optJSONObject(i).optString("s_latitude") + "&s_longitude=" + jsonArray.optJSONObject(i).optString("s_longitude") + "&d_latitude=" + jsonArray.optJSONObject(i).optString("d_latitude") + "&d_longitude=" + jsonArray.optJSONObject(i).optString("d_longitude") + "&service_type=2", new Response.Listener<String>() {
//                                    @Override
//                                    public void onResponse(String response) {
//
//
//                                        try {
//                                            JSONObject jsonObject = new JSONObject(response);
//
//                                            if (response != null) {
//                                                System.out.println("payment details estimated data : " + jsonObject.toString());
//                                                jsonObject.optString("estimated_fare");
//                                                jsonObject.optString("distance");
//                                                jsonObject.optString("time");
//                                                jsonObject.optString("tax_price");
//                                                jsonObject.optString("base_price");
//                                                jsonObject.optString("discount");
//                                                jsonObject.optString("currency");
//
//                                                String con = jsonObject.optString("currency") + " ";
//
//
////                                                txt04InvoiceId.setText(jsonArray.optJSONObject(i).optString("booking_id"));
//                                                lblBasePrice.setText(con + jsonObject.optString("base_price"));
//                                                lblDistancePrice.setText(jsonObject.optString("distance") + " KM");
//                                                lblTaxPrice.setText(con + jsonObject.optString("tax_price"));
//
//                                                paymentTypeImg.setImageResource(R.drawable.money1);
//
//
//                                                try {
//                                                    System.out.println("Fare : "+con + jsonObject.optString("estimated_fare"));
//
//                                                    Double fare = Double.valueOf(jsonObject.optString("estimated_fare"));
//                                                    int no_of_seat = Integer.parseInt(noofseats);
//                                                    Double c_fare = fare * no_of_seat;
//                                                    String calculated_fare = con + c_fare;
//
//                                                    tripAmount.setText(calculated_fare);
//                                                    lblTotalPrice.setText(calculated_fare);
//
//                                                }catch (Exception e){
//                                                    e.printStackTrace();
//                                                }
//
//
//                                                System.out.println("ESTIMATED FARE STATUS :" + response.toString());
//
//                                            }
//
//                                        } catch (JSONException e) {
//                                            Toast.makeText(HistoryDetailsUser.this, "Something went wrong", Toast.LENGTH_SHORT).show();
//                                            e.printStackTrace();
//                                        }
//
//
//                                    }
//                                }, new Response.ErrorListener() {
//                                    @Override
//                                    public void onErrorResponse(VolleyError error) {
//
//                                        try {
//                                            Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
//                                        } catch (Exception e) {
//                                            e.printStackTrace();
//                                        }
//
//                                    }
//
//                                }) {
//                                    @Override
//                                    public Map<String, String> getHeaders() {
//                                        HashMap<String, String> headers = new HashMap<String, String>();
//                                        headers.put("X-Requested-With", "XMLHttpRequest");
//                                        headers.put("Authorization", "Bearer " + SharedHelper.getKey(getApplicationContext(), "access_token"));
//                                        return headers;
//                                    }
//
//                                };
//
//                                ClassLuxApp.getInstance().addToRequestQueue(request);




                                try {
                                    tripDate.setText(getDate(form) + "th " + getMonth(form) + " " + getYear(form) + "\n" + getTime(form));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

//                                paymentType.setText(jsonArray.optJSONObject(i).optString("payment_mode"));
//                                if (jsonArray.optJSONObject(i).optString("payment_mode").equalsIgnoreCase("CASH")) {
//                                    paymentTypeImg.setImageResource(R.drawable.money1);
//                                } else {
//                                    paymentTypeImg.setImageResource(R.drawable.visa_icon);
//                                }

                                paymentType.setText("CASH");
                                paymentTypeImg.setImageResource(R.drawable.money1);


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

                                if (!jsonArray.optJSONObject(i).optJSONObject("provider").optString("rating").equalsIgnoreCase("null")) {
                                    System.out.println("case : "+jsonArray.optJSONObject(i).optJSONObject("provider").optString("rating"));
                                    tripProviderRating.setRating(Float.parseFloat(jsonArray.optJSONObject(i).optJSONObject("provider").optString("rating")));
                                } else {
                                    tripProviderRating.setRating(0);
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
                    mFrameLayout.setVisibility(View.GONE);




                } catch (JSONException e) {
                    displayMessage("Something went wrong");
                    e.printStackTrace();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(HistoryDetailsUser.this, "Something went wrong", Toast.LENGTH_SHORT).show();
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
                mFrameLayout.setVisibility(View.GONE);

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
        String monthName = new SimpleDateFormat("M").format(cal.getTime());
        String name = getMonthName(Integer.parseInt(monthName));


        return name;
    }

    public  String getMonthName(int month)
    {
        switch(month)
        {
            case 1:
                return "Jan";
            case 2:
                return "Feb";
            case 3:
                return "Mar";

            case 4:
                return "April";

            case 5:
                return "May";

            case 6:
                return "June";

            case 7:
                return "July";

            case 8:
                return "Aug";

            case 9:
                return "Sep";

            case 10:
                return "Oct";

            case 11:
                return "Nov";

            case 12:
                return "Dec";

        }
        return "";
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
