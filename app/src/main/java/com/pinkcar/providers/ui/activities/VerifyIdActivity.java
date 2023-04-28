package com.pinkcar.providers.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pinkcar.providers.R;

public class VerifyIdActivity extends AppCompatActivity {

    FloatingActionButton floatingActionButton2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_id);

        initComponent();
        onClickListenerForComponenet();
    }

    private void onClickListenerForComponenet() {
        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VerifyIdActivity.this, VerifyIdMainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initComponent() {
        floatingActionButton2 = findViewById(R.id.floatingActionButton2);

    }
}