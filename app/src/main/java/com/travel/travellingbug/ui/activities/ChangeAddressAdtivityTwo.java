package com.travel.travellingbug.ui.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.travel.travellingbug.ClassLuxApp;
import com.travel.travellingbug.R;
import com.travel.travellingbug.helper.SharedHelper;
import com.travel.travellingbug.helper.URLHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChangeAddressAdtivityTwo extends AppCompatActivity {

    EditText fullAdressETL;
    FloatingActionButton floatingActionButton;
    String full_address = "";

    ImageView imageView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_address_adtivity_two);


        initComponent();
        clickHandlerOnComponent();


    }

    private void updateAddress() {

        StringRequest request = new StringRequest(Request.Method.POST, URLHelper.UPDATE_POSTAL_ADDRESS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                System.out.println("size : " + response.length());
                System.out.println("Request Data : " + response);
                String location;


                try {

                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {

//                        JSONObject jsonObject = new JSONObject(response);
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        if (response != null) {

                            JSONObject profileJsonObj = jsonObject.optJSONObject("profile");

                            try {
                                Toast.makeText(ChangeAddressAdtivityTwo.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                                SharedHelper.putKey(getApplicationContext(),"full_address",full_address);
                                finish();
                            }catch (Exception e){
                                e.printStackTrace();
                            }



//                            showAddressUpdatedDialog();



                        } else {
                            Toast.makeText(ChangeAddressAdtivityTwo.this, "No Request Available", Toast.LENGTH_SHORT).show();
                        }


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ChangeAddressAdtivityTwo.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }

        }) {


            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("address", full_address);
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

    private void showAddressUpdatedDialog() {


        Dialog confirmDialog = new Dialog(getApplicationContext());
        confirmDialog.setContentView(R.layout.schedule_dialog);

        TextView tvDone = confirmDialog.findViewById(R.id.tvDone);
        TextView bookingStatusTitleTv = confirmDialog.findViewById(R.id.bookingStatusTitleTv);
        TextView bookingStatusSubTitleTv = confirmDialog.findViewById(R.id.bookingStatusSubTitleTv);
        TextView tvDriverMsg = confirmDialog.findViewById(R.id.tvDriverMsg);

        bookingStatusTitleTv.setText("Address Updated");

        bookingStatusSubTitleTv.setText("Your address has been successfully updated ");

        tvDriverMsg.setText("");

        confirmDialog.show();
        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog.dismiss();
                startActivity(new Intent(ChangeAddressAdtivityTwo.this, HomeScreenActivity.class));

            }
        });
    }

    private void clickHandlerOnComponent() {


        full_address = fullAdressETL.getText().toString();

        fullAdressETL.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                full_address = fullAdressETL.getText().toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                full_address = fullAdressETL.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(full_address.length() > 0){
                    Toast.makeText(ChangeAddressAdtivityTwo.this, "Processing ", Toast.LENGTH_SHORT).show();
                    updateAddress();
                }else {
                    Toast.makeText(ChangeAddressAdtivityTwo.this, "Enter Address", Toast.LENGTH_SHORT).show();
                }

//                Intent intent = new Intent(ChangeAddressAdtivityTwo.this, VehicleDetailsBrandActivity.class);
//                intent.putExtra("license_number", license_number);
//                startActivity(intent);

            }
        });


        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initComponent() {
        floatingActionButton = findViewById(R.id.floatingActionButton);
        fullAdressETL = findViewById(R.id.fullAdressETL);
        imageView3 = findViewById(R.id.imageView3);

    }
}