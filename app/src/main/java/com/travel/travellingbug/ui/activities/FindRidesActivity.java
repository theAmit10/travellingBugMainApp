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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
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

import es.dmoral.toasty.Toasty;

public class FindRidesActivity extends AppCompatActivity {

    Button details, request;
    CustomDialog customDialog;

    boolean scheduleTrip = false;

    RecyclerView recyclerView;

    LinearLayout errorLayout;

    TextView from, destination;
    UpcomingsAdapter upcomingsAdapter;

    String s_latitude = "", s_longitude = "", d_latitude = "", d_longitude = "", s_address = "", d_address = "", service_type = "", distance = "", schedule_date = "", schedule_time = "", upcoming = "", use_wallet = "", payment_mode = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_rides);

        initData();
        getIntentData();
        setDataOnComponent();
        clickHandler();
//        getUpcomingList();

        sendRequestToGetProvider();


    }

    private void setDataOnComponent() {
        from.setText(s_address);
        destination.setText(d_address);
    }

    private void clickHandler() {

//        details.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getApplicationContext(), FindRideDetails.class));
//            }
//        });
//
//        request.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showConfirmDialog();
//            }
//        });
    }

    private void initData() {
//        details = findViewById(R.id.details);
//        request = findViewById(R.id.request);
        recyclerView = findViewById(R.id.findRideRequestRv);
        from = findViewById(R.id.from);
        destination = findViewById(R.id.destination);
        errorLayout = findViewById(R.id.errorLayout);

    }

    private void getIntentData() {

        s_latitude = getIntent().getStringExtra("s_latitude");
        s_longitude = getIntent().getStringExtra("s_longitude");
        d_latitude = getIntent().getStringExtra("d_latitude");
        d_longitude = getIntent().getStringExtra("d_longitude");
        s_address = getIntent().getStringExtra("s_address");
        d_address = getIntent().getStringExtra("d_address");
        service_type = getIntent().getStringExtra("service_type");
        distance = getIntent().getStringExtra("distance");
        schedule_date = getIntent().getStringExtra("schedule_date");
        schedule_time = getIntent().getStringExtra("schedule_time");
        upcoming = getIntent().getStringExtra("upcoming");
        use_wallet = getIntent().getStringExtra("use_wallet");
        payment_mode = getIntent().getStringExtra("payment_mode");


    }

    private void showConfirmDialog() {

//        AlertDialog alertDialog = new AlertDialog.Builder(FirstActivity.getInstance()).create();
        Dialog confirmDialog = new Dialog(this);
        confirmDialog.setContentView(R.layout.schedule_dialog);

        TextView tvDone = confirmDialog.findViewById(R.id.tvDone);
        TextView bookingStatusTitleTv = confirmDialog.findViewById(R.id.bookingStatusTitleTv);
        TextView bookingStatusSubTitleTv = confirmDialog.findViewById(R.id.bookingStatusSubTitleTv);
        TextView tvDriverMsg = confirmDialog.findViewById(R.id.tvDriverMsg);

        bookingStatusTitleTv.setText("Requested Successful");

        bookingStatusSubTitleTv.setText("Your ride has been Requested successfully ");

        tvDriverMsg.setText("");

        confirmDialog.show();
        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog.dismiss();
                Intent intent = new Intent(getApplicationContext(), HomeScreenActivity.class);
                startActivity(intent);
            }
        });
    }

    public void displayMessage(String toastString) {
        Toasty.info(getApplicationContext(), toastString, Toast.LENGTH_SHORT, true).show();
    }

    public void sendRequestToGetProvider() {
        StringRequest request = new StringRequest(Request.Method.POST, URLHelper.SEND_REQUEST_API_PROVIDER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                System.out.println("size : "+response.length());
                System.out.println("data : "+response);

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    System.out.println("size : "+jsonArray.length());
                    System.out.println("data : "+jsonArray);
                    System.out.println("data : "+jsonArray.getString(0));


                    if (response != null) {
                        System.out.println("data : "+jsonArray.getString(0));
                        upcomingsAdapter = new FindRidesActivity.UpcomingsAdapter(jsonArray);
                        //  recyclerView.setHasFixedSize(true);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext().getApplicationContext());
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        if (upcomingsAdapter != null && upcomingsAdapter.getItemCount() > 0) {
                            recyclerView.setVisibility(View.VISIBLE);
                    errorLayout.setVisibility(View.GONE);
                            recyclerView.setAdapter(upcomingsAdapter);
                        } else {
//                    errorLayout.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }

                    } else {
                errorLayout.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }

                    for(int i=0 ; i<jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        System.out.println("data : "+jsonObject.toString());
                        System.out.println("data : "+jsonObject.getString("s_address"));
                    }

                } catch (JSONException e) {
//                    throw new RuntimeException(e);
                    displayMessage(e.toString());
                    errorLayout.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }

//                Toast.makeText(FindRidesActivity.this, "Data Found succesfully..", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(FindRidesActivity.this, "Error Found", Toast.LENGTH_SHORT).show();
            }

        }) {


            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("s_latitude", s_latitude);
                params.put("s_longitude", s_longitude);
                params.put("d_latitude", d_latitude);
                params.put("d_longitude", d_longitude);
                params.put("s_address", s_address);
                params.put("d_address", d_address);

                params.put("service_type", service_type);
                params.put("distance", distance);
                params.put("schedule_date", schedule_date);
                params.put("schedule_time", schedule_time);
                params.put("upcoming", upcoming);
                params.put("use_wallet", use_wallet);
                params.put("payment_mode", payment_mode);
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

    public void getUpcomingList() {

//        customDialog = new CustomDialog(getApplicationContext());
//        customDialog.setCancelable(false);
//        customDialog.show();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URLHelper.MY_PUBLISH_UPCOMMING_TRIPS, response -> {

            Log.v("GetHistoryList", response.toString());
            if (response != null) {
                upcomingsAdapter = new FindRidesActivity.UpcomingsAdapter(response);
                //  recyclerView.setHasFixedSize(true);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext().getApplicationContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                if (upcomingsAdapter != null && upcomingsAdapter.getItemCount() > 0) {
                    recyclerView.setVisibility(View.VISIBLE);
//                    errorLayout.setVisibility(View.GONE);
                    recyclerView.setAdapter(upcomingsAdapter);
                } else {
//                    errorLayout.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }

            } else {
//                errorLayout.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }

//            customDialog.dismiss();

        }, error -> {
//            customDialog.dismiss();
            displayMessage(getString(R.string.something_went_wrong));
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("X-Requested-With", "XMLHttpRequest");
                headers.put("Authorization", "Bearer " + SharedHelper.getKey(getApplicationContext(), "access_token"));
                return headers;
            }
        };

        ClassLuxApp.getInstance().addToRequestQueue(jsonArrayRequest);
    }

    private class UpcomingsAdapter extends RecyclerView.Adapter<FindRidesActivity.UpcomingsAdapter.MyViewHolder> {
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
        public FindRidesActivity.UpcomingsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.design_find_ride, parent, false);
            return new FindRidesActivity.UpcomingsAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(FindRidesActivity.UpcomingsAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
//            Picasso.get().load(jsonArray.optJSONObject(position).optString("static_map")).placeholder(R.drawable.placeholder).error(R.drawable.placeholder).into(holder.tripImg);
            try {
                if (!jsonArray.optJSONObject(position).optString("schedule_at", "").isEmpty()) {
                    String form = jsonArray.optJSONObject(position).optString("schedule_at");
                    try {
                        holder.tripDate.setText(getDate(form) + "th " + getMonth(form) + " " + "at " + getTime(form));
//                        holder.tripId.setText(jsonArray.optJSONObject(position).optString("booking_id"));

                        holder.listitemrating.setRating(Float.parseFloat("3.0"));


                        holder.txtSource.setText(jsonArray.optJSONObject(position).optString("s_address"));
                        holder.txtDestination.setText(jsonArray.optJSONObject(position).optString("d_address"));
//                        if (jsonArray.optJSONObject(position).optString("status").equalsIgnoreCase("PENDING")) {
//                            holder.status.setBackgroundResource(R.drawable.auth_btn_yellow_bg);
//                        }
//                        holder.status.setText(jsonArray.optJSONObject(position).optString("status"));

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                JSONObject serviceObj = jsonArray.getJSONObject(position).optJSONObject("service_type");
                if (serviceObj != null) {
                    holder.car_name.setText(serviceObj.optString("name"));
                    //holder.tripAmount.setText(SharedHelper.getKey(context, "currency")+serviceObj.optString("price"));
//                    Picasso.get().load(serviceObj.optString("image"))
//                            .placeholder(R.drawable.car_select).error(R.drawable.car_select).into(holder.driver_image);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                JSONObject serviceObj = jsonArray.getJSONObject(position).optJSONObject("provider");
                if (serviceObj != null) {
//                    holder.car_name.setText(serviceObj.optString("name"));
                    //holder.tripAmount.setText(SharedHelper.getKey(context, "currency")+serviceObj.optString("price"));


//                    URLHelper.BASE + "storage/app/public/" + response.optString("picture")

                    Picasso.get().load(URLHelper.BASE + "storage/app/public/" + serviceObj.optString("avatar"))
                            .placeholder(R.drawable.car_select).error(R.drawable.car_select).into(holder.driver_image);

                    holder.profileNameTv.setText(serviceObj.optString("first_name"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

//            holder.btnCancel.setOnClickListener(v -> {
//                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
//                builder.setMessage(getString(R.string.cencel_request))
//                        .setCancelable(false)
//                        .setPositiveButton("YES", (dialog, id) -> {
//                            dialog.dismiss();
//                            Log.e("canceljson", jsonArray + "j");
////                            cancelRequest(jsonArray.optJSONObject(position).optString("id"));
//                        })
//                        .setNegativeButton("NO", (dialog, id) -> dialog.dismiss());
//                AlertDialog alert = builder.create();
//                alert.show();
//            });


            holder.request.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedHelper.putKey(getApplicationContext(), "current_status", "");
                    Log.e("Intent", "" + jsonArray.optJSONObject(position).toString());
//                    try {
////                        SharedHelper.putKey(getApplicationContext(), "request_id", "" + jsonArray.get(0).toString().optJSONObject("id"));
//                    } catch (JSONException e) {
//                        throw new RuntimeException(e);
//                    }
//                    scheduleTrip = !scheduledDate.equalsIgnoreCase("") && !scheduledTime.equalsIgnoreCase("");
                    // flowValue = 3;
                    //layoutChanges();
//                    flowValue = 0;
//                    layoutChanges();



                    SharedHelper.putKey(getApplicationContext(), "current_status", "");
                    SharedHelper.putKey(getApplicationContext(), "request_id", "80");
//                    scheduleTrip = !scheduledDate.equalsIgnoreCase("") && !scheduledTime.equalsIgnoreCase("");
                    // flowValue = 3;
                    //layoutChanges();

//                    flowValue = 0;
//                    layoutChanges();
                    Intent intent = new Intent(getApplicationContext(), TrackActivityDriver.class);
                    intent.putExtra("flowValue", 3);
                    startActivity(intent);


                }
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
//                Intent i = new Intent(getApplicationContext(), HomeScreenActivity.class);
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

            TextView tripTime, car_name;
            TextView tripDate, tripAmount, tripId, txtSource, txtDestination, status;

            ImageView tripImg, driver_image;
            Button btnCancel, btnStart;


            ImageView profileImageIv;
            TextView profileNameTv,reviewCount,fare,availableSeat,fromTv,destinationTv,startTimeVal,carTypeVal;
            RatingBar listitemrating;
            Button details,request;


            public MyViewHolder(View itemView) {
                super(itemView);
//                tripDate = itemView.findViewById(R.id.tripDate);
                tripTime = itemView.findViewById(R.id.tripTime);
//                tripAmount = itemView.findViewById(R.id.tripAmount);
//                tripImg = itemView.findViewById(R.id.tripImg);
//                car_name = itemView.findViewById(R.id.car_name);
//                driver_image = itemView.findViewById(R.id.driver_image);
                btnCancel = itemView.findViewById(R.id.btnCancel);
                btnStart = itemView.findViewById(R.id.btnStart);
//                tripId = itemView.findViewById(R.id.tripid);
//                txtDestination = itemView.findViewById(R.id.txtDestination);
//                txtSource = itemView.findViewById(R.id.txtSource);
//                status = itemView.findViewById(R.id.status);
//                listitemrating = itemView.findViewById(R.id.listitemrating);



                driver_image = itemView.findViewById(R.id.driver_image);
                profileNameTv = itemView.findViewById(R.id.profileNameTv);
                reviewCount = itemView.findViewById(R.id.reviewCount);
                tripAmount = itemView.findViewById(R.id.fare);
                availableSeat = itemView.findViewById(R.id.availableSeat);
                txtSource = itemView.findViewById(R.id.fromTv);
                txtDestination = itemView.findViewById(R.id.destinationTv);
                tripDate = itemView.findViewById(R.id.startTimeVal);
                car_name = itemView.findViewById(R.id.carTypeVal);
                listitemrating = itemView.findViewById(R.id.listitemrating);
                details = itemView.findViewById(R.id.details);
                request = itemView.findViewById(R.id.request);

                itemView.setOnClickListener(view -> {
                    Intent intent = new Intent(getApplicationContext(), HistoryDetails.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    Log.e("Intent", "" + jsonArray.optJSONObject(getAdapterPosition()).toString());
                    intent.putExtra("post_value", jsonArray.optJSONObject(getAdapterPosition()).toString());
                    intent.putExtra("tag", "upcoming_trips");
                    startActivity(intent);

                });
            }
        }
    }


}