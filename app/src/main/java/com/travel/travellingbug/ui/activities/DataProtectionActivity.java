package com.travel.travellingbug.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.travel.travellingbug.R;

public class DataProtectionActivity extends AppCompatActivity {

    ImageView backArrow;
    TextView privacyPolicy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_protection);

        backArrow = findViewById(R.id.backArrow);
        privacyPolicy = findViewById(R.id.privacyPolicy);


        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        privacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(DataProtectionActivity.this, "Comming soon", Toast.LENGTH_SHORT).show();
                Uri uri = Uri.parse("https://travellingbug.in/terms-and-condition");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
            }
        });



    }
}