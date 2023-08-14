package com.travel.travellingbug.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

public class VehicleDetailsBrandActivity extends AppCompatActivity {

    FloatingActionButton floatingActionButton;
    String license_number = "";
    String service_name = "";

    EditText vehicleBrandETL;
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
        setContentView(R.layout.activity_vehicle_details_brand);

        license_number = getIntent().getStringExtra("license_number");

        initComponent();
        clickHandlerOnComponent();

        vehicleBrandETL.setText(SharedHelper.getKey(getApplicationContext(), "service_name"));
        int textLength = vehicleBrandETL.getText().length();
        vehicleBrandETL.setSelection(textLength);
        service_name = SharedHelper.getKey(getApplicationContext(), "service_name");

        vehicleBrandETL.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                service_name = vehicleBrandETL.getText().toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                service_name = vehicleBrandETL.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        getVehicle();
        settingAdapterForRecycleView();


    }

    private void clickHandlerOnComponent() {
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(service_name.equalsIgnoreCase("") || service_name.length() == 0){
                    Toast.makeText(VehicleDetailsBrandActivity.this, "Enter Vehicle Brand", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(VehicleDetailsBrandActivity.this, VehicleDetailsModelActivity.class);
                    intent.putExtra("license_number", license_number);
                    intent.putExtra("service_name", service_name);
                    startActivity(intent);
                }

            }
        });
    }

    private void initComponent() {
        floatingActionButton = findViewById(R.id.floatingActionButton);
        vehicleBrandETL = findViewById(R.id.vehicleBrandETL);


        travelPreferenceRV = findViewById(R.id.vehicleBrandRV);

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

//        list = new ArrayList<>();
//
//        list.add(new VerifyIdMainActivityModel("Chattiness","I'm Chatty when i feel confortable "));
//        list.add(new VerifyIdMainActivityModel("Music","I'll jam dependent on mood"));
//        list.add(new VerifyIdMainActivityModel("Smoking","Cigarette break outside the car are ok."));
//        list.add(new VerifyIdMainActivityModel("Pet's","I'll travel with pets depending on the animal"));


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
                Toast.makeText(VehicleDetailsBrandActivity.this, "Error Found", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
                customDialog.dismiss();
                customDialog.cancel();
            }

        }) {


            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("attributetype", "brand");
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
                vehicleBrandETL.setText(vehicelModel.getVal());
            }
        };







    }

    public void displayMessage(String toastString) {
        Toasty.info(getApplicationContext(), toastString, Toast.LENGTH_SHORT, true).show();
    }
}