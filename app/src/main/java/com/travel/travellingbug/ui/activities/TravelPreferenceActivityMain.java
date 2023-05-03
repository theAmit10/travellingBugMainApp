package com.travel.travellingbug.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.travel.travellingbug.R;
import com.travel.travellingbug.models.VerifyIdMainActivityModel;
import com.travel.travellingbug.ui.adapters.VerifyIdMainActivityAdapter;

import java.util.ArrayList;

public class TravelPreferenceActivityMain extends AppCompatActivity {

    RecyclerView travelPreferenceRV;
    ArrayList<VerifyIdMainActivityModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_preference_main);

        initComponenet();
        settingAdapterForRecycleView();
    }

    private void settingAdapterForRecycleView() {
        // adding data to the list

        list = new ArrayList<>();

        list.add(new VerifyIdMainActivityModel("Chattiness","I'm Chatty when i feel confortable "));
        list.add(new VerifyIdMainActivityModel("Music","I'll jam dependent on mood"));
        list.add(new VerifyIdMainActivityModel("Smoking","Cigarette break outside the car are ok."));
        list.add(new VerifyIdMainActivityModel("Pet's","I'll travel with pets depending on the animal"));


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false);
        VerifyIdMainActivityAdapter verifyIdMainActivityAdapter = new VerifyIdMainActivityAdapter(getApplicationContext(),list);
        travelPreferenceRV.setLayoutManager(linearLayoutManager);
        travelPreferenceRV.setAdapter(verifyIdMainActivityAdapter);
        travelPreferenceRV.setNestedScrollingEnabled(false);


    }

    private void initComponenet() {
        travelPreferenceRV = findViewById(R.id.travelPreferenceRV);

    }
}