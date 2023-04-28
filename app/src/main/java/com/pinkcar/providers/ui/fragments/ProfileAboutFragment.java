package com.pinkcar.providers.ui.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pinkcar.providers.R;
import com.pinkcar.providers.ui.activities.PersonalDetailsActivity;
import com.pinkcar.providers.ui.activities.ProfilePictureActivity;
import com.pinkcar.providers.ui.activities.TravelPreferenceActivity;
import com.pinkcar.providers.ui.activities.TravelPreferenceActivityMain;
import com.pinkcar.providers.ui.activities.VehicleDetailsLicensePlateNumberActivity;
import com.pinkcar.providers.ui.activities.VerifyEmailActivity;
import com.pinkcar.providers.ui.activities.VerifyIdActivity;
import com.pinkcar.providers.ui.activities.VerifyMobileNumberActivity;


public class ProfileAboutFragment extends Fragment {

    TextView verifyId,addMyPreferencesTv,addAMiniBioTv,addVehicleTv,editProfilePictv,editPersonalDetailstv,confirmEmailTv,phoneTv;


    public ProfileAboutFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_about, container, false);

        initComponent(view);
        clickHandlerOnComponenet();

        return view;
    }

    private void clickHandlerOnComponenet() {
        verifyId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), VerifyIdActivity.class);
                startActivity(intent);
            }
        });

        addMyPreferencesTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), TravelPreferenceActivityMain.class);
                startActivity(intent);
            }
        });

        addAMiniBioTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), TravelPreferenceActivity.class);
                startActivity(intent);
            }
        });

        addVehicleTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), VehicleDetailsLicensePlateNumberActivity.class);
                startActivity(intent);
            }
        });



        editProfilePictv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ProfilePictureActivity.class);
                startActivity(intent);
            }
        });

        editPersonalDetailstv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PersonalDetailsActivity.class);
                startActivity(intent);
            }
        });

        confirmEmailTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), VerifyEmailActivity.class);
                startActivity(intent);
            }
        });

        phoneTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), VerifyMobileNumberActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initComponent(View view) {
        verifyId = view.findViewById(R.id.verifyId);
        addMyPreferencesTv = view.findViewById(R.id.addMyPreferencesTv);
        addAMiniBioTv = view.findViewById(R.id.addAMiniBioTv);
        addVehicleTv = view.findViewById(R.id.addVehicleTv);

        editProfilePictv = view.findViewById(R.id.editProfilePictv);
        editPersonalDetailstv = view.findViewById(R.id.editPersonalDetailstv);
        confirmEmailTv = view.findViewById(R.id.confirmEmailTv);
        phoneTv = view.findViewById(R.id.phoneTv);
    }
}