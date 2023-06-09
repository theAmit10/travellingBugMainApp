package com.travel.travellingbug.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.travel.travellingbug.R;
import com.travel.travellingbug.helper.SharedHelper;

public class ChangeAddressAdtivityOne extends AppCompatActivity {

    TextView addPostalAddressTv;

    ImageView imageView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_address_adtivity_one);





        addPostalAddressTv = findViewById(R.id.addPostalAddressTv);
        imageView3 = findViewById(R.id.imageView3);


        System.out.println("postal address "+SharedHelper.getKey(getApplicationContext(), "full_address"));
        System.out.println("postal address len "+SharedHelper.getKey(getApplicationContext(), "full_address").length());


        if(SharedHelper.getKey(getApplicationContext(), "full_address").equalsIgnoreCase("") || SharedHelper.getKey(getApplicationContext(), "full_address").equalsIgnoreCase("null")){
            addPostalAddressTv.setText("Add Postal Address");
        }else {
            addPostalAddressTv.setText(SharedHelper.getKey(getApplicationContext(), "full_address"));
        }

        addPostalAddressTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChangeAddressAdtivityOne.this, ChangeAddressAdtivityTwo.class);
                startActivity(intent);
            }
        });


        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}