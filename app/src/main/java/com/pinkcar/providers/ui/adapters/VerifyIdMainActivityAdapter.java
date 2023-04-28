package com.pinkcar.providers.ui.adapters;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pinkcar.providers.R;
import com.pinkcar.providers.models.VerifyIdMainActivityModel;

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
