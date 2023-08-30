package com.travel.travellingbug.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.travel.travellingbug.R;

public class TermsAndConditionActivity extends AppCompatActivity {
    ImageView backArrow;
    TextView moreTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_condition);

        backArrow = findViewById(R.id.backArrow);

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
            }
        });

        moreTv = findViewById(R.id.moreTv);

        moreTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://travellingbug.in/terms-and-condition");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
            }
        });

    }
}