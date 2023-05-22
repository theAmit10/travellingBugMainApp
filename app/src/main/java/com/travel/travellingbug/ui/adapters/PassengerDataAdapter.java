package com.travel.travellingbug.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.travel.travellingbug.R;
import com.travel.travellingbug.models.PassengerDataModel;

import java.util.ArrayList;

public class PassengerDataAdapter extends RecyclerView.Adapter<PassengerDataAdapter.ViewHolder> {

    Context context;
    ArrayList<PassengerDataModel> list;

    public PassengerDataAdapter(Context context, ArrayList<PassengerDataModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.design_pasenger_data,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        PassengerDataModel passengerDataModel = list.get(position);

        holder.plocation.setText(passengerDataModel.getPlocation());
        holder.pname.setText(passengerDataModel.getPname());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView plocation;
        TextView pname;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            plocation = itemView.findViewById(R.id.plocation);
            pname = itemView.findViewById(R.id.pname);
        }
    }
}
