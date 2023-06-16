package com.travel.travellingbug.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.travel.travellingbug.R;
import com.travel.travellingbug.models.VerifyIdMainActivityModel;
import com.travel.travellingbug.ui.activities.UpdatePreference;

import java.util.ArrayList;

public class VerifyIdMainActivityAdapter extends RecyclerView.Adapter<VerifyIdMainActivityAdapter.ViewHolder> {

    Context context;
    ArrayList<VerifyIdMainActivityModel> list;

    public VerifyIdMainActivityAdapter(Context context, ArrayList<VerifyIdMainActivityModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.design_verify_id_main,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        VerifyIdMainActivityModel verifyIdMainActivityModel = list.get(position);
        holder.title.setText(verifyIdMainActivityModel.getTitle());
        holder.description.setText(verifyIdMainActivityModel.getDescription());


        holder.preferenceContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), UpdatePreference.class);
                intent.putExtra("id", verifyIdMainActivityModel.getId());
                intent.putExtra("title", verifyIdMainActivityModel.getTitle());
                intent.putExtra("subtitle", verifyIdMainActivityModel.getDescription());
                intent.putExtra("title_id", verifyIdMainActivityModel.getTitle_id());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                v.getContext().startActivity(intent);
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

        ConstraintLayout preferenceContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.titleVIMtv);
            description = itemView.findViewById(R.id.descriptionVIMtv);
            preferenceContainer = itemView.findViewById(R.id.preferenceContainer);
        }
    }
}
