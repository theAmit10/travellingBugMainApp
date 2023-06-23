package com.travel.travellingbug.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.travel.travellingbug.models.PassengerCallModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PassengerCallAdapter extends RecyclerView.Adapter<PassengerCallAdapter.ViewHolder> {

    Context context;
    ArrayList<PassengerCallModel> list;

    ItemClickListener itemClickListener;
    int selectedPosition=-1;

    String Passanger_image = "";

    public PassengerCallAdapter(Context context, ArrayList<PassengerCallModel> list) {
        this.context = context;
        this.list = list;
    }

    public PassengerCallAdapter(Context context, ArrayList<PassengerCallModel> list, ItemClickListener itemClickListener) {
        this.context = context;
        this.list = list;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

//        View view = LayoutInflater.from(context).inflate(R.layout.design_call_request_passenger,parent,false);

        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.design_call_request_passenger,parent,false);
        // return holder
        return new ViewHolder(view);

//        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        PassengerCallModel passengerCallModel = list.get(position);

        // Getting User details
        StringRequest request = new StringRequest(Request.Method.POST, URLHelper.GET_DETAILS_OF_ONE_USER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                System.out.println("size : " + response.length());
                System.out.println("data : " + response);

                try {
                    JSONObject jsonObjectUser = new JSONObject(response);

                    if (response != null) {

//                        String image = jsonObjectUser.optString("avatar");

//                        SharedHelper.putKey(context,"passanger_image",jsonObjectUser.optString("avatar"));
                        Picasso.get().load(URLHelper.BASE + "storage/app/public/" +jsonObjectUser.optString("avatar")).error(R.drawable.ic_dummy_user).placeholder(R.drawable.ic_dummy_user).into(holder.image);
//                        Passanger_image = jsonObjectUser.optString("avatar");




                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                                                        Toast.makeText(getContext(), "Error Found", Toast.LENGTH_SHORT).show();
                System.out.println("error : " + error);
            }

        }) {


            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", passengerCallModel.getUser_id());
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("X-Requested-With", "XMLHttpRequest");
                headers.put("Authorization", "Bearer " + SharedHelper.getKey(context, "access_token"));
                return headers;
            }

        };

        ClassLuxApp.getInstance().addToRequestQueue(request);





        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                // call listener
//                itemClickListener.onClick(position,list.get(position));
                itemClickListener.onClick(position,passengerCallModel);
                System.out.println("FROM ADAPTER user_id: "+passengerCallModel.getUser_id());
                System.out.println("FROM ADAPTER UID: "+passengerCallModel.getU_id());
                System.out.println("FROM ADAPTER PS : "+passengerCallModel.getProvider_status());
                System.out.println("FROM ADAPTER S : "+passengerCallModel.getStatus());
                Toast.makeText(context, "FROM Adapter"+passengerCallModel.getUser_id(), Toast.LENGTH_SHORT).show();
                // update position
                selectedPosition=position;
                // notify
                notifyDataSetChanged();
            }
        });


//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // get adapter position
//                int position=holder.getAdapterPosition();
//                // call listener
//                itemClickListener.onClick(position,list.get(position));
//                Toast.makeText(context, "FROM Adapter view"+passengerCallModel.getUser_id(), Toast.LENGTH_SHORT).show();
//                // update position
//                selectedPosition=position;
//                // notify
//                notifyDataSetChanged();
//            }
//        });

        // check conditions
        if(selectedPosition==position)
        {
            // When current position is equal
            // to selected position
            // set black background color
//            holder.cardView.setCardBackgroundColor(Color.parseColor("#000000"));
            // set white text color
//            holder.textView.setTextColor(Color.parseColor("#FFFFFF"));

        }
        else
        {
            // when current position is different
            // set white background
//            holder.cardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
            // set black text color
//            holder.textView.setTextColor(Color.parseColor("#000000"));

        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.filterImage);
        }
    }

}
