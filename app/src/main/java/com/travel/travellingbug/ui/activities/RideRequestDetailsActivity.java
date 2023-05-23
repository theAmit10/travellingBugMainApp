package com.travel.travellingbug.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.travel.travellingbug.R;

public class RideRequestDetailsActivity extends AppCompatActivity {

    String first_name = "",rating = "",rating_val = "",profile_image = "",user_id = "",s_address = "",d_address = "",pick_up_date = "",pick_up_time = "",noofseat = "",fare = "",request_id = "",tag = "";
    ImageView backArrow,profileImgeIv;
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
                Toast.makeText(RideRequestDetailsActivity.this, "Accepted", Toast.LENGTH_SHORT).show();
            }
        });


        rejectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RideRequestDetailsActivity.this, "Rejected", Toast.LENGTH_SHORT).show();
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
    }
}