package com.travel.travellingbug.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.travel.travellingbug.R;
import com.travel.travellingbug.models.InvoiceModel;
import com.travel.travellingbug.ui.activities.AddTrackingLinkActivity;

import java.util.ArrayList;

public class TrackingLinkUsetActivityAdapter extends RecyclerView.Adapter<TrackingLinkUsetActivityAdapter.ViewHolder> {


    Context context;
    ArrayList<InvoiceModel> list;

    public TrackingLinkUsetActivityAdapter(Context context, ArrayList<InvoiceModel> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.new_history_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        InvoiceModel invoiceModel = list.get(position);


        holder.datetime.setText(invoiceModel.getTime());
        holder.txtSource.setText(invoiceModel.getFromAddress());

        holder.txtDestination.setText(invoiceModel.getDestAddress());
        holder.status.setText(invoiceModel.getStatus());
        holder.userName.setText(invoiceModel.getUsername());

        holder.ratingVal.setText(invoiceModel.getRatingVal());
        holder.availableSeat.setText(invoiceModel.getSeat());
        holder.fare.setText(invoiceModel.getFare());
        holder.carTypeVal.setText(invoiceModel.getVehicleDetails());

        holder.listitemrating.setRating(Float.parseFloat(invoiceModel.getRating()));

        Picasso.get().load(invoiceModel.getImage()).placeholder(R.drawable.ic_dummy_user).error(R.drawable.ic_dummy_user).into(holder.profileImgeIv);

        holder.historyContainerLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                    Intent intent = new Intent(context, AddTrackingLinkActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    request_id = jsonArray.optJSONObject(position).optString("id");
//                    s_address = jsonArray.optJSONObject(position).optString("s_address");
//                    d_address = jsonArray.optJSONObject(position).optString("d_address");
//
//
//
//                    String form = jsonArray.optJSONObject(position).optString("schedule_at");
//                    try {
//                        s_date = getDate(form) + "th " + getMonth(form) + " " + getYear(form);
//                        s_time = getTime(form);
//                    } catch (ParseException e) {
//                        Toast.makeText(TrackingLinkActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
//                    }
//
//
//                    try {
//                        JSONObject serviceObj = jsonArray.getJSONObject(position).optJSONObject("service_type");
//                        if (serviceObj != null) {
//
////                        holder.tripAmount.setText("₹ "+serviceObj.optString("fixed"));
//                            if (serviceObj.optString("fixed") != null) {
//                                fare = "₹ " + serviceObj.optString("fixed");
//                            }
//                            //holder.tripAmount.setText(SharedHelper.getKey(context, "currency")+serviceObj.optString("price"));
////                        Picasso.get().load(serviceObj.optString("image"))
////                                .placeholder(R.drawable.car_select).error(R.drawable.car_select).into(holder.driver_image);
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }


//                Toast.makeText(getContext(), "" + request_id, Toast.LENGTH_SHORT).show();
//                    intent.putExtra("request_id", request_id);
//                    intent.putExtra("s_address", s_address);
//                    intent.putExtra("d_address", d_address);
//                    intent.putExtra("s_date", s_date);
//                    intent.putExtra("s_time", s_time);
//                    intent.putExtra("fare", fare);
                    intent.putExtra("latitude", invoiceModel.getSlat());
                    intent.putExtra("longitude", invoiceModel.getSlong());
//                    intent.putExtra("rideid", jsonArray.optJSONObject(position).optString("id"));
//                    intent.putExtra("seat_left", jsonArray.optJSONObject(position).optString("availablecapacity"));
                    context.startActivity(intent);



            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView datetime, txtSource, txtDestination, status, userName, ratingVal, availableSeat, fare, carTypeVal;

        RatingBar listitemrating;

        ImageView profileImgeIv, invoiceDownload;
        Button rateRider;

        LinearLayout historyContainerLL;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            datetime = itemView.findViewById(R.id.datetime);
            txtSource = itemView.findViewById(R.id.txtSource);
            txtDestination = itemView.findViewById(R.id.txtDestination);
            status = itemView.findViewById(R.id.status);
            rateRider = itemView.findViewById(R.id.rateRider);
            userName = itemView.findViewById(R.id.userName);
            ratingVal = itemView.findViewById(R.id.ratingVal);
            listitemrating = itemView.findViewById(R.id.listitemrating);
            profileImgeIv = itemView.findViewById(R.id.profileImgeIv);
            historyContainerLL = itemView.findViewById(R.id.historyContainerLL);

            carTypeVal = itemView.findViewById(R.id.carTypeVal);
            fare = itemView.findViewById(R.id.fare);
            availableSeat = itemView.findViewById(R.id.availableSeat);
            invoiceDownload = itemView.findViewById(R.id.invoiceDownload);
        }
    }
}
