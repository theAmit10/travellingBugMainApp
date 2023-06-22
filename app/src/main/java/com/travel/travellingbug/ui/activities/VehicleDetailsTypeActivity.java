package com.travel.travellingbug.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.travel.travellingbug.R;

public class VehicleDetailsTypeActivity extends AppCompatActivity {

    FloatingActionButton floatingActionButton;

    String license_number = "";
    String service_name = "";
    String service_model = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_details_type);

        license_number = getIntent().getStringExtra("license_number");
        service_name = getIntent().getStringExtra("service_name");
        service_model = getIntent().getStringExtra("service_model");

        initComponent();
        clickHandlerOnComponent();
    }

    private void clickHandlerOnComponent() {
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VehicleDetailsTypeActivity.this, VehicleDetailsColorActivity.class);
                intent.putExtra("license_number", license_number);
                intent.putExtra("service_name", service_name);
                intent.putExtra("service_model", service_model);
                startActivity(intent);
            }
        });
    }

    private void initComponent() {
        floatingActionButton = findViewById(R.id.floatingActionButton);

    }
}