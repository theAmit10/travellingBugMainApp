package com.travel.travellingbug.ui.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
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
import com.travel.travellingbug.chat.InboxChatActivity;
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

    TextView startRideTv;
    RideRequest.UpcomingsAdapter upcomingsAdapter;

    String noofseat="",request_id="", person_id="",s_address="",d_address="",s_date = "",s_time = "",seat_left = "", profile_image = "",fare = "";
    String rating="",ratingVal="";
    String booking_id = "", status = "", payment_mode = "", estimated_fare = "", verification_code = "", static_map = "", first_name = "", provider_phone_number = "", avatar = "";

    String post_value = "", tag = "";
    String current_trip_user_id = "";
    String provider_id = "";
    String s_latitude = "",s_longitude="",d_latitude="",d_longitude="";
    String trip_distance = "";
    String trip_fare = "";
    String distance = "";

    // Today Worked on the Publish Section Backend of the Travelling Bug Project

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_request);

        try {
            errorLayout = findViewById(R.id.errorLayoutRl);
            recyclerView = findViewById(R.id.rideRequestRv);
            getIntentData();
            startRideTv = findViewById(R.id.startRideTv);
            getRideRequest();
            startRideTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    s_address = getIntent().getStringExtra("s_address");
                    d_address = getIntent().getStringExtra("d_address");
                    request_id = getIntent().getStringExtra("request_id");
                    s_date = getIntent().getStringExtra("s_date");
                    s_time = getIntent().getStringExtra("s_time");
                    seat_left = getIntent().getStringExtra("seat_left");
                    fare = getIntent().getStringExtra("fare");

                    // for history screen to start ride
                    request_id = getIntent().getStringExtra("request_id");
                    s_address = getIntent().getStringExtra("s_address");
                    d_address = getIntent().getStringExtra("d_address");
                    s_date = getIntent().getStringExtra("s_date");
                    s_time = getIntent().getStringExtra("s_time");
                    status = getIntent().getStringExtra("status");
                    payment_mode = getIntent().getStringExtra("payment_mode");
                    estimated_fare = getIntent().getStringExtra("estimated_fare");
                    verification_code = getIntent().getStringExtra("verification_code");
                    static_map = getIntent().getStringExtra("static_map");
                    first_name = getIntent().getStringExtra("first_name");
                    rating = getIntent().getStringExtra("rating");
                    avatar = getIntent().getStringExtra("avatar");
                    booking_id = getIntent().getStringExtra("booking_id");
                    current_trip_user_id = getIntent().getStringExtra("current_trip_user_id");
                    post_value = getIntent().getStringExtra("post_value");
                    tag = getIntent().getStringExtra("tag");

                    System.out.println("request_id : "+request_id);
                    System.out.println("RR INTENT rating : "+rating);


                    if(startRideTv.getText().toString().equalsIgnoreCase("COMPLETED")){
                        Toast.makeText(RideRequest.this, "Ride already completed", Toast.LENGTH_SHORT).show();
                    }else if(startRideTv.getText().toString().equalsIgnoreCase("CANCELLED")){
                        Toast.makeText(RideRequest.this, "Ride has been cancelled", Toast.LENGTH_SHORT).show();
                    }else {
                        Intent intent = new Intent(RideRequest.this, HistoryDetails.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        Log.e("Intent", "" + post_value);
                        intent.putExtra("post_value", ""+post_value);
                        intent.putExtra("tag", tag);
                        intent.putExtra("request_id", request_id);
                        intent.putExtra("s_address", s_address);
                        intent.putExtra("d_address", d_address);
                        intent.putExtra("booking_id", booking_id);
                        intent.putExtra("s_date", s_date);
                        intent.putExtra("s_time", s_time);
                        intent.putExtra("status", status);
                        intent.putExtra("payment_mode", payment_mode);
                        intent.putExtra("estimated_fare", estimated_fare);
                        intent.putExtra("verification_code", verification_code);
                        intent.putExtra("static_map", static_map);
                        intent.putExtra("first_name", first_name);
                        intent.putExtra("rating", rating);
                        intent.putExtra("avatar", avatar);
                        intent.putExtra("current_trip_user_id", current_trip_user_id);
                        startActivity(intent);
                    }


                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }


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
                getRideRequest();
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
                getRideRequest();
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

//                        s_latitude = "",s_longitude="",d_latitude="",d_longitude="";
                        s_latitude = jsonObject.optString("s_latitude");
                        s_longitude = jsonObject.optString("s_longitude");
                        d_latitude = jsonObject.optString("d_latitude");
                        d_longitude = jsonObject.optString("d_longitude");
                        provider_id = jsonObject.optString("provider_id");

                        if(jsonObject.optString("status").equalsIgnoreCase("COMPLETED")){
                            startRideTv.setText("Completed");
                        }else if(jsonObject.optString("status").equalsIgnoreCase("CANCELLED")){
                            startRideTv.setText("Cancelled");
                        }


                        startRideTv.setVisibility(View.VISIBLE);

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
                params.put("total_amount",trip_fare);
                params.put("totaldistance",trip_distance);
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
        distance = getIntent().getStringExtra("distance");

        // for history screen to start ride
        request_id = getIntent().getStringExtra("request_id");
        s_address = getIntent().getStringExtra("s_address");
        d_address = getIntent().getStringExtra("d_address");
        s_date = getIntent().getStringExtra("s_date");
        s_time = getIntent().getStringExtra("s_time");
        status = getIntent().getStringExtra("status");
        payment_mode = getIntent().getStringExtra("payment_mode");
        estimated_fare = getIntent().getStringExtra("estimated_fare");
        verification_code = getIntent().getStringExtra("verification_code");
        static_map = getIntent().getStringExtra("static_map");
        first_name = getIntent().getStringExtra("first_name");
        rating = getIntent().getStringExtra("rating");
        avatar = getIntent().getStringExtra("avatar");
        booking_id = getIntent().getStringExtra("booking_id");
        current_trip_user_id = getIntent().getStringExtra("current_trip_user_id");
        post_value = getIntent().getStringExtra("post_value");
        tag = getIntent().getStringExtra("tag");
        provider_phone_number = getIntent().getStringExtra("provider_phone_number");

        System.out.println("request_id : "+request_id);
        System.out.println("RR INTENT rating : "+rating);
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


            try {
                if (!jsonArray.optJSONObject(position).optString("first_name", "").isEmpty()) {


                    holder.nametv.setText(jsonArray.optJSONObject(position).optString("first_name"));
                    person_id = jsonArray.optJSONObject(position).optString("id");

                    holder.startTimeVal.setText(s_date + " "+s_time);
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

                                    if(!jsonObject.optString("asuser_rating").equalsIgnoreCase("null")){
                                        holder.listitemrating.setRating(Float.parseFloat(jsonObject.optString("asuser_rating")));
                                        holder.ratingVal.setText("( "+jsonObject.optString("asuser_noofrating")+" Reviews )");

                                        rating =""+ Float.parseFloat(jsonObject.optString("asuser_rating"));
                                        ratingVal =""+ jsonObject.optString("asuser_noofrating");
                                    }else {
                                        holder.listitemrating.setRating(0);
                                        holder.ratingVal.setText("( 0"+" Reviews )");

                                        rating =""+0;
                                        ratingVal =""+ jsonObject.optString("asuser_noofrating");
                                    }


                                    Picasso.get().load(URLHelper.BASE + "storage/app/public/" + jsonObject.optString("avatar"))
                                            .placeholder(R.drawable.ic_dummy_user).error(R.drawable.ic_dummy_user).into(holder.profileImgeIv);


                                }


                            } catch (JSONException e) {
                                    e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                            error.printStackTrace();
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
                            holder.acceptBtn.setClickable(false);

                            holder.acceptBtn.setVisibility(View.GONE);
                            holder.rejectBtn.setVisibility(View.GONE);
                            holder.afterAcceptedContainerLL.setVisibility(View.VISIBLE);

                        }else if(jsonArray.optJSONObject(position).optString("status").equalsIgnoreCase("CANCELLED")){
                            System.out.println("CANCELLED : "+jsonArray.optJSONObject(position).optString("status").equalsIgnoreCase("CANCELLED"));
                            holder.rejectBtn.setText("CANCELLED");
                            holder.rejectBtn.setClickable(false);
                            holder.rejectBtn.setVisibility(View.VISIBLE);
                            holder.acceptBtn.setVisibility(View.GONE);
                        }
                    }else {
                        System.out.println("else");
                        holder.acceptBtn.setVisibility(View.VISIBLE);
                        holder.rejectBtn.setVisibility(View.VISIBLE);
                    }



                    // for fare details

                    try {
//                        System.out.println("Fare : "+con + jsonObject.optString("estimated_fare"));

                        Double fares = Double.valueOf(fare);
                        int no_of_seat = Integer.parseInt(jsonArray.optJSONObject(position).optString("noofseats"));
                        Double c_fare = fares * no_of_seat;
                        String calculated_fare = URLHelper.INR_SYMBOL + c_fare;
                        fare = calculated_fare;
                        trip_fare = c_fare+"";

                        holder.fare.setText(calculated_fare);

                            trip_distance = distance;

                        System.out.println("DISTANCE TRIP : "+trip_distance);
                        System.out.println("DISTANCE TRIP trip_fare : "+trip_fare);

                    }catch (Exception e){
                        e.printStackTrace();
                    }



