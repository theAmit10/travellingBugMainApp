package com.travel.travellingbug.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.travel.travellingbug.R;
import com.travel.travellingbug.helper.SharedHelper;

public class VehicleDetailsModelActivity extends AppCompatActivity {

    FloatingActionButton floatingActionButton;
    String license_number = "";
    String service_name = "";
    String service_model = "";

    EditText vehicleModelETL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_details_model);

        license_number = getIntent().getStringExtra("license_number");
        service_name = getIntent().getStringExtra("service_name");


        initComponent();
        clickHandlerOnComponent();

        vehicleModelETL.setText(SharedHelper.getKey(getApplicationContext(), "service_model"));

        vehicleModelETL.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                service_model = vehicleModelETL.getText().toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                service_model = vehicleModelETL.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void clickHandlerOnComponent() {
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VehicleDetailsModelActivity.this, VehicleDetailsTypeActivity.class);
                intent.putExtra("license_number", license_number);
                intent.putExtra("service_name", service_name);
                intent.putExtra("service_model", service_model);
                startActivity(intent);
            }
        });
    }

    private void initComponent() {
        floatingActionButton = findViewById(R.id.floatingActionButton);
        vehicleModelETL = findViewById(R.id.vehicleModelETL);

    }
}