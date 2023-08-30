package com.travel.travellingbug.ui.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;
import com.travel.travellingbug.ClassLuxApp;
import com.travel.travellingbug.R;
import com.travel.travellingbug.helper.CustomDialog;
import com.travel.travellingbug.helper.SharedHelper;
import com.travel.travellingbug.helper.URLHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class ConfirmRideRequestActivity extends AppCompatActivity {

    Button confirmBtn;
    ImageView profileImgeIv,backArrow;
    TextView nameTv, carModelAndColor, pickupLocation, dropLocation, dateVal, timeVal, fareVal, seatVal;

    String sc_address = "", dc_address = "", s_profileImage = "", s_name = "", s_carModleAndColor = "", s_date = "", s_time = "", s_fare = "", s_seat = "", s_id = "";

    String s_latitude="", s_longitude="", d_latitude="",d_longitude="";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_ride_request);

        initComponent();
        getIntentDate();
        settingDateToComponent();
        clickHandlerComponenet();


    }

    private void settingDateToComponent() {

        Picasso.get().load(s_profileImage)
                .placeholder(R.drawable.car_select).error(R.drawable.car_select).into(profileImgeIv);
        nameTv.setText(s_name);
        carModelAndColor.setText(s_carModleAndColor);
        pickupLocation.setText(sc_address);
        dropLocation.setText(dc_address);
        dateVal.setText(s_date);
        timeVal.setText(s_time);
//        fareVal.setText(s_fare);
        seatVal.setText(s_seat + " Seats");


        // Getting Fare Value
        // for fare details
        try {
            StringRequest request = new StringRequest(Request.Method.GET, URLHelper.ESTIMATED_FARE_AND_DISTANCE + "?s_latitude=" + s_latitude + "&s_longitude=" + s_longitude + "&d_latitude=" + d_latitude + "&d_longitude=" + d_longitude + "&service_type=2", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {


                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        if (response != null) {
                            System.out.println("payment details estimated data : " + jsonObject.toString());
                            jsonObject.optString("estimated_fare");
                            jsonObject.optString("distance");
                            jsonObject.optString("time");
                            jsonObject.optString("tax_price");
                            jsonObject.optString("base_price");
                            jsonObject.optString("discount");
                            jsonObject.optString("currency");

                            String con = jsonObject.optString("currency") + " ";

                            System.out.println("ESTIMATED FARE STATUS :" + response.toString());

                            try {
                                System.out.println("Fare : "+con + jsonObject.optString("estimated_fare"));

                                Double fare = Double.valueOf(jsonObject.optString("estimated_fare"));
                                int no_of_seat = Integer.parseInt(s_seat);
                                Double c_fare = fare * no_of_seat;
                                String calculated_fare = con + c_fare;

                                fareVal.setText(calculated_fare);

                            }catch (Exception e){
                                e.printStackTrace();
                            }


                        }

                    } catch (JSONException e) {

                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    try {
                        Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            }) {




                @Override
                public Map<String, String> getHeaders() {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("X-Requested-With", "XMLHttpRequest");
                    headers.put("Authorization", "Bearer " + SharedHelper.getKey(getApplicationContext(), "access_token"));
                    return headers;
                }

            };

            ClassLuxApp.getInstance().addToRequestQueue(request);



        }catch (Exception e){
            e.printStackTrace();
        }



    }

    private void getIntentDate() {

        sc_address = getIntent().getStringExtra("s_address");
        dc_address = getIntent().getStringExtra("d_address");
        s_profileImage = getIntent().getStringExtra("s_profileImage");
        s_carModleAndColor = getIntent().getStringExtra("s_carModleAndColor");
        s_date = getIntent().getStringExtra("s_date");
        s_time = getIntent().getStringExtra("s_time");
        s_fare = getIntent().getStringExtra("s_fare");
        s_seat = getIntent().getStringExtra("s_seat");
        s_name = getIntent().getStringExtra("s_name");
        s_id = getIntent().getStringExtra("s_id");


        s_latitude = getIntent().getStringExtra("s_latitude");
        s_longitude = getIntent().getStringExtra("s_longitude");
        d_latitude = getIntent().getStringExtra("d_latitude");
        d_longitude = getIntent().getStringExtra("d_longitude");


        System.out.println("S_ID : " + s_id);
        System.out.println("S_SEATS : " + s_seat);


    }

    private void clickHandlerComponenet() {
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequestToPublisher();
            }


        });

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void showConfirmDialog() {

        Dialog confirmDialog = new Dialog(this);
        confirmDialog.setContentView(R.layout.schedule_dialog);

        TextView tvDone = confirmDialog.findViewById(R.id.tvDone);
        TextView bookingStatusTitleTv = confirmDialog.findViewById(R.id.bookingStatusTitleTv);
        TextView bookingStatusSubTitleTv = confirmDialog.findViewById(R.id.bookingStatusSubTitleTv);
        TextView tvDriverMsg = confirmDialog.findViewById(R.id.tvDriverMsg);

        bookingStatusTitleTv.setText("Requested Successful");

        bookingStatusSubTitleTv.setText("Your ride has been Requested successfully ");

        tvDriverMsg.setText("");

        confirmDialog.show();
        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog.dismiss();
                Intent intent = new Intent(getApplicationContext(), HomeScreenActivity.class);
                startActivity(intent);
//                finish();
            }
        });
    }

    private void sendRequestToPublisher() {

        CustomDialog customDialog = new CustomDialog(ConfirmRideRequestActivity.this);
        customDialog.show();


        StringRequest request = new StringRequest(Request.Method.POST, URLHelper.BOOK_FOR_UPCOMMING_TRIPS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(customDialog.isShowing()){
                    customDialog.dismiss();
                }

                System.out.println("size : " + response.length());
                System.out.println("data : " + response);

                if (response != null) {
                    showConfirmDialog();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(customDialog.isShowing()){
                    customDialog.dismiss();
                }
                Toast.makeText(ConfirmRideRequestActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("request_id", s_id);
                params.put("noofseat", s_seat);
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

    public void displayMessage(String toastString) {
        Toasty.info(getApplicationContext(), toastString, Toast.LENGTH_SHORT, true).show();
    }

    private void initComponent() {
        confirmBtn = findViewById(R.id.confirmBtn);
        profileImgeIv = findViewById(R.id.profileImgeIv);
        nameTv = findViewById(R.id.nameTv);
        carModelAndColor = findViewById(R.id.carModelAndColor);
        pickupLocation = findViewById(R.id.pickupLocation);
        dropLocation = findViewById(R.id.dropLocation);
        dateVal = findViewById(R.id.dateVal);
        timeVal = findViewById(R.id.timeVal);
        fareVal = findViewById(R.id.fareVal);
        seatVal = findViewById(R.id.seatVal);
        backArrow = findViewById(R.id.backArrow);
    }
}