//                    try {
//                        StringRequest requestFare = new StringRequest(Request.Method.GET, URLHelper.ESTIMATED_FARE_AND_DISTANCE + "?s_latitude=" + s_latitude + "&s_longitude=" + s_longitude + "&d_latitude=" + d_latitude + "&d_longitude=" + d_longitude + "&service_type=2", new Response.Listener<String>() {
//                            @Override
//                            public void onResponse(String response) {
//
//
//                                try {
//                                    JSONObject jsonObject = new JSONObject(response);
//
//                                    if (response != null) {
//                                        System.out.println("payment details estimated data : " + jsonObject.toString());
//                                        jsonObject.optString("estimated_fare");
//                                        jsonObject.optString("distance");
//                                        jsonObject.optString("time");
//                                        jsonObject.optString("tax_price");
//                                        jsonObject.optString("base_price");
//                                        jsonObject.optString("discount");
//                                        jsonObject.optString("currency");
//
//                                        String con = jsonObject.optString("currency") + " ";
//
//                                        System.out.println("ESTIMATED FARE STATUS :" + response.toString());
//
//
//                                        try {
//                                            System.out.println("Fare : "+con + jsonObject.optString("estimated_fare"));
//
//                                            Double fares = Double.valueOf(jsonObject.optString("estimated_fare"));
//                                            int no_of_seat = Integer.parseInt(jsonArray.optJSONObject(position).optString("noofseats"));
//                                            Double c_fare = fares * no_of_seat;
//                                            String calculated_fare = con + c_fare;
//                                            fare = calculated_fare;
//                                            trip_fare = c_fare+"";
//
//                                            holder.fare.setText(calculated_fare);
//                                            if(jsonObject.optString("distance") != null){
//                                                trip_distance = jsonObject.optString("distance");
//                                            }
//                                            System.out.println("DISTANCE TRIP : "+trip_distance);
//                                            System.out.println("DISTANCE TRIP trip_fare : "+trip_fare);
//
//                                        }catch (Exception e){
//                                            e.printStackTrace();
//                                        }
//
//                                    }
//
//                                } catch (JSONException e) {
//
//                                    e.printStackTrace();
//                                }
//
//
//                            }
//                        }, new Response.ErrorListener() {
//                            @Override
//                            public void onErrorResponse(VolleyError error) {
//
//                                try {
//                                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//
//                            }
//
//                        }) {
//
//
//
//
//                            @Override
//                            public Map<String, String> getHeaders() {
//                                HashMap<String, String> headers = new HashMap<String, String>();
//                                headers.put("X-Requested-With", "XMLHttpRequest");
//                                headers.put("Authorization", "Bearer " + SharedHelper.getKey(getApplicationContext(), "access_token"));
//                                return headers;
//                            }
//
//                        };
//
//                        ClassLuxApp.getInstance().addToRequestQueue(requestFare);
//
//
//
//                    }catch (Exception e){
//                        e.printStackTrace();
//                    }


                    holder.acceptBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(jsonArray.optJSONObject(position).optString("status").equalsIgnoreCase("ACCEPTED")){
                                Toast.makeText(RideRequest.this, "You have already accepted.", Toast.LENGTH_SHORT).show();
                            }else{
                                acceptRequest(jsonArray.optJSONObject(position).optString("id"));
                            }
                        }
                    });

                    holder.rejectBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (jsonArray.optJSONObject(position).optString("status").equalsIgnoreCase("CANCELLED")){
                                Toast.makeText(RideRequest.this, "Already cancelled.", Toast.LENGTH_SHORT).show();
                            }else {
                                cancelRequest(jsonArray.optJSONObject(position).optString("id"));
                            }
                        }
                    });

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

            holder.rContainer.setOnClickListener(view -> {
                Intent intent = new Intent(getApplicationContext(), RideRequestDetailsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Log.e("Intent", "" + jsonArray.optJSONObject(position).toString());
                intent.putExtra("post_value", jsonArray.optJSONObject(position).toString());
                intent.putExtra("first_name", jsonArray.optJSONObject(position).optString("first_name"));
                intent.putExtra("rating", rating);
                intent.putExtra("rating_val", "( "+ratingVal+" Reviews )");
                intent.putExtra("profile_image", profile_image);
                intent.putExtra("user_id", jsonArray.optJSONObject(position).optString("user_id"));
                intent.putExtra("id", jsonArray.optJSONObject(position).optString("id"));
                System.out.println("IC userid -> "+jsonArray.optJSONObject(position).optString("user_id"));
                System.out.println("IC  id -> "+jsonArray.optJSONObject(position).optString("id"));
//                jsonArray.optJSONObject(position).optString("id")
                intent.putExtra("s_address", s_address);
                intent.putExtra("d_address", d_address);
                intent.putExtra("pick_up_date",s_date );
                intent.putExtra("pick_up_time", s_time);
                intent.putExtra("provider_id", provider_id);
                intent.putExtra("noofseat", jsonArray.optJSONObject(position).optString("noofseats"));
                intent.putExtra("fare", fare);
                intent.putExtra("request_id", jsonArray.optJSONObject(position).optString("request_id"));
                intent.putExtra("tag", "RideRequestDetails");
                intent.putExtra("person_id",jsonArray.optJSONObject(position).optString("id"));
                intent.putExtra("status",jsonArray.optJSONObject(position).optString("status"));
                intent.putExtra("trip_distance", trip_distance);
                intent.putExtra("trip_fare", trip_fare);
                startActivity(intent);
            });

            // For Calling
            holder.trackBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (provider_phone_number != null && !provider_phone_number.equalsIgnoreCase("null") && provider_phone_number.length() > 0) {
                        System.out.println("val if 1 : " + provider_phone_number);
                        try {
                            Intent intentCall = new Intent(Intent.ACTION_DIAL);
                            intentCall.setData(Uri.parse("tel:" + provider_phone_number));
                            startActivity(intentCall);
                            System.out.println("val if 2 : " + provider_phone_number);

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    } else {
                        System.out.println("val error : " + provider_phone_number);
                        Toast.makeText(RideRequest.this, "User do not have a valid mobile number", Toast.LENGTH_SHORT).show();
                    }

                }
            });

            holder.messageBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Toast.makeText(RideRequest.this, "Getting Message ", Toast.LENGTH_SHORT).show();
                    Intent intentChat = new Intent(RideRequest.this, InboxChatActivity.class);
                    intentChat.putExtra("requestId", jsonArray.optJSONObject(position).optString("request_id"));
                    intentChat.putExtra("providerId", provider_id);
                    intentChat.putExtra("userId", jsonArray.optJSONObject(position).optString("user_id"));
                    intentChat.putExtra("userName", jsonArray.optJSONObject(position).optString("first_name"));
                    intentChat.putExtra("messageType", "up");
                    System.out.println("request id: "+jsonArray.optJSONObject(position).optString("request_id"));
                    startActivity(intentChat);
                }
            });

            holder.pickUpBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(RideRequest.this, "Getting Ready for pickup", Toast.LENGTH_SHORT).show();


                }
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
            Button btnCancel, btnStart,rejectBtn,acceptBtn,trackBtn,messageBtn,pickUpBtn;
            LinearLayout rContainer,afterAcceptedContainerLL;

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


                afterAcceptedContainerLL = itemView.findViewById(R.id.afterAcceptedContainerLL);
                trackBtn = itemView.findViewById(R.id.trackBtn);
                messageBtn = itemView.findViewById(R.id.messageBtn);
                pickUpBtn = itemView.findViewById(R.id.pickUpBtn);




            }
        }
    }
}