package com.travel.travellingbug.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.travel.travellingbug.ClassLuxApp;
import com.travel.travellingbug.R;
import com.travel.travellingbug.helper.ConnectionHelper;
import com.travel.travellingbug.helper.CustomDialog;
import com.travel.travellingbug.helper.SharedHelper;
import com.travel.travellingbug.helper.URLHelper;
import com.travel.travellingbug.models.VehicelModel;
import com.travel.travellingbug.ui.adapters.VehicelAdapter;
import com.travel.travellingbug.ui.adapters.VehicleClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class VehicleDetailsColorActivity extends AppCompatActivity {


    String license_number = "";
    String service_name = "";
    String service_model = "";
    String service_type = "";
    String service_color = "";


    VehicleClickListener vehicleClickListener;
    CustomDialog customDialog;

    RecyclerView travelPreferenceRV;
    ArrayList<VehicelModel> list = new ArrayList<>();

    ConnectionHelper helper;
    Boolean isInternet;
    ImageView backArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_details_color);


        license_number = getIntent().getStringExtra("license_number");
        service_name = getIntent().getStringExtra("service_name");
        service_model = getIntent().getStringExtra("service_model");
        service_type = getIntent().getStringExtra("service_type");

        service_color = SharedHelper.getKey(getApplicationContext(), "service_color");

        initComponent();
        clickHandlerOnComponent();
        getVehicle();
        settingAdapterForRecycleView();
    }

    private void clickHandlerOnComponent() {




    }

    private void initComponent() {


        travelPreferenceRV = findViewById(R.id.vehicleTypeRV);
        helper = new ConnectionHelper(getApplicationContext());
        isInternet = helper.isConnectingToInternet();
        backArrow = findViewById(R.id.backArrow);

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void settingAdapterForRecycleView() {


        // adding data to the list



        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        VehicelAdapter vehicelAdapter = new VehicelAdapter(getApplicationContext(), list);
        travelPreferenceRV.setLayoutManager(linearLayoutManager);
        travelPreferenceRV.setAdapter(vehicelAdapter);
        travelPreferenceRV.setNestedScrollingEnabled(false);


    }

    public void getVehicle() {



        customDialog = new CustomDialog(this);
        customDialog.setCancelable(false);
        customDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, URLHelper.VEHICEL_UPDATE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                customDialog.dismiss();
                customDialog.cancel();

                System.out.println("size : " + response.length());
                System.out.println("vehicel data : " + response);

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    System.out.println("size : " + jsonArray.length());
                    System.out.println("data : " + jsonArray);


                    if (response.length() > 0) {
                        System.out.println("data : " + jsonArray.getString(0));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            System.out.println("data : " + jsonObject.toString());

                            list.add(new VehicelModel(jsonObject.optString("id"),jsonObject.optString("attributevalues")));

                        }

                        VehicelAdapter vehicelAdapter = new VehicelAdapter(getApplicationContext(), list,vehicleClickListener);
                        travelPreferenceRV.setAdapter(vehicelAdapter);
                        if (customDialog != null)
                            customDialog.dismiss();
                    }

                } catch (JSONException e) {

                    displayMessage(e.toString());
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(VehicleDetailsColorActivity.this, "Error Found", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
                customDialog.dismiss();
                customDialog.cancel();
            }

        }) {


            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("attributetype", "color");
                System.out.println("PARAMS : "+params.toString());

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


        vehicleClickListener = new VehicleClickListener() {
            @Override
            public void onClick(int position, VehicelModel vehicelModel) {
                service_color = vehicelModel.getVal();
                Intent intent = new Intent(VehicleDetailsColorActivity.this, VehicleDetailsMenufactureActivity.class);
                intent.putExtra("license_number", license_number);
                intent.putExtra("service_name", service_name);
                intent.putExtra("service_model", service_model);
                intent.putExtra("service_type", service_type);
                intent.putExtra("service_color", service_color);
                startActivity(intent);

            }
        };







    }

    public void displayMessage(String toastString) {
        Toasty.info(getApplicationContext(), toastString, Toast.LENGTH_SHORT, true).show();
    }
}