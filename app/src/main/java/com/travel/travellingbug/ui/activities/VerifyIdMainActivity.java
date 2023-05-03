package com.travel.travellingbug.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.travel.travellingbug.R;
import com.travel.travellingbug.models.VerifyIdMainActivityModel;
import com.travel.travellingbug.ui.adapters.VerifyIdMainActivityAdapter;

import java.util.ArrayList;

public class VerifyIdMainActivity extends AppCompatActivity {

    RecyclerView verifyIdMainRV;
    ArrayList<VerifyIdMainActivityModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_id_main);

        initComponenet();
        settingAdapterForRecycleView();
    }

    private void settingAdapterForRecycleView() {
        // adding data to the list

        list.add(new VerifyIdMainActivityModel("Passport","Face Photo Page"));
        list.add(new VerifyIdMainActivityModel("Aadhar Card","EU ID only"));
        list.add(new VerifyIdMainActivityModel("PAN Card","Front and Back"));
        list.add(new VerifyIdMainActivityModel("DL No","Front and Back"));
        list.add(new VerifyIdMainActivityModel("RC No","Front and Back"));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false);
        VerifyIdMainActivityAdapter verifyIdMainActivityAdapter = new VerifyIdMainActivityAdapter(getApplicationContext(),list);
        verifyIdMainRV.setLayoutManager(linearLayoutManager);
        verifyIdMainRV.setAdapter(verifyIdMainActivityAdapter);
        verifyIdMainRV.setNestedScrollingEnabled(false);


    }

    private void initComponenet() {
        verifyIdMainRV = findViewById(R.id.verifyIdMainRV);

    }
}