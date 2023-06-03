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

    ItemClickListener itemClickListener;
    int selectedPosition=-1;

    public PassengerCallAdapter(Context context, ArrayList<PassengerCallModel> list) {
        this.context = context;
        this.list = list;
    }

    public PassengerCallAdapter(Context context, ArrayList<PassengerCallModel> list, ItemClickListener itemClickListener) {
        this.context = context;
        this.list = list;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

//        View view = LayoutInflater.from(context).inflate(R.layout.design_call_request_passenger,parent,false);

        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.design_call_request_passenger,parent,false);
        // return holder
        return new ViewHolder(view);

//        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        PassengerCallModel passengerCallModel = list.get(position);

        Picasso.get().load(URLHelper.BASE + "storage/app/public/" +passengerCallModel.getImage()).error(R.drawable.ic_dummy_user).placeholder(R.drawable.ic_dummy_user).into(holder.image);

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                // call listener
                itemClickListener.onClick(position,list.get(position));
                Toast.makeText(context, "FROM Adapter"+passengerCallModel.getUser_id(), Toast.LENGTH_SHORT).show();
                // update position
                selectedPosition=position;
                // notify
                notifyDataSetChanged();
            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get adapter position
                int position=holder.getAdapterPosition();
                // call listener
                itemClickListener.onClick(position,list.get(position));
                Toast.makeText(context, "FROM Adapter view"+passengerCallModel.getUser_id(), Toast.LENGTH_SHORT).show();
                // update position
                selectedPosition=position;
                // notify
                notifyDataSetChanged();
            }
        });

        // check conditions
        if(selectedPosition==position)
        {
            // When current position is equal
            // to selected position
            // set black background color
//            holder.cardView.setCardBackgroundColor(Color.parseColor("#000000"));
            // set white text color
//            holder.textView.setTextColor(Color.parseColor("#FFFFFF"));

        }
        else
        {
            // when current position is different
            // set white background
//            holder.cardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
            // set black text color
//            holder.textView.setTextColor(Color.parseColor("#000000"));

        }

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
