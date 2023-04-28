package com.pinkcar.providers.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pinkcar.providers.R;

public class VehicleDetailsTypeActivity extends AppCompatActivity {

    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_details_type);

        initComponent();
        clickHandlerOnComponent();
    }

    private void clickHandlerOnComponent() {
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VehicleDetailsTypeActivity.this, VehicleDetailsColorActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initComponent() {
        floatingActionButton = findViewById(R.id.floatingActionButton);

    }
}