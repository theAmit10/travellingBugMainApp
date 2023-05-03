package com.travel.travellingbug.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.travel.travellingbug.R;
import com.travel.travellingbug.models.VerifyIdMainActivityModel;

import java.util.ArrayList;



public class TravelPreferenceActivityMainAdapter extends RecyclerView.Adapter<TravelPreferenceActivityMainAdapter.ViewHolder> {

    Context context;
    ArrayList<VerifyIdMainActivityModel> list;

    public TravelPreferenceActivityMainAdapter(Context context, ArrayList<VerifyIdMainActivityModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.design_travel_preference,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        VerifyIdMainActivityModel verifyIdMainActivityModel = list.get(position);
        holder.title.setText(verifyIdMainActivityModel.getTitle());
        holder.description.setText(verifyIdMainActivityModel.getDescription());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.titleVIMtv);
            description = itemView.findViewById(R.id.descriptionVIMtv);
        }
    }
}
