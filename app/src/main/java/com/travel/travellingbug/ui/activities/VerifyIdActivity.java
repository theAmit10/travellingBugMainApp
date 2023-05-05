package com.travel.travellingbug.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;
import com.travel.travellingbug.R;
import com.travel.travellingbug.helper.SharedHelper;

public class VerifyIdActivity extends AppCompatActivity {

    FloatingActionButton floatingActionButton2;
    TextInputEditText firstNameETL;

    ImageView backArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_id);

        initComponent();
        onClickListenerForComponenet();

        String first_nameS = SharedHelper.getKey(getApplicationContext(), "first_name");
        if (first_nameS != null && !first_nameS.equalsIgnoreCase("null") && first_nameS.length() > 0) {
            firstNameETL.setText(first_nameS);
            firstNameETL.setTextColor(getResources().getColor(R.color.dark_gray));
            firstNameETL.setTypeface(firstNameETL.getTypeface(), Typeface.BOLD);

        } else {
            firstNameETL.setText("Add name");


        }
    }

    private void onClickListenerForComponenet() {
        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VerifyIdActivity.this, DocUploadActivity.class);
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

    private void initComponent() {
        floatingActionButton2 = findViewById(R.id.floatingActionButton2);
        firstNameETL = findViewById(R.id.firstNameETL);
        backArrow = findViewById(R.id.backArrow);

    }
}