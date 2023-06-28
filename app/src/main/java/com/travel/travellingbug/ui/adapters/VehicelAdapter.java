package com.travel.travellingbug.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.travel.travellingbug.R;
import com.travel.travellingbug.models.VehicelModel;

import java.util.ArrayList;


public class VehicelAdapter extends RecyclerView.Adapter<VehicelAdapter.ViewHolder> {

    Context context;
    ArrayList<VehicelModel> list;

    int selectedPosition=-1;

    VehicleClickListener vehicleClickListener;

    public VehicelAdapter(Context context, ArrayList<VehicelModel> list) {
        this.context = context;
        this.list = list;
    }

    public VehicelAdapter(Context context, ArrayList<VehicelModel> list, VehicleClickListener vehicleClickListener) {
        this.context = context;
        this.list = list;
        this.vehicleClickListener = vehicleClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.design_vehicle_brand,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        VehicelModel vehicelModel = list.get(position);
        holder.brandNametv.setText(vehicelModel.getVal().toUpperCase());


        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                // call listener

                vehicleClickListener.onClick(position,vehicelModel);
                System.out.println("FROM ADAPTER user_id: "+vehicelModel.getVal());
                // update position
                selectedPosition=position;
                // notify
                notifyDataSetChanged();
            }
        });
//        holder.description.setText(VehicelModel.getDescription());


//        holder.preferenceContainer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(v.getContext(), UpdatePreference.class);
//                intent.putExtra("id", verifyIdMainActivityModel.getId());
//                intent.putExtra("title", verifyIdMainActivityModel.getTitle());
//                intent.putExtra("subtitle", verifyIdMainActivityModel.getDescription());
//                intent.putExtra("title_id", verifyIdMainActivityModel.getTitle_id());
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                v.getContext().startActivity(intent);
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView brandNametv;
        LinearLayout container;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            brandNametv = itemView.findViewById(R.id.brandNametv);
            container = itemView.findViewById(R.id.container);
        }
    }
}
