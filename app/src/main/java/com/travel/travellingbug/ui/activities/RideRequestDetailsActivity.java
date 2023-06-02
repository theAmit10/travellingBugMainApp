package com.travel.travellingbug.ui.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
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

import java.util.HashMap;
import java.util.Map;

public class RideRequestDetailsActivity extends AppCompatActivity {

    String first_name = "",rating = "",rating_val = "",profile_image = "",user_id = "",s_address = "",d_address = "",pick_up_date = "",pick_up_time = "",noofseat = "",fare = "",request_id = "",tag = "",person_id="",status="";
    ImageView backArrow,profileImgeIv;

    CustomDialog customDialog;
    RatingBar listitemrating;
    TextView nametv,ratingVal,viewProfileTv,chatTv,pickupLocation,dropLocation,pickUpDataVal,pickUpTimeVal,fareVal,seatVal;
    Button rejectBtn,acceptBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_request_details);

        getIntentData();
        initComponentData();
        setDataOnComponent();
        clickHandlerComponent();
    }

    private void setDataOnComponent() {
        listitemrating.setRating(2f);

        nametv.setText(first_name);
        ratingVal.setText(rating_val);

        pickupLocation.setText(s_address);
        dropLocation.setText(d_address);

        pickUpDataVal.setText(pick_up_date);
        pickUpTimeVal.setText(pick_up_time);

        fareVal.setText(fare);
        seatVal.setText(noofseat);

        Picasso.get().load(profile_image)
                .placeholder(R.drawable.ic_dummy_user).error(R.drawable.ic_dummy_user).into(profileImgeIv);


        if(!status.equalsIgnoreCase("Pending")){
            System.out.println("if");
            System.out.println("! pending : "+!status.equalsIgnoreCase("Pending"));
            if(status.equalsIgnoreCase("ACCEPTED")){
                System.out.println("ACCEPTED : "+status.equalsIgnoreCase("ACCEPTED"));
                acceptBtn.setText("ACCEPTED");
                acceptBtn.setVisibility(View.VISIBLE);
                rejectBtn.setVisibility(View.GONE);

            }else if(status.equalsIgnoreCase("CANCELLED")){
                System.out.println("CANCELLED : "+status.equalsIgnoreCase("CANCELLED"));
                rejectBtn.setText("CANCELLED");
                rejectBtn.setVisibility(View.VISIBLE);
                acceptBtn.setVisibility(View.GONE);
            }
        }else {
            System.out.println("else");
            acceptBtn.setVisibility(View.VISIBLE);
            rejectBtn.setVisibility(View.VISIBLE);
        }
    }

    private void clickHandlerComponent() {

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptRequest(person_id);
            }
        });


        rejectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelRequest(person_id);
            }
        });

        viewProfileTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RideRequestDetailsActivity.this, UserProfileActivity.class));
            }
        });

        chatTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RideRequestDetailsActivity.this, "Chat", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initComponentData() {
        backArrow = findViewById(R.id.backArrow);
        profileImgeIv = findViewById(R.id.profileImgeIv);
        listitemrating = findViewById(R.id.listitemrating);
        nametv = findViewById(R.id.nametv);
        ratingVal = findViewById(R.id.ratingVal);
        viewProfileTv = findViewById(R.id.viewProfileTv);
        chatTv = findViewById(R.id.chatTv);
        pickupLocation = findViewById(R.id.pickupLocation);
        dropLocation = findViewById(R.id.dropLocation);
        pickUpDataVal = findViewById(R.id.pickUpDataVal);
        pickUpTimeVal = findViewById(R.id.pickUpTimeVal);
        fareVal = findViewById(R.id.fareVal);
        seatVal = findViewById(R.id.seatVal);
        rejectBtn = findViewById(R.id.rejectBtn);
        acceptBtn = findViewById(R.id.acceptBtn);
    }

    private void acceptRequest(String id){
        System.out.println("Accepting Request... ");
        customDialog = new CustomDialog(this);
        customDialog.setCancelable(false);
        customDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, URLHelper.ACCEPT_REQUEST , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if(response != null){
                    customDialog.dismiss();
                    showAcceptedDialog();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                customDialog.dismiss();
                Toast.makeText(RideRequestDetailsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }

        }) {


            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id",id);
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

    private void cancelRequest(String id){
        System.out.println("Cancelling Request... ");
        customDialog = new CustomDialog(this);
        customDialog.setCancelable(false);
        customDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, URLHelper.CANCEL_REQUEST , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if(response != null){
                    customDialog.dismiss();
                    showCanceldDialog();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                customDialog.dismiss();
                Toast.makeText(RideRequestDetailsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }

        }) {


            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id",id);
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

    private void showAcceptedDialog() {

//        AlertDialog alertDialog = new AlertDialog.Builder(FirstActivity.getInstance()).create();
        Dialog confirmDialog = new Dialog(this);
        confirmDialog.setContentView(R.layout.schedule_dialog);

        TextView tvDone = confirmDialog.findViewById(R.id.tvDone);
        TextView bookingStatusTitleTv = confirmDialog.findViewById(R.id.bookingStatusTitleTv);
        TextView bookingStatusSubTitleTv = confirmDialog.findViewById(R.id.bookingStatusSubTitleTv);
        TextView tvDriverMsg = confirmDialog.findViewById(R.id.tvDriverMsg);

        bookingStatusTitleTv.setText("Accepted");

        bookingStatusSubTitleTv.setText("Ride Request has been Accepted successfully ");

        tvDriverMsg.setText("");

        confirmDialog.show();
        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog.dismiss();
            }
        });
    }

    private void showCanceldDialog() {

//        AlertDialog alertDialog = new AlertDialog.Builder(FirstActivity.getInstance()).create();
        Dialog confirmDialog = new Dialog(this);
        confirmDialog.setContentView(R.layout.schedule_dialog);

        TextView tvDone = confirmDialog.findViewById(R.id.tvDone);
        TextView bookingStatusTitleTv = confirmDialog.findViewById(R.id.bookingStatusTitleTv);
        TextView bookingStatusSubTitleTv = confirmDialog.findViewById(R.id.bookingStatusSubTitleTv);
        TextView tvDriverMsg = confirmDialog.findViewById(R.id.tvDriverMsg);

        bookingStatusTitleTv.setText("Cancelled");

        bookingStatusSubTitleTv.setText("Ride Request has been Cancelled successfully");

        tvDriverMsg.setText("");

        confirmDialog.show();
        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog.dismiss();
            }
        });
    }

    private void getIntentData() {

        first_name = getIntent().getStringExtra("first_name");
        rating = getIntent().getStringExtra("rating");
        rating_val = getIntent().getStringExtra("rating_val");
        profile_image = getIntent().getStringExtra("profile_image");
        user_id = getIntent().getStringExtra("user_id");
        s_address = getIntent().getStringExtra("s_address");
        d_address = getIntent().getStringExtra("d_address");
        pick_up_date = getIntent().getStringExtra("pick_up_date");
        pick_up_time = getIntent().getStringExtra("pick_up_time");
        noofseat = getIntent().getStringExtra("noofseat");
        fare = getIntent().getStringExtra("fare");
        request_id = getIntent().getStringExtra("request_id");
        tag = getIntent().getStringExtra("tag");
        person_id = getIntent().getStringExtra("person_id");
        status = getIntent().getStringExtra("status");
    }
}