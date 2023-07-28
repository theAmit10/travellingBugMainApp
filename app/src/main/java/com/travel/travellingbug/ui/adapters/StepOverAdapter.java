package com.travel.travellingbug.ui.adapters;

import static com.travel.travellingbug.ClassLuxApp.getContext;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.travel.travellingbug.ClassLuxApp;
import com.travel.travellingbug.R;
import com.travel.travellingbug.helper.SharedHelper;
import com.travel.travellingbug.helper.URLHelper;
import com.travel.travellingbug.models.StopOverModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StepOverAdapter extends RecyclerView.Adapter<StepOverAdapter.ViewHolder> {

    Context context;

    String TAG = "STEPOVERADAPTER";
    ArrayList<StopOverModel> list;

    String user_id;


    public StepOverAdapter(Context context, ArrayList<StopOverModel> list) {
        this.context = context;
        this.list = list;
    }

    public StepOverAdapter(Context context, ArrayList<StopOverModel> list, String user_id) {
        this.context = context;
        this.list = list;
        this.user_id = user_id;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.design_stopover_rv,parent,false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        StopOverModel stopOverModel = list.get(position);
        holder.location.setText(stopOverModel.getArea());

//        String user_id = getContext();

        JSONArray jsonArray = new JSONArray();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URLHelper.PREFERENCES+"?user_id="+user_id,
                jsonArray, response -> {
            Log.v("GetPreferences", response.toString());
            if (response.length() > 0) {
                for (int i = 0; i < response.length(); i++) {
                    try {

                        JSONObject jsonObjectPreference = response.getJSONObject(i);
                        Log.v("jsonObjectPreference : ", jsonObjectPreference.optString("title"));

//                        jsonObjectPreference.optString("title");

                        if(jsonObjectPreference.optString("title") != null){
                            if(stopOverModel.getArea().equalsIgnoreCase(jsonObjectPreference.optString("title"))){
                                holder.location.setChecked(true);
                            }
                        }



                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }



            }


        }, error -> {
            System.out.println("error");
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("X-Requested-With", "XMLHttpRequest");
                Log.e(TAG, "getHeaders: Token" + SharedHelper.getKey(getContext(), "access_token") + SharedHelper.getKey(getContext(), "token_type"));
                headers.put("Authorization", "" + "Bearer" + " " + SharedHelper.getKey(getContext(), "access_token"));
                return headers;
            }
        };

        ClassLuxApp.getInstance().addToRequestQueue(jsonArrayRequest);



//        if(stopOverModel.getLocation().equalsIgnoreCase("Music")){
//            holder.location.setChecked(true);
//        }

    }

//    public void getPreference() {
//
//            JSONArray jsonArray = new JSONArray();
//            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URLHelper.PREFERENCES,
//                    jsonArray, response -> {
//                Log.v("GetPreferences", response.toString());
//
//                if (response.length() > 0) {
//
//                    for (int i = 0; i < response.length(); i++) {
//                        try {
//
//
//                            JSONObject jsonObjectPreference = response.getJSONObject(i);
//                            Log.v("jsonObjectPreference : ", jsonObjectPreference.toString());
//                            Log.v("jsonObjectPreference : ", jsonObjectPreference.optString("title"));
//                            Log.v("jsonObjectPreference : ", jsonObjectPreference.optString("subtitle"));
//
//
//                            titles = jsonObjectPreference.optString("title");
//
//
//
//                        } catch (JSONException e) {
//                            throw new RuntimeException(e);
//                        }
//                    }
//
//
//
//                }
//
//
//            }, error -> {
//                System.out.println("error");
//            }) {
//                @Override
//                public Map<String, String> getHeaders() {
//                    HashMap<String, String> headers = new HashMap<String, String>();
//                    headers.put("X-Requested-With", "XMLHttpRequest");
//                    Log.e(TAG, "getHeaders: Token" + SharedHelper.getKey(getContext(), "access_token") + SharedHelper.getKey(getContext(), "token_type"));
//                    headers.put("Authorization", "" + "Bearer" + " " + SharedHelper.getKey(getContext(), "access_token"));
//                    return headers;
//                }
//            };
//
//            ClassLuxApp.getInstance().addToRequestQueue(jsonArrayRequest);
//
//
//
//
//    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CheckBox location;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            location = itemView.findViewById(R.id.location);
        }
    }
}
