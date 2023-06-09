package com.travel.travellingbug.ui.fragments;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.travel.travellingbug.ClassLuxApp;
import com.travel.travellingbug.R;
import com.travel.travellingbug.ui.activities.MainActivity;
import com.travel.travellingbug.ui.activities.SplashScreen;
import com.travel.travellingbug.helper.CustomDialog;
import com.travel.travellingbug.helper.SharedHelper;
import com.travel.travellingbug.helper.URLHelper;

import org.json.JSONObject;

import java.util.HashMap;

import es.dmoral.toasty.Toasty;

public class Help extends Fragment implements View.OnClickListener {

    ImageView backImg;
    ImageView phoneImg;
    ImageView webImg;
    ImageView mailImg;

    String phone, email;

    public Help() {
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
        View view = inflater.inflate(R.layout.fragment_help, container, false);
        findViewByIdAndInitialize(view);
        setClickListeners();
        getHelp();
        return view;
    }

    private void setClickListeners() {
        backImg.setOnClickListener(this);
        mailImg.setOnClickListener(this);
        webImg.setOnClickListener(this);
        phoneImg.setOnClickListener(this);
    }

    public void findViewByIdAndInitialize(View view) {
        backImg = view.findViewById(R.id.backArrow);
        phoneImg = view.findViewById(R.id.img_phone);
        webImg = view.findViewById(R.id.img_web);
        mailImg = view.findViewById(R.id.img_mail);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backArrow:
                startActivity(new Intent(getActivity(), MainActivity.class));
//                getFragmentManager().popBackStack();
                break;
            case R.id.img_mail:
                String to = email;
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/html");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name) + "-" + getString(R.string.help));
                intent.putExtra(Intent.EXTRA_TEXT, "Hello team");
                startActivity(Intent.createChooser(intent, "Send Email"));
                break;
            case R.id.img_phone:
                if (phone != null && !phone.equalsIgnoreCase("null") && !phone.equalsIgnoreCase("") && phone.length() > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 1);
                    } else {
                        Intent intentCall = new Intent(Intent.ACTION_CALL);
                        intentCall.setData(Uri.parse("tel:" + phone));
                        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        startActivity(intentCall);
                    }
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(getString(R.string.app_name))
                            .setIcon(R.mipmap.ic_launcher)
                            .setMessage(getString(R.string.sorry_for_inconvinent))
                            .setCancelable(false)
                            .setPositiveButton("ok",
                                    (dialog, id) -> dialog.dismiss());
                    AlertDialog alert1 = builder.create();
                    alert1.show();
                }

                break;
            case R.id.img_web:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(URLHelper.HELP_URL));
                startActivity(browserIntent);
                break;
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    //Toast.makeText(SignInActivity.this, "PERMISSION_GRANTED", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + phone));
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    startActivity(intent);
                } else {
                    requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 1);
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void getHelp() {
        final CustomDialog customDialog = new CustomDialog(getActivity());
        customDialog.setCancelable(false);
        customDialog.show();
        JSONObject object = new JSONObject();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URLHelper.HELP, object, response -> {
            customDialog.dismiss();
            phone = response.optString("contact_number");
            email = response.optString("contact_email");
        }, error -> {
            customDialog.dismiss();
            displayMessage(getString(R.string.something_went_wrong));
        }) {
            @Override
            public java.util.Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("X-Requested-With", "XMLHttpRequest");
                headers.put("Authorization", "Bearer " + SharedHelper.getKey(getContext(), "access_token"));
                Log.e("", "Access_Token" + SharedHelper.getKey(getContext(), "access_token"));
                return headers;
            }
        };
        ClassLuxApp.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    public void displayMessage(String toastString) {
        Toasty.info(getActivity(), toastString, Toast.LENGTH_SHORT, true).show();
    }

    public void GoToBeginActivity() {
        SharedHelper.putKey(getContext(), "loggedIn", getString(R.string.False));
        Intent mainIntent = new Intent(getContext(), SplashScreen.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainIntent);
        getActivity().finish();
    }

}
