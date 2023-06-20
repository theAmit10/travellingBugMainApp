package com.travel.travellingbug.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.travel.travellingbug.R;
import com.travel.travellingbug.models.StopOverModel;

import java.util.ArrayList;



public class StepOverOrgAdapter extends RecyclerView.Adapter<StepOverOrgAdapter.ViewHolder> {

    Context context;

    String TAG = "STEPOVERADAPTER";
    ArrayList<StopOverModel> list;

    public StepOverOrgAdapter(Context context, ArrayList<StopOverModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.design_travel_preference_find_ride_detials,parent,false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        StopOverModel stopOverModel = list.get(position);
        holder.location.setText(stopOverModel.getArea());

//        JSONArray jsonArray = new JSONArray();
//        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URLHelper.PREFERENCES,
//                jsonArray, response -> {
//            Log.v("GetPreferences", response.toString());
//
//            if (response.length() > 0) {
//
//                for (int i = 0; i < response.length(); i++) {
//                    try {
//
//                        JSONObject jsonObjectPreference = response.getJSONObject(i);
//                        Log.v("jsonObjectPreference : ", jsonObjectPreference.optString("title"));
//
//
//
////                        jsonObjectPreference.optString("title");
//
////                        if(jsonObjectPreference.optString("title") != null){
////                            if(stopOverModel.getArea().equalsIgnoreCase(jsonObjectPreference.optString("title"))){
////                                holder.location.setChecked(true);
////                            }
////                        }
//
//
//
//                    } catch (JSONException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//
//
//
//            }
//
//
//        }, error -> {
//            System.out.println("error");
//        }) {
//            @Override
//            public Map<String, String> getHeaders() {
//                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("X-Requested-With", "XMLHttpRequest");
//                Log.e(TAG, "getHeaders: Token" + SharedHelper.getKey(getContext(), "access_token") + SharedHelper.getKey(getContext(), "token_type"));
//                headers.put("Authorization", "" + "Bearer" + " " + SharedHelper.getKey(getContext(), "access_token"));
//                return headers;
//            }
//        };
//
//        ClassLuxApp.getInstance().addToRequestQueue(jsonArrayRequest);



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView location;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            location = itemView.findViewById(R.id.location);
        }
    }
}

