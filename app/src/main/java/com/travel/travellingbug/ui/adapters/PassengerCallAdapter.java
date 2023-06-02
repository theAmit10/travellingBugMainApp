package com.travel.travellingbug.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.travel.travellingbug.R;
import com.travel.travellingbug.helper.URLHelper;
import com.travel.travellingbug.models.PassengerCallModel;

import java.util.ArrayList;

public class PassengerCallAdapter extends RecyclerView.Adapter<PassengerCallAdapter.ViewHolder> {

    Context context;
    ArrayList<PassengerCallModel> list;

    public PassengerCallAdapter(Context context, ArrayList<PassengerCallModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.design_call_request_passenger,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        PassengerCallModel passengerCallModel = list.get(position);

        Picasso.get().load(URLHelper.BASE + "storage/app/public/" +passengerCallModel.getImage()).error(R.drawable.ic_dummy_user).placeholder(R.drawable.ic_dummy_user).into(holder.image);

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, ""+passengerCallModel.getU_id(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.filterImage);
        }
    }

}
