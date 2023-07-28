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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;
import com.travel.travellingbug.ClassLuxApp;
import com.travel.travellingbug.R;
import com.travel.travellingbug.helper.SharedHelper;
import com.travel.travellingbug.helper.URLHelper;
import com.travel.travellingbug.models.InvoiceModel;
import com.travel.travellingbug.ui.activities.AddTrackingLinkActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
//        holder.fare.setText(invoiceModel.getFare());
        holder.carTypeVal.setText(invoiceModel.getVehicleDetails());

        holder.listitemrating.setRating(Float.parseFloat(invoiceModel.getRating()));

        Picasso.get().load(invoiceModel.getImage()).placeholder(R.drawable.ic_dummy_user).error(R.drawable.ic_dummy_user).into(holder.profileImgeIv);


        // for fare details
        try {
            StringRequest request = new StringRequest(Request.Method.GET, URLHelper.ESTIMATED_FARE_AND_DISTANCE + "?s_latitude=" + invoiceModel.getSlat() + "&s_longitude=" + invoiceModel.getSlong() + "&d_latitude=" + invoiceModel.getDlat() + "&d_longitude=" + invoiceModel.getDlong() + "&service_type=2", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {


                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        if (response != null) {
                            System.out.println("payment details estimated data : " + jsonObject.toString());
                            jsonObject.optString("estimated_fare");
                            jsonObject.optString("distance");
                            jsonObject.optString("time");
                            jsonObject.optString("tax_price");
                            jsonObject.optString("base_price");
                            jsonObject.optString("discount");
                            jsonObject.optString("currency");

                            String con = jsonObject.optString("currency") + " ";


                            System.out.println("ESTIMATED FARE STATUS :" + response.toString());




                            try {
                                System.out.println("Fare : "+con + jsonObject.optString("estimated_fare"));

                                Double fares = Double.valueOf(jsonObject.optString("estimated_fare"));
                                String no_of_seat_string = String.valueOf(invoiceModel.getSeat().charAt(0));
                                System.out.println(no_of_seat_string);
                                int no_of_seat = Integer.parseInt(no_of_seat_string);
                                Double c_fare = fares * no_of_seat;
                                String calculated_fare = con + c_fare;

                                holder.fare.setText(calculated_fare);

                            }catch (Exception e){
                                e.printStackTrace();
                            }

                        }

                    } catch (JSONException e) {

                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    try {
                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            }) {




                @Override
                public Map<String, String> getHeaders() {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("X-Requested-With", "XMLHttpRequest");
                    headers.put("Authorization", "Bearer " + SharedHelper.getKey(context, "access_token"));
                    return headers;
                }

            };

            ClassLuxApp.getInstance().addToRequestQueue(request);



        }catch (Exception e){
            e.printStackTrace();
        }

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
