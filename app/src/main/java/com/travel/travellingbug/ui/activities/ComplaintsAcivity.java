package com.travel.travellingbug.ui.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.travel.travellingbug.ClassLuxApp;
import com.travel.travellingbug.R;
import com.travel.travellingbug.helper.ConnectionHelper;
import com.travel.travellingbug.helper.CustomDialog;
import com.travel.travellingbug.helper.SharedHelper;
import com.travel.travellingbug.helper.URLHelper;
import com.travel.travellingbug.utills.Utilities;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class ComplaintsAcivity extends AppCompatActivity {

    private static final String[] HEAR_OPTIONS = {
            "Driver Late",
            "Driver Unprofessional",
            "I Had Different Issue",
            "Driver Changed",
    };

    ImageView ivBack;
    TextView tvSubmit;
    MaterialSpinner spRegister;
    EditText etComplaint;
    String complaintName = "Driver Late";

    Utilities utils = new Utilities();
    CustomDialog customDialog;
    Boolean isInternet;
    ConnectionHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SharedHelper.getKey(this, "selectedlanguage").contains("ar")) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }


        setContentView(R.layout.activity_complaints);
        helper = new ConnectionHelper(ComplaintsAcivity.this);
        isInternet = helper.isConnectingToInternet();
        ivBack = findViewById(R.id.ivBack);
        tvSubmit = findViewById(R.id.tvSubmit);
        spRegister = findViewById(R.id.spRegister);
        etComplaint = findViewById(R.id.etComplaint);
        ivBack.setOnClickListener(view -> onBackPressed());

        spRegister.setItems(HEAR_OPTIONS);
        spRegister.setOnItemSelectedListener((MaterialSpinner.OnItemSelectedListener<String>)
                (view, position, id, item) -> {
                    complaintName = item;
                    Log.d("complaintName", complaintName);
                });
        Log.d("complaintName1", complaintName);

        spRegister.setOnNothingSelectedListener(spinner -> {

        });

        tvSubmit.setOnClickListener(view -> {
            if (etComplaint.getText().equals("")) {
                displayMessage(getString(R.string.complaint_not_empty));
            } else {
                submitComplaint();
            }
        });
    }

    public void displayMessage(String toastString) {
        Toasty.info(this, toastString, Toast.LENGTH_SHORT, true).show();
    }

    private void submitComplaint() {
        if (isInternet) {
            customDialog = new CustomDialog(ComplaintsAcivity.this);
            if (customDialog != null)
                customDialog.show();

            String issue = etComplaint.getText().toString();
            String issueType = complaintName;
            String id = SharedHelper.getKey(ComplaintsAcivity.this, "id");

            JSONObject object = new JSONObject();
            try {
                object.put("user_id", id);
                object.put("description", issue);
                object.put("complaint_type", issueType);
                utils.print("input to complaint", "" + object);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST,
                    URLHelper.COMPLAINT, object, response -> {
                if ((customDialog != null) && customDialog.isShowing())
                    customDialog.dismiss();
                utils.print("ComplaintResponse", response.toString());
                displayMessage(getString(R.string.complaint_submitted));
                onBackPressed();

            }, error -> {

            }) {
                @Override
                public Map<String, String> getHeaders() {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("X-Requested-With", "XMLHttpRequest");
                    return headers;
                }
            };

            ClassLuxApp.getInstance().addToRequestQueue(objectRequest);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
