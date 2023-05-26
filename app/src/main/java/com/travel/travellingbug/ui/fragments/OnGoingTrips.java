package com.travel.travellingbug.ui.fragments;

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

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;
import com.travel.travellingbug.ClassLuxApp;
import com.travel.travellingbug.R;
import com.travel.travellingbug.helper.ConnectionHelper;
import com.travel.travellingbug.helper.CustomDialog;
import com.travel.travellingbug.helper.SharedHelper;
import com.travel.travellingbug.helper.URLHelper;
import com.travel.travellingbug.ui.activities.HistoryDetails;
import com.travel.travellingbug.ui.activities.MainActivity;
import com.travel.travellingbug.ui.activities.RideRequest;
import com.travel.travellingbug.ui.activities.SplashScreen;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class OnGoingTrips extends Fragment {

    Boolean isInternet;
    View rootView;
    UpcomingsAdapter upcomingsAdapter;
    RecyclerView recyclerView;
    RelativeLayout errorLayout;
    ConnectionHelper helper;
    CustomDialog customDialog;

    LinearLayout toolbar;
    ImageView backImg;

    String noofseat = "", request_id = "", s_address = "", d_address = "", s_date = "", s_time = "";

    String booking_id="",status="",payment_mode="",estimated_fare="",verification_code="",static_map="",first_name="",mobile="",avatar="",rating="";


    public OnGoingTrips() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_on_going_trips, container, false);
        findViewByIdAndInitialize();

        if (isInternet) {
            getUpcomingList();
        }

        backImg.setOnClickListener(v -> getFragmentManager().popBackStack());

        Bundle bundle = getArguments();
        String toolbar = null;
        if (bundle != null)
            toolbar = bundle.getString("toolbar");

        if (toolbar != null && toolbar.length() > 0) {
            this.toolbar.setVisibility(View.VISIBLE);
        }

        return rootView;
    }

    public void findViewByIdAndInitialize() {
        recyclerView = rootView.findViewById(R.id.recyclerView);
        errorLayout = rootView.findViewById(R.id.errorLayout);
        errorLayout.setVisibility(View.GONE);
        helper = new ConnectionHelper(getActivity());
        isInternet = helper.isConnectingToInternet();

        toolbar = rootView.findViewById(R.id.lnrTitle);
        backImg = rootView.findViewById(R.id.backArrow);
    }

    @Override
    public void onResume() {
        if (upcomingsAdapter != null) {
            getUpcomingList();
        }
        super.onResume();
    }

    public void getUpcomingList() {

        customDialog = new CustomDialog(getActivity());
        customDialog.setCancelable(false);
        customDialog.show();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URLHelper.MY_PUBLISH_UPCOMMING_TRIPS, response -> {

            Log.v("GetPublishedList", response.toString());
            if (response != null) {
                upcomingsAdapter = new UpcomingsAdapter(response);
                //  recyclerView.setHasFixedSize(true);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
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

            } else {
                errorLayout.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }

            customDialog.dismiss();

        }, error -> {
            customDialog.dismiss();
            errorLayout.setVisibility(View.VISIBLE);
            displayMessage(getString(R.string.something_went_wrong));
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("X-Requested-With", "XMLHttpRequest");
                headers.put("Authorization", "Bearer " + SharedHelper.getKey(getActivity(), "access_token"));
                return headers;
            }
        };

        ClassLuxApp.getInstance().addToRequestQueue(jsonArrayRequest);
    }

    public void GoToBeginActivity() {
        Intent mainIntent = new Intent(getActivity(), SplashScreen.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainIntent);
        getActivity().finish();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void displayMessage(String toastString) {
        Toasty.info(getActivity(), toastString, Toast.LENGTH_SHORT, true).show();
    }

    public void cancelRequest(final String request_id) {

        customDialog = new CustomDialog(getActivity());
        customDialog.setCancelable(false);
        customDialog.show();
        JSONObject object = new JSONObject();
        try {
            object.put("id", request_id);
            object.put("cancel_reason", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("objectcancel", object + "obj");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLHelper.CANCEL_REQUEST_API, object, response -> {
            Log.e("CancelRequestResponse", response.toString());
            customDialog.dismiss();
            getUpcomingList();
        }, error -> {
            customDialog.dismiss();
            displayMessage(getString(R.string.something_went_wrong));
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("X-Requested-With", "XMLHttpRequest");
                headers.put("Authorization", "Bearer " + SharedHelper.getKey(getActivity(), "access_token"));
                return headers;
            }
        };

        ClassLuxApp.getInstance().addToRequestQueue(jsonObjectRequest);
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

    private class UpcomingsAdapter extends RecyclerView.Adapter<UpcomingsAdapter.MyViewHolder> {
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
        public UpcomingsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.upcoming_list_item, parent, false);
            return new UpcomingsAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(UpcomingsAdapter.MyViewHolder holder, final int position) {

            Log.e("Published Data : ", "" + jsonArray.optJSONObject(position).toString());

            Picasso.get().load(jsonArray.optJSONObject(position).optString("static_map")).placeholder(R.drawable.placeholder).error(R.drawable.placeholder).into(holder.tripImg);
            try {
                if (!jsonArray.optJSONObject(position).optString("schedule_at", "").isEmpty()) {
                    String form = jsonArray.optJSONObject(position).optString("schedule_at");
                    try {
                        holder.tripDate.setText(getDate(form) + "th " + getMonth(form) + " " + "at " + getTime(form));

                        s_date = getDate(form) + "th " + getMonth(form) + " " + getYear(form);
                        s_time = getTime(form);

                        holder.tripId.setText(jsonArray.optJSONObject(position).optString("booking_id"));

                        holder.listitemrating.setRating(Float.parseFloat("3.0"));
                        request_id = jsonArray.optJSONObject(position).optString("id");

                        holder.txtSource.setText(jsonArray.optJSONObject(position).optString("s_address"));
                        holder.txtDestination.setText(jsonArray.optJSONObject(position).optString("d_address"));
                        if (jsonArray.optJSONObject(position).optString("status").equalsIgnoreCase("PENDING")) {
                            holder.status.setBackgroundResource(R.drawable.auth_btn_yellow_bg);
                        }
                        holder.status.setText(jsonArray.optJSONObject(position).optString("status"));

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
                    Picasso.get().load(serviceObj.optString("image"))
                            .placeholder(R.drawable.car_select).error(R.drawable.car_select).into(holder.driver_image);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // getting firstname
            try {
                JSONObject providerObj = jsonArray.getJSONObject(position).optJSONObject("provider");
                if (providerObj != null) {
                    holder.name.setText(providerObj.optString("first_name"));

                    Picasso.get().load(URLHelper.BASE + "storage/app/public/" + providerObj.optString("avatar"))
                            .placeholder(R.drawable.car_select).error(R.drawable.car_select).into(holder.driver_image);


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            holder.btnCancel.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(getString(R.string.cencel_request))
                        .setCancelable(false)
                        .setPositiveButton("YES", (dialog, id) -> {
                            dialog.dismiss();
                            Log.e("canceljson", jsonArray + "j");
                            cancelRequest(jsonArray.optJSONObject(position).optString("id"));
                        })
                        .setNegativeButton("NO", (dialog, id) -> dialog.dismiss());
                AlertDialog alert = builder.create();
                alert.show();
            });

            holder.btnStart.setOnClickListener(view -> {
                //Toast.makeText(getActivity(),"Start Ride",Toast.LENGTH_SHORT).show();
                Log.e("Intent", "" + jsonArray.optJSONObject(position).toString());
                JSONArray array = new JSONArray();
                JSONObject req = new JSONObject();
                try {
                    JSONObject object = (JSONObject) new JSONTokener(jsonArray.optJSONObject(position).toString()).nextValue();
                    req.put("request", object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                array.put(req);
                Log.e("TAG", "REQ: " + array);
                Intent i = new Intent(getActivity(), MainActivity.class);
                i.putExtra("datas", array.toString());
                i.putExtra("type", "SCHEDULED");
                startActivity(i);
            });


            holder.pContainer.setOnClickListener(view -> {
                Intent intent = new Intent(getActivity(), RideRequest.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                request_id = jsonArray.optJSONObject(position).optString("id");
                s_address = jsonArray.optJSONObject(position).optString("s_address");
                d_address = jsonArray.optJSONObject(position).optString("d_address");

                Toast.makeText(getContext(), "" + request_id, Toast.LENGTH_SHORT).show();
                intent.putExtra("request_id", request_id);
                intent.putExtra("s_address", s_address);
                intent.putExtra("d_address", d_address);
                intent.putExtra("s_date", s_date);
                intent.putExtra("s_time", s_time);
                startActivity(intent);

            });


            holder.pContainer.setOnLongClickListener(view -> {
                Intent intent = new Intent(getActivity(), HistoryDetails.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Log.e("Intent", "" + jsonArray.optJSONObject(position).toString());
                intent.putExtra("post_value", jsonArray.optJSONObject(position).toString());
                intent.putExtra("tag", "upcoming_trips");

                request_id = jsonArray.optJSONObject(position).optString("id");

                s_address = jsonArray.optJSONObject(position).optString("s_address");

                d_address = jsonArray.optJSONObject(position).optString("d_address");

                booking_id = jsonArray.optJSONObject(position).optString("booking_id");

                status = jsonArray.optJSONObject(position).optString("status");

                if(jsonArray.optJSONObject(position).optString("payment_mode") != null){
                    payment_mode = jsonArray.optJSONObject(position).optString("payment_mode");
                }

                if(jsonArray.optJSONObject(position).optString("estimated_fare") != null){
                    estimated_fare = jsonArray.optJSONObject(position).optString("estimated_fare");

                }

                if(jsonArray.optJSONObject(position).optString("verification_code") != null){
                    verification_code = jsonArray.optJSONObject(position).optString("verification_code");
                }

                if(jsonArray.optJSONObject(position).optString("static_map") != null){
                    static_map = jsonArray.optJSONObject(position).optString("static_map");
                }


                // getting firstname
                try {
                    JSONObject providerObj = jsonArray.getJSONObject(position).optJSONObject("provider");
                    if (providerObj != null) {

                        first_name  = providerObj.optString("first_name");
                        rating  = providerObj.optString("rating");
                        avatar = providerObj.optString("avatar");

//                        Picasso.get().load(URLHelper.BASE + "storage/app/public/" + providerObj.optString("avatar"))
//                                .placeholder(R.drawable.car_select).error(R.drawable.car_select).into(holder.driver_image);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


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

                startActivity(intent);
                return true;
            });

        }

        @Override
        public int getItemCount() {
            return jsonArray.length();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView tripTime, car_name, name, rateVal;
            TextView tripDate, tripAmount, tripId, txtSource, txtDestination, status;
            ImageView tripImg, driver_image;
            Button btnCancel, btnStart;

            RatingBar listitemrating;

            LinearLayout pContainer;

            public MyViewHolder(View itemView) {
                super(itemView);
                tripDate = itemView.findViewById(R.id.tripDate);
                tripTime = itemView.findViewById(R.id.tripTime);
                tripAmount = itemView.findViewById(R.id.tripAmount);
                tripImg = itemView.findViewById(R.id.tripImg);
                car_name = itemView.findViewById(R.id.car_name);
                driver_image = itemView.findViewById(R.id.driver_image);
                btnCancel = itemView.findViewById(R.id.btnCancel);
                btnStart = itemView.findViewById(R.id.btnStart);
                tripId = itemView.findViewById(R.id.tripid);
                txtDestination = itemView.findViewById(R.id.txtDestination);
                txtSource = itemView.findViewById(R.id.txtSource);
                status = itemView.findViewById(R.id.status);
                listitemrating = itemView.findViewById(R.id.listitemrating);
                name = itemView.findViewById(R.id.name);
                rateVal = itemView.findViewById(R.id.rateVal);
                pContainer = itemView.findViewById(R.id.pContainer);

//                itemView.setOnClickListener(view -> {
//                    Intent intent = new Intent(getActivity(), RideRequest.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    Toast.makeText(getContext(), ""+request_id, Toast.LENGTH_SHORT).show();
//                    intent.putExtra("noofseat","2");
//                    intent.putExtra("request_id",request_id);
//                    startActivity(intent);
//
//                });

//                itemView.setOnLongClickListener(view -> {
//                    Intent intent = new Intent(getActivity(), HistoryDetails.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    Log.e("Intent", "" + jsonArray.optJSONObject(getAdapterPosition()).toString());
//                    intent.putExtra("post_value", jsonArray.optJSONObject(getAdapterPosition()).toString());
//                    intent.putExtra("tag", "upcoming_trips");
//                    startActivity(intent);
//                    return true;
//                });


            }
        }
    }
}
