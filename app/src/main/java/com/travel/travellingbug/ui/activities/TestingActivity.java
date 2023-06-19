package com.travel.travellingbug.ui.activities;

import static com.travel.travellingbug.ClassLuxApp.getContext;
import static com.travel.travellingbug.ui.activities.DocUploadActivity.TAG;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.travel.travellingbug.ClassLuxApp;
import com.travel.travellingbug.R;
import com.travel.travellingbug.helper.SharedHelper;
import com.travel.travellingbug.helper.URLHelper;
import com.travel.travellingbug.models.FRDModel;
import com.travel.travellingbug.models.StopOverModel;
import com.travel.travellingbug.ui.adapters.StepOverAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TestingActivity extends AppCompatActivity {

    ArrayList<FRDModel> list;
    RecyclerView recyclerViewPreference;

    StepOverAdapter stepOverAdapter;
    ArrayList<StopOverModel> stopOverModelArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);

        recyclerViewPreference = findViewById(R.id.recyclerViewPreference);

        list = new ArrayList<>();

//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
//        FRDAdapter verifyIdMainActivityAdapter = new FRDAdapter(getContext(), list);
//        recyclerViewPreference.setLayoutManager(linearLayoutManager);
//        recyclerViewPreference.setAdapter(verifyIdMainActivityAdapter);
//        recyclerViewPreference.setNestedScrollingEnabled(false);
        getPreferencesTitle();




    }

    public void getPreferencesTitle() {

            JSONObject object = new JSONObject();
            JSONArray jsonArray = new JSONArray();
//            list = new ArrayList<>();

        stopOverModelArrayList = new ArrayList<>();


            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URLHelper.PREFERENCES_TITLE,
                    jsonArray, response -> {
                Log.v("GetPreferencesTitle", response.toString());

                if (response.length() > 0) {

                    SharedHelper.putKey(getApplicationContext(),"TravelPreferenceStatus", String.valueOf(response.length()));

                    for (int i = 0; i < response.length(); i++) {
                        try {

                            Log.v("jsonObjectLength : ", String.valueOf(response.length()));
                            Log.v("jsonObjectLength : ", String.valueOf(jsonArray.length()));

                            JSONObject jsonObjectPreference = response.getJSONObject(i);
                            Log.v("jsonObjectPreference : ", jsonObjectPreference.toString());
                            Log.v("jsonObjectPreference : ", jsonObjectPreference.optString("title"));

//                            if(!jsonObjectPreference.optString("title").equalsIgnoreCase("null") || !jsonObjectPreference.optString("title").equalsIgnoreCase("") || !jsonObjectPreference.optString("title").equalsIgnoreCase(null)){
//                                titles = jsonObjectPreference.optString("title");
//                            }

//                            list.add(new FRDModel(jsonObjectPreference.optString("title")));

                            stopOverModelArrayList.add(new StopOverModel(jsonObjectPreference.optString("title")));


                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

//                    VerifyIdMainActivityAdapter verifyIdMainActivityAdapter = new VerifyIdMainActivityAdapter(getApplicationContext(), list);
//                    FRDAdapter frdAdapter = new FRDAdapter(getContext(), list);
//                    recyclerViewPreference.setAdapter(frdAdapter);




                    stepOverAdapter = new StepOverAdapter(getContext(), stopOverModelArrayList);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
                    recyclerViewPreference.setLayoutManager(linearLayoutManager);
                    recyclerViewPreference.setAdapter(stepOverAdapter);
                    recyclerViewPreference.setNestedScrollingEnabled(false);



                }

            }, error -> {
//                displayMessage(getString(R.string.something_went_wrong));
                Toast.makeText(this, "Error:", Toast.LENGTH_SHORT).show();
            }) {
                @Override
                public Map<String, String> getHeaders() {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("X-Requested-With", "XMLHttpRequest");
                    Log.e(TAG, "getHeaders: Token" + SharedHelper.getKey(getApplicationContext(), "access_token") + SharedHelper.getKey(getContext(), "token_type"));
                    headers.put("Authorization", "" + "Bearer" + " " + SharedHelper.getKey(getApplicationContext(), "access_token"));
                    return headers;
                }
            };

            ClassLuxApp.getInstance().addToRequestQueue(jsonArrayRequest);




    }
}