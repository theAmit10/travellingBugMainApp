package com.travel.travellingbug.ui.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
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
import com.squareup.picasso.Picasso;
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

    RelativeLayout errorLayout;
    RideRequest.UpcomingsAdapter upcomingsAdapter;

    String noofseat="",request_id="", person_id="",s_address="",d_address="",s_date = "",s_time = "",seat_left = "", profile_image = "",fare = "";

    String rating="0";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_request);

        errorLayout = findViewById(R.id.errorLayoutRl);
        recyclerView = findViewById(R.id.rideRequestRv);


        getIntentData();

        getRideRequest();
    }

    private void showAcceptedDialog() {

//        AlertDialog alertDialog = new AlertDialog.Builder(FirstActivity.getInstance()).create();
        Dialog confirmDialog = new Dialog(this);
        confirmDialog.setContentView(R.layout.schedule_dialog);

        TextView tvDone = confirmDialog.findViewById(R.id.tvDone);
        TextView bookingStatusTitleTv = confirmDialog.findViewById(R.id.bookingStatusTitleTv);
        TextView bookingStatusSubTitleTv = confirmDialog.findViewById(R.id.bookingStatusSubTitleTv);
        TextView tvDriverMsg = confirmDialog.findViewById(R.id.tvDriverMsg);

        bookingStatusTitleTv.setText("Accepted");

        bookingStatusSubTitleTv.setText("Ride Request has been Accepted successfully ");

        tvDriverMsg.setText("");

        confirmDialog.show();
        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upcomingsAdapter.notifyDataSetChanged();
                confirmDialog.dismiss();
            }
        });
    }

    private void showCanceldDialog() {

//        AlertDialog alertDialog = new AlertDialog.Builder(FirstActivity.getInstance()).create();
        Dialog confirmDialog = new Dialog(this);
        confirmDialog.setContentView(R.layout.schedule_dialog);

        TextView tvDone = confirmDialog.findViewById(R.id.tvDone);
        TextView bookingStatusTitleTv = confirmDialog.findViewById(R.id.bookingStatusTitleTv);
        TextView bookingStatusSubTitleTv = confirmDialog.findViewById(R.id.bookingStatusSubTitleTv);
        TextView tvDriverMsg = confirmDialog.findViewById(R.id.tvDriverMsg);

        bookingStatusTitleTv.setText("Cancelled");

        bookingStatusSubTitleTv.setText("Ride Request has been Cancelled successfully");

        tvDriverMsg.setText("");

        confirmDialog.show();
        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upcomingsAdapter.notifyDataSetChanged();
                confirmDialog.dismiss();
            }
        });
    }

    private void getRideRequest() {

        System.out.println("Getting Ride Data... ");
        customDialog = new CustomDialog(RideRequest.this);
        customDialog.setCancelable(false);
        customDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, URLHelper.UPCOMMING_TRIPS_DETAILS_ONE , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                System.out.println("size : "+response.length());
                System.out.println("Request Data : "+response);
                String location;


                try {
                    customDialog.dismiss();

                    JSONArray jsonArray = new JSONArray(response);

                    for(int i=0;i <jsonArray.length(); i++){

//                        JSONObject jsonObject = new JSONObject(response);
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        JSONArray filterArray = jsonObject.getJSONArray("filters");
                        if(response != null ){
                            upcomingsAdapter = new RideRequest.UpcomingsAdapter(filterArray);
                            System.out.println("filter length : "+filterArray.length());

//                            Toast.makeText(RideRequest.this, "filter length : "+filterArray.length(), Toast.LENGTH_SHORT).show();

                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());

                            if (upcomingsAdapter != null && upcomingsAdapter.getItemCount() > 0) {
                                recyclerView.setVisibility(View.VISIBLE);
                            errorLayout.setVisibility(View.GONE);
                                recyclerView.setAdapter(upcomingsAdapter);
                            } else {
                            errorLayout.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            }


                        }else {
                            Toast.makeText(RideRequest.this, "No Request Available", Toast.LENGTH_SHORT).show();

                        }


                    }




                } catch (JSONException e) {
                    customDialog.dismiss();
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RideRequest.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
                customDialog.dismiss();
            }

        }) {


            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("request_id",request_id);
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


    private void acceptRequest(String id){
        System.out.println("Accepting Request... ");
        customDialog = new CustomDialog(this);
        customDialog.setCancelable(false);
        customDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, URLHelper.ACCEPT_REQUEST , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if(response != null){
                    customDialog.dismiss();
                    showAcceptedDialog();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                customDialog.dismiss();
                Toast.makeText(RideRequest.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }

        }) {


            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id",id);
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

    private void cancelRequest(String id){
        System.out.println("Cancelling Request... ");
        customDialog = new CustomDialog(this);
        customDialog.setCancelable(false);
        customDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, URLHelper.CANCEL_REQUEST , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if(response != null){
                    customDialog.dismiss();
                    showCanceldDialog();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                customDialog.dismiss();
                Toast.makeText(RideRequest.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }

        }) {


            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id",id);
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
        s_address = getIntent().getStringExtra("s_address");
        d_address = getIntent().getStringExtra("d_address");
        request_id = getIntent().getStringExtra("request_id");
        s_date = getIntent().getStringExtra("s_date");
        s_time = getIntent().getStringExtra("s_time");
        seat_left = getIntent().getStringExtra("seat_left");
        fare = getIntent().getStringExtra("fare");

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
        public void onBindViewHolder(RideRequest.UpcomingsAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {

            holder.saddress.setText(s_address);
            holder.dropLocation.setText(d_address);
            holder.fare.setText(fare);

            try {
                if (!jsonArray.optJSONObject(position).optString("first_name", "").isEmpty()) {



//                    holder.listitemrating.setRating(Float.parseFloat("3.0"));
                    holder.nametv.setText(jsonArray.optJSONObject(position).optString("first_name"));
                    person_id = jsonArray.optJSONObject(position).optString("id");



                    holder.startTimeVal.setText(s_date + " "+s_time);

// getting taken seat
//                    JSONArray filterJsonArray = jsonArray.optJSONObject(position).optJSONArray("filters");
//                    if (filterJsonArray != null && filterJsonArray.length() > 0) {
//                        for (int j = 0; j < filterJsonArray.length(); j++) {
//                            JSONObject filterJsonObject = filterJsonArray.optJSONObject(j);
//                            System.out.println("j" + filterJsonObject.optString("user_id"));
//                            System.out.println("j" + SharedHelper.getKey(getApplicationContext(), "id"));
//                            if (filterJsonObject.optString("user_id").equalsIgnoreCase(SharedHelper.getKey(getApplicationContext(), "id"))) {
//                                holder.availableSeat.setText(filterJsonObject.optString("noofseats")+" Seat");
//                            }
//                        }
//                    }



//                    holder..setText(seat_left +" Seat left");
                    holder.availableSeat.setText(jsonArray.optJSONObject(position).optString("noofseats")+" Seat");





                    // Getting other details of profile

                    StringRequest request = new StringRequest(Request.Method.POST, URLHelper.GET_DETAILS_OF_ONE_USER, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            System.out.println("size : "+response.length());
                            System.out.println("data : "+response);

                            try {
                                JSONObject jsonObject = new JSONObject(response);

                                if (response != null) {
                                    System.out.println("data : "+jsonObject.toString());


                                    profile_image = URLHelper.BASE + "storage/app/public/" + jsonObject.optString("avatar");

                                    if(jsonObject.optString("rating") != null){
                                        holder.listitemrating.setRating(Float.parseFloat(jsonObject.optString("rating")));
                                        holder.ratingVal.setText("( "+Float.parseFloat(jsonObject.optString("rating"))+" Reviews )");

                                        rating =""+ Float.parseFloat(jsonObject.optString("rating"));
                                    }


                                    Picasso.get().load(URLHelper.BASE + "storage/app/public/" + jsonObject.optString("avatar"))
                                            .placeholder(R.drawable.ic_dummy_user).error(R.drawable.ic_dummy_user).into(holder.profileImgeIv);


                                }


                            } catch (JSONException e) {

                            }


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Error Found", Toast.LENGTH_SHORT).show();
                        }

                    }) {


                        @Override
                        public Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();
                            params.put("id", jsonArray.optJSONObject(position).optString("user_id"));
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

                    if(!jsonArray.optJSONObject(position).optString("status").equalsIgnoreCase("Pending")){
                        System.out.println("if");
                        System.out.println("! pending : "+!jsonArray.optJSONObject(position).optString("status").equalsIgnoreCase("Pending"));
                        if(jsonArray.optJSONObject(position).optString("status").equalsIgnoreCase("ACCEPTED")){
                            System.out.println("ACCEPTED : "+jsonArray.optJSONObject(position).optString("status").equalsIgnoreCase("ACCEPTED"));
                            holder.acceptBtn.setText("ACCEPTED");
                            holder.acceptBtn.setVisibility(View.VISIBLE);
                            holder.rejectBtn.setVisibility(View.GONE);

                        }else if(jsonArray.optJSONObject(position).optString("status").equalsIgnoreCase("CANCELLED")){
                            System.out.println("CANCELLED : "+jsonArray.optJSONObject(position).optString("status").equalsIgnoreCase("CANCELLED"));
                            holder.rejectBtn.setText("CANCELLED");
                            holder.rejectBtn.setVisibility(View.VISIBLE);
                            holder.acceptBtn.setVisibility(View.GONE);
                        }
                    }else {
                        System.out.println("else");
                        holder.acceptBtn.setVisibility(View.VISIBLE);
                        holder.rejectBtn.setVisibility(View.VISIBLE);
                    }

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

            holder.acceptBtn.setOnClickListener(v -> {
                acceptRequest(person_id);
            });

            holder.rejectBtn.setOnClickListener(v -> {
                cancelRequest(person_id);
            });

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

            holder.rContainer.setOnClickListener(view -> {
                Intent intent = new Intent(getApplicationContext(), RideRequestDetailsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Log.e("Intent", "" + jsonArray.optJSONObject(position).toString());
                intent.putExtra("post_value", jsonArray.optJSONObject(position).toString());
                intent.putExtra("first_name", jsonArray.optJSONObject(position).optString("first_name"));
                intent.putExtra("rating", rating);
                intent.putExtra("rating_val", "( "+rating+" Reviews )");
                intent.putExtra("profile_image", profile_image);
                intent.putExtra("user_id", jsonArray.optJSONObject(position).optString("user_id"));
                intent.putExtra("s_address", s_address);
                intent.putExtra("d_address", d_address);
                intent.putExtra("pick_up_date",s_date );
                intent.putExtra("pick_up_time", s_time);
                intent.putExtra("noofseat", jsonArray.optJSONObject(position).optString("noofseats"));
                intent.putExtra("fare", fare);
                intent.putExtra("request_id", jsonArray.optJSONObject(position).optString("request_id"));
                intent.putExtra("tag", "RideRequestDetails");
                intent.putExtra("person_id",jsonArray.optJSONObject(position).optString("id"));
                intent.putExtra("status",jsonArray.optJSONObject(position).optString("status"));
                startActivity(intent);



            });

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

            LinearLayout rContainer;


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
                rContainer = itemView.findViewById(R.id.rContainer);


            }
        }
    }
}