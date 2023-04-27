package com.pinkcar.providers.ui.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pinkcar.providers.R;
import com.pinkcar.providers.ui.activities.ChangeAddressAdtivityOne;
import com.pinkcar.providers.ui.activities.ChangePasswordActivity;
import com.pinkcar.providers.ui.activities.HelpActivity;
import com.pinkcar.providers.ui.activities.TermsAndConditionActivity;


public class ProfileAccountFragment extends Fragment {

    TextView changePasswordtv,postalAddresstv,helpTv,termConditionTv;


    public ProfileAccountFragment() {
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
        View view = inflater.inflate(R.layout.fragment_profile_account, container, false);

        changePasswordtv = view.findViewById(R.id.changePasswordtv);
        postalAddresstv = view.findViewById(R.id.postalAddresstv);
        helpTv = view.findViewById(R.id.helpTv);
        termConditionTv = view.findViewById(R.id.termConditionTv);

        changePasswordtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ChangePasswordActivity.class);
                startActivity(intent);
            }
        });

        postalAddresstv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ChangeAddressAdtivityOne.class);
                startActivity(intent);
            }
        });


        helpTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), HelpActivity.class);
                startActivity(intent);
            }
        });

        termConditionTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), TermsAndConditionActivity.class);
                startActivity(intent);
            }
        });


        return view;
    }
}