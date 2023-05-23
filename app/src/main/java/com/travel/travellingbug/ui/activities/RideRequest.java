package com.travel.travellingbug.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.travel.travellingbug.ClassLuxApp;
import com.travel.travellingbug.R;
import com.travel.travellingbug.helper.CustomDialog;
import com.travel.travellingbug.helper.SharedHelper;
import com.travel.travellingbug.helper.URLHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RideRequest extends AppCompatActivity {

    CustomDialog customDialog;
    RecyclerView recyclerView;
    RideRequest.UpcomingsAdapter upcomingsAdapter;

    String noofseat="",request_id="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_request);

        recyclerView = findViewById(R.id.rideRequestRv);
        getIntentData();

        getRideRequest();
    }

    private void getRideRequest() {

//        customDialog = new CustomDialog(getApplicationContext());
//        customDialog.setCancelable(false);
//        customDialog.show();
//
//        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URLHelper.MY_PUBLISH_UPCOMMING_TRIPS, response -> {
//
//            Log.v("GetHistoryList", response.toString());
//            if (response != null) {
//                upcomingsAdapter = new RideRequest.UpcomingsAdapter(response);
//                //  recyclerView.setHasFixedSize(true);
//                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
//                recyclerView.setLayoutManager(mLayoutManager);
//                recyclerView.setItemAnimator(new DefaultItemAnimator());
//                if (upcomingsAdapter != null && upcomingsAdapter.getItemCount() > 0) {
//                    recyclerView.setVisibility(View.VISIBLE);
//                    recyclerView.setAdapter(upcomingsAdapter);
//                } else {
//                    recyclerView.setVisibility(View.GONE);
//                }
//
//            } else {
//                recyclerView.setVisibility(View.GONE);
//            }
//
//            customDialog.dismiss();
//
//        }, error -> {
//            customDialog.dismiss();
//            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
//        }) {
//            @Override
//            public Map<String, String> getHeaders() {
//                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("X-Requested-With", "XMLHttpRequest");
//                headers.put("Authorization", "Bearer " + SharedHelper.getKey(getApplicationContext(), "access_token"));
//                return headers;
//            }
//        };
//
//        ClassLuxApp.getInstance().addToRequestQueue(jsonArrayRequest);



        StringRequest request = new StringRequest(Request.Method.POST, URLHelper.BOOK_FOR_UPCOMMING_TRIPS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                System.out.println("size : "+response.length());
                System.out.println("data : "+response);
                String location;

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsonObject1 = jsonObject.optJSONObject("data");
                    JSONArray filterArray = jsonObject1.getJSONArray("filters");
                    if(filterArray != null ){
                        upcomingsAdapter = new RideRequest.UpcomingsAdapter(filterArray);
                        System.out.println("filter length : "+filterArray.length());

                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());

                        if (upcomingsAdapter != null && upcomingsAdapter.getItemCount() > 0) {
                            recyclerView.setVisibility(View.VISIBLE);
//                            errorLayout.setVisibility(View.GONE);
                            recyclerView.setAdapter(upcomingsAdapter);
                        } else {
//                            errorLayout.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }


                    }else {
                        Toast.makeText(RideRequest.this, "No Request Available", Toast.LENGTH_SHORT).show();
                    }





                } catch (JSONException e) {
                    System.out.println("Error : "+e.getMessage());
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RideRequest.this, "Error Found", Toast.LENGTH_SHORT).show();
            }

        }) {


            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("request_id",request_id);
                params.put("noofseat", noofseat);
                return params;
            }
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("X-Requested-With", "XMLHttpRequest");
                headers.put("Authorization", "Bearer " + SharedHelper.getKey(getApplicationContext(), "access_token"));
                return headers;
            }

        };

        ClassLuxApp.getInstance().addToRequestQueue(request);


    }

    private String getMonth(String date) throws ParseException {
        Date d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        String monthName = new SimpleDateFormat("MMM").format(cal.getTime());
        return monthName;
    }

    private String getDate(String date) throws ParseException {
        Date d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        String dateName = new SimpleDateFormat("dd").format(cal.getTime());
        return dateName;
    }

    private String getYear(String date) throws ParseException {
        Date d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        String yearName = new SimpleDateFormat("yyyy").format(cal.getTime());
        return yearName;
    }

    private String getTime(String date) throws ParseException {
        Date d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        String timeName = new SimpleDateFormat("hh:mm a").format(cal.getTime());
        return timeName;
    }

    private void getIntentData() {
        noofseat = getIntent().getStringExtra("noofseat");
        request_id = getIntent().getStringExtra("request_id");

        System.out.println("noofseat : "+noofseat);
        System.out.println("request_id : "+request_id);
    }

    private class UpcomingsAdapter extends RecyclerView.Adapter<RideRequest.UpcomingsAdapter.MyViewHolder> {
        JSONArray jsonArray;

        public UpcomingsAdapter(JSONArray array) {
            this.jsonArray = array;
        }

        public void append(JSONArray array) {
            try {
                for (int i = 0; i < array.length(); i++) {
                    this.jsonArray.put(array.get(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public RideRequest.UpcomingsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.design_ride_request, parent, false);
            return new RideRequest.UpcomingsAdapter.MyViewHolder(itemView);
        }


        @Override
        public void onBindViewHolder(RideRequest.UpcomingsAdapter.MyViewHolder holder, final int position) {

            try {
                if (!jsonArray.optJSONObject(position).optString("first_name", "").isEmpty()) {
//                    String form = jsonArray.optJSONObject(position).optString("first_name");
                    //                        holder.tripDate.setText(getDate(form) + "th " + getMonth(form) + " " + "at " + getTime(form));
//                        holder.tripId.setText(jsonArray.optJSONObject(position).optString("booking_id"));

                    holder.listitemrating.setRating(Float.parseFloat("3.0"));
                    holder.nametv.setText(jsonArray.optJSONObject(position).optString("first_name"));

//
//                        holder.txtSource.setText(jsonArray.optJSONObject(position).optString("s_address"));
//                        holder.txtDestination.setText(jsonArray.optJSONObject(position).optString("d_address"));
//                        if(jsonArray.optJSONObject(position).optString("status").equalsIgnoreCase("PENDING")){
//                            holder.status.setBackgroundResource(R.drawable.auth_btn_yellow_bg);
//                        }
//                        holder.status.setText(jsonArray.optJSONObject(position).optString("status"));

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
//            try {
//                JSONObject serviceObj = jsonArray.getJSONObject(position).optJSONObject("service_type");
//                if (serviceObj != null) {
//                    holder.car_name.setText(serviceObj.optString("name"));
//                    //holder.tripAmount.setText(SharedHelper.getKey(context, "currency")+serviceObj.optString("price"));
//                    Picasso.get().load(serviceObj.optString("image"))
//                            .placeholder(R.drawable.car_select).error(R.drawable.car_select).into(holder.driver_image);
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }

//            holder.btnCancel.setOnClickListener(v -> {
//                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                builder.setMessage(getString(R.string.cencel_request))
//                        .setCancelable(false)
//                        .setPositiveButton("YES", (dialog, id) -> {
//                            dialog.dismiss();
//                            Log.e("canceljson", jsonArray + "j");
//                            cancelRequest(jsonArray.optJSONObject(position).optString("id"));
//                        })
//                        .setNegativeButton("NO", (dialog, id) -> dialog.dismiss());
//                AlertDialog alert = builder.create();
//                alert.show();
//            });

//            holder.btnStart.setOnClickListener(view -> {
//                //Toast.makeText(getActivity(),"Start Ride",Toast.LENGTH_SHORT).show();
//                Log.e("Intent", "" + jsonArray.optJSONObject(position).toString());
//                JSONArray array = new JSONArray();
//                JSONObject req = new JSONObject();
//                try {
//                    JSONObject object = (JSONObject) new JSONTokener(jsonArray.optJSONObject(position).toString()).nextValue();
//                    req.put("request", object);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                array.put(req);
//                Log.e("TAG", "REQ: " + array);
//                Intent i = new Intent(getActivity(), MainActivity.class);
//                i.putExtra("datas", array.toString());
//                i.putExtra("type", "SCHEDULED");
//                startActivity(i);
//            });

        }

        @Override
        public int getItemCount() {
            return jsonArray.length();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView nametv, ratingVal;
            RatingBar listitemrating;
            TextView fare, availableSeat, saddress,dropLocation,startTimeVal;
            ImageView  profileImgeIv;
            Button btnCancel, btnStart,rejectBtn,acceptBtn;


            public MyViewHolder(View itemView) {
                super(itemView);
                nametv = itemView.findViewById(R.id.nametv);
                ratingVal = itemView.findViewById(R.id.ratingVal);
                fare = itemView.findViewById(R.id.fare);

                availableSeat = itemView.findViewById(R.id.availableSeat);
                profileImgeIv = itemView.findViewById(R.id.profileImgeIv);
                btnCancel = itemView.findViewById(R.id.btnCancel);
                btnStart = itemView.findViewById(R.id.btnStart);
                rejectBtn = itemView.findViewById(R.id.rejectBtn);
                acceptBtn = itemView.findViewById(R.id.acceptBtn);
                saddress = itemView.findViewById(R.id.saddress);
                dropLocation = itemView.findViewById(R.id.dropLocation);
                startTimeVal = itemView.findViewById(R.id.startTimeVal);

                listitemrating = itemView.findViewById(R.id.listitemrating);

                itemView.setOnClickListener(view -> {
                    Intent intent = new Intent(getApplicationContext(), RideRequestDetailsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    Log.e("Intent", "" + jsonArray.optJSONObject(getAdapterPosition()).toString());
                    intent.putExtra("post_value", jsonArray.optJSONObject(getAdapterPosition()).toString());
                    intent.putExtra("first_name", jsonArray.optJSONObject(getAdapterPosition()).optString("first_name"));
                    intent.putExtra("rating", "rating");
                    intent.putExtra("rating_val", "rating");
                    intent.putExtra("profile_image", "rating");
                    intent.putExtra("user_id", "rating");
                    intent.putExtra("s_address", "rating");
                    intent.putExtra("d_address", "rating");
                    intent.putExtra("pick_up_date", "rating");
                    intent.putExtra("pick_up_time", "rating");
                    intent.putExtra("noofseat", "rating");
                    intent.putExtra("fare", "rating");
                    intent.putExtra("request_id", "rating");
                    intent.putExtra("tag", "RideRequestDetails");
                    startActivity(intent);

                });
            }
        }
    }
}