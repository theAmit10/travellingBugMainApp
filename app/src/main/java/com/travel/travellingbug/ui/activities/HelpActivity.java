package com.travel.travellingbug.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.travel.travellingbug.R;

public class HelpActivity extends AppCompatActivity {

    ImageView backArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);


//        {"grant_type":"password","client_id":2,"client_secret":"WifS1rMi3LvuorP1G2UdtKZairUNSH2iMqrKivPf","email":"","mobile":"888888788","scope":"","device_type":"android","device_id":"0079ae652f15b06f","device_token":"eqbYFHiiQgKyQ86Lmx0EUZ:APA91bFSv48-EnMOeasV7LW5g1i0fQnL3TzP82J5-fV8jIMOT4WKrbuRMNK6uAF4B2fB7iXf_jaFXRq7h8dXtq1gIrvtggnIfgEXt3CHy5iqOSsQ_iOcs9GqbYNV6m7R57_hCUhWH4mC","logged_in":"1"}
        backArrow = findViewById(R.id.backArrow);

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}