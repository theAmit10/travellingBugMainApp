package com.travel.travellingbug.ui.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.JsonArrayRequest;
import com.squareup.picasso.Picasso;
import com.travel.travellingbug.ClassLuxApp;
import com.travel.travellingbug.R;
import com.travel.travellingbug.chat.UserChatActivity;
import com.travel.travellingbug.helper.ConnectionHelper;
import com.travel.travellingbug.helper.CustomDialog;
import com.travel.travellingbug.helper.SharedHelper;
import com.travel.travellingbug.helper.URLHelper;
import com.travel.travellingbug.ui.activities.SplashScreen;
import com.travel.travellingbug.ui.activities.TrackActivity;

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

public class InboxFragment extends Fragment {

    Boolean isInternet;
    InboxFragment.PostAdapter postAdapter;
    RecyclerView recyclerView;
    RelativeLayout errorLayout;
    ConnectionHelper helper;
    CustomDialog customDialog;
    View rootView;

    ImageView backImg;
    LinearLayout toolbar;

    String providerFirstName = "";
    String providerId = "";



    public InboxFragment() {
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
        rootView = inflater.inflate(R.layout.fragment_inbox, container, false);
        findViewByIdAndInitialize();

        if (isInternet) {
            getHistoryList();
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

    public void getHistoryList() {

        customDialog = new CustomDialog(getActivity());
        customDialog.setCancelable(false);
        customDialog.show();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URLHelper.GET_ALL_RIDES, response -> {

            if (response != null) {
                postAdapter = new InboxFragment.PostAdapter(response);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()) {
                    @Override
                    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
                        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT);
                    }
                });
                if (postAdapter != null && postAdapter.getItemCount() > 0) {
                    errorLayout.setVisibility(View.GONE);
                    recyclerView.setAdapter(postAdapter);
                } else {
                    errorLayout.setVisibility(View.VISIBLE);
                }

            } else {
                errorLayout.setVisibility(View.VISIBLE);
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
    public void onDetach() {
        super.onDetach();
    }

    public void displayMessage(String toastString) {
        Toasty.info(getActivity(), toastString, Toast.LENGTH_SHORT, true).show();
    }

    public void GoToBeginActivity() {
        SharedHelper.putKey(getActivity(), "loggedIn", getString(R.string.False));
        Intent mainIntent = new Intent(getActivity(), SplashScreen.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainIntent);
        getActivity().finish();
    }

    private String getMonth(String date) throws ParseException {
        Date d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        String monthName = new SimpleDateFormat("M").format(cal.getTime());
        String name = getMonthName(Integer.parseInt(monthName));


        return name;
    }

    public String getMonthName(int month) {
        switch (month) {
            case 1:
                return "Jan";
            case 2:
                return "Feb";
            case 3:
                return "Mar";

            case 4:
                return "April";

            case 5:
                return "May";

            case 6:
                return "June";

            case 7:
                return "July";

            case 8:
                return "Aug";

            case 9:
                return "Sep";

            case 10:
                return "Oct";

            case 11:
                return "Nov";

            case 12:
                return "Dec";

        }
        return "";
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

    private class PostAdapter extends RecyclerView.Adapter<InboxFragment.PostAdapter.MyViewHolder> {
        JSONArray jsonArray;

        public PostAdapter(JSONArray array) {
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
        public InboxFragment.PostAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.design_inbox_fragment, parent, false);
            return new InboxFragment.PostAdapter.MyViewHolder(itemView);
        }


        @Override
        public void onBindViewHolder(InboxFragment.PostAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            try {
                JSONObject jsonObjectTrip = jsonArray.getJSONObject(position).optJSONObject("trip");
                holder.txtSource.setText(jsonObjectTrip.optString("s_address"));
                holder.txtDestination.setText(jsonObjectTrip.optString("d_address"));
                JSONObject providerJsonObject = jsonObjectTrip.getJSONObject("provider");
                try {
                    holder.userName.setText(providerJsonObject.optString("first_name"));
                    Picasso.get().load(URLHelper.BASE + "storage/app/public/" + jsonArray.getJSONObject(position).optJSONObject("trip").optJSONObject("provider").optString("avatar"))
                            .placeholder(R.drawable.ic_dummy_user).error(R.drawable.ic_dummy_user).into(holder.profileImgeIv);

                    providerId = providerJsonObject.optString("provider_id");

                    providerJsonObject.optJSONObject("provider").optString("first_name");


                } catch (Exception e) {
                    e.printStackTrace();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            holder.historyContainerLL.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Intent intent = new Intent(getContext(), TrackActivity.class);
                    intent.putExtra("flowValue", 3);
                    intent.putExtra("request_id_from_trip", jsonArray.optJSONObject(position).optString("request_id"));
                    startActivity(intent);

                    return true;
                }
            });


            holder.historyContainerLL.setOnClickListener(view -> {

//                Intent intent = new Intent(getActivity(), HistoryDetailsUser.class);
                Intent intent = new Intent(getContext(), UserChatActivity.class);
//                intent.putExtra("request_id", jsonArray.optJSONObject(position).optString("request_id"));
//                intent.putExtra("user_id", jsonArray.optJSONObject(position).optString("user_id"));
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                Log.e("Intent", "" + jsonArray.optJSONObject(position).toString());
//                intent.putExtra("post_value", jsonArray.optJSONObject(position).toString());
//                intent.putExtra("tag", "past_trips");
                // getting firstname
                try {
                    JSONObject providerObj = jsonArray.getJSONObject(position).optJSONObject("provider");
                    if (providerObj != null) {

                        providerFirstName = providerObj.optString("first_name");



                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                intent.putExtra("requestId", jsonArray.optJSONObject(position).optString("request_id"));
                intent.putExtra("providerId", jsonArray.optJSONObject(position).optJSONObject("trip").optJSONObject("provider").optString("id"));
                intent.putExtra("userId", jsonArray.optJSONObject(position).optString("user_id"));
                intent.putExtra("userName", jsonArray.optJSONObject(position).optJSONObject("trip").optJSONObject("provider").optString("first_name"));

                intent.putExtra("messageType", "pu");
                System.out.println("rid : "+jsonArray.optJSONObject(position).optString("request_id"));
                System.out.println("rproviderId : "+jsonArray.optJSONObject(position).optJSONObject("trip").optJSONObject("provider").optString("id"));
                System.out.println("ruserId : "+jsonArray.optJSONObject(position).optString("user_id"));
                System.out.println("ruserName : "+jsonArray.optJSONObject(position).optJSONObject("trip").optJSONObject("provider").optString("first_name"));

                startActivity(intent);

//                request_id = jsonArray.optJSONObject(position).optString("id");
//
//                s_address = jsonArray.optJSONObject(position).optString("s_address");
//
//                d_address = jsonArray.optJSONObject(position).optString("d_address");
//
//                booking_id = jsonArray.optJSONObject(position).optString("booking_id");
//
//                status = jsonArray.optJSONObject(position).optString("status");
//
//                if(jsonArray.optJSONObject(position).optString("payment_mode") != null){
//                    payment_mode = jsonArray.optJSONObject(position).optString("payment_mode");
//                }
//
//                if(jsonArray.optJSONObject(position).optString("estimated_fare") != null){
//                    estimated_fare = jsonArray.optJSONObject(position).optString("estimated_fare");
//
//                }
//
//                if(jsonArray.optJSONObject(position).optString("verification_code") != null){
//                    verification_code = jsonArray.optJSONObject(position).optString("verification_code");
//                }else{
//                    verification_code = "0000";
//                }
//
//                if(jsonArray.optJSONObject(position).optString("static_map") != null){
//                    static_map = jsonArray.optJSONObject(position).optString("static_map");
//                }
//
//

//
//                intent.putExtra("request_id", request_id);
//                intent.putExtra("s_address", s_address);
//                intent.putExtra("d_address", d_address);
//                intent.putExtra("booking_id", booking_id);
//                intent.putExtra("s_date", s_date);
//                intent.putExtra("s_time", s_time);
//                intent.putExtra("status", status);
//                intent.putExtra("payment_mode", payment_mode);
//                intent.putExtra("estimated_fare", estimated_fare);
//                intent.putExtra("verification_code", verification_code);
//                intent.putExtra("static_map", static_map);
//                intent.putExtra("first_name", userName);
//                intent.putExtra("rating", rating);
//                intent.putExtra("avatar", userProfileImage);

                startActivity(intent);

            });

        }

        @Override
        public int getItemCount() {
            return jsonArray.length();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView txtSource, txtDestination, userName;
            ImageView profileImgeIv;
            Button rateRider;
            LinearLayout historyContainerLL;

            public MyViewHolder(View itemView) {
                super(itemView);

                txtSource = itemView.findViewById(R.id.txtSource);
                txtDestination = itemView.findViewById(R.id.txtDestination);
                rateRider = itemView.findViewById(R.id.rateRider);
                userName = itemView.findViewById(R.id.userName);
                profileImgeIv = itemView.findViewById(R.id.profileImgeIv);
                historyContainerLL = itemView.findViewById(R.id.historyContainerLL);
            }
        }
    }
}