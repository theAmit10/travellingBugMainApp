package com.travel.travellingbug.ui.adapters;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.travel.travellingbug.R;

public class FindRideRequestListAdapter {

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImageIv;
        TextView profileNameTv,reviewCount,fare,availableSeat,fromTv,destinationTv,startTimeVal,carTypeVal;
        RatingBar listitemrating;
        Button details,request;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profileImageIv = itemView.findViewById(R.id.profileImageIv);
            profileNameTv = itemView.findViewById(R.id.profileNameTv);
            reviewCount = itemView.findViewById(R.id.reviewCount);
            fare = itemView.findViewById(R.id.fare);
            availableSeat = itemView.findViewById(R.id.availableSeat);
            fromTv = itemView.findViewById(R.id.fromTv);
            destinationTv = itemView.findViewById(R.id.destinationTv);
            startTimeVal = itemView.findViewById(R.id.startTimeVal);
            carTypeVal = itemView.findViewById(R.id.carTypeVal);
            listitemrating = itemView.findViewById(R.id.listitemrating);
            details = itemView.findViewById(R.id.details);
            request = itemView.findViewById(R.id.request);


        }
    }
}
