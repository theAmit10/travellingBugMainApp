package com.travel.travellingbug.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.travel.travellingbug.R;
import com.travel.travellingbug.models.PaymentDataModel;

import java.util.ArrayList;

public class PaymentDataAdapter extends RecyclerView.Adapter<PaymentDataAdapter.ViewHolder> {
    Context context;
    ArrayList<PaymentDataModel> list;

    public PaymentDataAdapter(Context context, ArrayList<PaymentDataModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.design_payment_refund_history,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder , int position) {

        PaymentDataModel paymentDataModel = list.get(position);
        try {

            System.out.println("DATA getUsername : ->  : "+paymentDataModel.getUsername());
            System.out.println("DATA getType() : ->  : "+paymentDataModel.getType());
            System.out.println("DATA getProfileImage() : ->  : "+paymentDataModel.getProfileImage());
            System.out.println("DATA getTime() : ->  : "+paymentDataModel.getTime());


            holder.username.setText(paymentDataModel.getUsername());
            holder.fare.setText(paymentDataModel.getFare());
            holder.dateTimeVal.setText(paymentDataModel.getTime());
            holder.username.setText(paymentDataModel.getUsername());
            holder.type.setText(paymentDataModel.getType());
            Picasso.get().load(paymentDataModel.getProfileImage()).placeholder(R.drawable.ic_dummy_user).error(R.drawable.ic_dummy_user).into(holder.profileImgeIv);

        }catch (Exception e){
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImgeIv;
        TextView username,dateTimeVal,fare,type;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profileImgeIv = itemView.findViewById(R.id.profileImgeIv);
            username = itemView.findViewById(R.id.username);
            dateTimeVal = itemView.findViewById(R.id.dateTimeVal);
            fare = itemView.findViewById(R.id.fare);
            type = itemView.findViewById(R.id.type);
        }
    }
}
