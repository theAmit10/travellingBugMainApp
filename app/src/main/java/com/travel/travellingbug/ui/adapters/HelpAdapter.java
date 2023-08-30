package com.travel.travellingbug.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.travel.travellingbug.R;
import com.travel.travellingbug.models.HelpModel;
import com.travel.travellingbug.ui.activities.ComplaintsAcivity;

import java.util.ArrayList;

public class HelpAdapter extends RecyclerView.Adapter<HelpAdapter.ViewHolder> {

    Context context;
    ArrayList<HelpModel> list;


    public HelpAdapter(Context context, ArrayList<HelpModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View  view = LayoutInflater.from(context).inflate(R.layout.design_help_layout,parent,false);

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        HelpModel helpModel = list.get(position);

        holder.title.setText(helpModel.getTitle());
        holder.description.setText(helpModel.getDescription());

        holder.containerCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ComplaintsAcivity.class);
                intent.putExtra("title",helpModel.getTitle());
                intent.putExtra("id",helpModel.getId());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                v.getContext().startActivity(intent);
//                overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView description;
        CardView containerCV;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            containerCV = itemView.findViewById(R.id.containerCV);

        }
    }
}
