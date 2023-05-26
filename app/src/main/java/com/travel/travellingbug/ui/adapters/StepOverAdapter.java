package com.travel.travellingbug.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.travel.travellingbug.R;
import com.travel.travellingbug.models.StopOverModel;

import java.util.ArrayList;

public class StepOverAdapter extends RecyclerView.Adapter<StepOverAdapter.ViewHolder> {

    Context context;
    ArrayList<StopOverModel> list;

    public StepOverAdapter(Context context, ArrayList<StopOverModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.design_stopover_rv,parent,false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        StopOverModel stopOverModel = list.get(position);
        holder.location.setText(stopOverModel.getLocation());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView location;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            location = itemView.findViewById(R.id.location);
        }
    }
}
