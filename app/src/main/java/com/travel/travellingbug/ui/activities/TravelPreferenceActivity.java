package com.travel.travellingbug.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.api.GoogleApiClient;
import com.travel.travellingbug.R;
import com.travel.travellingbug.helper.ConnectionHelper;
import com.travel.travellingbug.helper.CustomDialog;
import com.travel.travellingbug.helper.SharedHelper;

import es.dmoral.toasty.Toasty;

public class TravelPreferenceActivity extends AppCompatActivity {

    GoogleApiClient mGoogleApiClient;

    CustomDialog customDialog;

    private static final String TAG = "TRAVEL PREFERENCE";
    private static final int SELECT_PHOTO = 100;
    public static int deviceHeight;
    public static int deviceWidth;

    //    CustomDialog customDialog;
    ConnectionHelper helper;
    Boolean isInternet;

    String titles = "";
    String subTitle = "";
    String id = "";

    ImageView backArrow;
    TextView bioTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_preference);

        initComponenet();
        getBio();
        clickHandler();
    }

    private void clickHandler() {
        bioTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TravelPreferenceActivity.this, UpdateProfile.class);
                intent.putExtra("parameter", "bio");
                intent.putExtra("value",bioTv.getText());
                startActivity(intent);

            }
        });

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getBio();
    }

    private void getBio() {

        String bio = SharedHelper.getKey(getApplicationContext(), "bio");
        if (bio != null || !bio.equalsIgnoreCase("null") || !bio.equalsIgnoreCase("")) {
            bioTv.setText(bio);
        } else {
            bioTv.setText("Add a mini bio");
        }
    }

    public void displayMessage(String toastString) {
        Toasty.info(getApplicationContext(), toastString, Toast.LENGTH_SHORT, true).show();
    }

    private void initComponenet() {


        helper = new ConnectionHelper(getApplicationContext());
        isInternet = helper.isConnectingToInternet();

        backArrow = findViewById(R.id.backArrow);
        bioTv = findViewById(R.id.bioTv);

        
    }
}