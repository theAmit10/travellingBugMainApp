package com.travel.travellingbug.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.travel.travellingbug.R;

public class ChangeAddressAdtivityOne extends AppCompatActivity {

    TextView addPostalAddressTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_address_adtivity_one);

        addPostalAddressTv = findViewById(R.id.addPostalAddressTv);

        addPostalAddressTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChangeAddressAdtivityOne.this, ChangeAddressAdtivityTwo.class);
                startActivity(intent);
            }
        });

    }
}